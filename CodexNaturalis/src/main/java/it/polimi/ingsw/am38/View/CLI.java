package it.polimi.ingsw.am38.View;

import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Enum.Orientation;
import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Board.Field;
import it.polimi.ingsw.am38.Model.Cards.*;
import it.polimi.ingsw.am38.Network.Server.SortPlayerThread;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.ingsw.am38.Enum.Orientation.*;

public class CLI implements Viewable{
    private final String emptyLine = "║                                                                                                                       ║\n";
    private final ArrayList<String> gameScreen = new ArrayList<>(24);
    private final String[][] p1GameField = new String[21][41];
    private final String[][] p2GameField = new String[21][41];
    private final String[][] p3GameField = new String[21][41];
    private final String[][] p4GameField = new String[21][41];
    private final ArrayList<String> chat = new ArrayList<>(6);
    private LinkedList<String> goldGround1 = new LinkedList<>();
    private LinkedList<String> goldGround2 = new LinkedList<>();
    private LinkedList<String> resourceGround1 = new LinkedList<>();
    private LinkedList<String> resourceGround2 = new LinkedList<>();
    private final LinkedList<String> topOfGDeck = new LinkedList<>();
    private final LinkedList<String> topOfRDeck = new LinkedList<>();
    private final String deckNames = "Gold          Resource     ";
    private final String deckWords = "Deck:         Deck:        ";
    private String sharedObj1, sharedObj2, personalObj;

/*
╔═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗
║ ENDGAME STARTED ! - ENDGAME STARTED ! - ENDGAME STARTED ! - ENDGAME STARTED ! - ENDGAME STARTED ! - ENDGAME STARTED ! ║
║  xx↓                 xx↓                 xx↓  PlayerNickname1   PlayerNickname2   PlayerNickname3   PlayerNickname4   ║
║ xx↗▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀  red    xxPTS ███  blue   xxPTS ███  green  xxPTS ███  yellow xxPTS ███  ║
║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀                                                                          ║
║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀  1) xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxx ║
║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀  2) xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxx ║
║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀  P) xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxx ║
║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀                                                                          ║
║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀  Gold          Resource                                                  ║
║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀  Deck:         Deck:            ┌──Card─Display───┐    ┌Shown─Symbols─┐  ║
║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀  ╔═══════════╗ ┌───────────┐    │                 │    │    ⚘ : xx    │  ║
║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀  ║           ║ │           │    │  ┌───────────┐  │    │    ଫ : xx    │  ║
║ xx↗▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀  ║           ║ │           │    │  │           │  │    │    ⍾ : xx    │  ║
║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀  ╚═══════════╝ └───────────┘    │  │           │  │    │    ♘ : xx    │  ║
║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀  Face Up Cards:                 │  └───────────┘  │    │    ⚲ : xx    │  ║
║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀  ╔═══════════╗ ┌───────────┐    │                 │    │    ✉ : xx    │  ║
║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀  ║           ║ │           │    └─────────────────┘    │    ⛫ : xx    │  ║
║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀  ║           ║ │           │   Cards in your hand:     └──────────────┘  ║
║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀  ╚═══════════╝ └───────────┘   1)            2)            3)            ║
║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀  ╔═══════════╗ ┌───────────┐   ┌───────────┐ ┌───────────┐ ┌───────────┐ ║
║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀  ║           ║ │           │   │           │ │           │ │           │ ║
║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀  ║           ║ │           │   │           │ │           │ │           │ ║
║ xx↗▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀  ╚═══════════╝ └───────────┘   └───────────┘ └───────────┘ └───────────┘ ║
╟──ChatBox──────────────────────────────────────────────────────────────────────────────────────────────────────────────╢
║Mattix124: cacca                                                                                                       ║
║Tommy(whispers): mattea è proprio scarso                                                                               ║
║Beppe: ez win                                                                                                          ║
║You: brb!                                                                                                              ║
║...                                                                                                                    ║
║xxxxxxxxx xxxxxxxxx xxxxxxxxx xxxxxxxxx xxxxxxxxx xxxxxxxxx xxxxxxxxx xxxxxxxxx xxxxxxxxx xxxxxxxxx xxxxxxxxx xxxxxxxxx║
╚═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝
*/
    /**
     * Constructor method for this class
     */
    public CLI(){
        initializeChat();
        gameScreen.addFirst("╔═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗\n");
    }

    //--------------------------------------------------------------------------------------------DisplayPrint

    /**
     * method used to print the entire game screen
     */
    public void printScreen(){
        gameScreen.forEach(System.out::println);
    }

