package myTetris;

import java.io.Serializable;
import java.util.Random;

public class Shape implements Serializable {
	
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 7613116876141336365L;

	enum Tetros {NoShape, ZShape, SShape, LineShape, TShape, SquareShape, LShape, MirroredLShape};
	
	private Tetros pieceShape;
	private int[][] coords;
	private int[][][] coordsTable;
	
	public Shape() {
		coords = new int[4][2];
		setShape(Tetros.NoShape);		
	}
	
	public void setShape(Tetros shape) {
		coordsTable = new int[][][] {
            { { 0, 0 },   { 0, 0 },   { 0, 0 },   { 0, 0 } },
            { { 0, -1 },  { 0, 0 },   { -1, 0 },  { -1, 1 } },
            { { 0, -1 },  { 0, 0 },   { 1, 0 },   { 1, 1 } },
            { { 0, -1 },  { 0, 0 },   { 0, 1 },   { 0, 2 } },
            { { -1, 0 },  { 0, 0 },   { 1, 0 },   { 0, 1 } },	
            { { 0, 0 },   { 1, 0 },   { 0, 1 },   { 1, 1 } },
            { { -1, -1 }, { 0, -1 },  { 0, 0 },   { 0, 1 } },
            { { 1, -1 },  { 0, -1 },  { 0, 0 },   { 0, 1 } }
        };
        
        for (int row = 0; row < 4; row++) {
        	for (int col = 0; col < 2; ++col){
        		coords[row][col] = coordsTable[shape.ordinal()][row][col];
        	}
        }        
        pieceShape = shape;
	}
	
	public void setRandomShape(){
		Random random = new Random();
		int x = Math.abs(random.nextInt()) % 7 + 1;
		Tetros[] values = Tetros.values();
		setShape(values[x]);		
	}
	
	public int minX() {
		int min = coords[0][0];		
		for (int i = 0; i < 4; i++) {
			min = Math.min(min, coords[i][0]);			
		}
		return min;		
	}
	
	public int minY() {
		int min = coords[0][1];
		for (int i = 0; i < 4; i++) {
			min = Math.min(min, coords[i][1]);
		}
		return min;
	}
	
	private void setX(int index, int x) {
		coords[index][0] = x;
	}
	
	private void setY(int index, int y) {
		coords[index][1] = y;
	}
	
	public int x(int index) {
		return coords[index][0];
	}
	
	public int y(int index) {
		return coords[index][1];
	}
	
	public Tetros getShape() {
		return pieceShape;
	}
	
	public Shape rotateRight() {
		if (pieceShape == Tetros.SquareShape) {
			return this;
		}
		
		Shape result = new Shape();
		result.pieceShape = pieceShape;		
		for (int i = 0; i < 4; i++) {
			result.setX(i, -y(i));
			result.setY(i, x(i));
		}
		return result;
	}
	
	public Shape rotateLeft() {
		if (pieceShape == Tetros.SquareShape) {
			return this;
		}
		
		Shape result = new Shape();
		result.pieceShape = pieceShape;
		for (int i = 0; i < 4; i++) {
			result.setX(i, y(i));
			result.setY(i, -x(i));
		}
		return result;
	}
}
