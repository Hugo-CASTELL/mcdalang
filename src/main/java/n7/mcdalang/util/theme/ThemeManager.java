package n7.mcdalang.util.theme;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import n7.mcdalang.util.GlobalInstances;

import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ThemeManager {

    private ThemeManager() {}

    public static void apply(Themes theme) {
        try {
            LookAndFeel lookAndFeel = null;
            switch (theme) {
                case Themes.LIGHT -> lookAndFeel = new FlatLightLaf();
                case Themes.DARK -> lookAndFeel = new FlatDarkLaf();
                case Themes.INTELLIJ -> lookAndFeel = new FlatIntelliJLaf();
                case Themes.DARCULA -> lookAndFeel = new FlatDarculaLaf();
            }
            if (lookAndFeel != null) {
                UIManager.setLookAndFeel(lookAndFeel);
                GlobalInstances.getAppManager().onUIUpdate();
            }
        } catch (Exception ex) {
            Logger.getLogger(ThemeManager.class.getName()).log(Level.SEVERE, ex.getMessage());
        }
    }
}
