package n7.mcdalang.input;

import n7.mcdalang.controllers.MainController;
import n7.mcdalang.views.components.util.CodeTextArea;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class CodeKeyListener implements KeyListener {

    private MainController mainController;
    private CodeTextArea codeTextArea;

    public CodeKeyListener(MainController mainController, CodeTextArea codeTextArea) {
        this.mainController = mainController;
        this.codeTextArea = codeTextArea;
    }

    /**
     * Invoked when a key has been typed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key typed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e) {
        SwingUtilities.invokeLater(() -> {
            mainController.triggerAutoRun();
            switch (e.getKeyChar()) {
                case '{' -> codeTextArea.completePreviousCharWith('}');
                case '[' -> codeTextArea.completePreviousCharWith(']');
                case '(' -> codeTextArea.completePreviousCharWith(')');
                case '\'' -> codeTextArea.completePreviousCharWith('\'');
                case '\"' -> codeTextArea.completePreviousCharWith('\"');
                case '\t' -> codeTextArea.addTab();
                case '\n' -> codeTextArea.addNewLine();
                default -> { /* No action for other characters */ }
            }
            codeTextArea.updateLineNumbers();
        });
    }

    /**
     * Invoked when a key has been pressed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key pressed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        // No specific action needed on key pressed
    }

    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {
        // No specific action needed on key released
    }
}
