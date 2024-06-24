package it.polimi.ingsw.am38.View;

import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Enum.Orientation;
import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Board.VisibleElements;
import it.polimi.ingsw.am38.Model.Cards.*;
import it.polimi.ingsw.am38.Network.Client.ClientCommandInterpreter;
import it.polimi.ingsw.am38.Network.Client.ClientWriter;

import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.ingsw.am38.Enum.Orientation.*;
import static it.polimi.ingsw.am38.Enum.Symbol.*;

public class CLI implements Viewable
{
    private final String emptyLine = "║                                                                                                                       ║";
    private final ArrayList<String> gameScreen = new ArrayList<>(24);
    private final HashMap<String, String[][]>  gameFields = new HashMap<>();
    private final LinkedList<String> cardDisplay = new LinkedList<>();
    private final ArrayList<String> chat = new ArrayList<>(6);
    private LinkedList<String> goldGround1 = new LinkedList<>();
    private LinkedList<String> goldGround2 = new LinkedList<>();
    private LinkedList<String> resourceGround1 = new LinkedList<>();
    private LinkedList<String> resourceGround2 = new LinkedList<>();
    private final LinkedList<String> topOfGDeck = new LinkedList<>();
    private final LinkedList<String> topOfRDeck = new LinkedList<>();
    private String sharedObj1, sharedObj2, personalObj;
    private final HashMap<String, LinkedList<String> > symbolsTabs = new HashMap<>();
    private final HashMap<String, HashMap<String, Integer>> playersFieldsLimits = new HashMap<>();
    private final ArrayList<String> coloredNicks = new ArrayList<>(4);
    private final HashMap<String, Symbol[]> handColors = new HashMap<>();
    private final HashMap<String, String> scores = new HashMap<>();
    ArrayList<LinkedList<String>> ownStringHand = new ArrayList<>();
    private String currentlyViewedPlayerNick;
    private int lateralShift = 40;
    private int heightShift = 40;
    private String nickname;


