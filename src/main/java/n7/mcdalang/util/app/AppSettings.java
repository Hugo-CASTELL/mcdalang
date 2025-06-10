package n7.mcdalang.util.app;

import n7.mcdalang.models.antlr.Languages;
import n7.mcdalang.util.theme.ThemeManager;
import n7.mcdalang.util.theme.Themes;

import java.util.ArrayList;
import java.util.List;

public class AppSettings {

    //#region Fields

    private Themes theme;
    private List<Languages> languages;

    //#endregion Fields

    //#region Constructor

    public AppSettings() {
        // see initializeDefaultSettings() method
        theme = null;
        languages = new ArrayList<>();
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
    }

    //#endregion Methods
}