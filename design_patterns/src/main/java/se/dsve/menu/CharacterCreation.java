package se.dsve.menu;
import se.dsve.character.Player;
import se.dsve.character.observer.PlayerEventBus;
import se.dsve.game.engine_utils.GameConfig;
import se.dsve.item.Weapon;
import se.dsve.presentation.render.RenderCommon;
import se.dsve.utils.InputHandler;

/***
 * Contains 3 legacy methods. Only moved them out of main.
 */
public class CharacterCreation {
    /***
     * Legacy
     * @return : returns a Player object
     */
    public static Player start(PlayerEventBus bus){
        return createPlayer(bus);
    }

    private static Weapon createNewWeapon (){
        RenderCommon.nameWeapon();
        String name = InputHandler.getString();
        int damage = GameConfig.STARTING_DMG;
        return new Weapon(name, damage);
    }

    private static Player createPlayer (PlayerEventBus bus){
        Weapon weapon = createNewWeapon();
        RenderCommon.namePlayer();
        String name = InputHandler.getString();
        int startHP = GameConfig.STARTING_HP;
        return new Player(name, startHP, weapon, bus);
    }
}
