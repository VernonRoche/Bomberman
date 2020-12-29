package fr.ubx.poo.model.go;

import fr.ubx.poo.game.*;
import fr.ubx.poo.model.decor.*;
import fr.ubx.poo.model.go.GameObject;

public class Bomb extends GameObject{
    private int range=1;
    private long bombTimer=4;
    private boolean hasExploded=false;
    private long timePlaced;
    private long timePassed=0;

    public Bomb (Game game, Position position, int range, long timePlaced){
        super(game,position);
        this.range=range;
        this.timePlaced=timePlaced;
    }

    public void bombSet(Position position){

    }

    public void bombExplode(Position position){
        World map = game.getWorld();
        map.clear(position);
        for(int xAxis= position.x-range; xAxis< position.x+range;xAxis++){
            map.clear(new Position(xAxis, position.y));
        }
        for(int yAxis= position.y-range; yAxis< position.y+range;yAxis++){
            map.clear(new Position(position.x, yAxis));
        }

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

    public long getTimePlaced(){
        return this.timePlaced;
    }

    public long getTimePassed(){
        return this.timePassed;
    }

    public void setTimePassed(long time){
        this.timePassed=time;
    }
}
