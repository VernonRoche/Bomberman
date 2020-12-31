package fr.ubx.poo.model.decor;

public class DoorNextClosed extends Decor{
    @Override
    public String toString() {
        return "DoorNextClosed";
    }

    @Override
    public boolean canWalk(){
        return false;
    };

    @Override
    public boolean canExplode(){ return false; }
}
