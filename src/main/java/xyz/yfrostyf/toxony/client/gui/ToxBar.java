package xyz.yfrostyf.toxony.client.gui;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;
import top.theillusivec4.curios.api.CuriosApi;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.client.ClientToxData;
import xyz.yfrostyf.toxony.registries.ItemRegistry;

public class ToxBar implements LayeredDraw.Layer {
    public static final ResourceLocation RESOURCE = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "textures/gui/tox_bars.png");

    static final int TEXTURE_WIDTH = 77;
    static final int TEXTURE_HEIGHT = 108;

    // |================= Bar UVs =================|

    static final int BAR_WIDTH = 62;
    static final int BAR_HEIGHT = 9;

    static final int BAR_UOFFSET = 0;

    static final int BAR1_TOX_VOFFSET = 0;
    static final int BAR2_TOX_VOFFSET = 27;
    static final int BAR3_TOX_VOFFSET = 54;
    static final int BAR4_TOX_VOFFSET = 81;

    static final int BAR1_TOL_VOFFSET = 9;
    static final int BAR2_TOL_VOFFSET = 36;
    static final int BAR3_TOL_VOFFSET = 63;
    static final int BAR4_TOL_VOFFSET = 90;

    static final int BAR1_BACK_VOFFSET = 18;
    static final int BAR2_BACK_VOFFSET = 45;
    static final int BAR3_BACK_VOFFSET = 72;
    static final int BAR4_BACK_VOFFSET = 99;

    // |================= Skull UVs =================|

    static final int SKULL_WIDTH = 9;
    static final int SKULL_HEIGHT = 21;

    static final int SKULL_UOFFSET = 67;

    static final int SKULL_WHITE_VOFFSET = 0;
    static final int SKULL_RED_VOFFSET = 21;

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Player player = Minecraft.getInstance().player;

        if(player == null)return;
        if(Minecraft.getInstance().options.hideGui)return;
        if(player.isSpectator())return;
        if(!isToxGaugeOnPlayer(player))return;
        if(ClientToxData.getToxData() == null)return;

        float tox = ClientToxData.getToxData().getTox();
        float tolerance = ClientToxData.getToxData().getTolerance();
        float thresholdGoal = ClientToxData.getToxData().getThresholdTolGoal();
        boolean deathState = ClientToxData.getToxData().getDeathState();

        int BarBackVOffset, BarTolVOffset, BarToxVOffset;
        switch (ClientToxData.getToxData().getThreshold()) {
            case 0:
                BarBackVOffset = BAR1_BACK_VOFFSET;
                BarTolVOffset = BAR1_TOL_VOFFSET;
                BarToxVOffset = BAR1_TOX_VOFFSET;
                break;
            case 1:
                BarBackVOffset = BAR2_BACK_VOFFSET;
                BarTolVOffset = BAR2_TOL_VOFFSET;
                BarToxVOffset = BAR2_TOX_VOFFSET;
                break;
            case 2:
                BarBackVOffset = BAR3_BACK_VOFFSET;
                BarTolVOffset = BAR3_TOL_VOFFSET;
                BarToxVOffset = BAR3_TOX_VOFFSET;
                break;
            default:
                BarBackVOffset = BAR4_BACK_VOFFSET;
                BarTolVOffset = BAR4_TOL_VOFFSET;
                BarToxVOffset = BAR4_TOX_VOFFSET;
        }


        int toxBarWidth = Mth.ceil(BAR_WIDTH * Math.min(tox/thresholdGoal, 1));
        int tolBarWidth = Mth.ceil(BAR_WIDTH * Math.min(tolerance/thresholdGoal, 1));

        int posX = guiGraphics.guiWidth()/2 - 88;
        int posY = guiGraphics.guiHeight() - 72;

        // Back Bar | (ResourceLocation atlasLocation, int x, int y, float uOffset, float vOffset, int width, int height, int textureWidth, int textureHeight)
        guiGraphics.blit(RESOURCE,
                posX-2, posY,
                BAR_UOFFSET, BarBackVOffset,
                BAR_WIDTH+4, BAR_HEIGHT,
                TEXTURE_WIDTH, TEXTURE_HEIGHT);

        // Tol Bar
        guiGraphics.blit(RESOURCE,
                posX, posY,
                BAR_UOFFSET, BarTolVOffset,
                tolBarWidth, BAR_HEIGHT,
                TEXTURE_WIDTH, TEXTURE_HEIGHT);

        // Tox Bar
        guiGraphics.blit(RESOURCE,
                posX, posY,
                BAR_UOFFSET, BarToxVOffset,
                toxBarWidth, BAR_HEIGHT,
                TEXTURE_WIDTH, TEXTURE_HEIGHT);

        // Skull
        guiGraphics.blit(RESOURCE,
                Math.min(Math.max(posX+toxBarWidth-4, posX-2), posX+BAR_WIDTH-1), posY-6,
                SKULL_UOFFSET, deathState ? SKULL_RED_VOFFSET : SKULL_WHITE_VOFFSET,
                SKULL_WIDTH, SKULL_HEIGHT,
                TEXTURE_WIDTH, TEXTURE_HEIGHT);

    }

    private static boolean isToxGaugeOnPlayer(Player player){
        boolean isHolding = player.isHolding(ItemRegistry.TOX_GAUGE.get());
        boolean isInCurios = false;

        if(ModList.get().isLoaded("curios") && CuriosApi.getCuriosInventory(Minecraft.getInstance().player).isPresent()){
            isInCurios = CuriosApi.getCuriosInventory(Minecraft.getInstance().player).get().isEquipped(ItemRegistry.TOX_GAUGE.get());
        }

        return isHolding || isInCurios;
    }
}
