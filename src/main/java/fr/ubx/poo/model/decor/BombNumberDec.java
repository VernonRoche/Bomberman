package fr.ubx.poo.model.decor;

public class BombNumberDec extends Decor {
    @Override
    public String toString() {
        return "BombNumberDec";
    }

    @Override
    public boolean canExplode() { return true; }
}
