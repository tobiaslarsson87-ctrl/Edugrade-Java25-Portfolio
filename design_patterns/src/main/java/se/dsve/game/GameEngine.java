package se.dsve.game;
import se.dsve.combat.Combatant;
import se.dsve.game.engine_utils.Encounter;
import se.dsve.game.engine_utils.Flow;
import se.dsve.game.engine_utils.GameEndCheck;
import se.dsve.game.engine_utils.Location;
import se.dsve.presentation.Log;
import se.dsve.presentation.hud.BattleHUD;
import se.dsve.presentation.render.RenderCommon;
import se.dsve.character.Player;
import se.dsve.combat.combat_utils.CombatResult;
import se.dsve.combat.CombatService;
import se.dsve.character.Boss;
import se.dsve.character.Monster;
import se.dsve.presentation.UI;
import se.dsve.presentation.render.RenderCombat;
import se.dsve.presentation.render.RenderGame;
import se.dsve.utils.Randomizer;

public class GameEngine {
    private final CombatService combatService = new CombatService(new BattleHUD());
    private final Encounter encounter = new Encounter();

    public void startGame() {
        GameState.getInstance().setGameRunning(true);
        Log.gameStarted();
    }

    public void endGame() {
        GameState.getInstance().setGameRunning(false);
        RenderCommon.exitGame();
    }

    public Flow gameLoop(Player player) {
        startGame();
        while (GameState.getInstance().isGameRunning()) {
            int choice = UI.locationMenu();
            switch (choice) {
                case 1 -> visitLocation(player, Location.FORREST);
                case 2 -> visitLocation(player, Location.CAVE);
                case 3 -> visitLocation(player, Location.CASTLE);
                case 4 -> visitLocation(player, Location.HELL);
                case 0 -> {
                    endGame();
                }
                default -> RenderCommon.invalidInput();
            }
            if (GameEndCheck.check(player) == Flow.END_GAME) {
                endGame();
                return Flow.END_GAME;
            }
        }
        return Flow.CONTINUE; //fallback only
    }

    public void visitLocation(Player player, Location location) {
        RenderGame.location(location);
        startAdventure(player);
    }

    private void startAdventure(Player player){
        boolean treasure = Randomizer.treasure();
        if(treasure){
            findTreasure(player);
        } else {
            fight(player);
        }
    }

    private void fight(Player player) {
        Combatant enemy = encounter.createEncounter();

        switch (enemy){
            case Boss b -> RenderCombat.bossEmerges();
            case Monster m -> RenderCombat.monsterEmerges();
            default -> RenderCombat.monsterEmerges();
        }

        CombatResult result = combatService.fight(player, enemy);

        if (result == CombatResult.PLAYER_WIN) {
            Monster m = (Monster) enemy;
            player.battleReward(m.getGoldReward(), m.getXpReward());
        }
    }

    private void findTreasure(Player player) {
        int gold = Randomizer.treasureAmount();
        player.treasure(gold);
    }
}
