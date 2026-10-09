package se.dsve.utils;
import se.dsve.game.engine_utils.GameConfig;
import java.util.concurrent.ThreadLocalRandom;

/***
 * A utility Class that handles simple randomization
 */
public class Randomizer {
    /***
     * A simple integer check
     * @return : returns the amount of gold found based on preset max/min values
     */
    public static int treasureAmount(){
        int max = GameConfig.MAX_TREASURE_GOLD;
        int min = GameConfig.MIN_TREASURE_GOLD;
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    /***
     * A simple boolean check
     * @return : returns true if a treasure was found
     */
    public static boolean treasure(){
        return ThreadLocalRandom.current().nextInt(101) < GameConfig.LOOT_CHANCE;
    }
}
