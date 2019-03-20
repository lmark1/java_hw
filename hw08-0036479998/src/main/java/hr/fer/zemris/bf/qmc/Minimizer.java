package hr.fer.zemris.bf.qmc;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import hr.fer.zemris.bf.model.BinaryOperatorNode;
import hr.fer.zemris.bf.model.ConstantNode;
import hr.fer.zemris.bf.model.Node;
import hr.fer.zemris.bf.model.UnaryOperatorNode;
import hr.fer.zemris.bf.model.VariableNode;
import hr.fer.zemris.bf.utils.QmcPrinter;
import hr.fer.zemris.bf.utils.Util;

/**
 * This class represents minimization process Quinn - McCluskey. It implements
 * this process for one boolean function.
 * 
 * @author Lovro Marković
 *
 */
public class Minimizer {

	/**
	 * Logger used by this class.
	 */
	private static final Logger LOG = Logger.getLogger("hr.fer.zemris.bf.qmc");

	/**
	 * Set of Integer values representing minterms.
	 */
	private Set<Integer> mintermSet;

	/**
	 * Set of Integer values representing dont cares.
	 */
	private Set<Integer> dontCareSet;

	/**
	 * List of variables in the expression.
	 */
	private List<String> variables;

	/**
	 * Minimal forms of the function;
	 */
	private List<Set<Mask>> minimalForms;

	/**
	 * Primary implicants of the function.
	 */
	private Set<Mask> primCover;
	
	/**
	 * Constructor used for Minimizer class. Initializes all class variables.
	 * 
	 * @param mintermSet
	 *            Set of Integer values representing minterms.
	 * @param dontCareSet
	 *            Set of integer values representing dont cares.
	 * @param variables
	 *            List of variables.
	 * @throws IllegalArgumentException
	 *             Exception thrown if given sets contain same values or null
	 *             values are passed as arguments or index values are greater
	 *             than maximum index value.
	 */
	public Minimizer(Set<Integer> mintermSet, Set<Integer> dontCareSet,
			List<String> variables) {

		if (mintermSet == null || dontCareSet == null || variables == null) {
			throw new IllegalArgumentException(
					"Null value passed as an argument.");
		}

		checkNonOverlapping(mintermSet, dontCareSet,
				(int) Math.pow(2, variables.size()));

		this.mintermSet = new HashSet<>(mintermSet);
		this.dontCareSet = new HashSet<>(dontCareSet);
		this.variables = new ArrayList<>(variables);

		primCover = findPrimaryImplicants();
		if (!primCover.isEmpty()) {
			LOG.log(Level.FINE, "Svi primarni implikanti: ");

			for (Mask mask : primCover) {
				LOG.log(Level.FINE, mask.toString());
			}
		}

		minimalForms = chooseMinimalCover(primCover);
	}

	/**
	 * Getter for minimal forms.
	 * 
	 * @return Minimal forms.
	 */
	public List<Set<Mask>> getMinimalForms() {
		return minimalForms;
	}

	/**
	 * Get minimal forms as an expression.
	 * 
	 * @return Minimal forms as an expression
	 */
	public List<Node> getMinimalFormsAsExpression() {
		List<Node> expressionList = new ArrayList<>();

		if (minimalForms.isEmpty()) {
			expressionList.add(new ConstantNode(false));
		}

		// Generate an expression for each Set of masks
		for (Set<Mask> set : minimalForms) {
			List<Node> orNodes = new ArrayList<>();

			// Build node tree for each mask
			for (Mask mask : set) {
				List<Node> andNodes = new ArrayList<>();

				// Add all possible and nodes
				for (int i = 0, size = mask.size(); i < size; i++) {
					byte b = mask.getValueAt(i);

					if (b == 2) continue;
					else if (b == 1)
						andNodes.add(new VariableNode(variables.get(i)));
					else
						andNodes.add(new UnaryOperatorNode("not",
								new VariableNode(variables.get(i)), (u) -> !u));
				}

				// If its a single and node dont return it as an and node
				if (andNodes.isEmpty()) {
					orNodes.add(new ConstantNode(true));
				} else if (andNodes.size() == 1)
					orNodes.add(andNodes.get(0));
				else
					orNodes.add(new BinaryOperatorNode("and", andNodes,
							(a, b) -> a && b));
			}

			// If its a sinelg or node dont return it as an or node
			if (orNodes.isEmpty())
				continue;
			else if (orNodes.size() == 1)
				expressionList.add(orNodes.get(0));
			else
				expressionList.add(new BinaryOperatorNode("or", orNodes,
						(a, b) -> a || b));
		}

		return expressionList;
	}

