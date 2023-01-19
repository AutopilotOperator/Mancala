package game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BigHole {
	public static int width = 100, height = 200;
	private JLabel pic;
	private GamePanel panel;
	private int stones;
	private BufferedImage[] images;
	private static final int  maxStone=48;


	public BigHole(GamePanel panel) {//represents each hole
		this.panel = panel;
		pic = new JLabel();
		stones=0;
//img = ImageIO.read(new File("D:\\Eclipse\\workspace\\Mancala\\src\\pictures\\h0.png"));
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("holes//h0.png"));
		} catch (IOException e) {

			e.printStackTrace();
		}

		images=new BufferedImage[maxStone+1];
		for(int i=0;i<maxStone+1;i++) {
			try {
				images[i]=ImageIO.read(new File("holes\\h"+i+".png"));
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
        pic.setPreferredSize(new Dimension(width,height));
//	    pic.setBounds(x, y, width, height);
//		panel.add(pic);
	}

	public JLabel getLabel() { // Return the label of the hole
		return this.pic;
	}
	
	public int getStones() {
		return stones;
	}
	public boolean addStones(int add) {
		stones+=add;
		updatePicture();
		return true;
	}
	public boolean resetHole() {
		stones=0;
		updatePicture();
		return true;
	}
	public boolean incrementStones() { //update the stone count of the hole
		stones++;
		updatePicture();
		return true;
	}
	public boolean updatePicture() { //updates the picture to the current stone count
		BufferedImage img = images[stones];
		

		Image dimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		ImageIcon stones = new ImageIcon(dimg);
		pic.setIcon(stones);
		return true;
	}

}
