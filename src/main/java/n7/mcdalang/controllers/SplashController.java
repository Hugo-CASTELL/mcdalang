package n7.mcdalang.controllers;

import n7.mcdalang.util.app.AppConfig;
import n7.mcdalang.util.audio.AudioPlayer;
import n7.mcdalang.views.SplashView;

public class SplashController extends Controller<SplashView> {

    //#region Constructor

    public SplashController(SplashView view) {
        super(view);
    }

    //#endregion Constructor

    //#region Overriden Methods

    @Override
    protected void updateView() {
        // No updates needed for the splash screen
    }

    @Override
    protected void registerListeners() {
        // No listeners to register for the splash screen
    }

    @Override
    public void show() {
        super.show();
        AudioPlayer.play(AppConfig.SPLASH_AUDIO);
    }

    //#endregion Overriden Methods

}
