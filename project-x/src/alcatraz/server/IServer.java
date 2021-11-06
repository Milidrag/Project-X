package alcatraz.server;

import alcatraz.common.Lobby;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

public interface IServer extends Remote {
    public List<Lobby> availableLobbies() throws RemoteException;
    public boolean joinLobby(String username, UUID lobbyId) throws RemoteException;
    public UUID createLobby(String username) throws RemoteException;
    public boolean leaveLobby(String username, UUID lobbyId) throws RemoteException;
    public boolean startGame(UUID lobbyID) throws RemoteException;
    //TODO: Methoden aus der Präsentation einfügen
}
