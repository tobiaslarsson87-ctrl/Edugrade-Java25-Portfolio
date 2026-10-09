package se.dsve.character.observer.milestones;
import se.dsve.character.Player;
import se.dsve.character.observer.PlayerObserver;
import se.dsve.presentation.Log;

public class Milestone_LegendaryWeapon implements PlayerObserver {
    private boolean unlocked = false;
    @Override
    public void update(Player player) {
        if (!unlocked && player.getWeapon().getDamage() > 25){
            unlocked = true;
            Log.milestoneWeapon();
        }
    }
}
