package se.dsve.common;

import se.dsve.combat.combat_utils.Attack;

public interface Damageable {
    int getHp();
    void takeDamage(Attack damage);
}
