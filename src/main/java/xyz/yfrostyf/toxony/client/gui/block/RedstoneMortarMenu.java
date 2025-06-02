package xyz.yfrostyf.toxony.client.gui.block;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import xyz.yfrostyf.toxony.blocks.RedstoneMortarBlock;
import xyz.yfrostyf.toxony.blocks.entities.RedstoneMortarBlockEntity;
import xyz.yfrostyf.toxony.registries.BlockRegistry;
import xyz.yfrostyf.toxony.registries.MenuRegistry;

import java.util.Objects;


public class RedstoneMortarMenu extends AbstractContainerMenu {
    public final ItemStackHandler inventory;
    public final RedstoneMortarBlockEntity blockEntity;
    public final ContainerLevelAccess access;
    public final ContainerData data;

    public RedstoneMortarMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, getBlockEntity(inv, extraData), new SimpleContainerData(3), ContainerLevelAccess.NULL);
    }

    public RedstoneMortarMenu(int id, Inventory plyInventory, RedstoneMortarBlockEntity blockEntity, ContainerData data, ContainerLevelAccess access) {
        super(MenuRegistry.REDSTONE_MORTAR_MENU.get(), id);

        this.inventory = blockEntity.getItemContainer();
        this.blockEntity = blockEntity;
        checkContainerDataCount(data, 3);
        this.data = data;
        this.access = access;

        // Mortar Slots
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                this.addSlot(new SlotItemHandler(inventory, j + i * 2, 52 + j * 18, 25 + i * 18) {
                    @Override
                    public void setChanged() {
                        RedstoneMortarMenu.this.slotsChanged(this.container);
                        super.setChanged();
                    }
                });
            }
        }

        // Use Slot
        this.addSlot(new SlotItemHandler(inventory, 4, 103, 52) {
            @Override
            public void setChanged() {
                RedstoneMortarMenu.this.slotsChanged(this.container);
                super.setChanged();
            }
        });

        // Result Slot
        this.addSlot(new SlotItemHandler(inventory, 5, 130, 33));

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

        this.addDataSlots(data);
    }

    private static RedstoneMortarBlockEntity getBlockEntity(final Inventory plyInventory, final FriendlyByteBuf data) {
        Objects.requireNonNull(plyInventory, "plyInventory cannot be null");
        Objects.requireNonNull(data, "data cannot be null");

        final BlockEntity blockAtPos = plyInventory.player.level().getBlockEntity(data.readBlockPos());
        if (blockAtPos instanceof RedstoneMortarBlockEntity) {
            return (RedstoneMortarBlockEntity) blockAtPos;
        }
        throw new IllegalStateException("The block entity is not correct at RedstoneMortarMenu#getBLockEntity " + blockAtPos);
    }

    // Total slots 0-41
    // Container 0-3
    // Use 4
    // Result 5
    // Inventory 6-32
    // Hotbar 33-41
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
        if (selectedSlotIndex >= 0 && selectedSlotIndex < 6) {
            // Try to move the result slot into the player inventory/hotbar
            if (!this.moveItemStackTo(rawStack, 6, 42, true)) {
                // If cannot move, no longer quick move
                return ItemStack.EMPTY;
            }
        }
        // If slot was selected in player inventory, then move it to either
        // container, player inventory or hotbar.
        else if (selectedSlotIndex >= 6 && selectedSlotIndex < 42) {
            // Check if we can move it into the input slot.
            if (!this.moveItemStackTo(rawStack, 0, 4, false)) {
                // Checks if it was selected in inventory
                if (selectedSlotIndex < 33) {
                    // Try to move item into hotbar
                    if (!this.moveItemStackTo(rawStack, 33, 42, false)) {
                        return ItemStack.EMPTY; // If cannot move, no longer quick move
                    }
                }
                // Assume we select a hotbar item, then try to move into player inventory slot
                else if (!this.moveItemStackTo(rawStack, 6, 33, false)) {
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

    public boolean isPestling() {
        return this.data.get(0) == 1;
    }

    public float getPestleProgress(){
        return (float) this.data.get(1) / RedstoneMortarBlock.PESTLE_TOTAL_COUNT;
    }

    @Override
    public void slotsChanged(Container container) {
        if (blockEntity.getLevel() == null) return;

        if (blockEntity.isPestling) {
            blockEntity.resetPestling();
        }
        super.slotsChanged(container);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, BlockRegistry.REDSTONE_MORTAR.get());
    }
}
