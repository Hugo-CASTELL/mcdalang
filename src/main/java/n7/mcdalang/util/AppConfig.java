package n7.mcdalang.util;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public final class AppConfig {

    public static final String APP_TITLE = "McDaLang";

    public static final String SPLASH_GIF_PATH = Objects.requireNonNull(AppConfig.class.getResource("/images/splash.gif")).getPath();
    public static final int SPLASH_DURATION_MS = 4000;

    public static final Dimension DEFAULT_SIZE = new Dimension(800, 600);
    public static final List<String> DEFAULT_LANGUAGES = List.of("Python", "Java");
    public static final String DEFAULT_THEME = "light";

    private AppConfig() {}
}
