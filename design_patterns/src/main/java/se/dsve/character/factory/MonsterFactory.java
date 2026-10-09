package se.dsve.character.factory;
import se.dsve.character.Boss;
import se.dsve.character.Monster;
import se.dsve.game.GameState;
import se.dsve.game.engine_utils.Difficulty;

import java.util.List;
import java.util.Random;

/***
 * Added powerscaling for monsters based on difficulty. Private methods and therefore not docs commented.
 * Used method overrides for boss/monster rather than generics.
 */
public class MonsterFactory {
    private final Random random = new Random();
    private Difficulty difficulty;

    public MonsterFactory(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    private final List<MonsterTemplate> monsterTemplates = List.of(
            new MonsterTemplate("Goblin", 20, 5, 20, 20),
            new MonsterTemplate("Orc", 25, 8, 30, 30),
            new MonsterTemplate("Troll", 35, 11, 40, 40),
            new MonsterTemplate("Baby Giant", 40, 14, 50, 50),
            new MonsterTemplate("Baby Dragon", 45, 17, 60, 60)
    );

    private final List<MonsterTemplate> bossTemplates = List.of(
            new MonsterTemplate("Giant Dragon", 50, 20, 60, 70),
            new MonsterTemplate("Giant Troll", 60, 25, 70, 80),
            new MonsterTemplate("Giant Giant", 70, 30, 80, 90)
    );

    public Monster createMonster() {
        MonsterTemplate template = monsterTemplates.get(random.nextInt(monsterTemplates.size()));
        Monster monster = new Monster(template.name, template.hp, template.damage, template.goldReward, template.xpReward);
        return powerScale(monster);
    }

    public Boss createBoss() {
        MonsterTemplate template = bossTemplates.get(random.nextInt(bossTemplates.size()));
        Boss boss = new Boss(template.name, template.hp, template.damage, template.goldReward, template.xpReward);
        return powerScale(boss);
    }

    private record MonsterTemplate(String name, int hp, int damage, int goldReward, int xpReward) {}

    private Monster powerScale(Monster monster){
        String name = monster.getName();
        int hp = (int) (monster.getHp() * difficulty.getMonsterPower());
        int dmg = (int) (monster.getDamage() * difficulty.getMonsterPower());;
        int gold = (int) (monster.getGoldReward() * difficulty.getMonsterPower());
        int xp = (int) (monster.getXpReward() * difficulty.getMonsterPower());
        return new Monster(name, hp, dmg, gold, xp);
    }

    private Boss powerScale(Boss boss){
        String name = boss.getName();
        int hp = (int) (boss.getHp() * difficulty.getMonsterPower());
        int dmg = (int) (boss.getDamage() * difficulty.getMonsterPower());
        int gold = (int) (boss.getGoldReward() * difficulty.getMonsterPower());
        int xp = (int) (boss.getXpReward() * difficulty.getMonsterPower());
        return new Boss(name, hp, dmg, gold, xp);
    }
}
