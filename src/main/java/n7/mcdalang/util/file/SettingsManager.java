package n7.mcdalang.util.file;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class SettingsManager {

    private SettingsManager() { }

    public static void saveAsFile(String filePath, List<Setting<?>> preferences) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(preferences);
        } catch (IOException e) {
            Logger.getLogger(SettingsManager.class.getName()).log(Level.SEVERE, "Could not save App Settings", e);
        }
    }

    public static List<Setting<Serializable>> loadFromFile(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return Collections.unmodifiableList((List<Setting<Serializable>>) ois.readObject());
        } catch (IOException | ClassNotFoundException e) {
            Logger.getLogger(SettingsManager.class.getName()).log(Level.SEVERE, "Could not load App Settings", e);
            return Collections.emptyList();
        }
    }
}
