package hr.fer.zemris.java.raytracer;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import hr.fer.zemris.java.raytracer.model.GraphicalObject;
import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.LightSource;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * Parallelizes the ray caster callculation using Fork join.
 * 
 * @author Lovro Marković
 *
 */
public class RayCasterParallel {

	/**
	 * Main method of the program.
	 * 
	 * @param args
	 *            Accepts no arguments.
	 */
	public static void main(String[] args) {
		RayTracerViewer.show(new MyProducer(), new Point3D(10, 0, 0),
				new Point3D(0, 0, 0), new Point3D(0, 0, 10), 20, 20);
	}

	/**
	 * This class implements a Job that calculates rgb colors for each pixel.
	 * 
	 * @author Lovro Marković
	 *
	 */
	public static class MyCalcJob extends RecursiveAction {

		/**
		 * Serial version of the job.
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Scene in the picture.
		 */
		private Scene scene;

		/**
		 * XAxis vector.
		 */
		private Point3D xAxis;

		/**
		 * YAxis vector.
		 */
		private Point3D yAxis;

		/**
		 * ZAxis vector.
		 */
		private Point3D zAxis;

		/**
		 * Position of the screen corner.
		 */
		private Point3D screenCorner;

		/**
		 * Poistion of the observer.
		 */
		private Point3D eye;

		/**
		 * View vector.
		 */
		private Point3D view;

		/**
		 * View up vector.
		 */
		private Point3D viewUp;

		/**
		 * Horizontal width of observed space.
		 */
		private double horizontal;

		/**
		 * Vertical width of observed space.
		 */
		private double vertical;

		/**
		 * Width of screen.
		 */
		private int width;

		/**
		 * Height of screen.
		 */
		private int height;

		/**
		 * Starting y position.
		 */
		private int yMin;

		/**
		 * Ending y position.
		 */
		private int yMax;

		/**
		 * Red pixel component vector reference.
		 */
		private short[] red;

		/**
		 * Blue pixel component vector reference.
		 */
		private short[] blue;

		/**
		 * Green pixel component vector reference.
		 */
		private short[] green;

		/**
		 * Treshold for direct compution.
		 */
		static final int treshold = 16;

		/**
		 * Constructor for the Job.
		 * 
		 * @param scene
		 *            Scene in the picture.
		 * @param xAxis
		 *            XAxis vector.
		 * @param yAxis
		 *            YAxis vector.
		 * @param zAxis
		 *            ZAxis vector.
		 * @param screenCorner
		 *            Position of the screen corner.
		 * @param eye
		 *            Position of the observer.
		 * @param view
		 *            View position.
		 * @param viewUp
		 *            Viewup position.
		 * @param horizontal
		 *            Horizontal width of observed space.
		 * @param vertical
		 *            Vertical width of observed space.
		 * @param width
		 *            Width of screen.
		 * @param height
		 *            Height of screen.
		 * @param yMin
		 *            Starting y position.
		 * @param yMax
		 *            Ending y position.
		 * @param red
		 *            Red pixel component vector reference.
		 * @param green
		 *            Green pixel component vector reference.
		 * @param blue
		 *            Blue pixel component vector reference.
		 */
		public MyCalcJob(Scene scene, Point3D xAxis, Point3D yAxis,
				Point3D zAxis, Point3D screenCorner, Point3D eye, Point3D view,
				Point3D viewUp, double horizontal, double vertical, int width,
				int height, int yMin, int yMax, short[] red, short[] green,
				short[] blue) {
			super();
			this.scene = scene;
			this.xAxis = xAxis;
			this.yAxis = yAxis;
			this.zAxis = zAxis;
			this.screenCorner = screenCorner;
			this.eye = eye;
			this.view = view;
			this.viewUp = viewUp;
			this.horizontal = horizontal;
			this.vertical = vertical;
			this.width = width;
			this.height = height;
			this.yMin = yMin;
			this.yMax = yMax;
			this.red = red;
			this.blue = blue;
			this.green = green;
		}

		@Override
		protected void compute() {
			if (yMax - yMin + 1 <= treshold) {
				computeDirect();
				return;
			}

			invokeAll(
					new MyCalcJob(scene, xAxis, yAxis, zAxis, screenCorner, eye,
							view, viewUp, horizontal, vertical, width, height,
							yMin, yMin + (yMax - yMin) / 2, red, green, blue),
					new MyCalcJob(scene, xAxis, yAxis, zAxis, screenCorner, eye,
							view, viewUp, horizontal, vertical, width, height,
							 yMin + (yMax - yMin) / 2 + 1, yMax, red, green,
							blue));
		}

		/**
		 * Direct compution of rgb component.
		 */
		private void computeDirect() {
			short[] rgb = new short[3];
			
			for (int y = yMin; y <= yMax; y++) {
				int offset = y * width;

				for (int x = 0; x < width; x++) {
					if (x == 166 && y == 166) {
						System.out.println("hello");
					}
					Point3D screenPoint = screenCorner
							.add(xAxis.scalarMultiply(
									(double) x / (width - 1) * horizontal))
							.sub(yAxis.scalarMultiply(
									(double) y / (height - 1) * vertical));

					Ray ray = Ray.fromPoints(eye, screenPoint);
					tracer(ray, rgb);
					red[offset] = rgb[0] > 255 ? 255 : rgb[0];
					green[offset] = rgb[1] > 255 ? 255 : rgb[1];
					blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
					offset++;
				}
			}
		}

