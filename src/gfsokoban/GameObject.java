package gfsokoban;

import gameframe.Direction;
import java.awt.*;

/**
 * Common base class for all game objects.
 *
 * Holds sprite, size and position and knows how to draw itself.
 */
public abstract class GameObject implements Drawable, Updatable {
    private Sokoban game;
    private Dimension size;
    private Point position;
    private Image sprite;
    private int renderIndex;

    public GameObject(Sokoban game, Dimension size, Point position, Image sprite, int renderIndex) {
        this.game = game;
        this.size = size;
        this.position = position;
        this.sprite = sprite;
        this.renderIndex = renderIndex;
    }

    public GameObject(Sokoban game, Dimension size, Point position, int renderIndex) {
        this(game, size, position, null, renderIndex);
    }

    public GameObject(Sokoban game, Dimension size, Point position) {
        this(game, size, position, 0);
    }

    protected boolean move(Direction direction) {
        if (this.game.canMove(this, direction)) {
            this.position = this.getPositionAfterMove(direction);
            return true;
        } else {
            return false;
        }
    }

    public Point getPositionAfterMove(Direction direction) {
        Point updatedPosition = new Point(position.x, position.y);

        if (direction == null)
            return updatedPosition;

        switch (direction) {
            case LEFT:
                updatedPosition.x -= this.size.width;
                break;
            case RIGHT:
                updatedPosition.x += this.size.width;
                break;
            case UP:
                updatedPosition.y -= this.size.height;
                break;
            case DOWN:
                updatedPosition.y += this.size.height;
                break;
        }

        return updatedPosition;
    }

    public boolean isAtPosition(Point position) {
        return this.position.x == position.x && this.position.y == position.y;
    }

    @Override
    public void draw(Graphics g) {
        if (this.sprite != null) {
            g.drawImage(this.sprite, this.position.x, this.position.y,
                    this.size.width, this.size.height, null);
        } else {
            g.drawRect(this.position.x, this.position.y,
                    this.size.width, this.size.height);
        }
    }

    @Override
    public abstract void update(float delta);

    public Sokoban getGame() {
        return game;
    }

    public Dimension getSize() {
        return this.size;
    }

    public Point getPosition() {
        return this.position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public int getRenderIndex() {
        return renderIndex;
    }

}
