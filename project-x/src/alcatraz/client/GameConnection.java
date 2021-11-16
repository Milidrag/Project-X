package alcatraz.client;

import at.falb.games.alcatraz.api.Alcatraz;

import javax.swing.*;


public class GameConnection {

    private Alcatraz alcatraz =new Alcatraz();
    private GameWindow gameWindow=new GameWindow();

    public GameConnection() {
        gameWindow.setGameConnection(this);
        gameWindow.generateWindow();
        gameWindow.generateGame();

    }

    public JPanel getGameWindow(){
        return alcatraz.getGameBoard();
    }




    public static void main(String[] args) {
       GameConnection gameConnection=new GameConnection();


    }
}
