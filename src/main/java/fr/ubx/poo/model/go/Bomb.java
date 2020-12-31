package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.game.World;
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
        /*for(int xAxis= position.x-range; xAxis< position.x+range;xAxis++){
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

        }*/

        int start_x = position.x - this.range;
        if(start_x < 0) start_x = 0;

        int end_x = position.x + this.range;
        if(end_x > game.getWorld().getDimension().getWidth()) end_x = game.getWorld().getDimension().getWidth();

        int start_y = position.y - this.range;
        if(start_y < 0) start_y = 0;

        int end_y = position.y + this.range;
        if(end_y > game.getWorld().getDimension().getHeight()) end_y = game.getWorld().getDimension().getHeight();

        for(int xAxis = position.x; start_x <= xAxis; xAxis--){
            if (playerPosition.x==xAxis && playerPosition.y==position.y){
                game.getPlayer().hurt();
            }
            for (Monster mons: game.getMonster()){
                if (mons.getPosition().x==xAxis && mons.getPosition().y==position.y){
                    mons.setLives(mons.getLives()-1);
                }
            }
            Position nextPos = new Position(xAxis, position.y);
            if (nextPos.inside(game.getWorld().dimension)) {            //on teste si le décor est dans les dimensions (éviter la statusbar)
                if(map.get(nextPos).canExplode()){                     //on teste si le décor peut exploser
                    if(map.get(nextPos).isBox()){                  //si le décor est une box on le détruit et ne propage pas l'explosion
                        map.clear(nextPos);
                        break;
                    }
                    else                                           //le décor n'est pas explosible, il stop l'explosion
                        map.clear(nextPos);
                }
                else
                    break;
            }
        }

        for(int xAxis = position.x; xAxis < end_x; xAxis++){
            if (playerPosition.x==xAxis && playerPosition.y==position.y){
                game.getPlayer().hurt();
            }
            for (Monster mons: game.getMonster()){
                if (mons.getPosition().x==xAxis && mons.getPosition().y==position.y){
                    mons.setLives(mons.getLives()-1);
                }
            }
            Position nextPos = new Position(xAxis, position.y);
            if (nextPos.inside(game.getWorld().dimension)) {
                if(map.get(nextPos).canExplode()){
                    if(map.get(nextPos).isBox()){
                        map.clear(nextPos);
                        break;
                    }
                    else
                        map.clear(nextPos);
                }
                else
                    break;
            }
        }

        for(int yAxis = position.y; start_y < yAxis; yAxis--){
            if (playerPosition.y==yAxis && playerPosition.x==position.x){
                game.getPlayer().hurt();
            }
            for (Monster mons: game.getMonster()){
                if (mons.getPosition().y==yAxis && mons.getPosition().x==position.x){
                    mons.setLives(mons.getLives()-1);
                }
            }
            Position nextPos = new Position(position.x, yAxis);
            if (nextPos.inside(game.getWorld().dimension)) {
                if(map.get(nextPos).canExplode()){
                    if(map.get(nextPos).isBox()){
                        map.clear(nextPos);
                        break;
                    }
                    else
                        map.clear(nextPos);
                }
                else
                    break;
            }
        }

        for(int yAxis = position.y; yAxis < end_x; yAxis++){
            if (playerPosition.y==yAxis && playerPosition.x==position.x){
                game.getPlayer().hurt();
            }
            for (Monster mons: game.getMonster()){
                if (mons.getPosition().y==yAxis && mons.getPosition().x==position.x){
                    mons.setLives(mons.getLives()-1);
                }
            }
            Position nextPos = new Position(position.x, yAxis);
            if (nextPos.inside(game.getWorld().dimension)) {
                if(map.get(nextPos).canExplode()){
                    if(map.get(nextPos).isBox()){
                        map.clear(nextPos);
                        break;
                    }
                    else
                        map.clear(nextPos);
                }
                else
                    break;
            }
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
