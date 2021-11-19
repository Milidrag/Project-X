package alcatraz.server;

import alcatraz.common.Lobby;
import alcatraz.common.User;

import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class ServerImpl implements IServer {
    static Registry reg;

    LobbyManager lobbyManager = new LobbyManager();

    public static void main(String[] args) {
        ServerImpl remoteObject = new ServerImpl();
        remoteObject.registerForRMI();

        remoteObject.test();//add lobbies for testing
    }


    //generate Lobbies for testing
    private void test(){
        Lobby lobby1=new Lobby();
        Lobby lobby2=new Lobby();
        Lobby lobby3=new Lobby();

        User user1=new User("test");

        User user2=new User("test User 2");

        User user3=new User("test User 3");

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
    public List<Lobby> availableLobbies() {
        return lobbyManager.getLobbies();
    }

    @Override
    public boolean joinLobby(User user, UUID lobbyId) throws RemoteException,AssertionError {
        try {
            if(lobbyManager.checkIfUsernameIsUsed(user.getUsername())){
                throw new AssertionError("Username already taken");
            }
            if(lobbyManager.getLobby(lobbyId).getUsers().size()>=4){
                throw new  RemoteException("Lobby is full");

            }else {
                lobbyManager.addUser(user, lobbyId);
                return true;
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Lobby createLobby(User user)  throws RemoteException,AssertionError{
        if(lobbyManager.checkIfUsernameIsUsed(user.getUsername())){
            throw new AssertionError("Username already taken");
        }
        return lobbyManager.genLobby(user);
    }

    @Override
    public boolean leaveLobby(User user, UUID lobbyId) {
        try {
            lobbyManager.removeUserFromLobby(user, lobbyId);
            return true;
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public Lobby startGame(UUID lobbyID) {
          return lobbyManager.changeLobbyStatus(lobbyID);

    }

}
