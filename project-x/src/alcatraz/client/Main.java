package alcatraz.client;

import java.rmi.RemoteException;

public class Main {

    private ClientImpl client=new ClientImpl();
    private GameConnection gameConnection;
    private UIGameWindow UIGameWindow;
    private UserInterfaceLobbies userInterfaceLobbies;

    private void init(){
        //init objects
        userInterfaceLobbies=new UserInterfaceLobbies(client);
        gameConnection=new GameConnection(client);
        UIGameWindow =new UIGameWindow(client);

        //set
        userInterfaceLobbies.setGameWindow(UIGameWindow);
        UIGameWindow.setGameConnection(gameConnection);
        client.setGameConnection(gameConnection);
        client.setGameWindow(UIGameWindow);

        client.connectToServer();

        userInterfaceLobbies.generateWindow();
    }




    public static void main(String[] args) {
      Main controller=new Main();
      controller.init();

    }

}
