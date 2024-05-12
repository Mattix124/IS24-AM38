package it.polimi.ingsw.am38.View;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CLITest {

    @Test
    void printTitle() {
     CLI cli = new CLI();
     cli.printTitle();
    }

    @Test
    void print(){
        String test1 = "╔═══════════╗\n" +
                "║⚘\u2004         ║\n" +
                "║⚲\u2004         ║\n" +
                "╚═══════════╝";
        String test2 = "┌───────────┐\n" +
                "│⚘\u2006          │\n" +
                "│⚲\u2006          │\n" +
                "└───────────┘";
        String test3 = "";
        String test4 = "";
        String test5 = "";
        String test6 = "";

        System.out.println(test1 + "\n");
        System.out.println(test2 + "\n");
    }
/*
u\2006, u\2009, u\205F, u\2005 = " ", u\2004, u\2000, u\2001
─ │ ┌ ┐ └ ┘ ├ ┤ ┬ ┴ ┼
═  ║  ╒	╓	╔	╕	╖	╗	╘	╙	╚	╛	╜	╝	╞	╟
╠  ╡	 ╢	╣	╤	╥	╦	╧	╨	╩	╪	╫	╬
13x4
┌───────────┐ ┌─┼───────┼─┐ ┌───────────┐ ┌───────────┐ ┌───────────┐
│           │ ┼           ┼ │          ✗│ │⊟         ⊡│ │           │
│           │ ┼─┐        x┼ │o         x│ │          ⎕│ │           │
└───────────┘ └─┼───────┼─┘ └───────────┘ └───────────┘ └───────────┘
╔═══════════╗
║⚘          ║
║✉          ║
╚═══════════╝
┌───────────┐
│⚘u\2006          │
│⚲u\2006          │
└───────────┘
╔══════╗ ┌──────┐
║      ║ │      │
╚══════╝ └──────┘
resources: ⚘ ଫ ⍾ ♘ oppure P B F A
items: ⚲ ✉ ⛫ oppure Q M I
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
 */
}