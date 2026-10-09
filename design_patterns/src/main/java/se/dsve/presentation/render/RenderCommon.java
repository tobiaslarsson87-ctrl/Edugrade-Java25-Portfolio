package se.dsve.presentation.render;
import se.dsve.presentation.ui_utils.MyColor;

/***
 * A class used for only printouts that are used on more than one place in the app
 */

public class RenderCommon {
    private static final String POSITIVE = MyColor.GREEN.code();
    private static final String HIGHLIGHT = MyColor.YELLOW.code();
    private static final String WARNING = MyColor.RED.code();
    private static final String RESET = MyColor.RESET.code();

    public static void  welcome(){
        System.out.println("WELCOME TO: " + POSITIVE + " ADVENTURE AWAITS" + RESET);
        lineBreak();
    }

    public static void invalidInput(){
        System.out.println(HIGHLIGHT + "INVALID INPUT" + RESET);
    }

    public static void exitGame(){
        System.out.println(HIGHLIGHT + "RETURNING TO MENU" + RESET);
    }

    public static void namePlayer(){
        System.out.println("=== 🧝🏻 Give your hero a proper name 🧝🏻 ===");
    }

    public static void nameWeapon(){
        System.out.println("=== ⚔️ Give your weapon a proper name ⚔️ ===");
    }

    public static void lineBreak(){
        System.out.println();
    }
}
