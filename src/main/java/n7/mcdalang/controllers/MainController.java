package n7.mcdalang.controllers;

import n7.mcdalang.listeners.*;
import n7.mcdalang.listeners.mcdabot.McdaBotActionListener;
import n7.mcdalang.util.GlobalInstances;
import n7.mcdalang.util.app.AppConfig;
import n7.mcdalang.util.font.Fonts;
import n7.mcdalang.util.theme.Themes;
import n7.mcdalang.views.MainView;
import n7.mcdalang.views.components.util.CodeTextArea;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static n7.mcdalang.models.antlr.Translate.translateToOther;

public class MainController extends Controller<MainView> {

    //#region Constructor

    public MainController(MainView view) {
        super(view);

        // Initialize the main view with the origin text area focused
        view.getOriginTextArea().focusCode();
    }

    //#endregion Constructor

    //#region Overriden Methods

    @Override
    protected void updateView() {
        // No specific updates needed for the main view at this time.
        setFont(GlobalInstances.getAppSettings().getFont());
        setFontSize(GlobalInstances.getAppSettings().getFontSize());
    }

    @Override
    protected void registerListeners() {
        view.getRunButton().addActionListener(new RunActionListener(this));
        view.getSwitchButton().addActionListener(new SwitchActionListener());
        view.getOptionsButton().addActionListener(new OptionActionListener(this));
        view.getMcdaBotButton().addActionListener(new McdaBotActionListener(this));
        view.getOriginTextArea().registerListener(new CodeKeyListener(this, view.getOriginTextArea()));
        view.getImportButton().addActionListener(new ImportActionListener(this));
        view.getExportButton().addActionListener(new ExportActionListener());
    }

    //#endregion Overriden Methods

    //#region Public Methods

    public void triggerChangeForOriginCode(String code) {
        changeOriginCode(code);
    }

    public void triggerAutoRun() {
        autoRun();
    }

    public void run() {
        try {
            for (CodeTextArea textArea : view.getCodeTextAreas()) {
                runTranslation(textArea);
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

    public void setFont(Fonts font) {
        GlobalInstances.getAppSettings().setFont(font);
        for (CodeTextArea textArea : view.getCodeTextAreas()) {
            textArea.setFont(textArea.createFont(AppConfig.FONT_ADAPTERS.get(font)));
        }
        view.getOriginTextArea().setFont(view.getOriginTextArea().createFont(AppConfig.FONT_ADAPTERS.get(font)));
    }

    public void setTheme(Themes theme) {
        GlobalInstances.getAppSettings().setTheme(theme);

    }

    //#endregion Public Methods

    //#region Private Methods

    private void changeOriginCode(String newCode){
        if(newCode != null){
            view.getOriginTextArea().setCode(newCode);
        }
    }

    private void autoRun() {
        if (GlobalInstances.getAppSettings().getAutoRun()) {
            run();
        }
    }

    private void runTranslation(CodeTextArea textArea) {
        try {
            String code = translateToOther(view.getOriginTextArea().getCode(), textArea.getLanguage());
            textArea.setCode(code, Color.BLACK);
        } catch (Exception e) {
            textArea.setCode(e.getMessage(), Color.RED);
        }
    }

    //#region Private Methods
}
