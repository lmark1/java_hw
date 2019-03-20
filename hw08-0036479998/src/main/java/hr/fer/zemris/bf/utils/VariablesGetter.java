package hr.fer.zemris.bf.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import hr.fer.zemris.bf.model.BinaryOperatorNode;
import hr.fer.zemris.bf.model.ConstantNode;
import hr.fer.zemris.bf.model.Node;
import hr.fer.zemris.bf.model.NodeVisitor;
import hr.fer.zemris.bf.model.UnaryOperatorNode;
import hr.fer.zemris.bf.model.VariableNode;

/**
 * This class implements NodeVisitor. It searches for and saves all unique variables
 * in the expression in lexicographic order.
 * 
 * @author Lovro Marković
 *
 */
public class VariablesGetter implements NodeVisitor{
	
	/**
	 * Unique list of variables.
	 */
	private Set<String> variables;
	
	/**
	 * Defualt constructor. Initializes unique list variable.
	 */
	public VariablesGetter() {
		variables = new TreeSet<>();
	}
	
	@Override
	public void visit(ConstantNode node) {
		return;	
	}

	@Override
	public void visit(VariableNode node) {		
		variables.add(node.getValue());
	}

	@Override
	public void visit(UnaryOperatorNode node) {
		node.getChild().accept(this);		
	}

	@Override
	public void visit(BinaryOperatorNode node) {
		List<Node> children = node.getChildren();
		
		for (Node child : children) {
			child.accept(this);
		}
	}
	
	/**
	 * Getter for the variable list.
	 * 
	 * @return Sorted variable list.
	 */
	public List<String> getVariables() {
		return new ArrayList<>(variables);
	}
}
