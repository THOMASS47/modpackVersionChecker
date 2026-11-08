package com.thomass47.modpackversionchecker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(
    modid = ModpackVersionChecker.MODID,
    version = Tags.VERSION,
    name = "Modpack Version Checker",
    dependencies = "required-after:unimixins-mixin",
    acceptedMinecraftVersions = "[1.7.10]",
    acceptableRemoteVersions = "*")
public class ModpackVersionChecker {

    public static final String MODID = "modpackversionchecker";
    public static final Logger LOG = LogManager.getLogger(MODID);

    @SidedProxy(
        clientSide = "com.thomass47.modpackversionchecker.ClientProxy",
        serverSide = "com.thomass47.modpackversionchecker.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }
}
