package alcatraz.server;

import alcatraz.common.Lobby;
import alcatraz.common.User;
import spread.SpreadConnection;
import spread.SpreadException;
import spread.SpreadGroup;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import spread.*;

public class ServerImpl implements IServer {
    static Registry reg;
    private boolean isRunning = true;
    private String serverId;
    private SpreadGroup myGroup;
    private SpreadGroup serverGroup;



    LobbyManager lobbyManager = new LobbyManager();

    public static void main(String[] args) {
        ServerImpl remoteObject = new ServerImpl();
        remoteObject.registerForRMI();

        remoteObject.test();//add lobbies for testing

        while(remoteObject.GetIsRunning()) {
            try {
                Thread.sleep(17000);
            } catch (InterruptedException ex) {
                //TODO or not, whatever
            }
        }
        System.out.println("Programm ended");


    }

    private boolean GetIsRunning() {
        return isRunning;
    }

    public ServerImpl(){
        this.serverId = UUID.randomUUID().toString();
        SpreadConnection newConnection = new SpreadConnection();
        try {
            newConnection.connect(InetAddress.getByName("127.0.0.1"), 4803, this.serverId, false, false);
            this.serverGroup = initSpreadGroup(newConnection, "spreadGroupName");

        } catch (SpreadException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }


    }

    private SpreadGroup initSpreadGroup(SpreadConnection newConnection, String spreadGroupName) {
            SpreadGroup group = new SpreadGroup();
            try {
                group.join(newConnection, spreadGroupName);
            }
            catch (SpreadException ex) {
                System.err.println("Spread Exception: " +ex.getMessage() + Arrays.toString(ex.getStackTrace()));
            }
            return group;
    }


    //generate Lobbies for testing
    private void test() {
        Lobby lobby1 = new Lobby();
        Lobby lobby2 = new Lobby();
        Lobby lobby3 = new Lobby();

        User user1 = new User("test");

        User user2 = new User("test User 2");

        User user3 = new User("test User 3");

        lobby1.addPlayer(user1);
        lobby2.addPlayer(user2);
        lobby3.addPlayer(user3);

        lobbyManager.getLobbies().add(lobby1);
        lobbyManager.getLobbies().add(lobby2);
        lobbyManager.getLobbies().add(lobby3);
    }

    public void registerForRMI() {
        try {
            IServer stub = (IServer) UnicastRemoteObject.exportObject(this, 0);
            reg = LocateRegistry.createRegistry(1099);
            reg = LocateRegistry.getRegistry(1099);
            reg.rebind("Server", stub);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public LobbyManager getLobbyManager() {
        return lobbyManager;
    }

    //TODO: Methoden aus der Präsentation implmentieren

    @Override
    public List<Lobby> availableLobbies()throws RemoteException {
        return lobbyManager.getLobbies();
    }

    @Override
    public boolean joinLobby(User user, UUID lobbyId) throws RemoteException, AssertionError {
        try {
            if (lobbyManager.checkIfUsernameIsUsed(user.getUsername())||user.getUsername()==null) {

                throw new AssertionError("Username already taken");
            } else {
                if (lobbyManager.getLobby(lobbyId).getUsers().size() >= 4) {
                    throw new RemoteException("Lobby is full");

                } else {
                    lobbyManager.addUser(user, lobbyId);
                    return true;
                }
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Lobby createLobby(User user) throws RemoteException, AssertionError {
        if (lobbyManager.checkIfUsernameIsUsed(user.getUsername())||user.getUsername()==null) {
            throw new AssertionError("Username already taken");
        } else {
            return lobbyManager.genLobby(user);
        }
    }

    @Override
    public boolean leaveLobby(User user, UUID lobbyId) throws RemoteException{
        try {
            lobbyManager.removeUserFromLobby(user, lobbyId);
            return true;
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public Lobby startGame(UUID lobbyID) throws RemoteException , NoSuchElementException {
        try {

            int userCountInLobby  = lobbyManager.getLobby(lobbyID).getUsers().size();
            if(userCountInLobby<2||userCountInLobby>4){
                throw new RemoteException("wrong Lobby size");
            }else {
                return lobbyManager.changeLobbyStatus(lobbyID);
            }
        }catch (Exception exception){
            throw new RemoteException();
        }

    }

}
