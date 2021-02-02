package gui.Settings;

import javax.swing.*;
import java.awt.*;

/**
 * A gui popup dialog that allows for entering in text and returns text.
 * Once called the application will freeze until the popup is terminated.
 * */
public class RegisterSearch {

    private static final int GUI_WIDTH = 300;
    private static final int GUI_HEIGHT = 150;

    private static String result = null;

    /**
     * Opens a JDialog to allow text to be entered and returns as a string.
     * If there is no text returned or the window is closed it will return null.
     * */
    public static String search() {
        //Set up content
        JPanel mainPanel = new JPanel();
        JPanel containerPanel = new JPanel(new GridLayout(0, 1, 10, 10));

        //Set up text filed
        JLabel messageLabel = new JLabel("Search");
        JTextField inputTextField = new JTextField(15);
        JPanel inputTextFieldPanel = new JPanel(new FlowLayout());
        inputTextFieldPanel.add(messageLabel);
        inputTextFieldPanel.add(inputTextField);

        //Set up buttons
        JButton cancel = new JButton("Cancel");
        JButton run = new JButton("Run");
        JPanel buttonContainerPanel = new JPanel(new FlowLayout());
        buttonContainerPanel.add(cancel);
        buttonContainerPanel.add(run);

        cancel.addActionListener(e -> {
            JButton button = (JButton)e.getSource();
            SwingUtilities.getWindowAncestor(button).dispose();
        });

        run.addActionListener(e -> {
            if(!inputTextField.getText().isEmpty()) {
                result = inputTextField.getText();
            }
            JButton button = (JButton)e.getSource();
            SwingUtilities.getWindowAncestor(button).dispose();
        });


        //Add panels to container
        containerPanel.add(inputTextFieldPanel);
        containerPanel.add(buttonContainerPanel);
        mainPanel.add(BorderLayout.CENTER, containerPanel);

        //Set up dialog box
        JDialog dialog = new JDialog();
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setModal(true);
        dialog.setTitle("Register Search");
        dialog.getContentPane().add(mainPanel);
        dialog.setPreferredSize(new Dimension(GUI_WIDTH, GUI_HEIGHT));
        dialog.setLocationRelativeTo(null);
        dialog.setIconImage(Toolkit.getDefaultToolkit().getImage("src/res/icon/icon.png"));
        dialog.setResizable(false);
        dialog.pack();
        dialog.setVisible(true);

        return result;
    }
    /**
     * Opens a JOptionPane to allow text to be entered and returns as a string.
     * If there is no text returned or the window is closed it will return null.
     * */
    public static String search2() {
        return JOptionPane.showInputDialog(null, "Register Search");
    }

}