    /**
     * Constructor method for this class
     */
    public CLI(){
        initializeChat();
        gameScreen.add(0, "╔═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
        gameScreen.add(1, emptyLine);
        initializeCardDisplay();
        initializeTopOfGDeck();
        initializeTopOfRDeck();
    }

    //-----------------------------------------------------------------------------------------------SetUpPrints

    private void initializeFieldsArray(){

    }

    private void initializeLimits(String nick){
        HashMap<String, Integer> m = new HashMap<>();
        m.put("up", 0);
        m.put("down", 0);
        m.put("right", 0);
        m.put("left", 0);
        playersFieldsLimits.put(nick, m);
    }

    //-------------------------------------------------------------------------------------------------CardsInHandStrings

    private void setCardInHand(int n, PlayableCard c){
        if(c.getCardID() == 0)
            ownStringHand.set(n, emptyCard());
        else if (c.getCardID() < 41)
            ownStringHand.set(n, colorCard(getCard((ResourceCard) c), c.getKingdom()));
        else
            ownStringHand.set(n, colorCard(getCard((GoldCard) c), c.getKingdom()));

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

    //------------------------------------------------------------------------------------NamesColorsScoresHandsEndGame

    private String getNick(String nickname){
        return String.format("%-15s", nickname) + "   ";
    }

    private String nicksLine(){
        StringBuilder sBuilder = new StringBuilder();
        for (String nick : coloredNicks)
            sBuilder.append(nick);
        sBuilder.append("               ".repeat(Math.max(0, 4 - coloredNicks.size())));
        return sBuilder.toString();
    }

    private String getPointsAndHandColors(){
        StringBuilder sBuilder = new StringBuilder();
        scores.forEach((k, v) -> sBuilder.append("   ").append(scores.get(k)).append(" ").append(getHandColors(k)).append("      "));
        sBuilder.append("                  ".repeat(Math.max(0, 4 - coloredNicks.size())));
        return sBuilder.toString();
    }

    private void initializeScore(String nick){
        scores.put(nick, " 0PTS");
    }

    private void setPoints(String nick, int pts){
        if(pts<10)
            scores.put(nick, " " + pts + "PTS");
        else if(pts < 20)
            scores.put(nick, pts + "PTS");
        else {
            setToEndgame();
            scores.put(nick, "\u001B[33m" + pts + "PTS\u001B[0m");
        }
    }

    private void setHandColors(String nick, Symbol[] colors){
        handColors.put(nick, colors);
    }

    private String getHandColors(String nick){//distinction between gold and resource cards wip
        return colorString(handColors.get(nick)[0], "█") + colorString(handColors.get(nick)[1], "█") + colorString(handColors.get(nick)[2], "█");
    }

    private void setToEndgame(){
       gameScreen.set(1, "║ ENDGAME STARTED ! - ENDGAME STARTED ! - ENDGAME STARTED ! - ENDGAME STARTED ! - ENDGAME STARTED ! - ENDGAME STARTED ! ║");
    }

    //-------------------------------------------------------------------------------------------Card Building

    private LinkedList<String> getCard(GoldCard c){
        LinkedList<String> card = new LinkedList<>();
        card.add(0, "\u001B[30m╔═══════════╗\u001B[0m");
        if(c.getFace()) {
            card.add(1, "\u001B[30m║" + getSymbol(c, NW) + "   " + getPointsCondition(c) + "   " + getSymbol(c, NE) + "║\u001B[0m");
            card.add(2, "\u001B[30m║" + getSymbol(c, SW) + "  " + getPlacementCondition(c) + "  " + getSymbol(c, SE) + "║\u001B[0m");
        }else{
            card.add(1, "\u001B[30m║0         0║\u001B[0m");
            card.add(2, "\u001B[30m║0         0║\u001B[0m");
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
            card.add(1, "\u001B[30m│0         0│\u001B[0m");
            card.add(2, "\u001B[30m│0         0│\u001B[0m");
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

    private LinkedList<String> emptyCard(){
        LinkedList<String> emptyCard = new LinkedList<>();
        emptyCard.add(0, "             ");
        emptyCard.add(1, "             ");
        emptyCard.add(2, "             ");
        emptyCard.add(3, "             ");
        return emptyCard;
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
        if(sy[4] != null)
            return getSymbolChar(sy[0]) + getSymbolChar(sy[1]) + getSymbolChar(sy[2]) + getSymbolChar(sy[3]) + getSymbolChar(sy[4]);
        if(sy[3] != null)
            return getSymbolChar(sy[0]) + getSymbolChar(sy[1]) + getSymbolChar(sy[2]) + getSymbolChar(sy[3]) + " ";
        return " " + getSymbolChar(sy[0]) + getSymbolChar(sy[1]) + getSymbolChar(sy[2]) + " ";
    }

    private String getSymbolChar(Symbol s){
        if(s == null)
            return " ";
        return switch (s) {
            case INKWELL -> "I";
            case MANUSCRIPT -> "M";
            case QUILL -> "Q";
            case FUNGI -> "F";
            case PLANT -> "P";
            case ANIMAL -> "A";
            case INSECT -> "B";
            case CORNER -> "▘";
            case NULL -> "0";
        };
    }

    private LinkedList<String> colorCard(LinkedList<String> card, Symbol kingdom){
        return card.stream().map(s -> colorBackgroundString(kingdom, s)).collect(Collectors.toCollection(LinkedList::new));
    }
    //-------------------------------------------------------------------------------------------CardDisplayAndSymbols

    private void initializeCardDisplay(){
        cardDisplay.add(0, "│                 │");
        cardDisplay.add(1, "│                 │");
        cardDisplay.add(2, "│                 │");
        cardDisplay.add(3, "│                 │");
        cardDisplay.add(4, "│                 │");
    }

    private void initializeSymbolsTabs(){
        LinkedList<String> symTab = new LinkedList<>();
        symTab.add(0, "│    P : 00    │");
        symTab.add(1, "│    B : 00    │");
        symTab.add(2, "│    F : 00    │");
        symTab.add(3, "│    A : 00    │");
        symTab.add(4, "│    Q : 00    │");
        symTab.add(5, "│    M : 00    │");
        symTab.add(6, "│    I : 00    │");
        gameFields.keySet().forEach((x) -> symbolsTabs.put(x, symTab));
    }

    private void initializeTempSymbolsTabs(HashMap<String, VisibleElements> pve){
        pve.forEach(this::setTempSymbolsTab);
    }

    /**
     * setter method for the symbolsTab to update it to the one sent as parameter
     * @param sym map representing the symbolsTab to generate : key = Symbol, value = Integer
     */
    private void setTempSymbolsTab(String nickname, VisibleElements sym){
        this.symbolsTabs.get(nickname).set(0, "│    P : " + formatInt(sym.get(PLANT)) + "    │");
        this.symbolsTabs.get(nickname).set(1, "│    B : " + formatInt(sym.get(INSECT)) + "    │");
        this.symbolsTabs.get(nickname).set(2, "│    F : " + formatInt(sym.get(FUNGI)) + "    │");
        this.symbolsTabs.get(nickname).set(3, "│    A : " + formatInt(sym.get(ANIMAL)) + "    │");
        this.symbolsTabs.get(nickname).set(4, "│    Q : " + formatInt(sym.get(QUILL)) + "    │");
        this.symbolsTabs.get(nickname).set(5, "│    M : " + formatInt(sym.get(MANUSCRIPT)) + "    │");
        this.symbolsTabs.get(nickname).set(6, "│    I : " + formatInt(sym.get(INKWELL)) + "    │");
    }

    /**
     * formats a given int (between 0 and 99) to his 2 characters String (ex: "8" becomes " 8", 37 remains as is)
     * @param i the int to be formatted
     * @return a 2 characters String of the int given
     */
    private String formatInt(int i){
        if(i<10)
            return " " + i;
        else
            return String.valueOf(i);
    }

    //-------------------------------------------------------------------------------------------TopOfDecksAndGrounds

    private void initializeTopOfGDeck(){
        topOfGDeck.add(0, "╔═══════════╗");
        topOfGDeck.add(1, "║           ║");
        topOfGDeck.add(2, "║           ║");
        topOfGDeck.add(3, "╚═══════════╝");
    }
    private void initializeTopOfRDeck(){
        topOfRDeck.add(0, "┌───────────┐");
        topOfRDeck.add(1, "│           │");
        topOfRDeck.add(2, "│           │");
        topOfRDeck.add(3, "└───────────┘");
    }
    private void setTopOfGDeck(Symbol color){
        topOfGDeck.set(0, colorBackgroundString(color, "\u001B[30m╔═══════════╗"));
        topOfGDeck.set(1, colorBackgroundString(color, "\u001B[30m║           ║"));
        topOfGDeck.set(2, colorBackgroundString(color, "\u001B[30m║           ║"));
        topOfGDeck.set(3, colorBackgroundString(color, "\u001B[30m╚═══════════╝"));
    }

    private void setTopOfRDeck(Symbol color){
        topOfRDeck.set(0, colorBackgroundString(color, "\u001B[30m┌───────────┐"));
        topOfRDeck.set(1, colorBackgroundString(color, "\u001B[30m│           │"));
        topOfRDeck.set(2, colorBackgroundString(color, "\u001B[30m│           │"));
        topOfRDeck.set(3, colorBackgroundString(color, "\u001B[30m└───────────┘"));
    }

    private void setRGround(ResourceCard c, int i) {
        if (i == 1){
            resourceGround1 = colorCard(getCard(c), c.getKingdom());
        }else if (i == 2){
            resourceGround2 = colorCard(getCard(c), c.getKingdom());
        }
    }

    private void setGGround(GoldCard c, int i) {
        if (i == 1) {
            goldGround1 = colorCard(getCard(c), c.getKingdom());
        }else if (i == 2){
            goldGround2 = colorCard(getCard(c), c.getKingdom());
        }
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

    //----------------------------------------------------------------------------------------------------Chat

    private void initializeChat(){
        for (int i = 0; i < 6; i++) {
            chat.add(i, emptyLine);
        }
    }

    private String formatMessage(String message){
        return "║" + String.format("%-119s", message) + "║";
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

    private void initializeFields(){
        gameFields.forEach((k, v) -> {
            for(int i = 0 ; i < 41; i++)
                for(int j = 0 ; j < 81 ; j++)
                    v[i][j] = " ";
            v[20][40] = "\u001B[37m▀\u001B[0m";
            /*alternative method, maybe better
            String[][] field = new String[41][81];
            for(int i = 0 ; i < 41; i++)
                for(int j = 0 ; j < 81 ; j++)
                    field[i][j] = " ";
            field[20][40] = "\u001B[37m▀";
            gameFields.put(k, field);
            */
        });
    }

    private String getFieldChar(Symbol s, int y){
        if(y % 2 == 1)
            return colorString(s, "▄");
        return colorString(s, "▀");
    }

    /*
    Your Own Placement Field Update
    -> player input (X,Y) {-41 to 41}, {-41 to 41}
    -> filter {x,y} must be (x-y)%2 == 0
    -> temporary save (if placement is successful gets confirmed)
    -> gameField's coords: x = (X+Y)/2, y = (Y-X)/2
    -> answer: placeable or not
    -> (possible) re-do (repeat from start)
    -> once done view update with the coords in the temporary save done before

    Other Players Field Updates
    <- successful placement saved in a message (waiting for the draw view update too)
    <- once it arrives, coords x and y translated accordingly by the CLI
    <- player's field made of chars gets updated, if this Client asks it's shown centered
    */

    private String formatIndicator(int c){
        if(c >= 0)
            if(c < 10)
                return "  " + c;
            else
                return " " + c;
        else
            if(c > -10)
                return " " + c;
            else
                return String.valueOf(c);
    }

    private String getFieldRow(String[][] f, int row, int lS, int hhS){
        StringBuilder sBuilder = new StringBuilder(f[row - 10 + hhS][-20 + lS]);
        for(int i = -19; i < 21; i++)
            sBuilder.append(f[row + hhS][i + lS]);
        return sBuilder.toString();
    /*
    f[row + hhS][-20 + lS] + f[row + hhS][-19 + lS] + f[row + hhS][-18 + lS]+ f[row + hhS][-17 + lS]+ f[row + hhS][-16 + lS]+ f[row + hhS][-15 + lS]+ f[row + hhS][-14 + lS]+ f[row + hhS][-13 + lS]+ f[row + hhS][-12 + lS]+ f[row + hhS][-11 + lS]
    + f[row + hhS][-10 + lS] + f[row + hhS][-9 + lS] + f[row + hhS][-8 + lS]+ f[row + hhS][-7 + lS]+ f[row + hhS][-6 + lS]+ f[row + hhS][-5 + lS]+ f[row + hhS][-4 + lS]+ f[row + hhS][-3 + lS]+ f[row + hhS][-2 + lS]+ f[row + hhS][-1 + lS]
    + f[row + hhS][lS]
    + f[row + hhS][1 + lS] + f[row + hhS][2 + lS] + f[row + hhS][3 + lS]+ f[row + hhS][4 + lS]+ f[row + hhS][5 + lS]+ f[row + hhS][6 + lS]+ f[row + hhS][7 + lS]+ f[row + hhS][8 + lS]+ f[row + hhS][9 + lS]+ f[row + hhS][10 + lS]
    + f[row + hhS][11 + lS] + f[row + hhS][12 + lS] + f[row + hhS][13 + lS]+ f[row + hhS][14 + lS]+ f[row + hhS][15 + lS]+ f[row + hhS][16 + lS]+ f[row + hhS][17 + lS]+ f[row + hhS][18 + lS]+ f[row + hhS][19 + lS]+ f[row + hhS][20 + lS];
    */
    }

    private void updateShifts(){
        this.lateralShift = (playersFieldsLimits.get(currentlyViewedPlayerNick).get("right") + playersFieldsLimits.get(currentlyViewedPlayerNick).get("left")) / 2 + 40;//resti potrebbero causare problemi, tbd
        this.heightShift = (playersFieldsLimits.get(currentlyViewedPlayerNick).get("up") + playersFieldsLimits.get(currentlyViewedPlayerNick).get("down")) / 2 + 40;
        updateScreen();
    }

    private void computeDecksTop(){
        computeScreenLine(11);
        computeScreenLine(12);
        computeScreenLine(13);
        computeScreenLine(14);
    }

    private void computeGrounds1(){
        computeScreenLine(16);
        computeScreenLine(17);
        computeScreenLine(18);
        computeScreenLine(19);
    }


    private void computeGrounds2orHand(){
        computeScreenLine(20);
        computeScreenLine(21);
        computeScreenLine(22);
        computeScreenLine(23);
    }

    private void computeScreenLine(int n){
        switch (n) {
            case 2 -> gameScreen.set(2, "║ " + formatIndicator(lateralShift - 20 - 40) + "↓                " + formatIndicator(lateralShift - 40) + "↓                " + formatIndicator(lateralShift + 20 - 40) + "↓  " + nicksLine() + "      ║");
            case 3 -> gameScreen.set(3, "║" + formatIndicator(20 + heightShift - 40) + "↗" + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 0, lateralShift, heightShift / 2 - 10) + "  " + getPointsAndHandColors() + "║");
            case 4 -> gameScreen.set(4, "║    " + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 1, lateralShift, heightShift / 2 - 10) + "                                                                          ║");
            case 5 -> gameScreen.set(5, "║    " + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 2, lateralShift, heightShift / 2 - 10) + "  " + sharedObj1 + " ║");
            case 6 -> gameScreen.set(6, "║    " + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 3, lateralShift, heightShift / 2 - 10) + "  " + sharedObj2 + " ║");
            case 7 -> gameScreen.set(7, "║    " + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 4, lateralShift, heightShift / 2 - 10) + "  " + personalObj + " ║");
            case 8 -> gameScreen.set(8, "║    " + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 5, lateralShift, heightShift / 2 - 10) + "                                                                          ║");
            case 9 -> gameScreen.set(9, "║    " + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 6, lateralShift, heightShift / 2 - 10) + "  Gold          Resource            Game Field Shown: " + getNick(currentlyViewedPlayerNick) + "  ║");
            case 10 -> gameScreen.set(10, "║    " + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 7, lateralShift, heightShift / 2 - 10) + "  Deck:         Deck:            ┌──Card─Display───┐    ┌Shown─Symbols─┐  ║");
            case 11 -> gameScreen.set(11, "║    " + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 8, lateralShift, heightShift / 2 - 10) + "  " + topOfGDeck.getFirst() + " " + topOfRDeck.getFirst() + "    " + cardDisplay.getFirst() + "    " + symbolsTabs.get(currentlyViewedPlayerNick).getFirst() + "  ║");
            case 12 -> gameScreen.set(12, "║    " + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 9, lateralShift, heightShift / 2 - 10) + "  " + topOfGDeck.get(1) + " " + topOfRDeck.get(1) + "    " + cardDisplay.get(1) + "    " + symbolsTabs.get(currentlyViewedPlayerNick).get(1) + "  ║");
            case 13 -> gameScreen.set(13, "║" + formatIndicator(heightShift - 40) + "↗" + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 10, lateralShift, heightShift / 2 - 10) + "  " + topOfGDeck.get(2) + " " + topOfRDeck.get(2) + "    " + cardDisplay.get(2) + "    " + symbolsTabs.get(currentlyViewedPlayerNick).get(2) + "  ║");
            case 14 -> gameScreen.set(14, "║    " + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 11, lateralShift, heightShift / 2 - 10) + "  " + topOfGDeck.get(3) + " " + topOfRDeck.get(3) + "    " + cardDisplay.get(3) + "    " + symbolsTabs.get(currentlyViewedPlayerNick).get(3) + "  ║");
            case 15 -> gameScreen.set(15, "║    " + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 12, lateralShift, heightShift / 2 - 10) + "  Face Up Cards:                 " + cardDisplay.get(4) + "    " + symbolsTabs.get(currentlyViewedPlayerNick).get(4) + "  ║");
            case 16 -> gameScreen.set(16, "║    " + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 13, lateralShift, heightShift / 2 - 10) + "  " + goldGround1.getFirst() + " " + resourceGround1.getFirst() + "    │                 │    " + symbolsTabs.get(currentlyViewedPlayerNick).get(5) + "  ║");
            case 17 -> gameScreen.set(17, "║    " + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 14, lateralShift, heightShift / 2 - 10) + "  " + goldGround1.get(1) + " " + resourceGround1.get(1) + "    └─────────────────┘    " + symbolsTabs.get(currentlyViewedPlayerNick).get(6) + "  ║");
            case 18 -> gameScreen.set(18, "║    " + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 15, lateralShift, heightShift / 2 - 10) + "  " + goldGround1.get(2) + " " + resourceGround1.get(2) + "   Cards in your hand:     └──────────────┘  ║");
            case 19 -> gameScreen.set(19, "║    " + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 16, lateralShift, heightShift / 2 - 10) + "  " + goldGround1.get(3) + " " + resourceGround1.get(3) + "   1)            2)            3)            ║");
            case 20 -> gameScreen.set(20, "║    " + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 17, lateralShift, heightShift / 2 - 10) + "  " + goldGround2.getFirst() + " " + resourceGround2.getFirst() + "   " + ownStringHand.get(0).get(0) + " " + ownStringHand.get(1).get(0) + " " + ownStringHand.get(2).get(0) + " ║");
            case 21 -> gameScreen.set(21, "║    " + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 18, lateralShift, heightShift / 2 - 10) + "  " + goldGround2.get(1) + " " + resourceGround2.get(1) + "   " + ownStringHand.get(0).get(1) + " " + ownStringHand.get(1).get(1) + " " + ownStringHand.get(2).get(1) + " ║");
            case 22 -> gameScreen.set(22, "║    " + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 19, lateralShift, heightShift / 2 - 10) + "  " + goldGround2.get(2) + " " + resourceGround2.get(2) + "   " + ownStringHand.get(0).get(2) + " " + ownStringHand.get(1).get(2) + " " + ownStringHand.get(2).get(2) + " ║");
            case 23 -> gameScreen.set(23, "║" + formatIndicator(-20 + heightShift - 40) + "↗" + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 20, lateralShift, heightShift / 2 - 10) + "  " + goldGround2.get(3) + " " + resourceGround2.get(3) + "   " + ownStringHand.get(0).get(3) + " " + ownStringHand.get(1).get(3) + " " + ownStringHand.get(2).get(3) + " ║");
        }
    }

    //------------------------------------------------------------------------------------------------------------ Chat

    private void printChat(){
        String chatLine = "╟──ChatBox──────────────────────────────────────────────────────────────────────────────────────────────────────────────╢";
        System.out.println(chatLine);
        for (String s : chat) {
            System.out.println(s);
        }
        String endOfScreen = "╚═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝";
        System.out.println(endOfScreen);
    }

