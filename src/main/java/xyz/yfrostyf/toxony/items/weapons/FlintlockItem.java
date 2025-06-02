package xyz.yfrostyf.toxony.items.weapons;

import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import xyz.yfrostyf.toxony.entities.item.FlintlockBall;
import xyz.yfrostyf.toxony.items.FlintlockRoundItem;
import xyz.yfrostyf.toxony.registries.*;

import java.util.List;
import java.util.function.Predicate;

public class FlintlockItem extends ProjectileWeaponItem {
    public static final Predicate<ItemStack> ROUNDS = (itemstack -> itemstack.is(TagRegistry.ROUNDS));

    private static final float ROUND_POWER = 3.6F;

    public FlintlockItem(Properties properties) {
        super(properties);
    }

    @Override
    public Predicate<ItemStack> getSupportedHeldProjectiles(ItemStack stack) {
        return ROUNDS;
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return ROUNDS;
    }

    public void performShooting(Level level, LivingEntity shooter,
                                InteractionHand hand, ItemStack weapon,
                                float velocity, float inaccuracy,
                                @Nullable LivingEntity target) {
        if (level instanceof ServerLevel serverlevel) {
            if (!(shooter instanceof Player)) return;

            ChargedProjectiles chargedprojectiles = weapon.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
            if (!chargedprojectiles.isEmpty()) {
                this.shoot(serverlevel, shooter, hand, weapon, chargedprojectiles.getItems(), velocity, inaccuracy, shooter instanceof Player, target);
                if (shooter instanceof ServerPlayer serverplayer) {
                    serverplayer.awardStat(Stats.ITEM_USED.get(weapon.getItem()));
                }
                weapon.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
            }
        }
    }

    protected Projectile createProjectile(Level level, LivingEntity shooter, ItemStack weapon, ItemStack ammo, boolean isCrit) {
        if(!ammo.isEmpty() && ammo.getItem() instanceof FlintlockRoundItem round){
            return round.asProjectile(level, shooter.position(), weapon, Direction.UP);
        }
        FlintlockBall roundEntity = new FlintlockBall(level, ammo, 9.0F, 0);
        return roundEntity;
    }

