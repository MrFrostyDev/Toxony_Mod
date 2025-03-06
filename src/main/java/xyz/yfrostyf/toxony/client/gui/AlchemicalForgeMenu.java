package xyz.yfrostyf.toxony.client.gui;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import xyz.yfrostyf.toxony.blocks.entities.AlchemicalForgeBlockEntity;
import xyz.yfrostyf.toxony.registries.BlockRegistry;
import xyz.yfrostyf.toxony.registries.ItemRegistry;
import xyz.yfrostyf.toxony.registries.MenuRegistry;

import java.util.Objects;


public class AlchemicalForgeMenu extends AbstractContainerMenu {
    public final ItemStackHandler inventory;
    public final AlchemicalForgeBlockEntity blockEntity;
    public final ContainerLevelAccess access;
    public final ContainerData data;

    public AlchemicalForgeMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, getBlockEntity(inv, extraData), new SimpleContainerData(2), ContainerLevelAccess.NULL);
    }

    public AlchemicalForgeMenu(int id, Inventory plyInventory, AlchemicalForgeBlockEntity blockEntity, ContainerData data, ContainerLevelAccess access) {
        super(MenuRegistry.ALCHEMICAL_FORGE_MENU.get(), id);

        this.inventory = blockEntity.getItemContainer();
        this.blockEntity = blockEntity;
        this.data = data;
        this.access = access;

        // Alchemical Forge Slots
        this.addSlot(new SlotItemHandler(inventory, 0, 80, 43));
        this.addSlot(new SlotItemHandler(inventory, 1, 54, 43));
        this.addSlot(new SlotItemHandler(inventory, 2, 106,43));

        // Solution Slots
        for(int i=0; i<3; i++){
            this.addSlot(new SlotItemHandler(inventory, 3+i, 60+20*i, 7){
                @Override
                public boolean mayPlace(ItemStack stack) {
                    if(stack.is(ItemRegistry.AFFINITY_SOLUTION)) return true;
                    return false;
                }
            });
        }

        // Fuel Slot
        this.addSlot(new SlotItemHandler(inventory, 6, 19, 16){
            @Override
            public boolean mayPlace(ItemStack stack) {
                if(stack.is(ItemRegistry.TOXIN)) return true;
                return false;
            }
        });

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

    private static AlchemicalForgeBlockEntity getBlockEntity(final Inventory plyInventory, final FriendlyByteBuf data) {
        Objects.requireNonNull(plyInventory, "plyInventory cannot be null");
        Objects.requireNonNull(data, "data cannot be null");

        final BlockEntity blockAtPos = plyInventory.player.level().getBlockEntity(data.readBlockPos());
        if (blockAtPos instanceof AlchemicalForgeBlockEntity) {
            return (AlchemicalForgeBlockEntity) blockAtPos;
        }
        throw new IllegalStateException("The block entity is not correct at AlchemicalForgeBlockEntity#getBLockEntity " + blockAtPos);
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
    public boolean stillValid(Player player) {
        return stillValid(access, player, BlockRegistry.ALCHEMICAL_FORGE.get());
    }
}
