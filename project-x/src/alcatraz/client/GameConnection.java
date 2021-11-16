package alcatraz.client;

import alcatraz.common.Lobby;
import alcatraz.common.User;
import at.falb.games.alcatraz.api.Alcatraz;

import javax.swing.*;


public class GameConnection {


    private Alcatraz alcatraz = new Alcatraz();
    private GameWindow gameWindow = new GameWindow();
    private ClientImpl client;

    public ClientImpl getClient() {
        return client;
    }

    public void setClient(ClientImpl client) {
        this.client = client;
    }


    public GameConnection(ClientImpl client) {
        this.client = client;



    }

    public void sartUI(){

        gameWindow.setGameConnection(this);
        gameWindow.generateWindow();
        gameWindow.generateGame();
    }

    public JPanel getGameWindow() {
        return alcatraz.getGameBoard();
    }

    public void initGame() {
        int numPlayers = client.getLobby().getUsers().size();
        if (numPlayers >= 2 && numPlayers <= 4) {
            alcatraz.init(numPlayers, 0);
        }
    }


    public static void main(String[] args) {
        ClientImpl myClient = new ClientImpl();
        myClient.init("player NR.1");

        User user2 = new User("player2");
        Lobby lobby = new Lobby();

        lobby.addPlayer(myClient.getThisUser());
        lobby.addPlayer(user2);

        myClient.setLobby(lobby);


        GameConnection gameConnection = new GameConnection(myClient);
        //gameConnection.initGame();
        gameConnection.sartUI();



    }
}
