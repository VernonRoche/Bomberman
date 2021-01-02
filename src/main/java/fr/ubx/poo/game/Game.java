/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;


import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.model.go.character.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Game {

    private final World world;
    private final Player player;
    private final String worldPath;
    public int initPlayerLives;


    public Game(String worldPath) throws IOException{
        world=new World(loadWorldFromFile(1, worldPath));
        //world = new WorldStatic();
        this.worldPath = worldPath;
        loadConfig(worldPath);
        Position positionPlayer = null;
        List<Position> positionMonster = new ArrayList<>();
        try {
            positionPlayer = world.findPlayer();
            player = new Player(this, positionPlayer);
            positionMonster = world.findMonster();
            for(Position pos : positionMonster)
                world.getMonsterList().add(new Monster(this, pos));
        } catch (PositionNotFoundException e) {
            System.err.println("Position not found : " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    public int getInitPlayerLives() {
        return initPlayerLives;
    }

    private void loadConfig(String path) {
        try (InputStream input = new FileInputStream(new File(path, "config.properties"))) {
            Properties prop = new Properties();
            // load the configuration file
            prop.load(input);
            initPlayerLives = Integer.parseInt(prop.getProperty("lives", "3"));
        } catch (IOException ex) {
            System.err.println("Error loading configuration");
        }
    }

    public World getWorld() {
        return world;
    }

    public Player getPlayer() {
        return this.player;
    }

    public WorldEntity[][] loadWorldFromFile(int levelNumber, String path) throws IOException{
        Reader reader = new FileReader(path+"/level"+String.valueOf(levelNumber)+".txt");
        WorldEntity[][] mapEntities = new WorldEntity[12][12];

        try(BufferedReader bufferedReader=new BufferedReader(reader)){
            String line=bufferedReader.readLine();
            int lineNumber=0;
            while(line!=null){
                //on lit chaque ligne et on construit le tableau WorldEntity[][]
                System.out.println(line);
                for(int x=0; x<line.length(); x++) {
                    if (WorldEntity.fromCode(line.charAt(x)).isPresent())
                        mapEntities[lineNumber][x] = WorldEntity.fromCode(line.charAt(x)).get();
                }
                line=bufferedReader.readLine();
                lineNumber++;
            }
            return mapEntities;

        }catch (IOException e){
            System.out.println(e);
        }
        return mapEntities;
    }


}
