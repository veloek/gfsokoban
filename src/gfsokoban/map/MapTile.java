/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gfsokoban.map;

/**
 *
 * @author veloek
 */
public enum MapTile {
    WALL('X'),
    PLAYER('@'),
    BRICK('*'),
    TARGET('.'),
    BACKGROUND(' ');
    
    private final char value;
    
    private MapTile(char value) {
        this.value = value;
    }
    
    public char getValue() {
        return value;
    }
    
    public static MapTile fromValue(char value) {
        switch (value) {
            case 'X':
                return WALL;
            case '@':
                return PLAYER;
            case '*':
                return BRICK;
            case '.':
                return TARGET;
            case ' ':
            default:
                return BACKGROUND;
        }
    }
}
