package n7.mcdalang.util.app;

import n7.mcdalang.models.antlr.Languages;
import n7.mcdalang.util.theme.Themes;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public final class AppConfig {

    public static final String APP_TITLE = "McDaLang";

    public static final String SPLASH_GIF_PATH = Objects.requireNonNull(AppConfig.class.getResource("/images/splash.gif")).getPath();
    public static final String SPLASH_AUDIO_PATH = Objects.requireNonNull(AppConfig.class.getResource("/audio/splash.wav")).getPath();
    public static final int SPLASH_DURATION_MS = 3300;

    public static final Dimension DEFAULT_SIZE = new Dimension(800, 600);
    public static final List<Languages> DEFAULT_LANGUAGES = List.of(Languages.C);
    public static final Themes DEFAULT_THEME = Themes.LIGHT;

    private AppConfig() {}
}
