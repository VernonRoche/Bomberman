package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.game.World;
import fr.ubx.poo.model.go.character.Monster;

public class Bomb extends GameObject{
    private int range=1; //Portee de la bombe
    private long bombTimer=4; //Le temps qu'il faut pour que le bombe explose
    private long timePlaced; //L'heure exacte ou la bombe a ete placee
    private long timePassed=0; //Le temps passe depuis que la bombe a ete placee
    int lives = 1; //Nombre de vies de la bombe, utilise pour faire exploser d'autres bombes en reaction en chaine

    public Bomb (Game game, Position position, int range, long timePlaced){
        super(game,position);
        this.range=range;
        this.timePlaced=timePlaced;
    }

    public void bombExplode(Position position){
        World map = game.getCurrentWorld();
        map.clear(position);
        Position playerPosition=game.getPlayer().getPosition();
        //Ici on va determiner les positions de debut et de fin d'explosion de la bombe, dans les axes X et Y
        int start_x = position.x - this.range;
        if(start_x < 0) start_x = 0;

        int end_x = position.x + this.range;
        if(end_x > game.getCurrentWorld().getDimension().getWidth()) end_x = game.getCurrentWorld().getDimension().getWidth();

        int start_y = position.y - this.range;
        if(start_y < 0) start_y = 0;

        int end_y = position.y + this.range;
        if(end_y > game.getCurrentWorld().getDimension().getHeight()) end_y = game.getCurrentWorld().getDimension().getHeight();

        //Dans les boucles suivante, on va iterer pour verifier si on doit endomager le joueur, un monstre, faire
        //exploser une autre bombe, ou exploser un decor
        //Les cases a gauche de la bombe
        for(int xAxis = position.x; start_x <= xAxis; xAxis--){
            //On verifie si le joueur se trouve dans l'explosion et on l'endommage
            if (playerPosition.x==xAxis && playerPosition.y==position.y){
                game.getPlayer().hurt();
            }
            //On verifie si un monstre se trouve dans l'explosion et on l'endommage
            for (Monster mons: game.getCurrentWorld().getMonsterList()){
                if (mons.getPosition().x==xAxis && mons.getPosition().y==position.y){
                    mons.setLives(mons.getLives()-1);
                }
            }
            //On verifie si on doit faire exploser une autre bombe et provoquer une reaction en chaine
            for (Bomb bomb: game.getCurrentWorld().getPlacedBombs()){
                if (bomb.getPosition().x == xAxis && bomb.getPosition().y == position.y){
                    bomb.setLives(bomb.getLives() - 1);
                }
            }
            Position nextPos = new Position(xAxis, position.y);
            if (nextPos.inside(game.getCurrentWorld().dimension)) {            //on teste si le décor est dans les dimensions (éviter la statusbar)
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

        //Les cases a droite de la bombe
        for(int xAxis = position.x; xAxis < end_x; xAxis++){
            if (playerPosition.x==xAxis && playerPosition.y==position.y){
                game.getPlayer().hurt();
            }
            for (Monster mons: game.getCurrentWorld().getMonsterList()){
                if (mons.getPosition().x==xAxis && mons.getPosition().y==position.y){
                    mons.setLives(mons.getLives()-1);
                }
            }
            for (Bomb bomb: game.getCurrentWorld().getPlacedBombs()){
                if (bomb.getPosition().x==xAxis && bomb.getPosition().y==position.y){
                    bomb.setLives(bomb.getLives() - 1);
                }
            }
            Position nextPos = new Position(xAxis, position.y);
            if (nextPos.inside(game.getCurrentWorld().dimension)) {
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

        //Les cases en haut de la bombe
        for(int yAxis = position.y; start_y < yAxis; yAxis--){
            if (playerPosition.y==yAxis && playerPosition.x==position.x){
                game.getPlayer().hurt();
            }
            for (Monster mons: game.getCurrentWorld().getMonsterList()){
                if (mons.getPosition().y==yAxis && mons.getPosition().x==position.x){
                    mons.setLives(mons.getLives()-1);
                }
            }
            for (Bomb bomb: game.getCurrentWorld().getPlacedBombs()){
                if (bomb.getPosition().y==yAxis && bomb.getPosition().x==position.x){
                    bomb.setLives(bomb.getLives() - 1);
                }
            }
            Position nextPos = new Position(position.x, yAxis);
            if (nextPos.inside(game.getCurrentWorld().dimension)) {
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

        //Les cases en bas de la bombe
        for(int yAxis = position.y; yAxis < end_y; yAxis++){
            if (playerPosition.y==yAxis && playerPosition.x==position.x){
                game.getPlayer().hurt();
            }
            for (Monster mons: game.getCurrentWorld().getMonsterList()){
                if (mons.getPosition().y==yAxis && mons.getPosition().x==position.x){
                    mons.setLives(mons.getLives()-1);
                }
            }
            for (Bomb bomb: game.getCurrentWorld().getPlacedBombs()){
                if (bomb.getPosition().y==yAxis && bomb.getPosition().x==position.x){
                    bomb.setLives(bomb.getLives() - 1);
                }
            }
            Position nextPos = new Position(position.x, yAxis);
            if (nextPos.inside(game.getCurrentWorld().dimension)) {
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

    public int getLives() {
        return this.lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }
}
