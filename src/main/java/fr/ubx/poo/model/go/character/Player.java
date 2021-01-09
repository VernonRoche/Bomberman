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

    private final boolean alive = true; //Est ce que le joueur est vivant
    Direction direction;
    private boolean moveRequested = false; //Est ce que le joueur a fait une demande de mouvement
    private boolean bombRequested = false; //Est ce que le joueur veut poser une bombe
    private int lives = 3; //Nombre de vies courantes du joueur
    private int nb_bomb = 1; //La taille de son sac de bombes
    private int range_bomb = 1; //La portee des bombes
    private int nb_key = 0; //Le nombre de cles que le joueur possede
    private boolean winner; //Si le joueur a gagne
    private boolean is_hurt = false; //Est ce que un monstre ou une bombe l'a endommage

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

    public void setWinner(boolean winner) { this.winner = winner; }

    public void setNb_bomb(int nb_bomb) { this.nb_bomb = nb_bomb; }

    public void setRange_bomb(int range_bomb) { this.range_bomb = range_bomb; }

    public void setNb_key(int nb_key) { this.nb_key = nb_key; }

    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
        }
        moveRequested = true;
    }

    public void requestBomb(){
        this.bombRequested=true;
    }

    //On verifie si le joueur peut avancer sur une case, dependant de sa direction
    @Override
    public boolean canMove(Direction direction) {
        Position nextPos=direction.nextPosition(getPosition());
        //On verifie si le joueur est dans les dimensions du monde
        if (!nextPos.inside(game.getCurrentWorld().dimension)){
            return false;
        }
        Decor decor = game.getCurrentWorld().get(nextPos);
        //On verifie si le methode canWalk du Decor renvoie true, et si oui on le recupere a sa facon
        if (decor.canWalk()){
            return decor.take(game, nextPos);
        }
        //Si le decor est une boite, on va la faire bouger avec moveBox
        if(decor.isBox()){
            return moveBox(nextPos);
        }
        return false;
    }

    //Ici on fait la resolution du mouvement d'une case, vers la direction demandee
    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
    }

    //On place une bombe si on le sac n'est pas vide, et on ajoute cette bombe dans les bombes actives du monde
    //courant
    public void placeBomb(Position position, long now){
        if (this.nb_bomb > 0) {
            this.nb_bomb = this.nb_bomb - 1;
            game.getCurrentWorld().addBombWorld(new Bomb(game,getPosition(),getRange_bomb(), now));
        }
        this.bombRequested=false;
    }

    public boolean isBombRequested() {
        return bombRequested;
    }

    public void setBombRequested(boolean bool){
        this.bombRequested=bool;
    }

    //On verifie si on a fait une demande de mouvement, si oui on resout ce mouvement apres verification de sa validite
    //et ensuite on verifie si le joueur veut poser une bombe
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

    //On verifie si on peut bouger une boite vers la position qu'on veut
    public boolean canMoveBox(Position position){
        Decor decor=game.getCurrentWorld().get(position);
        List<Monster> monster;
        monster = game.getCurrentWorld().getMonsterList();
        //Si un monstre se trouve a la position qu'on veut, la boite ne peut pas bouger
        for(Monster mons: monster){
            if(mons.getPosition().x == position.x && mons.getPosition().y == position.y){
                return false;
            }
        }
        //Si c'est du sol, alors dans cette condition seuleument elle peut bouger
        if (decor instanceof Floor){
            return true;
        }
        //Sinon on peut pas
        return false;
    }

    //On resoud le mouvement de la boite, en fonction de la position ou on veut la faire avancer et de la direction
    //du joueur
    public boolean moveBox(Position nextPos){
        World map = game.getCurrentWorld();
        //On verifie vers quelle direction on veut faire avancer la boite. Dans chaque cas, on va creer si possible
        //une boite a la position ou on veut bouger une boite, et transformer l'ancienne boite en sol
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

    //Ici on va endomager le joueur et lui enlever un point de vie, lui donnant une periode d'invincibilite
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

    //On ouvre une porte vers un niveau suivant fermee
    public void requestKey(){
        Position nextPos = direction.nextPosition(getPosition());
        World map = game.getCurrentWorld();

        if (nb_key>0 && game.getCurrentWorld().getRaw()[nextPos.y][nextPos.x] == DoorNextClosed){
            map.set(nextPos, new DoorNextOpened());
            nb_key=nb_key-1;
        }
    }

    //On va scanner tout le monde et en fonction de si on va a un niveau suivant ou precedent, on va teleporter le
    //joueur a la porte ouverte correspondate
    public void movePlayerNextToDoor(Player player, Position position, int exitingLevelNumber){
        if (!position.inside(this.game.getCurrentWorld().dimension)){ }
        else{
            Decor decor = this.game.getCurrentWorld().get(position);
            //on regarde si on retourne a un niveau precedent ou suivant
            if(this.game.getCurrentLevel()>exitingLevelNumber){//on passe au niveau suivant
                if (decor.isPreviousDoor()){
                    player.setPosition(position);
                }
            }
            else{//on retourne a un niveau precedent
                if (decor.isNextDoor()){
                    player.setPosition(position);
                }
            }
        }
    }

}
