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

    @Override
    public boolean take(Game game, Position nextPos){
        game.getWorld().clear(nextPos);
        game.getPlayer().setNb_bomb(game.getPlayer().getNb_bomb() + 1);
        return true;
    }
}