		/**
		 * Calculates the rgb color of for the given ray.
		 * 
		 * @param ray
		 *            Given ray.
		 * @param rgb
		 *            Reference to the rgb color.
		 */
		private void tracer(Ray ray, short[] rgb) {
			rgb[0] = 0;
			rgb[1] = 0;
			rgb[2] = 0;

			RayIntersection closest = findClosestIntersection(ray);

			if (closest == null)
				return;

//			rgb[0] = 255;
//			rgb[1] = 255;
//			rgb[2] = 255;
			determineColorFor(closest, ray, rgb);
		}

		/**
		 * Determines color for the given ray, intersection point and scene.
		 * 
		 * @param s
		 *            Given intersection point.
		 * @param ray
		 *            Ray that intersects an object.
		 * @param rgb
		 *            Rgb color determined.
		 */
		private void determineColorFor(RayIntersection s, Ray ray,
				short[] rgb) {

			rgb[0] = 15;
			rgb[1] = 15;
			rgb[2] = 15;

			List<LightSource> ls = scene.getLights();
			for (LightSource lightSource : ls) {
				Ray rCrt = Ray.fromPoints(lightSource.getPoint(), s.getPoint());

				RayIntersection sCrt = findClosestIntersection(rCrt);

				if (sCrt != null
						&& ((sCrt.getPoint().sub(lightSource.getPoint()).norm()
								+ 0.01 < s.getPoint()
										.sub(lightSource.getPoint()).norm()))) {
					continue;
				}

				// Calculate all vectors
				Point3D n = s.getNormal();
				Point3D l = Ray.fromPoints(s.getPoint(),
						lightSource.getPoint()).direction;
				Point3D negativeL = l.negate();
				Point3D r = negativeL
						.sub(n.scalarMultiply(2 * negativeL.scalarProduct(n)))
						.normalize();
				Point3D v = Ray.fromPoints(s.getPoint(), ray.start).direction;

				// Calculate diffuse coefficient
				double diffuseCoefficient = Math.max(0.0, l.scalarProduct(n));

				// Calculate reflect coefficient
				double pom = r.scalarProduct(v);
				double reflectCoefficient = Math.pow(pom, s.getKrn());

				double rComponent = lightSource.getR()
						* (s.getKdr() * diffuseCoefficient
								+ s.getKrr() * reflectCoefficient);
				double gComponent = lightSource.getG()
						* (s.getKdg() * diffuseCoefficient
								+ s.getKrg() * reflectCoefficient);
				double bComponent = lightSource.getB()
						* (s.getKdb() * diffuseCoefficient
								+ s.getKrb() * reflectCoefficient);

				rgb[0] += rComponent;
				rgb[1] += gComponent;
				rgb[2] += bComponent;
			}

		}

		/**
		 * Finds the closest intersection point of ray with an object in the
		 * scene.
		 * 
		 * @param ray
		 *            Ray.
		 * @return Intersection point.
		 */
		private RayIntersection findClosestIntersection(Ray ray) {
			List<GraphicalObject> objectList = scene.getObjects();
			RayIntersection closestIntersection = null;

			for (GraphicalObject gObj : objectList) {
				RayIntersection newIntersection = gObj
						.findClosestRayIntersection(ray);

				if (newIntersection == null) {
					continue;
				}

				if (closestIntersection == null) {
					closestIntersection = newIntersection;
				} else if (closestIntersection.getDistance()
						- newIntersection.getDistance() > 0.01)
					closestIntersection = newIntersection;
			}

			return closestIntersection;
		}

	}

	/**
	 * Producer for the ray tracer.
	 * 
	 * @author Lovro Marković
	 *
	 */
	public static class MyProducer implements IRayTracerProducer {

		@Override
		public void produce(Point3D eye, Point3D view, Point3D viewUp,
				double horizontal, double vertical, int width, int height,
				long requestNo, IRayTracerResultObserver observer) {
			System.out.println("Zapocinjem izracun...");

			short[] red = new short[width * height];
			short[] green = new short[width * height];
			short[] blue = new short[width * height];

			Point3D zAxis = view.sub(eye).normalize();
			Point3D yAxis = viewUp.normalize()
					.sub(zAxis.scalarMultiply(
							zAxis.scalarProduct(viewUp.normalize())))
					.normalize();
			Point3D xAxis = zAxis.vectorProduct(yAxis).normalize();
			Point3D screenCorner = view
					.sub(xAxis.scalarMultiply(horizontal / 2))
					.add(yAxis.scalarMultiply(vertical / 2));

			Scene scene = RayTracerViewer.createPredefinedScene();

			ForkJoinPool pool = new ForkJoinPool();
			pool.invoke(new MyCalcJob(scene, xAxis, yAxis, zAxis, screenCorner,
					eye, view, viewUp, horizontal, vertical, width, height, 0,
					height - 1, red, green, blue));
			pool.shutdown();

			System.out.println(
					"Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");
			observer.acceptResult(red, green, blue, requestNo);
		}

	}
}
