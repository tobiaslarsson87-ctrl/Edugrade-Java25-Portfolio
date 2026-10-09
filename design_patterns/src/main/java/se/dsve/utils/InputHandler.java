package se.dsve.utils;
import se.dsve.presentation.ui_utils.MyColor;
import se.dsve.presentation.render.RenderCommon;
import java.util.Scanner;

public class InputHandler {
    private static final Scanner scanner = new Scanner(System.in);

    public static int getInt() {
        while(true){
            System.out.print(MyColor.MAGENTA.code() + "ENTER NUMBER: " + MyColor.RESET.code());
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                RenderCommon.invalidInput();
            }
        }
    }

    public static String getString() {
        System.out.print(MyColor.MAGENTA.code() + "ENTER TEXT: " + MyColor.RESET.code());
        return scanner.nextLine();
    }

    /***
     * Copy of getString() but without a prompt
     * @return : Returns any String.
     */
    public static String paus() {
        return scanner.nextLine();
    }
}
