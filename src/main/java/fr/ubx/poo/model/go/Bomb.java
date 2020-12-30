package fr.ubx.poo.model.go;

import fr.ubx.poo.game.*;
import fr.ubx.poo.model.decor.*;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.character.Monster;

public class Bomb extends GameObject{
    private int range=1;
    private long bombTimer=4;
    private long timePlaced;
    private long timePassed=0;

    public Bomb (Game game, Position position, int range, long timePlaced){
        super(game,position);
        this.range=range;
        this.timePlaced=timePlaced;
    }

    public void bombExplode(Position position){
        World map = game.getWorld();
        map.clear(position);
        Position playerPosition=game.getPlayer().getPosition();
        for(int xAxis= position.x-range; xAxis< position.x+range;xAxis++){
            if (playerPosition.x==xAxis && playerPosition.y==position.y){
                    game.getPlayer().hurt();
            }
            for (Monster mons: game.getMonster()){
                if (mons.getPosition().x==xAxis && mons.getPosition().y==position.y){
                    mons.setLives(mons.getLives()-1);
                }
            }
            map.clear(new Position(xAxis, position.y));

        }
        for(int yAxis= position.y-range; yAxis< position.y+range;yAxis++){
            if (playerPosition.y==yAxis && playerPosition.x==position.x){
                    game.getPlayer().hurt();
            }
            for (Monster mons: game.getMonster()){
                if (mons.getPosition().x==yAxis && mons.getPosition().y==position.x){
                    mons.setLives(mons.getLives()-1);
                }
            }
            map.clear(new Position(position.x, yAxis));

        }

    }

    public long getBombTimer(){
        return this.bombTimer;
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
