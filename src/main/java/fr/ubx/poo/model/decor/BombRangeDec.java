package fr.ubx.poo.model.decor;

public class BombRangeDec extends Decor {
    @Override
    public String toString() {
        return "BombRangeDec";
    }

    @Override
    public boolean canExplode() { return true; }
}
