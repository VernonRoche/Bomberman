package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;

public class BombNumberDec extends Decor {
    @Override
    public String toString() {
        return "BombNumberDec";
    }

    @Override
    public boolean canExplode() { return true; }

    //On decremente la taille du sac des bombes et on efface le malus
    @Override
    public boolean take(Game game, Position nextPos){
        if(game.getPlayer().getNb_bomb() == 0)
            return true;
        game.getCurrentWorld().clear(nextPos);
        game.getPlayer().setNb_bomb(game.getPlayer().getNb_bomb() - 1);
        return true;
    }
}
