package n7.mcdalang.controllers;

import n7.mcdalang.input.CodeKeyListener;
import n7.mcdalang.input.OptionActionListener;
import n7.mcdalang.input.RunActionListener;
import n7.mcdalang.input.SwitchActionListener;
import n7.mcdalang.util.GlobalInstances;
import n7.mcdalang.views.MainView;
import n7.mcdalang.views.components.CodeTextArea;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static n7.mcdalang.models.antlr.Translate.translateToOther;

public class MainController extends Controller<MainView> {

    //#region Fields

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
        // No specific updates needed for the main view at this time.
        setFontSize(GlobalInstances.getAppSettings().getFontSize());
    }

    @Override
    protected void registerListeners() {
        view.getRunButton().addActionListener(new RunActionListener(this));
        view.getSwitchButton().addActionListener(new SwitchActionListener());
        view.getOptionsButton().addActionListener(new OptionActionListener(this));

        view.getOriginTextArea().registerListener(new CodeKeyListener(this, view.getOriginTextArea()));
    }

    //#endregion Implemented Methods

    public void triggerChangeForOriginCode(String code) {
        changeOriginCode(code);
    }

    private void changeOriginCode(String newCode){
        if(newCode != null){
            view.getOriginTextArea().setCode(newCode);
        }
    }

    public void triggerAutoRun() {
        autoRun();
    }

    private void autoRun() {
        if (GlobalInstances.getAppSettings().getAutoRun()) {
            run();
        }
    }

    public void run() {
        try {
            for (CodeTextArea textArea : view.getCodeTextAreas()) {
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

    public void enableAutoRun(boolean enabled) {
        GlobalInstances.getAppSettings().setAutoRun(enabled);
        this.autoRun();
    }

    public void setFontSize(int fontSize) {
        GlobalInstances.getAppSettings().setFontSize(fontSize);
        for (CodeTextArea textArea : view.getCodeTextAreas()) {
            textArea.setSizeFont(fontSize);
        }
        view.getOriginTextArea().setSizeFont(fontSize);
    }
}
