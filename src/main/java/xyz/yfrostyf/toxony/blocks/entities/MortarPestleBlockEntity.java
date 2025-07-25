package xyz.yfrostyf.toxony.blocks.entities;

import net.minecraft.client.Minecraft;
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
import net.minecraft.world.InteractionHand;
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
import xyz.yfrostyf.toxony.blocks.MortarPestleBlock;
import xyz.yfrostyf.toxony.client.gui.block.MortarPestleMenu;
import xyz.yfrostyf.toxony.items.BlendItem;
import xyz.yfrostyf.toxony.recipes.MortarPestleRecipe;
import xyz.yfrostyf.toxony.registries.DataComponentsRegistry;
import xyz.yfrostyf.toxony.registries.RecipeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static xyz.yfrostyf.toxony.registries.BlockRegistry.MORTAR_PESTLE_ENTITY;

public class MortarPestleBlockEntity extends BlockEntity implements IItemHandler, MenuProvider {
    public static final float DEFAULT_PESTLE_TICK = 20;
    private static final int CONTAINER_SIZE = 4;
    public static final int PESTLE_TOTAL_COUNT = 3;

    public boolean isPestling;
    public int pestleCount;
    public int pestleTick;
    public ItemStack resultItem;
    public ItemStackHandler itemContainer;

    protected final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> MortarPestleBlockEntity.this.isPestling ? 1 : 0;
                case 1 -> MortarPestleBlockEntity.this.pestleCount;
                case 2 -> MortarPestleBlockEntity.this.pestleTick;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    MortarPestleBlockEntity.this.isPestling = value > 0;
                    break;
                case 1:
                    MortarPestleBlockEntity.this.pestleCount = value;
                    break;
                case 2:
                    MortarPestleBlockEntity.this.pestleTick = value;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    public MortarPestleBlockEntity(BlockPos pos, BlockState state){
        super(MORTAR_PESTLE_ENTITY.get(), pos, state);
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
        if(!(entity instanceof MortarPestleBlockEntity blockEntity))return;

        // Client/Server Side actions
        //
        if(blockEntity.pestleTick > 0 && blockEntity.isPestling){
            blockEntity.pestleTick--;
        }

        // Server Side actions
        //
        if(level.isClientSide()){return;}

        // Manage block state for MortarPestleBlock
        if (!(state.getBlock() instanceof MortarPestleBlock)) return;
        if(blockEntity.isPestling && blockEntity.pestleTick > 0) return;

        BlockState blockState = state;
        if(blockEntity.pestleCount >= PESTLE_TOTAL_COUNT){
            blockState = blockState.setValue(MortarPestleBlock.INGREDIENTS, 5);
        }
        else if(blockEntity.pestleCount == PESTLE_TOTAL_COUNT-1){
            blockState = blockState.setValue(MortarPestleBlock.INGREDIENTS, 4);
        }
        else if(blockEntity.pestleCount == PESTLE_TOTAL_COUNT-2){
            blockState = blockState.setValue(MortarPestleBlock.INGREDIENTS, 3);
        }
        else if (!blockEntity.getResultItem().isEmpty()) {
            blockState = blockState.setValue(MortarPestleBlock.INGREDIENTS, 2);
        }
        else if (blockEntity.hasItemInInventory()) {
            blockState = blockState.setValue(MortarPestleBlock.INGREDIENTS, 1);
        }
        else{
            blockState = blockState.setValue(MortarPestleBlock.INGREDIENTS, 0);
        }
        level.setBlock(pos, blockState, Block.UPDATE_CLIENTS);
    }

    //
    // Try to match ingredients with a possible recipe, if found then
    // return the recipe's resulting ItemStack. If none was found, return
    // an empty Stack.
    //
    public Optional<RecipeHolder<MortarPestleRecipe>> findRecipe(){
        if(this.level == null) return Optional.empty();

        // Remove all empty space (air) to match with recipes.
        NonNullList<ItemStack> noAirList = NonNullList.create();
        for(int i=0; i<this.getItemContainer().getSlots();i++){
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
        if(this.level == null) return;

        this.pestleTick = 0;
        this.isPestling = true;
        this.setResultItem(this.findRecipe()
                .map(RecipeHolder::value)
                .map(MortarPestleRecipe::getResultItem)
                .orElse(ItemStack.EMPTY));

        this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
    }

    public void finishPestling(Player player, Level level){

        ItemStack itemStackInHand = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack useItem = findRecipe().map(RecipeHolder::value).get().getUseItem().orElse(ItemStack.EMPTY);
        List<ItemStack> ingredientsCache = new ArrayList<>();

        if(itemStackInHand.getItem() == useItem.getItem() || useItem.isEmpty()) {
            if(level.isClientSide()) return;

            for (int i=0; i<this.getItemContainer().getSlots(); i++){
                ingredientsCache.add(this.getItemContainer().extractItem(i, 1, false));
            }

            // If item is a blend, handle affinity stored items
            if(this.resultItem.getItem() instanceof BlendItem) {
                List<Holder<Item>> stored_items = new ArrayList<>();
                for (ItemStack item : ingredientsCache) {
                    if (item.has(DataComponentsRegistry.POSSIBLE_AFFINITIES)) stored_items.add(item.getItemHolder());
                }
                this.resultItem.set(DataComponentsRegistry.AFFINITY_STORED_ITEMS, stored_items);
            }

            if(!useItem.isEmpty()){
                itemStackInHand.consume(1, player);
            }
            if (!player.addItem(this.getResultItem())) {
                Block.popResource(this.level, this.getBlockPos(), this.getResultItem());
            }
            this.isPestling = false;
            this.pestleCount = 0;
            this.setResultItem(this.findRecipe()
                    .map(RecipeHolder::value)
                    .map(MortarPestleRecipe::getResultItem)
                    .orElse(ItemStack.EMPTY)
            );
            level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
        }
        else{
            Minecraft.getInstance().gui.setOverlayMessage(
                    Component.translatable("message.toxony.mortar.warning", Component.translatable(useItem.getDescriptionId())),
                    false
            );
        }
    }

    public void setResultItem(ItemStack item){
        this.resultItem = item.copy();
    }

    public boolean hasItemInInventory(){
        for(int i=0;i<getItemContainer().getSlots();i++){
            if (!getItemContainer().getStackInSlot(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public ItemStack getResultItem(){
        return resultItem.copy();
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
        this.isPestling = tag.getBoolean("mortar_pestling");
        this.pestleCount = tag.getInt("mortar_pestle_count");
        this.pestleTick = tag.getInt("mortar_pestle_tick");

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
        tag.putBoolean("mortar_pestling", this.isPestling);
        tag.putInt("mortar_pestle_count", this.pestleCount);
        tag.putInt("mortar_pestle_tick", this.pestleTick);

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
        return Component.translatable("block.toxony.mortar_pestle");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new MortarPestleMenu(id, playerInventory, this, this.data, ContainerLevelAccess.create(player.level(), worldPosition));
    }
}
