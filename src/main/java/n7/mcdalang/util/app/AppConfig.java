package n7.mcdalang.util.app;

import n7.mcdalang.models.antlr.Languages;
import n7.mcdalang.util.font.Fonts;
import n7.mcdalang.util.theme.Themes;

import java.awt.*;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class AppConfig {

    public static final String APP_TITLE = "McDaLang";
    public static final String APP_SETTINGS_FILE = "mcdalang.properties";
    public static final URL MCDA_ICON = getURL("/images/mcdala.png");

    public static final URL SPLASH_GIF = getURL("/images/splash.gif");
    public static final URL SPLASH_AUDIO = getURL("/audio/splash.wav");
    public static final int SPLASH_DURATION_MS = 3300;

    public static final URL TOTEM_GIF = getURL("/images/totem2-without_all.gif");
    public static final int TOTEM_DURATION = 2300;

    // FontAdapters
    public static final Map<Fonts, URL> FONT_ADAPTERS = Map.of(
        Fonts.JETBRAINS_LIGHT, getURL("/fonts/JetBrainsMono-Light.ttf"),
        Fonts.JETBRAINS_MEDIUM, getURL("/fonts/JetBrainsMono-Medium.ttf"),
        Fonts.JETBRAINS_BOLD, getURL("/fonts/JetBrainsMono-Bold.ttf"),

        Fonts.FIRACODE_LIGHT, getURL("/fonts/FiraCode-Light.ttf"),
        Fonts.FIRACODE_MEDIUM, getURL("/fonts/FiraCode-Medium.ttf"),

        Fonts.FIRACODE_BOLD, getURL("/fonts/FiraCode-Bold.ttf"),
        Fonts.CONSOLAS_LIGHT, getURL("/fonts/ConsolasMono-Light.ttf"),
        Fonts.CONSOLAS_BOLD, getURL("/fonts/ConsolasMono-Bold.ttf")
    );

    public static final URL MCDABOT_HEAD_PATH = getURL("/images/mcdala_tete_grand.png");

    public static final Dimension DEFAULT_SIZE = new Dimension(1080, 720);
    public static final List<Languages> DEFAULT_LANGUAGES = List.of(Languages.PYTHON, Languages.C, Languages.CPlusPlus, Languages.RUST);
    public static final Themes DEFAULT_THEME = Themes.LIGHT;
    public static final boolean DEFAULT_AUTORUN_MODE = false;
    public static final int DEFAULT_FONT_SIZE = 12;
    public static final Fonts DEFAULT_FONT = Fonts.JETBRAINS_BOLD;

    private static URL getURL(String path) {
        return Objects.requireNonNull(AppConfig.class.getResource(path));
    }

    private AppConfig() {}
}
