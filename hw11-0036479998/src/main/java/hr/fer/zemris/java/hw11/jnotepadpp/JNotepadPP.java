package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Collator;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.Segment;

import hr.fer.zemris.java.hw11.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LJMenu;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizableAction;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;

/**
 * This class implements a simple notpad GUI with various text editor
 * functionalities.
 * 
 * @author Lovro Marković
 *
 */
public class JNotepadPP extends JFrame {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Tabbed pane used in the application.
	 */
	private CustomTabbedPane tabbedPane;

	/**
	 * Image icon used for unmodified tabs in the editor.
	 */
	private final ImageIcon unmodifiedIcon = loadImageIcon(
			"./icons/green_diskette.png");

	/**
	 * Image icon used for modified tabs in the editor.
	 */
	private final ImageIcon modifiedIcon = loadImageIcon(
			"./icons/red_diskette.png");

	/**
	 * Buffer used for cutting and copying.
	 */
	private String pasteBuffer;

	/**
	 * Counts number of empty opened tabs.
	 */
	private int tabCounter;

	/**
	 * Panel containing tabbedPane and status bar.
	 */
	private JPanel centerPanel;

	/**
	 * Form localization provider object.
	 */
	private FormLocalizationProvider flp = new FormLocalizationProvider(
			LocalizationProvider.getInstance(), this);

	/**
	 * Main method of the program. Executes when program starts.
	 * 
	 * @param args
	 *            Accepts no command line arguments.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new JNotepadPP().setVisible(true));
	}

	/**
	 * Default constructor for the JNotepad frame.
	 */
	public JNotepadPP() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setSize(600, 500);
		setLocation(20, 20);
		setTitle("JNotepad++");

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				removeAllTabs();
				
				if (tabbedPane.getTabCount()>0) {
					return;
				}
				
