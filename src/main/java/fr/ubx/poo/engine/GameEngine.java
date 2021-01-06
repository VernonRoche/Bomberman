/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.engine;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.World;
import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.view.sprite.Sprite;
import fr.ubx.poo.view.sprite.SpriteFactory;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public final class GameEngine {

    private static AnimationTimer gameLoop;
    private final String windowTitle;
    private final Game game;
    private final Player player;
    private final List<Sprite> sprites = new ArrayList<>();
    private StatusBar statusBar;
    private Pane layer;
    private Input input;
    private Stage stage;
    private Sprite spritePlayer;
    private List<Sprite> spriteBombs= new ArrayList<>();

    private List<Sprite> spriteMonsterList = new ArrayList<>();

    public GameEngine(final String windowTitle, Game game, final Stage stage) {
        this.windowTitle = windowTitle;
        this.game = game;
        this.player = game.getPlayer();
        initialize(stage, game);
        buildAndSetGameLoop();
    }

    private void initialize(Stage stage, Game game) {
        this.stage = stage;
        Group root = new Group();
        layer = new Pane();

        int height = game.getCurrentWorld().dimension.height;
        int width = game.getCurrentWorld().dimension.width;
        int sceneWidth = width * Sprite.size;
        int sceneHeight = height * Sprite.size;

        Scene scene = new Scene(root, sceneWidth, sceneHeight + StatusBar.height);
        scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
        scene.setFill(Color.web("#DEB887"));

        stage.setTitle(windowTitle);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        input = new Input(scene);
        root.getChildren().add(layer);
        statusBar = new StatusBar(root, sceneWidth, sceneHeight, game);
        // Create decor sprites
        game.getCurrentWorld().forEach( (pos, d) -> sprites.add(SpriteFactory.createDecor(layer, pos, d)));
        spritePlayer = SpriteFactory.createPlayer(layer, player);
        for(Monster monster: game.getCurrentWorld().getMonsterList())
            spriteMonsterList.add(SpriteFactory.createMonster(layer, monster));
    }

    protected final void buildAndSetGameLoop() {
        gameLoop = new AnimationTimer() {
            public void handle(long now) {
                // Check keyboard actions
                processInput(now);

                // Do actions
                try {
                    update(now);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Graphic update
                render();
                statusBar.update(game);
            }
        };
    }

    private void processInput(long now) {
        if (input.isExit()) {
            gameLoop.stop();
            Platform.exit();
            System.exit(0);
        }
        if (input.isMoveDown()) {
            player.requestMove(Direction.S);
        }
        if (input.isMoveLeft()) {
            player.requestMove(Direction.W);
        }
        if (input.isMoveRight()) {
            player.requestMove(Direction.E);
        }
        if (input.isMoveUp()) {
            player.requestMove(Direction.N);
        }
        if (input.isBomb()){
            player.requestBomb();
        }
        if (input.isKey()){
            player.requestKey();
        }
        input.clear();
    }

    private void showMessage(String msg, Color color) {
        Text waitingForKey = new Text(msg);
        waitingForKey.setTextAlignment(TextAlignment.CENTER);
        waitingForKey.setFont(new Font(60));
        waitingForKey.setFill(color);
        StackPane root = new StackPane();
        root.getChildren().add(waitingForKey);
        Scene scene = new Scene(root, 400, 200, Color.WHITE);
        stage.setTitle(windowTitle);
        stage.setScene(scene);
        input = new Input(scene);
        stage.show();
        new AnimationTimer() {
            public void handle(long now) {
                processInput(now);
            }
        }.start();
    }


    private void update(long now) throws IOException {
        player.update(now);
        /*for(Monster monster: game.getWorld().getMonsterList()){
            if (monster.getLives()==0){
                game.getWorld().getMonsterList().remove(monster);
                spriteMonsterList.remove(monster);
            }
        }*/
        for(int x = 0; x<game.getCurrentWorld().getMonsterList().size(); x++){
            if (game.getCurrentWorld().getMonsterList().get(x).getLives()==0){
                game.getCurrentWorld().getMonsterList().remove(game.getCurrentWorld().getMonsterList().get(x));
                spriteMonsterList.remove(spriteMonsterList.get(x));
            }
        }

        for (World world: game.getWorlds()){
            for(Monster monster: world.getMonsterList()){
                monster.update(now, 1000);
                //System.out.println("MONSTER UPDATED");
                //System.out.println(monster.getPosition());
            }

        }

        for(Monster monster: game.getCurrentWorld().getMonsterList()){
            if(player.getPosition().x == monster.getPosition().x && player.getPosition().y == monster.getPosition().y){
                player.hurt();
            }
        }

        if(game.getCurrentWorld().update) {
            sprites.forEach(sprite -> sprite.remove());
            sprites.clear();
            game.getCurrentWorld().forEach( (pos, d) -> sprites.add(SpriteFactory.createDecor(layer, pos, d)));

            game.getCurrentWorld().update = false;
        }
        if (game.getCurrentWorld().getPlacedBombs().isEmpty()==false){
            //Ajouter les bombes manquantes dans les sprites
            if(game.getCurrentWorld().getPlacedBombs().size()!=spriteBombs.size()){
                for(int x = spriteBombs.size(); x<game.getCurrentWorld().getPlacedBombs().size(); x++){
                        spriteBombs.add(SpriteFactory.createBombSprite(layer,game.getCurrentWorld().getPlacedBombs().get(x)));
                }
            }
            //Resolution du timer des bombes
            for(int x = 0; x<game.getCurrentWorld().getPlacedBombs().size(); x++){
                //Explosion de bombe si now-le vieu now est egal au timer
                if(game.getCurrentWorld().getPlacedBombs().get(x).getTimePassed()>=game.getCurrentWorld().getPlacedBombs().get(x).getBombTimer()){
                    game.getCurrentWorld().getPlacedBombs().get(x).bombExplode(game.getCurrentWorld().getPlacedBombs().get(x).getPosition());
                    game.getCurrentWorld().getPlacedBombs().remove(x);
                    spriteBombs.remove(x);
                    //System.out.println(spriteBombs);
                }
                else{
                    game.getCurrentWorld().getPlacedBombs().get(x).setTimePassed(now/(1000000000)-game.getCurrentWorld().getPlacedBombs().get(x).getTimePlaced());

                }
            }
        }
        //on change de niveau
        if (game.hasRequestedLevelChange){
            int exitingLevelNumber=game.getCurrentWorld().getLevelNumber();
            if (game.getCurrentLevel()<game.getCurrentWorld().getLevelNumber()){//on retourne en arriere
                System.out.println("IM GETTING BACK");
                for (World world: game.getWorlds()){
                    if (world.getLevelNumber()==game.getCurrentLevel()){
                        game.setCurrentWorld(world);
                        showNextLevel(new Stage(), this.game, exitingLevelNumber);
                        game.hasRequestedLevelChange=false;
                    }
                }

            }
            else{ //on va au niveau suivant
                if (game.numberOfWorlds<game.getCurrentLevel()){} //on verifie si on depasse le nombre de mondes max
                else{
                    Boolean undiscoveredWorld=true;
                    for (World world: game.getWorlds()){ //on verifie si on a deja explore ce monde
                        if (game.getCurrentLevel()==world.getLevelNumber()){
                            undiscoveredWorld=false;
                            game.setCurrentWorld(world);
                            showNextLevel(new Stage(), this.game, exitingLevelNumber);
                            game.hasRequestedLevelChange=false;
                            break;
                        }
                    }
                    if (undiscoveredWorld){
                        game.setCurrentWorld(new World(game.loadWorldFromFile(game.getCurrentLevel(), game.getWorldPath()), game));
                        game.getCurrentWorld().setLevelNumber(game.getCurrentLevel());
                        game.getWorlds().add(game.getCurrentWorld());
                        showNextLevel(new Stage(), this.game, exitingLevelNumber);
                        game.hasRequestedLevelChange=false;
                    }
                }
            }
        }

        if (player.isAlive() == false) {
            gameLoop.stop();
            showMessage("Perdu!", Color.RED);
        }
        if (player.isWinner()) {
            gameLoop.stop();
            showMessage("Gagné", Color.BLUE);
        }
    }

    private void showNextLevel(Stage stage, Game game, int exitingLevelNumber){
        this.stage.close();
        this.stage=stage;
        this.spriteBombs.clear();
        this.sprites.clear();
        this.spriteMonsterList.clear();
        Group root = new Group();
        layer = new Pane();

        int height = game.getCurrentWorld().dimension.height;
        int width = game.getCurrentWorld().dimension.width;
        int sceneWidth = width * Sprite.size;
        int sceneHeight = height * Sprite.size;

        Scene scene = new Scene(root, sceneWidth, sceneHeight + StatusBar.height);
        scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
        scene.setFill(Color.web("#DEB887"));

        stage.setTitle(windowTitle);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        input = new Input(scene);
        root.getChildren().add(layer);
        statusBar = new StatusBar(root, sceneWidth, sceneHeight, game);
        // Create decor sprites
        game.getCurrentWorld().forEach( (pos, d) -> sprites.add(SpriteFactory.createDecor(layer, pos, d)));
        // Bouger le joueur a cote de la porte ouverte
        game.getCurrentWorld().forEach( ((position, decor) -> game.getPlayer().movePlayerNextToDoor(player, position, exitingLevelNumber)));
        spritePlayer = SpriteFactory.createPlayer(layer, player);
        for(Monster monster: game.getCurrentWorld().getMonsterList())
            spriteMonsterList.add(SpriteFactory.createMonster(layer, monster));
    }



    private void render() {
        sprites.forEach(Sprite::render);
        spriteBombs.forEach(Sprite::render);
        // last rendering to have player in the foreground
        for(Sprite spriteMons: spriteMonsterList)
            spriteMons.render();
        spritePlayer.render();
    }

    public void start() {
        gameLoop.start();
    }
}
