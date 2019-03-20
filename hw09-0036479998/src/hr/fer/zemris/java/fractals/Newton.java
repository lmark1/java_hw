package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * Program calculates newton fractal based on the roots from the inputs.
 * 
 * @author Lovro Marković
 *
 */
public class Newton {

	/**
	 * Main method of the program. Executed when program is run.
	 * 
	 * @param args
	 *            Accepts no command line arguments.
	 */
	public static void main(String[] args) {
		System.out.println(
				"Welcome to Newton-Raphson iteration-based fractal viewer.");

		int rootIndex = 1;
		Scanner inputScanner = new Scanner(System.in);
		List<Complex> roots = new ArrayList<>();

		System.out.println("Please enter at least two roots, one root per line."
				+ " Enter 'done' when done.");
		while (true) {
			System.out.printf("Root %d>", rootIndex);
			String complexString = inputScanner.nextLine();

			if (complexString.equals("done")) {

				if (rootIndex <= 2) {
					System.out.println("Please enter atleast two roots.");
					continue;
				}

				System.out.println(
						"Image of fractal will appear shortly. Thank you.");
				break;
			}

			Complex newComplex = null;
			try {
				newComplex = parse(complexString);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				continue;
			}

			roots.add(newComplex);
			rootIndex++;
		}

		inputScanner.close();

		Complex[] rootArray = new Complex[roots.size()];
		rootArray = roots.toArray(rootArray);

		ComplexRootedPolynomial newRootPoly = new ComplexRootedPolynomial(
				rootArray);
		System.out.println(newRootPoly);
		
		FractalViewer.show(new MyProducer(newRootPoly));
	}

	/**
	 * Parse complex number from given string. (accepts strings such as: "3.51",
	 * "-3.17", "-2.71i", "i", "1", "-2.71-3.15i")
	 * 
	 * @param s
	 *            String that contains some form of a complex number.
	 * @return {@link Complex} object containing real and / or imaginary data
	 *         from the given insput.
	 * @throws IllegalArgumentException
	 *             If the complex number is not valid.
	 */
	public static Complex parse(String s) {
		double real = 0, imag = 0;

		String newS = removeSpacesAndFirstPlus(s);
		String[] splitString = (newS + ' ').split("");
		StringBuilder makeNumber = new StringBuilder();
		int numberChecker = 0;

		for (String currentElement : splitString) {

			if (makeNumber.length() != 0
					&& (currentElement.equals("+") || currentElement.equals("-")
							|| currentElement.equals(" "))) {

				double[] newC = parseRealOrImag(makeNumber.toString());
				numberChecker++;

				// Parse up to 2 numbers
				if (newC == null || numberChecker > 2) {
					throw new IllegalArgumentException(
							"Invalid complex number format.");
				}

				real += newC[0];
				imag += newC[1];
				makeNumber.setLength(0);
			}
			makeNumber.append(currentElement);
		}

		Complex newC = new Complex(real, imag);
		return newC;
	}

	/**
	 * Removes all spaces and first plus sign from the string.
	 * 
	 * @param s
	 *            Input string.
	 * @return Formatted string.
	 */
	private static String removeSpacesAndFirstPlus(String s) {
		s = s.replaceAll("\\s+", "");
		StringBuilder sNew = new StringBuilder(s);
		if (sNew.charAt(0) == '+') {
			sNew.setCharAt(0, ' ');
		}
		String newS = new String(sNew);
		newS = newS.replaceAll("\\s+", "");
		return newS;
	}

	/**
	 * Parse real or imaginary number.
	 * 
	 * @param strCheck
	 *            String that will be parsed to a complex number.
	 * @return Double array containing real and imaginary part.
	 */
	private static double[] parseRealOrImag(String strCheck) {

		boolean isImag = false;
		strCheck = removeSpacesAndFirstPlus(strCheck);

		if ((strCheck.contains("i") && strCheck.length() == 1)
				|| (strCheck.contains("i") && strCheck.length() == 2
						&& strCheck.contains("-"))) {
			strCheck = strCheck.replace('i', '1');
			isImag = true;

		} else if (strCheck.contains("i")) {

			int iCount = 0;
			for (int i = 0; i < strCheck.length(); i++) {
				if (strCheck.charAt(i) == 'i')
					iCount++;
			}

			if (iCount > 1)
				return null;
			strCheck = strCheck.replace("i", "");
			isImag = true;
		}

		// Try to parse the number and reset buffer
		double imag = 0;
		double real = 0;
		try {
			if (isImag) {
				imag = Double.parseDouble(strCheck);
			} else {
				real = Double.parseDouble(strCheck);
			}

		} catch (NumberFormatException ex) {
			return null;
		}

		return new double[] { real, imag };
	}

	/**
	 * This class implements a job which calculates newton fractals.
	 * 
	 * @author Lovro Marković
	 *
	 */
	public static class CalculatingJob implements Callable<Void> {

		/**
		 * Minimal value on the real axis.
		 */
		private double reMin;

		/**
		 * Maximum value on the real axis.
		 */
		private double reMax;

		/**
		 * Minimum value on the imaginary axis.
		 */
		private double imMin;

		/**
		 * Maximum value on the imaginary axis.
		 */
		private double imMax;

		/**
		 * Width of the window showing fractal.
		 */
		private int width;

		/**
		 * Height of the window showing fractal.
		 */
		private int height;

		/**
		 * Y value from which the data field is getting filled. (included)
		 */
		private int yMin;

		/**
		 * Y value to which the data field is getting filled. (included)
		 */
		private int yMax;

		/**
		 * Number of tries until convergence.
		 */
		private int m;

		/**
		 * Field in which the result is stored.
		 */
		private short[] data;

