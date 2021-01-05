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

    private List<World> worlds= new ArrayList<>();
    private World currentWorld;
    private final Player player;
    private final String worldPath;
    public int numberOfWorlds;
    public int initPlayerLives;
    private int currentLevel=1;
    public boolean hasRequestedLevelChange=false;
    private String levelPrefix;


    public Game(String worldPath) throws IOException{
        loadConfig(worldPath);
        currentWorld =new World(loadWorldFromFile(this.currentLevel, worldPath), this);
        currentWorld.setLevelNumber(currentLevel);
        worlds.add(currentWorld);
        //world = new WorldStatic();
        this.worldPath = worldPath;
        Position positionPlayer = null;
        try {
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

    private void loadConfig(String path) {
        try (InputStream input = new FileInputStream(new File(path, "config.properties"))) {
            Properties prop = new Properties();
            // load the configuration file
            prop.load(input);
            initPlayerLives = Integer.parseInt(prop.getProperty("lives", "3"));
            numberOfWorlds = Integer.parseInt(prop.getProperty("levels", "3"));
            levelPrefix =prop.getProperty("prefix", "level");
        } catch (IOException ex) {
            System.err.println("Error loading configuration");
        }
    }

    public World getCurrentWorld() {
        return currentWorld;
    }

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

    public WorldEntity[][] loadWorldFromFile(int levelNumber, String path) throws IOException{
        Reader reader = new FileReader(path+"/"+levelPrefix+levelNumber+".txt");
        //initialisation du tableau en fonction des lignes (0) et des lettres (1) du fichier
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
            return mapEntities;
        }catch (IOException e){
            System.out.println(e);
        }
        reader.close();
        return null;
    }

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
