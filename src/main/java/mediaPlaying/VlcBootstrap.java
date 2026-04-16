package mediaPlaying;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;

public class VlcBootstrap {

    static {
        System.setProperty(
                "jna.library.path",
                "C:\\Program Files\\VideoLAN\\VLC"
        );
    }

    public static void init() {
        try {
            new MediaPlayerFactory().release();
            System.out.println("VLC native library loaded successfully!");
        } catch (Exception e) {
            System.err.println("Failed to load VLC native library: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