	/**
	 * Generate string from minimal forms.
	 * 
	 * @return List of minimal forms strings.
	 */
	public List<String> getMinimalFormsAsString() {
		List<Node> expressions = getMinimalFormsAsExpression();
		List<String> stringExpressions = new ArrayList<>();

		for (Node expression : expressions) {
			QmcPrinter newPrinter = new QmcPrinter();
			expression.accept(newPrinter);
			stringExpressions.add(newPrinter.getExpression());
		}

		return stringExpressions;
	}

	/**
	 * Generates table of coverage of minterms by primary implicants and based
	 * on that it searches for minimal primary implicants to cover the table.
	 * 
	 * @param primCover
	 *            Set of primary implicants.
	 * @return Table of coverage.
	 */
	private List<Set<Mask>> chooseMinimalCover(Set<Mask> primCover) {

		// Izgradi polja implikanata i minterma (rub tablice):
		Mask[] implicants = primCover.toArray(new Mask[primCover.size()]);
		Integer[] minterms = mintermSet.toArray(new Integer[mintermSet.size()]);

		// Mapiraj minterm u stupac u kojem se nalazi:
		Map<Integer, Integer> mintermToColumnMap = new HashMap<>();
		for (int i = 0; i < minterms.length; i++) {
			Integer index = minterms[i];
			mintermToColumnMap.put(index, i);
		}

		// Napravi praznu tablicu pokrivenosti:
		boolean[][] table = buildCoverTable(implicants, minterms,
				mintermToColumnMap);
		
		// Donji redak tablice: koje sam minterme pokrio?
		boolean[] coveredMinterms = new boolean[minterms.length];

		// Pronađi primarne implikante...
		Set<Mask> importantSet = selectImportantPrimaryImplicants(implicants,
				mintermToColumnMap, table, coveredMinterms);

		LOG.log(Level.FINER, "");
		LOG.log(Level.FINER, "Bitni primarni implikanti su: ");
		if (importantSet.isEmpty()) {
			LOG.log(Level.FINER, "Nema bitnih primarnih implikanata");
		} else {

			for (Mask mask : importantSet) {
				LOG.log(Level.FINER, mask.toString());
			}
		}

		// Izgradi funkciju pokrivenosti:
		List<Set<BitSet>> pFunction = buildPFunction(table, coveredMinterms);

		LOG.log(Level.FINER, "");
		LOG.log(Level.FINER, "p funkcija je");
		if (pFunction.isEmpty()) {
			LOG.log(Level.FINER, "Svi mintermi su pokriveni");
		} else {
			LOG.log(Level.FINER, pFunction.toString());
		}

		// Pronađi minimalne dopune:
		Set<BitSet> minset = findMinimalSet(pFunction);

		LOG.log(Level.FINER, "");
		LOG.log(Level.FINER, "Minimalna pokrivanja još trebaju:");
		if (pFunction.isEmpty()) {
			LOG.log(Level.FINER, "Svi mintermi su pokriveni");
		} else {
			LOG.log(Level.FINER, minset.toString());
		}

		// Izgradi minimalne zapise funkcije:
		List<Set<Mask>> minimalForms = new ArrayList<>();
		if (minset.isEmpty() && !importantSet.isEmpty()) {
			minimalForms.add(new LinkedHashSet<>(importantSet));
		} else {

			for (BitSet bs : minset) {
				Set<Mask> set = new LinkedHashSet<>(importantSet);
				bs.stream().forEach(i -> set.add(implicants[i]));
				minimalForms.add(set);
			}
		}

		LOG.log(Level.FINE, "");
		LOG.log(Level.FINE, "Minimalni oblici funkcija su");
		if (minimalForms.isEmpty()) {
			LOG.log(Level.FINE, "Nisam pronašao minimalne oblike funkcije");
		} else {

			for (int i = 0; i < minimalForms.size(); i++) {
				String outString = String.format("%d. %s", i + 1,
						minimalForms.get(i).toString());
				LOG.log(Level.FINE, outString);
			}
		}

		return minimalForms;
	}