				dispose();
			}
		});

		initGUI();
	}

	/**
	 * This method initailizes various GUI components.
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());

		tabbedPane = new CustomTabbedPane(unmodifiedIcon);
		tabbedPane.addChangeListener(getChangeListener());

		centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(tabbedPane, BorderLayout.CENTER);
		cp.add(centerPanel, BorderLayout.CENTER);

		createActions();
		createMenus();
		createToolbars();

		// Array of all actions that can be disabled
		// Passed to the status bar - responsible for disabling the actions
		Action[] actions = new Action[] { toUpperCaseAction, toLowerCaseAction,
				invertCaseAction, sortAscendingAction, sortDescendingAction,
				filterUniqueAction };

		// Add status bar on the center panel
		centerPanel.add(new StatusBar(tabbedPane, flp, actions),
				BorderLayout.PAGE_END);
	}

	/**
	 * Defines all actions used by this text editor.
	 */
	private void createActions() {
		newDocumentAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control N"));
		newDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);
		
		openDocumentAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control O"));
		openDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
		
		saveDocumentAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control S"));
		saveDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
		
		saveAsDocumentAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control E"));
		saveAsDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
		
		closeDocumentAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control W"));
		closeDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_L);
		
		copySelectedTextAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control C"));
		copySelectedTextAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
	
		cutSelectedTextAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control X"));
		cutSelectedTextAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_U);
		
		pasteSelectedTextAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control V"));
		pasteSelectedTextAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_P);
		
		getStatisticsAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control T"));
		getStatisticsAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_T);
		
		exitApplicationAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("alt F4"));
		exitApplicationAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);
		
		// Set initial state of case operation as disabled.
		toUpperCaseAction.setEnabled(false);
		toLowerCaseAction.setEnabled(false);
		invertCaseAction.setEnabled(false);
		
		// Set initial state of sort operations
		sortAscendingAction.setEnabled(false);
		sortDescendingAction.setEnabled(false);
		filterUniqueAction.setEnabled(false);
		
		// Replace default cut / copy / paste action
		ActionMap amap = tabbedPane.getActionMap();
		amap.put(DefaultEditorKit.copyAction, copySelectedTextAction);
		amap.put(DefaultEditorKit.cutAction, cutSelectedTextAction);
		amap.put(DefaultEditorKit.pasteAction, pasteSelectedTextAction);
	}

	/**
	 * Creates menus for this text editor.
	 */
	private void createMenus() {
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new LJMenu("file", flp);
		fileMenu.setMnemonic(KeyEvent.VK_F);
		fileMenu.add(newDocumentAction);
		fileMenu.add(openDocumentAction);
		fileMenu.add(saveDocumentAction);
		fileMenu.add(saveAsDocumentAction);
		fileMenu.addSeparator();
		fileMenu.add(closeDocumentAction);
		fileMenu.addSeparator();
		fileMenu.add(exitApplicationAction);

		JMenu editMenu = new LJMenu("edit", flp);
		editMenu.setMnemonic(KeyEvent.VK_E);
		editMenu.add(copySelectedTextAction);
		editMenu.add(cutSelectedTextAction);
		editMenu.add(pasteSelectedTextAction);
		editMenu.addSeparator();
		editMenu.add(getStatisticsAction);

		JMenu languageMenu = new LJMenu("languages", flp);
		languageMenu.add(enLanguageSwitch);
		languageMenu.add(deLanguageSwitch);
		languageMenu.add(hrLanguageSwitch);
		
		JMenu caseMenu = new LJMenu("changecase", flp);
		caseMenu.add(toUpperCaseAction);
		caseMenu.add(toLowerCaseAction);
		caseMenu.add(invertCaseAction);

		JMenu sortMenu = new LJMenu("sort", flp);
		sortMenu.add(sortAscendingAction);
		sortMenu.add(sortDescendingAction);

		JMenu toolsMenu = new LJMenu("tools", flp);
		toolsMenu.add(filterUniqueAction);
		toolsMenu.addSeparator();
		toolsMenu.add(caseMenu);
		toolsMenu.add(sortMenu);
		
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(languageMenu);
		menuBar.add(toolsMenu);
		
		setJMenuBar(menuBar);
	}

	/**
	 * Create toolbars for this text editor.
	 */
	private void createToolbars() {
		JToolBar toolBar = new JToolBar("Tool bar");

		toolBar.add(newDocumentAction);
		toolBar.add(openDocumentAction);
		toolBar.add(saveDocumentAction);
		toolBar.add(saveAsDocumentAction);

		toolBar.addSeparator();
		toolBar.add(copySelectedTextAction);
		toolBar.add(cutSelectedTextAction);
		toolBar.add(pasteSelectedTextAction);

		toolBar.addSeparator();
		toolBar.add(getStatisticsAction);

		toolBar.addSeparator();
		toolBar.add(closeDocumentAction);
		toolBar.add(exitApplicationAction);

		getContentPane().add(toolBar, BorderLayout.PAGE_START);
	}

	/**
	 * Action performed when creating a new document.
	 */
	private final Action newDocumentAction = new LocalizableAction("new", flp) {

		/**
		 * Serial version ID.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextArea newTextArea = new JTextArea();
			JScrollPane scrollText = new JScrollPane(newTextArea);
			newTextArea.getDocument()
					.addDocumentListener(getDocumentListener());
			newTextArea.setCaretPosition(0);

			tabCounter++;
			tabbedPane.addTab("Untitled" + tabCounter, unmodifiedIcon,
					new JScrollPane(scrollText), null);
			tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
		}
	};

	/**
	 * Action performed when opening documents.
	 */
	private final Action openDocumentAction = new LocalizableAction("open",
			flp) {

		/**
		 * Serial version ID.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Open file");
			if (fc.showOpenDialog(
					JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
				return;
			}

			File fileName = fc.getSelectedFile();
			Path filePath = fileName.toPath();

			if (!Files.isReadable(filePath)) {
				JOptionPane.showMessageDialog(JNotepadPP.this,
						"File " + filePath + " can't be read!", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			byte[] data = null;

			try {
				data = Files.readAllBytes(filePath);
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(JNotepadPP.this,
						"Error reading file " + filePath, "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			String text = new String(data, StandardCharsets.UTF_8);

			JTextArea newTextArea = new JTextArea(text);
			newTextArea.setCaretPosition(0);

			JScrollPane scrollText = new JScrollPane(newTextArea);
			newTextArea.getDocument()
					.addDocumentListener(getDocumentListener());
			tabbedPane.addTab(filePath.getFileName().toString(), unmodifiedIcon,
					scrollText, filePath.toString());
			tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
		}
	};

	/**
	 * Action performed when trying to save document in the current tab.
	 */
	private final Action saveDocumentAction = new LocalizableAction("save",
			flp) {

		/**
		 * Serial version ID.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {

			int selectedIndex = tabbedPane.getSelectedIndex();

			if (selectedIndex < 0 || isUnmodified(selectedIndex)) {
				return;
			}

			Path path = null;
			if (tabbedPane.getToolTipTextAt(selectedIndex) == null) {
				JFileChooser fc = new JFileChooser();
				fc.setDialogTitle("Save file");

				if (fc.showSaveDialog(
						JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
					JOptionPane.showMessageDialog(JNotepadPP.this,
							"Document won't be saved.", "Info message",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}

				path = fc.getSelectedFile().toPath();

			} else {
				path = Paths.get(tabbedPane.getToolTipTextAt(selectedIndex));
			}

			saveDocument(path, selectedIndex);
		}
	};

	/**
	 * This action is performed when saving document.
	 */
	private final Action saveAsDocumentAction = new LocalizableAction("saveas",
			flp) {

		/**
		 * Serial version ID.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			int selectedIndex = tabbedPane.getSelectedIndex();

			if (selectedIndex < 0) {
				return;
			}

			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Save file");

			if (fc.showSaveDialog(
					JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
				JOptionPane.showMessageDialog(JNotepadPP.this,
						"Document won't be saved.", "Info message",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			Path path = fc.getSelectedFile().toPath();

			saveDocument(path, selectedIndex);
		}
	};

	/**
	 * Action performed when user wants to close a document in the currently
	 * opened tab.
	 */
	private final Action closeDocumentAction = new LocalizableAction("closetab",
			flp) {

		/**
		 * Serial version ID.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			removeCurrentTab();
		}
	};

	/**
	 * Action performed when user wants to copy a selection
	 */
	private final Action copySelectedTextAction = new LocalizableAction("copy",
			flp) {

		/**
		 * Serial version ID.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			int selectedIndex = tabbedPane.getSelectedIndex();

			if (selectedIndex < 0) {
				return;
			}

			JTextArea editor = getTextArea(tabbedPane, selectedIndex);
			String text = getHighlightedText(editor, false);

			if (text == null) {
				return;
			}

			pasteBuffer = new String(text);
		}
	};

	/**
	 * Performs cut action on the highlited text.
	 */
	private final Action cutSelectedTextAction = new LocalizableAction("cut",
			flp) {

		/**
		 * Serial version ID.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			int selectedIndex = tabbedPane.getSelectedIndex();

			if (selectedIndex < 0) {
				return;
			}

			JTextArea editor = getTextArea(tabbedPane, selectedIndex);
			String text = getHighlightedText(editor, true);

			if (text == null) {
				return;
			}

			pasteBuffer = new String(text);
		}
	};

	/**
	 * Pastes the text that is currently in the paste buffer.
	 */
	private final Action pasteSelectedTextAction = new LocalizableAction(
			"paste", flp) {

		/**
		 * Serial version ID.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {

			int selectedIndex = tabbedPane.getSelectedIndex();
			if (selectedIndex < 0) {
				return;
			}

			if (pasteBuffer == null) {
				return;
			}

			JTextArea editor = getTextArea(tabbedPane, selectedIndex);
			Document doc = editor.getDocument();

			try {
				doc.insertString(editor.getCaretPosition(), pasteBuffer, null);
			} catch (BadLocationException ignorable) {
			}

		}
	};

	/**
	 * Get statistical data about the currently opened docuemnt. Number of
	 * characters found in document, number of non-blank characters found in
	 * document and number of lines that the document contains.
	 */
	private final Action getStatisticsAction = new LocalizableAction("stats",
			flp) {

		/**
		 * Serial version ID.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {

			int selectedIndex = tabbedPane.getSelectedIndex();
			if (selectedIndex < 0) {
				return;
			}

			JTextArea editor = getTextArea(tabbedPane, selectedIndex);
			Document doc = editor.getDocument();

			int allChars = doc.getLength();
			int nonBlankChars = 0;
			int lines = 0;

			String text = null;
			try {
				text = doc.getText(0, allChars);
			} catch (BadLocationException ignorable) {
			}

			for (int i = 0, len = text.length(); i < len; i++) {
				nonBlankChars += Character.isWhitespace(text.charAt(i)) ? 0 : 1;
				lines += text.charAt(i) == '\n' ? 1 : 0;
			}

			if (!text.isEmpty()) {
				lines += text.endsWith(String.valueOf('\n')) ? 0 : 1;
			}

			String message = String.format(
					"Number of characters found in document is: %d.%n"
							+ "Number of non blank characters found in document is: %d.%n"
							+ "Number of lines in the document: %d.",
					allChars, nonBlankChars, lines);
			JOptionPane.showMessageDialog(JNotepadPP.this, message,
					"Document statistics", JOptionPane.INFORMATION_MESSAGE);
		}
	};

	/**
	 * Action performed when user wants to exit application.
	 */
	private final Action exitApplicationAction = new LocalizableAction("exit",
			flp) {

		/**
		 * Serial version ID.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			removeAllTabs();
			
			if (tabbedPane.getTabCount()>0){
				return;
			}
			
			dispose();
		}
	};

	/**
	 * Switch localization language to english.
	 */
	private final Action enLanguageSwitch = new LocalizableAction("english",
			flp) {

		/**
		 * Serial version ID.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("en");
		}
	};

	/**
	 * Switch localization language to german.
	 */
	private final Action deLanguageSwitch = new LocalizableAction("german",
			flp) {

		/**
		 * Serial version ID.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("de");

		}
	};

	/**
	 * Switch localization language to croatian.
	 */
	private final Action hrLanguageSwitch = new LocalizableAction("croatian",
			flp) {

		/**
		 * Serial version ID.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("hr");

		}
	};

	/**
	 * Action performed when user wants to switch selection to uppercase.
	 */
	private final Action toUpperCaseAction = new LocalizableAction(
			"touppercase", flp) {

		/**
		 * Serial version ID.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			performHighlightOperation((s) -> s.toUpperCase());
		}

	};

	/**
	 * Action performed when user wants to switch highlighted text to lower
	 * case.
	 */
	private final Action toLowerCaseAction = new LocalizableAction(
			"tolowercase", flp) {

		/**
		 * Serial version ID.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			performHighlightOperation((s) -> s.toLowerCase());
		}
	};

	/**
	 * Action performed when user wants to inver case of the highlighted lettes.
	 */
	private final Action invertCaseAction = new LocalizableAction("invertcase",
			flp) {

		/**
		 * Serial version ID.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			performHighlightOperation(new StringOperation() {

				@Override
				public String apply(String s) {
					StringBuilder sb = new StringBuilder(s.length());
					for (char c : s.toCharArray()) {
						if (Character.isUpperCase(c)) {
							sb.append(Character.toLowerCase(c));
						} else if (Character.isLowerCase(c)) {
							sb.append(Character.toUpperCase(c));
						} else {
							sb.append(c);
						}
					}

					return sb.toString();
				}
			});
		}
	};

	/**
	 * Action performed when user wants to sort lines in ascending order.
	 */
	private final Action sortAscendingAction = new LocalizableAction(
			"ascending", flp) {

		/**
		 * Serial version ID.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {

			performLinesOperation(new StringOperation() {

				@Override
				public String apply(String s) {
					Comparator<String> c = new Comparator<String>() {

						@Override
						public int compare(String o1, String o2) {
							Locale locale = new Locale(LocalizationProvider
									.getInstance().getLanguage());
							Collator collator = Collator.getInstance(locale);
							return collator.compare(o1, o2);
						}

					};

					String text = sortText(s, c);
					return text;
				}
			});
		}

	};

	/**
	 * Action performed when user wants to sort lines in descending order.
	 */
	private final Action sortDescendingAction = new LocalizableAction(
			"descending", flp) {

		/**
		 * Serial version ID.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			performLinesOperation(new StringOperation() {

				@Override
				public String apply(String s) {
					Comparator<String> c = new Comparator<String>() {

						@Override
						public int compare(String o1, String o2) {
							Locale locale = new Locale(LocalizationProvider
									.getInstance().getLanguage());
							Collator collator = Collator.getInstance(locale);
							return -collator.compare(o1, o2);
						}

					};

					String text = sortText(s, c);
					return text;
				}
			});
		}

	};

	/**
	 * Action performed when user wants to filter unique lines in the document.
	 */
	private final Action filterUniqueAction = new LocalizableAction("unique",
			flp) {

		/**
		 * Serial version ID.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			performLinesOperation(new StringOperation() {

				@Override
				public String apply(String s) {
					String[] split = s.split("\\r?\\n");

					if (split.length == 1) {
						return s;
					}

					List<String> splitList = Arrays.asList(split);
					Set<String> unique = new LinkedHashSet<>();
					unique.addAll(splitList);

					StringBuilder sb = new StringBuilder();

					for (String string : unique) {
						sb.append(string);
						sb.append('\n');
					}

					String uString = sb.toString();
					uString = uString.substring(0, uString.length() - 1);

					return uString;
				}
			});
		}
	};

	/**
	 * @param tabbedPane
	 *            Tabbed pane that contains the text area object.
	 * @param selectedIndex
	 *            Valid index of opened tab.
	 * @return Returns JTextArea object from the tab at the selected index.
	 */
	static JTextArea getTextArea(JTabbedPane tabbedPane,
			int selectedIndex) {

		JScrollPane sPane = ((JScrollPane) tabbedPane
				.getComponentAt(selectedIndex));
		JViewport view = sPane.getViewport();

		if (view.getView() instanceof JTextArea) {
			return (JTextArea) view.getView();
		}

		JScrollPane sPane1 = (JScrollPane) view.getView();
		JViewport view1 = sPane1.getViewport();

		return (JTextArea) view1.getView();
	}

	/**
	 * Save the document from the tab at the given index.
	 * 
	 * @param path
	 *            Path to the save target.
	 * @param selectedIndex
	 *            Index of the document tab.
	 */
	private void saveDocument(Path path, int selectedIndex) {
		String curPath = tabbedPane.getToolTipTextAt(selectedIndex);
	
		if (Files.exists(path) && !path.toString().equals(curPath)) {
			int dialogButton = JOptionPane.showConfirmDialog(JNotepadPP.this,
					"Are you sure that you want to overwrite "
							+ path.getFileName().toString(),
					"Warning message", JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE);
	
			if (dialogButton == JOptionPane.NO_OPTION) {
				JOptionPane.showMessageDialog(JNotepadPP.this,
						"Document won't be saved.", "Info message",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
		}
	
		JTextArea textArea = getTextArea(tabbedPane, selectedIndex);
		String text = textArea.getText();
	
		try {
			Files.write(path, text.getBytes(StandardCharsets.UTF_8));
	
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(JNotepadPP.this,
					"Error occured while saving.", "Info message",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
	
		JOptionPane.showMessageDialog(JNotepadPP.this, "File is saved.",
				"Info message", JOptionPane.INFORMATION_MESSAGE);
	
		tabbedPane.setTitleAt(selectedIndex, path.getFileName().toString());
		tabbedPane.setToolTipTextAt(selectedIndex, path.toString());
		tabbedPane.setIconAt(selectedIndex, unmodifiedIcon);
	}

	/**
	 * Perform a given StringOperation operation on the highlighted text.
	 * 
	 * @param operation
	 *            String operation.
	 */
	private void performHighlightOperation(StringOperation operation) {
		int selectedIndex = tabbedPane.getSelectedIndex();

		if (selectedIndex < 0) {
			return;
		}

		JTextArea textArea = getTextArea(tabbedPane, selectedIndex);
		Document doc = textArea.getDocument();
		Caret caret = textArea.getCaret();

		int len = Math.abs(caret.getDot() - caret.getMark());

		if (len == 0) {
			return;
		}

		int offset = Math.min(caret.getDot(), caret.getMark());

		try {
			String text = doc.getText(offset, len);
			text = operation.apply(text);
			doc.remove(offset, len);
			doc.insertString(offset, text, null);
		} catch (BadLocationException ignorable) {
		}
	}

	/**
	 * Method performs a text operation on the highlighted lines in the text.
	 * 
	 * @param operation
	 *            String operations
	 */
	private void performLinesOperation(StringOperation operation) {
		int selectedIndex = tabbedPane.getSelectedIndex();

		if (selectedIndex < 0) {
			return;
		}

		JTextArea textArea = getTextArea(tabbedPane, selectedIndex);
		Document doc = textArea.getDocument();
		Caret caret = textArea.getCaret();

		int len = Math.abs(caret.getDot() - caret.getMark());

		if (len == 0) {
			return;
		}

		int offset = Math.min(caret.getDot(), caret.getMark());
		try {
			int firstLine = textArea.getLineOfOffset(offset);
			int lastLine = textArea.getLineOfOffset(offset + len);

			int firstLineOffset = textArea.getLineStartOffset(firstLine);
			int lastLineOffset = 0;

			if (lastLine + 1 >= textArea.getLineCount()) {
				lastLineOffset = doc.getLength();

			} else {
				lastLineOffset = textArea.getLineStartOffset(lastLine + 1) - 1;
			}

			String text = doc.getText(firstLineOffset,
					lastLineOffset - firstLineOffset);
			text = operation.apply(text);

			doc.remove(firstLineOffset, lastLineOffset - firstLineOffset);
			doc.insertString(firstLineOffset, text, null);
		} catch (BadLocationException ignorable) {
		}
	}

	/**
	 * @param text
	 *            Text given for line sorting.
	 * @param comparator
	 *            Given String comparator.
	 * @return Sorts the string using given comparator.
	 */
	private String sortText(String text, Comparator<String> comparator) {
		String[] split = text.split("\\r?\\n");

		if (split.length == 1) {
			return text;
		}

		boolean noSwap = false;
		while (!noSwap) {
			noSwap = true;

			for (int i = 0; i < split.length - 1; i++) {
				if (comparator.compare(split[i], split[i + 1]) > 0) {
					String help = new String(split[i + 1]);
					split[i + 1] = new String(split[i]);
					split[i] = new String(help);
					noSwap = false;
				}
			}

		}

		// Rebuild the original string
		StringBuilder sb = new StringBuilder();

		for (String string : split) {
			sb.append(string);
			sb.append('\n');
		}

		String sorted = sb.toString();
		sorted = sorted.substring(0, sorted.length() - 1);

		return sorted;
	}

	/**
	 * Check if the document at the selected intex is modified. If true, a
	 * message box will appear.
	 * 
	 * @param selectedIndex
	 *            Index of the document tab.
	 * @return True if it is not modified, otherwise false.
	 */
	private boolean isUnmodified(int selectedIndex) {
		return tabbedPane.getIconAt(selectedIndex).equals(unmodifiedIcon);
	}

	/**
	 * Remove current tab.
	 * 
	 * @return True if tab is sucessfully removed, false otherwise.
	 */
	private boolean removeCurrentTab() {
		int selectedIndex = tabbedPane.getSelectedIndex();

		if (selectedIndex < 0) {
			return false;
		}

		// Check if it modified
		if (isUnmodified(selectedIndex)) {
			tabbedPane.remove(selectedIndex);
			return true;
		}

		int dialog = JOptionPane.showConfirmDialog(JNotepadPP.this,
				"Do you want to save " + tabbedPane.getTitleAt(selectedIndex)
						+ " document?",
				"Save document?", JOptionPane.YES_NO_OPTION);

		if (dialog == JOptionPane.NO_OPTION) {
			tabbedPane.remove(selectedIndex);
			return true;
		}

		String curPath = tabbedPane.getToolTipTextAt(selectedIndex);
		Path path = null;
		if (curPath == null) {
			// Ask user where to save the document
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Save file");
			if (fc.showSaveDialog(
					JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
				JOptionPane.showMessageDialog(JNotepadPP.this,
						"Document won't be saved.", "Info message",
						JOptionPane.INFORMATION_MESSAGE);
				return false;
			}

			path = fc.getSelectedFile().toPath();

		} else {
			path = Paths.get(curPath);
		}

		saveDocument(path, selectedIndex);
		tabbedPane.remove(selectedIndex);
		return true;
	}

	/**
	 * Removes all tabs.
	 */
	private void removeAllTabs() {
		while (removeCurrentTab()) {
			// Do nothing
		}

		int tabCount = tabbedPane.getTabCount();
		if (tabCount > 0) {
			return;
		}
	}

	/**
	 * @param editor
	 *            Reference to the editor.
	 * @param remove
	 *            True if caller wants to also remove highlighted text, false
	 *            otherwise.
	 * @return Highlighted text.
	 */
	private String getHighlightedText(JTextArea editor, boolean remove) {

		int len = Math
				.abs(editor.getCaret().getDot() - editor.getCaret().getMark());

		if (len == 0) {
			return null;
		}

		int offset = Math.min(editor.getCaret().getDot(),
				editor.getCaret().getMark());
		Document doc = editor.getDocument();

		Segment text = new Segment();
		try {
			doc.getText(offset, len, text);

			if (remove) {
				doc.remove(offset, len);
			}
		} catch (BadLocationException ignorable) {
		}

		return text.toString();
	}

	/**
	 * Loads image icon from hr.fer.zemris.java.hw11.jnotepadpp.icons package.
	 * 
	 * @param resource
	 *            Given resource icon to load.
	 * @return Appropriate icons object.
	 */
	private ImageIcon loadImageIcon(String resource) {
		InputStream is = this.getClass().getResourceAsStream(resource);

		if (is == null) {
			throw new IllegalArgumentException("Invalid resource path given.");
		}

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int nRead;
		byte data[] = new byte[4096];

		try {
			while ((nRead = is.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}

			byte[] byteImg = buffer.toByteArray();
			buffer.close();
			is.close();
			return new ImageIcon(byteImg);

		} catch (IOException e) {
			throw new IllegalArgumentException(
					"Error occured while reading from given path");
		}

	}

	/**
	 * For the given text area reference generates its listener.
	 * 
	 * @return Listener for the given text area.
	 */
	private DocumentListener getDocumentListener() {
		return new DocumentListener() {

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
			 * Updates the icon when change is detected.
			 */
			void update() {
				int index = tabbedPane.getSelectedIndex();
				tabbedPane.setIconAt(index, modifiedIcon);
			}
		};
	}

	/**
	 * @return Returns a change listener for setting setting titles in GUI.
	 */
	private ChangeListener getChangeListener() {
		return new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				int selectedIndex = tabbedPane.getSelectedIndex();

				if (selectedIndex < 0) {
					JNotepadPP.this.setTitle("JNotepad++");
					return;
				}

				if (tabbedPane.getToolTipTextAt(selectedIndex) == null) {
					JNotepadPP.this
							.setTitle(tabbedPane.getTitleAt(selectedIndex)
									+ " - JNotepad++");
					return;
				}

				JNotepadPP.this
						.setTitle(tabbedPane.getToolTipTextAt(selectedIndex)
								+ " - JNotepad++");
			}
		};
	}

}
