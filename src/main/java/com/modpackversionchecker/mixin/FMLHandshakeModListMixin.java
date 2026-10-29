package com.modpackversionchecker.mixin;

import java.util.List;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.modpackversionchecker.Config;
import com.modpackversionchecker.ModpackVersionChecker;

import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.network.handshake.FMLHandshakeMessage;

@Mixin(value = FMLHandshakeMessage.ModList.class, remap = false)
public abstract class FMLHandshakeModListMixin {

    @Shadow
    private Map<String, String> modTags;

    @Inject(method = "<init>(Ljava/util/List;)V", at = @At("RETURN"))
    private void modpackVersionChecker$advertiseVersion(List<ModContainer> mods, CallbackInfo callback) {
        modTags.put(ModpackVersionChecker.MODID, Config.modpackVersion);
    }
}
