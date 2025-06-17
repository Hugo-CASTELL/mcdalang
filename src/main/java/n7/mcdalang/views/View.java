package n7.mcdalang.views;

import javax.swing.*;

public abstract class View extends JPanel {

    //#region Fields

    private int popupOptions;

    //#region Fields

    //#region Getters and Setters

    public int getPopupOptions() {
        return popupOptions;
    }

    public void setPopupOptions(int options) {
        this.popupOptions = options;
    }

    //#endregion Getters and Setters

}
