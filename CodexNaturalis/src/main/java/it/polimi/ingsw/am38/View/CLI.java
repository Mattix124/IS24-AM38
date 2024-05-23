package it.polimi.ingsw.am38.View;

import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Enum.Orientation;
import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Board.Coords;
import it.polimi.ingsw.am38.Model.Board.Field;
import it.polimi.ingsw.am38.Model.Cards.*;
import it.polimi.ingsw.am38.Model.Player;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static it.polimi.ingsw.am38.Enum.Orientation.*;

public class CLI implements Viewable{
    private final String emptyLine = "║                                                                                                                       ║\n";
    private final ArrayList<String> gameScreen = new ArrayList<>(24);
    private final char[][] p1GameField = new char[21][41];
    private final char[][] p2GameField = new char[21][41];
    private final char[][] p3GameField = new char[21][41];
    private final char[][] p4GameField = new char[21][41];
    private int yShift, xShift, yCenter, xCenter;
    private final LinkedList<String> cardDisplay = new LinkedList<>();
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
║ xx↗▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀  PColor xxPTS ███  PColor xxPTS ███  PColor xxPTS ███  PColor xxPTS ███  ║
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
        gameScreen.addFirst("╔═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
        gameScreen.add(1, emptyLine);
    }

    //--------------------------------------------------------------------------------------------DisplayPrint

    /**
     * method used to print the entire game screen
     */
    public void printScreen(){
        gameScreen.forEach(System.out::println);
    }

