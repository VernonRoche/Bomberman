/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;

import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.Floor;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.model.go.character.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class World {
    private final Map<Position, Decor> grid;
    private final WorldEntity[][] raw;
    public final Dimension dimension;
    public boolean update = false; //
    private List<Bomb> placedBombs=new ArrayList<>();
    private List<Monster> monsterList=new ArrayList<>();
    private int levelNumber;

    public World(WorldEntity[][] raw) {
        this.raw = raw;
        dimension = new Dimension(raw.length, raw[0].length);
        grid = WorldBuilder.build(raw, dimension);
    }

    public int getLevelNumber(){ return this.levelNumber; }

    public void setLevelNumber(int number){ this.levelNumber=number; }

    public Position findPlayer() throws PositionNotFoundException {
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == WorldEntity.Player) {
                    return new Position(x, y);
                }
            }
        }
        throw new PositionNotFoundException("Player");
    }

    public List<Position> findMonster() throws PositionNotFoundException {
        List<Position> positions = new ArrayList<>();
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == WorldEntity.Monster) {
                    positions.add(new Position(x, y));
                }
            }
        }
        if(!positions.isEmpty()) return positions;
        throw new PositionNotFoundException("Monster");
    }

    public Decor get(Position position) {
        return grid.get(position);
    }

    public void set(Position position, Decor decor) {
        this.update=true;
        grid.put(position, decor);
    }

    public void clear(Position position) {
        this.update = true;
        grid.remove(position);
        set(position,new Floor());
    }

    public void forEach(BiConsumer<Position, Decor> fn) {
        grid.forEach(fn);
    }

    public Collection<Decor> values() {
        return grid.values();
    }

    public boolean isInside(Position position) {
        return true; // to update
    }

    public boolean isEmpty(Position position) {
        return grid.get(position) == null;
    }

    public void addBombWorld(Bomb bomb){
        placedBombs.add(bomb);
    }

    public List<Bomb> getPlacedBombs(){
        return placedBombs;
    }

    public List<Monster> getMonsterList(){
        return monsterList;
    }

    public void addMonster(Monster monster){ monsterList.add(monster); }

    public WorldEntity[][] getRaw() { return raw; }

    public Dimension getDimension() { return dimension; }


}
