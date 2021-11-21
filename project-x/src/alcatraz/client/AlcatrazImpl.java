package alcatraz.client;

import alcatraz.common.Move;
import at.falb.games.alcatraz.api.*;

public class AlcatrazImpl implements MoveListener {
    private ClientImpl client;
    private int numberOfPlayers;
    private Alcatraz alcatraz;

    public AlcatrazImpl(ClientImpl client) {
        this.client = client;
    }

    public void setClient(ClientImpl client) {
        this.client = client;
    }

    public void init(){
        numberOfPlayers=client.getLobby().getUsers().size();
        alcatraz=new Alcatraz();

        int positionOfThisPlayer= 0;
        try {
            positionOfThisPlayer=client.getLobby().getUserPosition(client.getThisUser());
        }catch (Exception e){
            System.out.println("Error in Alc int position ca not be foun");
            e.printStackTrace();

        }
        alcatraz.init(numberOfPlayers,positionOfThisPlayer);
        for (int i=0;i<numberOfPlayers;i++){
            if(i!=positionOfThisPlayer){
                alcatraz.getPlayer(i).setName(client.getLobby().getUsers().get(i).getUsername());
            }
        }
        alcatraz.showWindow();
        alcatraz.start();
    }

    public void makeRMIMove(Move move){
        try {
            alcatraz.doMove(move.getPlayer(),move.getPrisoner(),move.getRowOrCol(),move.getRow(),move.getCol());
        } catch (IllegalMoveException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void moveDone(Player player, Prisoner prisoner, int rowOrCol, int row, int col) {
        try {
            alcatraz.doMove(player,prisoner,rowOrCol,row,col);

            Move move=new Move(client.getThisUser(), player,prisoner,rowOrCol,row,col);


            //TODO send RMI



        } catch (IllegalMoveException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gameWon(Player player) {
        System.out.println(player.getName() +" has won the game");

        //TODO : send end game RMI
    }



}
