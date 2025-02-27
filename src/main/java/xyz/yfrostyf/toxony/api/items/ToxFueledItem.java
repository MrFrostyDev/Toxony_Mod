package xyz.yfrostyf.toxony.api.items;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import xyz.yfrostyf.toxony.api.tox.ToxData;
import xyz.yfrostyf.toxony.items.WitchingBladeItem;
import xyz.yfrostyf.toxony.network.SyncToxPacket;
import xyz.yfrostyf.toxony.registries.DataAttachmentRegistry;
import xyz.yfrostyf.toxony.registries.DataComponentsRegistry;

public class ToxFueledItem extends Item {
    protected int tickrate;
    protected int cooldown;
    protected SoundEvent sound;

    public ToxFueledItem(Properties properties, int tickrate, int cooldown, SoundEvent sound) {
        super(properties.stacksTo(1).component(DataComponentsRegistry.ACTIVE.get(), false));
        this.cooldown = cooldown;
        this.tickrate = tickrate;
        this.sound = sound;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

        if(!(entity instanceof ServerPlayer svplayer)) return;
        if(!stack.has(DataComponentsRegistry.ACTIVE)) return;
        if(level.getServer().getTickCount() % tickrate != 0) {return;}

        ToxData plyToxData = entity.getData(DataAttachmentRegistry.TOX_DATA);

        if(plyToxData.getTox() > 0 && isActive(stack) && !plyToxData.getDeathState()){
            plyToxData.addTox(-1);
            PacketDistributor.sendToPlayer(svplayer, SyncToxPacket.create(plyToxData.getTox()));
        }
        else{
            stack.set(DataComponentsRegistry.ACTIVE, false);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        ToxData plyToxData = player.getData(DataAttachmentRegistry.TOX_DATA);

        if (!player.getCooldowns().isOnCooldown(this)) {
            if (!isActive(itemstack) && plyToxData.getTox() > 10) {
                player.playSound(sound, 1.0F, 1.0F);
                itemstack.set(DataComponentsRegistry.ACTIVE, true);
            }
            else{
                itemstack.set(DataComponentsRegistry.ACTIVE, false);
            }
            player.getCooldowns().addCooldown(this, cooldown);
        }
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }

    public static boolean isActive(ItemStack itemStack){
        return itemStack.has(DataComponentsRegistry.ACTIVE)
                && itemStack.get(DataComponentsRegistry.ACTIVE);
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder {
        protected Properties properties = new Properties();
        protected int tickrate = 40;
        protected int cooldown = 60;
        protected SoundEvent sound = SoundEvents.BREWING_STAND_BREW;

        public Builder properties(Properties properties){
            this.properties = properties;
            return this;
        }

        public Builder tickrate(int tickrate){
            this.tickrate = tickrate;
            return this;
        }

        public Builder cooldown(int cooldown){
            this.cooldown = cooldown;
            return this;
        }

        public Builder sound(SoundEvent sound){
            this.sound = sound;
            return this;
        }


        public ToxFueledItem build(){
            return new WitchingBladeItem(this.properties, this.tickrate, this.cooldown, this.sound);
        }
    }
}
