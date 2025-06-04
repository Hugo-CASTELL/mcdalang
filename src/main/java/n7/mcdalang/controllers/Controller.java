package n7.mcdalang.controllers;

import n7.mcdalang.util.GlobalInstances;
import n7.mcdalang.views.View;

public abstract class Controller<V extends View> {
    protected final V view;

    protected Controller(V view) {
        this.view = view;
    }

    public void showView() {
        GlobalInstances.getAppManager().display(view);
    }

    protected abstract void updateView();
}
