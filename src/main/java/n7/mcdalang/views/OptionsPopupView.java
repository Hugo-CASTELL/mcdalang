package n7.mcdalang.views;

import n7.mcdalang.util.app.AppConfig;
import n7.mcdalang.util.font.Fonts;
import n7.mcdalang.util.theme.Themes;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OptionsPopupView extends View {

    //#region Fields

    private final JCheckBox autoRunCheckBox;

    private final JSpinner fontSizeSpinner;

    private final ButtonGroup fontGroup;

    private final ButtonGroup themeGroup;

    //#endregion Fields

    //#region Constructor

    public OptionsPopupView() {
        setName("Options");
        setPopupOptions(JOptionPane.OK_CANCEL_OPTION);
        setLayout(new MigLayout("fill"));

        autoRunCheckBox = new JCheckBox("AutoRun");
        add(autoRunCheckBox, "cell " + (2) + " 0");

        JLabel fontSizeLabel = new JLabel("Font Size :");

        add(fontSizeLabel, "cell 0 0, alignx right");
        SpinnerModel fontSizeSpinnerModel = new SpinnerNumberModel(
                12, // init value
                8,  // minimum value
                72, // maximum value
                1); // step value
        fontSizeSpinner = new JSpinner(fontSizeSpinnerModel);
        add(fontSizeSpinner, "cell 1 0, growx");

        fontGroup = new ButtonGroup();
        List<JRadioButton> fontRadioButtons = new ArrayList<>();
        for (Map.Entry<Fonts, URL> entry : AppConfig.FONT_ADAPTERS.entrySet()) {
            JRadioButton radioButton = new JRadioButton(entry.getKey().toString());
            fontRadioButtons.add(radioButton);
            fontGroup.add(radioButton);
            int col = (fontRadioButtons.size() - 1) % 3;
            int row = 3 + (fontRadioButtons.size() - 1) / 3;
            add(radioButton, "cell " + col + " " + row);
        }

        themeGroup = new ButtonGroup();
        List<JRadioButton> themeRadioButtons = new ArrayList<>();
        int themeRow = 3;
        for (Themes themes : Themes.values()) {
            JRadioButton radioButton = new JRadioButton(themes.toString());
            themeRadioButtons.add(radioButton);
            themeGroup.add(radioButton);
            add(radioButton, "cell 4 " + themeRow);
            themeRow++;
        }

    }

    //#endregion Constructor

    //#region Getters

    public JCheckBox getAutoRunCheckBox() {
        return autoRunCheckBox;
    }

    public JSpinner getFontSizeSpinner() {
        return fontSizeSpinner;
    }

    public ButtonGroup getFontGroup() {
        return fontGroup;
    }

    public ButtonGroup getThemeGroup() {
        return  themeGroup;
    }

    //#endregion Getters

}
