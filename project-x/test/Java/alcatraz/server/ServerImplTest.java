package alcatraz.server;

import alcatraz.client.ClientImpl;
import alcatraz.common.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServerImplTest {



    @Test
    public void testRMIServerConnection(){
        try {
            ServerImpl server=new ServerImpl();
            ClientImpl client= new ClientImpl();
            User user=new User("test");

            server.getLobbyManager().genLobby(user);
            server.registerForRMI();

            client.connectToServer();
            client.serverConTest();

        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }




    @Test
    public void testRMIServerCreateLobby(){
        try {
            int expectedResult=1;
            int actualResult;

            ServerImpl server=new ServerImpl();
            ClientImpl client= new ClientImpl();
            User user=new User("test");


            server.registerForRMI();

            client.connectToServer();
            client.serverCreateLobby();

            actualResult=server.lobbyManager.getLobbies().size();


            assertEquals(expectedResult,actualResult);

        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }
}