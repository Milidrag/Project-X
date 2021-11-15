package alcatraz.client;

import alcatraz.common.Lobby;
import alcatraz.common.Move;
import alcatraz.common.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IClient extends Remote {
    public void presentPlayers(Lobby lobby) throws RemoteException;

    public void Move(String username, Move move) throws RemoteException;

    public void startGame();

    public void endGame();

    public GameStaus rejoinGame();
}
