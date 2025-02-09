package xyz.yfrostyf.toxony.client.gui;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import xyz.yfrostyf.toxony.blocks.entities.CopperCrucibleBlockEntity;
import xyz.yfrostyf.toxony.recipes.CrucibleRecipe;
import xyz.yfrostyf.toxony.registries.BlockRegistry;
import xyz.yfrostyf.toxony.registries.MenuRegistry;
import xyz.yfrostyf.toxony.registries.RecipeRegistry;

import java.util.Objects;

public class CopperCrucibleMenu extends AbstractContainerMenu {
    public final ItemStackHandler inventory;
    public final CopperCrucibleBlockEntity blockEntity;
    public final ContainerData data;
    public final ContainerLevelAccess access;

    private static final RecipeType<CrucibleRecipe> recipeType = RecipeRegistry.CRUCIBLE_RECIPE.get();

    public CopperCrucibleMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, getBlockEntity(inv, extraData), new SimpleContainerData(4), ContainerLevelAccess.NULL);
    }

    public CopperCrucibleMenu(int id, Inventory plyInventory, CopperCrucibleBlockEntity blockEntity, ContainerData data, ContainerLevelAccess access) {
        super(MenuRegistry.COPPER_CRUCIBLE_MENU.get(), id);

        this.inventory = blockEntity.getItemContainer();
        this.blockEntity = blockEntity;
        checkContainerDataCount(data, 4);
        this.data = data;
        this.access = access;

        // Crucible Slots
        this.addSlot(new SlotItemHandler(inventory, 0, 80, 21){
            @Override
            public void setChanged(){
                // Only detect change for input/output slot, not fuel slot.
                CopperCrucibleMenu.this.slotsChanged(this.container);
                this.container.setChanged();
            }
        });
        this.addSlot(new SlotItemHandler(inventory, 1, 80, 55){
            @Override
            public boolean mayPlace(ItemStack stack) {
                // Only allow fuel items in this slot.
                if (stack.getBurnTime(recipeType) <= 0)
                    return false;
                return super.mayPlace(stack);
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

        this.addDataSlots(data);
    }

    private static CopperCrucibleBlockEntity getBlockEntity(final Inventory plyInventory, final FriendlyByteBuf data) {
        Objects.requireNonNull(plyInventory, "plyInventory cannot be null");
        Objects.requireNonNull(data, "data cannot be null");

        final BlockEntity blockAtPos = plyInventory.player.level().getBlockEntity(data.readBlockPos());
        if (blockAtPos instanceof CopperCrucibleBlockEntity) {
            return (CopperCrucibleBlockEntity) blockAtPos;
        }
        throw new IllegalStateException("The block entity is not correct at CopperCrucibleMenu#getBLockEntity " + blockAtPos);
    }

    @Override
    public void slotsChanged(Container container) {
        if (blockEntity.getLevel() == null) return;
        blockEntity.resetCrucible();

        blockEntity.getLevel().sendBlockUpdated(blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity.getBlockState(), Block.UPDATE_CLIENTS);
        super.slotsChanged(container);
    }


    // Total slots 0-37
    // Container 0-1
    // Inventory 2-28
    // Hotbar 29-37
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

        // Check the input slot or fuel slot is selected.
        if (selectedSlotIndex >= 0 && selectedSlotIndex < 2) {
            // Try to move the result slot into the player inventory/hotbar
            if (!this.moveItemStackTo(rawStack, 2, 38, true)) {
                // If cannot move, no longer quick move
                return ItemStack.EMPTY;
            }
        }
        // If slot was selected in player inventory, then move it to either
        // container, player inventory or hotbar.
        else if (selectedSlotIndex >= 2 && selectedSlotIndex < 38) {
            // Check if item can be used as fuel or not.
            if(selectedSlot.getItem().getBurnTime(recipeType) > 0){
                // Check if we can move it into the fuel slot.
                if (!this.moveItemStackTo(rawStack, 1, 2, false)) {
                    // Check if we can move it into the input slot.
                    if (!this.moveItemStackTo(rawStack, 0, 1, false)) {
                        // Checks if it was selected in inventory
                        if (selectedSlotIndex < 29) {
                            // Try to move item into hotbar
                            if (!this.moveItemStackTo(rawStack, 29, 38, false)) {
                                return ItemStack.EMPTY; // If cannot move, no longer quick move
                            }
                        }
                        // Assume we select a hotbar item, then try to move into player inventory slot
                        else if (!this.moveItemStackTo(rawStack, 2, 29, false)) {
                            return ItemStack.EMPTY; // If cannot move, no longer quick move
                        }
                    }
                }
            }
            else{
                // Check if we can move it into the input slot.
                if (!this.moveItemStackTo(rawStack, 0, 1, false)) {
                    // Checks if it was selected in inventory
                    if (selectedSlotIndex < 29) {
                        // Try to move item into hotbar
                        if (!this.moveItemStackTo(rawStack, 29, 38, false)) {
                            return ItemStack.EMPTY; // If cannot move, no longer quick move
                        }
                    }
                    // Assume we select a hotbar item, then try to move into player inventory slot
                    else if (!this.moveItemStackTo(rawStack, 2, 29, false)) {
                        return ItemStack.EMPTY; // If cannot move, no longer quick move
                    }
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
        return stillValid(access, player, BlockRegistry.COPPER_CRUCIBLE.get());
    }

    protected boolean isFuel(ItemStack stack) {
        return stack.getBurnTime(this.recipeType) > 0;
    }

    public float getCookProgress() {
        int i = this.data.get(2);
        int j = this.data.get(3);
        return j != 0 && i != 0 ? Mth.clamp((float)i / (float)j, 0.0F, 1.0F) : 0.0F;
    }

    public float getLitProgress() {
        int i = this.data.get(1);
        if (i == 0) {
            i = 200;
        }

        return Mth.clamp((float)this.data.get(0) / (float)i, 0.0F, 1.0F);
    }

    public boolean isCooking() {
        return this.data.get(2) > 0;
    }

    public boolean isLit() {
        return this.data.get(0) > 0;
    }
}
