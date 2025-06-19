package n7.mcdalang.controllers;

import n7.mcdalang.util.GlobalInstances;
import n7.mcdalang.util.app.AppConfig;
import n7.mcdalang.util.font.Fonts;
import n7.mcdalang.util.theme.Themes;
import n7.mcdalang.views.OptionsPopupView;

import javax.swing.*;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

public class OptionsPopupController extends Controller<OptionsPopupView> {

    //#region Constructor

    public OptionsPopupController(OptionsPopupView view) {
        super(view);
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

        Collections.list(view.getThemeGroup().getElements()).forEach(button -> {
            if  (button.getText().equals(GlobalInstances.getAppSettings().getTheme().toString())) {
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

    public int getSelectedFontSize() {
        return (Integer) view.getFontSizeSpinner().getValue();
    }

    public Fonts getSelectedFont() {
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

    public Themes getSelectedTheme() {
        Enumeration<AbstractButton> buttonsIterator = view.getThemeGroup().getElements();
        while (buttonsIterator.hasMoreElements()) {
            AbstractButton button = buttonsIterator.nextElement();
            if (button.isSelected()) {
                for (Themes theme : Themes.values()) {
                    if (theme.toString().equals(button.getText())) {
                        return theme;
                    }
                }
                return AppConfig.DEFAULT_THEME;
            }
        }
        return AppConfig.DEFAULT_THEME;
    }

    //#endregion Public Methods

}
