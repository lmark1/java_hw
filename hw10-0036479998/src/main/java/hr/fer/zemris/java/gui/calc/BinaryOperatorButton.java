package hr.fer.zemris.java.gui.calc;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;

/**
 * This class implements a binary operator button.
 * 
 * @author Lovro Marković
 *
 */
public class BinaryOperatorButton extends JButton{

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Name of the button.
	 */
	@SuppressWarnings("unused")
	private String name;
	
	
	/**
	 * Constructor for Binary operator button.
	 * 
	 * @param name Name of the binary operator.
	 * @param e Action listener.
	 */
	public BinaryOperatorButton(String name, ActionListener e) {
		
		this.name = name;
		
		setBackground(Color.CYAN);
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setText(name);
		setOpaque(true);
		setFont(getFont().deriveFont(16f));
		addActionListener(e);
	}
	
}
