package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * Custom tabbed pane.
 * 
 * @author Lovro Marković
 *
 */
public class CustomTabbedPane extends JTabbedPane {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Icon represents an unmodified document.
	 */
	private ImageIcon unmodifiedIcon;

	/**
	 * Blank document counter.
	 */
	private int untitledCounter;

	/**
	 * Constructor for the tabbed pane.
	 * 
	 * @param unmodifiedIcon
	 *            Unmodified icon.
	 */
	public CustomTabbedPane(ImageIcon unmodifiedIcon) {
		this.unmodifiedIcon = unmodifiedIcon;
		this.untitledCounter = 0;
	}

	@Override
	public void addTab(String title, Icon icon, Component component, String tip) {
		JPanel tabPanel = new JPanel();

		if (title.equals("Untitled")) {
			untitledCounter++;
			title += untitledCounter;
		}

		tabPanel.add(new JLabel(icon));
		tabPanel.add(new JLabel(title));

		JButton exitButton = new JButton("x");
		exitButton.addActionListener(getExitButtonListener());
		exitButton.setMargin(new Insets(1, 1, 1, 1));
		exitButton.setBorder(BorderFactory.createEmptyBorder());
		
		tabPanel.add(exitButton);

		addTab(null, null, component);

		int tabPosition = getTabCount() - 1;
		setTabComponentAt(tabPosition, tabPanel);
		setToolTipTextAt(tabPosition, tip);
	}

	/**
	 * Generate an exit button listener.
	 * 
	 * @return Exit button listener.
	 */
	private ActionListener getExitButtonListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Component c = (Component) e.getSource();

				JPanel parent = (JPanel) c.getParent();
				int selectedIndex = 0;

				// Find selected index
				for (int i = 0, count = getTabCount(); i < count; i++) {
					JPanel currentPanel = (JPanel) getTabComponentAt(i);
					if (parent.equals(currentPanel)) {
						selectedIndex = i;
						break;
					}
				}

				// Check if it modified
				if (getIconAt(selectedIndex).equals(unmodifiedIcon)) {
					remove(selectedIndex);
					return;
				}

				// If it is modified ask the user if the program should save
				// modified file
				JFrame parentFrame = (JFrame) SwingUtilities
						.getWindowAncestor(CustomTabbedPane.this);
				Component[] comps = parent.getComponents();
				int dialog = JOptionPane.showConfirmDialog(parentFrame,
						"Do you want to save " + ((JLabel) comps[1]).getText() + " document?",
						"Save document?", JOptionPane.YES_NO_OPTION);

				if (dialog == JOptionPane.NO_OPTION) {
					remove(selectedIndex);
					return;
				}

				// Ask user where to save the document
				Path path = null;
				if (getToolTipTextAt(selectedIndex) == null) {
						
					JFileChooser fc = new JFileChooser();
					fc.setDialogTitle("Save file");
					if (fc.showSaveDialog(parentFrame) != JFileChooser.APPROVE_OPTION) {
						JOptionPane.showMessageDialog(parentFrame, "Document won't be saved.",
								"Info message", JOptionPane.INFORMATION_MESSAGE);
						return;
					}

					path = fc.getSelectedFile().toPath();
				
				} else {
					path = Paths.get(getToolTipTextAt(selectedIndex));
				}
				
				JTextArea textArea = JNotepadPP.getTextArea(CustomTabbedPane.this, selectedIndex);
				String text = textArea.getText();
				
				try {
					Files.write(path, text.getBytes(StandardCharsets.UTF_8));

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(parentFrame, "Error occured while saving.",
							"Info message", JOptionPane.INFORMATION_MESSAGE);
					remove(selectedIndex);
					return;
				}

				JOptionPane.showMessageDialog(parentFrame, "File is saved.", "Info message",
						JOptionPane.INFORMATION_MESSAGE);
				remove(selectedIndex);
			}
		};
	}

	@Override
	public void setIconAt(int index, Icon icon) {
		JPanel c = (JPanel) getTabComponentAt(index);
		
		Component[] components = c.getComponents();
		JLabel iconLabel = (JLabel) components[0];
		iconLabel.setIcon(icon);
	}

	@Override
	public Icon getIconAt(int index) {
		JPanel c = (JPanel) getTabComponentAt(index);
		
		if (c == null) {
			return null;
		}
		
		Component[] components = c.getComponents();
		JLabel iconLabel = (JLabel) components[0];
		return iconLabel.getIcon();
	}

	@Override
	public void setTitleAt(int index, String title) {
		JPanel c = (JPanel) getTabComponentAt(index);

		Component[] components = c.getComponents();
		JLabel titleLabel = (JLabel) components[1];
		titleLabel.setText(title);
	}
	
	@Override
	public String getTitleAt(int index) {
		JPanel c = (JPanel) getTabComponentAt(index);
		
		if (c == null) {
			return null;
		}
		
		Component[] components = c.getComponents();
		JLabel titleLabel = (JLabel) components[1];
		return titleLabel.getText();
	}
}
