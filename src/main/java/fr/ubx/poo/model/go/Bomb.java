package fr.ubx.poo.model.go;

import fr.ubx.poo.game.*;
import fr.ubx.poo.model.decor.*;
import fr.ubx.poo.model.go.GameObject;

public class Bomb extends GameObject{
    private int range=1;
    private double timer=3.0;

    public Bomb (Game game, Position position, int range){
        super(game,position);
        this.range=range;
    }

    public void bombSet(Position position){

    }

    public void bombExplode(Position position){

    }

    public void bombTimer(){

    }
}
