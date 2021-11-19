package alcatraz.client;

import javax.swing.*;

public class UIGameWindow {
    private ClientImpl client;

    private JPanel gamePanel;
    private JPanel root;

    private GameConnection gameConnection;


    public void setGameConnection(GameConnection gameConnection) {
        this.gameConnection = gameConnection;
    }

    //draws the Window
    public void generateWindow() {
        JFrame frame = new JFrame("UserInterface");
        frame.setContentPane(this.root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void generateGame(){

        gamePanel.add(gameConnection.getGameWindow());
    }

    public UIGameWindow(ClientImpl client) {
        this.client=client;
    }

    public void start() {
        generateWindow();
    }
}
