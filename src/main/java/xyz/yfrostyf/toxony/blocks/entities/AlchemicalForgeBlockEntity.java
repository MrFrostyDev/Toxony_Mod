package xyz.yfrostyf.toxony.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import xyz.yfrostyf.toxony.blocks.AlchemicalForgeBlock;
import xyz.yfrostyf.toxony.client.gui.block.AlchemicalForgeMenu;
import xyz.yfrostyf.toxony.recipes.AlchemicalForgeRecipe;
import xyz.yfrostyf.toxony.recipes.inputs.AlchemicalForgeRecipeInput;
import xyz.yfrostyf.toxony.registries.BlockRegistry;
import xyz.yfrostyf.toxony.registries.ItemRegistry;
import xyz.yfrostyf.toxony.registries.RecipeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class AlchemicalForgeBlockEntity extends BlockEntity implements IItemHandler, MenuProvider {
    public static final int FORGE_TOTAL_TIME = 3000;
    public static final int MAX_FUEL = 400;
    public static final int REQUIRED_FUEL = 400;
    private static final int CONTAINER_SIZE = 7;

    public int fuel;
    public int forgeProgress;
    public ItemStackHandler itemContainer;
    public boolean isForging;
    private RecipeType<AlchemicalForgeRecipe> recipeType;

    protected final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> AlchemicalForgeBlockEntity.this.fuel;
                case 1 -> AlchemicalForgeBlockEntity.this.forgeProgress;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    AlchemicalForgeBlockEntity.this.fuel = value;
                    break;
                case 1:
                    AlchemicalForgeBlockEntity.this.forgeProgress = value;
                    break;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public AlchemicalForgeBlockEntity(BlockPos pos, BlockState state){
        super(BlockRegistry.ALCHEMICAL_FORGE_ENTITY.get(), pos, state);
        this.fuel = 0;
        this.forgeProgress = 0;
        this.isForging = false;
        itemContainer = new ItemStackHandler(CONTAINER_SIZE){
            @Override
            public int getSlotLimit(int slot) {
                if(slot == 3 || slot == 4 || slot == 5) return 1;
                return Item.DEFAULT_MAX_STACK_SIZE;
            }
        };
    }

    //
    // |--------------------------Methods--------------------------|
    //


    public static void tick(Level level, BlockPos pos, BlockState state, BlockEntity entity) {
        if(!(entity instanceof AlchemicalForgeBlockEntity blockEntity))return;

        // If block is just its right side then do visuals instead
        if(state.getValue(AlchemicalForgeBlock.PART) == ChestType.RIGHT){
            Direction directionToBody = AlchemicalForgeBlock.getNeighbourDirection(state.getValue(AlchemicalForgeBlock.PART), state.getValue(AlchemicalForgeBlock.FACING));
            BlockPos neighbourBlockPos = pos.relative(directionToBody);
            if(level.getBlockEntity(neighbourBlockPos) instanceof AlchemicalForgeBlockEntity neighbourBlockEntity){
                if(neighbourBlockEntity.isForging) {
                    state = state.setValue(AlchemicalForgeBlock.LIT, Boolean.valueOf(true));
                }
                else{
                    state = state.setValue(AlchemicalForgeBlock.LIT, Boolean.valueOf(false));
                }
                level.setBlock(pos, state, AlchemicalForgeBlock.UPDATE_CLIENTS);
                return;
            }
        }

        // Server Side actions
        if(level.isClientSide()){return;}

        ItemStack inputOutput = blockEntity.itemContainer.getStackInSlot(0);
        ItemStack aux1 = blockEntity.itemContainer.getStackInSlot(1);
        ItemStack aux2 = blockEntity.itemContainer.getStackInSlot(2);
        ItemStack solution3 = blockEntity.itemContainer.getStackInSlot(3);
        ItemStack solution4 = blockEntity.itemContainer.getStackInSlot(4);
        ItemStack solution5 = blockEntity.itemContainer.getStackInSlot(5);
        ItemStack fuel = blockEntity.itemContainer.getStackInSlot(6);

        // Set fuel when toxin is placed
        if (blockEntity.fuel < MAX_FUEL && fuel.is(ItemRegistry.TOXIN)) {
            int stackcount = blockEntity.itemContainer.extractItem(6, ItemRegistry.TOXIN.get().getDefaultMaxStackSize(), false).getCount();

            blockEntity.itemContainer.setStackInSlot(6, new ItemStack(ItemRegistry.GLASS_VIAL, stackcount));
            blockEntity.addFuel(50 * stackcount);
            level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
        }

        // Process when alchemical forge has enough fuel
        if (blockEntity.fuel >= REQUIRED_FUEL && blockEntity.isForging) {
            // If alchemical forge has a result item, check if its finished forging or not
            if(blockEntity.forgeProgress < FORGE_TOTAL_TIME){
                blockEntity.forgeProgress = Mth.clamp(blockEntity.forgeProgress + 1, 0, FORGE_TOTAL_TIME);
            }
            else{
                // check if any recipes in input match a recipe or potion
                Optional<RecipeHolder<AlchemicalForgeRecipe>> optionalRecipe = blockEntity.findRecipe();
                NonNullList<ItemStack> solutionItems = NonNullList.createWithCapacity(3);
                solutionItems.add(solution3);
                solutionItems.add(solution4);
                solutionItems.add(solution5);

                List<ItemStack> auxiliaryItems = new ArrayList<>(2);
                auxiliaryItems.add(aux1);
                auxiliaryItems.add(aux2);

                ItemStack newResultItem = new ItemStack(ItemRegistry.TOXIC_PASTE);
                if(optionalRecipe.isPresent()){
                    newResultItem = optionalRecipe.get().value().assemble(
                            new AlchemicalForgeRecipeInput(inputOutput, solutionItems, auxiliaryItems),
                            level.registryAccess());
                }

                for(int i=1; i<6; i++){
                    blockEntity.itemContainer.setStackInSlot(i, ItemStack.EMPTY);
                }

                // Check if a solution was used for each slot, if so return an empty vial to that slot
                for(int i=0; i<solutionItems.size(); i++){
                    if(!solutionItems.get(i).isEmpty()){
                        blockEntity.itemContainer.setStackInSlot(i+3, new ItemStack(ItemRegistry.GLASS_VIAL.get()));
                    }
                }

                blockEntity.itemContainer.setStackInSlot(0, newResultItem);
                blockEntity.resetAlchemicalForge();
            }
            level.sendBlockUpdated(pos, blockEntity.getBlockState(), blockEntity.getBlockState(), Block.UPDATE_CLIENTS);
        }

        // For visuals when its forging
        if (blockEntity.isForging) {
            state = state.setValue(AlchemicalForgeBlock.LIT, Boolean.valueOf(true));
            level.setBlock(pos, state, AlchemicalForgeBlock.UPDATE_ALL);
        }
        else{
            state = state.setValue(AlchemicalForgeBlock.LIT, Boolean.valueOf(false));
            level.setBlock(pos, state, AlchemicalForgeBlock.UPDATE_ALL);
        }
    }

    public Optional<RecipeHolder<AlchemicalForgeRecipe>> findRecipe() {
        if (this.level == null) return Optional.empty();
        if(this.itemContainer.getStackInSlot(0).isEmpty()) return Optional.empty();

        ItemStack inputItem = this.itemContainer.getStackInSlot(0);

        NonNullList<ItemStack> solutionItems = NonNullList.createWithCapacity(3);
        solutionItems.add(this.itemContainer.getStackInSlot(3));
        solutionItems.add(this.itemContainer.getStackInSlot(4));
        solutionItems.add(this.itemContainer.getStackInSlot(5));

        List<ItemStack> auxiliaryItems = new ArrayList<>(2);
        auxiliaryItems.add(this.itemContainer.getStackInSlot(1));
        auxiliaryItems.add(this.itemContainer.getStackInSlot(2));

        AlchemicalForgeRecipeInput input = new AlchemicalForgeRecipeInput(inputItem, solutionItems, auxiliaryItems);
        return this.level.getRecipeManager().getRecipeFor(
                RecipeRegistry.ALCHEMICAL_FORGE_RECIPE.get(),
                input,
                this.level);
    }

    public void startAlchemicalForge(){
        if(this.level == null) return;
        this.forgeProgress = 0;
        this.isForging = true;
        this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
    }

    public void resetAlchemicalForge(){
        this.fuel = this.fuel - REQUIRED_FUEL;
        this.forgeProgress = 0;
        this.isForging = false;
    }

    public ItemStackHandler getItemContainer(){
        return itemContainer;
    }

    public void addFuel(int amount){
        if(this.fuel + amount > MAX_FUEL){
            this.fuel = MAX_FUEL;
            return;
        }
        this.fuel = this.fuel + amount;
    }

    public boolean hasFuel() {
        return this.fuel > 0;
    }

    public float getFuelPercentage(){
        return Mth.clamp((float)this.data.get(0) / (float)MAX_FUEL, 0.0F, 1.0F);
    }

    public float getProgress(){
        return Mth.clamp((float)this.data.get(1) / (float)FORGE_TOTAL_TIME, 0.0F, 1.0F);
    }

    public boolean canForge(){
        boolean hasSolution = false;
        for(int i=3; i<6; i++){
            if(!itemContainer.getStackInSlot(i).isEmpty()){
                hasSolution = true;
                break;
            }
        }
        return  hasSolution && !itemContainer.getStackInSlot(0).isEmpty() && fuel >= REQUIRED_FUEL;
    }

    private boolean isProgressing() {
        return this.forgeProgress > 0;
    }

    //
    // |-----------------------Data/Network Handling Methods-----------------------|
    //

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        NonNullList<ItemStack> list = NonNullList.create();
        for(int i=0; i<CONTAINER_SIZE;i++){
            list.add(i, itemContainer.getStackInSlot(i).copy());
        }

        ContainerHelper.saveAllItems(tag, list, registries);

        tag.putInt("Fuel", this.fuel);
        tag.putInt("ProgressTime", this.forgeProgress);
        tag.putBoolean("IsForging", this.isForging);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        NonNullList<ItemStack> list = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, list, registries);

        int i = 0;
        while(i<CONTAINER_SIZE){
            this.itemContainer.setStackInSlot(i, list.get(i));
            i++;
        }
        this.fuel = tag.getInt("Fuel");
        this.forgeProgress = tag.getInt("ProgressTime");
        this.isForging = tag.getBoolean("IsForging");
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
        ItemStack targetStack = this.itemContainer.getStackInSlot(slot).copy();
        ItemStack returnItem;
        if(targetStack.getCount() > amount){
            returnItem = targetStack.copy();
            returnItem.setCount(amount);
            targetStack.setCount(targetStack.getCount()-amount);
        }
        else{
            returnItem = targetStack.copy();
            this.itemContainer.setStackInSlot(slot, ItemStack.EMPTY);
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
        return Component.translatable("block.toxony.alchemical_forge");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new AlchemicalForgeMenu(id, playerInventory, this, this.data, ContainerLevelAccess.create(player.level(), worldPosition));
    }
}
