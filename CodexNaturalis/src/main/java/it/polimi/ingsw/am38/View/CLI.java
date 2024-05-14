package it.polimi.ingsw.am38.View;

import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Enum.Orientation;
import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Board.Field;
import it.polimi.ingsw.am38.Model.Cards.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.ingsw.am38.Enum.Orientation.*;

public class CLI implements Viewable, Serializable {
    @Serial
    private static final long serialVersionUID = -2027642178634818222L;
    //shown to the screen
    private String goldDeckTop;
    private String goldFaceUp1, goldFaceUp2;
    private String resourceDeckTop;
    private String resourceFaceUp1, resourceFaceUp2;
    private Field shownGameField;
    private String sharedObjective1, SharedObjective2;
    private String PersonalObjective;
    private final ArrayList<String> chat = new ArrayList<>(6);
    private final ArrayList<String> gameScreen = new ArrayList<>(32);
    private final ArrayList<String> topOfGDeck = new ArrayList<>(6);
    private final ArrayList<String> topOfRDeck = new ArrayList<>(6);
    private final String[][] p1GameField = new String[21][41];
    private final String[][] p2GameField = new String[21][41];
    private final String[][] p3GameField = new String[21][41];
    private final String[][] p4GameField = new String[21][41];
    private final String firstLine = "╔═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗\n";
    private final String chatLine = "╟──ChatBox──────────────────────────────────────────────────────────────────────────────────────────────────────────────╢\n";
    private final String lastLine = "╚═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝\n";
    private final String emptyLine = "║                                                                                                                       ║\n";

    //official infos needed:


    /**
     * Constructor method for this class
     */
    public CLI(){
        initializeChat();
        initializeTopOfGDeck();
        initializeTopOfRDeck();
    }

    //-------------------------------------------------------------------------------------------display print

    /**
     * method used to print the entire game screen
     */
    public void printScreen(){
        gameScreen.forEach(System.out::println);
    }
    //-------------------------------------------------------------------------------------------Help box

    /**
     * method used to print the HelpBox
     */
    public void printHelpBox(){
        System.out.println("┌─HelpBox──────────────────────┬────────────────────────────────────────────────────────────────────────────────────┐\n" +
                "│ \u001B[1mCOMMANDS\u001B[22m                     │ \u001B[1mHOW TO USE THEM\u001B[22m                                                                    │\n" +
                "├──────────────────────────────┼────────────────────────────────────────────────────────────────────────────────────┤\n" +
                "│\u001B[4mALWAYS AVAILABLE:\u001B[24m             │                                                                                    │\n" +
                "│-ShowField 'nickname'         │ displays the given player's game field                                             │\n" +
                "│-ShowCard 'x' 'y'             │ displays the card at the given coordinates (when possible)                         │\n" +
                "│-All 'message'                │ sends the message to every player in the game                                      │\n" +
                "│-W 'nickname' ' message'      │ sends the message only to the chosen player                                        │\n" +
                "│\u001B[4mONLY DURING YOUR TURN:\u001B[24m        │                                                                                    │\n" +
                "│-Play 'n card' 'x' 'y' 'face' │ place chosen card at the given ('x', 'y') and the facing 'up' or 'down'            │\n" +
                "│-Draw 'card type' 'n'         │ draw a 'resource'/'gold' card; n={0 (deck), 1 (first face up), 2 (second face up)} │\n" +
                "└──────────────────────────────┴────────────────────────────────────────────────────────────────────────────────────┘");
    }
    //-------------------------------------------------------------------------------------------ObjectiveChoice

    public void printObjectiveChoice(ObjectiveCard obj1, ObjectiveCard obj2){
        System.out.println(" ");
    }

    //-------------------------------------------------------------------------------------------StarterCardFacingChoice

