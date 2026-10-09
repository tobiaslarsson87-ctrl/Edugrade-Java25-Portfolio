package se.dsve.presentation.hud;
import se.dsve.combat.combat_utils.BattleAction;

public class BattleMOCK extends BattleHUD {
    private BattleAction nextAction;

    public void setNextAction(BattleAction action) {
        this.nextAction = action;
    }

    @Override
    public BattleAction call() {
        return nextAction;
    }
}
