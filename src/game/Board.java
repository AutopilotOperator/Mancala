package game;

import java.awt.Color;

import javax.swing.JFrame;



	public class Board extends JFrame{
		private GamePanel board;
	
	public static void main(String[] args) {
		Board b= new Board();
		b.setVisible(true);
				

	}
	public Board() {
		setSize(900,300);
		setTitle("Mancala");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		board=new GamePanel();
		this.setContentPane(board);
		setResizable(true);
		setVisible(true);
		
		
	}
	}


