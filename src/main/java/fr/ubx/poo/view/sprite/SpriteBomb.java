package fr.ubx.poo.view.sprite;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.decor.Floor;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.view.image.ImageFactory;
import fr.ubx.poo.view.image.ImageResource;
import javafx.scene.layout.Pane;

public class SpriteBomb extends SpriteGameObject{

    private boolean hasExploded=false;

    public SpriteBomb(Pane layer, Bomb bomb) {
        super(layer, null, bomb);
        updateImage();
    }

    //On verifie si la bombe a explose, et si oui on affiche l'image du sol car la bombe existe plus
    //Sinon on recupere le temps passe depuis que la bombe est possee, pour afficher l'image correcte
    @Override
    public void updateImage() {
        if (hasExploded){
            setImage(ImageFactory.getInstance().get(ImageResource.FLOOR));
        }
        Bomb bomb = (Bomb) go;
        setImage(ImageFactory.getInstance().getBomb(bomb.getTimePassed()));
    }

    public boolean isHasExploded(){
        return this.hasExploded;
    }

    public void setHasExploded(boolean bool){
        this.hasExploded=bool;
    }
}
