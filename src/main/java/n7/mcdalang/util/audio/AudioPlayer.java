package n7.mcdalang.util.audio;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class AudioPlayer {

    private AudioPlayer() {}

    public static void play(URL url) {
        try {
            if (url == null) {
                Logger.getLogger(AudioPlayer.class.getName()).log(Level.WARNING, "Audio file not found : {0} ", url);
                return;
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            Logger.getLogger(AudioPlayer.class.getName()).log(Level.SEVERE, e.getMessage());
        }
    }

}
