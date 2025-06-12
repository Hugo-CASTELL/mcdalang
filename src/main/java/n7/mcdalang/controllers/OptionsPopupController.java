package n7.mcdalang.controllers;

import n7.mcdalang.input.ExportActionListener;
import n7.mcdalang.input.ImportActionListener;
import n7.mcdalang.util.GlobalInstances;
import n7.mcdalang.views.OptionsPopupView;

public class OptionsPopupController extends Controller<OptionsPopupView> {

    //#region Fields

    private MainController mainController;

    //#endregion Fields

    //#region Constructor

    public OptionsPopupController(OptionsPopupView view, MainController mainController) {
        super(view);
        this.mainController = mainController;
    }

    //#endregion Constructor

    //#region Implemented Methods

    @Override
    protected void updateView() {
        view.getAutoRunCheckBox().setSelected(GlobalInstances.getAppSettings().getAutoRun());
    }

    @Override
    protected void registerListeners() {
        view.getExportButton().addActionListener(new ExportActionListener());
        view.getImportButton().addActionListener(new ImportActionListener(this));
    }

    //#endregion Implemented Methods

    public boolean hasSelectedAutoRun() {
        return view.getAutoRunCheckBox().isSelected();
    }

    public void triggerChangeForOriginCode(String code){
        this.mainController.triggerChangeForOriginCode(code);
    }

}
