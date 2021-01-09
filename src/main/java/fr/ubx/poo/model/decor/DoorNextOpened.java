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

    //La porte est ouverte, donc ca methode take va actualiser le numero du monde courant et on va demander
    //un changement de niveau
    @Override
    public boolean take(Game game, Position nextPos){
        game.setCurrentLevel(game.getCurrentLevel()+1);
        game.hasRequestedLevelChange=true;
        return true;
    }

    @Override
    public boolean isNextDoor() {
        return true;
    }
}