	/**
	 * Finds minimal set of given List of BitSet objects needed to cover the
	 * remaining part of the table.
	 * 
	 * @param pFunction
	 *            Function as a product of a sum of implicant indexes.
	 * @return Minimal set of indexes needed.
	 */
	private Set<BitSet> findMinimalSet(List<Set<BitSet>> pFunction) {
		if (pFunction.isEmpty())
			return new LinkedHashSet<>();

		Set<BitSet> currentSet = pFunction.get(0);
		for (int i = 1, len = pFunction.size(); i < len; i++) {
			Set<BitSet> newSet = new HashSet<>();
		
			for (BitSet bs1 : currentSet) {
				for (BitSet bs2 : pFunction.get(i)) {
					
					BitSet combinedBS = new BitSet();
					combinedBS.or(bs1);
					combinedBS.or(bs2);
					newSet.add((BitSet) combinedBS.clone());
				}
			}

			currentSet = new LinkedHashSet<>(newSet);
		}

		List<Set<BitSet>> currentList = new ArrayList<>();
		currentList.add(currentSet);

		try {
			LOG.log(Level.FINER, "");
			LOG.log(Level.FINER,
					"Nakon prevorbe p-funkcije u sumu produkata: ");
			LOG.log(Level.FINER, currentList.toString());
		} catch (OutOfMemoryError e) {
			LOG.log(Level.FINER,
					"OutOfMemoryException se desio pri pisanju na output");
		}

		Set<BitSet> minimumSet = new LinkedHashSet<>();
		
		try {

			Set<BitSet> sumAll = currentList.get(0);
			int minimumSize = -1;

			for (BitSet bitSet : sumAll) {
				if (minimumSize == -1 || minimumSize > bitSet.cardinality()) {
					minimumSize = bitSet.cardinality();
				}
			}

			for (BitSet bitSet : sumAll) {
				if (bitSet.cardinality() == minimumSize)
					minimumSet.add(bitSet);
			}
		} catch (IndexOutOfBoundsException e) {
		}

		return minimumSet;
	}

	/**
	 * This method builds function of coverage which will be used later in
	 * minimization.
	 * 
	 * @param table
	 *            Table of coverage.
	 * @param coveredMinterms
	 *            Covered minterms.
	 * @return Function of coverage.
	 */
	private List<Set<BitSet>> buildPFunction(boolean[][] table,
			boolean[] coveredMinterms) {

		List<Set<BitSet>> prodFunction = new ArrayList<>();

		for (int j = 0; j < coveredMinterms.length; j++) {
			if (coveredMinterms[j])
				continue;
			Set<BitSet> newEntry = new LinkedHashSet<>();
			;

			// Find all implicants that cover - uncovered minterms
			// and add them to BitSet set
			for (int i = 0; i < table.length; i++) {
				if (table[i][j]) {

					// Generate a new BitSet object with value set to index
					// add it to the Set

					BitSet newBitSet = new BitSet();
					newBitSet.set(i);
					newEntry.add(newBitSet);
				}
			}

			prodFunction.add(newEntry);
		}

		return prodFunction;
	}

