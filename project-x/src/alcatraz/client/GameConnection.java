package alcatraz.client;


import at.falb.games.alcatraz.api.Alcatraz;

import javax.swing.*;


public class GameConnection {


    private Alcatraz alcatraz = new Alcatraz();
    private UIGameWindow UIGameWindow;
    private ClientImpl client;

    public ClientImpl getClient() {
        return client;
    }

    public void setGameWindow(UIGameWindow UIGameWindow) {
        this.UIGameWindow = UIGameWindow;
    }

    public void setClient(ClientImpl client) {
        this.client = client;
    }


    public GameConnection(ClientImpl client) {
        this.client = client;

    }



//    public void sartUI(){
//
//        UIGameWindow.setGameConnection(this);
//        UIGameWindow.generateWindow();
//        UIGameWindow.generateGame();
//    }

    public JPanel getGameWindow() {
        return alcatraz.getGameBoard();
    }

    public void initGame() {
        int numPlayers = client.getLobby().getUsers().size();
        if (numPlayers >= 2 && numPlayers <= 4) {
            alcatraz.init(numPlayers, 0);
        }
    }


}
