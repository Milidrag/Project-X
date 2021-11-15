package alcatraz.common;

import java.util.UUID;

public class Move {
    private UUID moveId = UUID.randomUUID();

    public UUID getMoveId() {
        return moveId;
    }
}