	/**
	 * Find all important primary implicants. Each important primary implicant
	 * has to be the only implicant covering a minterm.
	 * 
	 * @param implicants
	 *            Given implicants.
	 * @param mintermToColumnMap
	 *            Map mapping minterms to columns.
	 * @param table
	 *            Cover table.
	 * @param coveredMinterms
	 *            Covered minterms.
	 * @return Important primary implicants.
	 */
	private Set<Mask> selectImportantPrimaryImplicants(Mask[] implicants,
			Map<Integer, Integer> mintermToColumnMap, boolean[][] table,
			boolean[] coveredMinterms) {
		Set<Mask> importantImplicants = new LinkedHashSet<>();

		// Go through all implicants...
		for (Mask implicant : implicants) {
			Set<Integer> indexes = implicant.getIndexes();

			// For each implicant, go through the minterm indexes they cover
			for (Integer index : indexes) {

				if (!mintermToColumnMap.containsKey(index))
					continue;
				int column = mintermToColumnMap.get(index);
				int coverCount = 0;

				// For each index covered find out how many other implicants
				// cover that minterm in the same column.
				for (int i = 0; i < table.length; i++) {
					if (table[i][column]) {
						coverCount++;
					}
				}

				// If it is the only implicant that covers the minterm index
				// than it is important implicant
				if (coverCount == 1) {
					importantImplicants.add(implicant);
				}
			}
		}

		// Set covered minterms
		for (Mask implicant : importantImplicants) {
			Set<Integer> indexes = implicant.getIndexes();

			for (Integer index : indexes) {

				if (!mintermToColumnMap.containsKey(index))
					continue;
				int column = mintermToColumnMap.get(index);
				coveredMinterms[column] = true;
			}
		}

		return importantImplicants;
	}

	/**
	 * Builds a cover table with rows as implicants and rows as minterms. If
	 * i-th implicant covers j-th minterm than the element at (i,j) position is
	 * true, otherwise false.
	 * 
	 * @param implicants
	 *            Given implicants.
	 * @param minterms
	 *            Given minterms.
	 * @param mintermToColumnMap
	 *            Map mapping minterms to columns.
	 * @return Generated cover table.
	 */
	private boolean[][] buildCoverTable(Mask[] implicants, Integer[] minterms,
			Map<Integer, Integer> mintermToColumnMap) {
		boolean[][] coverTable = new boolean[implicants.length][minterms.length];

		for (int i = 0; i < implicants.length; i++) {
			Set<Integer> indexes = implicants[i].getIndexes();

			for (Integer index : indexes) {

				if (!mintermToColumnMap.containsKey(index))
					continue;
				Integer column = mintermToColumnMap.get(index);
				if (column == null)
					continue;
				coverTable[i][column] = true;
			}
		}
		return coverTable;
	}

	/**
	 * Generates primary implicants from the given minterm and dont care set.
	 * 
	 * @return Set of primary implicants type Mask.
	 */
	private Set<Mask> findPrimaryImplicants() {
		Set<Mask> pImplicants = new LinkedHashSet<>();
		List<Set<Mask>> firstColumn = createFirstColumn();

		List<Set<Mask>> currentColumn = firstColumn;
		boolean stop = false;
		while (!stop) {
			List<Set<Mask>> nextColumn = createNextColumn(currentColumn);

			logCurrentColumn(currentColumn);
			Set<Mask> newImplicants = getImplicantsFromColumn(currentColumn);
			pImplicants.addAll(newImplicants);

			if (!newImplicants.isEmpty()) {

				for (Mask mask : newImplicants) {
					LOG.log(Level.FINEST,
							"Pronašao primarni implikant: " + mask);
				}
				LOG.log(Level.FINEST, "");
			}

			currentColumn = new ArrayList<>(nextColumn);

			// Check if current column is empty
			int numberOfGroups = currentColumn.size();
			int emptyGroups = 0;
			for (Set<Mask> set : currentColumn) {
				if (set.isEmpty())
					emptyGroups++;
			}
			if (emptyGroups == numberOfGroups)
				stop = true;
		}

		return pImplicants;
	}

	/**
	 * Logs current column data if the log level is set to FINER.
	 * 
	 * @param currentColumn
	 *            Given column.
	 */
	private void logCurrentColumn(List<Set<Mask>> currentColumn) {

		LOG.log(Level.FINER, "Stupac tablice:");
		LOG.log(Level.FINER, "=================================");

		boolean borderFlag = false;
		for (int i = 0; i < currentColumn.size(); i++) {

			if (currentColumn.get(i).isEmpty())
				continue;
			if (borderFlag) {
				LOG.log(Level.FINER, "-------------------------------");
			}

			for (Mask mask : currentColumn.get(i)) {
				LOG.log(Level.FINER, mask.toString());
			}

			if (!borderFlag) {
				borderFlag = true;
			}
		}
		LOG.log(Level.FINER, "");
	}

