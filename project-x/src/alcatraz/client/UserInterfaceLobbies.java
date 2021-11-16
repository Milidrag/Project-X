package alcatraz.client;

import javax.swing.*;

public class UserInterfaceLobbies {


    private JPanel root;
    private JPanel usernamePanel;
    private JTextField userNameTextField;
    private JLabel userNameInfoLabel;
    private JScrollPane lobbiesScrollPane;
    private JButton createLobbyButton;


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
    }

    public static void main(String[] args) {
       UserInterfaceLobbies userInterfaceLobbies =new UserInterfaceLobbies();


    }





}
