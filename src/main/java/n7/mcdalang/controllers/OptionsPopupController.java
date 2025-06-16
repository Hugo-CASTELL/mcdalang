package n7.mcdalang.controllers;

import n7.mcdalang.util.GlobalInstances;
import n7.mcdalang.util.app.AppConfig;
import n7.mcdalang.util.font.Fonts;
import n7.mcdalang.views.OptionsPopupView;

import javax.swing.*;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

public class OptionsPopupController extends Controller<OptionsPopupView> {

    //#region Fields

    private MainController mainController;

    //#endregion Fields

    //#region Constructor

    public OptionsPopupController(OptionsPopupView view, MainController mainController) {
        super(view);
        this.mainController = mainController;
    }

    //#endregion Constructor

    //#region Implemented Methods

    @Override
    protected void updateView() {
        view.getAutoRunCheckBox().setSelected(GlobalInstances.getAppSettings().getAutoRun());
        view.getFontSizeSpinner().setValue(GlobalInstances.getAppSettings().getFontSize());

        Collections.list(view.getFontGroup().getElements()).forEach(button -> {
            if (button.getText().equals(GlobalInstances.getAppSettings().getFont().toString())) {
                button.setSelected(true);
            }
        });
    }

    @Override
    protected void registerListeners() {
        // No specific listeners to register for the options popup
    }

    //#endregion Implemented Methods

    //#region Public Methods

    public boolean hasSelectedAutoRun() {
        return view.getAutoRunCheckBox().isSelected();
    }

    public int selectFontSize() {
        return (Integer) view.getFontSizeSpinner().getValue();
    }

    public Fonts selectFont() {
        Enumeration<AbstractButton> buttonsIterator = view.getFontGroup().getElements();
        while (buttonsIterator.hasMoreElements()) {
            AbstractButton button = buttonsIterator.nextElement();
            if (button.isSelected()) {
                for (Map.Entry<Fonts, URL> entry : AppConfig.FONT_ADAPTERS.entrySet()) {
                    if (entry.getKey().toString().equals(button.getText())) {
                        return entry.getKey();
                    }
                }
                return AppConfig.DEFAULT_FONT;
            }
        }
        return AppConfig.DEFAULT_FONT;
    }

    public void triggerChangeForOriginCode(String code){
        this.mainController.triggerChangeForOriginCode(code);
    }

    //#endregion Public Methods

}
