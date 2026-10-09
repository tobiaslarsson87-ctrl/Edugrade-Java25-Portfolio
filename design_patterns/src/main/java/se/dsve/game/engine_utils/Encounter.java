package se.dsve.game.engine_utils;
import se.dsve.character.factory.MonsterFactory;
import se.dsve.combat.Combatant;
import se.dsve.game.GameState;

import java.util.concurrent.ThreadLocalRandom;

/***
 * Rather than having this as a private method in the GameEngine that simply randomized a value and then
 * did a check inside fight() I decided to make it a new method that returns a Combatant. Can be scaled
 * into returning enemies of more types than just Monster/Boss
 */
public class Encounter {

    public Combatant createEncounter(){
        MonsterFactory monsterFactory = new MonsterFactory(GameState.getInstance().getDifficulty());
        int roll = ThreadLocalRandom.current().nextInt(101);
        if (roll < GameConfig.BOSS_ENCOUNTER_CHANCE){
            return monsterFactory.createBoss();
        } else {
            return monsterFactory.createMonster();
        }
    }
}
