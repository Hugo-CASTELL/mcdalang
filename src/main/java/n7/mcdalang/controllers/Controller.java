package n7.mcdalang.controllers;

import n7.mcdalang.util.GlobalInstances;
import n7.mcdalang.views.View;

public abstract class Controller<V extends View> {

    //#region Fields

    protected boolean isViewReady;
    protected final V view;

    //#region Fields

    //#region Constructor

    protected Controller(V view) {
        this.view = view;
        this.isViewReady = false;
    }

    //#endregion Constructor

    //#region Abstract Methods

    /**
     * Updates the view with the latest data or state.
     */
    protected abstract void updateView();

    /**
     * Registers listeners for user interactions or events.
     * This method should be implemented to set up any necessary event handling.
     */
    protected abstract void registerListeners();

    //#endregion Abstract Methods

    //#region Public Methods

    public void show() {
        prepareView();
        GlobalInstances.getAppManager().display(view);
    }

    public int showAsPopup() {
        prepareView();
        return GlobalInstances.getAppManager().displayPopup(view);
    }

    //#endregion Public Methods

    //#region Private Methods

    private void prepareView() {
        if (!isViewReady) {
            updateView();
            registerListeners();
            this.isViewReady = true;
        }
    }

    //#endregion Private Methods

}
