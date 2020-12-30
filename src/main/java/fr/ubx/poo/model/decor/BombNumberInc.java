package fr.ubx.poo.model.decor;

public class BombNumberInc extends Decor {
    @Override
    public String toString() {
        return "BombNumberInc";
    }

    @Override
    public boolean canExplode() { return true; }
}
