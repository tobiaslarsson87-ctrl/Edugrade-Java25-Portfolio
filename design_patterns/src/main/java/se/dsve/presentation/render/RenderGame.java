package se.dsve.presentation.render;
import se.dsve.game.engine_utils.Location;
import se.dsve.presentation.ui_utils.MyColor;

/***
 * Console logs that were formerly in the GameEngine
 */
public class RenderGame {
    private static final String COLOR = MyColor.YELLOW.code();
    private static final String POSITIVE = MyColor.GREEN.code();
    private static final String WARNING = MyColor.RED.code();
    private static final String RESET = MyColor.RESET.code();

    public static void treasure(int gold){
        System.out.println("You found a treasure chest!");
        System.out.println("You found " + COLOR + gold + RESET + " gold!");
    }

    public static void gameCompletion(){
        System.out.println("You have reached level 10! You win!");
    }

    public static void gameOver(){
        System.out.println("☠️ Game Over ☠️");
    }

    public static void location(Location location){
        System.out.println(location.getDescription());
    }
}
