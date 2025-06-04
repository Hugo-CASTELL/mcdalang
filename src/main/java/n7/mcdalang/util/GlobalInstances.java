package n7.mcdalang.util;

public final class GlobalInstances {

    private GlobalInstances() {}

    public static AppSettings getAppSettings() {
        return AppSettingsSingletonHolder.INSTANCE;
    }

    public static AppManager getAppManager() {
        return AppManagerSingletonHolder.INSTANCE;
    }

    private static final class AppSettingsSingletonHolder {
        private static final AppSettings INSTANCE = new AppSettings();
    }

    private static final class AppManagerSingletonHolder {
        private static final AppManager INSTANCE = new AppManager();
    }
}
