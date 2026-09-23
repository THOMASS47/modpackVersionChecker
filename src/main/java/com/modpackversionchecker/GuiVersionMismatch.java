package com.modpackversionchecker;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.EnumChatFormatting;

public final class GuiVersionMismatch extends GuiScreen {

    private static final int JOIN_ANYWAY = 0;
    private static final int BACK = 1;

    private final GuiScreen parent;
    private final ServerData server;
    private final String serverVersion;

    public GuiVersionMismatch(GuiScreen parent, ServerData server, String serverVersion) {
        this.parent = parent;
        this.server = server;
        this.serverVersion = serverVersion;
    }

    @Override
    public void initGui() {
        buttonList.clear();
        int buttonY = height / 6 + 110;
        buttonList.add(new GuiButton(JOIN_ANYWAY, width / 2 - 155, buttonY, 150, 20, "I know what I'm doing"));
        buttonList.add(new GuiButton(BACK, width / 2 + 5, buttonY, 150, 20, "Back"));
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
        drawCenteredString(
            fontRendererObj,
            EnumChatFormatting.RED + "Modpack version mismatch",
            width / 2,
            65,
            0xFFFFFF);
        drawCenteredString(
            fontRendererObj,
            "This server uses version " + EnumChatFormatting.YELLOW + serverVersion + EnumChatFormatting.WHITE + ".",
            width / 2,
            88,
            0xFFFFFF);
        drawCenteredString(
            fontRendererObj,
            "Your client uses version " + EnumChatFormatting.YELLOW
                + Config.modpackVersion
                + EnumChatFormatting.WHITE
                + ".",
            width / 2,
            103,
            0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
