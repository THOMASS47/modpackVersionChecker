package com.modpackversionchecker;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public final class Config {

    public static String modpackVersion = "1.0.0";
    public static boolean kickOnVersionMismatch = true;

    private Config() {}

    public static void synchronize(File configFile) {
        Configuration configuration = new Configuration(configFile);

        modpackVersion = configuration
            .getString("version", Configuration.CATEGORY_GENERAL, modpackVersion, "The modpack version.");
        kickOnVersionMismatch = configuration.getBoolean(
            "kickOnVersionMismatch",
            Configuration.CATEGORY_GENERAL,
            kickOnVersionMismatch,
            "Whether the server rejects clients whose modpack version differs or is unavailable.");
        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
