package n7.mcdalang.controllers;

import com.ibm.icu.impl.Pair;
import n7.mcdalang.input.mcdabot.ExampleTabListener;
import n7.mcdalang.input.mcdabot.LeaveListener;
import n7.mcdalang.views.McdaBotMainView;
import n7.mcdalang.views.components.mcdabot.ExampleTab;
import n7.mcdalang.views.components.mcdabot.example.*;

import java.util.List;

public class McdaBotController extends Controller<McdaBotMainView> {

    private MainController mainController;

    public McdaBotController(McdaBotMainView view, MainController mainController) {
        super(view);
        this.mainController = mainController;
    }

    @Override
    protected void updateView() {
        // No updates needed for the splash screen
    }

    @Override
    protected void registerListeners() {
        List<Pair<String, ExampleTab>> exampleTabs = List.of(
            Pair.of("Variable et types\n", new VarTab()),
            Pair.of("Boucles",new LoopTab()),
            Pair.of("Conditions",new ConditionTab()),
            Pair.of("Fonctions",new FuncTab()),
            Pair.of("Blocs",new BaseSyntaxTab()),
            Pair.of("Commentaires",new CommentTab()),
            Pair.of("Operations",new OperationTab())
        );

        for (Pair<String, ExampleTab> exampleTabPair : exampleTabs) {
            ExampleTab tab = exampleTabPair.second;
            tab.getBtnTryCode().addActionListener(new LeaveListener(() -> {
                mainController.triggerChangeForOriginCode(tab.getCode());
                mainController.show();
            }));
            tab.getBtnLeave().addActionListener(new LeaveListener(() -> this.view.show(this.view.getMenuTab())));
            view.getMenuTab().createOption(exampleTabPair.first, new ExampleTabListener(tab, this));
        }

        view.getMenuTab().getBtnLeave().addActionListener(new LeaveListener(mainController::show));
    }

    public void triggerShow(ExampleTab exampleTab) {
        view.show(exampleTab);
    }

    @Override
    public void show() {
        super.show();
    }
}
