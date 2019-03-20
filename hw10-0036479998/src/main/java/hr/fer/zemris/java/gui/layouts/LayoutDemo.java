package hr.fer.zemris.java.gui.layouts;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

@SuppressWarnings("javadoc")
public class LayoutDemo {

	public static void main(String[] args) {
		JPanel p = new JPanel(new CalcLayout(3));
		
		JLabel label1 = new JLabel("x");
		label1.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		
		JLabel label2 = new JLabel("y");
		label2.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		
		JLabel label3 = new JLabel("x");
		label3.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		
		JLabel label4 = new JLabel("y");
		label4.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		
		JLabel label5 = new JLabel("x");
		label5.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		
		JLabel label6 = new JLabel("x");
		label6.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		
		p.add(label1, new RCPosition(1, 1));
		p.add(label2, new RCPosition(2, 3));
		p.add(label3, new RCPosition(2, 7));
		p.add(label4, new RCPosition(4, 5));
		p.add(label5, new RCPosition(4, 6));
		p.add(label6, new RCPosition(4, 7));
//		p.add(new JLabel("x"), "1,1");
//		p.add(new JLabel("y"), "2,3");
//		p.add(new JLabel("z"), "2,7");
//		p.add(new JLabel("w"), "4,2");
//		p.add(new JLabel("a"), "4,5");
//		p.add(new JLabel("b"), "4,7");
		
		JFrame frame = new JFrame();
		frame.add(p);
		frame.setSize(p.getMinimumSize());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}
}
