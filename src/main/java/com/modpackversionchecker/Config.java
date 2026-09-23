package com.modpackversionchecker;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public final class Config {

    public static String modpackVersion = "1.0.0";
    public static String rejectionMessage = "Your modpack is out of date.\nRequired version: {serverVersion}\nYour version: {clientVersion}";

    private Config() {}

    public static void synchronize(File configFile) {
        Configuration configuration = new Configuration(configFile);

        modpackVersion = configuration.getString(
            "version",
            Configuration.CATEGORY_GENERAL,
            modpackVersion,
            "The modpack version advertised by clients and required by the server.");
        rejectionMessage = configuration.getString(
            "rejectionMessage",
            Configuration.CATEGORY_GENERAL,
            rejectionMessage,
            "Message shown to rejected players. Supports {serverVersion}, {clientVersion}, \\n, and & color codes.");

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }

    public static String formatRejection(String clientVersion) {
        String displayedClientVersion = clientVersion == null || clientVersion.isEmpty() ? "unknown" : clientVersion;
        return rejectionMessage.replace("\\n", "\n")
            .replace('&', '\u00a7')
            .replace("{serverVersion}", modpackVersion)
            .replace("{clientVersion}", displayedClientVersion);
    }
}
