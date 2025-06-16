package n7.mcdalang.views;

import n7.mcdalang.views.components.mcdabot.MenuTab;

import javax.swing.*;
import java.awt.*;

public class McdaBotMainView extends View {

    //#region Fields

    private final MenuTab menuTab;

    //#endregion Fields

    //#region Constructor

    public McdaBotMainView() {
        this.setLayout(new BorderLayout());
        menuTab = new MenuTab();
        show(menuTab);
    }

    //#endregion Constructor

    //#region Getters

    public MenuTab getMenuTab(){
        return menuTab;
    }

    //#endregion Getters

    //#region Public Methods

    public void show(JPanel tab) {
        this.removeAll();
        this.add(tab, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }

    //#region Public Methods
}
