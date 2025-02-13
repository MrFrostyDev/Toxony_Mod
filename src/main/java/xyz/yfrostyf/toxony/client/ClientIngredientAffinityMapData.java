package xyz.yfrostyf.toxony.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.data.world.IngredientAffinityMapData;

import java.util.Optional;

@Mod(value = ToxonyMain.MOD_ID, dist = Dist.CLIENT)
public class ClientIngredientAffinityMapData {
    private static Optional<IngredientAffinityMapData> ingredientAffinityMapData = Optional.empty();

    public static void setData (IngredientAffinityMapData IngredientAffinityMapData){
        ingredientAffinityMapData = Optional.of(IngredientAffinityMapData);
    }

    public static Optional<IngredientAffinityMapData> getData(){
        return ingredientAffinityMapData;
    }
}
