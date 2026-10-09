package se.dsve.combat.combat_utils;

/***
 * A simple data carrier that makes separation easier of presentation and logic for domain classes
 * @param damage : carries the int damage value
 * @param special : carries a boolean for special attack, only used by Boss
 */
public record Attack(int damage, boolean special) {
}
