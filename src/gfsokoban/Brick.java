/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gfsokoban;

import gameframe.Direction;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

/**
 *
 * @author veloek
 */
public class Brick extends GameObject implements NPC {

    private boolean finished;

    public Brick(Sokoban game, Dimension size, Point position) {
        super(game, size, position);
        
        this.finished = false;
    }

    @Override
    public void update(float delta) {
        
    }
    
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.drawRect(this.position.x, this.position.y,
                this.size.width, this.size.height);
    }

    @Override
    public boolean tryingToEnter(GameObject object, Direction objectDirection) {
        return object instanceof Player && this.move(objectDirection);
    }
    
    public boolean isFinished() {
        return this.finished;
    }

    public void setFinished(boolean b) {
        this.finished = true;
    }
    
}
