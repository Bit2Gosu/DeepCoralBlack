package components;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Kurz extends JFrame {

	public Kurz() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(640, 480));
		JPanel panel = new JPanel();
		panel.add(new JScrollPane(new JLabel(new ImageIcon("images/LondonOriginal.jpeg"))));
		add(panel);
		pack();
		setVisible(true);
	}

	public static void main(String[] args) {
		new v2();
	}

}
