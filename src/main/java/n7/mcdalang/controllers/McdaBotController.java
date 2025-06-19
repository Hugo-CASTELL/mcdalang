package n7.mcdalang.controllers;

import com.ibm.icu.impl.Pair;
import n7.mcdalang.listeners.mcdabot.ExampleTabListener;
import n7.mcdalang.listeners.mcdabot.LeaveListener;
import n7.mcdalang.views.McdaBotMainView;
import n7.mcdalang.views.components.mcdabot.ExampleTab;
import n7.mcdalang.views.components.mcdabot.examples.CodeCompleteTab;
import n7.mcdalang.views.components.mcdabot.examples.*;

import java.util.List;

public class McdaBotController extends Controller<McdaBotMainView> {

    //#region Fields

    private final MainController mainController;

    //#region Fields

    //#region Constructor

    public McdaBotController(McdaBotMainView view, MainController mainController) {
        super(view);
        this.mainController = mainController;
    }

    //#endregion Constructor

    //#region Overriden Methods

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
            Pair.of("Operations",new OperationTab()),
            Pair.of("Exemple concret",new CodeCompleteTab())
        );

        for (Pair<String, ExampleTab> exampleTabPair : exampleTabs) {
            ExampleTab tab = exampleTabPair.second;
            tab.getTryCodeButton().addActionListener(new LeaveListener(() -> {
                mainController.triggerChangeForOriginCode(tab.getCode());
                mainController.run();
                mainController.show();
            }));
            tab.getLeaveButton().addActionListener(new LeaveListener(() -> this.view.show(this.view.getMenuTab())));
            view.getMenuTab().createOption(exampleTabPair.first, new ExampleTabListener(tab, this));
        }

        view.getMenuTab().getBtnLeave().addActionListener(new LeaveListener(mainController::show));
    }

    //#endregion Overriden Methods

    //#region Public Methods

    public void triggerShow(ExampleTab exampleTab) {
        changeTab(exampleTab);
    }

    //#endregion Public Methods

    //#region Private Methods

    private void changeTab(ExampleTab tab) {
        view.show(tab);
    }

    //#endregion Private Methods

}
