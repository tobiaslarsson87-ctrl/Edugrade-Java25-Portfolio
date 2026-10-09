package se.dsve.combat;
import se.dsve.character.Player;
import se.dsve.combat.combat_utils.Attack;
import se.dsve.combat.combat_utils.CombatResult;
import se.dsve.game.GameState;
import se.dsve.presentation.hud.BattleHUD;
import se.dsve.presentation.render.RenderCombat;
import se.dsve.utils.InputHandler;
import java.util.concurrent.ThreadLocalRandom;

/***
 * Same flow is maintained as the original before the refactor I have only made it way better.
 * AttackIsKill now rely on abstractions instead of concretes and makes the control of the
 * flow much easier since it utilizes a return value that automatically flags isDead() for the
 * target. Allowed me to implement more options while keeping the code readable.
 */
public class CombatService {
    private final BattleHUD hud;

    public CombatService(BattleHUD hud) {
        this.hud = hud;
    }

    public CombatResult fight(Combatant player, Combatant monster) {
        while (anyoneIsAlive(player, monster)) {
            switch (hud.call()){
                case ATTACK -> {
                    if (attackIsKill(player, monster)) return CombatResult.PLAYER_WIN;
                    if (attackIsKill(monster, player)) return CombatResult.PLAYER_LOSS;
                }
                case USE_POTION -> {
                    if (potionIsUsed(player)) {
                        if (attackIsKill(monster, player)) return CombatResult.PLAYER_LOSS;
                    } else {
                        RenderCombat.outOfPotions(player);
                        continue;
                    }
                }
                case ESCAPE -> {
                    if (escape()) {
                        RenderCombat.escapeSuccess(player);
                        return CombatResult.PLAYER_ESCAPE;
                    } else {
                        RenderCombat.escapeFail(player);
                        if (attackIsKill(monster, player)) return CombatResult.PLAYER_LOSS;
                    }
                }
            }

            RenderCombat.displayCombatStatus(player, monster);
            InputHandler.paus();

        }
        return CombatResult.PLAYER_ESCAPE; //fallback only, nothing happens
    }

    private boolean attackIsKill(Combatant attacker, Combatant target){
        Attack attack = attacker.attack();
        RenderCombat.displayAttack(attacker, attack);
        target.takeDamage(attack);
        if (target.isDead()) {
            RenderCombat.death(target);
            return true;
        } return false;
    }

    private boolean potionIsUsed(Combatant user){
        if (user instanceof Player p) {
            if (p.usePotion()){
                RenderCombat.potionUse(p);
                return true;
            } else {
                return false;
            }
        }
        return false; //fallback for all combatants with no potion functionality
    }

    private boolean escape(){
        double chance = GameState.getInstance().getDifficulty().getEscapeChance();
        double roll = ThreadLocalRandom.current().nextDouble();
        return roll < chance;
    }

    private boolean anyoneIsAlive(Combatant player, Combatant monster) {
        return !player.isDead() && !monster.isDead();
    }
}
