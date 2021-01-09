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
    private List<Sprite> spriteBombs= new ArrayList<>(); //liste qui contient les sprites des bombes
    private List<Sprite> spriteMonsterList = new ArrayList<>(); //liste qui contient les sprites des monstres

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
        //On regarde si un monstre est mort et on l'efface de la liste des monstres du monde courant et de la liste des
        //sprites
        for(int x = 0; x<game.getCurrentWorld().getMonsterList().size(); x++){
            if (game.getCurrentWorld().getMonsterList().get(x).getLives()==0){
                game.getCurrentWorld().getMonsterList().remove(game.getCurrentWorld().getMonsterList().get(x));
                spriteMonsterList.remove(spriteMonsterList.get(x));
            }
        }

        //On parcoure tout les mondes, pour recuperer tout les monstres et les faire bouger
        for(World world: game.getWorlds()){
            for(Monster monster: world.getMonsterList()){
                monster.update(now, 1000);
            }
        }

        //On verifie pour tout les monstres du monde courant si elles sont en train d'attaquer le joueur et on lui
        //enleve un point de vie
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
        //Si on a pose une bombe, on verifie quelques conditions
        if (game.getCurrentWorld().getPlacedBombs().isEmpty()==false){
            //Si on a place une bombe et le sprite n'est pas apparu, on l'ajoute dans la liste des sprites
            if(game.getCurrentWorld().getPlacedBombs().size()!=spriteBombs.size()){
                for(int x = spriteBombs.size(); x<game.getCurrentWorld().getPlacedBombs().size(); x++){
                        spriteBombs.add(SpriteFactory.createBombSprite(layer,game.getCurrentWorld().getPlacedBombs().get(x)));
                }
            }
            //Resolution du timer des bombes
            for(int x = 0; x<game.getCurrentWorld().getPlacedBombs().size(); x++){
                //Explosion de bombe si now moins le vieu now est egal au timer
                if(game.getCurrentWorld().getPlacedBombs().get(x).getTimePassed()>=game.getCurrentWorld().getPlacedBombs().get(x).getBombTimer()){
                    game.getCurrentWorld().getPlacedBombs().get(x).bombExplode(game.getCurrentWorld().getPlacedBombs().get(x).getPosition());
                    game.getCurrentWorld().getPlacedBombs().remove(x);
                    spriteBombs.remove(x);
                    game.getPlayer().setNb_bomb(game.getPlayer().getNb_bomb() + 1);
                }
                else{
                    game.getCurrentWorld().getPlacedBombs().get(x).setTimePassed(now/(1000000000)-game.getCurrentWorld().getPlacedBombs().get(x).getTimePlaced());
                }
            }
        }
        //On verifie si une bombe en fait exploser une autre, et on la fait exploser ensuite
        for (int x = 0; x<game.getCurrentWorld().getPlacedBombs().size(); x++){
            if(game.getCurrentWorld().getPlacedBombs().get(x).getLives() == 0){
                game.getCurrentWorld().getPlacedBombs().get(x).bombExplode(game.getCurrentWorld().getPlacedBombs().get(x).getPosition());
                game.getPlayer().setNb_bomb(game.getPlayer().getNb_bomb() + 1);
                game.getCurrentWorld().getPlacedBombs().remove(game.getCurrentWorld().getPlacedBombs().get(x));
                spriteBombs.remove(spriteBombs.get(x));
            }
        }

        //on change de niveau
        if (game.hasRequestedLevelChange){
            int exitingLevelNumber=game.getCurrentWorld().getLevelNumber();
            //Si la condition est vraie, on retourne au niveau precedent
            if (game.getCurrentLevel()<game.getCurrentWorld().getLevelNumber()){
                System.out.println("IM GETTING BACK");
                for (World world: game.getWorlds()){
                    if (world.getLevelNumber()==game.getCurrentLevel()){
                        game.setCurrentWorld(world);
                        showNextLevel(new Stage(), this.game, exitingLevelNumber);
                        game.hasRequestedLevelChange=false;
                    }
                }

            }
            //Sinon on va au niveau suivant
            else{
                //on verifie si on depasse le nombre de mondes max
                if (game.numberOfWorlds<game.getCurrentLevel()){}
                else{
                    Boolean undiscoveredWorld=true;
                    //On verifie si on a deja explore ce monde et on le charge comme monde courant, et ensuite on
                    //l'affiche avec showNextLevel
                    for (World world: game.getWorlds()){
                        if (game.getCurrentLevel()==world.getLevelNumber()){
                            undiscoveredWorld=false;
                            game.setCurrentWorld(world);
                            showNextLevel(new Stage(), this.game, exitingLevelNumber);
                            game.hasRequestedLevelChange=false;
                            break;
                        }
                    }
                    //Si on est jamais venu dans ce monde, on l'ajoute dans la liste des mondes et on l'affiche
                    if (undiscoveredWorld){
                        game.setCurrentWorld(new World(game.loadWorldFromFile(game.getCurrentLevel(), game.getWorldPath()), game, game.getCurrentLevel()));
                        game.getWorlds().add(game.getCurrentWorld());
                        showNextLevel(new Stage(), this.game, exitingLevelNumber);
                        game.hasRequestedLevelChange=false;
                    }
                }
            }
        }

        //On verifie si le joueur est mort (Points de vie=0)
        if (player.isAlive() == false) {
            gameLoop.stop();
            showMessage("Perdu!", Color.RED);
        }
        //On verifie si on a gagne et on affiche un message (quand on marche sur la princesse)
        if (player.isWinner()) {
            gameLoop.stop();
            showMessage("Gagné", Color.BLUE);
        }
    }

    //Ici on change de stage, et on affiche une nouvelle fenetre, dependant du world courant
    private void showNextLevel(Stage stage, Game game, int exitingLevelNumber){
        //On efface les elements des listes des sprites, on ferme le stage et on ouvre un autre
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
        //On change la couleur de fond
        scene.setFill(Color.web("#DEB887"));

        stage.setTitle(windowTitle);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        //On ajoute tout les elements de sprites
        input = new Input(scene);
        root.getChildren().add(layer);
        statusBar = new StatusBar(root, sceneWidth, sceneHeight, game);
        // Create decor sprites
        game.getCurrentWorld().forEach( (pos, d) -> sprites.add(SpriteFactory.createDecor(layer, pos, d)));
        // Bouger le joueur a cote de la porte ouverte, dependant si on va a un niveau precedent ou suivant
        game.getCurrentWorld().forEach( ((position, decor) -> game.getPlayer().movePlayerNextToDoor(player, position, exitingLevelNumber)));
        spritePlayer = SpriteFactory.createPlayer(layer, player);
        for(Monster monster: game.getCurrentWorld().getMonsterList())
            spriteMonsterList.add(SpriteFactory.createMonster(layer, monster));
    }


    //On affiche d'abord les decors, puis les bombes, suivi des monstres et du joueur
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
