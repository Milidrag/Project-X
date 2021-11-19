package alcatraz.server;

import alcatraz.common.Lobby;
import alcatraz.common.User;
import spread.SpreadConnection;
import spread.SpreadException;
import spread.SpreadGroup;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import spread.*;

public class ServerImpl implements IServer, AdvancedMessageListener {
    static Registry reg;
    private boolean isRunning = true;
    private String serverId;
    private SpreadGroup myGroup;
    private SpreadGroup serverGroup;
    private SpreadGroup currentPrimaryGroup;
    private boolean isPrimary = true;
    SpreadConnection newConnection;
    private final short lobbyMessage = 2;
    private final short primaryMessage = 1;
    private final short playerMessage = 3;




    LobbyManager lobbyManager = new LobbyManager();


    public static void main(String[] args) {
        ServerImpl remoteObject = new ServerImpl();
       // remoteObject.registerForRMI();

      //  remoteObject.test();//add lobbies for testing

        while(remoteObject.GetIsRunning()) {
            try {
                Thread.sleep(17000);
            } catch (InterruptedException ex) {
                //TODO or not, whatever
            }
        }
        System.out.println("Programm ended");


    }

    private boolean GetIsRunning() {
        return isRunning;
    }

    public ServerImpl(){
        this.serverId = UUID.randomUUID().toString();
        newConnection = new SpreadConnection();
        try {
            newConnection.connect(InetAddress.getByName("127.0.0.1"), 4803, this.serverId, false, true);
            //add advanced Message listener
            newConnection.add(this);
            this.serverGroup = initSpreadGroup(newConnection, "spreadGroupName");
            //TODO verstehe nicht was hier genau passiert. Wozu braucht man eine private Group?
            this.myGroup = newConnection.getPrivateGroup();
           // sendSpreadMessage(newConnection,"spreadGroupName","test", (short) 2);
        } catch (SpreadException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }


    }


    private static void sendSpreadMessage(SpreadConnection connection, String groupname, Object data, short messagetype) {
        try {
            SpreadMessage message = new SpreadMessage();
            message.setObject((Serializable)data);
            message.addGroup(groupname);
            //TODO: laut Doku wird setReliable() per default aufgerufen. Es sollte also hier nicht notwendig sein es wieder zu setzen
            message.setReliable();
            message.setType(messagetype);
            connection.multicast(message);
        } catch (SpreadException ex) {
            //TODO
        }
    }


    private SpreadGroup initSpreadGroup(SpreadConnection newConnection, String spreadGroupName) {
            SpreadGroup group = new SpreadGroup();
            try {
                group.join(newConnection, spreadGroupName);
            }
            catch (SpreadException ex) {
                System.err.println("Spread Exception: " +ex.getMessage() + Arrays.toString(ex.getStackTrace()));
            }
            return group;
    }


    //generate Lobbies for testing
    private void test() {
        Lobby lobby1 = new Lobby();
        Lobby lobby2 = new Lobby();
        Lobby lobby3 = new Lobby();

        User user1 = new User("test");

        User user2 = new User("test User 2");

        User user3 = new User("test User 3");

        lobby1.addPlayer(user1);
        lobby2.addPlayer(user2);
        lobby3.addPlayer(user3);

        lobbyManager.getLobbies().add(lobby1);
        lobbyManager.getLobbies().add(lobby2);
        lobbyManager.getLobbies().add(lobby3);
    }