    private void updateGameScreen(boolean endGame){
        gameScreen.add(1, isEndgame(endGame));
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
    //-------------------------------------------------------------------------------------------------Objectives

    public void printSharedObjectives(String sharedObj1, String sharedObj2){//, , LinkedList<String> nicknames){
        System.out.println("Shared objectives:\n" + sharedObj1 + "\n" + sharedObj2 + "\n");
    }

    public void printObjectiveChoice(String objChoice1, String objChoice2){
        System.out.println("Choose:\n1) " + objChoice1 + "\n2) " + objChoice2 + "\n");
    }

    public void setPersonalObjective(String objective){
            this.personalObj = "P) " + String.format("%-68s", objective);
    }

    //-------------------------------------------------------------------------------------------StarterCardFacingChoice

    /**
     * method that prints both faces of a StarterCard to show to the Player so that they can choose
     * @param sc the StarterCard drawn by the Player
     */
    public void printStarterCardChoice(StarterCard sc){
        sc.setFace(true);
        LinkedList<String> front = getCard(sc);
        sc.setFace(false);
        LinkedList<String> back = getCard(sc);
        System.out.println("Face-up:      Face-down:\n" +
                front.getFirst() + " " + back.getFirst() +"\n" +
                front.get(1) + " " + back.get(1) + "\n" +
                front.get(2) + " " + back.get(2) + "\n" +
                front.get(3) + " " + back.get(3));
    }

    //------------------------------------------------------------------------------------NamesColorsScoresHandsEndGame

    //"PlayerNicknameX   " -> getNick()
    //"Color xxPTS BBB  " -> getPlayerColor() + getPoints + getHand()

    private String getNick(String nickname){
        return String.format("%-15s", nickname) + "   ";
    }

    private String getPlayerColor(Color color){
        return switch (color) {
            case RED -> "\u001B[31mRed\u001B[0m    ";
            case YELLOW -> "\u001B[33mYellow\u001B[0m ";
            case BLUE -> "\u001B[34mBlue\u001B[0m   ";
            case GREEN -> "\u001B[32mGreen\u001B[0m  ";
            default -> null;
        };
    }

    private String getPoints(int pts){
        if(pts<10)
            return " " + pts + "PTS ";
        else
            return pts + "PTS ";
    }

    private String getHandColors(Symbol[] hand){
        return colorString(hand[0], "█") + colorString(hand[0], "█") + colorString(hand[0], "█");
    }

    private String isEndgame(boolean b){
        if(b)
            return emptyLine;
        else
            return "║ ENDGAME STARTED ! - ENDGAME STARTED ! - ENDGAME STARTED ! - ENDGAME STARTED ! - ENDGAME STARTED ! - ENDGAME STARTED ! ║\n";

    }

    //-------------------------------------------------------------------------------------------Card Building

    private LinkedList<String> getCard(GoldCard c){
        LinkedList<String> card = new LinkedList<>();
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

    private LinkedList<String> getCard(ResourceCard c){
        LinkedList<String> card = new LinkedList<>();
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

    private LinkedList<String> getCard(StarterCard c){
        LinkedList<String> card = new LinkedList<>();
        card.add(0, "\u001B[47m\u001B[30m┌───────────┐\u001B[0m");
        card.add(1, "\u001B[47m\u001B[30m│" + getSymbol(c, NW) +"   " + getCentralResources(c) + "   " + getSymbol(c, NE) +"│\u001B[0m");
        card.add(2, "\u001B[47m\u001B[30m│" + getSymbol(c, SW) +"         " + getSymbol(c, SE) +"│\u001B[0m");
        card.add(3, "\u001B[47m\u001B[30m└───────────┘\u001B[0m");
        return card;
    }

    private String getSymbol(GoldCard c, Orientation o){
        if(c.getCorner(o) == null)
            return " ";
        return getSymbolChar(c.getCorner(o).getSymbol());
    }

    private String getSymbol(ResourceCard c, Orientation o){
        if(c.getCorner(o) == null)
            return " ";
        return getSymbolChar(c.getCorner(o).getSymbol());
    }

    private String getSymbol(StarterCard c, Orientation o){
        if(c.getCorner(o) == null)
            return " ";
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
        return switch (sy.length) {
            case 3 -> " " + getSymbolChar(sy[0]) + getSymbolChar(sy[1]) + getSymbolChar(sy[2]) + " ";
            case 4 -> getSymbolChar(sy[0]) + getSymbolChar(sy[1]) + getSymbolChar(sy[2]) + getSymbolChar(sy[3]) + " ";
            case 5 -> getSymbolChar(sy[0]) + getSymbolChar(sy[1]) + getSymbolChar(sy[2]) + getSymbolChar(sy[3]) + getSymbolChar(sy[4]);
            default -> null;
        };
    }

    private String getSymbolChar(Symbol s){
        if(s == null)
            return " ";
        return switch (s) {
            case INKWELL -> "⛫";
            case MANUSCRIPT -> "✉";
            case QUILL -> "⚲";
            case FUNGI -> "⍾";
            case PLANT -> "☘";
            case ANIMAL -> "♘";
            case INSECT -> "ଫ";
            case CORNER -> "▘";
            case NULL -> "☐";
        };
    }

    private ArrayList<String> colorCard(ArrayList<String> card, Symbol kingdom){
        return card.stream().map(s -> colorBackgroundString(kingdom, s)).collect(Collectors.toCollection(ArrayList::new));
    }

    //-------------------------------------------------------------------------------------------TopOfDecksAndGrounds

    public void setTopOfGDeck(Symbol color){
        topOfGDeck.add(2, colorBackgroundString(color, "╔═══════════╗"));
        topOfGDeck.add(3, colorBackgroundString(color, "║           ║"));
        topOfGDeck.add(4, colorBackgroundString(color, "║           ║"));
        topOfGDeck.add(5, colorBackgroundString(color, "╚═══════════╝"));
    }

    public void setTopOfRDeck(Symbol color){
        topOfRDeck.add(2, colorBackgroundString(color, "┌───────────┐"));
        topOfRDeck.add(3, colorBackgroundString(color, "│           │"));
        topOfRDeck.add(4, colorBackgroundString(color, "│           │"));
        topOfRDeck.add(5, colorBackgroundString(color, "└───────────┘"));
    }

    public void setGround(ResourceCard c, int i){
        if(i == 1)
            resourceGround1 = getCard(c);
        else if(i == 2)
            resourceGround2 = getCard(c);
    }

    public void setGround(GoldCard c, int i){
        if(i == 1)
            goldGround1 = getCard(c);
        else if(i == 2)
            goldGround2 = getCard(c);
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
        return switch (kingdom) {
            case ANIMAL -> "\u001B[34m" + s + "\u001B[0m";
            case FUNGI -> "\u001B[31m" + s + "\u001B[0m";
            case PLANT -> "\u001B[32m" + s + "\u001B[0m";
            case INSECT -> "\u001B[35m" + s + "\u001B[0m";
            default -> s;
        };
    }

    private String colorBackgroundString(Symbol kingdom, String s) {
        return switch (kingdom) {
            case ANIMAL -> "\u001B[44m" + s + "\u001B[0m";
            case FUNGI -> "\u001B[41m" + s + "\u001B[0m";
            case PLANT -> "\u001B[42m" + s + "\u001B0m";
            case INSECT -> "\u001B[45m" + s + "\u001B[0m";
            default -> "\u001B[47m" + s + "\u001B[0m";
        };
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
        return "║" + String.format("%-119s", message) + "║\n";
    }

    private void printChat(){
        String chatLine = "╟──ChatBox──────────────────────────────────────────────────────────────────────────────────────────────────────────────╢\n";
        System.out.println(chatLine);
        for (String s : chat) {
            System.out.println(s);
        }
        String endOfScreen = "╚═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝\n";
        System.out.println(endOfScreen);
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
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   1) xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxx ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   2) xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxx ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   P) xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxx ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀                                                                           ║
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
║xxxxxxxxx xxxxxxxxx xxxxxxxxx xxxxxxxxx xxxxxxxxx xxxxxxxxx xxxxxxxxx xxxxxxxxx xxxxxxxxx xxxxxxxxx xxxxxxxxx xxxxxxxxx║
╚═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝
┌──────────┐
│  ⚘ U+2618 : xx  │
│  ଫ U+0B2B: xx  │
│  ⍾ U+237E: xx  │
│  ♘U+2658 : xx  │
│  ⚲U+26B2 : xx  │
│  ✉U+2709 : xx  │
│  ⛫U+26EB : xx  │
└──────────┘
⛶ U+26F6
▘ U+2598

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
resources: ☘ ଫ ⍾ ♘
items: ⚲ ✉ ⛫
(⛶ empty corner != non-existing corner)
placing conditions: da 3 a 5 kingdoms " *** ", "**** ", "*****"
points conditions: (corner covered:) "2|▘", (items shown:) "1|*", (flat points:) " 3 ",
☘ ଫ ⍾ ♘ ⚲ ✉ ⛫ ⛶ 2|▘
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
