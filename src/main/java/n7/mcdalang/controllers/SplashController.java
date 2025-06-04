package n7.mcdalang.controllers;

import n7.mcdalang.util.AppConfig;
import n7.mcdalang.util.AudioPlayer;
import n7.mcdalang.views.SplashView;

public class SplashController extends Controller<SplashView> {

    public SplashController(SplashView view) {
        super(view);
    }

    @Override
    protected void updateView() {
        // No updates needed for the splash screen
    }

    @Override
    public void showView() {
        super.showView();
        AudioPlayer.play(AppConfig.SPLASH_AUDIO_PATH);
    }
}
