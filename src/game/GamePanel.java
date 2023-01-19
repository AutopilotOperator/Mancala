package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.Timer;

public class GamePanel extends JPanel {
	private Hole smallHoles[]; // Array that contains the 12 small playable holes
	private JPanel smalls; // a jpanel that contains all the small holes (12 in total)
	private JPanel field; // a jpanel that contains all the components that represent the mancala board
	private JPanel dashboard; // a jpanel that contains additional info in the bottom part of the screen
	private BigHole bigHoles[]; // an array to contain the 2 big holes ("home holes")
	private Player currentP; // An enum that represents a player
	private JLabel curName; // A label that indicates which player plays right now
	private JLabel message; // A changing message in the bottom dashboard for the human player
	private JLabel turnNumLabel;// Counter for the current turn
	private static int turnCount = 1; // turn counter
	private DashboardButton resetButton;

	public GamePanel() { // Contains the holes

		createBoard();

	}
	private void createBoard() {  //Handles the initiation of the game graphics
		
		this.setSize(900, 200);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		field = new JPanel();
		field.setSize(800, 200);
		field.setLayout(new GridBagLayout());
		field.setBackground(Color.DARK_GRAY);
		
		bigHoles = new BigHole[2];
		for (int i = 0; i < 2; i++) {
			bigHoles[i] = new BigHole(this);

		}
		createHoles();
		addDashboard();
		field.setVisible(true);
		smalls.setVisible(true);
	}
	private boolean addBigHole(JLabel bigButton) { //Adds the big holes to the field
		field.add(bigButton); 
		return true;
	}
	private boolean addDashboard() {
		currentP = Player.Player1;
		curName = new JLabel("Player1");
		message = new JLabel();
		turnNumLabel = new JLabel();
		turnNumLabel.setText("Turn-" + (turnCount));

		dashboard = new JPanel();
		GridLayout dash = new GridLayout(); // organizes the dashboard in a grid type layout

		dashboard.setLayout(dash);
		dashboard.setSize(800, 100);
		dashboard.setVisible(true);
		this.add(dashboard);

		dashboard.add(curName);
		dashboard.add(message);
		dashboard.add(turnNumLabel);
		resetButton = new DashboardButton(this, ButtonType.Reset);
		dashboard.add(resetButton.getButton());
		return true;
	}
	
	private boolean createHoles() { //Creates the holes for the field
		JButton tempHoleB;
		addBigHole(bigHoles[0].getLabel()); // adds the first big hole ("home hole")
	
		smalls = new JPanel(); // contains the small holes
		smalls.setSize(600, 200);
		smalls.setLayout(new GridLayout(2, 6, 0, 0));
//		smalls.setBackground(Color.gray);
		field.add(smalls);
		this.add(field);
		smallHoles = new Hole[12];

		// 2 Loops to generate the small holes
		// I used 2 loops t make the orientation of the holes counterclockwise
		// Example- 11 10 9 8 7 6
		// 0 1 2 3 4 5

		for (int i = 11; i >= 6; i--) {
			smallHoles[i] = new Hole(this, i);
			tempHoleB = smallHoles[i].getButton();
			smalls.add(tempHoleB);
		}
		for (int i = 0; i <= 5; i++) {
			smallHoles[i] = new Hole(this, i);
			tempHoleB = smallHoles[i].getButton();
			smalls.add(tempHoleB);
		}

		addBigHole(bigHoles[1].getLabel()); // adds the second big hole ("home hole")

		return true;
	}
	

	public boolean holePressed(int position) { // Handle the pressed hole, returns false if the player picked the wrong
												// row

		if ((position <= 5 && currentP == Player.Player1) || (position >= 6 && currentP == Player.Player2))
		// Checks if the right player clicked at the right row
		// Player 1-lower row
		// Player 2-higher row
		{

			int tempStones = smallHoles[position].getStones();
			System.out.println("Stones-"+tempStones);
			smallHoles[position].updateCount(0);
			moveStones(position, tempStones);
			checkEndGame();
			return true;
		}
		return false;
	}

	public boolean dashboardPress(ButtonType buttonType) { // Handles each dashboard button click
		switch (buttonType) {
		case Reset: //Resets the field to the starting state
			for(int i=0;i<12;i++)
				smallHoles[i].updateCount(4);
			bigHoles[0].resetHole();
			bigHoles[1].resetHole();
			currentP=Player.Player1;
			message.setText("");
			turnCount=1;
			curName.setText("Player 1");
			turnNumLabel.setText("Turn-" + (turnCount));
			break;
		case TurnBack:
			break;
		default:
		}
		return true;
	}

