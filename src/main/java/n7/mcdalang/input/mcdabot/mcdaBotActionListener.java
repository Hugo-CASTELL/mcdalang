package n7.mcdalang.input.mcdabot;

import n7.mcdalang.controllers.MainController;
import n7.mcdalang.controllers.McdaBotController;
import n7.mcdalang.views.McdaBotMainView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class mcdaBotActionListener implements ActionListener {
    private MainController mainController;

    public mcdaBotActionListener(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        McdaBotMainView mcdaBotMainView = new McdaBotMainView();
        new McdaBotController(mcdaBotMainView).show();
        mcdaBotMainView.setMainViewController(this.mainController);
    }


}