    @Override
    protected void shootProjectile(LivingEntity shooter, Projectile projectile, int index, float velocity, float inaccuracy, float angle, @Nullable LivingEntity target) {
        // This is the vector thats facing up from the shooter.
        Vec3 vec3 = shooter.getUpVector(1.0F);
        // This is setting a quaternion where its rotating around that up vector by angle(in radians)
        Quaternionf quaternionf = new Quaternionf().setAngleAxis((angle * (float) (Math.PI / 180.0)), vec3.x, vec3.y, vec3.z);
        Vec3 vec31 = shooter.getViewVector(1.0F);
        Vector3f vector3f = vec31.toVector3f().rotate(quaternionf);

        if(projectile instanceof FlintlockBall roundEntity){
            roundEntity.shoot(shooter, vector3f.x, vector3f.y, vector3f.z, velocity, inaccuracy);
        }

        ItemStack mainstack = shooter.getMainHandItem();
        ItemStack offstack = shooter.getOffhandItem();

        ItemStack usingstack = shooter.getUseItem();

        Quaternionf quaternionfoffset = quaternionf;
        final float rotationOffset = 11.0F;
        // Shoot for mainstack
        if(mainstack.is(ItemRegistry.FLINTLOCK)){
            if(ItemStack.matches(offstack, usingstack) || !offstack.is(ItemRegistry.FLINTLOCK)){
                quaternionfoffset = new Quaternionf().setAngleAxis(((angle - rotationOffset) * (float) (Math.PI / 180.0)), vec3.x, vec3.y, vec3.z);
            }
        }
        // Shoot for offstack
        if(offstack.is(ItemRegistry.FLINTLOCK)){
            if(ItemStack.matches(mainstack, usingstack) || !mainstack.is(ItemRegistry.FLINTLOCK)){
                quaternionfoffset = new Quaternionf().setAngleAxis((double)((angle + rotationOffset) * (float) (Math.PI / 180.0)), vec3.x, vec3.y, vec3.z);
            }
        }

        Vector3f vector3foffset = vec31.toVector3f().rotate(quaternionfoffset);
        Vector3f vector3fScaled = vector3foffset.mul(1.5F);
        ServerLevel svlevel = (ServerLevel)shooter.level();
        svlevel.sendParticles(ParticleRegistry.FLINTLOCK_BLAST_LARGE.get(),
                shooter.getX() + vector3fScaled.x, shooter.getEyeY() - 0.3F + vector3fScaled.y, shooter.getZ() + vector3fScaled.z,
                1, 0, 0, 0, 0);
        svlevel.sendParticles(ParticleTypes.LAVA,
                shooter.getX() + vector3fScaled.x, shooter.getEyeY() - 0.3F + vector3fScaled.y, shooter.getZ() + vector3fScaled.z,
                4, 0, 0, 0, 0);

        float pitch = getShotPitch(shooter.getRandom(), index);
        shooter.level().playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEventRegistry.FLINTLOCK_SHOOT, shooter.getSoundSource(), 1.0F, pitch);
    }

    private static float getShotPitch(RandomSource random, int index) {
        float highPitchFactor = (index & 1) == 1 ? 0.83F : 0.63F;
        float value = 1.0F / (random.nextFloat() * 0.5F + 1.8F) + highPitchFactor;
        return index == 0 ? 1.0F : value;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 16;
    }

    @Override
    protected int getDurabilityUse(ItemStack stack) {
        return 1;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack thisStack = player.getItemInHand(hand);

        if (isDuelWielding(player) && (!player.getProjectile(thisStack).isEmpty() || player.hasInfiniteMaterials())) {
            ItemStack mainStack = player.getMainHandItem();
            ItemStack offStack = player.getOffhandItem();

            // Check if mainhand is loading and offhand is loaded, shoot offhand, reload mainhand.
            if (!FlintlockItem.isLoaded(mainStack) && FlintlockItem.isLoaded(offStack)) {
                player.startUsingItem(InteractionHand.MAIN_HAND);
                return InteractionResultHolder.consume(thisStack);
            }
            // Check if mainhand is loaded and offhand is loading, shoot mainhand, reload offhand.
            else if (FlintlockItem.isLoaded(mainStack) && !FlintlockItem.isLoaded(offStack)) {
                player.startUsingItem(InteractionHand.OFF_HAND);
                return InteractionResultHolder.consume(thisStack);
            }
            else if(FlintlockItem.isLoaded(mainStack) && FlintlockItem.isLoaded(offStack)){
                this.performShooting(level, player, hand, mainStack, ROUND_POWER, 1.0F, null);
                return InteractionResultHolder.consume(thisStack);
            }
            else{
                player.startUsingItem(InteractionHand.MAIN_HAND);
                return InteractionResultHolder.consume(thisStack);
            }
        }
        else{
            if (player.getProjectile(thisStack).isEmpty() && !player.hasInfiniteMaterials() && !isLoaded(thisStack)) {
                return InteractionResultHolder.pass(thisStack);
            }
            else if (isLoaded(thisStack)) {
                this.performShooting(level, player, hand, thisStack, ROUND_POWER, 1.0F, null);
                return InteractionResultHolder.consume(thisStack);
            }
            else {
                player.startUsingItem(hand);
                return InteractionResultHolder.consume(thisStack);
            }
        }
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack thisStack, int remaining) {
        if (!level.isClientSide) {
            int useDuration = this.getUseDuration(thisStack, livingEntity);

            // Play start noise when remaining is the total duration.
            if(remaining == useDuration / 2){
                level.playSound(null,
                        livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                        SoundEventRegistry.FLINTLOCK_CLICK, livingEntity.getSoundSource(),
                        1.0F, 1.0F);
            }
            // Handle using differently if both hands are flintlocks
            if(isDuelWielding(livingEntity) && livingEntity instanceof Player player){
                InteractionHand otherHand = player.getUsedItemHand() == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
                ItemStack notUsingStack = player.getItemInHand(otherHand);

                if(remaining == useDuration / 2 && isLoaded(notUsingStack)){
                    this.performShooting(level, player, otherHand, notUsingStack, ROUND_POWER, 1.0F, null);
                }
                if(remaining <= 6){
                    if (this.tryLoadProjectiles(livingEntity, thisStack, livingEntity.getProjectile(thisStack))){
                        level.playSound(null,
                                livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                                SoundEventRegistry.FLINTLOCK_LOAD, livingEntity.getSoundSource(),
                                0.8F, 1.0F / (level.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F);
                        livingEntity.stopUsingItem();
                    }
                }
            }
            else{
                // Handle ammo as normal.
                if(remaining <= 1){
                    if (this.tryLoadProjectiles(livingEntity, thisStack, livingEntity.getProjectile(thisStack))){
                        level.playSound(null,
                                livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                                SoundEventRegistry.FLINTLOCK_LOAD, livingEntity.getSoundSource(),
                                0.8F, 1.0F / (level.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F);
                    }
                }
            }
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeLeft) {
        if (isLoaded(stack)) {
            level.playSound(null,
                    livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                    SoundEventRegistry.FLINTLOCK_CLICK, livingEntity.getSoundSource(),
                    0.8F, 1.0F / (level.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F);
        }
    }

    private boolean tryLoadProjectiles(LivingEntity shooter, ItemStack stack, ItemStack ammo) {
        List<ItemStack> list = draw(stack, ammo, shooter);
        if (!list.isEmpty()) {
            stack.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.of(list));
            return true;
        }
        return false;
    }

    public static boolean isLoaded(ItemStack stack) {
        ChargedProjectiles chargedprojectiles = stack.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
        return !chargedprojectiles.isEmpty();
    }

    public static boolean isDuelWielding(LivingEntity entity) {
        return entity.getMainHandItem().is(ItemRegistry.FLINTLOCK) && entity.getOffhandItem().is(ItemRegistry.FLINTLOCK);
    }

    public ItemStack getDefaultCreativeAmmo(@Nullable Player player, ItemStack projectileWeaponItem) {
        return ItemRegistry.IRON_ROUND.get().getDefaultInstance();
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.CUSTOM;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return isDuelWielding(entity) ? 24 : 28;
    }

    @Override
    public boolean useOnRelease(ItemStack stack) {
        return stack.is(this);
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 15;
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return repair.is(Items.IRON_INGOT);
    }
}
