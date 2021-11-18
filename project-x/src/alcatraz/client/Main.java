package alcatraz.client;

import java.rmi.RemoteException;

public class Main {

    private ClientImpl client=new ClientImpl();
    private GameConnection gameConnection;
    private GameWindow gameWindow;
    private UserInterfaceLobbies userInterfaceLobbies;

    private void init(){

        userInterfaceLobbies=new UserInterfaceLobbies(client);
        userInterfaceLobbies.setGameWindow(gameWindow);
        gameConnection=new GameConnection(client);
        gameWindow=new GameWindow(client);
        gameWindow.setGameConnection(gameConnection);
        client.setGameConnection(gameConnection);
        client.setGameWindow(gameWindow);


        client.connectToServer();
        try {
            client.startClientRMI();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        userInterfaceLobbies.generateWindow();
    }




    public static void main(String[] args) {
      Main controller=new Main();
      controller.init();

    }

}
