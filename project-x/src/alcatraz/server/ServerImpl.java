package alcatraz.server;

import alcatraz.common.Lobby;
import alcatraz.common.User;

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

    public List<Lobby> availableLobbies() {
        return lobbyManager.getLobbies();
    }

    public boolean joinLobby(User user, UUID lobbyId) {
        try {
            lobbyManager.addUser(user, lobbyId);
            return true;
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Lobby createLobby(User user) {
        return lobbyManager.genLobby(user);
    }

    public boolean leaveLobby(User user, UUID lobbyId) {
        try {
            lobbyManager.removeUserFromLobby(user, lobbyId);
            return true;
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return false;
        }

    }

    public Lobby startGame(UUID lobbyID) {
          return lobbyManager.changeLobbyStatus(lobbyID);

    }

}
