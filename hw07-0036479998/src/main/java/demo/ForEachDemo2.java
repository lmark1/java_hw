package demo;

import java.util.Arrays;
import java.util.List;

import hr.fer.zemris.bf.model.Node;
import hr.fer.zemris.bf.parser.Parser;
import hr.fer.zemris.bf.utils.ExpressionEvaluator;
import hr.fer.zemris.bf.utils.Util;
import hr.fer.zemris.bf.utils.VariablesGetter;

@SuppressWarnings("javadoc")
public class ForEachDemo2 {

	public static void main(String[] args) {
		Node expression = new Parser("A and b or C").getExpression();
		
		VariablesGetter getter = new VariablesGetter();
		expression.accept(getter);
		
		List<String> variables = getter.getVariables();
		
		ExpressionEvaluator eval = new ExpressionEvaluator(variables);
		
		Util.forEach(
				variables,
				values -> {
					eval.setValues(values);
					expression.accept(eval);
					System.out.println(Arrays.toString(values)
							.replaceAll("true", "1")
							.replaceAll("false", "0") + 
							" ==> " + (eval.getResult() ? "1" : "0"));
				}
		);
	}
	
}