    private void updateGameScreen(){

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

    //-----------------------------------------------------------------------------------------------SetUpPrints
/*
 Nicknamexxxxxxx(███) Nicknamexxxxxxx(███) Nicknamexxxxxxx(███) Nicknamexxxxxxx(███) //colorati del loro colore
   ┌───────────┐        ┌───────────┐        ┌───────────┐        ┌───────────┐
   │⚘    1    ଫ│        │⚘    1    ଫ│        │⚘    1    ଫ│        │⚘    1    ଫ│
   │⚲         ⛶│        │⚲         ⛶│        │⚲         ⛶│        │⚲         ⛶│
   └───────────┘        └───────────┘        └───────────┘        └───────────┘
 Your Hand:
xxxxx
 Shared objectives:
xxxxx
 Choose your personal objective:
xxxxx
*/
    /**
     * prints all infos a Player has during his Personal Objective Choice
     * @param pc all Players nicknames and colors
     * @param hcc each Player's Hand Cards Colors
     * @param psc each Player's StarterCard (only the facing chosen by the owner of the StarterCard will be shown)
     * @param ownHand this Player's Cards in Hand
     * @param sharedObj1 the first Objective shared by all Players
     * @param sharedObj2 the second Objective shared by all Players
     * @param objChoice1 first Objective this Player can choose from
     * @param objChoice2 second Objective this Player can choose from
     */
    public void postFacingSelectionPrint(HashMap<String, Color> pc, HashMap<String, Symbol[]> hcc, HashMap<String, StarterCard> psc, LinkedList<PlayableCard> ownHand, ObjectiveCard sharedObj1, ObjectiveCard sharedObj2, ObjectiveCard objChoice1, ObjectiveCard objChoice2){
        pc.forEach((k, v) -> System.out.print(" " + "(" + getHandColors(hcc.get(k)) + ")" + colorPlayer(getNick(k), v)));//print colored names
        System.out.println();
        LinkedList<LinkedList<String>> cards = new LinkedList<>();
        psc.forEach((k, v) -> cards.add(getCard(psc.get(k))));//saves starting cards (with the right facing) as Lists of Strings
        for(int i = 0; i < 4; i++){
            cards.forEach(x -> System.out.print("   " + x.removeFirst() + "        "));//prints all starter cards, layer by layer
            System.out.println();
        }
        System.out.println("Cards in your hand:");
        LinkedList<LinkedList<String>> hand = new LinkedList<>();
        ownHand.forEach(card -> {
            if (card.getCardID() < 41)
                hand.add(colorCard(getCard((ResourceCard) card), card.getKingdom()));
            else
                hand.add(colorCard(getCard((GoldCard) card), card.getKingdom()));
        });
        for(int i = 0 ; i < 4 ; i++)
                System.out.println("   " + hand.get(0).get(i) + "        "+ hand.get(1).get(i) + "        "+ hand.get(2).get(i));
        System.out.println();
        printSharedObjectives(sharedObj1.getDescription(), sharedObj2.getDescription());
        printObjectiveChoice(objChoice1.getDescription(), objChoice2.getDescription());
    }

    //-------------------------------------------------------------------------------------------------Objectives

    private void printSharedObjectives(String sharedObj1, String sharedObj2){//, , LinkedList<String> nicknames){
        setSharedObjectives(sharedObj1, sharedObj2);
        System.out.println(" Shared objectives:\n" + getSharedObj1() + "\n" + getSharedObj2() + "\n");
    }

    private void setSharedObjectives(String obj1, String obj2){
        this.sharedObj1 = "1) " + String.format("%-68s", obj1);
        this.sharedObj2 = "2) " + String.format("%-68s", obj2);
    }

    private String getSharedObj1(){
        return sharedObj1;
    }

    private String getSharedObj2(){
        return sharedObj2;
    }

    private void printObjectiveChoice(String objChoice1, String objChoice2){
        System.out.println(" Choose your personal objective:\n1) " + objChoice1 + "\n2) " + objChoice2 + "\n");
    }

    public void setPersonalObjective(String objective){
        this.personalObj = "P) " + String.format("%-68s", objective);
        System.out.println("You chose:\n P) " + this.personalObj + "\n");
    }

    //-------------------------------------------------------------------------------------------StarterCardFacingChoice
/*
 Gold)      Decks:              Face Up Cards:
        ╔═══════════╗   ╔═══════════╗ ╔═══════════╗
        ║           ║   ║           ║ ║           ║
        ║           ║   ║           ║ ║           ║
        ╚═══════════╝   ╚═══════════╝ ╚═══════════╝
 Resource)
        ┌───────────┐   ┌───────────┐ ┌───────────┐
        │           │   │           │ │           │
        │           │   │           │ │           │
        └───────────┘   └───────────┘ └───────────┘
 */
    /**
     * method that prints both faces of a StarterCard to show to the Player so that they can choose
     * @param sc the StarterCard drawn by the Player
     */
    public void printStarterCardChoice(StarterCard sc, Symbol gt, Symbol rt, GoldCard g1, GoldCard g2, ResourceCard r1, ResourceCard r2){
        setTopOfGDeck(gt); setGround(g1, 1); setGround(g2, 2);
        setTopOfRDeck(rt); setGround(r1, 1); setGround(r2, 2);
        System.out.println(" Gold)      Decks:              Face Up Cards:");
        for(int i = 0 ; i < 4 ; i++)
            System.out.println("        " + topOfGDeck.get(i) + "   " + goldGround1.get(i) + " " + goldGround2.get(i));
        System.out.println(" Resource)");
        for(int i = 0 ; i < 4 ; i++)
            System.out.println("        " + topOfRDeck.get(i) + "   " + resourceGround1.get(i) + " " + resourceGround2.get(i));
        sc.setFace(true);
        LinkedList<String> front = getCard(sc);
        sc.setFace(false);
        LinkedList<String> back = getCard(sc);
        System.out.println("Choose a face for your card: type face and orientation: (up or down)\n" +
                "Face-up:        Face-down:\n" +
                front.getFirst() + "   " + back.getFirst() +"\n" +
                front.get(1) + "   " + back.get(1) + "\n" +
                front.get(2) + "   " + back.get(2) + "\n" +
                front.get(3) + "   " + back.get(3));
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

    private String getHandColors(Symbol[] hand){//distinction between gold and resource cards wip
        return colorString(hand[0], "█") + colorString(hand[1], "█") + colorString(hand[2], "█");
    }

    private void setToEndgame(){
       gameScreen.add(1, "║ ENDGAME STARTED ! - ENDGAME STARTED ! - ENDGAME STARTED ! - ENDGAME STARTED ! - ENDGAME STARTED ! - ENDGAME STARTED ! ║");
    }

    //-------------------------------------------------------------------------------------------Card Building

    private LinkedList<String> getCard(GoldCard c){
        LinkedList<String> card = new LinkedList<>();
        card.add(0, "\u001B[30m╔═══════════╗\u001B[0m");
        if(c.getFace()) {
            card.add(1, "\u001B[30m║" + getSymbol(c, NW) + "   " + getPointsCondition(c) + "   " + getSymbol(c, NE) + "║\u001B[0m");
            card.add(2, "\u001B[30m║" + getSymbol(c, SW) + "  " + getPlacementCondition(c) + "  " + getSymbol(c, SE) + "║\u001B[0m");
        }else{
            card.add(1, "\u001B[30m║⛶         ⛶║\u001B[0m");
            card.add(2, "\u001B[30m║⛶         ⛶║\u001B[0m");
        }
        card.add(3, "\u001B[30m╚═══════════╝\u001B[0m");
        return card;
    }

    private LinkedList<String> getCard(ResourceCard c){
        LinkedList<String> card = new LinkedList<>();
        card.add(0, "\u001B[30m┌───────────┐\u001B[0m");
        if(c.getFace()){
            card.add(1, "\u001B[30m│" + getSymbol(c, NW) +"   " + getPointsCondition(c) + "   " + getSymbol(c, NE) +"│\u001B[0m");
            card.add(2, "\u001B[30m│" + getSymbol(c, SW) +"         " + getSymbol(c, SE) +"│\u001B[0m");
        }else{
            card.add(1, "\u001B[30m│⛶         ⛶│\u001B[0m");
            card.add(2, "\u001B[30m│⛶         ⛶│\u001B[0m");
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

    private LinkedList<String> colorCard(LinkedList<String> card, Symbol kingdom){
        return card.stream().map(s -> colorBackgroundString(kingdom, s)).collect(Collectors.toCollection(LinkedList::new));
    }
    //-------------------------------------------------------------------------------------------CardDisplay

    private void setCardDisplay(PlayableCard card){
        LinkedList<String> c = new LinkedList<>();
        if(card.getCardID() < 41)
            c.addAll(colorCard(getCard((ResourceCard) card), card.getKingdom()));
        else if(card.getCardID() < 81)
            c.addAll(colorCard(getCard((GoldCard)card), card.getKingdom()));
        else
            c.addAll(colorCard(getCard((StarterCard) card), card.getKingdom()));
        cardDisplay.add(0, "┌──Card─Display───┐");
        cardDisplay.add(1, "│                 │");
        cardDisplay.add(2, "│  " + c.getFirst() + "  │");
        cardDisplay.add(3, "│  " + c.get(1) + "  │");
        cardDisplay.add(4, "│  " + c.get(2) + "  │");
        cardDisplay.add(5, "│  " + c.get(3) + "  │");
        cardDisplay.add(6, "│                 │");
        cardDisplay.add(7, "└─────────────────┘");
    }

    //-------------------------------------------------------------------------------------------TopOfDecksAndGrounds

    public void setTopOfGDeck(Symbol color){
        topOfGDeck.add(0, colorBackgroundString(color, "\u001B[30m╔═══════════╗"));
        topOfGDeck.add(1, colorBackgroundString(color, "\u001B[30m║           ║"));
        topOfGDeck.add(2, colorBackgroundString(color, "\u001B[30m║           ║"));
        topOfGDeck.add(3, colorBackgroundString(color, "\u001B[30m╚═══════════╝"));
    }

    public void setTopOfRDeck(Symbol color){
        topOfRDeck.add(0, colorBackgroundString(color, "\u001B[30m┌───────────┐"));
        topOfRDeck.add(1, colorBackgroundString(color, "\u001B[30m│           │"));
        topOfRDeck.add(2, colorBackgroundString(color, "\u001B[30m│           │"));
        topOfRDeck.add(3, colorBackgroundString(color, "\u001B[30m└───────────┘"));
    }

    public void setGround(ResourceCard c, int i){
        if(i == 1)
            resourceGround1 = colorCard(getCard(c), c.getKingdom());
        else if(i == 2)
            resourceGround2 = colorCard(getCard(c), c.getKingdom());
    }

    public void setGround(GoldCard c, int i){
        if(i == 1)
            goldGround1 = colorCard(getCard(c), c.getKingdom());
        else if(i == 2)
            goldGround2 = colorCard(getCard(c), c.getKingdom());
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
            case PLANT -> "\u001B[42m" + s + "\u001B[0m";
            case INSECT -> "\u001B[45m" + s + "\u001B[0m";
            default -> "\u001B[47m" + s + "\u001B[0m";
        };
    }

    private String colorPlayer(String s, Color color) {
        return switch (color) {
            case RED -> "\u001B[31m" + s + "\u001B[0m";
            case YELLOW -> "\u001B[33m" + s + "\u001B[0m";
            case BLUE -> "\u001B[34m" + s + "\u001B[0m";
            case GREEN -> "\u001B[32m" + s + "\u001B[0m";
            default -> "";
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
        return "║" + String.format("%-119s", message) + "║";
    }

    private void printChat(){
        String chatLine = "╟──ChatBox──────────────────────────────────────────────────────────────────────────────────────────────────────────────╢";
        System.out.println(chatLine);
        for (String s : chat) {
            System.out.println(s);
        }
        String endOfScreen = "╚═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝";
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

        /*
        String gameTitle2 = "\u001B[31m░░░░░░░░░░\u001B[32m░░░░░░░░░░\u001B[34m░░░░░░░░░░\u001B[35m░░░░░░░░░░\u001B[31m░░░░░░░░░░\u001B[32m░░░░░░\u001B[34m░░░░░░░░░░\u001B[35m░░░░░░░░░░\u001B[31m░░░░░░░░░░\u001B[32m░░░░░░░░░░\u001B[34m░░░░░░░░░░\u001B[35m░░░░░░░░░░\u001B[31m░░░░░░░░░░\u001B[32m░░░░░░░░░░\u001B[34m░░░░░░░░░░\u001B[0m\n" +
                "\u001B[31m░░      ░░\u001B[32m░░      ░░\u001B[34m░       ░░\u001B[35m░        ░\u001B[31m░  ░░░░  ░\u001B[32m░░░░░░\u001B[34m░   ░░░  ░\u001B[35m░░      ░░\u001B[31m░        ░\u001B[32m░  ░░░░  ░\u001B[34m░       ░░\u001B[35m░░      ░░\u001B[31m░  ░░░░░░░\u001B[32m░        ░\u001B[34m░░      ░░\u001B[0m\n" +
                "\u001B[31m▒  ▒▒▒▒  ▒\u001B[32m▒  ▒▒▒▒  ▒\u001B[34m▒  ▒▒▒▒  ▒\u001B[35m▒  ▒▒▒▒▒▒▒\u001B[31m▒▒  ▒▒  ▒▒\u001B[32m▒▒▒▒▒▒\u001B[34m▒    ▒▒  ▒\u001B[35m▒  ▒▒▒▒  ▒\u001B[31m▒▒▒▒  ▒▒▒▒\u001B[32m▒  ▒▒▒▒  ▒\u001B[34m▒  ▒▒▒▒  ▒\u001B[35m▒  ▒▒▒▒  ▒\u001B[31m▒  ▒▒▒▒▒▒▒\u001B[32m▒▒▒▒  ▒▒▒▒\u001B[34m▒  ▒▒▒▒▒▒▒\u001B[0m\n" +
                "\u001B[31m▓  ▓▓▓▓▓▓▓\u001B[32m▓  ▓▓▓▓  ▓\u001B[34m▓  ▓▓▓▓  ▓\u001B[35m▓      ▓▓▓\u001B[31m▓▓▓    ▓▓▓\u001B[32m▓▓▓▓▓▓\u001B[34m▓  ▓  ▓  ▓\u001B[35m▓  ▓▓▓▓  ▓\u001B[31m▓▓▓▓  ▓▓▓▓\u001B[32m▓  ▓▓▓▓  ▓\u001B[34m▓       ▓▓\u001B[35m▓  ▓▓▓▓  ▓\u001B[31m▓  ▓▓▓▓▓▓▓\u001B[32m▓▓▓▓  ▓▓▓▓\u001B[34m▓▓      ▓▓\u001B[0m\n" +
                "\u001B[31m█  ████  █\u001B[32m█  ████  █\u001B[34m█  ████  █\u001B[35m█  ███████\u001B[31m██  ██  ██\u001B[32m██████\u001B[34m█  ██    █\u001B[35m█        █\u001B[31m████  ████\u001B[32m█  ████  █\u001B[34m█  ███  ██\u001B[35m█        █\u001B[31m█  ███████\u001B[32m████  ████\u001B[34m███████  █\u001B[0m\n" +
                "\u001B[31m██      ██\u001B[32m██      ██\u001B[34m█       ██\u001B[35m█        █\u001B[31m█  ████  █\u001B[32m██████\u001B[34m█  ███   █\u001B[35m█  ████  █\u001B[31m████  ████\u001B[32m██      ██\u001B[34m█  ████  █\u001B[35m█  ████  █\u001B[31m█        █\u001B[32m█        █\u001B[34m██      ██\u001B[0m\n" +
                "\u001B[31m██████████\u001B[32m██████████\u001B[34m██████████\u001B[35m██████████\u001B[31m██████████\u001B[32m██████\u001B[34m██████████\u001B[35m██████████\u001B[31m██████████\u001B[32m██████████\u001B[34m██████████\u001B[35m██████████\u001B[31m██████████\u001B[32m██████████\u001B[34m██████████\u001B[0m";
        */
        System.out.println(gameTitle1);
        //System.out.println(gameTitle2);
        }

    //-------------------------------------------------------------------------------------------CoordinatesConversion

    /**
     * method used to get the value of x on the real game field from the x and y of the model's data structure
     * @param sdx coordinate of a position for a card on the data structure
     * @param sdy coordinate of a position for a card on the data structure
     * @return the respective x value on the shown game field
     */
    private int getStraightX(int sdx, int sdy){
        return sdx-sdy;
    }

    /**
     * method used to get the value of y on the real game field from the x and y of the model's data structure
     * @param sdx coordinate of a position for a card on the data structure
     * @param sdy coordinate of a position for a card on the data structure
     * @return the respective y value on the shown game field
     */
    private int getStraightY(int sdx, int sdy){
        return sdx + sdy;
    }

    /**
     * method used to get the line in which to write a char, given the coordinate y given
     * @param y coordinate of the game field given
     * @return the line in which to write
     */
    private int yToLine(int y){
        if(y<0)
            return 10 + (Math.abs(y)/2);
        else
            return 10 - ((y+1)/2);
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
        int lowerBound = Collections.min(cd);
        cd = pF.getSortedVector().stream()
                .map(c -> c.coordinates().x() - c.coordinates().y())
                .toList();
        int leftBound = Collections.min(cd);
        this.yCenter = (upperBound + lowerBound)/2;
        this.xCenter = (rightBound + leftBound)/2;
        if(upperBound > 20 || lowerBound > 20)
            this.yShift = Math.max(upperBound, lowerBound) - 20;
        if(rightBound > 20 || leftBound > 20)
            this.xShift = Math.max(rightBound, leftBound) - 20;
    }

    /*public void printGameFieldCheck(HashMap<Coords, int>){

    }*/
}
/*
╔═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗
║ ENDGAME STARTED ! - ENDGAME STARTED ! - ENDGAME STARTED ! - ENDGAME STARTED ! - ENDGAME STARTED ! - ENDGAME STARTED ! ║
║  xx↓                 xx↓                 xx↓  PlayerNickname1   PlayerNickname2   PlayerNickname3   PlayerNickname4   ║
║ xx↗▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀  PColor xxPTS ███  PColor xxPTS ███  PColor xxPTS ███  PColor xxPTS ███  ║
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
