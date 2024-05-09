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
    private final ArrayList<String> chat = new ArrayList<>();
    private final ArrayList<String> gameScreen = new ArrayList<>(32);
    private final String[][] gameField = new String[21][41];//?
    private final HashMap<String, String> playersScores = new HashMap<>();

    //info stored and needed
    private Field p1F, p2F, p3F, p4F;
    private int numOfPlayers;
    private int gameID;

    /**
     * Constructor method for this class
     */
    public CLI(){
    }

    /**
     * method used to get the color of a PlayableCard present on the shownGameField
     * @param x coordinate
     * @param y coordinate
     * @return the color of that card (String)
     */
    public String getCardKingdomColor(int x, int y){
        PlayableCard c = shownGameField.getCardFromCoordinate(x, y);
        if(c == null)
            return " ";
        Symbol s = c.getKingdom();
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

    public String makePixel(String s){
        switch (s){
            case "blue" -> {
                return "\u001B[34m ";
            }
            case "red" -> {
                return "\u001B[31m ";
            }
            case "green" -> {
                return "\u001B[42m ";
            }
            case "purple" -> {
                return "\u001B[45m ";
            }
            default -> {
                return " ";
            }
        }
    }

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
        String test1 = "\u001B[33m┌───────────┐\n" +
                "│ଫ    5     │\n" +
                "│♘  ⚘⚘⚘⚘⚘ ┌─┴─────────┐\n" +
                "└─────────┤⚲   1|✉    │\n" +
                "┌─────────┼─┐  ⚘⚘⚘    ├─────────┐\n" +
                "│⍾        └─┼───────┬─┘         │\n" +
                "│          x│       │           │\n" +
                "└───────────┘       └───────────┘\u001B[33m";
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
                .map(c -> -(c.coordinates().x()) + c.coordinates().y())
                .toList();
        int leftBound = Collections.max(cd);
        cd = pF.getSortedVector().stream()
                .map(c -> c.coordinates().x() - c.coordinates().y())
                .toList();
        int lowerBound = -Collections.min(cd);
        cd = pF.getSortedVector().stream()
                .map(c -> -(c.coordinates().x()) + c.coordinates().y())
                .toList();
        int rightBound = -Collections.min(cd);
        int height = upperBound + lowerBound +1;
        int width = rightBound + leftBound +1;
    }
}

//╔═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗
//║                                                                                                                       ║
//║ 10 ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀ 41x41(41 char x 21 righe)senza contare caso in cui un player salti turni ║
//║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀                                                                          ║
//║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀ da 1x1 a max 21x21 se facciamo che si espande con il piazzamento         ║
//║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀                                                                          ║
//║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀                                                                          ║
//║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀                                                                          ║
//║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀                                                                          ║
//║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀                                                                          ║
//║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀                                                                          ║
//║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀                                                                          ║
//║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀                                                                          ║
//║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀                                                                          ║
//║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀                                                                          ║
//║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀                                                                          ║
//║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀                                                                          ║
//║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀                                                                          ║
//║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀                                                                          ║
//║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀                                                                          ║
//║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀                                                                          ║
//║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀                                                                          ║
//║-10 ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀ ▀                                                                          ║
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

