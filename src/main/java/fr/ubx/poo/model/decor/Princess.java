package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;

public class Princess extends Decor{
    @Override
    public String toString() {
        return "Princess";
    }

    @Override
    public boolean take(Game game, Position nextPos){
        game.getPlayer().setWinner(true);
        return true;
    }

    @Override
    public boolean canExplode(){ return false; }
}
