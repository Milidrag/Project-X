package alcatraz.client;

import alcatraz.common.Move;
import alcatraz.common.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IClient extends Remote {
    public void presentPlayers(List<User> users) throws RemoteException;
    public void Move(String username, Move move) throws RemoteException;
    //TODO Methoden aus der Präsentation einfügen
}