    public void registerForRMI() {
        try {
            IServer stub = (IServer) UnicastRemoteObject.exportObject(this, 0);
            reg = LocateRegistry.createRegistry(1099);
            reg = LocateRegistry.getRegistry(1099);
            reg.rebind("Server", stub);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public LobbyManager getLobbyManager() {
        return lobbyManager;
    }

    //TODO: Methoden aus der Präsentation implmentieren

    @Override
    public List<Lobby> availableLobbies()throws RemoteException {
        return lobbyManager.getLobbies();
    }

    @Override
    public boolean joinLobby(User user, UUID lobbyId) throws RemoteException, AssertionError {
        try {
            if (lobbyManager.checkIfUsernameIsUsed(user.getUsername())||user.getUsername()==null) {

                throw new AssertionError("Username already taken");
            } else {
                if (lobbyManager.getLobby(lobbyId).getUsers().size() >= 4) {
                    throw new RemoteException("Lobby is full");

                } else {
                    lobbyManager.addUser(user, lobbyId);
                    return true;
                }
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Lobby createLobby(User user) throws RemoteException, AssertionError {
        if (lobbyManager.checkIfUsernameIsUsed(user.getUsername())||user.getUsername()==null) {
            throw new AssertionError("Username already taken");
        } else {
            if(isPrimary == true) {
                //TODO Dynamischer machen wegen spreadGroupName. es sollte möglich sein mehrere Gruppen zu verwalten
                sendSpreadMessage(newConnection, "spreadGroupName", lobbyManager.getLobbies(), lobbyMessage);
            }


            return lobbyManager.genLobby(user);
        }
    }

    @Override
    public boolean leaveLobby(User user, UUID lobbyId) throws RemoteException{
        try {
            lobbyManager.removeUserFromLobby(user, lobbyId);
            return true;
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public Lobby startGame(UUID lobbyID) throws RemoteException , NoSuchElementException {
        try {
            System.out.println(lobbyID);
            int userCountInLobby  = lobbyManager.getLobby(lobbyID).getUsers().size();
            if(userCountInLobby<2||userCountInLobby>4){
                System.out.println("lobby s="+userCountInLobby);
                throw new RemoteException("wrong Lobby size ="+userCountInLobby);
            }else {
                return lobbyManager.changeLobbyStatus(lobbyID);
            }
        }catch (Exception exception){
            exception.printStackTrace();
            throw new RemoteException("error");
        }

    }

    @Override
    public void regularMessageReceived(SpreadMessage spreadMessage) {

        DisplayMessage(spreadMessage);
    }

    /**
     * hier wird festgestellt ob ein neues Gruppenmitglied dazugekommen ist
     * außerdem wird hier festgestellt ob der derzeitige Server der Primary ist
     * Primary falls er der erste ist welche in der Gruppe ist
     */
    @Override
    public void membershipMessageReceived(SpreadMessage spreadMessage) {
        DisplayMessage(spreadMessage);

        //TODO: feststellen ob der derzeitige Server der primary ist
    }



    private void DisplayMessage(SpreadMessage msg)
    {
        try
        {
            if(msg.isRegular())
            {
                //TODO nur für Testzwecke derzeit drinnen gelassen
                System.out.print("Received a ");
                if(msg.isUnreliable())
                    System.out.print("UNRELIABLE");
                else if(msg.isReliable())
                    System.out.print("RELIABLE");
                else if(msg.isFifo())
                    System.out.print("FIFO");
                else if(msg.isCausal())
                    System.out.print("CAUSAL");
                else if(msg.isAgreed())
                    System.out.print("AGREED");
                else if(msg.isSafe())
                    System.out.print("SAFE");
                System.out.println(" message.");

                System.out.println("Sent by  " + msg.getSender() + ".");

                System.out.println("Type is " + msg.getType() + ".");

                if(msg.getEndianMismatch() == true)
                    System.out.println("There is an endian mismatch.");
                else
                    System.out.println("There is no endian mismatch.");

                SpreadGroup groups[] = msg.getGroups();
                System.out.println("To " + groups.length + " groups.");

                byte data[] = msg.getData();
                System.out.println("The data is " + data.length + " bytes.");

                System.out.println("The message is: " + new String(data));

                if(msg.getType() == primaryMessage) {
                    this.currentPrimaryGroup = msg.getSender();
                    System.out.println("primary set: "+ this.currentPrimaryGroup.toString());
                }

                if(msg.getType() == lobbyMessage)
                {
                    try {
                        lobbyManager.setLobbies((ArrayList<Lobby>) msg.getObject());
                        System.out.println("Lobbies updated");
                    } catch (SpreadException ex) {
                        //TODO catch me if you can
                    }
                }
                //TODO Player fehlen noch bei uns
            /*
                if(msg.getType() == playerMessage)
                {
                    try {
                        this.AllPlayers = (ArrayList<User>) message.getObject();
                        System.out.println("User list updated");

                    } catch (SpreadException ex) {
                        //TODO something useful
                    }
                }

             */


            }
            else if (msg.isMembership())
            {
                MembershipInfo info = msg.getMembershipInfo();
                printMembershipInfo(info);
                definePrimary(info);
            } else if ( msg.isReject() )
            {
                // Received a Reject message
                System.out.print("Received a ");
                if(msg.isUnreliable())
                    System.out.print("UNRELIABLE");
                else if(msg.isReliable())
                    System.out.print("RELIABLE");
                else if(msg.isFifo())
                    System.out.print("FIFO");
                else if(msg.isCausal())
                    System.out.print("CAUSAL");
                else if(msg.isAgreed())
                    System.out.print("AGREED");
                else if(msg.isSafe())
                    System.out.print("SAFE");
                System.out.println(" REJECTED message.");

                System.out.println("Sent by  " + msg.getSender() + ".");

                System.out.println("Type is " + msg.getType() + ".");

                if(msg.getEndianMismatch() == true)
                    System.out.println("There is an endian mismatch.");
                else
                    System.out.println("There is no endian mismatch.");

                SpreadGroup groups[] = msg.getGroups();
                System.out.println("To " + groups.length + " groups.");

                byte data[] = msg.getData();
                System.out.println("The data is " + data.length + " bytes.");

                System.out.println("The message is: " + new String(data));
            } else {
                System.out.println("Message is of unknown type: " + msg.getServiceType() );
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void definePrimary(MembershipInfo info) {

        if(info.isCausedByJoin())
        {
            if (info.getMembers().length == 1) {
                this.currentPrimaryGroup = this.myGroup;
                this.isPrimary = true;
                System.out.println("New primary: "+myGroup.toString());
              //TODO auskommentiert damit Lukas am Frontend weiterarbeiten kann
                //setRMIforPrimary();
            }

            if(this.isPrimary == true) {
                sendSpreadMessage(newConnection, "spreadGroupName", "" , primaryMessage);
                System.out.println("primary message sent");
                sendSpreadMessage(newConnection, "spreadGroupName", getLobbyManager().getLobbies(), lobbyMessage);
                System.out.println("Lobby message sent");
                //TODO Player fehlen bei uns noch
                /*
                sendSpreadMessage(newConnection, "spreadGroupName", AllPlayers, playerMessage );
                System.out.println("Player message sent");

                 */
            }
        }

    }

    private void setRMIforPrimary() {
            try {
                IServer stub = (IServer) UnicastRemoteObject.exportObject(this, 0);

                reg = LocateRegistry.createRegistry(1099);
                reg = LocateRegistry.getRegistry(1099);

                reg.rebind("Server", stub);
            } catch (RemoteException ex) {
                Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);

                //TODO ich habe es jetzt mal so direkt reingeschrieben. Damit wir hier nicht gleich zuviele Methoden auf einmal haben
                //refactoren auf 2 eine Methode
                try {
                    this.serverGroup.leave();
                    this.newConnection.disconnect();
                } catch (SpreadException e) {
                    e.printStackTrace();
                }

                this.isRunning = false;
            }

    }



    // Print this membership data.  Does so in a generic way so identical
    // function is used in recThread and User.
    private void printMembershipInfo(MembershipInfo info)
    {
        SpreadGroup group = info.getGroup();
        if(info.isRegularMembership()) {
            SpreadGroup members[] = info.getMembers();
            MembershipInfo.VirtualSynchronySet virtual_synchrony_sets[] = info.getVirtualSynchronySets();
            MembershipInfo.VirtualSynchronySet my_virtual_synchrony_set = info.getMyVirtualSynchronySet();

            System.out.println("REGULAR membership for group " + group +
                    " with " + members.length + " members:");
            for( int i = 0; i < members.length; ++i ) {
                System.out.println("\t\t" + members[i]);
            }
            System.out.println("Group ID is " + info.getGroupID());

            System.out.print("\tDue to ");
            if(info.isCausedByJoin()) {
                System.out.println("the JOIN of " + info.getJoined());
            }	else if(info.isCausedByLeave()) {
                System.out.println("the LEAVE of " + info.getLeft());
            }	else if(info.isCausedByDisconnect()) {
                System.out.println("the DISCONNECT of " + info.getDisconnected());
            } else if(info.isCausedByNetwork()) {
                System.out.println("NETWORK change");
                for( int i = 0 ; i < virtual_synchrony_sets.length ; ++i ) {
                    MembershipInfo.VirtualSynchronySet set = virtual_synchrony_sets[i];
                    SpreadGroup         setMembers[] = set.getMembers();
                    System.out.print("\t\t");
                    if( set == my_virtual_synchrony_set ) {
                        System.out.print( "(LOCAL) " );
                    } else {
                        System.out.print( "(OTHER) " );
                    }
                    System.out.println( "Virtual Synchrony Set " + i + " has " +
                            set.getSize() + " members:");
                    for( int j = 0; j < set.getSize(); ++j ) {
                        System.out.println("\t\t\t" + setMembers[j]);
                    }
                }
            }
        } else if(info.isTransition()) {
            System.out.println("TRANSITIONAL membership for group " + group);
        } else if(info.isSelfLeave()) {
            System.out.println("SELF-LEAVE message for group " + group);
        }
    }
}
