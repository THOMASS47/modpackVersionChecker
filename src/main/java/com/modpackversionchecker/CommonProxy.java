package com.modpackversionchecker;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        Config.synchronize(event.getSuggestedConfigurationFile());
        ModpackVersionChecker.LOG.info("Configured modpack version: {}", Config.modpackVersion);
    }

    public void init(FMLInitializationEvent event) {}
}
