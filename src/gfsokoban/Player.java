package gfsokoban;

import java.awt.*;
import gameframe.Direction;

public class Player extends GameObject implements DirectionChangedListener {
    private static final float UPDATE_INTERVAL = 0.2F;

    private float timeSinceLastUpdate;
    private Direction direction;
    private boolean finished = false;

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
        g.fillRect(this.position.x, this.position.y,
                this.size.width, this.size.height);
    }

    private synchronized void tryMove() {
        boolean timeToUpdate = timeSinceLastUpdate >= UPDATE_INTERVAL;

        if (timeToUpdate && this.direction != null) {
            if (move(this.direction)) {
                timeSinceLastUpdate = 0;

                if (!this.finished)
                    game.reportMove();
            }
        }
    }

    @Override
    public void onDirectionChanged(Direction direction) {
        this.direction = direction;
        tryMove();
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

}
