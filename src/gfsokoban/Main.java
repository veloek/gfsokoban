package gfsokoban;

import java.awt.*;
import gameframe.*;
import gameframe.api.*;
import java.util.ArrayList;

/**
 * Game entry point.
 *
 * This is where the game is set up. We run the update loop and redirect
 * input events.
 */
public class Main extends GFGame {
    private static final boolean DEBUG = false;

    private Sokoban game;

    public Main(Dimension windowSize) {
        super(windowSize);
        
        // TODO: Read state?
        ArrayList<String> levels = new ArrayList<>();
        for (int level=1; level <= 60; level++)
            levels.add("levels/level_" + level + ".txt");

        game = new Sokoban(windowSize, levels.toArray(new String[levels.size()]));
    }

    @Override
    public void update(float delta, Graphics g) {
        g.clearRect(0, 0, getSize().width, getSize().height);

        game.update(delta);
        game.draw(g);
    }

    @Override
    public void onAction() {
        game.onAction();
    }

    @Override
    public void onAlternate() {
        game.onAlternate();
    }

    @Override
    public void onDirection(Direction direction) {
        super.onDirection(direction);
        game.onDirection(direction);
    }

    public static void main(String args[]) {
        showTestFrame();
    }

    private static void showTestFrame() {
        Dimension testFrameSize = new Dimension(
                GFTestFrame.WIDTH,
                GFTestFrame.HEIGHT);
        Main sokoban = new Main(testFrameSize);
        GFTestFrame testFrame = new GFTestFrame(sokoban, DEBUG);
    }

}
