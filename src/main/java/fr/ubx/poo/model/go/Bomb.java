package fr.ubx.poo.model.go;

import fr.ubx.poo.game.*;
import fr.ubx.poo.model.decor.*;
import fr.ubx.poo.model.go.GameObject;

public class Bomb extends GameObject{
    private int range=1;
    private long bombTimer=4;
    private boolean hasExploded=false;

    public Bomb (Game game, Position position, int range){
        super(game,position);
        this.range=range;
    }

    public void bombSet(Position position){

    }

    public void bombExplode(Position position){

    }

    public long getBombTimer(){
        return this.bombTimer;
    }

    public void setBombTimer(long newtimer){
        this.bombTimer=newtimer;
    }

    public boolean getHasExploded(){
        return this.hasExploded;
    }

    public void setHasExploded(boolean bool){
        this.hasExploded=bool;
    }
}
