package n7.mcdalang.util;

public final class GlobalInstances {

    private GlobalInstances() {}

    public static AppSettings getAppSettings() {
        return AppSettingsHolder.INSTANCE;
    }

    public static AppManager getAppManager() {
        return AppManagerHolder.INSTANCE;
    }

    private static final class AppSettingsHolder {
        private static final AppSettings INSTANCE = new AppSettings();
    }

    private static final class AppManagerHolder {
        private static final AppManager INSTANCE = new AppManager();
    }
}
