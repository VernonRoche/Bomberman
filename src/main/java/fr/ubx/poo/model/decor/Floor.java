package fr.ubx.poo.model.decor;

//Element de decor qui remplace le Decor null original, pour pouvoir ajouter une representation graphique du sol
public class Floor extends Decor {
    @Override
    public String toString() {
        return "Floor";
    }
}
