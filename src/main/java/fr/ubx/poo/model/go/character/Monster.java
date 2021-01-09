package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.game.World;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.GameObject;

public class Monster extends GameObject implements Movable {

    Direction direction;
    private boolean moveRequested = false; //a fait une demande de mouvement
    private int lives = 1; //le nombre de vies d'un monstre
    private boolean is_move = true; //utilise pour le update et limiter les mouvements du monstre
    private boolean hasDetectedPlayer=false; //si le joueur est dans le champ de vision des monstres
    private int level; //Le numero du monde ou ce trouve le monstre

    public Monster(Game game, Position position, int level) {
        super(game, position);
        this.direction = Direction.S;
        this.level = level;
    }

    public int getLevel() {
        return level;
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


    //On verifie si le monstre peut bouger sur une case
    @Override
    public boolean canMove(Direction direction) {
        World world = game.getWorld(this.level-1);
        Position nextPos = direction.nextPosition(getPosition());
        //on verifie si le monstre se trouve dans les dimensions du monde
        if (!nextPos.inside(world.dimension)) {
            return false;
        }
        Decor decor = world.get(nextPos);
        return decor.canWalk();
    }

    //On effectue le mouvement d'une case vers la direction demandee
    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
    }

    //Le monstre va chercher le joueur, puis il va faire un mouvement
    public void update(long now, long delay) {
        if(is_move){
            //Si le joueur est dans son champ de vision (dans les axes X ou Y), il va bouger 2 fois plus vite
            if (detectPlayer()){
                delay=delay/2;
                hasDetectedPlayer=true;
            }
            //Sinon le monstre oublie qu'il a vu le joueur
            else{
                hasDetectedPlayer=false;
            }
            //Mis en place du delai de mouvement
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
            //Si le monstre a detecte le joueur, il avance vers sa direction pour l'attaquer
            if (hasDetectedPlayer){
                requestMove(chasePlayer());
            }
            //Sinon il bouge aleatoirement, jusqu'a qu'il trouve le joueur
            else{
                requestMove(Direction.random());
            }
            //Resolution du mouvement: on regarde si on a fait une requete de mouvement, si oui on regarde si on
            //peut bouger sur cette case et ensuite on fait le mouvement d'une case
            if (moveRequested) {
                if (canMove(direction)) {
                    doMove(direction);
                }
            }
            moveRequested = false;
        }
    }

    //On recupere la direction vers laquelle le monstre doit avancer pour se rapprocher du joueur
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

    //On verifie si le joueur est dans le champ de vision du monstre: dans ses axes X ou Y
    public boolean detectPlayer(){
        Position playerPosition=game.getPlayer().getPosition();
        //Maintenant on va voir par rapport a la position du monstre, si il voit le joueur dans ses axes X et Y
        Decor decor;
        //Le monstre regarde a sa droite
        for (int x=this.getPosition().x; x<game.getCurrentWorld().getDimension().width; x++){
            decor=game.getCurrentWorld().get(new Position(x, this.getPosition().y));

                if (decor == null || !decor.canWalk()) {
                    break;
                }
                if (playerPosition.x == x && playerPosition.y == this.getPosition().y) {
                    return true;
                }

        }

        //Ensuite vers sa gauche
        for (int x=this.getPosition().x; x>0; x--){
            decor=game.getCurrentWorld().get(new Position(x, this.getPosition().y));

                if (decor == null || !decor.canWalk()) {
                    break;
                }
                if (playerPosition.x == x && playerPosition.y == this.getPosition().y) {
                    return true;
                }

        }

        //Ensuite vers le bas
        for (int y=this.getPosition().y; y<game.getCurrentWorld().getDimension().height; y++){
            decor=game.getCurrentWorld().get(new Position(this.getPosition().x, y));

                if (decor == null || !decor.canWalk()) {
                    break;
                }
                if (playerPosition.y == y && playerPosition.x == this.getPosition().x) {
                    return true;
                }

        }

        //Ensuite vers la haut
        for (int y=this.getPosition().y; y>0; y--){
            decor=game.getCurrentWorld().get(new Position(this.getPosition().x, y));

                if (decor == null || !decor.canWalk()) {
                    break;
                }
                if (playerPosition.y == y && playerPosition.x == this.getPosition().x) {
                    return true;
                }

        }
        //Il a pas vu le joueur
        return false;
    }

}
