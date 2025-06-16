package n7.mcdalang.views.components.mcdabot;

import n7.mcdalang.views.McdaBotMainView;
import n7.mcdalang.views.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExampleTabListener implements ActionListener {
    private String explication;
    private String code;
    private View menuTab;


    public ExampleTabListener(String explication, String code, View menuTab) {
        this.explication = explication;
        this.code = code;
        this.menuTab = menuTab;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        McdaBotMainView mainView = (McdaBotMainView) this.menuTab.getParent();
        mainView.show(new ExampleTab(this.explication, this.code));
    }
}
