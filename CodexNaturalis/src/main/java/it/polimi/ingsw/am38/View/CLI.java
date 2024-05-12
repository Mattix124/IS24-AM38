package it.polimi.ingsw.am38.View;

import it.polimi.ingsw.am38.Enum.Orientation;
import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Board.Field;
import it.polimi.ingsw.am38.Model.Cards.GoldCard;
import it.polimi.ingsw.am38.Model.Cards.PlayableCard;
import it.polimi.ingsw.am38.Model.Cards.ResourceCard;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

import static it.polimi.ingsw.am38.Enum.Orientation.*;
import static it.polimi.ingsw.am38.Enum.Symbol.*;

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
    private final String[][] chat = new String [6][200];
    private final ArrayList<String> gameScreen = new ArrayList<>(32);
    private final HashMap<String, String> playersScores = new HashMap<>();
    private final String[][] p1GameField = new String[21][41];
    private final String[][] p2GameField = new String[21][41];
    private final String[][] p3GameField = new String[21][41];
    private final String[][] p4GameField = new String[21][41];
    private final String firstLine = "╔═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗";
    private final String chatLine = "╟──ChatBox──────────────────────────────────────────────────────────────────────────────────────────────────────────────╢";
    private final String lastLine = "╚═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝";
    private final String emptyLine = "║                                                                                                                       ║";

    //official infos needed:


    /**
     * Constructor method for this class
     */
    public CLI(){
    }

    //-------------------------------------------------------------------------------------------display updates

    private void getStartingScreen(){

    }

    //-------------------------------------------------------------------------------------------Card Building

    private ArrayList<String> getGoldCard(GoldCard c){
        ArrayList<String> card = new ArrayList<>(4);
        card.add(0, "╔═══════════╗");
        card.add(1, "║" + getSymbol(c, NW) +"   " + getPointsCondition(c) + "   " + getSymbol(c, NE) +"║");
        card.add(2, "║" + getSymbol(c, SW) +"  " + getPlacementCondition(c) + "  " + getSymbol(c, SE) +"║");
        card.add(3, "╚═══════════╝");
        return card;
    }

    private ArrayList<String> getResourceCard(ResourceCard c){
        ArrayList<String> card = new ArrayList<>(4);
        card.add(0, "┌───────────┐");
        card.add(1, "│" + getSymbol(c, NW) +"   " + getPointsCondition(c) + "   " + getSymbol(c, NE) +"│");
        card.add(2, "│" + getSymbol(c, SW) +"         " + getSymbol(c, SE) +"│");
        card.add(3, "└───────────┘");
        return card;
    }

    private String getSymbol(GoldCard c, Orientation o){
        return getSymbolChar(c.getCorner(o).getSymbol());
    }

    private String getSymbol(ResourceCard c, Orientation o){
        return getSymbolChar(c.getCorner(o).getSymbol());
    }

    private String getPointsCondition(GoldCard c){
        if(c.getGoldPointsCondition() == null)
            return String.valueOf(c.getPointsPerCondition());
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
        int i;
        String s = getSymbolChar(sy[0]);
        for(i = 1; i < sy.length; i++)
            s.concat(getSymbolChar(sy[i]));
        return s;
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
    //--------------------------------------------------------------------------------------------------------
    /**
     * method used to get the color of a PlayableCard present on the shownGameField
     * @param x coordinate
     * @param y coordinate
     * @return the color of that card (String)
     */
    public String getCardColor(int x, int y){
        PlayableCard c = shownGameField.getCardFromCoordinate(x, y);
        if(c == null)
            return " ";
        return getKingdomColor(c.getKingdom());
    }

    public String colorString(String color, String s) {
        switch (color) {
            case "blue" -> {
                return "\u001B[34m" + s + "\u001B[34m";
            }
            case "red" -> {
                return "\u001B[31m" + s + "\u001B[31m";
            }
            case "green" -> {
                return "\u001B[42m" +s + "\u001B[42m";
            }
            case "purple" -> {
                return "\u001B[45m" + s + "\u001B[45m";
            }
            default -> {
                return s;
            }
        }
    }
        /*public String makeCharacterPixel (){
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

    /**
     * method used to print the title of the game on the CLI
     */
    public void printTitle(){
        String gameTitle1 = "   ____ ___  ____  _______  __    _   _       _                   _ _\n" +
                "  / ___/ _ \\|  _ \\| ____\\ \\/ /_  | \\ | | __ _| |_ _   _ _ __ __ _| (_)___\n" +
                " | |  | | | | | | |  _|  \\  /(_) |  \\| |/ _` | __| | | | '__/ _` | | / __|\n" +
                " | |__| |_| | |_| | |___ /  \\ _  | |\\  | (_| | |_| |_| | | | (_| | | \\__ \\\n" +
                "  \\____\\___/|____/|_____/_/\\_(_) |_| \\_|\\__,_|\\__|\\__,_|_|  \\__,_|_|_|___/";
        String gameTitle2 = "░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░\n" +
                "░░      ░░░░      ░░░       ░░░        ░░  ░░░░  ░░░░░░░░   ░░░  ░░░      ░░░        ░░  ░░░░  ░░       ░░░░      ░░░  ░░░░░░░░        ░░░      ░░\n" +
                "▒  ▒▒▒▒  ▒▒  ▒▒▒▒  ▒▒  ▒▒▒▒  ▒▒  ▒▒▒▒▒▒▒▒▒  ▒▒  ▒▒▒▒▒▒▒▒▒    ▒▒  ▒▒  ▒▒▒▒  ▒▒▒▒▒  ▒▒▒▒▒  ▒▒▒▒  ▒▒  ▒▒▒▒  ▒▒  ▒▒▒▒  ▒▒  ▒▒▒▒▒▒▒▒▒▒▒  ▒▒▒▒▒  ▒▒▒▒▒▒▒\n" +
                "▓  ▓▓▓▓▓▓▓▓  ▓▓▓▓  ▓▓  ▓▓▓▓  ▓▓      ▓▓▓▓▓▓    ▓▓▓▓▓▓▓▓▓▓  ▓  ▓  ▓▓  ▓▓▓▓  ▓▓▓▓▓  ▓▓▓▓▓  ▓▓▓▓  ▓▓       ▓▓▓  ▓▓▓▓  ▓▓  ▓▓▓▓▓▓▓▓▓▓▓  ▓▓▓▓▓▓      ▓▓\n" +
                "█  ████  ██  ████  ██  ████  ██  █████████  ██  █████████  ██    ██        █████  █████  ████  ██  ███  ███        ██  ███████████  ███████████  █\n" +
                "██      ████      ███       ███        ██  ████  ████████  ███   ██  ████  █████  ██████      ███  ████  ██  ████  ██        ██        ███      ██\n" +
                "██████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████";

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

    //----------------------------------------------------------------------------------------------PRIVATE METHODS

    /**
     * method that transforms a Symbol into a String representing that Symbol's color
     * @param s the Symbol to translate
     * @return the color of the given Symbol
     */
    private String getKingdomColor(Symbol s){
        switch (s){
            case ANIMAL -> {
                return "blue";
            }
            case FUNGI -> {
                return "red";
            }
            case PLANT -> {
                return "green";
            }
            case INSECT -> {
                return "purple";
            }
            default -> {
                return null;
            }
        }
    }
}
/*
╔═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗
║                                                                                                                       ║
║    ▄ ▄ ▄ ▄ ▄ ▄ ▄ ▄ ▄ ▄ ▄ ▄ ▄ ▄ ▄ ▄ ▄ ▄ ▄ ▄    PlayerNickname1   PlayerNickname2   PlayerNickname3   PlayerNickname4   ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   red xxPTS ▀▄▀     blue xxPTS ▀▄▀    green xxPTS ▀▄▀   yellow xxPTS ▀▄▀  ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀                                                                           ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀                                                                           ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   Shared Objective 1: xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx   ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   Shared Objective 2: xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx   ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   Personal Objective: xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx   ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀                                                                           ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   Gold     Resource     ┌───────────────────────────────────────────┐     ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   Deck:    Deck:        │                                           │     ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   ╔══════╗ ┌──────┐     │                                           │     ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   ║      ║ │      │     │                                           │     ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   ╚══════╝ └──────┘     │                                           │     ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀                         │                                           │     ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   Face Up Cards:        │                                           │     ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   ╔══════╗ ┌──────┐     │                                           │     ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   ║      ║ │      │     │                                           │     ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   ╚══════╝ └──────┘     │                                           │     ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   ╔══════╗ ┌──────┐     │                                           │     ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   ║      ║ │      │     │                                           │     ║
║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   ╚══════╝ └──────┘     └───────────────────────────────────────────┘     ║
║                                                                                                                       ║
╟──ChatBox──────────────────────────────────────────────────────────────────────────────────────────────────────────────╢
║Mattix124: cacca                                                                                                       ║
║Tommy(whispers): mattea è proprio scarso                                                                               ║
║Beppe: ez win                                                                                                          ║
║You: brb!                                                                                                              ║
║...                                                                                                                    ║
║                                                                                                                       ║
╚═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝

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
┌───────────┐
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
┌─────────────────────────────────────────┐
│                                         │
│                                         │
│                                         │
│                                         │
│                                         │
│                                         │
│                                         │
│                                         │
│                                         │
└─────────────────────────────────────────┘
ඩ ඞ
*/
