package xyz.yfrostyf.toxony.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import xyz.yfrostyf.toxony.blocks.CopperCrucibleBlock;
import xyz.yfrostyf.toxony.client.gui.CopperCrucibleMenu;
import xyz.yfrostyf.toxony.recipes.CrucibleRecipe;
import xyz.yfrostyf.toxony.registries.BlockRegistry;
import xyz.yfrostyf.toxony.registries.RecipeRegistry;

import java.util.Optional;


public class CopperCrucibleBlockEntity extends BlockEntity implements IItemHandler, MenuProvider {
    private static final int CONTAINER_SIZE = 2;


    public int litTime;
    public int litDuration;
    public int cookingProgress;
    public int cookingTotalTime;
    public ItemStack resultItem;
    public ItemStackHandler itemContainer;
    private RecipeType<CrucibleRecipe> recipeType;

    protected final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    if (litDuration > Short.MAX_VALUE) {
                        // Neo: preserve litTime / litDuration ratio on the client as data slots are synced as shorts.
                        return Mth.floor(((double) litTime / litDuration) * Short.MAX_VALUE);
                    }
                    return CopperCrucibleBlockEntity.this.litTime;
                case 1:
                    return Math.min(CopperCrucibleBlockEntity.this.litDuration, Short.MAX_VALUE);
                case 2:
                    return CopperCrucibleBlockEntity.this.cookingProgress;
                case 3:
                    return CopperCrucibleBlockEntity.this.cookingTotalTime;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    CopperCrucibleBlockEntity.this.litTime = value;
                    break;
                case 1:
                    CopperCrucibleBlockEntity.this.litDuration = value;
                    break;
                case 2:
                    CopperCrucibleBlockEntity.this.cookingProgress = value;
                    break;
                case 3:
                    CopperCrucibleBlockEntity.this.cookingTotalTime = value;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    public CopperCrucibleBlockEntity(BlockPos pos, BlockState state){
        super(BlockRegistry.COPPER_CRUCIBLE_ENTITY.get(), pos, state);
        this.litTime = 0;
        this.litDuration = 0;
        this.cookingProgress = 0;
        this.cookingTotalTime = 0;
        resultItem = ItemStack.EMPTY;
        itemContainer = new ItemStackHandler(CONTAINER_SIZE){
            @Override
            public int getSlotLimit(int slot) {
                // Have to set stack limit here.
                if(slot == 0)return 1;
                return Item.DEFAULT_MAX_STACK_SIZE;
            }
        };
    }

    //
    // |--------------------------Methods--------------------------|
    //


    public static void tick(Level level, BlockPos pos, BlockState state, BlockEntity entity) {
        if(!(entity instanceof CopperCrucibleBlockEntity blockEntity))return;

        // Server Side actions
        if(level.isClientSide()){return;}
        ItemStack input = blockEntity.itemContainer.getStackInSlot(0);
        ItemStack fuel = blockEntity.itemContainer.getStackInSlot(1);
        ItemStack result = blockEntity.getResultItem();

        // Lit crucible if currently not lit and has fuel
        if (!blockEntity.isLit() && !fuel.isEmpty() && !blockEntity.getResultItem().isEmpty()) {
            ItemStack extract = blockEntity.itemContainer.extractItem(1, 1, false);
            blockEntity.litTime = extract.getBurnTime(blockEntity.recipeType);
            blockEntity.litDuration = blockEntity.litTime;
        }

        // Process when crucible is lit
        if (blockEntity.isLit()) {
            // If crucible has a result item, check if its finished smelting or not
            if (!result.isEmpty()) {
                if (blockEntity.cookingProgress < blockEntity.cookingTotalTime) {
                    blockEntity.cookingProgress = Mth.clamp(blockEntity.cookingProgress + 1, 0, blockEntity.cookingTotalTime);
                } else {
                    blockEntity.itemContainer.setStackInSlot(0, blockEntity.getResultItem());
                    blockEntity.resetCrucible();
                }
            }
            blockEntity.litTime--;
        }
        else{
            blockEntity.cookingProgress = Mth.clamp(blockEntity.cookingProgress - 2, 0, blockEntity.cookingTotalTime);
        }

        // If result is empty, check if any recipes in input match a recipe
        if(result.isEmpty() && blockEntity.findRecipe().isPresent()){
            Optional<RecipeHolder<CrucibleRecipe>> recipe = blockEntity.findRecipe();
            blockEntity.setResultItem(recipe.get().value().assemble(
                    new SingleRecipeInput(blockEntity.itemContainer.getStackInSlot(0)),
                    level.registryAccess()
            ));
            blockEntity.cookingTotalTime = recipe.get().value().getCookTime();
        }

        // For visuals when lit
        if (blockEntity.isLit()) {
            state = state.setValue(CopperCrucibleBlock.LIT, Boolean.valueOf(true));
            level.setBlock(pos, state, CopperCrucibleBlock.UPDATE_ALL);
        }
        else{
            state = state.setValue(CopperCrucibleBlock.LIT, Boolean.valueOf(false));
            level.setBlock(pos, state, CopperCrucibleBlock.UPDATE_ALL);
        }

        level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
    }

