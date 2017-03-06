package gfsokoban;

import java.util.*;
import java.awt.*;
import gameframe.Direction;
import gameframe.api.GFInputListener;
import gfsokoban.map.MapFormatException;
import gfsokoban.map.MapTile;
import gfsokoban.map.MapUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Sokoban implements GFInputListener, Updatable, Drawable {
    private final Dimension windowSize;
    private String[] levels;
    
    private int levelIndex = 0;
    private MapTile[][] level;
    private ArrayList<GameObject> gameObjects;
    private Direction direction;
    private boolean updatingGameObjects = false;

    public Sokoban(Dimension windowSize, String[] levels) {
        this.windowSize = windowSize;
        this.levels = levels;

        this.loadLevel();
        this.init();
    }
    
    // Load level from file
    private void loadLevel() {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream(
                        this.levels[levelIndex])))) {
            
            String line;
            ArrayList<String> input = new ArrayList<>();
            
            while ((line = br.readLine()) != null) {
                input.add(line);
            }
            
            this.level = MapUtils.parseMap(input.toArray(new String[input.size()]));

        } catch (IOException | MapFormatException e) {
            System.err.println("Error while loading map: " + e.getMessage());
            System.exit(1);
        }
    }
    
    // Remove current game objects and create new from level definition
    private void init() {
        this.updatingGameObjects = true;
        this.gameObjects = new ArrayList<>();
        
        int width = this.windowSize.width / this.level[0].length;
        int height = this.windowSize.height / this.level.length;
        Dimension cellSize = new Dimension(width, height);
        
        for (int row=0; row<this.level.length; row++) {
            MapTile[] mapRow = this.level[row];

            for (int cell=0; cell<mapRow.length; cell++) {
                MapTile tile = mapRow[cell];
                Point position = new Point(cell*cellSize.width, row*cellSize.height);

                switch (tile) {
                    case WALL:
                        this.gameObjects.add(new Wall(this, cellSize, position));
                        break;
                    case PLAYER:
                        this.gameObjects.add(new Player(this, cellSize, position));
                        break;
                    case BRICK:
                        this.gameObjects.add(new Brick(this, cellSize, position));
                        break;
                    case TARGET:
                        this.gameObjects.add(new Target(this, cellSize, position));
                        break;
                    default:
                }
            }
        }
        
        this.updatingGameObjects = false;
    }

    // Check boundries for movement
    public boolean canMove(GameObject go, Direction direction) {
        if (this.updatingGameObjects)
            return false;
        
        Point pos = go.getPositionAfterMove(direction);
        return !gameObjects.stream()
                .filter(g -> g.isAtPosition(pos))
                .anyMatch(g -> g instanceof NPC && !((NPC)g).tryingToEnter(go, direction));
    }
    
    public void checkFinished() {
        if (this.updatingGameObjects)
            return;
        
        if (this.gameObjects.stream()
                .filter(go -> go instanceof Brick)
                .allMatch(go -> ((Brick)go).isFinished())) {

            this.loadNextLevel();
        }
    }
    
    private void loadNextLevel() {
        if (++this.levelIndex == this.levels.length) {
            this.levelIndex = 0;
        }

        this.loadLevel();
        this.init();
    }

    @Override
    public void onAction() {
        this.init();
    }

    @Override
    public void onAlternate() {
        this.loadNextLevel();
    }

    @Override
    public void onDirection(Direction direction) {
        this.direction = direction;
        
        if (!this.updatingGameObjects)
            this.gameObjects.forEach(go -> go.onDirectionChanged());
    }

    @Override
    public void update(float delta) {
        if (!this.updatingGameObjects)
            this.gameObjects.forEach(go -> go.update(delta));
    }

    @Override
    public void draw(Graphics g) {
        g.drawString("Level " + (this.levelIndex+1) + "/" + this.levels.length, 5, this.windowSize.height-5);

        if (!this.updatingGameObjects)
            this.gameObjects.forEach(go -> go.draw(g));
    }

    public Direction getDirection() {
        return this.direction;
    }

}
