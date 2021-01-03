package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;

public class DoorNextOpened extends Decor{
    @Override
    public String toString() {
        return "DoorNextOpen";
    }

    @Override
    public boolean canExplode(){ return false; }

    @Override
    public boolean take(Game game, Position nextPos){
        game.setCurrentLevel(game.getCurrentLevel()+1);
        game.hasRequestedLevelChange=true;
        return true;
    }
}