    /**
     * method that prints both faces of a StarterCard to show to the Player so that they can choose
     * @param sc the StarterCard drawn by the Player
     */
    public void printStarterCardChoice(StarterCard sc){
        sc.setFace(true);
        ArrayList<String> front = getCard(sc);
        sc.setFace(false);
        ArrayList<String> back =getCard(sc);
        System.out.println("Face-up:\n" +
                " " + front.getFirst() + "\n" +
                " " + front.get(1) + "\n" +
                " " + front.get(2) + "\n" +
                " " + front.get(3) + "\n" +
                "Face-down:\n" +
                " " + back.getFirst() + "\n" +
                " " + back.get(1) + "\n" +
                " " + back.get(2) + "\n" +
                " " + back.get(3) + "\n");
    }

    //-------------------------------------------------------------------------------------------NamesColorsScoresHands

    //"PlayerNicknameX   " -> getNick()
    //"Color xxPTS BBB  " -> getPlayerColor() + getPoints + getHand()

    private String getNick(String nickname){
        return String.format("%-15s", nickname) + "   ";
    }

    private String getPlayerColor(Color color){
        switch (color){
            case RED -> {
                return "\u001B[31mRed\u001B[0m    ";
            }
            case YELLOW -> {
                return "\u001B[33mYellow\u001B[0m ";
            }
            case BLUE -> {
                return "\u001B[34mBlue\u001B[0m   ";
            }
            case GREEN -> {
                return "\u001B[32mGreen\u001B[0m  ";
            }
            default -> {
                return null;
            }
        }
    }

    private String getPoints(int pts){
        if(pts<10)
            return " " + pts + "PTS ";
        else
            return pts + "PTS ";
    }

    private String getHand(Symbol[] hand){
        return colorString(hand[0], "█") + colorString(hand[0], "█") + colorString(hand[0], "█");
    }

    //-------------------------------------------------------------------------------------------Card Building

    private ArrayList<String> getCard(GoldCard c){
        ArrayList<String> card = new ArrayList<>(4);
        card.add(0, "\u001B[30m╔═══════════╗\u001B[0m");
        if(c.getFace()) {
            card.add(1, "\u001B[30m║" + getSymbol(c, NW) + "   " + getPointsCondition(c) + "   " + getSymbol(c, NE) + "\u001B[0m║");
            card.add(2, "\u001B[30m║" + getSymbol(c, SW) + "  " + getPlacementCondition(c) + "  " + getSymbol(c, SE) + "\u001B[0m║");
        }else{
            card.add(1, "\u001B[30m║⛶         ⛶\u001B[0m║");
            card.add(2, "\u001B[30m║⛶         ⛶\u001B[0m║");
        }
        card.add(3, "\u001B[30m╚═══════════╝\u001B[0m");
        return card;
    }

    private ArrayList<String> getCard(ResourceCard c){
        ArrayList<String> card = new ArrayList<>(4);
        card.add(0, "\u001B[30m┌───────────┐\u001B[0m");
        if(c.getFace()){
            card.add(1, "\u001B[30m│" + getSymbol(c, NW) +"   " + getPointsCondition(c) + "   " + getSymbol(c, NE) +"\u001B[0m│");
            card.add(2, "\u001B[30m│" + getSymbol(c, SW) +"         " + getSymbol(c, SE) +"\u001B[0m│");
        }else{
            card.add(1, "\u001B[30m│⛶         ⛶\u001B[0m│");
            card.add(2, "\u001B[30m│⛶         ⛶\u001B[0m│");
        }
        card.add(3, "\u001B[30m└───────────┘\u001B[0m");
        return card;
    }

    private ArrayList<String> getCard(StarterCard c){
        ArrayList<String> card = new ArrayList<>(4);
        card.add(0, "\u001B[30m┌───────────┐\u001B[0m");
        card.add(1, "\u001B[30m│" + getSymbol(c, NW) +"   " + getCentralResources(c) + "   " + getSymbol(c, NE) +"\u001B[0m│");
        card.add(2, "\u001B[30m│" + getSymbol(c, SW) +"         " + getSymbol(c, SE) +"\u001B[0m│");
        card.add(3, "\u001B[30m└───────────┘\u001B[0m");
        return card;
    }

