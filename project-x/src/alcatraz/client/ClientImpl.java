package alcatraz.client;

import alcatraz.common.Move;
import alcatraz.server.IServer;
import alcatraz.common.User;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class ClientImpl implements IClient{

    static IServer stub;

    public static void main(String[] args){
        try {
            Registry reg = LocateRegistry.getRegistry();
            stub = (IServer) reg.lookup("Server");
            System.out.println(stub.availableLobbies().get(0).toString());
        }catch (Exception e){
            System.out.println(e);
        }
    }

    //TODO: Methoden aus der Präsentation implmentieren
    public void presentPlayers(List<User> users){

    }

    public void Move(String username, Move move){

    }

}
