package xyz.yfrostyf.toxony.client.events.subscribers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.IClientMobEffectExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import org.jetbrains.annotations.Nullable;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.client.ToxonyArmPoses;
import xyz.yfrostyf.toxony.client.utils.AnimationUtils;
import xyz.yfrostyf.toxony.client.utils.ModelUtils;
import xyz.yfrostyf.toxony.registries.ItemRegistry;
import xyz.yfrostyf.toxony.registries.MobEffectRegistry;

@EventBusSubscriber(modid = ToxonyMain.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientExtensionsRegisterEvent {

    @SubscribeEvent
    public static void onClientExtensionItemRegister(RegisterClientExtensionsEvent event){

        // Cyclebow
        event.registerItem(new IClientItemExtensions(){
            @Override
            public HumanoidModel.@Nullable ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                return ToxonyArmPoses.ONE_HAND_CROSSBOW_ENUM_PROXY.getValue();
            }

            @Override
            public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
                AnimationUtils.handleCycleAnimation(poseStack, player, arm, itemInHand, partialTick, equipProcess, swingProcess);
                return true;
            }
        }, ItemRegistry.CYCLEBOW.get());

        // Flintlock
        event.registerItem(new IClientItemExtensions(){
            @Override
            public HumanoidModel.@Nullable ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                return ToxonyArmPoses.ONE_HAND_PISTOL_ENUM_PROXY.getValue();
            }
            @Override
            public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
                AnimationUtils.handlePistolAnimation(poseStack, player, arm, itemInHand, partialTick, equipProcess, swingProcess);
                return true;
            }
        }, ItemRegistry.FLINTLOCK.get());

        // Flail
        event.registerItem(new IClientItemExtensions(){
            @Override
            public HumanoidModel.@Nullable ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                return ToxonyArmPoses.FLAIL_ENUM_PROXY.getValue();
            }

        }, ItemRegistry.FLAIL.get());
    }

    @SubscribeEvent
    public static void onClientExtensionArmourRegister(RegisterClientExtensionsEvent event){

        // |-----------------------------------------------------------------------------------|
        // |--------------------------------Plague Doctor Armor--------------------------------|
        // |-----------------------------------------------------------------------------------|
        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                return ModelUtils.PlagueDoctorArmor.getHead(livingEntity, itemStack, equipmentSlot, original);
            }
        }, ItemRegistry.PLAGUE_DOCTOR_HOOD.get());

        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                return ModelUtils.PlagueDoctorArmor.getBody(livingEntity, itemStack, equipmentSlot, original);
            }
        }, ItemRegistry.PLAGUE_DOCTOR_COAT.get());

        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                return ModelUtils.PlagueDoctorArmor.getLegs(livingEntity, itemStack, equipmentSlot, original);
            }
        }, ItemRegistry.PLAGUE_DOCTOR_LEGGINGS.get());

        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                return ModelUtils.PlagueDoctorArmor.getLegs(livingEntity, itemStack, equipmentSlot, original);
            }
        }, ItemRegistry.PLAGUE_DOCTOR_BOOTS.get());

        // |-----------------------------------------------------------------------------------|
        // |--------------------------------Plaguebringer Armor--------------------------------|
        // |-----------------------------------------------------------------------------------|
        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                return ModelUtils.PlaguebringerArmor.getHead(livingEntity, itemStack, equipmentSlot, original);
            }
        }, ItemRegistry.PLAGUEBRINGER_MASK.get());

        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                return ModelUtils.PlaguebringerArmor.getBody(livingEntity, itemStack, equipmentSlot, original);
            }
        }, ItemRegistry.PLAGUEBRINGER_COAT.get());

        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                return ModelUtils.PlaguebringerArmor.getLegs(livingEntity, itemStack, equipmentSlot, original);
            }
        }, ItemRegistry.PLAGUEBRINGER_LEGGINGS.get());

        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                return ModelUtils.PlaguebringerArmor.getLegs(livingEntity, itemStack, equipmentSlot, original);
            }
        }, ItemRegistry.PLAGUEBRINGER_BOOTS.get());


        // |-----------------------------------------------------------------------------------|
        // |------------------------------------Hunter Armor-----------------------------------|
        // |-----------------------------------------------------------------------------------|
        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                return ModelUtils.HunterArmor.getHead(livingEntity, itemStack, equipmentSlot, original);
            }
        }, ItemRegistry.HUNTER_HAT.get());

        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                return ModelUtils.HunterArmor.getBody(livingEntity, itemStack, equipmentSlot, original);
            }
        }, ItemRegistry.HUNTER_COAT.get());

        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                return ModelUtils.HunterArmor.getLegs(livingEntity, itemStack, equipmentSlot, original);
            }
        }, ItemRegistry.HUNTER_LEGGINGS.get());

        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                return ModelUtils.HunterArmor.getLegs(livingEntity, itemStack, equipmentSlot, original);
            }
        }, ItemRegistry.HUNTER_BOOTS.get());

        // |-----------------------------------------------------------------------------------|
        // |----------------------------Professional Hunter Armor------------------------------|
        // |-----------------------------------------------------------------------------------|
        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                return ModelUtils.ProfessionalHunterArmor.getHead(livingEntity, itemStack, equipmentSlot, original);
            }
        }, ItemRegistry.PROFESSIONAL_HUNTER_HAT.get());

        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                return ModelUtils.ProfessionalHunterArmor.getBody(livingEntity, itemStack, equipmentSlot, original);
            }
        }, ItemRegistry.PROFESSIONAL_HUNTER_COAT.get());

        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                return ModelUtils.ProfessionalHunterArmor.getLegs(livingEntity, itemStack, equipmentSlot, original);
            }
        }, ItemRegistry.PROFESSIONAL_HUNTER_LEGGINGS.get());

        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                return ModelUtils.ProfessionalHunterArmor.getLegs(livingEntity, itemStack, equipmentSlot, original);
            }
        }, ItemRegistry.PROFESSIONAL_HUNTER_BOOTS.get());
    }

    @SubscribeEvent
    public static void onClientExtensionMutagenRegister(RegisterClientExtensionsEvent event){
        final ResourceLocation  MUTAGEN_BACKGROUND_SMALL_SPRITE = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "container/inventory/mutagen_background_small");
        final ResourceLocation MUTAGEN_BACKGROUND_LARGE_SPRITE = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "container/inventory/mutagen_background_large");

        event.registerMobEffect(new IClientMobEffectExtensions() {
            @Override
            public boolean renderInventoryIcon(MobEffectInstance instance, EffectRenderingInventoryScreen<?> screen, GuiGraphics guiGraphics, int x, int y, int blitOffset) {
                int i = screen.getGuiLeft() + 176 + 2;
                int j = screen.width - i;
                boolean isSmall = j >= 120;
                var event = net.neoforged.neoforge.client.ClientHooks.onScreenPotionSize(screen, j, !isSmall, i);
                isSmall = !event.isCompact();

                if (isSmall) {
                    guiGraphics.blitSprite(MUTAGEN_BACKGROUND_LARGE_SPRITE, x - 6, y, 120, 32);
                }
                else {
                    guiGraphics.blitSprite(MUTAGEN_BACKGROUND_SMALL_SPRITE, x - 7, y, 32, 32);
                }
                return false;
            }
        },
                MobEffectRegistry.BEAST_MUTAGEN,
                MobEffectRegistry.AQUA_MUTAGEN,
                MobEffectRegistry.HOLLOW_MUTAGEN,
                MobEffectRegistry.NECROTIC_MUTAGEN,
                MobEffectRegistry.INFERNAL_MUTAGEN,
                MobEffectRegistry.SPIRIT_MUTAGEN);
    }
}

