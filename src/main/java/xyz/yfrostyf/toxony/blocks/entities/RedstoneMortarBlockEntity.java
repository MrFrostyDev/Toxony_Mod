package xyz.yfrostyf.toxony.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.Nullable;
import xyz.yfrostyf.toxony.blocks.RedstoneMortarBlock;
import xyz.yfrostyf.toxony.client.gui.block.RedstoneMortarMenu;
import xyz.yfrostyf.toxony.items.BlendItem;
import xyz.yfrostyf.toxony.recipes.MortarPestleRecipe;
import xyz.yfrostyf.toxony.registries.DataComponentsRegistry;
import xyz.yfrostyf.toxony.registries.RecipeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static xyz.yfrostyf.toxony.registries.BlockRegistry.REDSTONE_MORTAR_ENTITY;

public class RedstoneMortarBlockEntity extends BlockEntity implements IItemHandler, MenuProvider {
    private static final int CONTAINER_SIZE = 6;
    private static final int USE_SLOT = 4;
    private static final int RESULT_SLOT = 5;

    public boolean isPestling;
    public int pestleCount;
    public int pestleTick;
    public ItemStack resultItem;
    public ItemStackHandler itemContainer;

    protected final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> RedstoneMortarBlockEntity.this.isPestling ? 1 : 0;
                case 1 -> RedstoneMortarBlockEntity.this.pestleCount;
                case 2 -> RedstoneMortarBlockEntity.this.pestleTick;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    RedstoneMortarBlockEntity.this.isPestling = value > 0;
                    break;
                case 1:
                    RedstoneMortarBlockEntity.this.pestleCount = value;
                    break;
                case 2:
                    RedstoneMortarBlockEntity.this.pestleTick = value;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    public RedstoneMortarBlockEntity(BlockPos pos, BlockState state){
        super(REDSTONE_MORTAR_ENTITY.get(), pos, state);
        isPestling = false;
        pestleCount = 0;
        pestleTick = 0;
        resultItem = ItemStack.EMPTY;
        itemContainer = new ItemStackHandler(CONTAINER_SIZE){
            @Override
            protected void onContentsChanged(int slot) {
                if(level != null){
                    level.sendBlockUpdated(pos, getBlockState(), getBlockState(), Block.UPDATE_ALL);
                    setChanged();
                }
            }
        };
    }

    //
    // |--------------------------Methods--------------------------|
    //

    // The signature of this method matches the signature of the BlockEntityTicker functional interface.
    public static void tick(Level level, BlockPos pos, BlockState state, BlockEntity entity) {
        if(!(entity instanceof RedstoneMortarBlockEntity blockEntity)) return;

        // Client/Server Side actions
        //
        if(blockEntity.pestleTick > 0 && blockEntity.isPestling){
            blockEntity.pestleTick--;
        }

        // Server Side actions
        //
        if(level.isClientSide()) return;

        if (!(state.getBlock() instanceof RedstoneMortarBlock)) return;
        if(blockEntity.isPestling && blockEntity.pestleTick > 0) return;

        BlockState blockState = state;

        if(blockEntity.getPestleProgress() >= 0.75F){
            blockState = blockState.setValue(RedstoneMortarBlock.INGREDIENTS, 5);
        }
        else if(blockEntity.getPestleProgress() >= 0.50F){
            blockState = blockState.setValue(RedstoneMortarBlock.INGREDIENTS, 4);
        }
        else if(blockEntity.getPestleProgress() >= 0.25F){
            blockState = blockState.setValue(RedstoneMortarBlock.INGREDIENTS, 3);
        }
        else if (!blockEntity.getResultItem().isEmpty()) {
            blockState = blockState.setValue(RedstoneMortarBlock.INGREDIENTS, 2);
        }
        else if (blockEntity.hasItemInInventory()) {
            blockState = blockState.setValue(RedstoneMortarBlock.INGREDIENTS, 1);
        }
        else{
            blockState = blockState.setValue(RedstoneMortarBlock.INGREDIENTS, 0);
        }
        level.setBlock(pos, blockState, Block.UPDATE_CLIENTS);
    }

    public float getPestleProgress(){
        return (float) this.pestleCount / RedstoneMortarBlock.PESTLE_TOTAL_COUNT;
    }

    //
    // Try to match ingredients with a possible recipe, if found then
    // return the recipe's resulting ItemStack. If none was found, return
    // an empty Stack.
    //
    public Optional<RecipeHolder<MortarPestleRecipe>> findRecipe(){
        if(this.level == null) return Optional.empty();

        // Remove all empty space (air) to match with recipes.
        // this.getItemContainer().getSlots()-2 Because we ignore the use slot and result slot.
        NonNullList<ItemStack> noAirList = NonNullList.create();
        for(int i=0; i<this.getItemContainer().getSlots()-2;i++){
            ItemStack item = this.getItemContainer().getStackInSlot(i);
            if(item.getItem() != Items.AIR){
                noAirList.add(item);
            }
        }
        RecipeWrapper wrapper = new RecipeWrapper(new ItemStackHandler(noAirList));

        return this.level.getRecipeManager().getRecipeFor(
                RecipeRegistry.MORTAR_PESTLE_RECIPE.get(),
                wrapper,
                this.level);
    }

