package n7.mcdalang.input;

import n7.mcdalang.models.antlr.Languages;
import n7.mcdalang.util.GlobalInstances;
import n7.mcdalang.util.file.FileUtils;
import n7.mcdalang.views.MainView;
import n7.mcdalang.views.components.util.CodeTextArea;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

public class ExportActionListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (GlobalInstances.getAppManager().getMainFrameCurrentView() instanceof MainView mainView) {
            JFileChooser dirChooser = new JFileChooser(new File(System.getProperty("user.dir")));
            dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            dirChooser.setAcceptAllFileFilterUsed(false);

            if(dirChooser.showOpenDialog(new Frame()) != JFileChooser.APPROVE_OPTION)
                return;

            try {
                File selectedDir = dirChooser.getSelectedFile();
                File wrappedDir = new File(selectedDir, "mcdalang_export");

                if (!wrappedDir.exists() && !wrappedDir.mkdirs()) {
                    throw new FileNotFoundException("Failed to create directory: " + selectedDir.getAbsolutePath());
                }

                FileUtils.writeFile(new File(wrappedDir, Languages.MCDALANG.toString()), mainView.getOriginTextArea().getCode());
                for (CodeTextArea codeTextArea : mainView.getCodeTextAreas()) {
                    FileUtils.writeFile(new File(wrappedDir, codeTextArea.getLanguage().toString()), codeTextArea.getCode());
                }

                JOptionPane.showMessageDialog(new Frame(), "Successfully exported to " + wrappedDir.getPath(), "Export success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(new Frame(), "Error while importing", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
