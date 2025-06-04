package n7.mcdalang.controllers;

import n7.mcdalang.views.MainView;

public class MainController extends Controller<MainView> {

    public MainController(MainView view) {
        super(view);

        // Initialize the main view with the origin text area focused
        view.getOriginTextArea().focusCode();
    }

    @Override
    protected void updateView() {

    }
}
