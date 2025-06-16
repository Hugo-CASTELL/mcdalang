package n7.mcdalang.util;

import n7.mcdalang.util.app.AppManager;
import n7.mcdalang.util.app.AppSettings;

public final class GlobalInstances {

    //#region Prevent instantiation

    private GlobalInstances() {}

    //#endregion Prevent instantiation

    //#region Singleton Instances Getters

    public static AppSettings getAppSettings() {
        return AppSettingsSingletonHolder.INSTANCE;
    }

    public static AppManager getAppManager() {
        return AppManagerSingletonHolder.INSTANCE;
    }

    //#endregion Singleton Instances Getters

    //#region Singleton Instances Holders

    private static final class AppSettingsSingletonHolder {
        private static final AppSettings INSTANCE = new AppSettings();
    }

    private static final class AppManagerSingletonHolder {
        private static final AppManager INSTANCE = new AppManager();
    }

    //#endregion Singleton Instances Holders
}
