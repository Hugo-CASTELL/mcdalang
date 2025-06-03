package n7.mcdalang.util;

import java.util.ArrayList;
import java.util.List;

public class AppSettings {

    private String currentTheme;
    private List<String> currentLanguages;

    public AppSettings() {
        this.currentTheme = AppConfig.DEFAULT_THEME;
        this.currentLanguages = new ArrayList<>(AppConfig.DEFAULT_LANGUAGES);
    }

    public String getCurrentTheme() {
        return currentTheme;
    }

    public List<String> getCurrentLanguages() {
        return currentLanguages;
    }
}