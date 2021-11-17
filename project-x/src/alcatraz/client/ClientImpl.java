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


        this.thisUser.setRmiPort(1100+numberOfClients);


        numberOfClients++;
    }

//    public static void main(String[] args) {
//        ClientImpl client = new ClientImpl();
//    }

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

    public void serverCreateLobby() throws RemoteException {
        this.lobby = stub.createLobby(thisUser);

    }

    public void serverLeaveLobby(UUID lobbyID) throws RemoteException {
        stub.leaveLobby(thisUser, lobbyID);
    }

    public List<Lobby> serverGetLobbies() throws RemoteException {
        List<Lobby> result=stub.availableLobbies();
        System.out.println(result);

        return result ;
    }

    public void sendUsersToOtherClients() throws RemoteException {
        for (IClient stub : this.clientStubs) {
            stub.presentPlayers(this.lobby);
        }
    }


    //!!! Client to server RMI function end

    public void startClientRMI() throws RemoteException {
        IClient clientStub = (IClient) UnicastRemoteObject.exportObject(this, 0);
        reg = LocateRegistry.createRegistry(thisUser.getRmiPort());
        reg = LocateRegistry.getRegistry(thisUser.getRmiPort());
        System.out.println("user/+ " + this.thisUser.getUsername() + "con= " + "client/" + this.thisUser.getUsername());
        reg.rebind("client/" + this.thisUser.getUsername(), clientStub);
    }

    public void connectToTheClients() throws RemoteException, NotBoundException {
        for (User user : this.lobby.getUsers()) {
            if (user != this.thisUser) {
                Registry reg = LocateRegistry.getRegistry(user.getRmiPort());

                System.out.println("verbinde zu " + "client/" + user.getUsername());
                IClient clientStub = (IClient) reg.lookup("client/" + user.getUsername());
                clientStubs.add(clientStub);
            }
        }
    }


    //??? Client to Client RMI function begin ???
    public void presentPlayers(Lobby lobby)throws RemoteException {
        System.out.println(lobby);

        if(!this.lobby.equals(lobby)){
            throw new RemoteException();
        }


    }

    public void Move(String username, Move move)throws RemoteException {

    }

    @Override
    public void startGame()throws RemoteException {

    }

    @Override
    public void endGame()throws RemoteException {

    }

    @Override
    public GameStatus rejoinGame()throws RemoteException {
        return null;
    }

    //??? Client to Client RMI function end ???

}
