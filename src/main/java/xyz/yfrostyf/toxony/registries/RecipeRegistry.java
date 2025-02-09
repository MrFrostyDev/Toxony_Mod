package xyz.yfrostyf.toxony.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.recipes.AlembicRecipe;
import xyz.yfrostyf.toxony.recipes.CrucibleRecipe;
import xyz.yfrostyf.toxony.recipes.MortarPestleRecipe;

import java.util.function.Supplier;

public class RecipeRegistry {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, ToxonyMain.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, ToxonyMain.MOD_ID);

    public static void register(IEventBus event){
        RECIPE_TYPES.register(event);
        RECIPE_SERIALIZERS.register(event);
    }

    // Recipe Types
    public static final Supplier<RecipeType<MortarPestleRecipe>> MORTAR_PESTLE_RECIPE = RECIPE_TYPES.register(
            "mortar_pestle",
            () -> RecipeType.<MortarPestleRecipe>simple(
                    ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "mortar_pestle")
            )
    );

    public static final Supplier<RecipeType<CrucibleRecipe>> CRUCIBLE_RECIPE = RECIPE_TYPES.register(
            "crucible",
            () -> RecipeType.<CrucibleRecipe>simple(
                    ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "crucible")
            )
    );

    public static final Supplier<RecipeType<AlembicRecipe>> ALEMBIC_RECIPE = RECIPE_TYPES.register(
            "alembic",
            () -> RecipeType.<AlembicRecipe>simple(
                    ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "alembic")
            )
    );


    // Recipe Serializers
    public static final Supplier<RecipeSerializer<?>> MORTAR_PESTLE_SERIALIZER = RECIPE_SERIALIZERS.register(
            "mortar_pestle",
            MortarPestleRecipe.Serializer::new);

    public static final Supplier<RecipeSerializer<?>> CRUCIBLE_SERIALIZER = RECIPE_SERIALIZERS.register(
            "crucible",
            CrucibleRecipe.Serializer::new);

    public static final Supplier<RecipeSerializer<?>> ALEMBIC_SERIALIZER = RECIPE_SERIALIZERS.register(
            "alembic",
            AlembicRecipe.Serializer::new);

}
