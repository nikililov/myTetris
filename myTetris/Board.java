package myTetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.Serializable;


import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import myTetris.Shape.Tetros;

public class Board extends JPanel implements Serializable {	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5634733239048330290L;
	
	final int boardRows = 22;
	final int boardColomns = 10;
	
	Shape curPiece;
	Tetros[][] board;
	JLabel statusBar;
	int curRow = 0;
	int curCol = 0;
	Timer timer;
	boolean isStarted;
	boolean isFallingFinished;
	boolean isPaused;
	int numLinesRemoved;
	
	public Board() {
		
	}
	
	public Board(TetrisPanel parent) {
		setFocusable(true);
		setBackground(Color.BLACK);
		curPiece = new Shape();
		statusBar = parent.getStatusBar();		
		isStarted = false;
		isFallingFinished = false;
		isPaused = false;
		numLinesRemoved = 0;
		board = new Tetros[boardRows][boardColomns];
		timer = new Timer(400, new TimerListener());		
		addKeyListener(new KSet());
		clearBoard();
	}
	
	public void start() {
		if (isPaused) {
			return;
		} else {			
			isStarted = true;
			isFallingFinished = false;
			numLinesRemoved = 0;
			clearBoard();
			newPiece();			
			timer.start();
		}		
	}
	
	public void stop() {
		timer.stop();
		isStarted = false;
		clearBoard();
	}
	
	public class TimerListener implements ActionListener {
	@Override
		public void actionPerformed(ActionEvent e) {
			if (isFallingFinished) {
				isFallingFinished = false;
				newPiece();
			} else {
				oneLineDown();
			}	
		}
	}
	
	Tetros shapeAt(int row, int col) {
		if ((row >= 0 || row <= 22) && (col >= 0 || col <= 10)) {
			return board[row][col];
		}
		else {
			return null;
		}
	}
	
	int squareWidth() {	
		return (int) getSize().getWidth() / boardColomns;
	}
	
	int squareHeight() {
		return (int) getSize().getHeight() / boardRows;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Dimension size = getSize();		
		int boardTop = (int) size.getHeight() - boardRows * squareHeight();
				
		for (int row = 0; row < boardRows; row++) {
			for (int col = 0; col < boardColomns; col++) {
				
				Tetros shape = shapeAt(boardRows - row - 1, col);				
				if (shape != Tetros.NoShape) {
					drawSquare(g, 0 + col * squareWidth(), boardTop + row * squareHeight(), shape);
				}
			}
		}
		
		if (curPiece.getShape() != Tetros.NoShape) {
			for (int i = 0; i < 4; i++) {
				int row = curRow - curPiece.y(i);
				int col = curCol + curPiece.x(i);				
				drawSquare(g, 0 + col * squareWidth(), boardTop + (boardRows - row - 1) * squareHeight(), curPiece.getShape());
			}
		}
		
	}
	
	private void drawSquare(Graphics g, int col, int row, Tetros shape) {
		Color colors[] = { new Color(0, 0, 0), new Color(204, 102, 102), 
				new Color(102, 204, 102), new Color(102, 102, 204),
				new Color(204, 204, 102), new Color(204, 102, 204),
				new Color(102, 204, 204), new Color(218, 170, 0)};
		
		Color color = colors[shape.ordinal()];
		
		g.setColor(color);
		g.fillRect(col + 1, row + 1, squareWidth() - 2, squareHeight() - 2);
		
		g.setColor(color.brighter());
		//vertical left
		g.drawLine(col, row + squareHeight() - 1, col, row);
		//horizontal top
		g.drawLine(col, row, col + squareWidth() - 1, row);
		
		g.setColor(color.darker());
		//horizontal down
		g.drawLine(col + 1, row + squareHeight() - 1, col + squareWidth() - 1, row + squareHeight() - 1);
		//vertical right
		g.drawLine(col + squareWidth() - 1, row + squareHeight() - 1, col + squareWidth() - 1, row + 1);
	}
	
