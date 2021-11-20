package alcatraz.client;

import alcatraz.common.Lobby;
import alcatraz.common.Move;
import alcatraz.server.IServer;
import alcatraz.common.User;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClientImpl implements IClient {


    static Registry reg;

    static IServer stub;

    static int numberOfClients = 0;


    private ArrayList<IClient> clientStubs = new ArrayList<>();

    //User of this client
    private User thisUser = new User();

    //Lobby of this client
    private Lobby lobby;

    private GameConnection gameConnection;
    private UIGameWindow UIGameWindow;
    private UserInterfaceLobbies userInterfaceLobbies;

    private boolean rmiStarted=false;


    public boolean isRmiStarted() {
        return rmiStarted;
    }

    public UserInterfaceLobbies getUserInterfaceLobbies() {
        return userInterfaceLobbies;
    }

    public void setUserInterfaceLobbies(UserInterfaceLobbies userInterfaceLobbies) {
        this.userInterfaceLobbies = userInterfaceLobbies;
    }

    public void setGameConnection(GameConnection gameConnection) {
        this.gameConnection = gameConnection;
    }

    public void setGameWindow(UIGameWindow UIGameWindow) {
        this.UIGameWindow = UIGameWindow;
    }

    public User getThisUser() {
        return thisUser;
    }

    public void setThisUser(User thisUser) {
        this.thisUser = thisUser;
    }

    public void init(String username) {
        thisUser.setUserId(UUID.randomUUID());
        thisUser.setUsername(username);
    }


    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public ClientImpl() {


        this.thisUser.setRmiPort(1100 + numberOfClients);


        numberOfClients++;
    }



    public void connectToServer() {
        try {
            Registry reg = LocateRegistry.getRegistry();
            stub = (IServer) reg.lookup("Server");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //!!! Client to server RMI function begin
    public void serverJoinLobby(UUID lobbyID) throws RemoteException {
        stub.joinLobby(thisUser, lobbyID);
    }

    public Lobby serverCreateLobby() throws RemoteException {
        Lobby lob = stub.createLobby(thisUser);
        this.lobby = lob;
        return lob;

    }

    public void serverLeaveLobby(UUID lobbyID) throws RemoteException {
        stub.leaveLobby(thisUser, lobbyID);
    }

    public List<Lobby> serverGetLobbies() throws RemoteException {
        List<Lobby> result = stub.availableLobbies();
        System.out.println(result);

        return result;
    }

    public Lobby serverStartGame() throws RemoteException {
        return stub.startGame(lobby.getLobbyId());
    }


    //!!! Client to server RMI function end


    public void sendUsersToOtherClients() throws RemoteException {
        for (IClient stub : this.clientStubs) {
            stub.presentPlayers(this.lobby);
        }
    }

    public void sendStartToOutherClients() throws RemoteException {
        for (IClient stub : this.clientStubs) {
            stub.startGame();
        }
    }

    public void startClientRMI() throws RemoteException {
        IClient clientStub = (IClient) UnicastRemoteObject.exportObject(this, 0);
        reg = LocateRegistry.createRegistry(thisUser.getRmiPort());
        reg = LocateRegistry.getRegistry(thisUser.getRmiPort());
        System.out.println("user/+ " + this.thisUser.getUsername() + "con= " + "client/" + this.thisUser.getUsername());
        reg.rebind("client/" + this.thisUser.getUsername(), clientStub);

        rmiStarted=true;
    }

    public void connectToTheClients() throws RemoteException, NotBoundException {
        for (User user : this.lobby.getUsers()) {
            if (!user.getUsername().equals(thisUser.getUsername())) {
                Registry reg = LocateRegistry.getRegistry(user.getRmiPort());

                System.out.println("verbinde zu " + "client/" + user.getUsername()+ " von "+thisUser.getUsername());
                IClient clientStub = (IClient) reg.lookup("client/" + user.getUsername());
                clientStubs.add(clientStub);
            }
        }
    }


    //??? Client to Client RMI function begin ???
    @Override
    public void presentPlayers(Lobby lobby) throws RemoteException {
        System.out.println("reciver ="+thisUser.getUsername()+" "+lobby);
        if (!this.lobby.equals(lobby)) {
            System.out.println("reciver ="+thisUser.getUsername()+" changed lobby");
            this.lobby = lobby;
        }
        try {
            if(!rmiStarted) {
                startClientRMI();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void Move(String username, Move move) throws RemoteException {

    }

    @Override
    public void startGame() throws RemoteException {
        //TODO controller benachrichten und start game

        System.out.println("reciver ="+thisUser.getUsername() +"Start game!!! ");

     //   userInterfaceLobbies.closeWindow();
        UIGameWindow.start();

    }

    @Override
    public void endGame() throws RemoteException {

    }

    @Override
    public GameStatus rejoinGame() throws RemoteException {
        return null;
    }

    //??? Client to Client RMI function end ???

}
