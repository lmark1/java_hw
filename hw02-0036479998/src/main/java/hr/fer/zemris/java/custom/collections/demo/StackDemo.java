package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;

/**
 * Program demonstrates solving expressions using ObjectStack class. Expression
 * must be in postfix representation. When starting program from console the user
 * must enclose whole expression into quotation marks, so that your program
 * always gets just one argument. In expressions, you can assume that everything
 * is separated by one (or more) spaces. Each operator takes two preceding
 * numbers and replaces them with operation result. You must support only +, -,
 * /, * and % (remainder of integer division).
 * 
 * @author Lovro Marković
 *
 */
public class StackDemo {

	/**
	 * Main method of the class.
	 * 
	 * @param args
	 *            Accepts only 1 command line argument.
	 */
	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println("Program needs only one argument.");
			System.exit(1);

		}

		String[] input = args[0].split("\\s+");
		ObjectStack stack = new ObjectStack();

		for (String string : input) {

			try {
				Integer value = Integer.parseInt(string);
				stack.push(value);
				continue;

			} catch (NumberFormatException e) {

				int num1, num2;
				try {
					// Get value of first 2 Objects
					Integer numObj1 = (Integer) stack.pop();
					Integer numObj2 = (Integer) stack.pop();
					num1 = numObj1.intValue();
					num2 = numObj2.intValue();

				} catch (EmptyStackException ex) {
					System.out.println("No elements in stack.");
					break;
				}

				stack = checkOperatorAndPush(stack, string, num1, num2);
			}
		}

		if (stack.size() != 1) {
			System.out.println("Invalid expression.");
		} else {
			Integer intSolution = (Integer) stack.pop();
			System.out.printf("Expression evaluates to %d.%n", intSolution.intValue());
		}

	}
	
	/**
	 * Checks if the passed string is an operator, pushes the result of the operation on stack.
	 * If there is a division by zero or the string is invalid, a message is printed and the program exits.
	 * 
	 * @param stack Reference for instance of {@link ObjectStack}.
	 * @param string String possibly
	 * @param num1 Number that the operator will be used on.
	 * @param num2 Number that the operator will be used on.
	 * @return Updated stack reference.
	 */
	private static ObjectStack checkOperatorAndPush(ObjectStack stack, String string, int num1, int num2) {
		
		switch (string) {

		case "+":
			int sum = num1 + num2;
			stack.push(new Integer(sum));
			break;

		case "-":
			int diff = num2 - num1;
			stack.push(new Integer(diff));
			break;

		case "*":
			int prod = num2 * num1;
			stack.push(prod);
			break;
			
		case "/":
			if (num1 == 0) {
				System.out.println("Division by zero.");
				System.exit(1);
			}
			int div = num2 / num1;
			stack.push(new Integer(div));
			break;

		case "%":
			if (num1 == 0) {
				System.out.println("Division by zero.");
				System.exit(1);
			}
			int modulo = num2 % num1;
			stack.push(new Integer(modulo));
			break;

		default:
			System.out.println("Invalid expression - Input only numbers and operators");
			System.exit(1);
		}
		
		return stack;
	}

}
