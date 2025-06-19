package n7.mcdalang.input.mcdabot;

import n7.mcdalang.controllers.MainController;
import n7.mcdalang.controllers.McdaBotController;
import n7.mcdalang.views.McdaBotMainView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class mcdaBotActionListener implements ActionListener {
    private MainController mainController;
    private McdaBotController mcdaBotController;

    public mcdaBotActionListener(MainController mainController) {
        System.out.println("new mcdaBotActionListener");
        this.mainController = mainController;
        McdaBotMainView mcdaBotMainView = new McdaBotMainView();
        this.mcdaBotController = new McdaBotController(mcdaBotMainView);
        mcdaBotMainView.setMainViewController(this.mainController);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.mcdaBotController.show();
    }


}
