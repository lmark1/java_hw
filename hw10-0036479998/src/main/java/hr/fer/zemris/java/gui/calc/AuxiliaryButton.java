package hr.fer.zemris.java.gui.calc;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;

/**
 * This class models auxiliary calculator buttons. 
 * 
 * @author Lovro Marković
 *
 */
public class AuxiliaryButton extends JButton{

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor for the auxiliary button.
	 * 
	 * @param name Name of the button.
	 * @param listener Action listener for the button press.
	 */
	public AuxiliaryButton(String name, ActionListener listener) {
		setBackground(Color.CYAN);
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setText(name);
		setOpaque(true);
		setFont(getFont().deriveFont(16f));
		addActionListener(listener);
	}
	
}
