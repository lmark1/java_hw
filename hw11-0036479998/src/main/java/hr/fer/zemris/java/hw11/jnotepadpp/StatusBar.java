package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;

import hr.fer.zemris.java.hw11.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LJLabel;

/**
 * This class implements Status Bar which shows information about currently
 * opened document.Status bar shows length of the current document, row and
 * column of the carret and size of the highlighted selection.
 * 
 * @author Lovro Marković
 *
 */
public class StatusBar extends JPanel {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Tabbed pane which status bar monitors.
	 */
	private JTabbedPane tabbedPane;

	/**
	 * First component of the status bar, calculates lenght.
	 */
	private LJLabel length;

	/**
	 * Second component of the status bar calculates carret position.
	 */
	private JLabel caretPos;

	/**
	 * Third component of the status bar, displays date and time.
	 */
	private JLabel myClock;

	/**
	 * Status bar panel.
	 */
	private JPanel statusBar;

	/**
	 * Currently selected text area.
	 */
	private JTextArea currentTextArea;

	/**
	 * References to all action that can be disabled.
	 */
	private Action[] actions;

	/**
	 * Constructor for the Status bar.
	 * 
	 * @param tabbedPane
	 *            Tabbed pane which status bar is monitoring.
	 * @param flp
	 *            Form localization provider.
	 * @param actions
	 *            References to all actions that should be disabled at some point.
	 */
	public StatusBar(JTabbedPane tabbedPane, FormLocalizationProvider flp,
			Action[] actions) {
		this.tabbedPane = tabbedPane;
		this.actions = actions;

		this.tabbedPane.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				int selectedIndex = StatusBar.this.tabbedPane
						.getSelectedIndex();

				if (selectedIndex < 0) {
					updateStatusBar(null);
					return;
				}

				JTextArea textArea = JNotepadPP.getTextArea(tabbedPane,
						selectedIndex);
				updateStatusBar(textArea);
			}
		});

		statusBar = new JPanel();
		statusBar.setLayout(new GridLayout(1, 3));

		length = new LJLabel("length", flp);
		length.setHorizontalAlignment(SwingConstants.LEFT);
		caretPos = new JLabel();
		caretPos.setBorder(null);

		myClock = new JLabel(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date()).trim());
		myClock.setHorizontalAlignment(SwingConstants.RIGHT);

		Timer t = new Timer(1000, updateClock);
		t.start();

		statusBar.add(length);
		statusBar.add(caretPos);
		statusBar.add(myClock);

		setLayout(new BorderLayout());
		add(statusBar, BorderLayout.CENTER);
	}

	/**
	 * Listener that updates the clock label.
	 */
	private final ActionListener updateClock = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			myClock.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(new Date()).trim());
		}
	};

	/**
	 * Caret listener used for updating caretPos label.
	 */
	private final CaretListener caretListener = new CaretListener() {

		@Override
		public void caretUpdate(CaretEvent e) {
			int highlightLen = getHighlightedSize();
			CaretPosition pos = getCaretPosition();
			StatusBar.this.caretPos
					.setText(getCaretText(pos.ln, pos.col, highlightLen));
			
			if (highlightLen == 0) {
				disableActions();
			} else {
				enableActions();
			}
		}
	};

	/**
	 * Document listener used for getting current size of document.
	 */
	private final DocumentListener documentListener = new DocumentListener() {

		@Override
		public void removeUpdate(DocumentEvent e) {
			update();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			update();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			update();
		}

		/**
		 * Update length label.
		 */
		private void update() {
			length.setDocLength(currentTextArea.getText().toString().length());
		}

	};

	/**
	 * Enable all given actions.
	 */
	private void enableActions() {
		for (Action action : actions) {
			action.setEnabled(true);
		}		
	}

	/**
	 * Disable all given actions.
	 */
	private void disableActions() {
		for (Action action : actions) {
			action.setEnabled(false);
		}		
	}

	/**
	 * Updates the status bar each time document changes. If null value is
	 * passed as a text area the method assumes that there are no more opened
	 * tabs and sets all values to 0.
	 * 
	 * @param textArea
	 *            New text area.
	 */
	private void updateStatusBar(JTextArea textArea) {

		if (currentTextArea != null) {
			removeAllListeners();
		}

		currentTextArea = textArea;

		if (currentTextArea == null) {
			length.setText("");
			caretPos.setText("");
			caretPos.setBorder(null);
			disableActions();
			return;
		}

		caretPos.setBorder(
				BorderFactory.createMatteBorder(0, 2, 0, 0, Color.GRAY));
		CaretPosition pos = getCaretPosition();
		caretPos.setText(getCaretText(pos.ln, pos.col, getHighlightedSize()));
		
		// Set case action state.
		if (getHighlightedSize() == 0) {
			disableActions();
		} else {
			enableActions();
		}
		
		currentTextArea.addCaretListener(caretListener);

		length.setDocLength(currentTextArea.getText().length());
		currentTextArea.getDocument().addDocumentListener(documentListener);
	}

	/**
	 * Remove all caret listeners and document listeners from the current text
	 * area.
	 */
	private void removeAllListeners() {

		currentTextArea.removeCaretListener(caretListener);
		Document doc = currentTextArea.getDocument();
		doc.removeDocumentListener(documentListener);
	}

	/**
	 * @return Returns current caret position in the current tab.
	 */
	private CaretPosition getCaretPosition() {
		Caret caret = currentTextArea.getCaret();

		int caretPos = caret.getDot();
		int ln = 0, col = 0;
		try {
			ln = currentTextArea.getLineOfOffset(caretPos);
			col = caretPos - currentTextArea.getLineStartOffset(ln);

		} catch (BadLocationException ignorable) {
		}
		ln++;
		col++;

		CaretPosition pos = new CaretPosition();
		pos.col = col;
		pos.ln = ln;

		return pos;
	}

	/**
	 * @return Returns size of currently highlighted text.
	 */
	private int getHighlightedSize() {
		Caret caret = currentTextArea.getCaret();
		int highlightLen = Math.abs(caret.getDot() - caret.getMark());

		return highlightLen;
	}

	/**
	 * @param ln
	 *            Line number.
	 * @param col
	 *            Column number.
	 * @param hLen
	 *            Highlight size.
	 * @return Return forumated string containing given data.
	 */
	private String getCaretText(int ln, int col, int hLen) {
		return String.format("Ln:%d Col:%d Sel:%d", ln, col, hLen);
	}

	/**
	 * Class containing caret position.
	 * 
	 * @author Lovro Marković
	 *
	 */
	private static class CaretPosition {
		/**
		 * Column of caret.
		 */
		private int col;

		/**
		 * Line of caret.
		 */
		private int ln;

	}
}
