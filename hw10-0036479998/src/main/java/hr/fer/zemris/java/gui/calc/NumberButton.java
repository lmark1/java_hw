package hr.fer.zemris.java.gui.calc;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * This class represents number buttons on the calculator GUI.
 * 
 * @author Lovro Marković
 *
 */
public class NumberButton extends JButton{

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Number that this button represents.
	 */
	private String number;
	
	/**
	 * Constructor used only for number buttons.
	 * 
	 * @param number
	 *            Number of the button.
	 * @param display
	 *            Display the button writes to.
	 * @param e Action listener.
	 */
	public NumberButton(String number, JLabel display, ActionListener e) {
		this.number = number;
		
		setBackground(Color.CYAN);
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setText(number);
		setOpaque(true);
		setFont(getFont().deriveFont(16f));
		addActionListener(e);
	}
	
	/**
	 * Getter for number.
	 * @return Number.
	 */
	public String getNumber() {
		return number;
	}
}
