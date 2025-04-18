package xyz.yfrostyf.toxony.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import xyz.yfrostyf.toxony.registries.DataComponentsRegistry;
import xyz.yfrostyf.toxony.registries.ItemRegistry;
import xyz.yfrostyf.toxony.registries.TagRegistry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class CycleBowItem extends ProjectileWeaponItem{
    public static final Predicate<ItemStack> ARROWS_BOLTS_CARTRIDGES = ((Predicate<ItemStack>)(itemstack -> itemstack.is(TagRegistry.BOLTS))).or(((itemstack -> itemstack.is(ItemTags.ARROWS)))).or(itemstack -> itemstack.is(ItemRegistry.BOLT_CARTRIDGE) && itemstack.has(DataComponents.CHARGED_PROJECTILES));

    private static final float ARROW_POWER = 3.4F;
    private static final float DEFAULT_SINGLE_LOAD = 20.0F;
    private final int maxShots;

    public CycleBowItem(Properties properties, int maxShots) {
        super(properties);
        this.maxShots = maxShots;
    }

    @Override
    public Predicate<ItemStack> getSupportedHeldProjectiles(ItemStack stack) {
        return ARROWS_BOLTS_CARTRIDGES;
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return ARROWS_BOLTS_CARTRIDGES;
    }

    public void performShooting(Level level, LivingEntity shooter,
                                InteractionHand hand, ItemStack weapon,
                                float velocity, float inaccuracy,
                                @Nullable LivingEntity target) {
        if (level instanceof ServerLevel serverlevel) {
            if (shooter instanceof Player player && net.neoforged.neoforge.event.EventHooks.onArrowLoose(weapon, shooter.level(), player, 1, true) < 0) return;

            weapon.set(DataComponentsRegistry.LOADED_SHOTS, weapon.getOrDefault(DataComponentsRegistry.LOADED_SHOTS, 0) - 1);
            ChargedProjectiles chargedprojectiles = weapon.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
            if (!chargedprojectiles.isEmpty()) {
                List<ItemStack> chargedprojectilesStacks = new ArrayList<>(chargedprojectiles.getItems());
                this.shoot(serverlevel, shooter, hand, weapon, List.of(chargedprojectilesStacks.removeFirst()), velocity, inaccuracy, shooter instanceof Player, target);
                if (shooter instanceof ServerPlayer serverplayer) {
                    serverplayer.awardStat(Stats.ITEM_USED.get(weapon.getItem()));
                }
                weapon.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.of(chargedprojectilesStacks));
            }

            if(!isLoaded(weapon)){
                weapon.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
            }

        }
    }

    @Override
    protected Projectile createProjectile(Level level, LivingEntity shooter, ItemStack weapon, ItemStack ammo, boolean isCrit) {
        ArrowItem arrowItemDefault = (ArrowItem)Items.ARROW;
        AbstractArrow abstractarrow;
        if(ammo.getItem() instanceof ArrowItem arrowItem){
            abstractarrow = arrowItem.createArrow(level, ammo, shooter, weapon);
        }
        else if(ammo.getItem() instanceof BoltItem boltItem){
            abstractarrow = boltItem.createBolt(level, ammo, shooter, weapon);
        }
        else{
            abstractarrow = arrowItemDefault.createArrow(level, ammo, shooter, weapon);
        }

        if (isCrit) {
            abstractarrow.setCritArrow(true);
        }
        return abstractarrow;
    }

    @Override
    protected void shootProjectile(LivingEntity shooter, Projectile projectile,
                                   int index, float velocity,
                                   float inaccuracy, float angle,
                                   @Nullable LivingEntity target) {
        Vector3f vector3f;
        if (target != null) {
            double d0 = target.getX() - shooter.getX();
            double d1 = target.getZ() - shooter.getZ();
            double d2 = Math.sqrt(d0 * d0 + d1 * d1);
            double d3 = target.getY(0.3333333333333333) - projectile.getY() + d2 * 0.2F;
            vector3f = getProjectileShotVector(shooter, new Vec3(d0, d3, d1), angle);
        } else {
            Vec3 vec3 = shooter.getUpVector(1.0F);
            Quaternionf quaternionf = new Quaternionf().setAngleAxis((angle * (float) (Math.PI / 180.0)), vec3.x, vec3.y, vec3.z);
            Vec3 vec31 = shooter.getViewVector(1.0F);
            vector3f = vec31.toVector3f().rotate(quaternionf);
        }

        projectile.shoot(vector3f.x(), vector3f.y(), vector3f.z(), velocity, inaccuracy);
        float f = getShotPitch(shooter.getRandom(), index);
        shooter.level().playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.CROSSBOW_SHOOT, shooter.getSoundSource(), 1.0F, f);
    }

    private static Vector3f getProjectileShotVector(LivingEntity shooter, Vec3 distance, float angle) {
        Vector3f vector3f = distance.toVector3f().normalize();
        Vector3f vector3f1 = new Vector3f(vector3f).cross(new Vector3f(0.0F, 1.0F, 0.0F));
        if ((double)vector3f1.lengthSquared() <= 1.0E-7) {
            Vec3 vec3 = shooter.getUpVector(1.0F);
            vector3f1 = new Vector3f(vector3f).cross(vec3.toVector3f());
        }

        Vector3f vector3f2 = new Vector3f(vector3f).rotateAxis((float) (Math.PI / 2), vector3f1.x, vector3f1.y, vector3f1.z);
        return new Vector3f(vector3f).rotateAxis(angle * (float) (Math.PI / 180.0), vector3f2.x, vector3f2.y, vector3f2.z);
    }

    private static float getShotPitch(RandomSource random, int index) {
        return index == 0 ? 1.0F : getRandomShotPitch((index & 1) == 1, random);
    }

    private static float getRandomShotPitch(boolean isHighPitched, RandomSource random) {
        float f = isHighPitched ? 0.83F : 0.63F;
        return 1.0F / (random.nextFloat() * 0.5F + 1.8F) + f;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 6;
    }

    private boolean tryLoadProjectiles(LivingEntity shooter, ItemStack stack, ItemStack ammo) {
        List<ItemStack> list = new LinkedList<>(stack.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY).getItems());
        int loadedShots = stack.getOrDefault(DataComponentsRegistry.LOADED_SHOTS, 0);

        if (loadedShots <= this.maxShots && !ammo.isEmpty()) {
            if(ammo.is(ItemRegistry.BOLT_CARTRIDGE)){
                if(ammo.has(DataComponents.CHARGED_PROJECTILES) && getLoadedShots(stack) == 0){
                    ItemStack usedAmmo = useAmmo(stack, ammo, shooter, false);
                    stack.set(DataComponents.CHARGED_PROJECTILES, usedAmmo.get(DataComponents.CHARGED_PROJECTILES));
                    if(shooter instanceof Player player){
                        usedAmmo.remove(DataComponents.CHARGED_PROJECTILES);
                        player.getInventory().add(usedAmmo.copy());
                    }
                    stack.set(DataComponentsRegistry.LOADED_SHOTS, 3);
                    return true;
                }
                return false;
            }
            else{
                ItemStack usedAmmo = useAmmo(stack, ammo, shooter, false);
                list.add(usedAmmo);
                stack.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.of(list));
                stack.set(DataComponentsRegistry.LOADED_SHOTS, loadedShots + 1);
                return true;
            }
        }
        return false;
    }

    public static int getLoadedShots(ItemStack crossbowStack) {
        return crossbowStack.getOrDefault(DataComponentsRegistry.LOADED_SHOTS, 0);
    }

    public static boolean isLoaded(ItemStack crossbowStack) {
        ChargedProjectiles chargedprojectiles = crossbowStack.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
        Integer loadedShots = crossbowStack.getOrDefault(DataComponentsRegistry.LOADED_SHOTS, 0);
        return !chargedprojectiles.isEmpty() && loadedShots > 0;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if(player.getProjectile(itemstack).isEmpty() && !player.hasInfiniteMaterials() && !isLoaded(itemstack)){
            return InteractionResultHolder.fail(itemstack);
        }
        else if (isLoaded(itemstack)) {
            this.performShooting(level, player, hand, itemstack, ARROW_POWER, 1.0F, null);
            itemstack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            player.getCooldowns().addCooldown(this, 10);
            return InteractionResultHolder.consume(itemstack);
        }
        else {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remaining) {
        if (!level.isClientSide && stack.getItem() instanceof CycleBowItem cycleBowItem) {
            int tickThreshold = Mth.floor((cycleBowItem.getUseDuration(stack, livingEntity) - DEFAULT_SINGLE_LOAD) / this.maxShots);
            int useDuration = this.getUseDuration(stack, livingEntity);
            ItemStack ammoStack = livingEntity.getProjectile(stack);

            // Play start noise when remaining is the total duration.
            if(remaining == useDuration){
                level.playSound(null,
                        livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                        SoundEvents.CROSSBOW_LOADING_START, livingEntity.getSoundSource(),
                        1.0F, 1.0F);
            }
            // Check if ammo is cartridge for special ammo handling.
            else if(ammoStack.is(ItemRegistry.BOLT_CARTRIDGE) && getLoadedShots(stack) == 0){
                if(remaining == DEFAULT_SINGLE_LOAD + 10 && this.tryLoadProjectiles(livingEntity, stack, ammoStack)){
                    level.playSound(null,
                            livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                            SoundEvents.CROSSBOW_LOADING_MIDDLE, livingEntity.getSoundSource(),
                            1.0F, 0.8F);
                }
            }
            // Else handle ammo as normal.
            else if(remaining % tickThreshold == 0 && remaining <= useDuration - tickThreshold && getLoadedShots(stack) < maxShots){
                if (this.tryLoadProjectiles(livingEntity, stack, ammoStack)){
                    level.playSound(null,
                            livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                            SoundEvents.CROSSBOW_LOADING_MIDDLE, livingEntity.getSoundSource(),
                            1.0F, 1.0F);
                }
            }
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeLeft) {
        if (isLoaded(stack)) {
            level.playSound(null,
                    livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                    SoundEvents.CROSSBOW_LOADING_END, livingEntity.getSoundSource(),
                    0.5F, 1.0F / (level.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F);
        }
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return (int) (this.maxShots * DEFAULT_SINGLE_LOAD + DEFAULT_SINGLE_LOAD);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.CROSSBOW;
    }

    @Override
    public boolean useOnRelease(ItemStack stack) {
        return stack.is(this);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        ChargedProjectiles chargedprojectiles = stack.get(DataComponents.CHARGED_PROJECTILES);
        if (chargedprojectiles != null && isLoaded(stack)) {
            MutableComponent component = Component.translatable("item.toxony.cyclebow.loaded")
                    .withStyle(ChatFormatting.GRAY);
            tooltipComponents.add(component);
            for(ItemStack itemstack : chargedprojectiles.getItems()){
                MutableComponent componentItem = Component.literal(itemstack.getDisplayName().getString())
                        .withStyle(ChatFormatting.DARK_GRAY);
                tooltipComponents.add(componentItem);
            }
        }
    }

}
