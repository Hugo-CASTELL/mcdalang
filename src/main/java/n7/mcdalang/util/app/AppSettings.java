package n7.mcdalang.util.app;

import n7.mcdalang.models.antlr.Languages;
import n7.mcdalang.util.file.Setting;
import n7.mcdalang.util.file.SettingsManager;
import n7.mcdalang.util.theme.ThemeManager;
import n7.mcdalang.util.theme.Themes;

import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppSettings {

    //#region Fields

    private boolean isModified = false;

    private Themes theme;
    private final List<Languages> languages;

    private boolean autoRun;

    //#endregion Fields

    //#region Constructor

    public AppSettings() {
        // see initializeDefaultSettings() method
        theme = null;
        languages = new ArrayList<>();
        autoRun = false;
        isModified = false;
        // see initializeDefaultSettings() method
    }

    //#endregion Constructor

    //#region Getters

    public List<Languages> getLanguages() {
        return languages;
    }

    public Themes getTheme() {
        return theme;
    }

    public boolean getAutoRun() {
        return autoRun;
    }

    //#endregion Getters

    //#region Setters

    public void setTheme(Themes theme) {
        this.theme = theme;
        ThemeManager.apply(theme);
        this.isModified = true;
    }

    public void setAutoRun(boolean autoRun) {
        this.autoRun = autoRun;
        this.isModified = true;
    }

    public void addLanguage(Languages language) {
        if (!languages.contains(language)) {
            languages.add(language);
        }
        this.isModified = true;
    }

    public void addLanguages(List<Languages> languages) {
        for (Languages language : languages) {
            addLanguage(language);
        }
    }

    //#endregion Setters

    //#region Methods

    public void initializeDefaultSettings() {
        this.setTheme(AppConfig.DEFAULT_THEME);
        this.addLanguages(AppConfig.DEFAULT_LANGUAGES);
        this.setAutoRun(AppConfig.DEFAULT_AUTORUN_MODE);
        this.isModified = false;
    }

    public void saveToFile() {
        if(isModified) {
            SettingsManager.saveAsFile(AppConfig.APP_SETTINGS_FILE, List.of(
                Setting.of("theme", theme),
                Setting.of("languages", (Serializable) languages),
                Setting.of("autoRun", autoRun)
            ));
        }
    }

    public void loadFromFile() {
        try {
            for (Setting<?> setting : SettingsManager.loadFromFile(AppConfig.APP_SETTINGS_FILE)) {
                switch (setting.getKey()) {
                    case "theme" -> setTheme((Themes) setting.getValue());
                    case "languages" -> addLanguages((List<Languages>) setting.getValue());
                    case "autoRun" -> setAutoRun((Boolean) setting.getValue());
                    default -> throw new IllegalArgumentException("Unknown key: " + setting.getKey());
                }
            }
        } catch (Exception e) {
            Logger.getLogger(AppSettings.class.getName()).log(Level.SEVERE, "Could not load App Settings", e);
            initializeDefaultSettings(); // Fallback to default settings if loading fails
        }
    }

    //#endregion Methods
}