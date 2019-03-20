package hr.fer.zemris.java.raytracer;

import java.util.List;

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
 * Demonstrative program for ray casters.
 * 
 * @author Lovro Marković
 *
 */
public class RayCaster {

	/**
	 * Main method of the program. Executed when program is run.
	 * 
	 * @param args
	 *            No arguments expected.
	 */
	public static void main(String[] args) {
		RayTracerViewer.show(getIRayTracerProducer(), new Point3D(10, 0, 0),
				new Point3D(0, 0, 0), new Point3D(0, 0, 10), 20, 20);
	}

	/**
	 * Generates producer for Ray tracer objects.
	 * 
	 * @return RayTracer producer.
	 */
	private static IRayTracerProducer getIRayTracerProducer() {

		return new IRayTracerProducer() {

			@Override
			public void produce(Point3D eye, Point3D view, Point3D viewUp,
					double horizontal, double vertical, int width, int height,
					long requestNo, IRayTracerResultObserver observer) {

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
				short[] rgb = new short[3];
				int offset = 0;

				for (int y = 0; y < height; y++) {

					for (int x = 0; x < width; x++) {
						Point3D screenPoint = screenCorner
								.add(xAxis.scalarMultiply(
										(double) x / (width - 1) * horizontal))
								.sub(yAxis.scalarMultiply(
										(double) y / (height - 1) * vertical));

						if (x == 166 && y == 166) {
							@SuppressWarnings("unused")
							boolean bl = true;
						}
						Ray ray = Ray.fromPoints(eye, screenPoint);
						tracer(scene, ray, rgb);
						if (x == 250 && y == 250) {
							System.out.println(
									rgb[0] + " " + rgb[1] + " " + rgb[2]);
						}
						red[offset] = rgb[0] > 255 ? 255 : rgb[0];
						green[offset] = rgb[1] > 255 ? 255 : rgb[1];
						blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
						offset++;
					}
				}

				System.out.println("Izračuni gotovi...");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Dojava gotova...");
			}

			/**
			 * Calculates the rgb color of for the given ray.
			 * 
			 * @param scene Given scene.
			 * @param ray Given ray.
			 * @param rgb Reference to the rgb color.
			 */
			private void tracer(Scene scene, Ray ray, short[] rgb) {

				rgb[0] = 0;
				rgb[1] = 0;
				rgb[2] = 0;

				RayIntersection closest = findClosestIntersection(scene, ray);

				if (closest == null)
					return;

				// rgb[0] = 255;
				// rgb[1] = 255;
				// rgb[2] = 255;
				determineColorFor(closest, scene, ray, rgb);
			}

			/**
			 * Finds the closest intersection point of ray with an object in the
			 * scene.
			 * 
			 * @param scene
			 *            Given scene.
			 * @param ray
			 *            Ray.
			 * @return Intersection point.
			 */
			private RayIntersection findClosestIntersection(Scene scene,
					Ray ray) {

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

			/**
			 * Determines color for the given ray, intersection point and scene.
			 * 
			 * @param s
			 *            Given intersection point.
			 * @param scene
			 *            Given scene description.
			 * @param ray
			 *            Ray that intersects an object.
			 * @param rgb
			 *            Rgb color determined.
			 */
			private void determineColorFor(RayIntersection s, Scene scene,
					Ray ray, short[] rgb) {
				rgb[0] = 15;
				rgb[1] = 15;
				rgb[2] = 15;

				List<LightSource> ls = scene.getLights();
				for (LightSource lightSource : ls) {
					Ray rCrt = Ray.fromPoints(lightSource.getPoint(),
							s.getPoint());

					RayIntersection sCrt = findClosestIntersection(scene, rCrt);

					if (sCrt != null && ((sCrt.getPoint()
							.sub(lightSource.getPoint()).norm() + 0.01 < s
									.getPoint().sub(lightSource.getPoint())
									.norm()))) {
						continue;
					}

					// Calculate all vectors
					Point3D n = s.getNormal();
					Point3D l = Ray.fromPoints(s.getPoint(),
							lightSource.getPoint()).direction;
					Point3D negativeL = l.negate();
					Point3D r = negativeL
							.sub(n.scalarMultiply(
									2 * negativeL.scalarProduct(n)))
							.normalize();
					Point3D v = Ray.fromPoints(s.getPoint(),
							ray.start).direction;

					// Calculate diffuse coefficient
					double diffuseCoefficient = Math.max(0.0,
							l.scalarProduct(n));

					// Calculate reflect coefficient
					double pom = r.scalarProduct(v);
					double reflectCoefficient = pom < 0 ? 0.0
							: Math.pow(pom, s.getKrn());

					double rComponent = lightSource.getR()
							* (s.getKdr() * diffuseCoefficient + s.getKrr() * reflectCoefficient);
					double gComponent = lightSource.getG()
							* (s.getKdg() * diffuseCoefficient + s.getKrg() * reflectCoefficient);
					double bComponent = lightSource.getB()
							* (s.getKdb() * diffuseCoefficient + s.getKrb() * reflectCoefficient);

					rgb[0] += rComponent;
					rgb[1] += gComponent;
					rgb[2] += bComponent;
				}

			}
		};
	}
}