	public boolean moveStones(int pos, int stones) {
		boolean extraTurn = false;
		// A delay to create some sort of animation

		for (int i = 0; i < stones; i++) {
			pos++;

			// Check for scoring

			if (pos == 6 && currentP == Player.Player1) { // scoring for player 1
				bigHoles[1].incrementStones();
				stones--; // Count the big hole
				if (i == stones) {// limit case when the last stone lands on home stone
					extraTurn = true;
					System.out.println("Player 1 gets an extra turn!");
					message.setText("Extra turn!");
					break;
				}
			}
			if (pos == 12) { // Just skip if its player 1
				pos = 0;
				if (currentP == Player.Player2) { // return the start if pos got the end
					bigHoles[0].incrementStones();
					stones--; // Count the big hole
					if (i == stones) {// limit case when the last stone lands on home stone
						extraTurn = true;
						System.out.println("Player 2 gets an extra turn!");
						message.setText("Extra turn!");

						break;
					}
				}
			}

			smallHoles[pos].updateCount(smallHoles[pos].getStones() + 1);
			if (i + 1 == stones)// Check if its the last iteration
				checkSteal(pos);

		}
		if (!extraTurn) { // If the player doesn't get an extra turn (Getting an extra turn when the last
							// stone lands in the home hole)
			nextTurn();
		} else { // Play the extra turn for player 2
			if (currentP == Player.Player2)
				playAI();
		}

		return true;
	}

	public boolean playAI() {
		boolean checkEnd = checkEndGame();
		if (!checkEnd) {
			int[] smallValues = new int[12];
			int[] bigValues = new int[2];
			for (int i = 0; i <= 11; i++) {
				smallValues[i] = smallHoles[i].getStones();
			}
			bigValues[0] = bigHoles[0].getStones();
			bigValues[1] = bigHoles[1].getStones();
			AIPlayer player2 = new AIPlayer(smallValues, bigValues);
			int choice = player2.chooseMove();
			System.out.println("Choice-" + choice);
			holePressed(choice);
			/*
			 * RANDOM PICK while (currentP == Player.Player2) { int choice =
			 * chooseTurn.nextInt(6) + 6; while (smallHoles[choice].getStones() == 0) {
			 * choice = chooseTurn.nextInt(6) + 6; } System.out.println("AI chooses hole " +
			 * choice); holePressed(choice); } return true;
			 */
		}
		return false;

	}

	public boolean nextTurn() { // Moves to the next player
		if (currentP == Player.Player1) {
			currentP = Player.Player2;
			curName.setText("Player2");
			message.setText("");
			turnNumLabel.setText("Turn-" + (++turnCount));
			System.out.println("----------------------------------------------------");

			playAI();

			return true;
		}
		currentP = Player.Player1;
		curName.setText("Player1");
		message.setText("");
		turnNumLabel.setText("Turn-" + (++turnCount));
		System.out.println("----------------------------------------------------");

		return true;

	}

	public boolean checkSteal(int pos) { // Check if the player steals from the other player
		int mirrorPos = 11 - pos;
		int sum = 0;
		if (smallHoles[pos].getStones() == 1 && smallHoles[mirrorPos].getStones() != 0) { // Check if the last stone
																							// landed in an empty hole
			if ((pos <= 5 && currentP == Player.Player1) || (pos >= 6 && currentP == Player.Player2)) {
				System.out.println("Steal!   Pos-"+pos);
				sum += 1 + smallHoles[mirrorPos].getStones();

				smallHoles[mirrorPos].updateCount(0);
				smallHoles[pos].updateCount(0);
				if (currentP == Player.Player1)
					bigHoles[1].addStones(sum);
				else
					bigHoles[0].addStones(sum);
				return true;
			}

		}

		return false;
	}

	public void clearField() { // clears the holes from any stones left
		for (int i = 0; i < 12; i++) {
			smallHoles[i].updateCount(0);
		}

	}

	public boolean printClickError() { // handles if an unavailable hole is picked
		message.setText("You can't pick that!");
		return true;
	}

	public boolean checkEndGame() { // Game ends when 1 row is stone empty

		int sumP1 = 0, sumP2 = 0;
		for (int i = 0; i <= 5; i++) { // sum of total stone in each row
			sumP1 += smallHoles[i].getStones();
			sumP2 += smallHoles[i + 6].getStones();

		}
		if (sumP1 == 0 || sumP2 == 0) {
			bigHoles[0].addStones(sumP2);
			bigHoles[1].addStones(sumP1);
			clearField();
			if (bigHoles[0].getStones() > bigHoles[1].getStones())
				curName.setText("Player 2 wins! Scorings-" + bigHoles[0].getStones() + "-" + bigHoles[1].getStones());
			else if (bigHoles[0].getStones() < bigHoles[1].getStones())
				curName.setText("Player 1 wins! Scorings-" + bigHoles[0].getStones() + "-" + bigHoles[1].getStones());
			else
				curName.setText("Tie! Scorings-" + bigHoles[0].getStones() + "-" + bigHoles[1].getStones());
			return true;

		}

		return false;
	}

}
