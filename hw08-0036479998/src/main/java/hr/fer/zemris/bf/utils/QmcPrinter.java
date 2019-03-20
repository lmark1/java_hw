package hr.fer.zemris.bf.utils;

import java.util.List;

import hr.fer.zemris.bf.model.BinaryOperatorNode;
import hr.fer.zemris.bf.model.ConstantNode;
import hr.fer.zemris.bf.model.Node;
import hr.fer.zemris.bf.model.NodeVisitor;
import hr.fer.zemris.bf.model.UnaryOperatorNode;
import hr.fer.zemris.bf.model.VariableNode;

/**
 * Printer used for printing minimal functions for QMC methode.
 * 
 * @author Lovro Marković
 *
 */
public class QmcPrinter implements NodeVisitor {

	/**
	 * String representation of expression.
	 */
	private String expression;

	/**
	 * Default constructor for this object.
	 */
	public QmcPrinter() {
		expression = new String();
	}

	/**
	 * Getter for the expression string.
	 * @return Expression string.
	 */
	public String getExpression() {
		return expression;
	}
	
	@Override
	public void visit(ConstantNode node) {
		expression += node.getValue() ? "TRUE" : "FALSE";
	}

	@Override
	public void visit(VariableNode node) {
		expression += node.getValue();
	}

	@Override
	public void visit(UnaryOperatorNode node) {
		expression += "NOT ";
		node.getChild().accept(this);
	}

	@Override
	public void visit(BinaryOperatorNode node) {
		List<Node> children = node.getChildren();

		for (int i = 0, size = children.size(); i < size; i++) {
			Node child = children.get(i);

			child.accept(this);
			if (i + 1 < children.size())
					expression += " " + node.getName().toUpperCase() + " ";
		}
	}

}
