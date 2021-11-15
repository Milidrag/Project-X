package alcatraz.client;

import alcatraz.common.Lobby;
import alcatraz.common.Move;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClient extends Remote {
    public void presentPlayers(Lobby lobby) throws RemoteException;

    public void Move(String username, Move move) throws RemoteException;

    public void startGame();

    public void endGame();

    public GameStatus rejoinGame();
}
