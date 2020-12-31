package fr.ubx.poo.model.decor;

public class Box extends Decor{
    @Override
    public String toString() {
        return "Box";
    }

    @Override
    public boolean canWalk(){
        return false;
    };

    @Override
    public boolean isBox() { return true; }


}
