package alcatraz.client;

import alcatraz.common.Lobby;
import alcatraz.common.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
                    client.serverCreateLobby();
                    fillLobbiesScrollPane(false);

                    startGamebutton.setVisible(true);
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }

            } else {
                userNameTextField.setText("Please enter a valid username!");
            }
        });

        startGamebutton.addActionListener(e -> {
        });

    }


    private void fillLobbiesScrollPane(Boolean generateButtons) {
        try {
            List<Lobby> lobbies = client.serverGetLobbies();
            //lobbiesScrollPane.removeAll();

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
                        try {
                            client.serverJoinLobby(lobby.getLobbyId());
                            jButton.setVisible(false);
                            lobbiesScrollPane.setVisible(false);
                            startGamebutton.setVisible(true);
                        } catch (RemoteException ex) {
                            ex.printStackTrace();
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


    //draws the Window
    public void generateWindow() {
        JFrame frame = new JFrame("UserInterface");
        frame.setContentPane(this.root);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        init();

    }

    public UserInterfaceLobbies() {
        //generateWindow();
        // init();
    }


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
