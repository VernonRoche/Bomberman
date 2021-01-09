package fr.ubx.poo.view.sprite;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.view.image.ImageFactory;
import fr.ubx.poo.view.image.ImageResource;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class SpriteDoor extends SpriteDecor {
        private boolean open;

        public SpriteDoor(Pane layer, Image image, Position position) {
            super(layer, image, position);
        }

    public void setOpen(boolean open) {
        this.open = open;
    }

    //On verifie si la porte est ouverte et on affiche l'image correspondate
    @Override
        public void updateImage() {
            openSpriteDoor();
        }

        private void openSpriteDoor(){
            if (open){
                setImage(ImageFactory.getInstance().get(ImageResource.DOOR_OPENED));
            }
        }


}
