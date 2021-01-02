package fr.ubx.poo.view.sprite;

import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.Pane;

public class SpriteMonster extends SpriteGameObject{
    private final ColorAdjust effect = new ColorAdjust();
    //private Monster monster_img = (Monster) go;

    public SpriteMonster(Pane layer, Monster monster) {
        super(layer, null, monster);
        updateImage();
    }

    @Override
    public void updateImage() {
        Monster monster = (Monster) go;
        if (monster.getLives()<=0){
            setImage(null);
        }
        else{
            setImage(ImageFactory.getInstance().getMonster(monster.getDirection()));
        }
    }
}
