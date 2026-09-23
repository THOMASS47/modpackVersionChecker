package com.modpackversionchecker.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import cpw.mods.fml.common.FMLModContainer;

@Mixin(value = FMLModContainer.class, remap = false)
public interface FMLModContainerAccessor {

    @Accessor("internalVersion")
    void modpackVersionChecker$setInternalVersion(String version);
}
