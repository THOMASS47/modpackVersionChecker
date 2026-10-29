package com.modpackversionchecker;

public final class VersionCheck {

    private VersionCheck() {}

    public static String mismatch(String remoteVersion) {
        if (remoteVersion == null || remoteVersion.isEmpty()) {
            return "unknown";
        }

        return Config.modpackVersion.equals(remoteVersion) ? null : remoteVersion;
    }
}
