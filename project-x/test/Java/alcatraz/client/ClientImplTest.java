package alcatraz.client;

import alcatraz.server.ServerImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientImplTest {


         @Test
         public void testRMIServerConnection(){
          try {
              ServerImpl server=new ServerImpl();
              ClientImpl client= new ClientImpl();

              server.registerForRMI();

              client.connectToServer();
              client.serverConTest();

          }catch (Exception e){
              e.printStackTrace();
              fail();
          }
         }
}