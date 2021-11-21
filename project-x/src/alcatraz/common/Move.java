package alcatraz.common;

import at.falb.games.alcatraz.api.Player;

import java.io.Serializable;
import java.util.UUID;

public class Move implements Serializable {
//    private UUID moveId = UUID.randomUUID();
//
//    public UUID getMoveId() {
//        return moveId;
//    }

    private User user;
    private Player player;
    private  int rowOrCol;
    private int row;
    private int col;

    public Move() {
    }

    public Move(User user, Player player, int rowOrCol, int row, int col) {
        this.user = user;
        this.player = player;
        this.rowOrCol = rowOrCol;
        this.row = row;
        this.col = col;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getRowOrCol() {
        return rowOrCol;
    }

    public void setRowOrCol(int rowOrCol) {
        this.rowOrCol = rowOrCol;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}
