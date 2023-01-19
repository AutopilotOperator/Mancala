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

public class DashboardButton implements ActionListener {
	public static int width = 20, height = 10;
	private JButton btn;
	private GamePanel panel;
	private ButtonType buttonType;

	public DashboardButton(GamePanel panel, ButtonType buttonType) {// represents The dashboard buttton
		this.panel = panel;
		this.buttonType = buttonType;

		// Set default picture

		btn = new JButton();
		btn.addActionListener(this);
//		BufferedImage img = null;
//		try {
//			img = ImageIO.read(new File("D:\\Eclipse\\workspace\\Mancala\\src\\pictures\\4.png"));
//		} catch (IOException e) {

//			e.printStackTrace();
//		}

//		Image dimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
//		ImageIcon stones = new ImageIcon(dimg);
//		btn.setIcon(stones);
		switch (buttonType) {
		case Reset:
			btn.setText("Reset");
			break;
		case TurnBack:
			btn.setText("TurnBack");

			break;
		default:

		}
		btn.setBorder(null);
		btn.setVisible(true);
		btn.setMaximumSize(new Dimension(width, height));
//      pic.setPreferredSize(new Dimension(x,y));
//	    pic.setBounds(x, y, width, height);
//		panel.add(pic);
	}

	public JButton getButton() { // return a reference to the button
		return this.btn;

	}

	@Override
	public void actionPerformed(ActionEvent e) { // Handle hole press, sends the pressed button type to the game panel
		panel.dashboardPress(buttonType);
	}

}
