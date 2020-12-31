package fr.ubx.poo.model.decor;

public class DoorNextOpened extends Decor{
    @Override
    public String toString() {
        return "DoorNextOpen";
    }

    @Override
    public boolean canExplode(){ return false; }
}