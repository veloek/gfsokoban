package gfsokoban;

import java.awt.*;
import gameframe.Direction;

public class Player extends GameObject {
    private static final float UPDATE_INTERVAL = 0.2F;

    private float timeSinceLastUpdate;

    public Player(Sokoban game, Dimension size, Point position) {
        super(game, size, position);

        this.timeSinceLastUpdate = (float)UPDATE_INTERVAL;
    }

    @Override
    public void update(float delta) {
        timeSinceLastUpdate += delta;
        tryMove();
    }
    
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawRect(this.position.x, this.position.y,
                this.size.width, this.size.height);
    }
    
    private synchronized void tryMove() {
        boolean timeToUpdate = timeSinceLastUpdate >= UPDATE_INTERVAL;
        Direction direction = this.game.getDirection();
        
        if (timeToUpdate && direction != null) {
            if (move(direction)) {
                timeSinceLastUpdate = 0;
            }
        }
    }
    
    @Override
    public void onDirectionChanged() {
        tryMove();
    }

}
