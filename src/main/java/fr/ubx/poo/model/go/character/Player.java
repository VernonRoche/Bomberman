/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.*;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.*;
import fr.ubx.poo.model.go.GameObject;

public class Player extends GameObject implements Movable {

    private final boolean alive = true;
    Direction direction;
    private boolean moveRequested = false;
    private int lives = 1;
    private int nb_bomb = 0;
    private int range_bomb = 1;
    private int nb_key = 0;
    private boolean winner;

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

    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
        }
        moveRequested = true;
    }

    @Override
    public boolean canMove(Direction direction) {
        Position nextPos=direction.nextPosition(getPosition());
        if (!nextPos.inside(game.getWorld().dimension)){
            return false;
        }

        World map = game.getWorld();
        Decor decor=game.getWorld().get(nextPos);

        if (decor instanceof Stone || decor instanceof Box || decor instanceof Tree){
            return false;
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
            if (nb_key>0){
                map.set(nextPos, new DoorNextOpened());
                nb_key=nb_key-1;
            }
            return true;
        }
        if (decor instanceof DoorNextOpened){
            //nextlevel

            return true;
        }

        return true;
    }

    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);

    }

    public void placeBomb(Position position){

    }

    public void update(long now) {
        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);
            }
        }
        moveRequested = false;
    }

    public boolean isWinner() {
        return winner;
    }

    public boolean isAlive() {
        return alive;
    }

}
