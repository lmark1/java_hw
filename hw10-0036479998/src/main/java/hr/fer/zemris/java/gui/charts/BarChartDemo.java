package hr.fer.zemris.java.gui.charts;

import java.awt.Container;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Demonstrative program for bar chart components.
 * 
 * @author Lovro Marković
 *
 */
public class BarChartDemo extends JFrame {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Main method of the progam. Executes when program starts.
	 * 
	 * @param args
	 *            Accepts no input arguments.
	 */
	public static void main(String[] args) {
		BarChartDemo barChart = new BarChartDemo();
		BarChart model = parseFile();		
		Container cp = barChart.getContentPane();
		cp.add(new BarChartComponent(model));
		
		SwingUtilities.invokeLater(() -> barChart.setVisible(true));
	}

	/**
	 * Parses a file and extracts bar data from it.
	 * 
	 * @return Bar chart object.
	 */
	private static BarChart parseFile() {
		List<String> lines = new ArrayList<>();

		try {
			lines = Files.readAllLines(Paths.get("./src/main/resources/bc1.txt"),
					StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.out.println("Can't load file.");
			System.exit(1);
		}
		
		try{
			String xLabel = lines.get(0);
			String yLabel = lines.get(1);
			List<XYValue> data = parseGraphData(lines.get(2));
			Integer yMin = Integer.parseInt(lines.get(3));
			Integer yMax = Integer.parseInt(lines.get(4));
			Integer delta = Integer.parseInt(lines.get(5));
			
			return new BarChart(data, xLabel, yLabel, yMin, yMax, delta);
			
		}catch (NumberFormatException e) {
			throw new IllegalArgumentException("Given file data is not valid");
		}
		
	}

	/**
	 * Make a list of XYValue objects from given string e.g. 1,2 3,4 5,6
	 * @param string Given string of data.
	 * @return List of XYValue objects.
	 */
	private static List<XYValue> parseGraphData(String string) {
		List<XYValue> data = new ArrayList<>();
		
		String[] splitString = string.split("\\s+");
		for (String entry : splitString) {
			
			String[] splitEntry = entry.split(",");
			Integer a = Integer.parseInt(splitEntry[0]);
			Integer b = Integer.parseInt(splitEntry[1]);
			
			data.add(new XYValue(a, b));
		}
		
		return data;
	}

	/**
	 * Constructor for the frame.
	 */
	public BarChartDemo() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLocation(50, 50);
		setSize(500, 400);
		setTitle("MyGraph");
	}
}
