package alcatraz.client;

import alcatraz.common.Lobby;
import alcatraz.common.Move;
import alcatraz.server.IServer;
import alcatraz.common.User;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.UUID;

public class ClientImpl implements IClient{

    static Registry reg;

    static IServer stub;

    static int numberOfClients=0;

    //User of this client
    private User thisUser;

    private Lobby lobby;

    public User getThisUser() {
        return thisUser;
    }

    public void setThisUser(User thisUser) {
        this.thisUser = thisUser;
    }

    public void init(String username){
        thisUser.setUserId(UUID.randomUUID());
        thisUser.setUsername(username);
    }

    public ClientImpl() {
       numberOfClients++;
    }

    public static void main(String[] args){
        ClientImpl client=new ClientImpl();
        client.connectToServer();
        try {
            client.serverConTest();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public  void connectToServer() {
        try {
            Registry reg = LocateRegistry.getRegistry();
            stub = (IServer) reg.lookup("Server");
           // System.out.println(stub.availableLobbies().get(0).toString());

        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void serverConTest() throws RemoteException {
        System.out.println(stub.availableLobbies().get(0).toString());
    }

    public void startClientRMI() throws RemoteException {
        IClient clientStub = (IClient) UnicastRemoteObject.exportObject(this, 0);
        reg=LocateRegistry.createRegistry(1100+numberOfClients);
        reg = LocateRegistry.getRegistry(1100+numberOfClients);
        reg.rebind("client/"+this.thisUser.getUsername(),clientStub);
    }






    //TODO: Methoden aus der Präsentation implmentieren
    public void presentPlayers(List<User> users){




    }

    public void Move(String username, Move move){

    }

}
