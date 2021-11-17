package alcatraz.client;

public class Controller {

    private ClientImpl client=new ClientImpl();
    private GameConnection gameConnection;
    private GameWindow gameWindow;
    private UserInterfaceLobbies userInterfaceLobbies;

    private void init(){
        client.connectToServer();
        userInterfaceLobbies=new UserInterfaceLobbies(client);
        gameConnection=new GameConnection(client);
        gameWindow=new GameWindow(client);


        userInterfaceLobbies.generateWindow();

    }





    public static void main(String[] args) {
      Controller controller=new Controller();
      controller.init();

    }

}
