package se.dsve.game.engine_utils;

/***
 * A simple container for constant values globally accessible. Only used
 * to clean up other classes that are currently extremely difficult to work in.
 */
public final class GameConfig {
    public static final double BOSS_ENCOUNTER_CHANCE = 0.1;
    public static final double LOOT_CHANCE = 0.1;
    public static final int STARTING_HP = 100;
    public static final int STARTING_DMG = 10;
    public static final int LEVEL_HP_INCREASE = 10;
    public static final int MIN_TREASURE_GOLD = 20;
    public static final int MAX_TREASURE_GOLD = 100;
    public static final int MAX_LEVEL = 10;
    public static final int UPGRADE_WEAPON_COST = 75;
    public static final int RESTORE_HP_COST = 25;
    public static final int POTION_COST = 10;
    public static final int VENOM_ENCHANTMENT_COST = 100;
    public static final int FIRE_ENCHANTMENT_COST = 200;
    public static final int MAGIC_ENCHANTMENT_COST = 400;
    public static final int EXP_ROOF_BASE = 50;
    public static final int EXP_ROOF_AMP = 25;
}
