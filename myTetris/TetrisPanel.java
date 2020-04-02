package myTetris;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class TetrisPanel extends JPanel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2881989231023749732L;
	
	JLabel statusBar;
	Board board;
	transient File saveFile = new File("/home/nlilov/develop/workspace/"
			+ "tetris/src/myTetris/saveLoad/saveLoad.ser");
	
	public TetrisPanel() {
		setLayout(new BorderLayout());
		setBackground(Color.LIGHT_GRAY);
		statusBar = new JLabel("Score: 0");
		add(statusBar, BorderLayout.SOUTH);
		board = new Board(this);		
		add(board, BorderLayout.CENTER);		
	}
	
	public void startGame() {		
		board.start();
	}
	
	public void stopGame() {
		if (board != null) {
			board.stop();
			board = null;
		} else {
			return;
		}
	}
	

	public JLabel getStatusBar() {
	    return statusBar;
	}
	
	public void saveGame() {
			
		try {
			OutputStream os = new FileOutputStream(saveFile);
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(board);
			oos.close();
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	public void loadGame() {
		Board savedGame = new Board();
		
		try {			
			InputStream is = new FileInputStream(saveFile);
			ObjectInputStream ois = new ObjectInputStream(is);			
			savedGame = (Board) ois.readObject();
					
			board.curPiece = savedGame.curPiece;
			board.curRow = savedGame.curRow;
			board.curCol = savedGame.curCol;
			board.board = savedGame.board;
			board.numLinesRemoved = savedGame.numLinesRemoved;
			statusBar = savedGame.statusBar;	
			board.isStarted = savedGame.isStarted;
			board.isFallingFinished = savedGame.isFallingFinished;			
			
			board.pause();
			
			is.close();
			ois.close();			
		} catch (FileNotFoundException e) {		
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}		
	}
	
//	public void print(Board board) {
//		System.out.println(Arrays.toString(board.board));
//		System.out.println(board.curPiece);
//		System.out.println(board.numLinesRemoved);		
//		System.out.println(board.isStarted);
//		System.out.println(board.isPaused);
//		System.out.println(board.isFallingFinished);
//		
//	}

}
