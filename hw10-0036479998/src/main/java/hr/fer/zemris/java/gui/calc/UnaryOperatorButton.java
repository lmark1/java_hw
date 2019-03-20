package hr.fer.zemris.java.gui.calc;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;

/**
 * This class implements an unary operator button.
 * 
 * @author Lovro Marković
 *
 */
public class UnaryOperatorButton extends JButton{

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Name of the operator.
	 */
	private String name;
	
	/**
	 * Constructor for unary operator buttons. 
	 * 
	 * @param name Name of the unary function.
	 * @param e Action listener.
	 */
	public UnaryOperatorButton(String name, ActionListener e) {
		this.name = name;
		
		setBackground(Color.CYAN);
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setText(this.name);
		setOpaque(true);
		setFont(getFont().deriveFont(16f));
		addActionListener(e);
	}
	
}
