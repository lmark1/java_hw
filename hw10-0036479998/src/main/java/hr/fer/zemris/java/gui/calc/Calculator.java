package hr.fer.zemris.java.gui.calc;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;

/**
 * This class represents a standard calculator GUI.
 * 
 * @author Lovro Marković
 *
 */
public class Calculator extends JFrame {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Container for the calculator frame.
	 */
	private static Container cp;

	/**
	 * Internal calculator stack used for 'push' and 'pop' function.
	 */
	private static Stack<Double> calculatorStack;

	/**
	 * Stack used for calculating binary operations.
	 */
	private static Stack<Double> operationStack;

	/**
	 * Calculator display reference.
	 */
	private static JLabel display;

	/**
	 * Currently pressed operator.
	 */
	private static BinaryOperator<Double> currentOperator;

	/**
	 * This flag will be true when any operation is finished. Set to false again
	 * when first number is typed. Used to check if it's ok to overwrite current
	 * disply on the calculator.
	 */
	private static boolean operationFinished = false;

	/**
	 * Map used for mapping unary functions to their names.
	 */
	private static Map<String, UnaryOperator<Double>> unaryMap;

	/**
	 * Map used for mapping binary operations to their names.
	 */
	private static Map<String, BinaryOperator<Double>> binaryMap;

	/**
	 * List of all buttons that switch operations.
	 */
	private static List<JButton> switchButtons = new ArrayList<>();

	/**
	 * Main method of the program. Executes when program is run.
	 * 
	 * @param args
	 *            Accepts no command line arguments.
	 */
	public static void main(String[] args) {
		JFrame calc = new Calculator();
		
		// Listener for limiting windows size
		calc.addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent e) {}
			
