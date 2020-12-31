package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;

public class BombRangeInc extends Decor {
    @Override
    public String toString() {
        return "BombRangeInc";
    }

    @Override
    public boolean canExplode() { return true; }

    @Override
    public boolean take(Game game, Position nextPos){
        game.getWorld().clear(nextPos);
        game.getPlayer().setRange_bomb(game.getPlayer().getRange_bomb() + 1);
        return true;
    }
}
