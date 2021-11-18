package alcatraz.client;

import alcatraz.common.Lobby;
import alcatraz.common.User;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.List;

public class UserInterfaceLobbies {

    private ClientImpl client;

    private JPanel root;
    private JPanel usernamePanel;
    private JTextField userNameTextField;
    private JLabel userNameInfoLabel;
    private JScrollPane lobbiesScrollPane;
    private JButton createLobbyButton;
    private JButton startGamebutton;
    private JPanel lobbyPanel;


    private JFrame frame;

    private GameWindow gameWindow;

    public void setGameWindow(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
    }

    public ClientImpl getClient() {
        return client;
    }

    public void setClient(ClientImpl client) {
        this.client = client;
    }

    public void init() {
        startGamebutton.setVisible(false);
        createLobbyButton.addActionListener(e -> {
            String username = userNameTextField.getText();
            if (!(username.equals("") || username == null)) {
                userNameTextField.setEnabled(false);
                client.getThisUser().setUsername(username);
                try {
                    Lobby lobby = client.serverCreateLobby();
                    fillLobbiesScrollPane(false);
                    createLobbyButton.setVisible(false);
                    addLeaveLobbieButton(lobby);

                    startGamebutton.setVisible(true);
                } catch (RemoteException ex) {
                    ex.printStackTrace();

                }

            } else {
                userNameTextField.setText("Please enter a valid username!");
            }
        });

        lobbyPanel.setLayout(new BoxLayout(lobbyPanel, BoxLayout.Y_AXIS));

        startGamebutton.addActionListener(e -> {
            try {
                Lobby lobby = client.serverStartGame();
                client.setLobby(lobby);
               closeWindow();
                //TODO:


                client.sendStartToOutherClients();
                gameWindow.start();

            } catch (RemoteException remoteException) {
                remoteException.printStackTrace();

            }
        });

        fillLobbiesScrollPane(true);
    }

    public void closeWindow(){
        this.frame.dispose();
    }


    private void fillLobbiesScrollPane(Boolean generateButtons) {
        try {
            List<Lobby> lobbies = client.serverGetLobbies();
            //lobbiesScrollPane.removeAll();

            lobbyPanel.removeAll();

            for (Lobby lobby : lobbies) {
                JPanel jPanel = new JPanel();

                JLabel jLabel = new JLabel();

                String labelText = "Lobby Nr" + lobby.getLobbyId();

                for (User user : lobby.getUsers()) {
                    labelText += " User:" + user.getUsername() + " ";
                }
                jLabel.setText(labelText);
                jPanel.add(jLabel);
                if (generateButtons) {

                    JButton jButton = new JButton();
                    jButton.setText("Join Lobby");

                    jButton.addActionListener(e -> {
                        String username = userNameTextField.getText();
                        if (!(username.equals("") || username == null)) {
                            try {
                                client.serverJoinLobby(lobby.getLobbyId());
                                jButton.setVisible(false);
                                client.setLobby(lobby);

                                addLeaveLobbieButton(lobby);

                                startGamebutton.setVisible(true);
                                createLobbyButton.setVisible(false);

                            } catch (RemoteException ex) {
                                ex.printStackTrace();

                            }
                        } else {
                            userNameTextField.setText("Please enter a valid username!");
                        }
                    });
                    jPanel.add(jButton);
                }
                lobbyPanel.add(jPanel);
                lobbyPanel.revalidate();
                lobbyPanel.repaint();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void addLeaveLobbieButton(Lobby lobby) {
        lobbyPanel.removeAll();
        JButton jButton = new JButton();
        jButton.setText("Leave lobby Nr " + lobby.getLobbyId() + "Users: " + lobby.getUsers());

        jButton.addActionListener(e -> {
            try {

                client.serverLeaveLobby(lobby.getLobbyId());
                fillLobbiesScrollPane(true);
                createLobbyButton.setVisible(true);
                startGamebutton.setVisible(false);
            } catch (Exception exception) {
                exception.printStackTrace();

            }
        });
        lobbyPanel.add(jButton);
        lobbyPanel.revalidate();
        lobbyPanel.repaint();
    }


    //draws the Window
    public void generateWindow() {
        frame = new JFrame("UserInterface");
        frame.setContentPane(this.root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        init();

    }
//
//    public UserInterfaceLobbies() {
//        //generateWindow();
//        // init();
//    }


    public UserInterfaceLobbies(ClientImpl client) {
        this.client = client;
        //generateWindow();
        //init();

    }

//    public static void main(String[] args) {
//        ClientImpl client=new ClientImpl();
//        UserInterfaceLobbies userInterfaceLobbies = new UserInterfaceLobbies( client);
//
//    }


}
