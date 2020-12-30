/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.*;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.*;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.GameObject;

import java.util.List;

import static fr.ubx.poo.game.WorldEntity.DoorNextClosed;

public class Player extends GameObject implements Movable {

    private final boolean alive = true;
    Direction direction;
    private boolean moveRequested = false;
    private boolean bombRequested = false;
    private int lives = 3;
    private int nb_bomb = 15;
    private int range_bomb = 3;
    private int nb_key = 0;
    private boolean winner;
    private boolean is_hurt = false;

    public Player(Game game, Position position) {
        super(game, position);
        this.direction = Direction.S;
        this.lives = game.getInitPlayerLives();
    }

    public int getLives() {
        return lives;
    }

    public int getNb_bomb() {
        return nb_bomb;
    }

    public int getRange_bomb() {
        return range_bomb;
    }

    public int getNb_key() {
        return nb_key;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setIs_hurt(boolean is_hurt) { this.is_hurt = is_hurt; }

    public void setLives(int lives) { this.lives = lives; }

    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
        }
        moveRequested = true;
    }

    public void requestBomb(){
        this.bombRequested=true;
    }

    @Override
    public boolean canMove(Direction direction) {
        Position nextPos=direction.nextPosition(getPosition());
        if (!nextPos.inside(game.getWorld().dimension)){
            return false;
        }

        World map = game.getWorld();
        Decor decor=game.getWorld().get(nextPos);

        if (decor instanceof Stone || decor instanceof Tree){
            return false;
        }
        if (decor instanceof Box){
            return moveBox(nextPos);
            //System.out.println("BOX");
            //return false;
        }
        //voir si on est sur la princesse
        if (decor instanceof Princess){
            winner=true;
        }
        if (decor instanceof Heart){
            map.clear(nextPos);
            lives = lives +1;
            return true;
        }
        if (decor instanceof BombNumberDec){
            if(nb_bomb == 0) {return true;}
            map.clear(nextPos);
            nb_bomb = nb_bomb -1;
            return true;
        }
        if (decor instanceof BombNumberInc){
            map.clear(nextPos);
            nb_bomb = nb_bomb +1;
            return true;
        }
        if (decor instanceof BombRangeDec){
            if(range_bomb == 1) {return true;}
            map.clear(nextPos);
            range_bomb = range_bomb -1;
            return true;
        }
        if (decor instanceof BombRangeInc){
            map.clear(nextPos);
            range_bomb = range_bomb +1;
            return true;
        }
        if (decor instanceof Key){
            map.clear(nextPos);
            nb_key = nb_key +1;
            return true;
        }
        if (decor instanceof DoorNextClosed){
            /*if (nb_key>0){
                map.set(nextPos, new DoorNextOpened());
                nb_key=nb_key-1;
            }*/
            return false;
        }
        if (decor instanceof DoorNextOpened){
            //nextlevel

            return true;
        }
        //System.out.println("FLOOR");
        return true;
    }

    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);

    }

    public void placeBomb(Position position, long now){
        if (this.nb_bomb > 0) {
            //System.out.println("PLACING BOMB");
            this.nb_bomb = this.nb_bomb - 1;
            game.getWorld().addBombWorld(new Bomb(game,getPosition(),getRange_bomb(), now));
        }
        this.bombRequested=false;
    }

    public boolean isBombRequested() {
        return bombRequested;
    }

    public void setBombRequested(boolean bool){
        this.bombRequested=bool;
    }

    public void update(long now) {
        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);
            }
        }
        moveRequested = false;

        if (isBombRequested()){
            placeBomb(getPosition(), now/(1000000000));
        }

    }

    public boolean isWinner() {
        return winner;
    }

    public boolean isAlive() {
        if(getLives() <= 0) return false;
        return alive;
    }

    public boolean canMoveBox(Position position){
        Decor decor=game.getWorld().get(position);

        List<Monster> monster;
        monster = game.getMonster();
        for(Monster mons: monster){
            if(mons.getPosition().x == position.x && mons.getPosition().y == position.y){
                return false;
            }
        }

        if (decor instanceof Floor){
            return true;
        }
        return false;
    }

    public boolean moveBox(Position nextPos){
        World map = game.getWorld();
        switch (getDirection()){
            case E:
                Position creationPosE=new Position(nextPos.x+1, nextPos.y);
                if (canMoveBox(creationPosE)){
                    map.set(nextPos,new Floor());
                    map.set(creationPosE,new Box());
                    return true;

                }
                else{
                    return false;
                }
            case N:
                Position creationPosN=new Position(nextPos.x, nextPos.y-1);
                if (canMoveBox(creationPosN)){
                    map.set(nextPos,new Floor());
                    map.set(creationPosN,new Box());
                    return true;
                }
                else{
                    return false;
                }
            case S:
                Position creationPosS=new Position(nextPos.x, nextPos.y+1);
                if (canMoveBox(creationPosS)){
                    map.set(nextPos,new Floor());
                    map.set(creationPosS,new Box());
                    return true;
                }
                else{
                    return false;
                }
            case W:
                Position creationPosW=new Position(nextPos.x-1, nextPos.y);
                if (canMoveBox(creationPosW)){
                    map.set(nextPos,new Floor());
                    map.set(creationPosW,new Box());
                    return true;
                }
                else{
                    return false;
                }
            default:
                return false;
        }
    }

    public void hurt(){
        if(!is_hurt){
            setIs_hurt(true);
            setLives(getLives() - 1);
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            setIs_hurt(false);
                        }
                    },
                    1000
            );
        }
    }

    public void requestKey(){
        Position nextPos = direction.nextPosition(getPosition());
        World map = game.getWorld();

        if (nb_key>0 && game.getWorld().getRaw()[nextPos.y][nextPos.x] == DoorNextClosed){
            map.set(nextPos, new DoorNextOpened());
            nb_key=nb_key-1;
        }
    }

}
