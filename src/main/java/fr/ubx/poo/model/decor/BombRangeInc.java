package fr.ubx.poo.model.decor;

public class BombRangeInc extends Decor {
    @Override
    public String toString() {
        return "BombRangeInc";
    }

    @Override
    public boolean canExplode() { return true; }
}
