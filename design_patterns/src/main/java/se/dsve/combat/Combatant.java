package se.dsve.combat;
import se.dsve.combat.combat_utils.Attack;
import se.dsve.common.Damageable;
import se.dsve.common.Killable;
import se.dsve.common.Nameable;

public interface Combatant extends Nameable, Damageable, Attacker, Killable {
    String getName();
    int getHp();
    void takeDamage(Attack attack);
    Attack attack();
}
