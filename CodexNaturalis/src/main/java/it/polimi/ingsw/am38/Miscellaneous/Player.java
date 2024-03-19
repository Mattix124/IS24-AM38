package it.polimi.ingsw.am38.Miscellaneous;

public class Player {
    private String name;
    private PlayableCards[] hand;
    private Board field;
    private Color color;
    private int score;

    //*items and resources on your board
    private int n_fungi;
    private int n_animals;
    private int n_insects;
    private int n_plants;
    private int n_inkwells;
    private int n_quills;
    private int n_manuscripts;

    public int getN_fungi(){

        return n_fungi;
    }
    public int getN_animals(){
        return n_animals;
    }
    public int getN_insects(){

        return n_insects;
    }
    public int getN_plants(){

        return n_plants;
    }
    public int getN_quills(){

        return n_quills;
    }
    public int getN_inkwells(){
        return n_inkwells;}
    public int getN_manuscripts(){
        return n_manuscripts

    }
    public void endgame(){
    }
}