		/**
		 * Complex polynomial.
		 */
		private ComplexPolynomial poly;

		/**
		 * Derived polynomial.
		 */
		private ComplexPolynomial derived;

		/**
		 * Rooted polynomial.
		 */
		private ComplexRootedPolynomial rootPoly;

		/**
		 * Default constructor initializing all values.
		 * 
		 * @param reMin
		 *            Real minimal value.
		 * @param reMax
		 *            Real maximal value.
		 * @param imMin
		 *            Minimal imaginary value.
		 * @param imMax
		 *            Mimaginary value.
		 * @param width
		 *            Width.
		 * @param height
		 *            Height.
		 * @param yMin
		 *            Minimal value from which data is calulated.
		 * @param yMax
		 *            Maximal value to which data is calculated.
		 * @param m
		 *            Number of tries until convergence.
		 * @param data
		 *            Result.
		 * @param rootPoly
		 *            Complex polynomial for Newton fractal.
		 */
		public CalculatingJob(double reMin, double reMax, double imMin,
				double imMax, int width, int height, int yMin, int yMax, int m,
				short[] data, ComplexRootedPolynomial rootPoly) {

			this.reMin = reMin;
			this.reMax = reMax;
			this.imMin = imMin;
			this.imMax = imMax;
			this.width = width;
			this.height = height;
			this.yMin = yMin;
			this.yMax = yMax;
			this.m = m;
			this.data = data;
			this.rootPoly = rootPoly;
			this.poly = rootPoly.toComplexPolynom();
			this.derived = this.poly.derive();

		}

		@Override
		public Void call() throws Exception {
			calculateNewton(reMin, reMax, imMin, imMax, width, height, yMin,
					yMax, m, data);
			return null;
		}

		/**
		 * Calculate fractal with newton method.
		 * 
		 * @param reMin
		 *            Real minimal value.
		 * @param reMax
		 *            Real maximal value.
		 * @param imMin
		 *            Minimal imaginary value.
		 * @param imMax
		 *            Mimaginary value.
		 * @param width
		 *            Width.
		 * @param height
		 *            Height.
		 * @param yMin
		 *            Minimal value from which data is calulated.
		 * @param yMax
		 *            Maximal value to which data is calculated.
		 * @param m
		 *            Number of tries until convergence.
		 * @param data
		 *            Result.
		 */
		private void calculateNewton(double reMin, double reMax, double imMin,
				double imMax, int width, int height, int yMin, int yMax, int m,
				short[] data) {

			for (int y = yMin; y <= yMax; y++) {
				int offset = y * width;
				
				for (int x = 1; x <= width; x++) {

					double cRe = x / (width - 1.0) * (reMax - reMin) + reMin;
					double cIm = (height - 1.0 - y) / (height - 1)
							* (imMax - imMin) + imMin;

					Complex zn = new Complex(cRe, cIm);

					int iter = 0;
					double module = 0;
					do {
						Complex numerator = poly.apply(zn);
						Complex denominator = derived.apply(zn);
						Complex fraction = numerator.divide(denominator);
						Complex zn1 = zn.sub(fraction);
						module = zn1.sub(zn).module();
						zn = zn1;
						iter++;
					} while (module > 1E-3 && iter <= m);
					
					int index = rootPoly.indexOfClosestRootFor(zn, 0.002);
					if (index == -1)
						data[offset++] = 0;
					else
						data[offset++] = (short) (index+1);
				}
			}
		}
	}

	/**
	 * This class implements a producer of fractals with parallel computing.
	 * 
	 * @author Lovro Marković
	 *
	 */
	public static class MyProducer implements IFractalProducer {

		/**
		 * Rooted polynomial.
		 */
		private ComplexRootedPolynomial rootPoly;

		/**
		 * Executor object.
		 */
		private ExecutorService pool;

		/**
		 * Default producer constructor, initalizes root polynomial.
		 * 
		 * @param rootPoly
		 *            Rooted polynomial.
		 */
		public MyProducer(ComplexRootedPolynomial rootPoly) {
			this.rootPoly = rootPoly;
			pool = Executors.newFixedThreadPool(
					Runtime.getRuntime().availableProcessors(),
					new ThreadFactory() {

						@Override
						public Thread newThread(Runnable r) {
							Thread t = Executors.defaultThreadFactory()
									.newThread(r);
							t.setDaemon(true);
							return t;
						}
					});

		}

		@Override
		public void produce(double reMin, double reMax, double imMin,
				double imMax, int width, int height, long requestNo,
				IFractalResultObserver observer) {

			System.out.println("Starting calulations...");
			int m = 16 * 16 * 16;
			short[] data = new short[width * height];
			final int numberOfStrips = 8
					* Runtime.getRuntime().availableProcessors();
			int numberOfYPerStrip = height / numberOfStrips;

			List<Future<Void>> rezultati = new ArrayList<>();

			for (int i = 0; i < numberOfStrips; i++) {
				int yMin = i * numberOfYPerStrip;
				int yMax = (i + 1) * numberOfYPerStrip - 1;
				if (i == numberOfStrips - 1) {
					yMax = height - 1;
				}

				CalculatingJob job = new CalculatingJob(reMin, reMax, imMin,
						imMax, width, height, yMin, yMax, m, data, rootPoly);
				rezultati.add(pool.submit(job));
			}

			for (Future<Void> posao : rezultati) {
				try {
					posao.get();
				} catch (InterruptedException | ExecutionException e) {
				}
			}

			System.out.println(
					"Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");
			observer.acceptResult(data,
					(short) (rootPoly.toComplexPolynom().order() + 1),
					requestNo);
		}

	}
}
