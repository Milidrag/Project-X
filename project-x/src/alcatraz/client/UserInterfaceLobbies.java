package alcatraz.client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserInterfaceLobbies {


    private JPanel root;
    private JPanel usernamePanel;
    private JTextField userNameTextField;
    private JLabel userNameInfoLabel;
    private JScrollPane lobbiesScrollPane;
    private JButton createLobbyButton;
    private JButton startGamebutton;


    public void init() {
        startGamebutton.setVisible(false);
        createLobbyButton.addActionListener(e -> {
            if (!(userNameTextField.getText().equals("") || userNameTextField.getText() == null)) {
                    userNameTextField.setEnabled(false);
            } else {
                userNameTextField.setText("Please enter a valid username!");

            }

        });
        startGamebutton.addActionListener(e -> {

        });

    }

    //draws the Window
    public void generateWindow() {
        JFrame frame = new JFrame("UserInterface");
        frame.setContentPane(this.root);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public UserInterfaceLobbies() {
        generateWindow();
        init();

    }

    public static void main(String[] args) {
        UserInterfaceLobbies userInterfaceLobbies = new UserInterfaceLobbies();


    }


}
