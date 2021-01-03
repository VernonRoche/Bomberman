package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;

public class Key extends Decor{
    @Override
    public String toString() {
        return "Key";
    }

    @Override
    public boolean take(Game game, Position nextPos){
        game.getCurrentWorld().clear(nextPos);
        game.getPlayer().setNb_key(game.getPlayer().getNb_key() + 1);
        return true;
    }

    @Override
    public boolean canExplode(){ return false; }
}
