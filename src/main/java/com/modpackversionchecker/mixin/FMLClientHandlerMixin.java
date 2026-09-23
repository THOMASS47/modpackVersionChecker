package com.modpackversionchecker.mixin;

import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ServerListEntryNormal;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.util.EnumChatFormatting;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.modpackversionchecker.ClientConnectionWarning;
import com.modpackversionchecker.Config;
import com.modpackversionchecker.GuiVersionMismatch;
import com.modpackversionchecker.ModpackVersionChecker;

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
        if (modpackVersionChecker$getMismatchedVersion(serverData) == null) {
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

        String serverVersion = modpackVersionChecker$getMismatchedVersion(serverDataTag.get(server));
        if (serverVersion != null) {
            callback.setReturnValue(
                EnumChatFormatting.RED + "Modpack version mismatch\n"
                    + EnumChatFormatting.WHITE
                    + "This server uses version "
                    + EnumChatFormatting.YELLOW
                    + serverVersion
                    + EnumChatFormatting.WHITE
                    + ".\nYour client uses version "
                    + EnumChatFormatting.YELLOW
                    + Config.modpackVersion
                    + EnumChatFormatting.WHITE
                    + ".");
        }
    }

    @Inject(method = "connectToServer", at = @At("HEAD"), cancellable = true)
    private void modpackVersionChecker$warnBeforeConnecting(GuiScreen parent, ServerData server,
        CallbackInfo callback) {
        if (ClientConnectionWarning.consumeBypass(server)) {
            return;
        }

        String serverVersion = modpackVersionChecker$getMismatchedVersion(serverDataTag.get(server));
        if (serverVersion == null) {
            return;
        }

        Minecraft.getMinecraft()
            .displayGuiScreen(new GuiVersionMismatch(parent, server, serverVersion));
        callback.cancel();
    }

    @Unique
    private static String modpackVersionChecker$getMismatchedVersion(ExtendedServerListData serverData) {
        if (serverData == null) {
            return null;
        }

        String serverVersion = serverData.modData.get(ModpackVersionChecker.MODID);
        if (serverVersion == null || serverVersion.isEmpty()) {
            return "unknown";
        }
        return Config.modpackVersion.equals(serverVersion) ? null : serverVersion;
    }
}