	public void pause() {
		if (!isStarted) {
			return;
		} else {
			isPaused = !isPaused;
			if (isPaused) {
				timer.stop();
				statusBar.setText("Paused");
			} else {
				timer.start();
				statusBar.setText("Score: " + String.valueOf(numLinesRemoved));
			}
		}
		repaint();
	}
	
	private void oneLineDown() {
		if (!tryMove(curPiece, curCol, curRow - 1)) {
			pieceDropped();
		}
	}
	
	private boolean tryMove(Shape newPiece, int newCol, int newRow) {
		for (int i = 0; i < 4; i++) {			
			int col = newCol + newPiece.x(i);
			int row = newRow - newPiece.y(i);
						
			if (col < 0 || col >= boardColomns || row < 0 || row >= boardRows) {
				return false;
			} else if (shapeAt(row, col) != Tetros.NoShape) {
				return false;
			}
		}	
			curPiece = newPiece;
			curRow = newRow;
			curCol = newCol;
			repaint();
			return true;		
	}
	
	private void pieceDropped() {
		for (int i = 0; i < 4; i++) {			
			int col = curCol + curPiece.x(i);
			int row = curRow - curPiece.y(i);
			board[row][col] = curPiece.getShape();
		}
		
		removeFullLines();
		
		if (!isFallingFinished) {
			newPiece();
		}
	}
	
	private void removeFullLines() {
		int numFullLines = 0;
		
		for (int row = boardRows - 1; row >= 0; row--) {
			boolean lineIsFull = true;
			
			for (int col = 0; col < boardColomns; col++) {
				if (shapeAt(row, col) == Tetros.NoShape) {
					lineIsFull = false;
					break;
				}
			}
			
			if (lineIsFull) {
				numFullLines++;
				for (int k = row; k < boardRows - 1; k++) {
					for (int col = 0; col < boardColomns - 1; col++) {
						board[k][col] = shapeAt(k + 1, col);
					}
				}
			}
		}
		
		if (numFullLines > 0) {
			numLinesRemoved += numFullLines;
			statusBar.setText("Score: " + String.valueOf(numLinesRemoved));
			isFallingFinished = true;
			curPiece.setShape(Tetros.NoShape);
			repaint();
		}
	}
	
	private void dropDown() {
		int newRow = curRow;
		while(newRow > 0) {
			if (!tryMove(curPiece, curCol, curRow - 1)) {
				break;				
			}
			newRow--;
		}
		pieceDropped();
	}
	
	private void clearBoard() {
		for (int row = 0; row < board.length; row++) {			
			for (int col = 0; col < board[0].length; col++) {				
				board[row][col] = Tetros.NoShape;
			}
		}
	}
	
	public void newPiece() {
		curPiece.setRandomShape();
		curCol = boardColomns / 2 + 1;		
		curRow = boardRows - 1 + curPiece.minY();
		
		if (!tryMove(curPiece, curCol, curRow)) {
			curPiece.setShape(Tetros.NoShape);
			timer.stop();
			isStarted = false;
			JOptionPane.showMessageDialog(null, "Game Over", "Game",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	class KSet extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			if (!isStarted || curPiece.getShape() == Tetros.NoShape) {
				return;
			} 
			
			int keyCode = e.getKeyCode();				
			if (keyCode == 'p'|| keyCode == 'P') {
					pause();
					return;
			} else if (isPaused) {
				return;
			}
			
			switch (keyCode) {
			case KeyEvent.VK_LEFT:
				tryMove(curPiece, curCol - 1, curRow);
				break;
			case KeyEvent.VK_RIGHT:
				tryMove(curPiece, curCol + 1, curRow);
				break;
			case KeyEvent.VK_DOWN:
				tryMove(curPiece.rotateRight(), curCol, curRow);
				break;
			case KeyEvent.VK_UP:
				tryMove(curPiece.rotateLeft(), curCol, curRow);
				break;
			case KeyEvent.VK_SPACE:
				dropDown();
				break;
			case 'd':
				oneLineDown();
				break;
			case 'D':
				oneLineDown();
				break;
			}
		}
	}
	
	public Board getBoard() {
		return this;
	}
}
