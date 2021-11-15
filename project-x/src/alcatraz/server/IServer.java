package alcatraz.server;

import alcatraz.common.Lobby;
import alcatraz.common.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

public interface IServer extends Remote {
    public List<Lobby> availableLobbies() throws RemoteException;
    public boolean joinLobby(User user, UUID lobbyId) throws RemoteException;
    public Lobby createLobby(User user) throws RemoteException;
    public boolean leaveLobby(User user, UUID lobbyId) throws RemoteException;
    public Lobby startGame(UUID lobbyID) throws RemoteException;
    //TODO: Methoden aus der Präsentation einfügen
}
