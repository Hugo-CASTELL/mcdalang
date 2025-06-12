package n7.mcdalang.views;

import javax.swing.*;

public abstract class View extends JPanel {
    private int popupOptions;

    public void setPopupOptions(int options) {
        this.popupOptions = options;
    }

    public int getPopupOptions() {
        return popupOptions;
    }
}
