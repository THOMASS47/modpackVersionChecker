package com.modpackversionchecker;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ServerData;

import cpw.mods.fml.client.FMLClientHandler;

public final class ClientConnectionWarning {

    private static ServerData bypassServer;

    private ClientConnectionWarning() {}

    public static void connectAnyway(GuiScreen parent, ServerData server) {
        bypassServer = server;
        FMLClientHandler.instance()
            .connectToServer(parent, server);
    }

    public static boolean consumeBypass(ServerData server) {
        if (bypassServer != server) {
            return false;
        }
        bypassServer = null;
        return true;
    }
}
