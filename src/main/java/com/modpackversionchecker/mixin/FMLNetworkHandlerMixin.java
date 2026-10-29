package com.modpackversionchecker.mixin;

import net.minecraft.util.EnumChatFormatting;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.modpackversionchecker.Config;
import com.modpackversionchecker.ModpackVersionChecker;
import com.modpackversionchecker.VersionCheck;

import cpw.mods.fml.common.network.handshake.FMLHandshakeMessage;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;

@Mixin(value = FMLNetworkHandler.class, remap = false)
public abstract class FMLNetworkHandlerMixin {

    @Inject(method = "enhanceStatusQuery(Lcom/google/gson/JsonObject;)V", at = @At("RETURN"))
    private static void modpackVersionChecker$advertiseVersion(JsonObject response, CallbackInfo callback) {
        for (JsonElement entry : response.getAsJsonObject("modinfo").getAsJsonArray("modList")) {
            JsonObject mod = entry.getAsJsonObject();
            if (mod.get("modid").getAsString().equals(ModpackVersionChecker.MODID)) {
                mod.addProperty("version", Config.modpackVersion);
                return;
            }
        }
    }

    @Inject(
        method = "checkModList(Lcpw/mods/fml/common/network/handshake/FMLHandshakeMessage$ModList;Lcpw/mods/fml/relauncher/Side;)Ljava/lang/String;",
        at = @At("HEAD"),
        cancellable = true)
    private static void modpackVersionChecker$checkConfiguredVersion(FMLHandshakeMessage.ModList modList, Side side,
        CallbackInfoReturnable<String> callback) {
        if (side.isServer() || !Config.kickOnVersionMismatch) {
            return;
        }

        String clientVersion = VersionCheck.mismatch(
            modList.modList()
                .get(ModpackVersionChecker.MODID));
        if (clientVersion == null) {
            return;
        }

        ModpackVersionChecker.LOG
            .info("Rejecting client with modpack version '{}' (required '{}')", clientVersion, Config.modpackVersion);
        callback.setReturnValue(VersionCheck.mismatchMessage(clientVersion, Config.modpackVersion));
    }
}
