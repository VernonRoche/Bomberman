package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;

public class BombNumberInc extends Decor {
    @Override
    public String toString() {
        return "BombNumberInc";
    }

    @Override
    public boolean canExplode() { return true; }

    //On incremente la taille du sac de bombes et on efface le bonus
    @Override
    public boolean take(Game game, Position nextPos){
        game.getCurrentWorld().clear(nextPos);
        game.getPlayer().setNb_bomb(game.getPlayer().getNb_bomb() + 1);
        return true;
    }
}