    public Optional<RecipeHolder<CrucibleRecipe>> findRecipe() {
        if (this.level == null) return Optional.empty();
        if(this.itemContainer.getStackInSlot(0) == ItemStack.EMPTY) return Optional.empty();

        SingleRecipeInput input = new SingleRecipeInput(this.itemContainer.getStackInSlot(0));
        return this.level.getRecipeManager().getRecipeFor(
                RecipeRegistry.CRUCIBLE_RECIPE.get(),
                input,
                this.level);
    }

    public void resetCrucible(){
        this.cookingProgress = 0;
        this.setResultItem(ItemStack.EMPTY);
    }

    public void setResultItem(ItemStack item){
        this.resultItem = item.copy();
    }

    public boolean hasItemInInventory(){
        if (getItemContainer().getStackInSlot(0) != ItemStack.EMPTY) {
                return true;
        }
        return false;
    }

    public ItemStack getResultItem(){
        return resultItem.copy();
    }

    public ItemStackHandler getItemContainer(){
        return itemContainer;
    }

    public boolean isLit() {
        return this.litTime > 0;
    }

    protected int getBurnDuration(ItemStack fuel) {
        if (fuel.isEmpty()) {
            return 0;
        } else {
            return fuel.getBurnTime(this.recipeType);
        }
    }

    //
    // |-----------------------Data/Network Handling Methods-----------------------|
    //

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        NonNullList<ItemStack> list = NonNullList.create();
        list.add(0, itemContainer.getStackInSlot(0).copy()); // store input/output item
        list.add(1, itemContainer.getStackInSlot(1).copy()); // store fuel
        list.addLast(resultItem); // store result item in sent list.

        ContainerHelper.saveAllItems(tag, list, registries);

        tag.putInt("BurnTime", this.litTime);
        tag.putInt("CookTime", this.cookingProgress);
        tag.putInt("CookTimeTotal", this.cookingTotalTime);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        NonNullList<ItemStack> list = NonNullList.withSize(3, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, list, registries);

        this.insertItem(0, list.get(0), false); // receive input/output item
        this.insertItem(1, list.get(1), false); // receive fuel
        this.setResultItem(list.getLast()); // receive result item in sent list.

        this.litTime = tag.getInt("BurnTime");
        this.cookingProgress = tag.getInt("CookTime");
        this.cookingTotalTime = tag.getInt("CookTimeTotal");
        this.litDuration = list.getLast().getBurnTime(this.recipeType);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries){
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    // Return our packet here. This method returning a non-null result tells the game to use this packet for syncing.
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        // The packet uses the CompoundTag returned by #getUpdateTag. An alternative overload of #create exists
        // that allows you to specify a custom update tag, including the ability to omit data the client might not need.
        return ClientboundBlockEntityDataPacket.create(this);
    }

    // Optionally: Run some custom logic when the packet is received.
    // The super/default implementation forwards to #loadAdditional.
    @Override
    public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket packet, HolderLookup.Provider registries) {
        super.onDataPacket(connection, packet, registries);
    }

    //
    // |-----------------------Container Methods-----------------------|
    //


    @Override
    public int getSlots() {
        return CONTAINER_SIZE;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return itemContainer.getStackInSlot(slot).copy();
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        ItemStack itemStack = stack.copy();
        itemContainer.setStackInSlot(slot, itemStack);
        return itemStack;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack targetStack = itemContainer.getStackInSlot(slot).copy();
        ItemStack returnItem;
        if(targetStack.getCount() > amount){
            returnItem = targetStack.copy();
            returnItem.setCount(amount);
            targetStack.setCount(targetStack.getCount()-amount);
        }
        else{
            returnItem = targetStack.copy();
            itemContainer.setStackInSlot(slot, ItemStack.EMPTY);
        }

        return returnItem;
    }

    @Override
    public int getSlotLimit(int slot) {
        return itemContainer.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        if(slot == 0)return true;
        if(stack.getBurnTime(this.recipeType) > 0)return true;
        return false;
    }

    //
    // |-----------------------MenuProvider Methods-----------------------|
    //

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.toxony.copper_crucible");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new CopperCrucibleMenu(id, playerInventory, this, this.data, ContainerLevelAccess.create(player.level(), worldPosition));
    }
}
