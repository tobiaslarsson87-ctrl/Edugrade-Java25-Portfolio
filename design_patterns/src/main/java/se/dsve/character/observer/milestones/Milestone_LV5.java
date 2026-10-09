package se.dsve.character.observer.milestones;
import se.dsve.character.Player;
import se.dsve.character.observer.PlayerObserver;
import se.dsve.presentation.Log;

public class Milestone_LV5 implements PlayerObserver {
    private boolean unlocked = false;
    @Override
    public void update(Player player) {
        if (!unlocked && player.getLevel() >= 5){
            unlocked = true;
            Log.milestoneLV5();
        }
    }
}
