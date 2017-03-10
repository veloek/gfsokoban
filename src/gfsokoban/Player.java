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
        super(game, size, position, 1);

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
        g.fillRect(getPosition().x, getPosition().y,
                getSize().width, getSize().height);
    }

    private void doAnimate() {
        int deltaX = this.animateTo.x - this.animateFrom.x;
        int deltaY = this.animateTo.y - this.animateFrom.y;
        float timePerc = this.timeSinceLastUpdate / UPDATE_INTERVAL;

        if (timePerc < 1) {
            Point newPosition = new Point(
                    this.animateFrom.x + (int) (deltaX * timePerc),
                    this.animateFrom.y + (int) (deltaY * timePerc));

            setPosition(newPosition);
        } else {
            setPosition(this.animateTo);
            this.animating = false;
        }

    }

    private synchronized void tryMove() {
        boolean timeToUpdate = timeSinceLastUpdate >= UPDATE_INTERVAL;

        if (timeToUpdate && this.direction != null) {
            if (move(this.direction)) {
                timeSinceLastUpdate = 0;

                if (!this.finished)
                    getGame().reportMove();
            }
        }
    }

    @Override
    protected boolean move(Direction direction) {
        if (getGame().canMove(this, direction)) {
            animate(getPosition(), getPositionAfterMove(direction));
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
