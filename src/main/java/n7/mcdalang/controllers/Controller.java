package n7.mcdalang.controllers;

import n7.mcdalang.util.GlobalInstances;
import n7.mcdalang.views.View;

public abstract class Controller<V extends View> {
    protected boolean initialized;
    protected final V view;

    protected Controller(V view) {
        this.view = view;
        this.initialized = false;
    }

    public void show() {
        prepareView();
        GlobalInstances.getAppManager().display(view);
    }

    public int showAsPopup() {
        prepareView();
        return GlobalInstances.getAppManager().displayPopup(view);
    }

    private void prepareView() {
        if (!initialized) {
            updateView();
            registerListeners();
            this.initialized = true;
        }
    }

    /**
     * Updates the view with the latest data or state.
     */
    protected abstract void updateView();

    /**
     * Registers listeners for user interactions or events.
     * This method should be implemented to set up any necessary event handling.
     */
    protected abstract void registerListeners();
}
