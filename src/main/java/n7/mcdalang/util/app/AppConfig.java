package n7.mcdalang.util.app;

import n7.mcdalang.models.antlr.Languages;
import n7.mcdalang.util.theme.Themes;

import java.awt.*;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public final class AppConfig {

    public static final String APP_TITLE = "McDaLang";

    public static final URL SPLASH_GIF = Objects.requireNonNull(AppConfig.class.getResource("/images/splash.gif"));
    public static final URL SPLASH_AUDIO = Objects.requireNonNull(AppConfig.class.getResource("/audio/splash.wav"));
    public static final int SPLASH_DURATION_MS = 3300;

    public static final Dimension DEFAULT_SIZE = new Dimension(800, 600);
    public static final List<Languages> DEFAULT_LANGUAGES = List.of(Languages.PYTHON, Languages.C);
    public static final Themes DEFAULT_THEME = Themes.LIGHT;
    public static final boolean DEFAULT_AUTORUN_MODE = false;

    private AppConfig() {}
}
