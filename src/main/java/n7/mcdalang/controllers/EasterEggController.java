package n7.mcdalang.controllers;

import n7.mcdalang.util.app.AppConfig;
import n7.mcdalang.util.audio.AudioPlayer;
import n7.mcdalang.views.EasterEggView;

public class EasterEggController extends Controller<EasterEggView> {

    //#region Constructor

    public EasterEggController(EasterEggView view) {
        super(view);
    }

    //#endregion Constructor

    //#region Overriden Methods

    @Override
    protected void updateView() {
        // No updates needed for the easter egg screen
    }

    @Override
    protected void registerListeners() {
        // No listeners to register for the easter egg screen
    }

    @Override
    public void show() {
        super.show();
        AudioPlayer.play(AppConfig.TOTEM_AUDIO);
    }

    //#endregion Overriden Methods

}
