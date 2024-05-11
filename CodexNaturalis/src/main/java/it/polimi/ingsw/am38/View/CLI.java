package it.polimi.ingsw.am38.View;

import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Board.CardData;
import it.polimi.ingsw.am38.Model.Board.Field;
import it.polimi.ingsw.am38.Model.Cards.PlayableCard;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

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
    private final String[][] p1GameField = new String[21][41];
    private final String[][] p2GameField = new String[21][41];
    private final String[][] p3GameField = new String[21][41];
    private final String[][] p4GameField = new String[21][41];
    private final HashMap<String, String> playersScores = new HashMap<>();

    //info stored and needed
    private Field p1F, p2F, p3F, p4F;
    private int numOfPlayers;
    private int gameID;
    private final String firstLine = "╔═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗";
    private final String chatLine = "╟──ChatBox──────────────────────────────────────────────────────────────────────────────────────────────────────────────╢";
    private final String lastLine = "╚═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝";
    private final String emptyLine = "║                                                                                                                       ║";
    /**
     * Constructor method for this class
     */
    public CLI(){
        gameScreen.addFirst(firstLine);
        gameScreen.add(25, chatLine);
        gameScreen.add(26, emptyLine);
        gameScreen.add(27, emptyLine);
        gameScreen.add(28, emptyLine);
        gameScreen.add(29, emptyLine);
        gameScreen.add(30, emptyLine);
        gameScreen.addLast(lastLine);
    }

    //-------------------------------------------------------------------------------------------CoordinatesConversion

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
        String test1 = "\u001B[43m┌───────────┐\n" +
                "│ଫ    5     │\n" +
                "│♘  ⚘⚘⚘⚘⚘ ┌─┴─────────┐\n" +
                "└─────────┤⚲   1|✉    │\n" +
                "┌─────────┼─┐  ⚘⚘⚘    ├─────────┐\n" +
                "│⍾        └─┼───────┬─┘         │\n" +
                "│          x│       │           │\n" +
                "└───────────┘       └───────────┘";
        System.out.println(gameTitle1);
        System.out.println(gameTitle2);
        System.out.println(test1);
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

//╔═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗
//║                                                                                                                       ║
//║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   PlayerNickname1   PlayerNickname2   PlayerNickname3   PlayerNickname4   ║
//║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   red xxPTS ▀▄▀     blue xxPTS ▀▄▀    green xxPTS ▀▄▀   yellow xxPTS ▀▄▀  ║
//║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀                                                                           ║
//║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   Shared Objective 1: xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx   ║
//║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   Shared Objective 2: xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx   ║
//║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   Personal Objective: xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx   ║
//║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀                                                                           ║
//║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   Gold     Resource                                                       ║
//║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   Deck:    Deck:                                                          ║
//║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   ╔══════╗ ┌──────┐                                                       ║
//║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   ║      ║ │      │                                                       ║
//║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   ╚══════╝ └──────┘                                                       ║
//║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   Face Up Cards:                                                          ║
//║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   ╔══════╗ ┌──────┐                                                       ║
//║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   ║      ║ │      │                                                       ║
//║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   ╚══════╝ └──────┘                                                       ║
//║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   ╔══════╗ ┌──────┐                                                       ║
//║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   ║      ║ │      │                                                       ║
//║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀   ╚══════╝ └──────┘                                                       ║
//║   ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀                                                                           ║
//║   ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀                                                                           ║
//║                                                                                                                       ║
//║                                                                                                                       ║
//╟──ChatBox──────────────────────────────────────────────────────────────────────────────────────────────────────────────╢
//║Mattix124: cacca                                                                                                       ║
//║Tommy(whispers): mattea è proprio scarso                                                                               ║
//║Beppe: ez win                                                                                                          ║
//║You: brb!                                                                                                              ║
//║...                                                                                                                    ║
//╚═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝


// ─ │ ┌ ┐ └ ┘ ├ ┤ ┬ ┴ ┼
// ═  ║  ╒	╓	╔	╕	╖	╗	╘	╙	╚	╛	╜	╝	╞	╟
// ╠  ╡	 ╢	╣	╤	╥	╦	╧	╨	╩	╪	╫	╬
// 13x4
// ┌───────────┐ ┌─┼───────┼─┐ ┌───────────┐ ┌───────────┐ ┌───────────┐
// │           │ ┼           ┼ │          ✗│ │⊟         ⊡│ │           │
// │           │ ┼─┐        x┼ │o         x│ │          ⎕│ │           │
// └───────────┘ └─┼───────┼─┘ └───────────┘ └───────────┘ └───────────┘
// ╔═╪═══════╪═╗
// ╫           ╫
// ╫─┐         ╫
// ╚═╪═══════╪═╝
//
// ╔══════╗ ┌──────┐
// ║      ║ │      │
// ╚══════╝ └──────┘
//
// resources: ⚘ ଫ ⍾ ♘ oppure P B F A
// items: ⚲ ✉ ⛫ oppure Q M I
// (⛶ empty corner != non-existing corner)
// placing conditions: da 3 a 5 kingdoms " *** ", "**** ", "*****"
// points conditions: (corner covered:) "2|▘", (items shown:) "1|*", (flat points:) " 3 ",
// ⚘ ଫ ⍾ ♘ ⚲ ✉ ⛫
// ┌───────────┐
// │ଫ    5     │
// │♘  ⚘⚘⚘⚘⚘ ╔═╪═══════╪═╗
// └─────────╫⚲   1|✉    ╫
// ┌─────────╫─┐  ⚘⚘⚘    ╫─────────┐
// │⍾        ╚═╪═══════╪═╝         │
// │          x│       │           │
// └───────────┘       └───────────┘
// ┌───────────┐
// │B    5     │
// │A  PPPPP ╔═══════════╗
// └─────────║Q   1|M    ║
// ┌─────────║    PPP    ║─────────┐
// │F        ╚═══════════╝         │
// │⎕         x│       │           │
// └───────────┘       └───────────┘
// sfondo carte colorato per rappresentre il kingdom (red, green, blue, purple)
//
//
// ┌───────────────┐
// │⍾     2|▘     ⛶│
// │               │
// │     ⚘⚘⚘⍾     ⍾│
// └───────────────┘
//
// ඩ ඞ

