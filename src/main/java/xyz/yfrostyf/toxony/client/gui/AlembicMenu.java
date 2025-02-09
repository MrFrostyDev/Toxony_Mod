package xyz.yfrostyf.toxony.client.gui;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import xyz.yfrostyf.toxony.blocks.entities.AlembicBlockEntity;
import xyz.yfrostyf.toxony.blocks.entities.CopperCrucibleBlockEntity;
import xyz.yfrostyf.toxony.recipes.AlembicRecipe;
import xyz.yfrostyf.toxony.recipes.CrucibleRecipe;
import xyz.yfrostyf.toxony.registries.BlockRegistry;
import xyz.yfrostyf.toxony.registries.MenuRegistry;
import xyz.yfrostyf.toxony.registries.RecipeRegistry;

import java.util.Objects;

public class AlembicMenu extends AbstractContainerMenu {
    public final ItemStackHandler inventory;
    public final AlembicBlockEntity blockEntity;
    public final ContainerData data;
    public final ContainerLevelAccess access;

    private static final RecipeType<AlembicRecipe> recipeType = RecipeRegistry.ALEMBIC_RECIPE.get();

    public AlembicMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, getBlockEntity(inv, extraData), new SimpleContainerData(3), ContainerLevelAccess.NULL);
    }

    public AlembicMenu(int id, Inventory plyInventory, AlembicBlockEntity blockEntity, ContainerData data, ContainerLevelAccess access) {
        super(MenuRegistry.ALEMBIC_MENU.get(), id);

        this.inventory = blockEntity.getItemContainer();
        this.blockEntity = blockEntity;
        checkContainerDataCount(data, 3);
        this.data = data;
        this.access = access;

        // Alembic Slot | Result
        this.addSlot(new SlotItemHandler(inventory, 0, 116, 46){
            @Override
            public boolean mayPlace(ItemStack stack) {
                // Only allow glass bottles to be placed in this slot.
                return stack.is(Items.GLASS_BOTTLE);
            }

            @Override
            public void setChanged(){
                // Detect change for input slot.
                AlembicMenu.this.slotsChanged(this.container);
                this.container.setChanged();
            }
        });

        // Alembic Slot | Input
        this.addSlot(new SlotItemHandler(inventory, 1, 80, 51){
            @Override
            public void setChanged(){
                // Detect change for output slot.
                AlembicMenu.this.slotsChanged(this.container);
                this.container.setChanged();
            }
        });

        // Alembic Slot | Fuel
        this.addSlot(new SlotItemHandler(inventory, 2, 30, 24){
            @Override
            public boolean mayPlace(ItemStack stack) {
                // Only allow blaze powder in this slot.
                return stack.is(Items.BLAZE_POWDER);
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

    private static AlembicBlockEntity getBlockEntity(final Inventory plyInventory, final FriendlyByteBuf data) {
        Objects.requireNonNull(plyInventory, "plyInventory cannot be null");
        Objects.requireNonNull(data, "data cannot be null");

        final BlockEntity blockAtPos = plyInventory.player.level().getBlockEntity(data.readBlockPos());
        if (blockAtPos instanceof AlembicBlockEntity) {
            return (AlembicBlockEntity) blockAtPos;
        }
        throw new IllegalStateException("The block entity is not correct at AlembicBlockMenu#getBLockEntity " + blockAtPos);
    }

    @Override
    public void slotsChanged(Container container) {
        if (blockEntity.getLevel() == null) return;
        blockEntity.resetAlembic();
        blockEntity.getLevel().sendBlockUpdated(blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity.getBlockState(), Block.UPDATE_CLIENTS);
        super.slotsChanged(container);
    }

    // Total slots 0-39
    // Container 0-2
    // Inventory 3-29
    // Hotbar 30-38
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
        if (selectedSlotIndex >= 0 && selectedSlotIndex < 3) {
            // Try to move the result slot into the player inventory/hotbar
            if (!this.moveItemStackTo(rawStack, 3, 39, true)) {
                // If cannot move, no longer quick move
                return ItemStack.EMPTY;
            }
        }
        // If slot was selected in player inventory, then move it to either
        // container, player inventory or hotbar.
        else if (selectedSlotIndex >= 3 && selectedSlotIndex < 39) {
            // Check if item can be used as fuel or not.
            if(selectedSlot.getItem().is(Items.BLAZE_POWDER)){
                // Check if we can move it into the input slot.
                if (!this.moveItemStackTo(rawStack, 2, 3, false)) {
                    // Checks if it was selected in inventory
                    if (selectedSlotIndex < 30) {
                        // Try to move item into hotbar
                        if (!this.moveItemStackTo(rawStack, 30, 39, false)) {
                            return ItemStack.EMPTY; // If cannot move, no longer quick move
                        }
                    }
                    // Assume we select a hotbar item, then try to move into player inventory slot
                    else if (!this.moveItemStackTo(rawStack, 3, 30, false)) {
                        return ItemStack.EMPTY; // If cannot move, no longer quick move
                    }
                }
            }
            else if(selectedSlot.getItem().is(Items.GLASS_BOTTLE)){
                // Check if we can move it into the input slot.
                if (!this.moveItemStackTo(rawStack, 0, 1, false)) {
                    // Checks if it was selected in inventory
                    if (selectedSlotIndex < 30) {
                        // Try to move item into hotbar
                        if (!this.moveItemStackTo(rawStack, 30, 39, false)) {
                            return ItemStack.EMPTY; // If cannot move, no longer quick move
                        }
                    }
                    // Assume we select a hotbar item, then try to move into player inventory slot
                    else if (!this.moveItemStackTo(rawStack, 3, 30, false)) {
                        return ItemStack.EMPTY; // If cannot move, no longer quick move
                    }
                }
            }
            else{
                // Check if we can move it into the input slot.
                if (!this.moveItemStackTo(rawStack, 1, 2, false)) {
                    // Checks if it was selected in inventory
                    if (selectedSlotIndex < 30) {
                        // Try to move item into hotbar
                        if (!this.moveItemStackTo(rawStack, 30, 39, false)) {
                            return ItemStack.EMPTY; // If cannot move, no longer quick move
                        }
                    }
                    // Assume we select a hotbar item, then try to move into player inventory slot
                    else if (!this.moveItemStackTo(rawStack, 3, 30, false)) {
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
        return stillValid(access, player, BlockRegistry.ALEMBIC.get());
    }

    public boolean isBoiling() {
        return this.data.get(1) > 0;
    }

    public boolean hasFuel() {
        return this.data.get(0) > 0;
    }

    public float getBoilProgress() {
        int i = this.data.get(1);
        int j = this.data.get(2);
        return j != 0 && i != 0 ? Mth.clamp((float)i / (float)j, 0.0F, 1.0F) : 0.0F;
    }

    public float getBoilTick() {
        return this.data.get(1);
    }

    public float getFuelProgress() {
        return Mth.clamp((float)this.data.get(0) / (float)AlembicBlockEntity.MAX_FUEL, 0.0F, 1.0F);
    }
}
