package n7.mcdalang.views.components.mcdabot;

import n7.mcdalang.views.McdaBotMainView;
import n7.mcdalang.views.View;
import n7.mcdalang.views.components.main.CodeTextArea;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TryCode implements ActionListener {
    private final View menuTab;
    private final CodeTextArea zoneCode;

    public TryCode(View menuTab, CodeTextArea zoneCode) {
        this.menuTab = menuTab;
        this.zoneCode = zoneCode;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        McdaBotMainView mcdaBotMainView = (McdaBotMainView) this.menuTab.getParent();
        mcdaBotMainView.getMainViewController().triggerChangeForOriginCode(this.zoneCode.getCode());
        mcdaBotMainView.showMainView();
    }
}
