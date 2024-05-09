package model.factories;

import model.Cell;
import java.awt.Point;

public class CellFactory {
    public Cell createCell(Point position){
        return new Cell(position);
    }
}
