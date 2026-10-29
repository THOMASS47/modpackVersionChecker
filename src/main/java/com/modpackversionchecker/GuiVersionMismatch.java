package com.modpackversionchecker;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;

/**
 * Why is this needed just to have a vertically centered 2 button gui with text
 */
public final class GuiVersionMismatch extends GuiScreen {

    private static final int JOIN_ANYWAY = 0;
    private static final int BACK = 1;
    private static final int BUTTON_HEIGHT = 20;
    private static final int BUTTON_GAP = 20;

    private final GuiScreen parent;
    private final ServerData server;
    private final String[] lines;
    private int messageY;

    public GuiVersionMismatch(GuiScreen parent, ServerData server, String serverVersion) {
        this.parent = parent;
        this.server = server;
        this.lines = VersionCheck.mismatchMessage(Config.modpackVersion, serverVersion)
            .split("\n", -1);
    }

    @Override
    public void initGui() {
        buttonList.clear();
        int messageHeight = lines.length * fontRendererObj.FONT_HEIGHT;
        int contentHeight = messageHeight + BUTTON_GAP + BUTTON_HEIGHT;
        messageY = (height - contentHeight) / 2;
        int buttonY = messageY + messageHeight + BUTTON_GAP;
        buttonList
            .add(new GuiButton(JOIN_ANYWAY, width / 2 - 155, buttonY, 150, BUTTON_HEIGHT, "I know what I'm doing"));
        buttonList.add(new GuiButton(BACK, width / 2 + 5, buttonY, 150, BUTTON_HEIGHT, I18n.format("gui.back")));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == JOIN_ANYWAY) {
            ClientConnectionWarning.connectAnyway(parent, server);
        } else if (button.id == BACK) {
            mc.displayGuiScreen(parent);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == 1) {
            mc.displayGuiScreen(parent);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        int y = messageY;
        for (String line : lines) {
            drawCenteredString(fontRendererObj, line, width / 2, y, 0xFFFFFF);
            y += fontRendererObj.FONT_HEIGHT;
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
