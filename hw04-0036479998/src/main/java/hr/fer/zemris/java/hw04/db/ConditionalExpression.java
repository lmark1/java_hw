package hr.fer.zemris.java.hw04.db;

/**
 * Model of the complete conditional expression.
 * 
 * @author Lovro Marković
 *
 */
public class ConditionalExpression {

	/**
	 * Variable which defines the getter strategy.
	 */
	private IFieldValueGetter getterStrategy;
	
	/**
	 * Variable which defines the comparison strategy.
	 */
	private IComparisonOperator comparisonStrategy;
	
	/**
	 * Variable which stores the string literal. 	
	 */
	private String stringLiteral;
	
	/**
	 * Conditional expression constructor. Initializes variables for the getter strategy,
	 * comparison strategy, and the string literal.
	 * 
	 * @param getterStrategy Strategy which determines what field will be analysed.
	 * @param stringLiteral Inital value for the string literal.
	 * @param comparisonStrategy Strategy which determines what comparison will be used.
	 */
	public ConditionalExpression(
			IFieldValueGetter getterStrategy, 
			String stringLiteral, 
			IComparisonOperator comparisonStrategy) {
		
		this.getterStrategy = getterStrategy;
		this.comparisonStrategy = comparisonStrategy;
		this.stringLiteral = stringLiteral;
	}
	
	/**
	 * Getter for the comparison strategy.
	 * 
	 * @return This comparison strategy.
	 */
	public IComparisonOperator getComparisonOperator() {
		return comparisonStrategy;
	}
	
	/**
	 * Getter for the getter strategy.
	 * 
	 * @return This getter strategy.
	 */
	public IFieldValueGetter getFieldGetter() {
		return getterStrategy;
	}
	
	/**
	 * Getter for the string literal.
	 * 
	 * @return This string literal.
	 */
	public String getStringLiteral() {
		return stringLiteral;
	}	
}