    private String getSymbol(GoldCard c, Orientation o){
        return getSymbolChar(c.getCorner(o).getSymbol());
    }

    private String getSymbol(ResourceCard c, Orientation o){
        return getSymbolChar(c.getCorner(o).getSymbol());
    }

    private String getSymbol(StarterCard c, Orientation o){
        return getSymbolChar(c.getCorner(o).getSymbol());
    }

    private String getCentralResources(StarterCard c){
        Symbol[] sy = c.getCentralKingdom();
        return getSymbolChar(sy[1]) + getSymbolChar(sy[0]) + getSymbolChar(sy[2]);
    }

    private String getPointsCondition(GoldCard c){
        if(c.getGoldPointsCondition() == null)
            return " " + c.getPointsPerCondition() + " ";
        else
            return c.getPointsPerCondition() + "|" + getSymbolChar(c.getGoldPointsCondition());
    }

    private String getPointsCondition(ResourceCard c){
        if(c.getPointsPerCondition() == 1)
            return " 1 ";
        else
            return "   ";
    }

    private String getPlacementCondition(GoldCard c){
        Symbol[] sy = c.getGoldPlayableCondition();
        switch (sy.length) {
            case 3 -> {
                return " " + getSymbolChar(sy[0]) + getSymbolChar(sy[1]) + getSymbolChar(sy[2]) + " ";
            }
            case 4 -> {
                return getSymbolChar(sy[0]) + getSymbolChar(sy[1]) + getSymbolChar(sy[2]) + getSymbolChar(sy[3]) + " ";
            }
            case 5 -> {
                return getSymbolChar(sy[0]) + getSymbolChar(sy[1]) + getSymbolChar(sy[2]) + getSymbolChar(sy[3]) + getSymbolChar(sy[4]);
            }
            default -> {
                return null;
            }
        }
    }

    private String getSymbolChar(Symbol s){
        switch (s) {
            case INKWELL -> {
                return "⛫";
            }
            case MANUSCRIPT -> {
                return "✉";
            }
            case QUILL -> {
                return "⚲";
            }
            case FUNGI -> {
                return "⍾";
            }
            case PLANT -> {
                return "⚘";
            }
            case ANIMAL -> {
                return "♘";
            }
            case INSECT -> {
                return "ଫ";
            }
            case CORNER -> {
                return "▘";
            }
            case NULL -> {
                return "⛶";
            }
            default -> {
                return " ";
            }
        }
    }

    private ArrayList<String> colorCard(ArrayList<String> card, Symbol kingdom){
        return card.stream().map(s -> colorBackgroundString(kingdom, s)).collect(Collectors.toCollection(ArrayList::new));
    }

    //-------------------------------------------------------------------------------------------TopOfDecks

    private void initializeTopOfGDeck(){
        topOfGDeck.add(0, "Gold         ");
        topOfGDeck.add(1, "Deck:        ");
    }

    private void initializeTopOfRDeck(){
        topOfRDeck.add(0, "Resource     ");
        topOfRDeck.add(1, "Deck:        ");
    }

    public void updateTopOfGDeck(Symbol color){
        topOfGDeck.add(2, colorBackgroundString(color, "╔═══════════╗"));
        topOfGDeck.add(3, colorBackgroundString(color, "║           ║"));
        topOfGDeck.add(4, colorBackgroundString(color, "║           ║"));
        topOfGDeck.add(5, colorBackgroundString(color, "╚═══════════╝"));
    }

    public void updateTopOfRDeck(Symbol color){
        topOfRDeck.add(2, colorBackgroundString(color, "┌───────────┐"));
        topOfRDeck.add(3, colorBackgroundString(color, "│           │"));
        topOfRDeck.add(4, colorBackgroundString(color, "│           │"));
        topOfRDeck.add(5, colorBackgroundString(color, "└───────────┘"));
    }

