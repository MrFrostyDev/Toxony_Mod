package xyz.yfrostyf.toxony.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import xyz.yfrostyf.toxony.ToxonyConfig;
import xyz.yfrostyf.toxony.api.affinity.Affinity;
import xyz.yfrostyf.toxony.api.tox.ToxData;
import xyz.yfrostyf.toxony.api.util.AffinityUtil;
import xyz.yfrostyf.toxony.network.SyncToxDataPacket;
import xyz.yfrostyf.toxony.registries.DataAttachmentRegistry;

import java.util.Collection;
import java.util.Map;

import static xyz.yfrostyf.toxony.registries.DataAttachmentRegistry.TOX_DATA;

public class ToxCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        LiteralCommandNode<CommandSourceStack> toxCommand = dispatcher.register(Commands.literal("tox")
                .requires((p) -> p.hasPermission(2))
                .then(Commands.literal("set")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.argument("amount", IntegerArgumentType.integer())
                                        .executes((context) -> changeTox(context.getSource(), EntityArgument.getPlayers(context, "targets"), IntegerArgumentType.getInteger(context, "amount"), true)))))
                .then(Commands.literal("add")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.argument("amount", IntegerArgumentType.integer())
                                        .executes((context) -> changeTox(context.getSource(), EntityArgument.getPlayers(context, "targets"), IntegerArgumentType.getInteger(context, "amount"), false)))))
                .then(Commands.literal("discover")
                        .then(Commands.argument("targets", EntityArgument.player())
                                .then(Commands.argument("isRevealOrHide", IntegerArgumentType.integer(0, 1))
                                        .executes((context -> discoverAffinities(context.getSource(), EntityArgument.getPlayer(context, "targets"), IntegerArgumentType.getInteger(context, "isRevealOrHide")))))))
                .then(Commands.literal("get")
                        .then(Commands.argument("targets", EntityArgument.player())
                                .executes((context) -> getTox(context.getSource(), EntityArgument.getPlayer(context, "targets"))))));
        LiteralCommandNode<CommandSourceStack> tolCommand = dispatcher.register(Commands.literal("tolerance")
                .requires((p) -> p.hasPermission(2))
                .then(Commands.literal("set")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.argument("amount", IntegerArgumentType.integer())
                                        .executes((context) -> changeTolerance(context.getSource(), EntityArgument.getPlayers(context, "targets"), IntegerArgumentType.getInteger(context, "amount"), true)))))
                .then(Commands.literal("add")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.argument("amount", IntegerArgumentType.integer())
                                        .executes((context) -> changeTolerance(context.getSource(), EntityArgument.getPlayers(context, "targets"), IntegerArgumentType.getInteger(context, "amount"), false))))));
 };

    private static int changeTox(CommandSourceStack source, Collection<ServerPlayer> targets, int amount, boolean isSet) {
        targets.forEach((svplayer -> {
            ToxData plyToxData = svplayer.getData(DataAttachmentRegistry.TOX_DATA);
            var base = isSet ? 0 : plyToxData.getTox();
            plyToxData.setTox(amount + base);
            PacketDistributor.sendToPlayer(svplayer, SyncToxDataPacket.create(plyToxData));
        }));

        String setString = isSet ? "set" : "add";
        if (targets.size() == 1) {
            source.sendSuccess(() -> Component.translatable("commands.tox." + setString + ".success", targets.iterator().next().getDisplayName(), amount), true);
        } else {
            source.sendSuccess(() -> Component.translatable("commands.tox." + setString + ".success", targets.size(), amount), true);
        }

        return targets.size();
    }

    private static int changeTolerance(CommandSourceStack source, Collection<ServerPlayer> targets, int amount, boolean isSet) {
        targets.forEach((svplayer -> {
            ToxData plyToxData = svplayer.getData(DataAttachmentRegistry.TOX_DATA);
            var base = isSet ? 0 : plyToxData.getTolerance();
            plyToxData.setTolerance(amount + base);
            PacketDistributor.sendToPlayer(svplayer, SyncToxDataPacket.create(plyToxData));
        }));

        String setString = isSet ? "set" : "add";
        if (targets.size() == 1) {
            source.sendSuccess(() -> Component.translatable("commands.tolerance." + setString + ".success", targets.iterator().next().getDisplayName(), amount), true);
        } else {
            source.sendSuccess(() -> Component.translatable("commands.tolerance." + setString + ".success", targets.size(), amount), true);
        }

        return targets.size();
    }

    private static int getTox(CommandSourceStack source, ServerPlayer svplayer) {
        ToxData plyToxData = svplayer.getData(TOX_DATA);
        String setString = plyToxData.getDeathState() ? "death." : "";
        source.sendSuccess(() -> Component.translatable("commands.tox.get."+ setString + "success", svplayer.getDisplayName(), plyToxData.getTox(), plyToxData.getTolerance()), true);
        source.sendSuccess(() -> Component.translatable("commands.tox.get.success.affinities", svplayer.getDisplayName(), plyToxData.getAffinities().toString()), true);
        source.sendSuccess(() -> Component.translatable("commands.tox.get.success.mutagens", svplayer.getDisplayName(), plyToxData.getMutagens().toString()), true);
        source.sendSuccess(() -> Component.translatable("commands.tox.get.success.known_ingredients", svplayer.getDisplayName(), plyToxData.getKnownIngredients().toString()), true);

        return (int) plyToxData.getTox();
    }

    private static int discoverAffinities(CommandSourceStack source, ServerPlayer svplayer, int isRevealOrHide) {
        ToxData plyToxData = svplayer.getData(TOX_DATA);
        boolean isRevealed = isRevealOrHide != 0;

        if(isRevealed){
            Level level = svplayer.level();
            Map<ResourceLocation, Affinity> map = AffinityUtil.getIngredientAffinityMap(level);

            for(Map.Entry<ResourceLocation, Affinity> entry: map.entrySet()){
                Item item = BuiltInRegistries.ITEM.get(entry.getKey());
                if(item.equals(Items.AIR)) continue;
                plyToxData.addKnownIngredients(item.getDefaultInstance(), ToxonyConfig.MIN_KNOWLEDGE_REQ.get());
            }
        }
        else{
            plyToxData.clearKnownIngredients();
        }


        PacketDistributor.sendToPlayer(svplayer, SyncToxDataPacket.create(plyToxData));
        String setString = isRevealed ? "reveal" : "hide";
        source.sendSuccess(() -> Component.translatable("commands.tox.discover." + setString, svplayer.getDisplayName()), true);
        return 0;
    }
}
