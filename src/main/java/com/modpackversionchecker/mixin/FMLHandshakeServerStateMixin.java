package com.modpackversionchecker.mixin;

import net.minecraft.util.EnumChatFormatting;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.modpackversionchecker.Config;
import com.modpackversionchecker.ModpackVersionChecker;

import cpw.mods.fml.common.network.handshake.FMLHandshakeMessage;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;

@Mixin(targets = "cpw.mods.fml.common.network.handshake.FMLHandshakeServerState$2", remap = false)
public abstract class FMLHandshakeServerStateMixin {

    @Redirect(
        method = "accept",
        at = @At(
            value = "INVOKE",
            target = "Lcpw/mods/fml/common/network/internal/FMLNetworkHandler;checkModList(Lcpw/mods/fml/common/network/handshake/FMLHandshakeMessage$ModList;Lcpw/mods/fml/relauncher/Side;)Ljava/lang/String;"),
        remap = false)
    private String modpackVersionChecker$checkConfiguredVersion(FMLHandshakeMessage.ModList modList, Side side) {
        String clientVersion = modList.modList()
            .get(ModpackVersionChecker.MODID);
        if (Config.kickOnVersionMismatch && !Config.modpackVersion.equals(clientVersion)) {
            String displayedClientVersion = clientVersion == null || clientVersion.isEmpty() ? "unknown"
                : clientVersion;
            ModpackVersionChecker.LOG.info(
                "Rejecting client with modpack version '{}' (required '{}')",
                displayedClientVersion,
                Config.modpackVersion);
            return EnumChatFormatting.RED + "Modpack version mismatch\n"
                + EnumChatFormatting.WHITE
                + "This server uses version "
                + EnumChatFormatting.YELLOW
                + Config.modpackVersion
                + EnumChatFormatting.WHITE
                + ".\nYour client uses version "
                + EnumChatFormatting.YELLOW
                + displayedClientVersion
                + EnumChatFormatting.WHITE
                + ".";
        }
        return FMLNetworkHandler.checkModList(modList, side);
    }
}
