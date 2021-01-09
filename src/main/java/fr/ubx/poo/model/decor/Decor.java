/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Entity;

/***
 * A decor is an element that does not know its own position in the grid.
 */
public class Decor extends Entity {

    //Est ce que le joueur ou le monstre peut marcher dessus
    public boolean canWalk(){ return true; }

    //Est ce que le decor est une boite
    public boolean isBox(){ return false; }

    //Est ce que le decor peut exploser
    public boolean canExplode(){ return true; }

    //Est ce que c'est une porte ouverte vers un niveau precedent
    public boolean isPreviousDoor(){ return false; }

    //Est ce que c'est une porte vers un niveau suivant
    public boolean isNextDoor(){ return false; }

    //Methode pour recuperer quelques decors specifiques
    public boolean take(Game game, Position nextPos){ return true; }
}
