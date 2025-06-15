package n7.mcdalang.util.app;

import n7.mcdalang.models.antlr.Languages;
import n7.mcdalang.util.theme.ThemeManager;
import n7.mcdalang.util.theme.Themes;

import java.util.ArrayList;
import java.util.List;

public class AppSettings {

    //#region Fields

    private Themes theme;
    private final List<Languages> languages;

    private boolean autoRun;

    private int fontSize;

    //#endregion Fields

    //#region Constructor

    public AppSettings() {
        // see initializeDefaultSettings() method
        theme = null;
        languages = new ArrayList<>();
        autoRun = false;
        // see initializeDefaultSettings() method
    }

    //#endregion Constructor

    //#region Getters

    public List<Languages> getLanguages() {
        return languages;
    }

    //#endregion Getters

    //#region Setters

    public void setTheme(Themes theme) {
        this.theme = theme;
        ThemeManager.apply(theme);
    }

    public Themes getTheme() {
        return theme;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setAutoRun(boolean autoRun) {
        this.autoRun = autoRun;
    }

    public boolean getAutoRun() {
        return autoRun;
    }
  
    public void addLanguage(Languages language) {
        if (!languages.contains(language)) {
            languages.add(language);
        }
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
        this.setFontSize(AppConfig.DEFAULT_FONT_SIZE);
    }

    //#endregion Methods
}