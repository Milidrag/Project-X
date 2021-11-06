package alcatraz.client;

import alcatraz.common.Move;
import alcatraz.common.User;

import java.rmi.RemoteException;
import java.util.List;

public interface IClient {
    public void presentPlayers(List<User> users) throws RemoteException;
    public void Move(String username, Move move) throws RemoteException;
    //TODO Methoden aus der Präsentation einfügen
}
