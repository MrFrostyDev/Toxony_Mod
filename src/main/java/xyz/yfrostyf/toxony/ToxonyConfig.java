package xyz.yfrostyf.toxony;

import net.neoforged.neoforge.common.ModConfigSpec;

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
}
