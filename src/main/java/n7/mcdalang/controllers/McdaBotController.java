package n7.mcdalang.controllers;

import n7.mcdalang.util.app.AppConfig;
import n7.mcdalang.util.audio.AudioPlayer;
import n7.mcdalang.views.McdaBotMainView;
import n7.mcdalang.views.SplashView;

public class McdaBotController extends Controller<McdaBotMainView> {

    public McdaBotController(McdaBotMainView view) {
        super(view);
    }

    @Override
    protected void updateView() {
        // No updates needed for the splash screen
    }

    @Override
    protected void registerListeners() {
        // No listeners to register for the splash screen
    }
}
