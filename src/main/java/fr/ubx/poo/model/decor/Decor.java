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

    public boolean canWalk(){ return true; }

    public boolean isBox(){ return false; }

    public boolean canExplode(){ return false; }

    public boolean take(Game game, Position nextPos){ return true; }
}
