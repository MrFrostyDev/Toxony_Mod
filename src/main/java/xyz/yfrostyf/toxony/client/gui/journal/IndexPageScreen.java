package xyz.yfrostyf.toxony.client.gui.journal;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;

import java.util.Map;

public class IndexPageScreen extends TextPageScreen {
    protected final Map<String, String> indexedPages;
    protected final Font font;

    protected IndexPageScreen(String translateID, Map<String, String> indexedPages, int indexID, JournalPages journalPages) {
        super(translateID, indexID, journalPages);
        this.indexedPages = indexedPages;
        this.font = Minecraft.getInstance().font;
    }

    @Override
    protected void init() {
        super.init();

        int i = 0;
        for(Map.Entry<String, String> entry : indexedPages.entrySet()){
            Component component = Component.translatable(entry.getKey());
            Button.OnPress onPress = b -> {
                journalPages.setPage(entry.getValue());
                journalPages.updatePage();
            };

                this.addRenderableWidget(new IndexTextButton(this.width/2 - 56, this.height/2 - 30 + i * (font.lineHeight + 1), component, onPress, this.font));
            i++;
        }
    }

    // Based on Minecraft's PlainTextButton
    public static class IndexTextButton extends Button{
        private final Font font;
        private final Component message;
        private final Component underlinedMessage;

        public IndexTextButton(int x, int y, Component message, OnPress onPress, Font font) {
            super(x, y, font.width(message), font.lineHeight, message, onPress, DEFAULT_NARRATION);
            this.font = font;
            this.message = ComponentUtils.mergeStyles(message.copy(), Style.EMPTY.withBold(true).withColor(0xcebd81));
            this.underlinedMessage = ComponentUtils.mergeStyles(message.copy(), Style.EMPTY.withBold(true).withUnderlined(true).withColor(0xcebd81));
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            Component component = this.isHoveredOrFocused() ? this.underlinedMessage : this.message;
            guiGraphics.drawString(this.font, component, this.getX(), this.getY(), 11184810 | Mth.ceil(this.alpha * 255.0F) << 24, false);
        }
    }

}
