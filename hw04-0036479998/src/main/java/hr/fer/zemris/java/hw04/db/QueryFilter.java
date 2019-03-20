package hr.fer.zemris.java.hw04.db;

import java.util.List;

/**
 * Class which implements IFilter interface. It defines the method for filtering
 * student records.
 * 
 * @author Lovro Marković
 *
 */
public class QueryFilter implements IFilter {

	/**
	 * Variable stores all conditional expressions defined by the query.
	 */
	private List<ConditionalExpression> expressionList;

	/**
	 * Constructor for QueryFilter objects, initalizes the expressionList
	 * variable.
	 * 
	 * @param expressionList
	 *            Inital expression list.
	 */
	public QueryFilter(List<ConditionalExpression> expressionList) {
		this.expressionList = expressionList;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Condition is defined by the given expressionList.
	 * 
	 * @throws IllegalArgumentException
	 *             Exception thrown when literal string with too many wild card
	 *             character is passed.
	 */
	@Override
	public boolean accepts(StudentRecord record) {
		boolean expressionResult = true;

		for (ConditionalExpression conditionalExpression : expressionList) {
			boolean newCondition = conditionalExpression.getComparisonOperator().satisfied(
					conditionalExpression.getFieldGetter().get(record), conditionalExpression.getStringLiteral());

			expressionResult = expressionResult && newCondition;
		}
		
		return expressionResult;
	}

}
