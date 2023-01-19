package game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Hole implements ActionListener {
	public static int width = 100, height = 100;
	public int position;
	private JButton pic;
	private GamePanel panel;
	private int stones;
	private BufferedImage[] images;
	private static final int  maxStone=15;

	public Hole(GamePanel panel, int position) {// represents each hole
		this.position = position;
		this.panel = panel;
		stones = 4;

		// Set default picture

		pic = new JButton();
		pic.addActionListener(this);
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("holes\\4.png"));
		} catch (IOException e) {

			e.printStackTrace();
		}
		images=new BufferedImage[maxStone+1];
		for(int i=0;i<maxStone+1;i++) {
			try {
				images[i]=ImageIO.read(new File("holes\\"+i+".png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Image dimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		ImageIcon stones = new ImageIcon(dimg);
		pic.setIcon(stones);

		pic.setBorder(null);
		pic.setVisible(true);
		pic.setMaximumSize(new Dimension(width, height));
//      pic.setPreferredSize(new Dimension(x,y));
//	    pic.setBounds(x, y, width, height);
//		panel.add(pic);
	}

	public JButton getButton() { // return a reference to the button
		return this.pic;
		
	}

	public synchronized boolean updateCount(int stones) { // update the stone count of the hole
		this.stones = stones;
		updatePicture();
		return true;
	}

	public boolean updatePicture() { // updates the picture to the current stone count
		BufferedImage img = images[stones];
	

		Image dimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		ImageIcon stones = new ImageIcon(dimg);
		pic.setIcon(stones);
		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e) { // Handle hole press, send the hole number which is pressed
		
		
		
		if(stones!=0) {
		boolean input = panel.holePressed(position);
		if (!input) {
			System.out.println("You can't pick it");
			panel.printClickError();
		}
		}
	}

	public int getStones() {
		return stones;

	}

}
