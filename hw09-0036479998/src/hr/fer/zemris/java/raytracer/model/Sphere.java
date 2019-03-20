package hr.fer.zemris.java.raytracer.model;

import hr.fer.zemris.java.raytracer.model.GraphicalObject;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;

/**
 * This class implements Sphere objects which extend Graphical Object class.
 * 
 * @author Lovro Marković
 *
 */
public class Sphere extends GraphicalObject{
	
	/**
	 * Center of sphere.
	 */
	private Point3D center;
	
	/**
	 * Radius of sphere.
	 */
	private double radius;
	
	/**
	 * R component - diffusion.
	 */
	private double kdr;
	
	/**
	 * G component - diffusion.
	 */
	private double kdg;
	
	/**
	 * B component - diffusion.
	 */
	private double kdb;
	
	/**
	 * R component - reflection.
	 */
	private double krr;
	
	/**
	 * G component - reflection.
	 */
	private double krg;
	
	/**
	 * B component - reflection.
	 */
	private double krb;
	
	/**
	 * n-th power - reflection.
	 */
	private double krn;
	
	/**
	 * Constructor for Sphere objects.
	 * 
	 * @param center Center of sphere.
	 * @param radius Radius of sphere.
	 * @param kdr R component - diffusion.
	 * @param kdg G component - diffusion.
	 * @param kdb B component - diffusion.
	 * @param krr R component - reflection.
	 * @param krg G component - reflection.
	 * @param krb B component - reflection.
	 * @param krn n-th power - reflection.
	 */
	public Sphere(Point3D center, double radius, double kdr, double kdg,
			double kdb, double krr, double krg, double krb, double krn) {
		super();
		this.center = center;
		this.radius = radius;
		this.kdr = kdr;
		this.kdg = kdg;
		this.kdb = kdb;
		this.krr = krr;
		this.krg = krg;
		this.krb = krb;
		this.krn = krn;
	}
	
	@Override
	public RayIntersection findClosestRayIntersection(Ray ray) {
		
		Point3D L = ray.start.sub(center);
		double a = ray.direction.scalarProduct(ray.direction);
		double b = 2*ray.direction.scalarProduct(L);
		double c = L.scalarProduct(L) - radius*radius;
		double t0, t1;
		
		double discriminant = b * b - 4 * a * c;
		if (discriminant < 0) return null;
		else if (discriminant == 0){
			t0 = -0.5 * b/a;
			t1 = t0;
		}
		else {
			double q = (b > 0) ? 
					- 0.5 * (b + Math.sqrt(discriminant)):
					- 0.5 * (b - Math.sqrt(discriminant));
					
			t0 = q / a;
			t1 = c / q;			
		}
		
		if (t0 > t1) {
			double temp = t0;
			t0 = t1;
			t1 = temp;
		}


	    Point3D intersection = ray.start.add(ray.direction.scalarMultiply(t0));
	    boolean isOuter = false;
	    if (t0 > 0 && t1 > 0) isOuter = true;
	    return new RayIntersection(intersection, intersection.sub(ray.start).norm(), isOuter) {
			
			@Override
			public Point3D getNormal() {
				return Ray.fromPoints(center, intersection).direction.normalize();
			}
			
			@Override
			public double getKrr() {
				return krr;
			}
			
			@Override
			public double getKrn() {
				return krn;
			}
			
			@Override
			public double getKrg() {
				return krg;
			}
			
			@Override
			public double getKrb() {
				return krb;
			}
			
			@Override
			public double getKdr() {
				return kdr;
			}
			
			@Override
			public double getKdg() {
				return kdg;
			}
			
			@Override
			public double getKdb() {
				return kdb;
			}
		};
	}

}
