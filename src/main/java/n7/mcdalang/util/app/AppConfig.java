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

    public static final URL SPLASH_GIF = Objects.requireNonNull(AppConfig.class.getResource("/images/splash.gif"));
    public static final URL SPLASH_AUDIO = Objects.requireNonNull(AppConfig.class.getResource("/audio/splash.wav"));
    public static final int SPLASH_DURATION_MS = 3300;

    // FontAdapters
    public static final Map<Fonts, URL> FONT_ADAPTERS = Map.of(
            Fonts.JETBRAINS_LIGHT, Objects.requireNonNull(AppConfig.class.getResource("/fonts/JetBrainsMono-Light.ttf")),
            Fonts.JETBRAINS_MEDIUM, Objects.requireNonNull(AppConfig.class.getResource("/fonts/JetBrainsMono-Medium.ttf")),
            Fonts.JETBRAINS_BOLD, Objects.requireNonNull(AppConfig.class.getResource("/fonts/JetBrainsMono-Bold.ttf")),

            Fonts.FIRACODE_LIGHT, Objects.requireNonNull(AppConfig.class.getResource("/fonts/FiraCode-Light.ttf")),
            Fonts.FIRACODE_MEDIUM, Objects.requireNonNull(AppConfig.class.getResource("/fonts/FiraCode-Medium.ttf")),

            Fonts.FIRACODE_BOLD, Objects.requireNonNull(AppConfig.class.getResource("/fonts/FiraCode-Bold.ttf")),
            Fonts.CONSOLAS_LIGHT, Objects.requireNonNull(AppConfig.class.getResource("/fonts/ConsolasMono-Light.ttf")),
            Fonts.CONSOLAS_BOLD, Objects.requireNonNull(AppConfig.class.getResource("/fonts/ConsolasMono-Bold.ttf"))
    );

    public static final URL MCDABOT_HEAD_PATH = Objects.requireNonNull(AppConfig.class.getResource("/images/mcdala_tete_grand.png"));

    public static final Dimension DEFAULT_SIZE = new Dimension(800, 600);
    public static final List<Languages> DEFAULT_LANGUAGES = List.of(Languages.PYTHON, Languages.C, Languages.CPlusPlus, Languages.RUST);
    public static final Themes DEFAULT_THEME = Themes.LIGHT;
    public static final boolean DEFAULT_AUTORUN_MODE = false;
    public static final int DEFAULT_FONT_SIZE = 12;
    public static final Fonts DEFAULT_FONT = Fonts.JETBRAINS_BOLD;

    private AppConfig() {}
}
