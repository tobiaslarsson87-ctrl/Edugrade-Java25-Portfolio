package se.edugrade.utility;

import se.edugrade.menu.UserInput;

import java.sql.Date;
import java.time.LocalDate;

public class Helper {
    //Metod för datumkonvertering.
    public static Date convertDate(int year, int month, int day) {
        LocalDate localDate = LocalDate.of(year, month, day);
        return Date.valueOf(localDate);
    }
    //Metod för olika färger
    public static String colorS(String color, String text) {
        final String RESET = "\u001B[0m";
        final String RED = "\u001B[31m";
        final String GREEN = "\u001B[32m";
        final String YELLOW = "\u001B[33m";
        final String BLUE = "\u001B[34m";

        String returnColor;

        if (color.equalsIgnoreCase("red")) {
            returnColor = RED;
        }
        else if (color.equalsIgnoreCase("green")) {
            returnColor = GREEN;
        }
        else if (color.equalsIgnoreCase("yellow")) {
            returnColor = YELLOW;
        }
        else if (color.equalsIgnoreCase("blue")) {
            returnColor = BLUE;
        }
        else {
            returnColor = RESET;
        }
        return returnColor + text + RESET;
    }
    // Metod som kan användas i slutet av en SubMenu. Returnerar en boolean baserat på användarens val.
    // Om du använder en WHILE loop i din submenu kan du bara sätta loopVariabel = Helper.subMenuExit();
    //Då kommer användaren automatiskt återvända till Main Menu utan att en extra While loop behövs i Sub Menyn.
    public static boolean subMenuExit() {
        boolean subMenuExitLoop = true;

        System.out.print(Helper.colorS("green", "\n[1] "));
        System.out.print("\uD83D\uDD01 Stay in this menu   ");
        System.out.print(Helper.colorS("red", "[0] "));
        System.out.println("\uD83D\uDD19 Back to previous menu \n");

        while (subMenuExitLoop) {
            switch (UserInput.getInt()) {
                case 1 -> { return true; }
                case 0 -> { return false; }
                default -> { System.out.println("\n❌Invalid Input."); UserInput.paus(); }
            }
            UserInput.paus();
        }
        return false; //WHY :(
    }
}