	/**
	 * Generates a set of primary implicants from given column.
	 * 
	 * @param currentColumn
	 *            Current column genrated using Quinn - McCluskey method.
	 * @return Set of primary implicants.
	 */
	private Set<Mask> getImplicantsFromColumn(List<Set<Mask>> currentColumn) {
		Set<Mask> implicants = new LinkedHashSet<>();

		for (Set<Mask> maskSet : currentColumn) {

			for (Mask mask : maskSet) {
				if (!mask.isCombined() && !mask.isDontCare())
					implicants.add(mask);
			}
		}
		return implicants;
	}

	/**
	 * Creates new column by combining groups of given column, based on the
	 * Quinn - McCluskey method.
	 * 
	 * @param currentColumn
	 *            Current column.
	 * @return New column.
	 */
	private List<Set<Mask>> createNextColumn(List<Set<Mask>> currentColumn) {
		List<Set<Mask>> newColumn = new ArrayList<>();
		int numberOfVariables = variables.size();

		// Fill the list with sets
		for (int i = 0; i < numberOfVariables; i++) {
			newColumn.add(new LinkedHashSet<>());
		}

		// Generate newColumn
		for (int i = 0, len = currentColumn.size(); i < len - 1; i++) {
			Set<Mask> currentSet = currentColumn.get(i);
			Set<Mask> nextSet = currentColumn.get(i + 1);

			// Combine neighbour sets
			for (Mask mask1 : currentSet) {
				for (Mask mask2 : nextSet) {
					Optional<Mask> optionalMask = mask1.combineWith(mask2);
					Mask newMask = optionalMask.orElse(null);

					if (newMask == null) {
						continue;
					} else {
						newColumn.get(i).add(newMask);
					}
				}
			}
		}

		return newColumn;
	}

	/**
	 * Creates the first column based on the given list of variables, set of
	 * minterms and dont cares.
	 * 
	 * @return First column.
	 */
	private List<Set<Mask>> createFirstColumn() {
		int numberOfVariables = variables.size();
		List<Set<Mask>> column = new ArrayList<>();

		// Fill the list with sets
		for (int i = 0; i <= numberOfVariables; i++) {
			column.add(new LinkedHashSet<>());
		}

		// Process minterms
		for (Integer value : mintermSet) {
			byte[] index = Util.indexToByteArray(value, numberOfVariables);
			int oneCount = numberOfOnes(index);
			column.get(oneCount).add(new Mask(value, numberOfVariables, false));
		}

		// Process dont cares
		for (Integer value : dontCareSet) {
			byte[] index = Util.indexToByteArray(value, numberOfVariables);
			int oneCount = numberOfOnes(index);
			column.get(oneCount).add(new Mask(value, numberOfVariables, true));
		}

		return column;
	}

	/**
	 * Returns number of bytes of value 1 in the given byte array.
	 * 
	 * @param index
	 *            Given byte array.
	 * @return Number of bytes value 1.
	 */
	private int numberOfOnes(byte[] index) {
		int count = 0;

		for (byte b : index) {
			if (b == 1) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Check if given sets overlap or contain values greater than maximum index.
	 * If any of the above happens exception is thrown.
	 * 
	 * @param mintermSet
	 *            Minterm set.
	 * @param dontCareSet
	 *            DontCare set.
	 * @param maxIndex
	 *            Maximum index value.
	 * @throws IllegalArgumentException
	 *             Exception thrown if given sets contain same values or null
	 *             values are passed as arguments or index values are greater
	 *             than maximum index value.
	 */
	private void checkNonOverlapping(Set<Integer> mintermSet,
			Set<Integer> dontCareSet, int maxIndex) {

		for (Integer value : mintermSet) {

			if (dontCareSet.contains(value)) {
				throw new IllegalArgumentException(
						"Pogreška: skup minterma i don't careova nije disjunktan.");
			}

			if (value > maxIndex) {
				throw new IllegalArgumentException("Minterm value " + value
						+ " is grater than maximum value " + maxIndex + ".");
			}
		}

		for (Integer value : dontCareSet) {
			if (value > maxIndex) {
				throw new IllegalArgumentException("Dont care value " + value
						+ " is grater than maximum value " + maxIndex + ".");
			}
		}
	}
	
}
