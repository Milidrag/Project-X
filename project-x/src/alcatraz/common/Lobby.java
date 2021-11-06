package alcatraz.common;

import java.io.Serializable;
import java.util.UUID;

public class Lobby implements Serializable {
    private UUID lobbyId = UUID.randomUUID();

    public Lobby(){

    }

    @Override
    public String toString() {
        return "Lobby{" +
                "lobbyId=" + lobbyId +
                '}';
    }
}