    //-------------------------------------------------------------------------------------------CoordinatesConversion

    /**
     * method used to get the value of x on the real game field from the x and y of the model's data structure
     * @param x coordinate of a position for a card on the data structure
     * @param y coordinate of a position for a card on the data structure
     * @return the respective x value on the shown game field
     */
    private int getStraightX(int x, int y){
            return x-y;
    }

    /**
     * method used to get the value of y on the real game field from the x and y of the model's data structure
     * @param x coordinate of a position for a card on the data structure
     * @param y coordinate of a position for a card on the data structure
     * @return the respective y value on the shown game field
     */
    private int getStraightY(int x, int y){
        return x + y;
    }

    /**
     * method used to get the line in which to write a char, given the coordinate y given
     * @param y coordinate of the game field given
     * @return the line in which to write
     */
    private int yToLine(int y){
        if(y<0)
            return -((Math.abs(y)+1)/2);
        else
            return y/2;
    }

    //------------------------------------------------------------------------------------------------GenericColoring

    private String colorString(Symbol kingdom, String s) {
        switch (kingdom) {
            case ANIMAL -> {
                return "\u001B[34m" + s + "\u001B[0m";
            }
            case FUNGI -> {
                return "\u001B[31m" + s + "\u001B[0m";
            }
            case PLANT -> {
                return "\u001B[32m" +s + "\u001B[0m";
            }
            case INSECT -> {
                return "\u001B[35m" + s + "\u001B[0m";
            }
            default -> {
                return s;
            }
        }
    }

    private String colorBackgroundString(Symbol kingdom, String s) {
        switch (kingdom) {
            case ANIMAL -> {
                return "\u001B[44m" + s + "\u001B[0m";
            }
            case FUNGI -> {
                return "\u001B[41m" + s + "\u001B[0m";
            }
            case PLANT -> {
                return "\u001B[42m" +s + "\u001B0m";
            }
            case INSECT -> {
                return "\u001B[45m" + s + "\u001B[0m";
            }
            default -> {
                return "\u001B[47m" + s + "\u001B[0m";
            }
        }
    }
        /*public String characterPixel (){
            switch () {
                case  -> {
                    return "▀";
                }
                case "" -> {
                    return "▄";
                }
                case "" -> {
                    return " ";
                }
                case "" -> {
                    return "";
                }
                default -> {
                    return "";
                }
            }
        }*/

    //----------------------------------------------------------------------------------------------------Chat

    private void initializeChat(){
        for (int i = 0; i < 6; i++) {
            chat.add(i, emptyLine);
        }
    }

    public void receiveMessage(String message){
        int i;
        for (i = 0; i < 6 && !chat.get(i).equals(emptyLine); i++);
        if(i == 6)
            for(i = 0; i < 5 ; i++)
                 chat.add(i, chat.get(i+1));
        chat.add(i, formatMessage(message));
    }

    private String formatMessage(String message){
        return "║" + String.format("%-100s", message) + "║\n";
    }

    private void printChat(){
        System.out.println(chatLine);
        for (String s : chat) {
            System.out.println(s);
        }
    }

    //---------------------------------------------------------------------------------------------------TitleTESTS

