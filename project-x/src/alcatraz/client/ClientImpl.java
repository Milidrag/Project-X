package alcatraz.client;

import alcatraz.common.Lobby;
import alcatraz.common.Move;
import alcatraz.common.User;
import alcatraz.server.IServer;

import java.net.UnknownHostException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientImpl implements IClient {

    static Registry reg;

    static IServer stub;

    static int numberOfClients = 0;

    private ArrayList<IClient> clientStubs = new ArrayList<>();

    //User of this client
    private User thisUser = new User();

    //Lobby of this client
    private Lobby lobby;

    //Game
    private AlcatrazImpl alcatrazImpl = new AlcatrazImpl(this);

    private UserInterfaceLobbies userInterfaceLobbies;

    private boolean rmiStarted = false;

    private Logger logger =Logger.getLogger(ClientImpl.class.getName());



    public boolean isRmiStarted() {
        return rmiStarted;
    }

    public UserInterfaceLobbies getUserInterfaceLobbies() {
        return userInterfaceLobbies;
    }

    public void setUserInterfaceLobbies(UserInterfaceLobbies userInterfaceLobbies) {
        this.userInterfaceLobbies = userInterfaceLobbies;

    }

    public User getThisUser() {
        return thisUser;
    }

    public void setThisUser(User thisUser) {
        this.thisUser = thisUser;
    }

    public void init(String username) {
        thisUser.setUserId(UUID.randomUUID());
        thisUser.setUsername(username);
    }


    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public ClientImpl() {
        this.thisUser.setRmiPort(1100 + numberOfClients);

        numberOfClients++;

        try {
            thisUser.setIpAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
      //  System.setProperty ("sun.rmi.transport.tcp.responseTimeout", "10000");
    }


    public void connectToServer() {
        try {
            Registry reg = LocateRegistry.getRegistry();
            stub = (IServer) reg.lookup("Server");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //!!! Client to server RMI function begin
    public void serverJoinLobby(UUID lobbyID) throws RemoteException {
        try {
            stub.joinLobby(thisUser, lobbyID);
        } catch (ConnectException ex) {
            connectToServer();
            stub.joinLobby(thisUser, lobbyID);
        }
    }

    public Lobby serverCreateLobby() throws RemoteException {
        try {
            Lobby lob = stub.createLobby(thisUser);
            this.lobby = lob;
            return lob;
        } catch (ConnectException ex) {
            connectToServer();
            Lobby lob = stub.createLobby(thisUser);
            this.lobby = lob;
            return lob;
        }
    }

    public void serverLeaveLobby(UUID lobbyID) throws RemoteException {
        try {
            stub.leaveLobby(thisUser, lobbyID);
        } catch (ConnectException ex) {
            connectToServer();
            stub.leaveLobby(thisUser, lobbyID);
        }
    }

    public List<Lobby> serverGetLobbies() throws RemoteException {
        try {
            List<Lobby> result = stub.availableLobbies();
            System.out.println(result);
            return result;

        } catch (ConnectException ex) {
            connectToServer();
            List<Lobby> result = stub.availableLobbies();
            System.out.println(result);
            return result;

        }

    }

    public Lobby serverStartGame() throws RemoteException {
        try {
            return stub.startGame(lobby.getLobbyId());
        } catch (ConnectException ex) {
            connectToServer();
            return stub.startGame(lobby.getLobbyId());
        }
    }


    //!!! Client to server RMI function end

    public void sendUsersToOtherClients() throws RemoteException {
        for (IClient stub : this.clientStubs) {
            try {
                stub.presentPlayers(this.lobby);
            } catch (Exception e) {
                e.printStackTrace();

            }

        }
    }

    public void sendStartToOtherClients() throws RemoteException {
        for (IClient stub : this.clientStubs) {
            try {
                stub.startGame();
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    public void sendMoveToOtherClients(Move move) throws RemoteException {
        System.out.println();
        System.out.println("send moveRMU" + move.toString());
        System.out.println("anz Clints" + clientStubs.size());
        for (IClient stub : this.clientStubs) {
            try {
                System.out.println("send to:");
                System.out.println(stub.toString());
                stub.Move(thisUser, move);
            } catch (Exception e) {
                e.printStackTrace();

              //  handelTimeOut(stub);
            }

        }
    }

    private void sendEndGameToOtherClients(){
        for (IClient stub:clientStubs){
            try {
                stub.endGame();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void startClientRMI() throws RemoteException {
        IClient clientStub = (IClient) UnicastRemoteObject.exportObject(this, 0);
        reg = LocateRegistry.createRegistry(thisUser.getRmiPort());
        reg = LocateRegistry.getRegistry(thisUser.getRmiPort());
       // System.out.println("user/+ " + this.thisUser.getUsername() + "con= " + "client/" + this.thisUser.getUsername());
        String connect = "client/" + this.thisUser.getUsername();
        reg.rebind(connect, clientStub);
        System.out.println();

        genInfLog(" client:" +thisUser.getUsername()+ "has started RMI with name= "+connect);
        rmiStarted = true;
    }

    public void connectToTheClients() throws RemoteException, NotBoundException {
        for (User user : this.lobby.getUsers()) {
            if (!user.getUsername().equals(thisUser.getUsername())) {
                Registry reg;
                String ipAddress = user.getIpAddress().toString();
                if (ipAddress != null) {
//                    System.out.println();
//                    System.out.println();
//                    System.out.println("connect to ip: " + ipAddress);
                    reg = LocateRegistry.getRegistry(ipAddress, user.getRmiPort());
                } else {
                    System.out.println();
                    genInfLog("No Ip Address from Target");
                    reg = LocateRegistry.getRegistry(user.getRmiPort());
                }

              //  System.out.println("verbinde zu " + "client/" + user.getUsername() + " von " + thisUser.getUsername());

                IClient clientStub = (IClient) reg.lookup("client/" + user.getUsername());
                clientStubs.add(clientStub);

                genInfLog(" Verbinde:" +thisUser.getUsername() +" zu:"+user.getUsername()+"\n"+" Ip address: "+ipAddress+" Port:"+user.getRmiPort());
            }
        }
    }

    private void genInfLog(String message){
        logger.log(Level.OFF,"\n");
        logger.log(Level.INFO, message );

    }

    public void startAlcatrazGame() {
        alcatrazImpl.init();
    }


    //??? Client to Client RMI function begin ???
    @Override
    public void presentPlayers(Lobby lobby) throws RemoteException {
        System.out.println("reciver =" + thisUser.getUsername() + " " + lobby);
        if (!this.lobby.equals(lobby)) {
            System.out.println("reciver =" + thisUser.getUsername() + " changed lobby");
            this.lobby = lobby;
        }
        try {
            if (!rmiStarted) {
                startClientRMI();
            }
            connectToTheClients();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void Move(User user, Move move) throws RemoteException {
        genInfLog("recived Move =\" + move.toString() ");
        this.alcatrazImpl.makeRMIMove(move);
    }

    @Override
    public void startGame() throws RemoteException {
        genInfLog("receiver =" + thisUser.getUsername() + "Start game!!! ");
        userInterfaceLobbies.closeWindow();
        startAlcatrazGame();
    }

    @Override
    public void endGame() throws RemoteException {
        alcatrazImpl.end();
        userInterfaceLobbies.showWindow();

    }


    //??? Client to Client RMI function end ???

}
