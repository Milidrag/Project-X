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
    private int numberofThisClass;

    private ArrayList<IClient> clientStubs = new ArrayList<>();

    //User of this client
    private User thisUser=new User();

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
        numberofThisClass=numberOfClients;
        numberOfClients++;
    }

    public static void main(String[] args) {
        ClientImpl client = new ClientImpl();
        client.connectToServer();
        try {
            client.serverConTest();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void connectToServer() {
        try {
            Registry reg = LocateRegistry.getRegistry();
            stub = (IServer) reg.lookup("Server");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void connectToTheClients() throws RemoteException, NotBoundException {
        for (User user : this.lobby.getUsers()) {
            if (user != this.thisUser) {
                Registry reg = LocateRegistry.getRegistry();

                System.out.println("verbinde zu "+"client/" + user.getUsername());
                IClient clientStub = (IClient) reg.lookup("client/" + user.getUsername());
                clientStubs.add(clientStub);
            }
        }
    }

    public void serverConTest() throws RemoteException {
        System.out.println(stub.availableLobbies().get(0).toString());
    }

    public void sendUsersToOtherClients() throws RemoteException {
        for (IClient stub : this.clientStubs) {
            stub.presentPlayers(this.lobby.getUsers());
        }
    }

    public void startClientRMI() throws RemoteException {
        IClient clientStub = (IClient) UnicastRemoteObject.exportObject(this, 0);
        reg = LocateRegistry.createRegistry(1100 + numberofThisClass);
        reg = LocateRegistry.getRegistry(1100 + numberofThisClass);
        System.out.println("user/+ "+this.thisUser.getUsername()+"con= "+"client/" + this.thisUser.getUsername());
        reg.rebind("client/" + this.thisUser.getUsername(), clientStub);
    }

    //TODO: Methoden aus der Präsentation implmentieren
    public void presentPlayers(List<User> users) {
        System.out.println(users);

    }

    public void Move(String username, Move move) {

    }

}
