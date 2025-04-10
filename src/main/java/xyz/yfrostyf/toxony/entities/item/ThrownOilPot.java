package xyz.yfrostyf.toxony.entities.item;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.*;
import net.neoforged.neoforge.common.Tags;
import xyz.yfrostyf.toxony.api.oils.ItemOil;
import xyz.yfrostyf.toxony.registries.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ThrownOilPot extends ThrowableItemProjectile implements ItemSupplier {
    public static final double SPLASH_RANGE = 4.0;
    private static final double SPLASH_RANGE_SQ = 16.0;
    private ItemStack itemStack;

    public ThrownOilPot(EntityType<? extends ThrownOilPot> entityType, Level level) {
        super(entityType, level);
    }

    public ThrownOilPot(Level level, LivingEntity shooter, ItemStack itemStack) {
        super(EntityRegistry.OIL_POT.get(), shooter, level);
        this.itemStack = itemStack;
    }

    public ThrownOilPot(Level level, ItemStack itemStack, double x, double y, double z) {
        super(EntityRegistry.OIL_POT.get(), x, y, z, level);
        this.itemStack = itemStack;
    }

    @Override
    protected Item getDefaultItem() {
        return ItemRegistry.EMPTY_OIL_POT.get();
    }

    public ItemStack getItem(){
        return new ItemStack(this.getDefaultItem());
    }

    @Override
    protected double getDefaultGravity() {
        return 0.04;
    }

    // Thank you Farmer's Delight for this method and a reminder to register this entity.
    @Override
    public void handleEntityEvent(byte id) {
        ItemStack entityStack = new ItemStack(this.getDefaultItem());
        if (id == 3) {
            ParticleOptions itemParticleOption = new ItemParticleOption(ParticleTypes.ITEM, entityStack);

            for (int i = 0; i < 12; ++i) {
                this.level().addParticle(itemParticleOption, this.getX(), this.getY(), this.getZ(),
                        ((double) this.random.nextFloat() * 2.0D - 1.0D) * 0.1F,
                        ((double) this.random.nextFloat() * 2.0D - 1.0D) * 0.1F + 0.1F,
                        ((double) this.random.nextFloat() * 2.0D - 1.0D) * 0.1F);
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level().isClientSide) {
            Direction direction = result.getDirection();
            BlockPos blockpos = result.getBlockPos();
            ItemOil itemOil = this.itemStack.getOrDefault(DataComponentsRegistry.OIL, ItemOil.EMPTY);
            if(itemOil.getOil() == OilsRegistry.ACID_OIL.get()){
                Vec3i vec3i = result.getDirection().getNormal();
                Vec3 vec3 = Vec3.atLowerCornerOf(vec3i).multiply(0.25, 0.25, 0.25);
                Vec3 vec31 = result.getLocation().add(vec3);
                applyAcidSplash(itemOil, vec31, 0.8F);
            }
            else if (!itemOil.isEmpty()) {
                this.placeOilBlock(blockpos);
                this.placeOilBlock(blockpos.relative(direction.getOpposite()));
                for (BlockPos blockNeighbourPos : Set.of(blockpos.north(), blockpos.east(), blockpos.south(), blockpos.west())) {
                    this.placeOilBlock(blockNeighbourPos);
                }
            }
        }
    }

    protected void placeOilBlock(BlockPos pos) {
        BlockState blockstate = this.level().getBlockState(pos);
        BlockState blockstateUp = this.level().getBlockState(pos.above());
        BlockState blockstateDown = this.level().getBlockState(pos.below());
        BlockState oilblockstate = BlockRegistry.OIL_LAYER.get().defaultBlockState();
        if(blockstateDown.is(BlockRegistry.OIL_LAYER)) return;
        if(blockstateUp.isEmpty() && blockstate.isFaceSturdy(this.level(), pos, Direction.UP)){
            this.level().setBlockAndUpdate(pos.above(), oilblockstate);
        }
        else if(this.level().isEmptyBlock(pos) && blockstateDown.isFaceSturdy(this.level(), pos.below(), Direction.UP)){
            this.level().setBlockAndUpdate(pos, oilblockstate);
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (this.level() instanceof ServerLevel svlevel) {
            ItemStack itemstack = this.itemStack;
            ItemOil itemOil = itemstack.getOrDefault(DataComponentsRegistry.OIL, ItemOil.EMPTY);
            svlevel.playSound(null, result.getLocation().x, result.getLocation().y, result.getLocation().z, SoundEvents.DECORATED_POT_SHATTER, SoundSource.NEUTRAL);

            if(itemOil.getOil() == OilsRegistry.ACID_OIL.get()) svlevel.playSound(null,
                    result.getLocation().x, result.getLocation().y, result.getLocation().z, SoundEvents.LAVA_EXTINGUISH, SoundSource.NEUTRAL);

            if(itemOil.getOil() == OilsRegistry.SMOKE_OIL.get()) svlevel.sendParticles(ParticleRegistry.SMOKE.get(),
                        result.getLocation().x, result.getLocation().y+1.0, result.getLocation().z,
                        3, 0.7, 0.5, 0.7, this.random.nextInt(4) * 0.1);
            else svlevel.sendParticles(ParticleRegistry.OIL_SMOKE.get(),
                        result.getLocation().x, result.getLocation().y+0.5, result.getLocation().z,
                        1, 0, 0, 0, 0);

            if (!itemOil.isEmpty()) {
                this.applySplash(
                        itemOil, result.getType() == HitResult.Type.ENTITY ? ((EntityHitResult)result).getEntity() : null
                );
            }
            this.discard();
        }
    }

    private void applySplash(ItemOil itemOil, @Nullable Entity p_entity) {
        AABB aabb = this.getBoundingBox().inflate(SPLASH_RANGE, 2.0, SPLASH_RANGE);
        List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, aabb);
        if (!list.isEmpty()) {
            for (LivingEntity livingentity : list) {
                if (livingentity.isAffectedByPotions()) {
                    double dist = this.distanceToSqr(livingentity);
                    if (dist < SPLASH_RANGE_SQ) {
                        double distMult;
                        if (livingentity == p_entity) {
                            distMult = 1.0;
                        } else {
                            distMult = 1.0 - Math.sqrt(dist) / SPLASH_RANGE;
                        }

                        if(itemOil.getOil() != OilsRegistry.ACID_OIL.get()){
                            MobEffectInstance mobeffectInstOil = new MobEffectInstance(MobEffectRegistry.FLAMMABLE, 600, 0);
                            livingentity.addEffect(mobeffectInstOil, this.getEffectSource());
                        }

                        for (Holder<MobEffect> holder : itemOil.getOil().getEffects()) {
                            MobEffectInstance mobeffectInst = new MobEffectInstance(holder, 400, itemOil.amplifier());
                            if (holder.value().isInstantenous()) {
                                holder.value().applyInstantenousEffect(this, this.getOwner(), livingentity, mobeffectInst.getAmplifier(), distMult);
                            } else {
                                int i = mobeffectInst.mapDuration(duration -> (int)(distMult * (double)duration + 0.5));
                                MobEffectInstance modifedMobEffectInstance = new MobEffectInstance(
                                        holder, i, mobeffectInst.getAmplifier(), mobeffectInst.isAmbient(), mobeffectInst.isVisible()
                                );
                                if (!modifedMobEffectInstance.endsWithin(20)) {
                                    livingentity.addEffect(modifedMobEffectInstance, this.getEffectSource());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Based on Minecraft's Explosion#explode method
    public void applyAcidSplash(ItemOil itemOil, Vec3 pos, float radius) {
        for (int j = 0; j < 4; j++) {
            for (int k = 0; k < 4; k++) {
                for (int l = 0; l < 4; l++) {
                    if (j == 0 || j == 3 || k == 0 || k == 3 || l == 0 || l == 3) {
                        double d0 = ((float) j / 3.0F * 2.0F - 1.0F);
                        double d1 = ((float) k / 3.0F * 2.0F - 1.0F);
                        double d2 = ((float) l / 3.0F * 2.0F - 1.0F);
                        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                        d0 /= d3;
                        d1 /= d3;
                        d2 /= d3;
                        float f = radius * (0.7F + this.level().random.nextFloat() * 0.6F);
                        double d4 = pos.x();
                        double d6 = pos.y();
                        double d8 = pos.z();

                        for (float f1 = 0.3F; f > 0.0F; f -= 0.22500001F) {
                            BlockPos blockpos = BlockPos.containing(d4, d6, d8);
                            BlockState blockstate = this.level().getBlockState(blockpos);
                            List<Pair<ItemStack, BlockPos>> dropsList = new ArrayList<>();
                            if (!this.level().isInWorldBounds(blockpos)) {
                                break;
                            }

                            // This is just for calculating explosion resistance.
                            Explosion explosion = new Explosion(level(), getEffectSource(), getX(), getY(), getZ(), radius, false, Explosion.BlockInteraction.KEEP);
                            float resistance = blockstate.getExplosionResistance(level(), blockpos, explosion);

                            if (resistance <= 7.0F) {
                                this.onAcidHit(itemOil.amplifier(), blockstate, level(), blockpos, this.getEffectSource() instanceof Player player ? player : null, dropsList);
                            }
                            for (Pair<ItemStack, BlockPos> pair : dropsList) {
                                Block.popResource(this.level(), pair.getSecond(), pair.getFirst());
                            }

                            d4 += d0 * 0.3F;
                            d6 += d1 * 0.3F;
                            d8 += d2 * 0.3F;
                        }
                    }
                }
            }
        }
    }

    protected void onAcidHit(int amplifier, BlockState state, Level level, BlockPos pos, @Nullable Player player, List<Pair<ItemStack, BlockPos>> list) {
        if (state.isAir() || !state.is(Tags.Blocks.ORES) && !state.is(Tags.Blocks.ORE_BEARING_GROUND_STONE) && !state.is(BlockTags.MINEABLE_WITH_PICKAXE)) return;
        if(amplifier < 1 && state.is(BlockTags.NEEDS_IRON_TOOL)) return;
        if(amplifier < 2 && state.is(BlockTags.NEEDS_DIAMOND_TOOL)) return;

        Optional<Holder.Reference<Enchantment>> fortune = level.holder(Enchantments.FORTUNE);
        if(fortune.isEmpty()) return;

        ItemStack stack = new ItemStack(Items.IRON_PICKAXE);
        ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        mutable.set(fortune.get(), 2);
        EnchantmentHelper.setEnchantments(stack, mutable.toImmutable());

        if (level instanceof ServerLevel serverlevel) {
            BlockEntity blockentity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
            LootParams.Builder lootParamBuilder = new LootParams.Builder(serverlevel)
                    .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                    .withParameter(LootContextParams.TOOL, stack)
                    .withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockentity)
                    .withOptionalParameter(LootContextParams.THIS_ENTITY, player)
                    .withLuck(player != null ? player.getLuck() : 0);

            state.spawnAfterBreak(serverlevel, pos, ItemStack.EMPTY, player != null);
            state.getDrops(lootParamBuilder).forEach(stack1 -> addOrAppendStack(list, stack1, pos));
        }

        level.destroyBlock(pos, false, this.getEffectSource());
    }

    private static void addOrAppendStack(List<Pair<ItemStack, BlockPos>> drops, ItemStack stack, BlockPos pos) {
        for (int i = 0; i < drops.size(); i++) {
            Pair<ItemStack, BlockPos> pair = drops.get(i);
            ItemStack itemstack = pair.getFirst();
            if (ItemEntity.areMergable(itemstack, stack)) {
                drops.set(i, Pair.of(ItemEntity.merge(itemstack, stack, 16), pair.getSecond()));
                if (stack.isEmpty()) {
                    return;
                }
            }
        }

        drops.add(Pair.of(stack, pos));
    }


    @Override
    public EntityType<?> getType() {
        return EntityRegistry.OIL_POT.get();
    }
}
