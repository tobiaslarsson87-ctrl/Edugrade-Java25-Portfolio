package se.dsve.character;
import se.dsve.combat.combat_utils.Attack;
import se.dsve.combat.Combatant;

public class Monster implements Combatant {
    private String name;
    private int hp;
    private int damage;
    private int goldReward;
    private int xpReward;

    public Monster(String name, int hp, int damage, int goldReward, int xpReward) {
        if (goldReward < 0 || xpReward < 0 || hp < 0 || damage < 0) {
            throw new IllegalArgumentException();
        }

        this.name = name;
        this.hp = hp;
        this.damage = damage;
        this.goldReward = goldReward;
        this.xpReward = xpReward;
    }

    protected int getDamageMultiplier(Player player, int dealtDamage) {
        if (player.getLevel() > 1) {
            int levelMultiplier = Math.round(player.getLevel() * 0.1f);
            dealtDamage = dealtDamage + levelMultiplier;
        }
        return dealtDamage;
    }

    @Override
    public void takeDamage(Attack attack) {
        setHp(this.hp - attack.damage());
    }

    @Override
    public Attack attack() {
        return new Attack(this.damage, false);
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, hp);
    }

    public int getDamage() {
        return damage;
    }

    public int getGoldReward() {
        return goldReward;
    }

    public void setGoldReward(int goldReward) {
        this.goldReward = goldReward;
    }

    public int getXpReward() {
        return xpReward;
    }

    public void setXpReward(int xpReward) {
        this.xpReward = xpReward;
    }

    /***
     * Simple utility method that makes it easier to use in a battle system. No more clamping required
     * @return a boolean true if hp == 0, otherwise false
     */
    @Override
    public boolean isDead(){
        return this.hp == 0;
    }
}
