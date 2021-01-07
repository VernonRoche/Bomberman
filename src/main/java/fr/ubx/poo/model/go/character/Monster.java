package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.GameObject;

public class Monster extends GameObject implements Movable {

    Direction direction;
    private boolean moveRequested = false;
    private int lives = 1;
    private boolean is_move = true;
    private boolean hasDetectedPlayer=false;

    public Monster(Game game, Position position) {
        super(game, position);
        this.direction = Direction.S;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int newlives){ this.lives=newlives; }

    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
        }
        moveRequested = true;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setIs_move(boolean is_move) {
        this.is_move = is_move;
    }



    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        if (!nextPos.inside(game.getCurrentWorld().dimension)) {
            return false;
        }
        Decor decor = game.getCurrentWorld().get(nextPos);
        return decor.canWalk();
    }

    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
    }

    public void update(long now, long delay) {
        if(is_move){
            if (detectPlayer()){
                delay=delay/2;
                hasDetectedPlayer=true;
            }
            else{
                hasDetectedPlayer=false;
            }
            setIs_move(false);
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            setIs_move(true);
                        }
                    },
                    delay
            );
            if (hasDetectedPlayer){
                requestMove(chasePlayer());
            }
            else{
                requestMove(Direction.random());
            }
            if (moveRequested) {
                if (canMove(direction)) {
                    doMove(direction);
                }
            }
            moveRequested = false;
        }
    }


    public Direction chasePlayer(){
        Position playerPosition=game.getPlayer().getPosition();
        if (playerPosition.x>this.getPosition().x){
            return Direction.E;
        }
        if (playerPosition.y>this.getPosition().y){
            return Direction.S;
        }
        if (playerPosition.x<this.getPosition().x){
            return Direction.W;
        }
        return Direction.N;
    }

    public boolean detectPlayer(){
        Position playerPosition=game.getPlayer().getPosition();
        //Maintenant on va voir par rapport a la position du monstre, si il voit le joueur dans ses axes X et Y
        Decor decor;
        for (int x=this.getPosition().x; x<game.getCurrentWorld().getDimension().width; x++){
            decor=game.getCurrentWorld().get(new Position(x, this.getPosition().y));
            if (decor==null){} else {
                if (!decor.canWalk()) {
                    break;
                }
                if (playerPosition.x == x && playerPosition.y == this.getPosition().y) {
                    return true;
                }
            }
        }

        for (int x=this.getPosition().x; x>0; x--){
            decor=game.getCurrentWorld().get(new Position(x, this.getPosition().y));
            if (decor==null){} else {
                if (!decor.canWalk()) {
                    break;
                }
                if (playerPosition.x == x && playerPosition.y == this.getPosition().y) {
                    return true;
                }
            }
        }

        for (int y=this.getPosition().y; y<game.getCurrentWorld().getDimension().height; y++){
            decor=game.getCurrentWorld().get(new Position(y, this.getPosition().x));
            if (decor==null){} else {
                if (!decor.canWalk()) {
                    break;
                }
                if (playerPosition.y == y && playerPosition.x == this.getPosition().x) {
                    return true;
                }
            }
        }

        for (int y=this.getPosition().y; y>0; y--){
            decor=game.getCurrentWorld().get(new Position(y, this.getPosition().x));
            if (decor==null){} else {
                if (!decor.canWalk()) {
                    break;
                }
                if (playerPosition.y == y && playerPosition.x == this.getPosition().x) {
                    return true;
                }
            }
        }
        return false;
    }

}
