package fr.ubx.poo.model.decor;


public class DoorPrevOpened extends Decor {
    @Override
    public String toString() {
        return "DoorPrevOpened";
    }

    @Override
    public boolean canExplode(){ return false; }
}