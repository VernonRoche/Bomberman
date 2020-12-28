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

    public Monster(Game game, Position position) {
        super(game, position);
        this.direction = Direction.S;
    }

    public int getLives() {
        return lives;
    }

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
        if (!nextPos.inside(game.getWorld().dimension)) {
            return false;
        }
        Decor decor = game.getWorld().get(nextPos);
        return decor.canWalk();
    }

    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
    }

    public void update(long now, long delay) {
        if(is_move){
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
            requestMove(Direction.random());
            if (moveRequested) {
                System.out.println("Yes");
                if (canMove(direction)) {
                    doMove(direction);
                }
            }
            moveRequested = false;
        }
    }

}
