package com.thomass47.modpackversionchecker.mixin;

import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ServerListEntryNormal;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.ServerStatusResponse;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.thomass47.modpackversionchecker.ClientConnectionWarning;
import com.thomass47.modpackversionchecker.Config;
import com.thomass47.modpackversionchecker.GuiVersionMismatch;
import com.thomass47.modpackversionchecker.ModpackVersionChecker;
import com.thomass47.modpackversionchecker.VersionCheck;

import cpw.mods.fml.client.ExtendedServerListData;
import cpw.mods.fml.client.FMLClientHandler;

@Mixin(value = FMLClientHandler.class, remap = false)
public abstract class FMLClientHandlerMixin {

    @Shadow
    private Map<ServerData, ExtendedServerListData> serverDataTag;

    @Inject(method = "bindServerListData", at = @At("RETURN"))
    private void modpackVersionChecker$markMismatchedServer(ServerData server, ServerStatusResponse response,
        CallbackInfo callback) {
        ExtendedServerListData serverData = serverDataTag.get(server);
        if (serverData == null || VersionCheck.mismatch(serverData.modData.get(ModpackVersionChecker.MODID)) == null) {
            return;
        }

        serverDataTag
            .put(server, new ExtendedServerListData(serverData.type, false, serverData.modData, serverData.isBlocked));
    }

    @Inject(method = "enhanceServerListEntry", at = @At("RETURN"), cancellable = true)
    private void modpackVersionChecker$describeMismatch(ServerListEntryNormal serverListEntry, ServerData server, int x,
        int width, int y, int relativeMouseX, int relativeMouseY, CallbackInfoReturnable<String> callback) {
        if (callback.getReturnValue() == null) {
            return;
        }

        ExtendedServerListData serverData = serverDataTag.get(server);
        if (serverData == null) {
            return;
        }

        String serverVersion = VersionCheck.mismatch(serverData.modData.get(ModpackVersionChecker.MODID));
        if (serverVersion != null) {
            callback.setReturnValue(VersionCheck.mismatchMessage(Config.modpackVersion, serverVersion));
        }
    }

    @Inject(method = "connectToServer", at = @At("HEAD"), cancellable = true)
    private void modpackVersionChecker$warnBeforeConnecting(GuiScreen parent, ServerData server,
        CallbackInfo callback) {
        if (ClientConnectionWarning.consumeBypass(server)) {
            return;
        }

        ExtendedServerListData serverData = serverDataTag.get(server);
        if (serverData == null) {
            return;
        }

        String serverVersion = VersionCheck.mismatch(serverData.modData.get(ModpackVersionChecker.MODID));
        if (serverVersion == null) {
            return;
        }

        Minecraft.getMinecraft()
            .displayGuiScreen(new GuiVersionMismatch(parent, server, serverVersion));
        callback.cancel();
    }
}
