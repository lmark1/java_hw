package hr.fer.zemris.java.gui.prim;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * Program runs a GUI which generates prim numbers into two separate lists.
 * 
 * @author Lovro Marković
 *
 */
public class PrimDemo extends JFrame {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Main method of the program. Executes when program runs.
	 * 
	 * @param args
	 *            Accepts no command line arguments.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new PrimDemo().setVisible(true);
			;
		});
	}

	/**
	 * Constructor for this frame.
	 */
	public PrimDemo() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLocation(20, 20);
		setSize(500, 400);
		setTitle("PrimDemo");

		intiGUI();
	}

	/**
	 * Initializes GUI.
	 */
	private void intiGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		
		PrimListModel primModel = new PrimListModel();
		primModel.next();
		
		JPanel newPanel = new JPanel(new GridLayout(1, 2));
		JList<Integer> listLeft = new JList<>(primModel);
		JList<Integer> listRight = new JList<>(primModel);
		newPanel.add(new JScrollPane(listLeft));
		newPanel.add(new JScrollPane(listRight));
		cp.add(newPanel, BorderLayout.CENTER);
		
		JButton next = new JButton("Next prim.");
		cp.add(next, BorderLayout.PAGE_END);
		
		next.addActionListener(e -> {
			primModel.next();
		});
	}

	/**
	 * List used for generating prim numbers implementing List Model.
	 * 
	 * @author Lovro Marković
	 */
	private static class PrimListModel implements ListModel<Integer> {

		/**
		 * Internal list containing prim numbers.
		 */
		private List<Integer> primNumbers = new ArrayList<>();

		/**
		 * Listener list.
		 */
		private List<ListDataListener> listeners = new ArrayList<>();

		/**
		 * Currentl prime number.
		 */
		private int currentPrime = 0;

		@Override
		public int getSize() {
			return primNumbers.size();
		}

		@Override
		public Integer getElementAt(int index) {
			return primNumbers.get(index);
		}

		@Override
		public void addListDataListener(ListDataListener l) {
			listeners.add(l);
		}

		@Override
		public void removeListDataListener(ListDataListener l) {
			listeners.remove(l);
		}

		/**
		 * Adds next prim number to the list.
		 */
		public void next() {

			while (true) {
				currentPrime++;
				if (isPrime())
					break;
			}

			int position = primNumbers.size();
			primNumbers.add(currentPrime);

			ListDataEvent event = new ListDataEvent(this,
					ListDataEvent.INTERVAL_ADDED, position, position);
			
			for (ListDataListener l: listeners) {
				l.intervalAdded(event);
			}
		}

		/**
		 * Checks if currentPrime is in fact a prime number.
		 * 
		 * @return True if currentPrime is a prime number, otherwise false.
		 */
		private boolean isPrime() {

			if (currentPrime == 2)
				return true;
			if (currentPrime % 2 == 0)
				return false;

			for (int i = 3; i * i <= currentPrime; i += 2) {

				if (currentPrime % i == 0)
					return false;
			}

			return true;
		}
	}
}
