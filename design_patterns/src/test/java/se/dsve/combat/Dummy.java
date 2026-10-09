package se.dsve.combat;
import se.dsve.combat.combat_utils.Attack;

class Dummy implements Combatant {
    private int hp;
    private int dmg;

    public Dummy(int hp, int dmg) {
        this.hp = hp;
        this.dmg = dmg;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public int getHp() {
        return hp;
    }

    @Override
    public void takeDamage(Attack attack) {
        this.hp -= attack.damage();
    }

    @Override
    public Attack attack() {
        return new Attack(dmg, false);
    }

    @Override
    public boolean isDead() {
        return this.hp <= 0;
    }
}
