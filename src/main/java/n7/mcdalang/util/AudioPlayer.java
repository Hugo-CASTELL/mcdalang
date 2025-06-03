package n7.mcdalang.util;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class AudioPlayer {

    public static void play(String filePath) {
        try {
            File soundFile = new File(filePath);
            if (!soundFile.exists()) {
                Logger.getLogger(AudioPlayer.class.getName()).log(Level.WARNING, "Audio file not found : {0} ", filePath);
                return;
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private AudioPlayer() {}

}
