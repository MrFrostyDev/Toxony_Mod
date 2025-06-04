package xyz.yfrostyf.toxony.api.util;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;

import java.util.Optional;

/**
 * Utility code to handle compatibility with other mods including their tags, and methods to handle their interactions with Toxony.
 */
public class CompatibilityUtil {

    public static final String VAMPIRISM = "vampirism";
    public static final TagKey<EntityType<?>> VAMPIRE = entityTag(VAMPIRISM, "vampire");

    public static final String WEREWOLVES = "werewolves";
    public static final TagKey<EntityType<?>> WEREWOLF = entityTag(WEREWOLVES, "werewolf");

    public static final String IRON_SPELLS = "irons_spellbooks";

    public static boolean isModLoaded(String modid){
        return ModList.get().isLoaded(modid);
    }

    public static float modifyDamageFromSilver(Entity entity, float damage){
        float modifiedDamage = damage;

        if(entity.getType().is(VAMPIRE) || entity.getType().is(WEREWOLF)){
            modifiedDamage *= 1.5F;
        }

        return modifiedDamage;
    }

    public static Optional<Holder.Reference<Attribute>> getModAttribute(Level level, String modid, String path){
        if(ModList.get().isLoaded(modid)){
            return level.registryAccess().registryOrThrow(Registries.ATTRIBUTE).getHolder(ResourceLocation.fromNamespaceAndPath(modid, path));
        }
        else{
            return Optional.empty();
        }
    }

    public static TagKey<EntityType<?>> entityTag(String id, String name){
        return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(id, name));
    }
}
