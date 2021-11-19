package alcatraz.common;

import java.io.Serializable;
import java.util.UUID;

public class Move implements Serializable {
    private UUID moveId = UUID.randomUUID();

    public UUID getMoveId() {
        return moveId;
    }
}
