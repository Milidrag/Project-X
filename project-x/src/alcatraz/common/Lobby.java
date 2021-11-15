package alcatraz.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Lobby implements Serializable {
    private UUID lobbyId = UUID.randomUUID();

    private List<User> users = new ArrayList<User>();

    private boolean isGameRunning=false;


    public Lobby() {

    }

    public boolean isGameRunning() {
        return isGameRunning;
    }

    public void setGameRunning(boolean gameRunning) {
        isGameRunning = gameRunning;
    }

    public void addPlayer(User user) {
        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public UUID getLobbyId() {
        return lobbyId;
    }

    public List<User> getUsers() {
        return users;
    }

    @Override
    public String toString() {
        return "Lobby{" +
                "lobbyId=" + lobbyId +
                ", users=" + users +
                '}';
    }
}