    public void startPestling(){
        Optional<RecipeHolder<MortarPestleRecipe>> recipe = this.findRecipe();
        if(recipe.isEmpty()) return;
        if(this.getUseItem().getItem() != recipe.get().value().getUseItem().orElse(ItemStack.EMPTY).getItem()) return;

        this.pestleTick = 0;
        this.isPestling = true;

        this.setResultItem(recipe.get().value().getResultItem());
    }

    public void finishPestling(Level level){
        if(level == null) return;

        List<ItemStack> ingredientsCache = this.getIngredients();
        ItemStack resultStack = this.getResultItem();

        // If item is a blend, handle affinity stored items
        if (resultStack.getItem() instanceof BlendItem) {
            List<Holder<Item>> stored_items = new ArrayList<>();
            for (ItemStack item : ingredientsCache) {
                if (item.has(DataComponentsRegistry.POSSIBLE_AFFINITIES)) stored_items.add(item.getItemHolder());
            }
            resultStack.set(DataComponentsRegistry.AFFINITY_STORED_ITEMS, stored_items);
        }

        if (!itemContainer.getStackInSlot(USE_SLOT).isEmpty()) {
            itemContainer.extractItem(USE_SLOT, 1, false);
        }

        ItemStack remaining = this.insertItem(RESULT_SLOT, resultStack, false);
        if (remaining != ItemStack.EMPTY) {
            Block.popResource(this.level, this.getBlockPos(), remaining);
        }

       this.resetPestling();
    }

    public void resetPestling(){
        if(level == null) return;

        this.isPestling = false;
        this.pestleTick = 0;
        this.pestleCount = 0;
        this.setResultItem(ItemStack.EMPTY);
        this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
    }

    public boolean hasItemInInventory(){
        for(int i=0;i<getItemContainer().getSlots();i++){
            if(i==4) continue;
            if (!getItemContainer().getStackInSlot(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public List<ItemStack> getIngredients(){
        List<ItemStack> ingredientsList = new ArrayList<>();

        for (int i = 0; i < this.getItemContainer().getSlots()-2; i++) {
            ingredientsList.add(this.getItemContainer().extractItem(i, 1, false));
        }

        return ingredientsList;
    }

    public void setResultItem(ItemStack item){
        this.resultItem = item.copy();
    }

    public ItemStack getResultItem(){
        return resultItem.copy();
    }

    public ItemStack getUseItem(){
        return itemContainer.getStackInSlot(USE_SLOT).copy();
    }

    public ItemStackHandler getItemContainer(){
        return itemContainer;
    }

    //
    // |-----------------------Data/Network Handling Methods-----------------------|
    //

    // Read values from the passed CompoundTag here.
    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.isPestling = tag.getBoolean("redstone_mortar_pestling");
        this.pestleCount = tag.getInt("redstone_mortar_pestle_count");
        this.pestleTick = tag.getInt("redstone_mortar_pestle_tick");

        NonNullList<ItemStack> list = NonNullList.withSize(CONTAINER_SIZE+1, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, list, registries);

        int i = 0;
        while(i<CONTAINER_SIZE){
            this.itemContainer.setStackInSlot(i, list.get(i));
            i++;
        }
        this.setResultItem(list.getLast()); // also receive result item in sent list.
    }

    // Save values into the passed CompoundTag here.
    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("redstone_mortar_pestling", this.isPestling);
        tag.putInt("redstone_mortar_pestle_count", this.pestleCount);
        tag.putInt("redstone_mortar_pestle_tick", this.pestleTick);

        NonNullList<ItemStack> list = NonNullList.create();
        for(int i=0; i<CONTAINER_SIZE;i++){
            list.add(i, itemContainer.getStackInSlot(i).copy());
        }
        list.add(CONTAINER_SIZE, resultItem); // also store result item in sent list.

        ContainerHelper.saveAllItems(tag, list, registries);
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
        return this.itemContainer.insertItem(slot, itemStack, simulate);
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

    //
    // Checks if the ItemStack is valid input for this container.
    // More specifically used by the SlotItemHandler in the Slot class
    // to check if the item (using SlotItemHandler#mayPlace) is allowed to be placed in
    // the Slot instance. Returns true if the inputted ItemStack is allowed, otherwise false;
    //
    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return true;
    }


    //
    // |-----------------------MenuProvider Methods-----------------------|
    //

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.toxony.redstone_mortar");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new RedstoneMortarMenu(id, playerInventory, this, this.data, ContainerLevelAccess.create(player.level(), worldPosition));
    }
}
