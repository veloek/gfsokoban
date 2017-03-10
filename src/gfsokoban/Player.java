package gfsokoban;

import java.awt.*;
import gameframe.Direction;

public class Player extends GameObject implements DirectionChangedListener {
    private static final float UPDATE_INTERVAL = 0.2F;

    private float timeSinceLastUpdate;
    private Direction direction;
    private boolean finished = false;
    private boolean animating = false;
    private Point animateFrom;
    private Point animateTo;

    public Player(Sokoban game, Dimension size, Point position) {
        super(game, size, position);

        this.timeSinceLastUpdate = (float)UPDATE_INTERVAL;
    }

    @Override
    public void update(float delta) {
        timeSinceLastUpdate += delta;

        if (animating)
            doAnimate();
        else
            tryMove();
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(this.position.x, this.position.y,
                this.size.width, this.size.height);
    }

    private void doAnimate() {
        int deltaX = this.animateTo.x - this.animateFrom.x;
        int deltaY = this.animateTo.y - this.animateFrom.y;
        float timePerc = this.timeSinceLastUpdate / UPDATE_INTERVAL;

        if (timePerc < 1) {
            this.position.x += (int) (deltaX * timePerc);
            this.position.y += (int) (deltaY * timePerc);
        } else {
            this.position = this.animateTo;
            this.animating = false;
        }

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
    protected boolean move(Direction direction) {
        if (this.game.canMove(this, direction)) {
            animate(this.position, this.getPositionAfterMove(direction));
            return true;
        } else {
            return false;
        }
    }

    private void animate(Point from, Point to) {
        this.animating = true;
        this.animateFrom = from;
        this.animateTo = to;
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
