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
public class Wall extends GameObject implements NPC {

    public Wall(Sokoban game, Dimension size, Point position) {
        super(game, size, position);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(getPosition().x, getPosition().y,
                getSize().width, getSize().height);
    }

    @Override
    public boolean tryingToEnter(GameObject object, Direction objectDirection) {
        return false; // Nothing can enter a wall... Doh!
    }

}
