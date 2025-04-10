package xyz.yfrostyf.toxony.client.gui;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import xyz.yfrostyf.toxony.blocks.entities.MortarPestleBlockEntity;
import xyz.yfrostyf.toxony.recipes.MortarPestleRecipe;
import xyz.yfrostyf.toxony.registries.BlockRegistry;
import xyz.yfrostyf.toxony.registries.MenuRegistry;

import java.util.Objects;


public class MortarPestleMenu extends AbstractContainerMenu {
    public final ItemStackHandler inventory;
    public final MortarPestleBlockEntity blockEntity;
    public final ContainerLevelAccess access;
    public final ContainerData data;

    public MortarPestleMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, getBlockEntity(inv, extraData), new SimpleContainerData(2), ContainerLevelAccess.NULL);
    }

    public MortarPestleMenu(int id, Inventory plyInventory, MortarPestleBlockEntity blockEntity, ContainerData data, ContainerLevelAccess access) {
        super(MenuRegistry.MORTAR_PESTLE_MENU.get(), id);

        this.inventory = blockEntity.getItemContainer();
        this.blockEntity = blockEntity;
        this.data = data;
        this.access = access;

        // Mortar Slots
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                this.addSlot(new SlotItemHandler(inventory, j + i * 2, 52 + j * 18, 25 + i * 18){
                    // Override setChanged within SlotItemhandler from its parent (Slots) so we can listen
                    // and call slotsChanged() in MortarPestleMenu when any of these slots change!
                    @Override
                    public void setChanged() {
                        MortarPestleMenu.this.slotsChanged(this.container);
                        super.setChanged();
                    }
                });
            }
        }

        // Player Inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(plyInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // Player Hotbar
        for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(plyInventory, k, 8 + k * 18, 142));
        }
    }

    private static MortarPestleBlockEntity getBlockEntity(final Inventory plyInventory, final FriendlyByteBuf data) {
        Objects.requireNonNull(plyInventory, "plyInventory cannot be null");
        Objects.requireNonNull(data, "data cannot be null");

        final BlockEntity blockAtPos = plyInventory.player.level().getBlockEntity(data.readBlockPos());
        if (blockAtPos instanceof MortarPestleBlockEntity) {
            return (MortarPestleBlockEntity) blockAtPos;
        }
        throw new IllegalStateException("The block entity is not correct at MortarPestleMenu#getBLockEntity " + blockAtPos);
    }

    // Total slots 0-39
    // Container 0-3
    // Inventory 4-30
    // Hotbar 31-39
    @Override
    public ItemStack quickMoveStack(Player player, int selectedSlotIndex) {
        // The quick moved slot stack
        ItemStack selectedStack = ItemStack.EMPTY;
        // The quick moved slot
        Slot selectedSlot = this.slots.get(selectedSlotIndex);

        // If the slot is in the valid range and the slot is not empty, otherwise skip quickMove.
        if (selectedSlot == null || !selectedSlot.hasItem())return ItemStack.EMPTY;

        ItemStack rawStack = selectedSlot.getItem();
        selectedStack = rawStack.copy();

        // Check the input slots are selected.
        if (selectedSlotIndex >= 0 && selectedSlotIndex < 4) {
            // Try to move the result slot into the player inventory/hotbar
            if (!this.moveItemStackTo(rawStack, 4, 40, true)) {
                // If cannot move, no longer quick move
                return ItemStack.EMPTY;
            }
        }
        // If slot was selected in player inventory, then move it to either
        // container, player inventory or hotbar.
        else if (selectedSlotIndex >= 4 && selectedSlotIndex < 40) {
            // Check if we can move it into the input slot.
            if (!this.moveItemStackTo(rawStack, 0, 4, false)) {
                // Checks if it was selected in inventory
                if (selectedSlotIndex < 31) {
                    // Try to move item into hotbar
                    if (!this.moveItemStackTo(rawStack, 31, 40, false)) {
                        return ItemStack.EMPTY; // If cannot move, no longer quick move
                    }
                }
                // Assume we select a hotbar item, then try to move into player inventory slot
                else if (!this.moveItemStackTo(rawStack, 4, 31, false)) {
                    return ItemStack.EMPTY; // If cannot move, no longer quick move
                }
            }
        }

        if (rawStack.isEmpty()) {
            // If the raw stack has completely moved out of the slot, set the slot to the empty stack
            selectedSlot.set(ItemStack.EMPTY);
        }
        else {
            // Otherwise, notify the slot that the stack count has changed
            selectedSlot.setChanged();
        }

        if (rawStack.getCount() == selectedStack.getCount()) {
            // If the raw stack was not able to move to another slot, no longer quick move
            return ItemStack.EMPTY;
        }
        // Execute logic on what to do post move with the remaining stack
        selectedSlot.onTake(player, rawStack);
        return selectedStack;
    }

    @Override
    public void slotsChanged(Container container) {
        if (blockEntity.getLevel() == null) return;

        ItemStack item = blockEntity.findRecipe()
                .map(RecipeHolder::value)
                .map(MortarPestleRecipe::getResultItem)
                .orElse(ItemStack.EMPTY);

        blockEntity.setResultItem(item);
        blockEntity.getLevel().sendBlockUpdated(blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity.getBlockState(), Block.UPDATE_CLIENTS);
        super.slotsChanged(container);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, BlockRegistry.MORTAR_PESTLE.get());
    }
}
