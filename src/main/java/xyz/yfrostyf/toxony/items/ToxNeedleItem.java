package xyz.yfrostyf.toxony.items;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import xyz.yfrostyf.toxony.api.tox.ToxData;
import xyz.yfrostyf.toxony.api.util.VialUtil;
import xyz.yfrostyf.toxony.damages.NeedleDamageSource;
import xyz.yfrostyf.toxony.registries.DataAttachmentRegistry;
import xyz.yfrostyf.toxony.registries.DataComponentsRegistry;
import xyz.yfrostyf.toxony.registries.ItemRegistry;

import java.util.List;

public class ToxNeedleItem extends Item {
    private final float HEALTH_THRESHOLD_PERCENT = 0.1F;

    public ToxNeedleItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack otherStack = player.getMainHandItem().is(this) ? player.getOffhandItem() : player.getMainHandItem();
        ItemStack thisStack = player.getItemInHand(usedHand);

        if (thisStack.has(DataComponentsRegistry.AFFINITY_STORED_ITEM) || thisStack.has(DataComponents.POTION_CONTENTS)) {
            // Remove contents of needle when crouched
            if(player.isCrouching()) {
                player.playSound(SoundEvents.GENERIC_BURN, 0.8F, 0.5F);
                return InteractionResultHolder.sidedSuccess(new ItemStack(ItemRegistry.COPPER_NEEDLE.get()), level.isClientSide());
            }
            // If an empty vial is in the other hand, put the contents of this needle into it.
            else if(otherStack.is(ItemRegistry.GLASS_VIAL)){
                if (thisStack.has(DataComponentsRegistry.AFFINITY_STORED_ITEM)) {
                    Holder<Item> holder = thisStack.get(DataComponentsRegistry.AFFINITY_STORED_ITEM);
                    ItemStack affinityStack = new ItemStack(ItemRegistry.AFFINITY_SOLUTION.get());
                    affinityStack.set(DataComponentsRegistry.AFFINITY_STORED_ITEM, holder);
                    player.getInventory().add(affinityStack);
                } else if (thisStack.has(DataComponents.POTION_CONTENTS)) {
                    PotionContents potionContents = thisStack.get(DataComponents.POTION_CONTENTS);
                    player.getInventory().add(VialUtil.createPotionItemStack(ItemRegistry.TOX_VIAL.get(), potionContents.potion().get()));
                }

                otherStack.consume(1, player);
                player.playSound(SoundEvents.BOTTLE_FILL, 1.0F, 0.5F);
                return InteractionResultHolder.sidedSuccess(new ItemStack(ItemRegistry.COPPER_NEEDLE.get()), level.isClientSide());
            }
        }

        return InteractionResultHolder.pass(player.getItemInHand(usedHand));
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity targetEntity, InteractionHand thisHand) {
        float targetHealth = targetEntity.getHealth();
        float targetMaxHealth = targetEntity.getMaxHealth();
        ToxData plyToxData = player.getData(DataAttachmentRegistry.TOX_DATA.get());

        if(targetHealth <= 0 || !targetEntity.isAlive()) return InteractionResult.PASS;

        if(stack.has(DataComponentsRegistry.AFFINITY_STORED_ITEM)){
            if (targetHealth > targetMaxHealth * HEALTH_THRESHOLD_PERCENT) {
                if (player.level().isClientSide()) {
                    Minecraft.getInstance().gui.setOverlayMessage(Component.translatable("message.toxony.needle.fail"), false);
                }
                return InteractionResult.FAIL;
            }
            handleKnowledgeFromItem(plyToxData, stack);
            targetEntity.hurt(new NeedleDamageSource(player.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(DamageTypes.PLAYER_ATTACK), player), 5.0F);

            player.playSound(SoundEvents.BEE_STING, 1.0F, 0.5F);
            player.awardStat(Stats.ITEM_USED.get(this));

            if (!player.hasInfiniteMaterials()) {
                player.setItemInHand(thisHand, new ItemStack(ItemRegistry.COPPER_NEEDLE));
            }

            return InteractionResult.sidedSuccess(targetEntity.level().isClientSide());
        }
        else if(stack.has(DataComponents.POTION_CONTENTS)){
            PotionContents potionContents = stack.get(DataComponents.POTION_CONTENTS);
            if( potionContents.hasEffects()){
                potionContents.forEachEffect(effect -> {
                    if (effect.getEffect().value().isInstantenous()) {
                        effect.getEffect().value().applyInstantenousEffect(player, player, targetEntity, effect.getAmplifier(), 1.0);
                    } else {
                        targetEntity.addEffect(effect);
                    }
                });
            }
            targetEntity.hurt(new NeedleDamageSource(player.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(DamageTypes.PLAYER_ATTACK), player), 2.0F);

            player.playSound(SoundEvents.BEE_STING, 1.0F, 0.5F);
            player.awardStat(Stats.ITEM_USED.get(this));

            if (!player.hasInfiniteMaterials()) {
                player.setItemInHand(thisHand, new ItemStack(ItemRegistry.COPPER_NEEDLE));
            }

            return InteractionResult.sidedSuccess(targetEntity.level().isClientSide());
        }

        return InteractionResult.PASS;
    }

    private static void handleKnowledgeFromItem(ToxData toxData, ItemStack stack){
        ItemStack storedStack = new ItemStack(stack.get(DataComponentsRegistry.AFFINITY_STORED_ITEM).value());
        Player player = toxData.getPlayer();

        toxData.addKnownIngredients(storedStack, 10);
        if(toxData.getIngredientProgress(storedStack) >= ToxData.MINIMUM_KNOW){
            if(player.level().isClientSide()){
                Minecraft.getInstance().gui.setOverlayMessage(
                        Component.translatable("message.toxony.needle.knowledge.success", Component.translatable(storedStack.getDescriptionId()).getString()),
                        false
                );
            }
        }
        else{
            if(player.level().isClientSide()){
                Minecraft.getInstance().gui.setOverlayMessage(
                        Component.translatable("message.toxony.needle.knowledge.fail", Component.translatable(storedStack.getDescriptionId()).getString()),
                        false
                );
            }
        }
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        if(stack.has(DataComponents.POTION_CONTENTS)){
            return Potion.getName(stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).potion(), this.getDescriptionId() + ".effect.");
        }
        else if(stack.has(DataComponentsRegistry.AFFINITY_STORED_ITEM)){
            return this.getDescriptionId() + ".affinity";
        }
        return super.getDescriptionId();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        PotionContents potioncontents = stack.get(DataComponents.POTION_CONTENTS);

        if (potioncontents != null) {
            potioncontents.addPotionTooltip(tooltipComponents::add, 1.0F, context.tickRate());
        }
    }
}
