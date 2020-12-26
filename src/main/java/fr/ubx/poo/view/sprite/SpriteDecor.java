package fr.ubx.poo.view.sprite;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.view.image.ImageFactory;
import fr.ubx.poo.view.image.ImageResource;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;


public class SpriteDecor extends Sprite {
    private Position position;
    private boolean isBox=false;

    public SpriteDecor(Pane layer, Image image, Position position) {
        super(layer, image);
        this.position = position;
    }

    @Override
    public void updateImage() {
        if (isBox){
            setImage(ImageFactory.getInstance().get(ImageResource.BOX));
            this.isBox=false;
        }
    }

    @Override
    public Position getPosition() {
        return position;
    }

    public void setIsBox(boolean bool){
        this.isBox=bool;
    }
}
