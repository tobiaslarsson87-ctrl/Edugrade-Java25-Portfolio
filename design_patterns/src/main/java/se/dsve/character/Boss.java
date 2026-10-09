package se.dsve.character;

import se.dsve.combat.combat_utils.Attack;

import java.util.Random;

public class Boss extends Monster {
    private final Random random = new Random();
    private static final double SPECIAL_ATTACK_CHANCE = 0.3;
    private static final double SPECIAL_ATTACK_MULTIPLIER = 1.5;

    public Boss(String name, int hp, int damage, int goldReward, int xpReward) {
        super(name, hp, damage, goldReward, xpReward);
        if (getRandomValueFromZeroToOneHundred() > 50) {
            multiplyRewards();
        }
    }

    @Override
    public Attack attack() {
        int baseDamage = super.attack().damage();

        if (random.nextDouble() < SPECIAL_ATTACK_CHANCE) {
            int specialDmg = (int) (baseDamage * SPECIAL_ATTACK_MULTIPLIER);
            return new Attack(specialDmg, true);
        }

        return new Attack(baseDamage, false);
    }

    private void multiplyRewards() {
        int xpReward = (int) (this.getXpReward() * 1.5);
        int goldReward = (int) (this.getGoldReward() * 1.5);
        this.setXpReward(xpReward);
        this.setGoldReward(goldReward);
    }

    private int getRandomValueFromZeroToOneHundred() {
        Random random = new Random();
        return random.nextInt(101);
    }
}
