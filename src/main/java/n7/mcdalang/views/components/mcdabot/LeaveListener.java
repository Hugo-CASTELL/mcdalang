package n7.mcdalang.views.components.mcdabot;

import n7.mcdalang.views.McdaBotMainView;
import n7.mcdalang.views.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LeaveListener implements ActionListener {
    private View view;

    public LeaveListener(View view) {
        this.view = view;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        McdaBotMainView mainView = (McdaBotMainView) this.view.getParent();
        if (this.view instanceof MenuTab){
            mainView.showMainView();
        }
        else if (this.view instanceof ExampleTab){
            mainView.showMenuTab();
        }



    }
}
