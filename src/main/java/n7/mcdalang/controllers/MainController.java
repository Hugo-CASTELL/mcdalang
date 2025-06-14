package n7.mcdalang.controllers;

import n7.mcdalang.input.CodeKeyListener;
import n7.mcdalang.input.OptionActionListener;
import n7.mcdalang.input.RunActionListener;
import n7.mcdalang.input.SwitchActionListener;
import n7.mcdalang.views.MainView;
import n7.mcdalang.views.components.CodeTextArea;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static n7.mcdalang.models.antlr.Translate.translateToOther;

public class MainController extends Controller<MainView> {

    //#region Fields

    private boolean autoRun;

    //#endregion Fields

    //#region Constructor

    public MainController(MainView view) {
        super(view);

        // Initialize the main view with the origin text area focused
        view.getOriginTextArea().focusCode();
    }

    //#endregion Constructor

    //#region Implemented Methods

    @Override
    protected void updateView() {

    }

    @Override
    protected void registerListeners() {
        view.getRunButton().addActionListener(new RunActionListener(this));
        view.getSwitchButton().addActionListener(new SwitchActionListener(autoRun));
        view.getOptionsButton().addActionListener(new OptionActionListener(this, autoRun));

        view.getOriginTextArea().registerListener(new CodeKeyListener(this, view.getOriginTextArea()));
    }

    //#endregion Implemented Methods

    public void setAutoRun(boolean b) {
        autoRun = b;
        this.autoRun();
    }

    public void setOriginTextAreaCode(String code) {
        view.getOriginTextArea().setCode(code);
    }

    public void triggerAutoRun() {
        autoRun();
    }

    private void autoRun() {
        if (autoRun) {
            run();
        }
    }

    public void run() {
        try {
            for (CodeTextArea textArea : view.getCodeTextArea()) {
                try {
                    String code = translateToOther(view.getOriginTextArea().getCode(), textArea.getLanguage());
                    textArea.setCode(code, Color.BLACK);
                } catch (Exception e) {
                    textArea.setCode(e.getMessage(), Color.RED);
                }
            }
        } catch (Exception e) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, e.getMessage());
        }

    }
}
