/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.view.image;

import fr.ubx.poo.game.Direction;
import javafx.scene.image.Image;

import static fr.ubx.poo.view.image.ImageResource.*;

public final class ImageFactory {
    private final Image[] images;

    private final ImageResource[] directions = new ImageResource[]{
            // Direction { N, E, S, W }
            PLAYER_UP, PLAYER_RIGHT, PLAYER_DOWN, PLAYER_LEFT,
    };

    private final ImageResource[] directionsMonster = new ImageResource[]{
            // Direction { N, E, S, W }
            MONSTER_UP, MONSTER_RIGHT, MONSTER_DOWN, MONSTER_LEFT,
    };

    private final ImageResource[] digits = new ImageResource[]{
            DIGIT_0, DIGIT_1, DIGIT_2, DIGIT_3, DIGIT_4,
            DIGIT_5, DIGIT_6, DIGIT_7, DIGIT_8, DIGIT_9,
    };

    private ImageFactory() {
        images = new Image[ImageResource.values().length];
    }

    /**
     * Point d'accès pour l'instance unique du singleton
     */
    public static ImageFactory getInstance() {
        return Holder.instance;
    }

    private Image loadImage(String file) {
        return new Image(getClass().getResource("/images/" + file).toExternalForm());
    }

    public void load() {
        for (ImageResource img : ImageResource.values()) {
            images[img.ordinal()] = loadImage(img.getFileName());
        }
    }

    public Image get(ImageResource img) {
        return images[img.ordinal()];
    }

    public Image getDigit(int i) {
        if (i < 0 || i > 9)
            throw new IllegalArgumentException();
        return get(digits[i]);
    }

    //On recupere l'image du joueur en fonction de sa direction
    public Image getPlayer(Direction direction) {
        return get(directions[direction.ordinal()]);
    }

    //On recupere l'image de la bombe en fonction de combien de temps elle se trouve sur le monde. Ou pour faire plus
    //simple en fonction de combien de temps il reste pour que le timer soit egal a 0
    public Image getBomb(long timePassed){
        if (timePassed==3){
            return get(BOMB1);
        }
        if (timePassed==2){
            return get(BOMB2);
        }
        if (timePassed==1){
            return get(BOMB3);
        }
        if (timePassed==0){
            return get(BOMB4);
        }
        if (timePassed==4){
            return get(EXPLOSION);
        }
        else{
            return null;
        }
    }

    //On recupere l'image du monstre en fonction de sa direction
    public Image getMonster(Direction direction) {
        return get(directionsMonster[direction.ordinal()]);
    }
    /**
     * Holder
     */
    private static class Holder {
        /**
         * Instance unique non préinitialisée
         */
        private final static ImageFactory instance = new ImageFactory();
    }

}
