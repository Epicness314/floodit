package flood;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * An attempt to clone the iPhone game Floodit
 */
public class Floodit {

	protected static final boolean DEBUG = false;
	//protected static final boolean DEBUG = false;

	private JFrame window = new JFrame("Flood It");
	private JPanel panel;
	private Grid grid;
	private int numMoves = 0;
	private JLabel numMovesLabel = new JLabel("0", JLabel.LEFT);
	private JMenu gameMenu;
	private JMenu helpMenu;

	private Canvas canvas = new Canvas() {
		@Override
		public void paint(Graphics g) {
			int squareWidth = getWidth() / grid.getWidth();
			int squareHeight = getHeight() / grid.getHeight();
			for (int i=0; i<grid.getWidth(); i++) {
				for (int j=0; j<grid.getWidth(); j++) {
					Square square = grid.get(i,j);
					boolean debugDot = DEBUG && grid.upperLeftGroupContains(square);
					square.paint(g, i * squareWidth, j * squareHeight,
							squareWidth, squareHeight, debugDot);
				}
			}
		}
	};

	public Floodit() {
		GridBagConstraints constraints;

		panel = new JPanel(new GridBagLayout());

		canvas.setSize(750, 750);

		addNumMovesLabel();

		constraints = new GridBagConstraints();
		constraints.gridx = 1;
		panel.add(canvas, constraints);

		addButtons();

		panel.setVisible(true);

		addMenuBar();
		window.setSize(1200, 900);
		window.add(panel);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);

		newGame();

		update();
	}

	private void addNumMovesLabel() {
		GridBagConstraints constraints;
		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		numMovesLabel.setSize(100, 100);
		numMovesLabel.setFont(new Font("Dialog", Font.BOLD, 34));
		panel.add(numMovesLabel, constraints);
	}

	private void addMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		gameMenu = new JMenu("Game");
		gameMenu.add("New");
		gameMenu.add("Undo");
		gameMenu.add("Redo");
		gameMenu.add("High scores");

		helpMenu = new JMenu("Help");
		helpMenu.add("Instructions");
		helpMenu.add("About");

		menuBar.add(gameMenu);
		menuBar.add(helpMenu);

		window.setJMenuBar(menuBar);
	}

	private void addButtons() {
		JPanel buttonPanel = new JPanel();
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		panel.add(buttonPanel, constraints);

		int i=0;
		for (final Color color : Square.colorsNames().keySet()) {
			Character name = Square.colorsNames().get(color);
			JButton button = new JButton(name.toString());
			button.setBackground(color);
			button.setMnemonic(name);

			button.setSize(50, 50);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					grid.changeUpperLeftGroupToColor(color);
					numMoves++;
					update();
				}
			});
			constraints = new GridBagConstraints();
			constraints.gridx = i++;
			constraints.weightx = 1;
			constraints.ipady = 10;
			buttonPanel.add(button, constraints);
		}
	}

	public void newGame(Dimension gridSize, int numColors) {
		grid = new Grid(gridSize, numColors);
		numMoves = 0;
		update();
	}

	public void newGame() {
		// TODO display a little window that lets you choose what kind of game you want
		// For now, just hard-code a default value.
		newGame(new Dimension(10, 10), 4);
	}

	public static void main(String[] args) {
		new Floodit();
	}

	public void update() {
		canvas.repaint();
		numMovesLabel.setText(Integer.toString(numMoves));

		if (grid.isAllSameColor()) {
			displayWinMessage();
			newGame();
		}
	}

	private void displayWinMessage() {
		JOptionPane.showMessageDialog(window, "You win!",
				"Congratulations", JOptionPane.PLAIN_MESSAGE);

	}
}
