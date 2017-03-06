/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gfsokoban.map;

import gfsokoban.Sokoban;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author veloek
 */
public class MapUtils {
    
    public static MapTile[][] parseMap(String[] map) throws MapFormatException {
        ArrayList<MapTile[]> tileRows = new ArrayList<>();
        
        int maxCols = 0;
        for (String row : map) {
            MapTile[] tileRow = MapUtils.parseMapLine(row);
            
            if (tileRow.length > maxCols)
                maxCols = tileRow.length;
            
            tileRows.add(tileRow);
        }
        
        // Add padding to center the map
        final int numRows = tileRows.size();
        final int numCols = maxCols;
        
        if (numCols > numRows) {
            int padding = (numCols - numRows) / 2;
            
            MapTile[] emptyRow = new MapTile[numCols];
            for (int i=0; i < numCols; i++) {
                emptyRow[i] = MapTile.BACKGROUND;
            }
            
            for (int i=0; i<padding; i++) {
                tileRows.add(0, emptyRow);
                tileRows.add(emptyRow);
            }
            
            if (((numCols - numRows) % 2) == 1)
                tileRows.add(emptyRow);
            
        } else {
            int padding = (numRows - numCols) / 2;
            
            ArrayList<MapTile[]> newTileRows = new ArrayList<>();
            
            tileRows.stream().forEach(tileRow -> {
                ArrayList<MapTile> newTileRow = new ArrayList<>(Arrays.asList(tileRow));
                
                for (int i=0; i<padding; i++) {
                    newTileRow.add(0, MapTile.BACKGROUND);
                    newTileRow.add(MapTile.BACKGROUND);
                }
                
                if (((numRows - numCols) % 2) == 1)
                    newTileRow.add(MapTile.BACKGROUND);
                
                newTileRows.add(newTileRow.toArray(new MapTile[newTileRow.size()]));
            });
            
            tileRows = newTileRows;
        }
        
        return tileRows.toArray(new MapTile[tileRows.size()][]);
    }

    private static MapTile[] parseMapLine(String line) throws MapFormatException {
        MapTile[] tiles = new MapTile[line.length()];
        int numTiles = line.length();
        
        for (int i=0; i<line.length(); i++) {
            char value = (numTiles > i) ? line.charAt(i) : ' ';
            tiles[i] = MapTile.fromValue(value);
        }
        
        return tiles;
    }
    
}
