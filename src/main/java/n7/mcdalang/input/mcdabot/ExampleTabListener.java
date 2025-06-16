package n7.mcdalang.input.mcdabot;

import n7.mcdalang.views.McdaBotMainView;
import n7.mcdalang.views.View;
import n7.mcdalang.views.components.mcdabot.ExampleTab;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExampleTabListener implements ActionListener {
    private final ExampleTab frame;
    private final View menuTab;


    public ExampleTabListener(ExampleTab frame, View menuTab) {
        this.frame = frame;
        this.menuTab = menuTab;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        McdaBotMainView mainView = (McdaBotMainView) this.menuTab.getParent();
        this.frame.reset();
        mainView.show(this.frame);
    }
}
