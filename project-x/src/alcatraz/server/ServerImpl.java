package alcatraz.server;

import alcatraz.common.Lobby;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServerImpl implements IServer{
    static Registry reg;

    public static void main(String[] args) {
        ServerImpl remoteObject = new ServerImpl();
        remoteObject.registerForRMI();
    }

    public void registerForRMI(){
        try {
            IServer stub = (IServer) UnicastRemoteObject.exportObject(this, 0);
            reg = LocateRegistry.createRegistry(1099);
            reg = LocateRegistry.getRegistry(1099);
            reg.rebind("Server", stub);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    //TODO: Methoden aus der Präsentation implmentieren

    public List<Lobby> availableLobbies(){
        ArrayList<Lobby> lobbies = new ArrayList<Lobby>();
        lobbies.add(new Lobby());
        return lobbies;
    }
    public boolean joinLobby(String username, UUID lobbyId){
        return false;
    }

    public UUID createLobby(String username){
        return UUID.randomUUID();
    }
    public boolean leaveLobby(String username, UUID lobbyId){
        return false;
    }
    public boolean startGame(UUID lobbyID){
        return false;
    }
}
