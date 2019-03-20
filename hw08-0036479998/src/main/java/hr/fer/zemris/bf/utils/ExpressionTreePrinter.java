package hr.fer.zemris.bf.utils;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.bf.model.BinaryOperatorNode;
import hr.fer.zemris.bf.model.ConstantNode;
import hr.fer.zemris.bf.model.Node;
import hr.fer.zemris.bf.model.NodeVisitor;
import hr.fer.zemris.bf.model.UnaryOperatorNode;
import hr.fer.zemris.bf.model.VariableNode;

/**
 * This class implements NodeVisitor. It visits all nodes in a tree and 
 * writes out their names. Each will be 2 spaces shifted.
 * 
 * @author Lovro Marković
 *
 */
public class ExpressionTreePrinter implements NodeVisitor{
	
	/**
	 * Number of spaces that the node name will be shifted.
	 */
	private int spaceNumber;
	
	/**
	 * Output list of Strings generetad by the visitor.
	 * Used for comparing with the original correct list.
	 */
	private List<String> outputList;
	
	/**
	 * Default object constructor. Initializes shift number variable.
	 */
	public ExpressionTreePrinter() {
		spaceNumber = 0;
		outputList = new ArrayList<>();
	}
	
	@Override
	public void visit(ConstantNode node) {
		String value = node.getValue() ? "1" : "0";
		String output = spaceGenerator() + value;
		
		System.out.println(output);
		outputList.add(output);
	}

	@Override
	public void visit(VariableNode node) {
		String output = spaceGenerator() + node.getValue();
		
		System.out.println(output);
		outputList.add(output);
	}

	@Override
	public void visit(UnaryOperatorNode node) {
		String output = spaceGenerator() + node.getName();
		System.out.println(output);
		outputList.add(output);		

		spaceNumber += 2;
		node.getChild().accept(this);
		spaceNumber -= 2;
	}

	@Override
	public void visit(BinaryOperatorNode node) {
		String output = spaceGenerator() + node.getName();
		System.out.println(output);
		outputList.add(output);
		
		spaceNumber += 2;
		List<Node> children = node.getChildren();
		
		for (Node child : children) {
			child.accept(this);
		}
		spaceNumber -=2;
	}
	
	/**
	 * Generates as many spaces as the space number specifies.
	 * 
	 * @return String with spaces.
	 */
	private String spaceGenerator() {
		StringBuilder newString = new StringBuilder();
		
		for (int i = 0; i < spaceNumber; i++) {
			newString.append(" ");
		}
		
		return newString.toString();
	}
	
	/**
	 * Getter for the generated output list.
	 * 
	 * @return Output list.
	 */
	public List<String> getOutputList() {
		return outputList;
	}
}
