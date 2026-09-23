package com.modpackversionchecker;

import com.modpackversionchecker.mixin.FMLModContainerAccessor;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        Config.synchronize(event.getSuggestedConfigurationFile());
        advertiseConfiguredVersion();
        ModpackVersionChecker.LOG.info("Configured modpack version: {}", Config.modpackVersion);
    }

    public void init(FMLInitializationEvent event) {}

    private static void advertiseConfiguredVersion() {
        ModContainer container = Loader.instance()
            .getIndexedModList()
            .get(ModpackVersionChecker.MODID);
        if (!(container instanceof FMLModContainerAccessor)) {
            throw new IllegalStateException("Could not find the Modpack Version Checker mod container");
        }
        ((FMLModContainerAccessor) container).modpackVersionChecker$setInternalVersion(Config.modpackVersion);
    }
}
