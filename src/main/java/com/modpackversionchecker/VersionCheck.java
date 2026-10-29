package com.modpackversionchecker;

import net.minecraft.util.EnumChatFormatting;

public final class VersionCheck {

    private VersionCheck() {}

    public static String mismatch(String remoteVersion) {
        if (remoteVersion == null || remoteVersion.isEmpty()) {
            return "unknown";
        }

        return Config.modpackVersion.equals(remoteVersion) ? null : remoteVersion;
    }

    public static String mismatchMessage(String clientVersion, String serverVersion) {
        return EnumChatFormatting.RED + "Modpack version mismatch\n\n"
            + EnumChatFormatting.WHITE
            + "Your client uses version "
            + EnumChatFormatting.YELLOW
            + clientVersion
            + EnumChatFormatting.WHITE
            + "\nThis server uses version "
            + EnumChatFormatting.YELLOW
            + serverVersion
            + EnumChatFormatting.WHITE;
    }
}
