package utils;

public class OSInfo {
    public static boolean isWindows() {

        String os = System.getProperty("os.name").toLowerCase();
        return (os.contains("win"));

    }

    public static boolean isMac() {

        String os = System.getProperty("os.name").toLowerCase();
        return (os.contains("mac"));

    }

    public static boolean isUnix() {

        String os = System.getProperty("os.name").toLowerCase();
        return (os.contains("nix") || os.contains("nux"));

    }

    public static String getOSVerion() {
        return System.getProperty("os.version");
    }

    public static String getUser() {
        return System.getProperty("user.name");
    }
}
