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

    // Total slots 0-42
    // Main 0-2
    // Solution 3-5
    // Toxin 6
    // Inventory 7-33
    // Hotbar 34-42
    @Override
    public ItemStack quickMoveStack(Player player, int selectedSlotIndex) {
        Slot selectedSlot = this.slots.get(selectedSlotIndex);

        if (selectedSlot == null || !selectedSlot.hasItem())return ItemStack.EMPTY;

        ItemStack rawStack = selectedSlot.getItem();
        ItemStack selectedStack = rawStack.copy();

        if (selectedSlotIndex >= 0 && selectedSlotIndex < 7) {
            if (!this.moveItemStackTo(rawStack, 7, 43, true)) {
                return ItemStack.EMPTY;
            }
        }
        else if(rawStack.is(ItemRegistry.TOXIN)){
            if (!this.moveItemStackTo(rawStack, 6, 7, true)) {
                if (selectedSlotIndex < 34) {
                    if (!this.moveItemStackTo(rawStack, 34, 43, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (!this.moveItemStackTo(rawStack, 7, 34, false)) {
                    return ItemStack.EMPTY;
                }
            }
        }
        else if(rawStack.is(ItemRegistry.AFFINITY_SOLUTION)){
            if (!this.moveItemStackTo(rawStack, 3, 6, true)) {
                if (selectedSlotIndex < 34) {
                    if (!this.moveItemStackTo(rawStack, 34, 43, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (!this.moveItemStackTo(rawStack, 7, 34, false)) {
                    return ItemStack.EMPTY;
                }
            }
        }
        else {
            if (!this.moveItemStackTo(rawStack, 0, 3, false)) {
                if (!this.moveItemStackTo(rawStack, 3, 6, true)) {
                    if (selectedSlotIndex < 35) {
                        if (!this.moveItemStackTo(rawStack, 35, 44, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (!this.moveItemStackTo(rawStack, 7, 35, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }
        }

        if (rawStack.isEmpty()) {
            selectedSlot.set(ItemStack.EMPTY);
        }
        else {
            selectedSlot.setChanged();
        }

        if (rawStack.getCount() == selectedStack.getCount()) {
            return ItemStack.EMPTY;
        }
        selectedSlot.onTake(player, rawStack);
        return selectedStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, BlockRegistry.ALCHEMICAL_FORGE.get());
    }
}