//------------------------------------------------------------------------------------------------------ Public Methods

    public void receiveOwnMessage(String s){
        receiveMessage(s);
    }

    //------------------------------------------------------------------------------------------------------ Game Start
    @Override
    public void showFirstScreen(String thisNick){
        this.currentlyViewedPlayerNick = thisNick;
        gameScreen.add(2, "║ " + formatIndicator(-20 + lateralShift - 40) + "↓                " + formatIndicator(lateralShift - 40) + "↓                " + formatIndicator(20 + lateralShift - 40) + "↓  " + nicksLine() + "      ║");
        gameScreen.add(3, "║" + formatIndicator(20 + heightShift - 40) + "↗" + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 0, lateralShift, heightShift / 2 - 10) + "  " + getPointsAndHandColors() + "║");
        gameScreen.add(4, "║    " + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 1, lateralShift, heightShift / 2 - 10) + "                                                                          ║");
        gameScreen.add(5, "║    " + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 2, lateralShift, heightShift / 2 - 10) + "  " + sharedObj1 + " ║");
        gameScreen.add(6, "║    " + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 3, lateralShift, heightShift / 2 - 10) + "  " + sharedObj2 + " ║");
        gameScreen.add(7, "║    " + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 4, lateralShift, heightShift / 2 - 10) + "  " + personalObj + " ║");
        gameScreen.add(8, "║    " + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 5, lateralShift, heightShift / 2 - 10) + "                                                                          ║");
        gameScreen.add(9, "║    " + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 6, lateralShift, heightShift / 2 - 10) + "  Gold          Resource            Game Field Shown: " + getNick(currentlyViewedPlayerNick) + "  ║");
        gameScreen.add(10, "║    " + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 7, lateralShift, heightShift / 2 - 10) + "  Deck:         Deck:            ┌──Card─Display───┐    ┌Shown─Symbols─┐  ║");
        gameScreen.add(11, "║    " + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 8, lateralShift, heightShift / 2 - 10) + "  " + topOfGDeck.getFirst() + " " + topOfRDeck.getFirst() + "    " + cardDisplay.getFirst() + "    " + symbolsTabs.get(currentlyViewedPlayerNick).getFirst() + "  ║");
        gameScreen.add(12, "║    " + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 9, lateralShift, heightShift / 2 - 10) + "  " + topOfGDeck.get(1) + " " + topOfRDeck.get(1) + "    " + cardDisplay.get(1) + "    " + symbolsTabs.get(currentlyViewedPlayerNick).get(1) + "  ║");
        gameScreen.add(13, "║" + formatIndicator(heightShift - 40) + "↗" + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 10, lateralShift, heightShift / 2 - 10) + "  " + topOfGDeck.get(2) + " " + topOfRDeck.get(2) + "    " + cardDisplay.get(2) + "    " + symbolsTabs.get(currentlyViewedPlayerNick).get(2) + "  ║");
        gameScreen.add(14, "║    " + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 11, lateralShift, heightShift / 2 - 10) + "  " + topOfGDeck.get(3) + " " + topOfRDeck.get(3) + "    " + cardDisplay.get(3) + "    " + symbolsTabs.get(currentlyViewedPlayerNick).get(3) + "  ║");
        gameScreen.add(15, "║    " + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 12, lateralShift, heightShift / 2 - 10) + "  Face Up Cards:                 " + cardDisplay.get(4) + "    " + symbolsTabs.get(currentlyViewedPlayerNick).get(4) + "  ║");
        gameScreen.add(16, "║    " + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 13, lateralShift, heightShift / 2 - 10) + "  " + goldGround1.getFirst() + " " + resourceGround1.getFirst() + "    │                 │    " + symbolsTabs.get(currentlyViewedPlayerNick).get(5) + "  ║");
        gameScreen.add(17, "║    " + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 14, lateralShift, heightShift / 2 - 10) + "  " + goldGround1.get(1) + " " + resourceGround1.get(1) + "    └─────────────────┘    " + symbolsTabs.get(currentlyViewedPlayerNick).get(6) + "  ║");
        gameScreen.add(18, "║    " + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 15, lateralShift, heightShift / 2 - 10) + "  " + goldGround1.get(2) + " " + resourceGround1.get(2) + "   Cards in your hand:     └──────────────┘  ║");
        gameScreen.add(19, "║    " + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 16, lateralShift, heightShift / 2 - 10) + "  " + goldGround1.get(3) + " " + resourceGround1.get(3) + "   1)            2)            3)            ║");
        gameScreen.add(20, "║    " + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 17, lateralShift, heightShift / 2 - 10) + "  " + goldGround2.getFirst() + " " + resourceGround2.getFirst() + "   " + ownStringHand.get(0).get(0) + " " + ownStringHand.get(1).get(0) + " " + ownStringHand.get(2).get(0) + " ║");
        gameScreen.add(21, "║    " + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 18, lateralShift, heightShift / 2 - 10) + "  " + goldGround2.get(1) + " " + resourceGround2.get(1) + "   " + ownStringHand.get(0).get(1) + " " + ownStringHand.get(1).get(1) + " " + ownStringHand.get(2).get(1) + " ║");
        gameScreen.add(22, "║    " + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 19, lateralShift, heightShift / 2 - 10) + "  " + goldGround2.get(2) + " " + resourceGround2.get(2) + "   " + ownStringHand.get(0).get(2) + " " + ownStringHand.get(1).get(2) + " " + ownStringHand.get(2).get(2) + " ║");
        gameScreen.add(23, "║" + formatIndicator(-20 + heightShift - 40) + "↗" + getFieldRow(gameFields.get(currentlyViewedPlayerNick), 20, lateralShift, heightShift / 2 - 10) + "  " + goldGround2.get(3) + " " + resourceGround2.get(3) + "   " + ownStringHand.get(0).get(3) + " " + ownStringHand.get(1).get(3) + " " + ownStringHand.get(2).get(3) + " ║");
        updateScreen();

    }


    //---------------------------------------------------------------------------------------------------- Card Display

    /**
     * setter method for the Card Display
     * @param card the PlayableCard to display
     * @param x its x coordinate
     * @param y its y coordinate
     */
    public void setCardDisplay(PlayableCard card, int x, int y){
        LinkedList<String> c = new LinkedList<>();
        if(card.getCardID() < 41)
            c.addAll(colorCard(getCard((ResourceCard) card), card.getKingdom()));
        else if(card.getCardID() < 81)
            c.addAll(colorCard(getCard((GoldCard)card), card.getKingdom()));
        else
            c.addAll(colorCard(getCard((StarterCard) card), card.getKingdom()));
        cardDisplay.set(0, "│   ( " + formatInt(x) + " , " + formatInt(y) + " )   │");
        cardDisplay.set(1, "│  " + c.getFirst() + "  │");
        cardDisplay.set(2, "│  " + c.get(1) + "  │");
        cardDisplay.set(3, "│  " + c.get(2) + "  │");
        cardDisplay.set(4, "│  " + c.get(3) + "  │");
    }

    //-------------------------------------------------------------------------------------------------------- Help box
    /**
     * method used to print the HelpBox
     */
    @Override
    public void printHelp() {
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


//------------------------------------------------------------------------------------------ Viewable Interface Methods

    public void playersTurn(String nickname){
        if(nickname.equals(this.nickname))
            System.out.println("Is now your turn! Use 'help' to see what you can do!");
        else
            System.out.println("It's" + nickname + "turn! Use 'help' to see what you can do!");
    }

    /**
     * method used to print the entire game screen
     */
    @Override
    public void updateScreen(){
        gameScreen.forEach(System.out::println);
        printChat();
    }

    /**
     * method that prints both faces of a StarterCard to show to the Player so that they can choose
     * @param sc the StarterCard drawn by the Player
     */
    @Override
    public void starterCardFacingChoice(StarterCard sc, Symbol gt, Symbol rt, GoldCard g1, GoldCard g2, ResourceCard r1, ResourceCard r2){
        setTopOfGDeck(gt); setGGround(g1, 1); setGGround(g2, 2);
        setTopOfRDeck(rt); setRGround(r1, 1); setRGround(r2, 2);
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

    /**
     * prints all infos a Player has during his Personal Objective Choice
     * @param pc each Player's nickname and color
     * @param hcc each Player's Hand Cards Colors
     * @param psc each Player's StarterCard (only the facing chosen by the owner of the StarterCard will be shown)
     * @param ownHand this Player's Cards in Hand
     * @param sharedObj1 the first Objective shared by all Players
     * @param sharedObj2 the second Objective shared by all Players
     * @param objChoice1 first Objective this Player can choose from
     * @param objChoice2 second Objective this Player can choose from
     */
    @Override
    public void personalObjectiveChoice(String nickname, HashMap<String, Color> pc, HashMap<String, Symbol[]> hcc, HashMap<String, StarterCard> psc, LinkedList<PlayableCard> ownHand, ObjectiveCard sharedObj1, ObjectiveCard sharedObj2, ObjectiveCard objChoice1, ObjectiveCard objChoice2, HashMap<String, VisibleElements> pve){
        this.nickname = nickname;
        pc.forEach((k, v) -> {//prints colored names and saves the nicks in the gameFields map
            setHandColors(k, hcc.get(k));
            coloredNicks.add(colorPlayer(getNick(k), v));
            System.out.print(" " + "(" + getHandColors(k) + ")" + colorPlayer(getNick(k), v));
            gameFields.put(k, new String[41][81]);
            initializeLimits(k);
            initializeScore(k);
        });
        initializeFields();
        initializeSymbolsTabs();
        initializeTempSymbolsTabs(pve);
        System.out.println();
        LinkedList<LinkedList<String>> cards = new LinkedList<>();
        psc.forEach((k, v) -> cards.add(getCard(psc.get(k))));//saves starting cards (with the right facing) as Lists of Strings
        for(int i = 0; i < 4; i++){
            cards.forEach(x -> System.out.print("   " + x.removeFirst() + "        "));//prints all starter cards, layer by layer
            System.out.println();
        }
        System.out.println("Cards in your hand:");
        ownHand.forEach(card -> {
            if (card.getCardID() < 41)
                ownStringHand.add(colorCard(getCard((ResourceCard) card), card.getKingdom()));
            else
                ownStringHand.add(colorCard(getCard((GoldCard) card), card.getKingdom()));
        });
        for(int i = 0 ; i < 4 ; i++)
            System.out.println("   " + ownStringHand.get(0).get(i) + "        "+ ownStringHand.get(1).get(i) + "        "+ ownStringHand.get(2).get(i));
        System.out.println();
        printSharedObjectives(sharedObj1.getDescription(), sharedObj2.getDescription());
        printObjectiveChoice(objChoice1.getDescription(), objChoice2.getDescription());
    }

    public void updateOtherPlayerDraw(String nickname, GoldCard gfu1, GoldCard gfu2, ResourceCard rfu1, ResourceCard rfu2, Symbol gtc, Symbol rtc, Symbol[] hcc){
        setTopOfGDeck(gtc);
        setTopOfRDeck(rtc);
        setGGround(gfu1, 1);
        setGGround(gfu2, 2);
        setRGround(rfu1, 1);
        setRGround(rfu2, 2);
        this.handColors.put(nickname, hcc);
        computeScreenLine(3);
        computeDecksTop();
        computeGrounds1();
        computeGrounds2orHand();
        updateScreen();
    }

    @Override
    public void setPersonalObjective(ObjectiveCard objective){
        this.personalObj = "P) " + String.format("%-68s", objective.getDescription());
        System.out.println("You chose:\n" + this.personalObj + "\n");
    }

    @Override
    public void receiveMessage(String message){
        int i;
        for (i = 0; i < 6 && !chat.get(i).equals(emptyLine); i++);
        if(i == 6)
            for(i = 0; i < 5 ; i++)
                chat.set(i, chat.get(i+1));
        chat.set(i, formatMessage(message));
        updateScreen();
    }

    /**
     * method that adds a card played by a player to his field (adjusts it according to how the gameField is stored in CLI)
     * @param nick String containing the nickname of the Player whose field the card is being added to
     * @param card the PlayableCard played by the Player whose nickname is given as a parameter
     * @param x int containing coordinate of the card played
     * @param y int containing coordinate of the card played
     */
    @Override
    public void setCardInField(String nick, PlayableCard card, int x, int y){
        int fixedY = y;
        if(y > 0)
            fixedY++;
        fixedY /= 2;
        this.gameFields.get(nick)[20 - (fixedY)][x + 40] = getFieldChar(card.getKingdom(), y);
        if(x < playersFieldsLimits.get(nick).get("left"))
            playersFieldsLimits.get(nick).put("left", x);
        else if(x > playersFieldsLimits.get(nick).get("right"))
            playersFieldsLimits.get(nick).put("right", x);
        if(fixedY < playersFieldsLimits.get(nick).get("down"))
            playersFieldsLimits.get(nick).put("down", fixedY);
        else if(fixedY > playersFieldsLimits.get(nick).get("up"))
            playersFieldsLimits.get(nick).put("up", fixedY);
    }

    @Override
    public void setHandAfterPlacement(LinkedList<PlayableCard> cardsInHand){
        for(int i = 0 ; i < 3; i++)
            setCardInHand(i, cardsInHand.get(i));
        computeScreenLine(20);
        computeScreenLine(21);
        computeScreenLine(22);
        computeScreenLine(23);
    }

    /**
     * method that updates and prints the Screen of this Player by giving them all the information about the Field of the Player chosen
     * @param nickname of the Player which this Player wants to see the game Field and information
     */
    @Override
    public void showPlayerField(String nickname) {
        this.currentlyViewedPlayerNick = nickname;
        updateShifts();
        computeScreenLine(2);
        computeScreenLine(4);
        computeScreenLine(5);
        computeScreenLine(6);
        computeScreenLine(7);
        computeScreenLine(8);
        computeScreenLine(9);
        computeScreenLine(10);
        updateScreen();
    }

    @Override
    public void updateScore(String nickname, int score){
        setPoints(nickname, score);
        computeScreenLine(3);
        updateScreen();
    }

    @Override
    public void updateEnemiesHandColors(String nick, Symbol[] handColors){
        setHandColors(nick, handColors);
        computeScreenLine(3);
        updateScreen();
    }

    @Override
    public void updateDraw(Symbol colorG, Symbol colorR, GoldCard gc1, GoldCard gc2, ResourceCard rc1, ResourceCard rc2, LinkedList<PlayableCard> card){
        setTopOfGDeck(colorG);
        setTopOfRDeck(colorR);
        setGGround(gc1, 1);
        setGGround(gc2, 2);
        setRGround(rc1, 1);
        setRGround(rc2, 2);
        for(int i = 0 ; i < 3; i++)
            setCardInHand(i, card.get(i));
        computeDecksTop();
        computeGrounds1();
        computeGrounds2orHand();
        updateScreen();
    }

    @Override
    public void setSymbolsTab(String nickname, VisibleElements symTab){
        setTempSymbolsTab(nickname, symTab);
        computeScreenLine(11);
        computeScreenLine(12);
        computeScreenLine(13);
        computeScreenLine(14);
        computeScreenLine(15);
        computeScreenLine(16);
        computeScreenLine(17);
        computeScreenLine(18);
    }

    @Override
    public void sendString(String s){
        System.out.println(s);
    }

    @Override
    public void priorityString(String s, int id){

        sendString(s.split("/")[1]);
    }

    public void displayString(String s){
        displayStringLogin(s);
    }

    @Override
    public ClientWriter startView(ClientCommandInterpreter cci) {
        String gameTitle1 = "\u001B[31m░░░░░░░░░░\u001B[32m░░░░░░░░░░\u001B[34m░░░░░░░░░░\u001B[35m░░░░░░░░░░\u001B[31m░░░░░░░░░░\u001B[32m░░░░░░\u001B[34m░░░░░░░░░░\u001B[35m░░░░░░░░░░\u001B[31m░░░░░░░░░░\u001B[32m░░░░░░░░░░\u001B[34m░░░░░░░░░░\u001B[35m░░░░░░░░░░\u001B[31m░░░░░░░░░░\u001B[32m░░░░░░░░░░\u001B[34m░░░░░░░░░░\u001B[0m\n" +
                "\u001B[32m░░      ░░\u001B[34m░░      ░░\u001B[35m░       ░░\u001B[31m░        ░\u001B[32m░  ░░░░  ░\u001B[34m░░░░░░\u001B[35m░   ░░░  ░\u001B[31m░░      ░░\u001B[32m░        ░\u001B[34m░  ░░░░  ░\u001B[35m░       ░░\u001B[31m░░      ░░\u001B[32m░  ░░░░░░░\u001B[34m░        ░\u001B[35m░░      ░░\u001B[0m\n" +
                "\u001B[34m▒  ▒▒▒▒  ▒\u001B[35m▒  ▒▒▒▒  ▒\u001B[31m▒  ▒▒▒▒  ▒\u001B[32m▒  ▒▒▒▒▒▒▒\u001B[34m▒▒  ▒▒  ▒▒\u001B[35m▒▒▒▒▒▒\u001B[31m▒    ▒▒  ▒\u001B[32m▒  ▒▒▒▒  ▒\u001B[34m▒▒▒▒  ▒▒▒▒\u001B[35m▒  ▒▒▒▒  ▒\u001B[31m▒  ▒▒▒▒  ▒\u001B[32m▒  ▒▒▒▒  ▒\u001B[34m▒  ▒▒▒▒▒▒▒\u001B[35m▒▒▒▒  ▒▒▒▒\u001B[31m▒  ▒▒▒▒▒▒▒\u001B[0m\n" +
                "\u001B[35m▓  ▓▓▓▓▓▓▓\u001B[31m▓  ▓▓▓▓  ▓\u001B[32m▓  ▓▓▓▓  ▓\u001B[34m▓      ▓▓▓\u001B[35m▓▓▓    ▓▓▓\u001B[31m▓▓▓▓▓▓\u001B[32m▓  ▓  ▓  ▓\u001B[34m▓  ▓▓▓▓  ▓\u001B[35m▓▓▓▓  ▓▓▓▓\u001B[31m▓  ▓▓▓▓  ▓\u001B[32m▓       ▓▓\u001B[34m▓  ▓▓▓▓  ▓\u001B[35m▓  ▓▓▓▓▓▓▓\u001B[31m▓▓▓▓  ▓▓▓▓\u001B[32m▓▓      ▓▓\u001B[0m\n" +
                "\u001B[31m█  ████  █\u001B[32m█  ████  █\u001B[34m█  ████  █\u001B[35m█  ███████\u001B[31m██  ██  ██\u001B[32m██████\u001B[34m█  ██    █\u001B[35m█        █\u001B[31m████  ████\u001B[32m█  ████  █\u001B[34m█  ███  ██\u001B[35m█        █\u001B[31m█  ███████\u001B[32m████  ████\u001B[34m███████  █\u001B[0m\n" +
                "\u001B[32m██      ██\u001B[34m██      ██\u001B[35m█       ██\u001B[31m█        █\u001B[32m█  ████  █\u001B[34m██████\u001B[35m█  ███   █\u001B[31m█  ████  █\u001B[32m████  ████\u001B[34m██      ██\u001B[35m█  ████  █\u001B[31m█  ████  █\u001B[32m█        █\u001B[34m█        █\u001B[35m██      ██\u001B[0m\n" +
                "\u001B[34m██████████\u001B[35m██████████\u001B[31m██████████\u001B[32m██████████\u001B[34m██████████\u001B[35m██████\u001B[31m██████████\u001B[32m██████████\u001B[34m██████████\u001B[35m██████████\u001B[31m██████████\u001B[32m██████████\u001B[34m██████████\u001B[35m██████████\u001B[31m██████████\u001B[0m";
        System.out.println(gameTitle1);
        ClientWriter cw = new ClientWriter(cci);
        cw.setDaemon(true);
        return cw;
    }

    @Override
    public void displayStringLogin(String s)
    {
		String[] tokens = s.split(" ");
        switch (tokens[0])
        {
          case "Insert" ->  System.out.println("Insert your username max 15 characters min 3 characters: (no space)");
          case "Taken" -> System.out.println("Nickname already taken, retry:");
          case "NotIn" -> System.out.println("Nickname not inserted, retry:");
		  case "What" -> System.out.println("What do you want to do?\n1) Create a game\n2) Join a game");
		  case "NotValidWhat" -> System.out.println("Your input is not valid. Retry:\n1) Create a game\n2)Join a game");
		  case "Create" -> System.out.println("To create a game specify the number of players that will participate (from 2 to 4) [type 'e' to go back in the menu]:");
		  case "NotValidCreate" -> System.out.println("Your input is not valid. Retry:\nFrom 2 to 4 players.");
		  case "Join"-> System.out.println( "To join a game specify its GameId number [type 'e' to go back in the menu]:");
          case "Full" -> System.out.println("The game you are trying to connect is full. Retry");
          case "NotFound" -> System.out.println("The id you specified doesn't exists. Insert the IdGame you or your friend have exposed on the screen. Retry [e to go back to menu]:");
		  case "NotNumber" -> System.out.println("The argument you have given is not a number please retry");
          case "SuccJoin" -> System.out.println("You joined a game successfully. Have fun!");
          case "SuccCreate" -> System.out.println("You created a game successfully, show your GAMEID to your friend to let them join you!\nGAMEID:"  + tokens[1]);
		}

    }
}

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

╔═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗
║ ENDGAME STARTED ! - ENDGAME STARTED ! - ENDGAME STARTED ! - ENDGAME STARTED ! - ENDGAME STARTED ! - ENDGAME STARTED ! ║
║  xx↓                 xx↓                 xx↓  PlayerNickname1   PlayerNickname2   PlayerNickname3   PlayerNickname4   ║
║ xx↗▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀     xxPTS ███         xxPTS ███         xxPTS ███         xxPTS ███      ║
║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀                                                                          ║
║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀  1) xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxx ║
║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀  2) xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxx ║
║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀  P) xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxx ║
║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀                                                                          ║
║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀  Gold          Resource            Game Field Shown: PlayerNicknameX     ║
║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀  Deck:         Deck:            ┌──Card─Display───┐    ┌Shown─Symbols─┐  ║
║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀  ╔═══════════╗ ┌───────────┐    │   ( xx , xx )   │    │    P : xx    │  ║
║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀  ║           ║ │           │    │  ┌───────────┐  │    │    B : xx    │  ║
║ xx↗▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀  ║           ║ │           │    │  │           │  │    │    F : xx    │  ║
║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀  ╚═══════════╝ └───────────┘    │  │           │  │    │    A : xx    │  ║
║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀  Face Up Cards:                 │  └───────────┘  │    │    Q : xx    │  ║
║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀  ╔═══════════╗ ┌───────────┐    │                 │    │    M : xx    │  ║
║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀  ║           ║ │           │    └─────────────────┘    │    I : xx    │  ║
║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀  ║           ║ │           │   Cards in your hand:     └──────────────┘  ║
║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀  ╚═══════════╝ └───────────┘   1)            2)            3)            ║
║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀  ╔═══════════╗ ┌───────────┐   ┌───────────┐ ┌───────────┐ ┌───────────┐ ║
║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀  ║           ║ │           │   │           │ │           │ │           │ ║
║    ▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀  ║           ║ │           │   │           │ │           │ │           │ ║
║ xx↗▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀▄▀  ╚═══════════╝ └───────────┘   └───────────┘ └───────────┘ └───────────┘ ║
╟──ChatBox──────────────────────────────────────────────────────────────────────────────────────────────────────────────╢
║Mattix124: cacca                                                                                                       ║
║Tommy(whispers): mattea è proprio scarso                                                                               ║
║Beppe: ez win                                                                                                          ║
║You: brb!                                                                                                              ║
║...                                                                                                                    ║
║xxxxxxxxx xxxxxxxxx xxxxxxxxx xxxxxxxxx xxxxxxxxx xxxxxxxxx xxxxxxxxx xxxxxxxxx xxxxxxxxx xxxxxxxxx xxxxxxxxx xxxxxxxxx║
╚═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝
┌──────────┐
│  P : xx  │
│  B : xx  │
│  F : xx  │
│  A : xx  │
│  Q : xx  │
│  M : xx  │
│  I : xx  │
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
ඩ ඞ
*/
