package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;

public class Heart extends Decor{
    @Override
    public String toString() {
        return "Heart";
    }

    @Override
    public boolean canExplode() { return true; }

    @Override
    public boolean take(Game game, Position nextPos){
        game.getCurrentWorld().clear(nextPos);
        game.getPlayer().setLives(game.getPlayer().getLives() + 1);
        return true;
    }
}
