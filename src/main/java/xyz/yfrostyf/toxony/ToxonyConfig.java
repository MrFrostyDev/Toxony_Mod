package xyz.yfrostyf.toxony;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.ModConfigSpec;
import xyz.yfrostyf.toxony.api.registries.ToxonyRegistries;

import java.util.List;
import java.util.function.Predicate;

public class ToxonyConfig {

    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.ConfigValue<Double> TOXIN_DRAIN;
    public static final ModConfigSpec.ConfigValue<Double> PLANT_GROWTH_MULT;

    public static final ModConfigSpec.ConfigValue<Double> OIL_DURABILITY_MULT;
    public static final ModConfigSpec.ConfigValue<Double> IRON_ROUND_DAMAGE;
    public static final ModConfigSpec.ConfigValue<Double> FLAIL_DAMAGE;
    public static final ModConfigSpec.ConfigValue<Double> FLAIL_SPIN_SPEED_MULT;
    public static final ModConfigSpec.ConfigValue<Double> WITCHINGBLADE_DAMAGE;

    public static final ModConfigSpec.ConfigValue<Integer> MIN_KNOWLEDGE_REQ;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> CUSTOM_ITEM_AFFINITIES;

    static {
        BUILDER.comment("Toxony Server Configurations");

        BUILDER.push("Toxin System");
        BUILDER.comment("");
        BUILDER.comment("How much toxin is removed from your system every few seconds. (Default: 1.0)");
        BUILDER.comment("If set to negative, gives toxin instead.");
        TOXIN_DRAIN = BUILDER.worldRestart().define("toxinDrain", 1.0);
        BUILDER.pop();

        BUILDER.push("Affinity System");
        BUILDER.comment("");
        BUILDER.comment("The minimum requirement to discover a poisonous ingredient's affinity. (Default: 20)");
        BUILDER.comment("(For reference, eating them gives 1 point of knowledge. Injecting Affinity Solutions give 10)");
        MIN_KNOWLEDGE_REQ = BUILDER.worldRestart().define("minKnowledgeReq", 20);
        BUILDER.comment("");
        BUILDER.comment("Customize which possible affinities appear on any items you define here.");
        BUILDER.comment("");
        BUILDER.comment("WARNING! Only change these values BEFORE WORLD CREATION, as the new item affinities will NOT be discoverable on existing worlds!");
        BUILDER.comment("");
        BUILDER.comment("List of Affinity IDs:\ntoxony:moon,\ntoxony:sun,\ntoxony:ocean,\ntoxony:forest,\ntoxony:wind,\ntoxony:cold,\ntoxony:soul,\ntoxony:decay,\ntoxony:nether,\ntoxony:end,\ntoxony:heat");
        BUILDER.comment("");
        BUILDER.comment("Formatted as [item, affinity.., item, affinity.., ...]");
        BUILDER.comment("");
        BUILDER.comment("Example: \n customItemAffinities = [\n \t\"minecraft:golden_apple\", \"toxony:sun\",\n \t\"modid:random_item\", \"toxony:ocean\", \"toxony:forest\"\n ]");
        CUSTOM_ITEM_AFFINITIES = BUILDER.worldRestart().defineListAllowEmpty("customItemAffinities", List.of(), () -> "", ItemOrAffinityPredicate.create());
        BUILDER.pop();

        BUILDER.push("World System");
        BUILDER.comment("");
        BUILDER.comment("The multiplier for how quickly plants grow. (Default: 1.0)");
        PLANT_GROWTH_MULT = BUILDER.worldRestart().define("plantGrowthMult", 1.0);
        BUILDER.pop();

        BUILDER.push("Items");
        BUILDER.comment("");
        BUILDER.comment("The damage done by iron rounds fired from a flintlock. 2.0 = 1 Heart (Default: 8.0)");
        IRON_ROUND_DAMAGE = BUILDER.worldRestart().define("ironRoundDmg", 8.0);
        BUILDER.comment("");
        BUILDER.comment("The damage the flail does when fully charged. 2.0 = 1 Heart (Default: 15.0)");
        FLAIL_DAMAGE = BUILDER.worldRestart().define("flailDmg", 15.0);
        BUILDER.comment("");
        BUILDER.comment("The multiplier for how fast the flail spins up. Higher = faster (Default: 1.0)");
        FLAIL_SPIN_SPEED_MULT = BUILDER.worldRestart().define("flailSpinSpeedMult", 1.0);
        BUILDER.comment("");
        BUILDER.comment("The damage the witching blade can do. Note, this does not modify its bonus damage. (Default: 7.0)");
        WITCHINGBLADE_DAMAGE = BUILDER.worldRestart().define("witchingBladeDamage", 7.0);
        BUILDER.comment("");
        BUILDER.comment("The multiplier for the amount of times oil can be used before its depleted from the item. (Default: 1.0)");
        OIL_DURABILITY_MULT = BUILDER.worldRestart().define("oilDurabilityMult", 1.0);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    private static class ItemOrAffinityPredicate implements Predicate<Object> {
        public ItemOrAffinityPredicate(){};

        static ItemOrAffinityPredicate create(){
            return new ItemOrAffinityPredicate();
        };

        @Override
        public boolean test(Object o) {
            if(!(o instanceof String s)) return false;

            ResourceLocation resourceLocation = ResourceLocation.tryParse(s);
            if (resourceLocation == null) return false;

            boolean isItem = BuiltInRegistries.ITEM.get(resourceLocation) != Items.AIR;
            boolean isAffinity = ToxonyRegistries.AFFINITY_REGISTRY.getHolder(resourceLocation).isPresent();

            return isItem || isAffinity;
        }
    }
}
