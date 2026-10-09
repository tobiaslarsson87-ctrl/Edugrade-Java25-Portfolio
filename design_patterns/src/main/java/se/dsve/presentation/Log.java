package se.dsve.presentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.dsve.character.Player;
import se.dsve.game.GameState;
import se.dsve.game.engine_utils.Difficulty;
import se.dsve.presentation.ui_utils.MyColor;

import java.text.NumberFormat;

/***
 * A class used for only logging. Logger dependency can be kept here only.
 */

public class Log {
    private static final Logger logger = LoggerFactory.getLogger(Log.class);
    private static final String GREEN = MyColor.GREEN.code();
    private static final String YELLOW = MyColor.YELLOW.code();
    private static final String RED = MyColor.RED.code();
    private static final String CYAN = MyColor.CYAN.code();
    private static final String RESET = MyColor.RESET.code();

    public static void gameStarted(){
        logger.info(CYAN +"Game started!" + RESET);
    }

    public  static void levelUp(Player player){
        logger.info("Player {} leveled up to level {}", player.getName(), player.getLevel());
    }

    private static String percentFormatter(double number){
        NumberFormat percent = NumberFormat.getPercentInstance();
        percent.setMinimumFractionDigits(0);
        percent.setMaximumFractionDigits(0);

        return percent.format(number);
    }

    public static void difficultyInfo(){
        String escapeChance = percentFormatter(GameState.getInstance().getDifficulty().getEscapeChance());
        String monsterPower = percentFormatter(GameState.getInstance().getDifficulty().getMonsterPower());

        logger.info(GREEN + "Difficulty set to {}" + RESET, GameState.getInstance().getDifficulty().name());
        logger.info("Escape Chance changed to: {}", escapeChance);
        logger.info("Monster Power changed to: {}", monsterPower);
    }
    public static void startUp(){
        logger.info(GREEN + "APPLICATION STARTED" + RESET);
    }

    public static void shutDown(){
        logger.warn(RED + "CLOSING DOWN APPLICATION" + RESET);
    }

    public static void milestoneLV5(){
        logger.info("A MILESTONE HAS BEEN REACHED: " + YELLOW + "LEVEL 5" + RESET);
    }

    public static void milestoneLV10(){
        logger.info("A MILESTONE HAS BEEN REACHED: " + YELLOW + "LEVEL 10" + RESET);
    }

    public static void milestoneWeapon(){
        logger.info("A MILESTONE HAS BEEN REACHED: " + YELLOW + "LEGENDARY WEAPON" + RESET);
    }
}
