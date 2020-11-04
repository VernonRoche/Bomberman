/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.sprite;

import fr.ubx.poo.entity.go.personage.Player;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class SpritePlayer extends SpriteGameObject {

    private final ColorAdjust effect = new ColorAdjust();
    protected final Image[] images;

    public SpritePlayer(Pane layer, Image[] images, Player player) {
        super(layer, null, player);
        this.images = images;
        updateImage();
    }

    @Override
    public void updateImage() {
        Player player = (Player) go;
        int code = player.getDirection().getCode();
        setImage(images[code]);
    }
}