			@Override
			public void componentResized(ComponentEvent e) {
				Dimension size = calc.getSize();
                Dimension max = calc.getMaximumSize();
                if (size.getWidth() > max.getWidth())
                {
                    calc.setSize((int)max.getWidth() ,(int) max.getHeight());
                }
                if (size.getHeight() > max.getHeight())
                {
                    calc.setSize((int)max.getWidth() ,(int) max.getHeight());
                }
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {}
			
			@Override
			public void componentHidden(ComponentEvent e) {}
		});
		
		SwingUtilities.invokeLater(() -> {
			calc.setVisible(true);
		});
	}

	/**
	 * Calculator constructor.
	 */
	public Calculator() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLocation(50, 50);
		setSize(500, 400);
		setTitle("MyCalculator");
		calculatorStack = new Stack<>();
		operationStack = new Stack<>();
		
		initGUI();
	}

	/**
	 * Initializes calculator GUI.
	 */
	private void initGUI() {
		cp = getContentPane();
		cp.setLayout(new CalcLayout(10));

		// Add display
		display = new JLabel("0", SwingConstants.RIGHT);
		display.setBackground(Color.YELLOW);
		display.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		display.setOpaque(true);
		display.setFont(display.getFont().deriveFont(32.0f));
		cp.add(display, new RCPosition(1, 1));

		addAuxiliaryButtons();
		addNumberButtons();
		addBinaryOperators();
		addUnaryOperators();
	}

	/**
	 * Initializes unary function map.
	 */
	private void initializeUnaryFunction() {
		unaryMap = new HashMap<>();

		unaryMap.put("sin", (a) -> Math.sin(a));
		unaryMap.put("asin", (a) -> Math.asin(a));
		unaryMap.put("cos", (a) -> Math.cos(a));
		unaryMap.put("acos", (a) -> Math.acos(a));
		unaryMap.put("tan", (a) -> Math.tan(a));
		unaryMap.put("atan", (a) -> Math.atan(a));
		unaryMap.put("ctg", (a) -> {
			if (Math.abs(a) < 10E-7) {
				throw new IllegalStateException("Error.");
			}
			return 1.0 / Math.tan(a);
		});
		unaryMap.put("actg", (a) -> {
			if (Math.abs(a) < 10E-7) {
				throw new IllegalStateException("Error.");
			}
			return Math.atan(1.0 / a);
		});

		unaryMap.put("log", (a) -> {
			if (a <= 1) {
				throw new IllegalStateException("Error");
			}
			return Math.log10(a);
		});
		unaryMap.put("10^", (a) -> Math.pow(10, a));
		unaryMap.put("ln", (a) -> {
			if (a <= 1) {
				throw new IllegalStateException("Error");
			}
			return Math.log(a);
		});
		unaryMap.put("e^", (a) -> Math.exp(a));
		unaryMap.put("1/x", (a) -> {
			if (a == 0) {
				throw new IllegalStateException("Division by zero!");
			}

			return 1.0 / a;
		});
	}

	/**
	 * Initializes binaryMap.
	 */
	private void initializeBinaryFunctions() {
		binaryMap = new HashMap<>();

		binaryMap.put("+", (a, b) -> a + b);
		binaryMap.put("-", (a, b) -> a - b);
		binaryMap.put("*", (a, b) -> a * b);
		binaryMap.put("/", (a, b) -> {
			if (b == 0) {
				throw new IllegalStateException("Division by zero!");
			}
			return a / b;
		});
		binaryMap.put("x^n", (a, b) -> Math.pow(a, b));
		binaryMap.put("nSqrt", (a, b) -> Math.pow(a, 1 / b));
	}

	/**
	 * Calculates the result of current binary operation and updates the
	 * display.
	 */
	private void calculateBinaryOperation() {
		double op1 = operationStack.pop();
		double op2 = operationStack.pop();

		try {
			double result = currentOperator.apply(op2, op1);
			if (result % 1 == 0) {
				display.setText(String.valueOf((int) result));
			} else {
				display.setText(String.valueOf(result));
			}

			operationStack.push(result);
			operationFinished = true;
			
		} catch (IllegalStateException e) {
			display.setText(e.getMessage());
			operationFinished = true;
			currentOperator = null;
			operationStack.clear();
			return;
		}
	}

	/**
	 * Add all buttons representing binary operators.
	 */
	private void addBinaryOperators() {
		ActionListener binaryListener = (e) -> {
			BinaryOperatorButton src = (BinaryOperatorButton) e.getSource();

			double value = 0;

			try {
				value = Double.parseDouble(display.getText());
			} catch (NumberFormatException e1) {
				return;
			}
			
			if (operationStack.size() == 1) {
				operationStack.push(value);
				
				if (currentOperator == null) {
					currentOperator = binaryMap.get(src.getText());
		//			calculateBinaryOperation();
				} else {
					calculateBinaryOperation();					
					currentOperator = binaryMap.get(src.getText());
				}
			
			} else if (operationStack.size() == 0) {
				operationStack.push(value);
				currentOperator = binaryMap.get(src.getText());
			
			} else if (operationStack.size() == 2) {
				calculateBinaryOperation();
				currentOperator = binaryMap.get(src.getText());
			}
			
			operationFinished = true;
		};

		initializeBinaryFunctions();
		cp.add(new BinaryOperatorButton("+", binaryListener),
				new RCPosition(5, 6));
		cp.add(new BinaryOperatorButton("-", binaryListener),
				new RCPosition(4, 6));
		cp.add(new BinaryOperatorButton("*", binaryListener),
				new RCPosition(3, 6));
		cp.add(new BinaryOperatorButton("/", binaryListener),
				new RCPosition(2, 6));

		BinaryOperatorButton pot = new BinaryOperatorButton("x^n",
				binaryListener);
		cp.add(pot, new RCPosition(5, 1));
		switchButtons.add(pot);
	}

	/**
	 * Add all unary operators.
	 */
	private void addUnaryOperators() {
		initializeUnaryFunction();

		ActionListener unaryListener = (e) -> {
			UnaryOperatorButton src = (UnaryOperatorButton) e.getSource();

			double value = 0;

			try {
				value = Double.parseDouble(display.getText());
			} catch (NumberFormatException e1) {
				return;
			}

			double result = 0;
			try {
				result = unaryMap.get(src.getText()).apply(value);
			} catch (IllegalStateException e1) {
				display.setText(e1.getMessage());
				operationFinished = true;
				return;
			}

			if (!operationStack.isEmpty()) {
				operationStack.pop();
			}
			
			operationStack.push(result);

			if (result % 1 == 0) {
				display.setText(String.valueOf((int) result));
			} else {
				display.setText(String.valueOf(result));
			}
			
			operationFinished = true;
		};

		UnaryOperatorButton b1 = new UnaryOperatorButton("sin", unaryListener);
		UnaryOperatorButton b2 = new UnaryOperatorButton("cos", unaryListener);
		UnaryOperatorButton b3 = new UnaryOperatorButton("tan", unaryListener);
		UnaryOperatorButton b4 = new UnaryOperatorButton("ctg", unaryListener);
		UnaryOperatorButton b5 = new UnaryOperatorButton("log", unaryListener);
		UnaryOperatorButton b6 = new UnaryOperatorButton("ln", unaryListener);

		switchButtons.add(b1);
		switchButtons.add(b2);
		switchButtons.add(b3);
		switchButtons.add(b4);
		switchButtons.add(b5);
		switchButtons.add(b6);

		cp.add(b1, new RCPosition(2, 2));
		cp.add(b2, new RCPosition(3, 2));
		cp.add(b3, new RCPosition(4, 2));
		cp.add(b4, new RCPosition(5, 2));
		cp.add(b5, new RCPosition(3, 1));
		cp.add(b6, new RCPosition(4, 1));
		cp.add(new UnaryOperatorButton("1/x", unaryListener),
				new RCPosition(2, 1));
	}

	/**
	 * Add all auxiliary buttons such as "clr", "=" etc.
	 */
	private void addAuxiliaryButtons() {
		// Add clear button
		cp.add(new AuxiliaryButton("clr", (e) -> {
			display.setText("0");
		}), new RCPosition(1, 7));

		// Add equals button
		cp.add(new AuxiliaryButton("=", (e) -> {
			double value = 0;

			try {
				value = Double.parseDouble(display.getText());
			} catch (NumberFormatException e1) {
				return;
			}

			operationStack.push(value);

			if (operationStack.size() <= 1 || currentOperator == null) {
				return;
			}

			calculateBinaryOperation();

			currentOperator = null;
			operationStack.clear();
		}), new RCPosition(1, 6));

		// Add reset button
		cp.add(new AuxiliaryButton("res", (e) -> {
			display.setText("0");
			calculatorStack.clear();
			operationStack.clear();
			currentOperator = null;
		}), new RCPosition(2, 7));

		// Add push button
		cp.add(new AuxiliaryButton("push", (e) -> {
			Double value = null;

			try {
				value = Double.parseDouble(display.getText());
			} catch (NumberFormatException e1) {
				return;
			}

			calculatorStack.push(value);
			operationFinished = true;
		}), new RCPosition(3, 7));

		// Add pop button
		cp.add(new AuxiliaryButton("pop", (e) -> {

			if (calculatorStack.isEmpty()) {
				JOptionPane.showMessageDialog(Calculator.this, "Stack is empty",
						"Empty stack warning!", JOptionPane.ERROR_MESSAGE);
				return;
			}

			Double stackValue = calculatorStack.pop();
			if (stackValue % 1 == 0) {
				display.setText(String.valueOf(stackValue.intValue()));
			} else {
				display.setText(String.valueOf(stackValue));
			}

		}), new RCPosition(4, 7));

		// Add comma button
		cp.add(new AuxiliaryButton(".", (e) -> {

			String value = display.getText();
			if (value.contains(".")) {
				return;
			}

			display.setText(value + ".");
		}), new RCPosition(5, 5));

		// Add sign button
		cp.add(new AuxiliaryButton("+/-", (e) -> {

			String value = display.getText();
			if (value.startsWith("-")) {
				String newValue = value.substring(1, value.length() - 1);
				if (newValue.length() == 0) {
					newValue = "0";
				}
				display.setText(newValue);
			} else {
				display.setText("-" + value);
			}
		}), new RCPosition(5, 4));

		// Add checkbox
		JCheckBox check = new JCheckBox("inv");
		check.setBackground(Color.CYAN);
		check.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		check.setOpaque(true);
		check.setBorderPainted(true);
		check.setFont(check.getFont().deriveFont(16f));
		check.addActionListener((e) -> {
			switchOperators();
		});
		cp.add(check, new RCPosition(5, 7));

	}

	/**
	 * Switches all the button names in the switch button list to their inverse
	 * operation.
	 */
	private void switchOperators() {
		for (JButton button : switchButtons) {
			switch (button.getText()) {
			
			case "sin":
				button.setText("asin");
				break;
			
			case "asin":
				button.setText("sin");
				break;
				
			case "cos":
				button.setText("acos");
				break;
				
			case "acos":
				button.setText("cos");
				break;
				
			case "tan":
				button.setText("atan");
				break;
			
			case "atan":
				button.setText("tan");
				break;
				
			case "ctg":
				button.setText("actg");
				break;
				
			case "actg":
				button.setText("ctg");
				break;
					
			case "log":
				button.setText("10^");
				break;
				
			case "10^":
				button.setText("log");
				break;
				
			case "ln":
				button.setText("e^");
				break;
				
			case "e^":
				button.setText("ln");
				break;
				
			case "x^n":
				button.setText("nSqrt");
				break;
				
			case "nSqrt":
				button.setText("x^n");
				break;
				
			default:
				break;
			}
		}
	}

	/**
	 * Adds all number buttons.
	 */
	private void addNumberButtons() {
		ActionListener listener = (e) -> {
			NumberButton button = (NumberButton) e.getSource();
			String current = display.getText();
			String newText = current + button.getNumber();
			
			try {
					
				if (operationFinished) {
					operationFinished = false;
					display.setText(button.getNumber());
				} else if (newText.contains(".")) {
					display.setText(newText);
				} else {
					Integer value = Integer.parseInt(newText);
					display.setText(value.toString());
				}
			} catch (NumberFormatException e1) {
				operationFinished = true;
				display.setText(button.getNumber());
				return;
			}
		};
		cp.add(new NumberButton("0", display, listener), new RCPosition(5, 3));
		cp.add(new NumberButton("1", display, listener), new RCPosition(4, 3));
		cp.add(new NumberButton("2", display, listener), new RCPosition(4, 4));
		cp.add(new NumberButton("3", display, listener), new RCPosition(4, 5));
		cp.add(new NumberButton("4", display, listener), new RCPosition(3, 3));
		cp.add(new NumberButton("5", display, listener), new RCPosition(3, 4));
		cp.add(new NumberButton("6", display, listener), new RCPosition(3, 5));
		cp.add(new NumberButton("7", display, listener), new RCPosition(2, 3));
		cp.add(new NumberButton("8", display, listener), new RCPosition(2, 4));
		cp.add(new NumberButton("9", display, listener), new RCPosition(2, 5));
	}
}