    /**
     * method used to print the title of the game on the CLI
     */
    public void printTitle(){
        String gameTitle1 = "\u001B[31m░░░░░░░░░░\u001B[32m░░░░░░░░░░\u001B[34m░░░░░░░░░░\u001B[35m░░░░░░░░░░\u001B[31m░░░░░░░░░░\u001B[32m░░░░░░\u001B[34m░░░░░░░░░░\u001B[35m░░░░░░░░░░\u001B[31m░░░░░░░░░░\u001B[32m░░░░░░░░░░\u001B[34m░░░░░░░░░░\u001B[35m░░░░░░░░░░\u001B[31m░░░░░░░░░░\u001B[32m░░░░░░░░░░\u001B[34m░░░░░░░░░░\u001B[0m\n" +
                "\u001B[32m░░      ░░\u001B[34m░░      ░░\u001B[35m░       ░░\u001B[31m░        ░\u001B[32m░  ░░░░  ░\u001B[34m░░░░░░\u001B[35m░   ░░░  ░\u001B[31m░░      ░░\u001B[32m░        ░\u001B[34m░  ░░░░  ░\u001B[35m░       ░░\u001B[31m░░      ░░\u001B[32m░  ░░░░░░░\u001B[34m░        ░\u001B[35m░░      ░░\u001B[0m\n" +
                "\u001B[34m▒  ▒▒▒▒  ▒\u001B[35m▒  ▒▒▒▒  ▒\u001B[31m▒  ▒▒▒▒  ▒\u001B[32m▒  ▒▒▒▒▒▒▒\u001B[34m▒▒  ▒▒  ▒▒\u001B[35m▒▒▒▒▒▒\u001B[31m▒    ▒▒  ▒\u001B[32m▒  ▒▒▒▒  ▒\u001B[34m▒▒▒▒  ▒▒▒▒\u001B[35m▒  ▒▒▒▒  ▒\u001B[31m▒  ▒▒▒▒  ▒\u001B[32m▒  ▒▒▒▒  ▒\u001B[34m▒  ▒▒▒▒▒▒▒\u001B[35m▒▒▒▒  ▒▒▒▒\u001B[31m▒  ▒▒▒▒▒▒▒\u001B[0m\n" +
                "\u001B[35m▓  ▓▓▓▓▓▓▓\u001B[31m▓  ▓▓▓▓  ▓\u001B[32m▓  ▓▓▓▓  ▓\u001B[34m▓      ▓▓▓\u001B[35m▓▓▓    ▓▓▓\u001B[31m▓▓▓▓▓▓\u001B[32m▓  ▓  ▓  ▓\u001B[34m▓  ▓▓▓▓  ▓\u001B[35m▓▓▓▓  ▓▓▓▓\u001B[31m▓  ▓▓▓▓  ▓\u001B[32m▓       ▓▓\u001B[34m▓  ▓▓▓▓  ▓\u001B[35m▓  ▓▓▓▓▓▓▓\u001B[31m▓▓▓▓  ▓▓▓▓\u001B[32m▓▓      ▓▓\u001B[0m\n" +
                "\u001B[31m█  ████  █\u001B[32m█  ████  █\u001B[34m█  ████  █\u001B[35m█  ███████\u001B[31m██  ██  ██\u001B[32m██████\u001B[34m█  ██    █\u001B[35m█        █\u001B[31m████  ████\u001B[32m█  ████  █\u001B[34m█  ███  ██\u001B[35m█        █\u001B[31m█  ███████\u001B[32m████  ████\u001B[34m███████  █\u001B[0m\n" +
                "\u001B[32m██      ██\u001B[34m██      ██\u001B[35m█       ██\u001B[31m█        █\u001B[32m█  ████  █\u001B[34m██████\u001B[35m█  ███   █\u001B[31m█  ████  █\u001B[32m████  ████\u001B[34m██      ██\u001B[35m█  ████  █\u001B[31m█  ████  █\u001B[32m█        █\u001B[34m█        █\u001B[35m██      ██\u001B[0m\n" +
                "\u001B[34m██████████\u001B[35m██████████\u001B[31m██████████\u001B[32m██████████\u001B[34m██████████\u001B[35m██████\u001B[31m██████████\u001B[32m██████████\u001B[34m██████████\u001B[35m██████████\u001B[31m██████████\u001B[32m██████████\u001B[34m██████████\u001B[35m██████████\u001B[31m██████████\u001B[0m";

        String gameTitle2 = "\u001B[31m░░░░░░░░░░\u001B[32m░░░░░░░░░░\u001B[34m░░░░░░░░░░\u001B[35m░░░░░░░░░░\u001B[31m░░░░░░░░░░\u001B[32m░░░░░░\u001B[34m░░░░░░░░░░\u001B[35m░░░░░░░░░░\u001B[31m░░░░░░░░░░\u001B[32m░░░░░░░░░░\u001B[34m░░░░░░░░░░\u001B[35m░░░░░░░░░░\u001B[31m░░░░░░░░░░\u001B[32m░░░░░░░░░░\u001B[34m░░░░░░░░░░\u001B[0m\n" +
                "\u001B[31m░░      ░░\u001B[32m░░      ░░\u001B[34m░       ░░\u001B[35m░        ░\u001B[31m░  ░░░░  ░\u001B[32m░░░░░░\u001B[34m░   ░░░  ░\u001B[35m░░      ░░\u001B[31m░        ░\u001B[32m░  ░░░░  ░\u001B[34m░       ░░\u001B[35m░░      ░░\u001B[31m░  ░░░░░░░\u001B[32m░        ░\u001B[34m░░      ░░\u001B[0m\n" +
                "\u001B[31m▒  ▒▒▒▒  ▒\u001B[32m▒  ▒▒▒▒  ▒\u001B[34m▒  ▒▒▒▒  ▒\u001B[35m▒  ▒▒▒▒▒▒▒\u001B[31m▒▒  ▒▒  ▒▒\u001B[32m▒▒▒▒▒▒\u001B[34m▒    ▒▒  ▒\u001B[35m▒  ▒▒▒▒  ▒\u001B[31m▒▒▒▒  ▒▒▒▒\u001B[32m▒  ▒▒▒▒  ▒\u001B[34m▒  ▒▒▒▒  ▒\u001B[35m▒  ▒▒▒▒  ▒\u001B[31m▒  ▒▒▒▒▒▒▒\u001B[32m▒▒▒▒  ▒▒▒▒\u001B[34m▒  ▒▒▒▒▒▒▒\u001B[0m\n" +
                "\u001B[31m▓  ▓▓▓▓▓▓▓\u001B[32m▓  ▓▓▓▓  ▓\u001B[34m▓  ▓▓▓▓  ▓\u001B[35m▓      ▓▓▓\u001B[31m▓▓▓    ▓▓▓\u001B[32m▓▓▓▓▓▓\u001B[34m▓  ▓  ▓  ▓\u001B[35m▓  ▓▓▓▓  ▓\u001B[31m▓▓▓▓  ▓▓▓▓\u001B[32m▓  ▓▓▓▓  ▓\u001B[34m▓       ▓▓\u001B[35m▓  ▓▓▓▓  ▓\u001B[31m▓  ▓▓▓▓▓▓▓\u001B[32m▓▓▓▓  ▓▓▓▓\u001B[34m▓▓      ▓▓\u001B[0m\n" +
                "\u001B[31m█  ████  █\u001B[32m█  ████  █\u001B[34m█  ████  █\u001B[35m█  ███████\u001B[31m██  ██  ██\u001B[32m██████\u001B[34m█  ██    █\u001B[35m█        █\u001B[31m████  ████\u001B[32m█  ████  █\u001B[34m█  ███  ██\u001B[35m█        █\u001B[31m█  ███████\u001B[32m████  ████\u001B[34m███████  █\u001B[0m\n" +
                "\u001B[31m██      ██\u001B[32m██      ██\u001B[34m█       ██\u001B[35m█        █\u001B[31m█  ████  █\u001B[32m██████\u001B[34m█  ███   █\u001B[35m█  ████  █\u001B[31m████  ████\u001B[32m██      ██\u001B[34m█  ████  █\u001B[35m█  ████  █\u001B[31m█        █\u001B[32m█        █\u001B[34m██      ██\u001B[0m\n" +
                "\u001B[31m██████████\u001B[32m██████████\u001B[34m██████████\u001B[35m██████████\u001B[31m██████████\u001B[32m██████\u001B[34m██████████\u001B[35m██████████\u001B[31m██████████\u001B[32m██████████\u001B[34m██████████\u001B[35m██████████\u001B[31m██████████\u001B[32m██████████\u001B[34m██████████\u001B[0m";

        System.out.println(gameTitle1);
        System.out.println(gameTitle2);
        }
    public void testingFieldPrint(Field pF){
        List<Integer> cd = pF.getSortedVector().stream()
                .map(c -> c.coordinates().x() + c.coordinates().y())
                .toList();
        int upperBound = Collections.max(cd);
        cd = pF.getSortedVector().stream()
                .map(c -> c.coordinates().x() - c.coordinates().y())
                .toList();
        int rightBound = Collections.max(cd);
        cd = pF.getSortedVector().stream()
                .map(c -> c.coordinates().x() + c.coordinates().y())
                .toList();
        int lowerBound = -Collections.min(cd);
        cd = pF.getSortedVector().stream()
                .map(c -> c.coordinates().x() - c.coordinates().y())
                .toList();
        int leftBound = -Collections.min(cd);
        int height = upperBound + lowerBound +1;
        int width = rightBound + leftBound +1;
    }

}
/*
╔═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗
║                                                                                                                       ║
║    ▄ ▄ ▄ ▄ ▄ ▄ ▄ ▄ ▄ ▄ ▄ ▄ ▄ ▄ ▄ ▄ ▄ ▄ ▄ ▄    PlayerNickname1   PlayerNickname2   PlayerNickname3   PlayerNickname4   ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   red    xxPTS ▀▄▀  blue   xxPTS ▀▄▀  green  xxPTS ▀▄▀  yellow xxPTS ▀▄▀  ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀                                                                           ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀                                                                           ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   Shared Objective 1: xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx   ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   Shared Objective 2: xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx   ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   Personal Objective: xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx   ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   Gold          Resource                                                  ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   Deck:         Deck:            ┌──Card─Display───┐    ┌Shown─Symbols─┐  ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   ╔═══════════╗ ┌───────────┐    │                 │    │    ⚘ : xx    │  ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   ║           ║ │           │    │  ┌───────────┐  │    │    ଫ : xx    │  ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   ║           ║ │           │    │  │           │  │    │    ⍾ : xx    │  ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   ╚═══════════╝ └───────────┘    │  │           │  │    │    ♘ : xx    │  ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   Face Up Cards:                 │  └───────────┘  │    │    ⚲ : xx    │  ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   ╔═══════════╗ ┌───────────┐    │                 │    │    ✉ : xx    │  ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   ║           ║ │           │    └─────────────────┘    │    ⛫ : xx    │  ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   ║           ║ │           │   Cards in your hand:     └──────────────┘  ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   ╚═══════════╝ └───────────┘   1)            2)            3)            ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   ╔═══════════╗ ┌───────────┐   ┌───────────┐ ┌───────────┐ ┌───────────┐ ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   ║           ║ │           │   │           │ │           │ │           │ ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   ║           ║ │           │   │           │ │           │ │           │ ║
║                                               ╚═══════════╝ └───────────┘   └───────────┘ └───────────┘ └───────────┘ ║
╟──ChatBox──────────────────────────────────────────────────────────────────────────────────────────────────────────────╢
║Mattix124: cacca                                                                                                       ║
║Tommy(whispers): mattea è proprio scarso                                                                               ║
║Beppe: ez win                                                                                                          ║
║You: brb!                                                                                                              ║
║...                                                                                                                    ║
║                                                                                                                       ║
╚═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝
┌──────────┐
│  ⚘ : xx  │
│  ଫ : xx  │
│  ⍾ : xx  │
│  ♘ : xx  │
│  ⚲ : xx  │
│  ✉ : xx  │
│  ⛫ : xx  │
└──────────┘
─ │ ┌ ┐ └ ┘ ├ ┤ ┬ ┴ ┼
═  ║  ╒	╓	╔	╕	╖	╗	╘	╙	╚	╛	╜	╝	╞	╟
╠  ╡	 ╢	╣	╤	╥	╦	╧	╨	╩	╪	╫	╬
13x4
┌───────────┐ ┌─┼───────┼─┐ ┌───────────┐ ┌───────────┐ ┌───────────┐
│           │ ┼           ┼ │          ✗│ │⊟         ⊡│ │           │
│           │ ┼─┐        x┼ │o         x│ │          ⎕│ │           │
└───────────┘ └─┼───────┼─┘ └───────────┘ └───────────┘ └───────────┘
╔═══════════╗
║⚘   1|▘   ⛶║
║✉  ଫଫଫଫଫ  ⛫║
╚═══════════╝
1) ╔═════════╗
   ║⚘  1|▘  ⛶║
   ║✉ ଫଫଫଫଫ ⛫║
   ╚═════════╝
2) ┌───────────┐
   │⚘    1    ଫ│
   │⚲         ⛶│
   └───────────┘
╔══════╗ ┌──────┐
║      ║ │      │
╚══════╝ └──────┘
resources: ⚘ ଫ ⍾ ♘
items: ⚲ ✉ ⛫
(⛶ empty corner != non-existing corner)
placing conditions: da 3 a 5 kingdoms " *** ", "**** ", "*****"
points conditions: (corner covered:) "2|▘", (items shown:) "1|*", (flat points:) " 3 ",
⚘ ଫ ⍾ ♘ ⚲ ✉ ⛫ ⛶ 2|▘
┌───────────┐
│ଫ    5     │
│♘  ⚘⚘⚘⚘⚘ ╔═╪═══════╪═╗
└─────────╫⚲   1|✉    ╫
┌─────────╫─┐  ⚘⚘⚘    ╫─────────┐
│⍾        ╚═╪═══════╪═╝         │
│          x│       │           │
└───────────┘       └───────────┘
┌───────────┐
│B    5     │
│A  PPPPP ╔═══════════╗
└─────────║Q   1|M    ║
┌─────────║    PPP    ║─────────┐
│F        ╚═══════════╝         │
│⎕         x│       │           │
└───────────┘       └───────────┘
sfondo carte colorato per rappresentre il kingdom (red, green, blue, purple)
⚘ ଫ ⍾ ♘ ⚲ ✉ ⛫
┌──────────┐
│  ⚘ : xx  │
│  ଫ : xx  │
│  ⍾ : xx  │
│  ♘ : xx  │
│  ⚲ : xx  │
│  ✉ : xx  │
│  ⛫ : xx  │
└──────────┘
┌─HelpBox───────────────────────────┬───────────────────────────────────────────────────────────────────────────────────────┐
│ COMMANDS                          │ HOW TO USE THEM                                                                       │
├───────────────────────────────────┼───────────────────────────────────────────────────────────────────────────────────────┤
│ALWAYS AVAILABLE:                  │                                                                                       │
│-ShowField 'nickname'              │ displays the given player's game field                                                │
│-ShowCard 'x' 'y'                  │ displays the card at the given coordinates (when possible)                            │
│-All 'message'                     │ sends the message to every player in the game                                         │
│-W 'nickname' ' message'           │ sends the message only to the chosen player                                           │
│ONLY DURING YOUR TURN:             │                                                                                       │
│-Play 'card number' 'x' 'y' 'face' │ place (when possible) the card at the given ('x', 'y') and the facing 'up' or 'down'  │
│-Draw 'card type' 'n'              │ draw a 'resource' or 'gold' card, n : 0 (deck), 1 (first face up), 2 (second face up) │
└───────────────────────────────────┴───────────────────────────────────────────────────────────────────────────────────────┘

ඩ ඞ
*/
