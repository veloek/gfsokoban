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
    private static final int STATUS_BAR_HEIGHT = 20;
    
    private final Dimension windowSize;
    private String[] levels;
    
    private int levelIndex = 0;
    private MapTile[][] level;
    private boolean restarting = false;
    private ArrayList<GameObject> gameObjects;
    private int moves;
    private int pushes;
    private float time;

    public Sokoban(Dimension windowSize, String[] levels) {
        this.windowSize = windowSize;
        this.levels = levels;

        loadLevel();
        init();
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
        this.restarting = true;
        this.gameObjects = new ArrayList<>();
        this.moves = 0;
        this.pushes = 0;
        this.time = 0;
        
        int width = this.windowSize.width / this.level[0].length;
        int height = (this.windowSize.height-STATUS_BAR_HEIGHT) / this.level.length;
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
        
        this.restarting = false;
    }

    public boolean canMove(GameObject go, Direction direction) {
        if (this.restarting)
            return false;
        
        Point pos = go.getPositionAfterMove(direction);
        return !gameObjects.stream()
                .filter(g -> g.isAtPosition(pos))
                .anyMatch(g -> g instanceof NPC && !((NPC)g).tryingToEnter(go, direction));
    }
    
    public void checkFinished() {
        if (this.restarting)
            return;
        
        if (this.gameObjects.stream()
                .filter(go -> go instanceof Brick)
                .allMatch(go -> ((Brick)go).isFinished())) {

            this.gameObjects.stream()
                    .filter(go -> go instanceof Player)
                    .forEach(p -> ((Player)p).setFinished(true));
            
            loadNextLevel();
        }
    }
    
    public void reportMove() {
        if (!this.restarting)
            this.moves++;
    }
    
    public void reportPush() {
        if (!this.restarting)
            this.pushes++;
    }
    
    private void loadNextLevel() {
        if (++this.levelIndex == this.levels.length) {
            this.levelIndex = 0;
        }

        loadLevel();
        init();
    }

    @Override
    public void onAction() {
        init();
    }

    @Override
    public void onAlternate() {
        loadNextLevel();
    }

    @Override
    public void onDirection(Direction direction) {
        if (!this.restarting) {
            this.gameObjects.stream()
                    .filter(go -> go instanceof DirectionChangedListener)
                    .forEach(dcl -> ((DirectionChangedListener)dcl)
                            .onDirectionChanged(direction));
        }
    }

    @Override
    public void update(float delta) {
        this.time += delta;
        
        if (!this.restarting)
            this.gameObjects.forEach(go -> go.update(delta));
    }

    @Override
    public void draw(Graphics g) {
        drawStatusBar(g);

        if (!this.restarting)
            this.gameObjects.forEach(go -> go.draw(g));
    }
    
    private void drawStatusBar(Graphics g) {
        StringBuilder statusString = new StringBuilder();
        statusString.append(padNumber(this.levelIndex+1, 2))
                .append(" | ")
                .append("moves:").append(padNumber(this.moves, 4))
                .append("          pushes:").append(padNumber(this.pushes, 4))
                .append("          time:").append(formatTime(this.time));
        
        g.setColor(Color.WHITE);
        g.fillRect(0, this.windowSize.height-STATUS_BAR_HEIGHT,
                this.windowSize.width, STATUS_BAR_HEIGHT);
        g.setColor(Color.BLACK);
        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, STATUS_BAR_HEIGHT-2));
        g.drawString(statusString.toString(), 5, this.windowSize.height-5);
    }
    
    private String padNumber(int number, int size) {
        String num = String.valueOf(number);
        String comp = String.valueOf((int)Math.pow(10, size-1));
        int lengthOfNum = num.length();
        int lengthOfComp = comp.length();
        
        for (int i=lengthOfNum; i<lengthOfComp; i++) {
            num = "0" + num;
        }
        
        return num;
    }
    
    private String formatTime(float elapsedTimeInSeconds) {
        int hours = (int) (elapsedTimeInSeconds / 3600);
        int minutes = (int) ((elapsedTimeInSeconds - hours*3600) / 60);
        int seconds = (int) (elapsedTimeInSeconds - hours*3600 - minutes*60);
        
        return hours + ":" + padNumber(minutes, 2) + ":" + padNumber(seconds, 2);
    }

}
