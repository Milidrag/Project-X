package alcatraz.client;

import javax.swing.*;

public class GameWindow {
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
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void generateGame(){

        gamePanel.add(gameConnection.getGameWindow());
    }

    public GameWindow() {

    }
}