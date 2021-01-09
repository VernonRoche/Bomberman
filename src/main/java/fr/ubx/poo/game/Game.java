/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;


import fr.ubx.poo.model.go.character.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Game {

    private List<World> worlds= new ArrayList<>(); //Liste des mondes qu'on a visite
    private World currentWorld; //Le monde courant
    private final Player player;
    private final String worldPath; //Le path ou on trouve les fichiers des mondes et du config
    public int numberOfWorlds; //Le nombre max de mondes
    public int initPlayerLives; //Le montant des vies initiales du joueur
    private int currentLevel=1; //Le numero du monde courant, commencant avec 1
    public boolean hasRequestedLevelChange=false; //Verification si on veut changer de monde
    private String levelPrefix; //Prefixe des fichiers des mondes avant leurs numero, utilise pendant leurs chargement


    public Game(String worldPath) throws IOException{
        loadConfig(worldPath); //On charge les valeurs du fichier config.properties
        //On charge le monde depuis un fichier
        currentWorld =new World(loadWorldFromFile(this.currentLevel, worldPath), this, currentLevel);
        //On ajoute le monde dans la liste des mondes visites
        worlds.add(currentWorld);
        this.worldPath = worldPath;
        Position positionPlayer = null;
        try {
            //On cree le joueur
            positionPlayer = currentWorld.findPlayer();
            player = new Player(this, positionPlayer);
        } catch (PositionNotFoundException e) {
            System.err.println("Position not found : " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    public int getInitPlayerLives() {
        return initPlayerLives;
    }

    public String getWorldPath(){ return this.worldPath; }

    //Chargement des valeurs du config.properties
    private void loadConfig(String path) {
        try (InputStream input = new FileInputStream(new File(path, "config.properties"))) {
            Properties prop = new Properties();
            // load the configuration file
            prop.load(input);
            //Vies initiales
            initPlayerLives = Integer.parseInt(prop.getProperty("lives", "3"));
            //Nombre max des mondes
            numberOfWorlds = Integer.parseInt(prop.getProperty("levels", "3"));
            //Prefix des niveau avant l'affichage de leurs numero
            levelPrefix =prop.getProperty("prefix", "level");
        } catch (IOException ex) {
            System.err.println("Error loading configuration");
        }
    }

    public World getCurrentWorld() {
        return currentWorld;
    }

    public World getWorld(int i) { return this.worlds.get(i); }

    public void setCurrentWorld(World world){ this.currentWorld=world; }

    public List<World> getWorlds(){ return this.worlds; }

    public Player getPlayer() {
        return this.player;
    }

    public int getCurrentLevel(){
        return this.currentLevel;
    }

    public void setCurrentLevel(int level){
        this.currentLevel=level;
    }

    //Chargement d'un monde depuis son fichier
    public WorldEntity[][] loadWorldFromFile(int levelNumber, String path) throws IOException{
        Reader reader = new FileReader(path+"/"+levelPrefix+levelNumber+".txt");
        //initialisation du tableau de WorldEntity en fonction des lignes (0) et des lettres (1) du fichier
        int[] linesAndNumbers=getLinesAndWordsFile(levelNumber,path);
        WorldEntity[][] mapEntities = new WorldEntity[linesAndNumbers[0]][linesAndNumbers[1]];

        try(BufferedReader bufferedReader=new BufferedReader(reader)){
            String line=bufferedReader.readLine();
            int lineNumber=0;
            while(line!=null){
                //on lit chaque ligne et on construit le tableau WorldEntity[][]
                for(int x=0; x<line.length(); x++) {
                    if (WorldEntity.fromCode(line.charAt(x)).isPresent())
                        mapEntities[lineNumber][x] = WorldEntity.fromCode(line.charAt(x)).get();
                }
                line=bufferedReader.readLine();
                lineNumber++;
            }
            bufferedReader.close();
            //On retourne le tableau pour pouvoir construire le monde avec les WorldEntity converties
            return mapEntities;
        }catch (IOException e){
            System.out.println(e);
        }
        reader.close();
        return null;
    }

    //On recupere la taille en ligne et en colonnes (characteres) du fichier levelX.txt et on renvoie un tableau
    //avec 2 elements
    public int[] getLinesAndWordsFile(int levelNumber, String path) throws IOException{
        Reader reader = new FileReader(path+"/level"+String.valueOf(levelNumber)+".txt");
        try(BufferedReader bufferedReader=new BufferedReader(reader)) {
            String line = bufferedReader.readLine();
            int lineNumber = 0;
            int wordCount = 0;
            while (line != null) {
                if (lineNumber > 0) {

                } else {
                    for (int x = 0; x < line.length(); x++) {
                        wordCount++;
                    }

                }
                line= bufferedReader.readLine();
                lineNumber++;
            }
            int[] linesAndWords={lineNumber,wordCount};
            bufferedReader.close();
            return linesAndWords;
        }catch (IOException e){
            System.out.println(e);
        }
        reader.close();
        return null;
    }


}
