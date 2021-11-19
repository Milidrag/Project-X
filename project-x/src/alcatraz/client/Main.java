package alcatraz.client;

import java.rmi.RemoteException;

public class Main {

    private ClientImpl client=new ClientImpl();
    private GameConnection gameConnection;
    private UIGameWindow UIGameWindow;
    private UserInterfaceLobbies userInterfaceLobbies;

    private void init(){

        userInterfaceLobbies=new UserInterfaceLobbies(client);
        userInterfaceLobbies.setGameWindow(UIGameWindow);
        gameConnection=new GameConnection(client);
        UIGameWindow =new UIGameWindow(client);
        UIGameWindow.setGameConnection(gameConnection);
        client.setGameConnection(gameConnection);
        client.setGameWindow(UIGameWindow);


        client.connectToServer();
//        try {
//            client.startClientRMI();
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }

        userInterfaceLobbies.generateWindow();
    }




    public static void main(String[] args) {
      Main controller=new Main();
      controller.init();

    }

}
