package com.modpackversionchecker.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.modpackversionchecker.Config;
import com.modpackversionchecker.ModpackVersionChecker;

import cpw.mods.fml.common.network.handshake.FMLHandshakeMessage;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;

/** Checks the pack version before Forge builds its generic mod-rejection message. */
@Mixin(targets = "cpw.mods.fml.common.network.handshake.FMLHandshakeServerState$2", remap = false)
public abstract class FMLHandshakeServerStateMixin {

    @Redirect(
        method = "accept",
        at = @At(
            value = "INVOKE",
            target = "Lcpw/mods/fml/common/network/internal/FMLNetworkHandler;checkModList(Lcpw/mods/fml/common/network/handshake/FMLHandshakeMessage$ModList;Lcpw/mods/fml/relauncher/Side;)Ljava/lang/String;"),
        remap = false)
    private String modpackVersionChecker$checkVersionFirst(FMLHandshakeMessage.ModList modList, Side side) {
        String clientVersion = modList.modList()
            .get(ModpackVersionChecker.MODID);
        if (!Config.modpackVersion.equals(clientVersion)) {
            ModpackVersionChecker.LOG.info(
                "Rejecting client with modpack version '{}' (required '{}')",
                clientVersion == null ? "unknown" : clientVersion,
                Config.modpackVersion);
            return Config.formatRejection(clientVersion);
        }
        return FMLNetworkHandler.checkModList(modList, side);
    }
}
