package xyz.yfrostyf.toxony.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import xyz.yfrostyf.toxony.blocks.AlembicBlock;
import xyz.yfrostyf.toxony.client.gui.AlembicMenu;
import xyz.yfrostyf.toxony.recipes.AlembicRecipe;
import xyz.yfrostyf.toxony.recipes.inputs.PairCombineRecipeInput;
import xyz.yfrostyf.toxony.registries.BlockRegistry;
import xyz.yfrostyf.toxony.registries.DataComponentsRegistry;
import xyz.yfrostyf.toxony.registries.RecipeRegistry;

import java.util.Optional;


public class AlembicBlockEntity extends BlockEntity implements IItemHandler, MenuProvider {
    public static final int MAX_FUEL = 10000;
    public static final int POTION_BOIL_TIME = 400;
    private static final int CONTAINER_SIZE = 3;

    public int fuel;
    public int boilProgress;
    public int boilTotalTime;
    public ItemStack resultItem;
    public ItemStack returnStack;
    public ItemStackHandler itemContainer;
    private RecipeType<AlembicRecipe> recipeType;

    protected final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> AlembicBlockEntity.this.fuel;
                case 1 -> AlembicBlockEntity.this.boilProgress;
                case 2 -> AlembicBlockEntity.this.boilTotalTime;
                default -> 0;
            };
        }
        
        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    AlembicBlockEntity.this.fuel = value;
                    break;
                case 1:
                    AlembicBlockEntity.this.boilProgress = value;
                    break;
                case 2:
                    AlembicBlockEntity.this.boilTotalTime = value;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    public AlembicBlockEntity(BlockPos pos, BlockState state){
        super(BlockRegistry.ALEMBIC_ENTITY.get(), pos, state);
        this.fuel = 0;
        this.boilProgress = 0;
        this.boilTotalTime = 0;
        resultItem = ItemStack.EMPTY;
        returnStack = ItemStack.EMPTY;
        itemContainer = new ItemStackHandler(CONTAINER_SIZE){
            @Override
            public int getSlotLimit(int slot) {
                // Have to set stack limit here.
                if(slot == 0 || slot == 1)return 1;
                return Item.DEFAULT_MAX_STACK_SIZE;
            }
        };
    }

    //
    // |--------------------------Methods--------------------------|
    //


    public static void tick(Level level, BlockPos pos, BlockState state, BlockEntity entity) {
        if(!(entity instanceof AlembicBlockEntity blockEntity))return;

        // Server Side actions
        if(level.isClientSide()){return;}
        ItemStack output = blockEntity.itemContainer.getStackInSlot(0);
        ItemStack input = blockEntity.itemContainer.getStackInSlot(1);
        ItemStack fuel = blockEntity.itemContainer.getStackInSlot(2);
        ItemStack returnStack = blockEntity.getReturnStack();
        ItemStack result = blockEntity.getResultItem();

        // Set fuel when blaze powder is placed and fuel is empty
        if (!blockEntity.hasFuel() && !fuel.isEmpty()) {
            blockEntity.itemContainer.extractItem(2, 1, false);
            blockEntity.fuel = MAX_FUEL;
        }

        // Process when alembic has fuel
        if (blockEntity.hasFuel()) {
            // If Alembic has a result item, check if its finished boiling or not
            if(!result.isEmpty()){
                if(blockEntity.boilProgress < blockEntity.boilTotalTime){
                    blockEntity.boilProgress = Mth.clamp(blockEntity.boilProgress + 1, 0, blockEntity.boilTotalTime);
                    blockEntity.fuel--;
                }
                else{
                    blockEntity.itemContainer.setStackInSlot(0, result);
                    blockEntity.itemContainer.setStackInSlot(1, returnStack);
                    blockEntity.resetAlembic();
                }
            }
        }
        else{
            blockEntity.boilProgress = Mth.clamp(blockEntity.boilProgress - 2, 0, blockEntity.boilTotalTime);
        }

        if(result.isEmpty()){
            // If result is empty, check if any recipes in input match a recipe or potion
            Optional<RecipeHolder<AlembicRecipe>> optionalRecipe = blockEntity.findRecipe();
            ItemStack resultPotion = AlembicBlockEntity.findPotionAmplify(input);
            if(optionalRecipe.isPresent()){
                AlembicRecipe recipeFound = optionalRecipe.get().value();
                ItemStack newResultItem = recipeFound.assemble(
                        new PairCombineRecipeInput(input, output),
                        level.registryAccess());

                newResultItem = handleNeedleStoredItem(input, newResultItem);
                blockEntity.setResultItem(newResultItem);
                blockEntity.returnStack = recipeFound.getRemainingingItem();
                blockEntity.boilTotalTime = recipeFound.getBoilTime();
            }
            else if(!resultPotion.isEmpty()){
                blockEntity.setResultItem(resultPotion);
                blockEntity.boilTotalTime = POTION_BOIL_TIME;
            }
        }

        // For visuals when boiling
        if (blockEntity.isBoiling()) {
            state = state.setValue(AlembicBlock.LIT, Boolean.valueOf(true));
            level.setBlock(pos, state, AlembicBlock.UPDATE_ALL);
        }
        else{
            state = state.setValue(AlembicBlock.LIT, Boolean.valueOf(false));
            level.setBlock(pos, state, AlembicBlock.UPDATE_ALL);
        }

        level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
    }

    public Optional<RecipeHolder<AlembicRecipe>> findRecipe() {
        if (this.level == null) return Optional.empty();
        ItemStack inputItem = this.itemContainer.getStackInSlot(1);
        ItemStack inputToConvertItem = this.itemContainer.getStackInSlot(0);
        if(this.itemContainer.getStackInSlot(1).isEmpty()) return Optional.empty();

        PairCombineRecipeInput input = new PairCombineRecipeInput(inputItem, inputToConvertItem);
        return this.level.getRecipeManager().getRecipeFor(
                RecipeRegistry.ALEMBIC_RECIPE.get(),
                input,
                this.level);
    }

    private static ItemStack findPotionAmplify(ItemStack stack){
        if(!(stack.getItem() instanceof PotionItem))return ItemStack.EMPTY;

        Optional<Holder<Potion>> optional = stack.getComponents().getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).potion();
        if(optional.isEmpty())return ItemStack.EMPTY;

        // Get potion path name needed for finding strong potion variant
        String potionName = optional.get().unwrapKey().get().location().getPath();

        // Search potion registry for any potions that
        // have "strong_" in front of it (aka the strong variant's path name within namespace)
        for(ResourceLocation resource : BuiltInRegistries.POTION.keySet()){
            Optional<Holder.Reference<Potion>> holder =
                    BuiltInRegistries.POTION.getHolder(ResourceLocation.withDefaultNamespace("strong_" + potionName));

            // If we found one, return a new, stronger variant of the potion.
            if(holder.isPresent()) return PotionContents.createItemStack(stack.getItem(), holder.get());
        }
        return ItemStack.EMPTY;
    }

    private static ItemStack handleNeedleStoredItem(ItemStack inputItem, ItemStack resultItem){
        if(!inputItem.has(DataComponentsRegistry.POSSIBLE_AFFINITIES) || resultItem.has(DataComponentsRegistry.AFFINITY_STORED_ITEM)) return resultItem;
        ItemStack newResultItem = resultItem.copy();
        newResultItem.set(DataComponentsRegistry.AFFINITY_STORED_ITEM, inputItem.getItemHolder());
        return newResultItem;
    }

    public void resetAlembic(){
        this.boilProgress = 0;
        this.returnStack = ItemStack.EMPTY;
        this.setResultItem(ItemStack.EMPTY);
    }

    public void setResultItem(ItemStack item){
        this.resultItem = item.copy();
    }

    public ItemStack getResultItem(){
        return resultItem.copy();
    }

    public ItemStack getReturnStack(){
        return returnStack.copy();
    }

    public ItemStackHandler getItemContainer(){
        return itemContainer;
    }

    public boolean hasFuel() {
        return this.fuel > 0;
    }

    private boolean isBoiling() {
        return this.boilProgress > 0;
    }

    //
    // |-----------------------Data/Network Handling Methods-----------------------|
    //

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        NonNullList<ItemStack> list = NonNullList.create();

        for(int i=0;i<CONTAINER_SIZE;i++){
            list.add(i, itemContainer.getStackInSlot(i).copy()); // store output item
        }
        list.add(returnStack); // store remaining item in sent list
        list.add(resultItem); // store result item in sent list.

        ContainerHelper.saveAllItems(tag, list, registries);

        tag.putInt("Fuel", this.fuel);
        tag.putInt("BoilProgress", this.boilProgress);
        tag.putInt("BoilProgressTime", this.boilTotalTime);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        NonNullList<ItemStack> list = NonNullList.withSize(5, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, list, registries);

        int i = 0;
        while(i<CONTAINER_SIZE){
            this.insertItem(i, list.get(i), false);
            i++;
        }
        this.returnStack = list.get(i); // receive remaining item in sent list.
        this.setResultItem(list.get(i+1)); // receive result item in sent list.

        this.fuel = tag.getInt("Fuel");
        this.boilProgress = tag.getInt("BoilProgress");
        this.boilTotalTime = tag.getInt("BoilProgressTime");
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
        return Component.translatable("block.toxony.alembic");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new AlembicMenu(id, playerInventory, this, this.data, ContainerLevelAccess.create(player.level(), worldPosition));
    }
}
