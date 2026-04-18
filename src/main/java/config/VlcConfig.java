package config;

import java.nio.file.Files;
import java.nio.file.Path;

public class VlcConfig {

    public static void init() {
        OS os = OSDetector.getOS();

        switch (os) {
            case WINDOWS -> initWindows();
            case LINUX -> initLinux();
            case MAC -> initMac();
            default -> throw new RuntimeException("Unsupported OS for VLC");
        }
    }

    private static void initWindows() {
        System.setProperty("jna.library.path",
                "C:\\Program Files\\VideoLAN\\VLC");
    }

    private static void initLinux() {
        String path = "/usr/lib64";

        System.out.println("Java working directory: " + System.getProperty("user.dir"));
        System.out.println("Can Java see /usr/lib64? " + Files.isDirectory(Path.of("/usr/lib64")));
        System.out.println("Does /usr/lib64/libvlc.so exist to Java? " + Files.exists(Path.of("/usr/lib64/libvlc.so")));

        if (!Files.exists(Path.of(path, "libvlc.so"))) {
            path = "/run/host/usr/lib64";
        }

        if (Files.exists(Path.of(path, "libvlc.so"))) {
            System.setProperty("jna.library.path", path);
            System.setProperty("vlcj.libvlc", path);
            System.setProperty("VLC_PLUGIN_PATH", path + "/vlc/plugins");
            System.out.println("✅ Found libvlc.so at: " + path);
        } else {
            System.out.println("DEBUG: Contents of /usr/lib64 visible to Java:");
            try (var s = Files.list(Path.of("/usr/lib64"))) {
                s.limit(5).forEach(p -> System.out.println(" - " + p.getFileName()));
            } catch (Exception ignored) {}

            throw new RuntimeException("CRITICAL: libvlc.so not found even in /run/host/usr/lib64.");
        }
    }


    private static void initMac() {
        System.setProperty("jna.library.path",
                "/Applications/VLC.app/Contents/MacOS/lib");
    }
}
