package alcatraz.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Lobby implements Serializable {
    private UUID lobbyId = UUID.randomUUID();

    private List<User> users =new ArrayList<User>();



    public Lobby(){

    }

    public void addPlayer(User user){
        users.add(user);
    }

    public void removeuser(User user){
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
