package myTetris;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;


public class Tetris {

	public static void main(String[] args) {
			
		Game game = new Game();
		game.gameStart();
	}
}

class Game extends JFrame {
				
	/**
	 * 
	 */
	private static final long serialVersionUID = -8319779086553720222L;

	void gameStart() {
		JFrame frame = new JFrame("Tetris Game");
		frame.setSize(226, 453);
		ImageIcon frameImg = new ImageIcon("/home/nlilov/develop/workspace/"
				+ "tetris/src/myTetris/images/frame.png");
		frame.setIconImage(frameImg.getImage());		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new TetrisPanel();		
		frame.add(panel);
		
		JMenuBar menuBar = new JMenuBar();
		ImageIcon icon = new ImageIcon("/home/nlilov/develop/workspace/"
				+ "tetris/src/myTetris/images/myGame.png");
		
		JMenu game = new JMenu("Game");
		JMenuItem startMenuItem = new JMenuItem("New Game", icon);
		startMenuItem.setToolTipText("Start New Game!");
		startMenuItem.addActionListener((ActionEvent e) -> {			
			((TetrisPanel) panel).startGame();
		});
		
		ImageIcon saveIcon = new ImageIcon("/home/nlilov/develop/workspace/"
				+ "tetris/src/myTetris/images/saveGame.png");
		JMenuItem saveMenuItem = new JMenuItem("Save Game", saveIcon);
		saveMenuItem.setToolTipText("Save Game!");
		saveMenuItem.addActionListener((ActionEvent e) -> {
			System.out.println("Saving..... ");
			((TetrisPanel) panel).saveGame();
		});
		
		ImageIcon loadIcon = new ImageIcon("/home/nlilov/develop/workspace/"
				+ "tetris/src/myTetris/images/loadGame.png");
		JMenuItem loadMenuItem = new JMenuItem("Load Game", loadIcon);
		loadMenuItem.setToolTipText("Load Game!");
		loadMenuItem.addActionListener((ActionEvent e) -> {
			System.out.println("Loading..... ");			
			((TetrisPanel) panel).loadGame();		
		});
		
		ImageIcon exitIcon = new ImageIcon("/home/nlilov/develop/workspace/"
				+ "tetris/src/myTetris/images/Tetris.png");
		JMenuItem exitMenuItem = new JMenuItem("Exit", exitIcon);
		exitMenuItem.setToolTipText("Exit Game!");
		exitMenuItem.addActionListener((ActionEvent e) -> {
			System.exit(0);
		});
		
		game.add(startMenuItem);
		game.add(saveMenuItem);
		game.add(loadMenuItem);
		game.add(exitMenuItem);
		menuBar.add(game);
		frame.setJMenuBar(menuBar);
		
		frame.setLocationRelativeTo(null);		
		frame.setVisible(true);
	}
}
