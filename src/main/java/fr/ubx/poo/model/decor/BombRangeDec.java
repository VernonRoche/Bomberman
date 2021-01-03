package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;

public class BombRangeDec extends Decor {
    @Override
    public String toString() {
        return "BombRangeDec";
    }

    @Override
    public boolean canExplode() { return true; }

    @Override
    public boolean take(Game game, Position nextPos){
        if(game.getPlayer().getRange_bomb() == 1)
            return true;
        game.getCurrentWorld().clear(nextPos);
        game.getPlayer().setRange_bomb(game.getPlayer().getRange_bomb() - 1);
        return true;
    }
}
