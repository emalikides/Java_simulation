package main;
import java.util.List;

/**
 * Vector is used to represent mathematical vectors within the Simulation.<br />
 * The x, y coordinates are displacement from the origin (0.0, 0.0)<br />
 * Within the simulation, vectors are used to represent the position & velocity of Matter, and constants such as
 * acceleration due to gravity.
 * 
 * @author u4685222 - Joshua Korner-Godsiff
 */
public class Vector {
	
	/**
	 * The x coordinate of the Vector
	 */
	public Double x;
	
	/**
	 * The y coordinate of the Vector
	 */
	public Double y;
	
	/**
	 * Constructs a new vector.
	 * 
	 * @param x Coordinate in the x-plane
	 * @param y Coordinate in the y-plane
	 */
	public Vector(Double x, Double y) {
		this.set(x, y);
	}
	
	public Vector(Vector v) {
		x = v.x;
		y = v.y;
	}
	
	/**
	 * Sets the coordinates of this vector.
	 * 
	 * @param x Coordinate in the x-plane
	 * @param y Coordinate in the y-plane
	 */
	public void set(Double x, Double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Adds a set of x-y coordinates to this vector.
	 * 
	 * @param x The x-coordinate to be added.
	 * @param y The y-coordinate to be added.
	 */
	public void add(Double x, Double y) {
		this.x += x;
		this.y = this.y + y;
	}
	
	/**
	 * Adds a vector to this vector.	 
	 * @param v The vector to be added.
	 */
	public void add(Vector v) {
		this.add(v.x, v.y);
	}
	
	public void subtract(Vector v) {
		//Don't want to modify the incoming Vector.
		Vector temp = new Vector(0.0, 0.0);
		temp.add(v);
		temp.scale(-1.0);
		this.add(temp);

	}
	
	/**
	 * Adds a list of vectors to this vector;
	 * @param vs The list of Vectors;
	 */
	public void add(List<Vector> vs) {
		for (Vector vector : vs) {
			this.add(vector);
		}
	}
	
	/**
	 * Calculates the unit vector;
	 * @return The unit vector;
	 */
	public Vector unit(){
		Vector unit;
		if(this.length()==0.0){ 
			unit = new Vector(0.0,0.0);
			System.out.println("Error in Vector.unit when dividing by zero." );
		}
		else{
			unit = this.scaleim(1/this.length());
		}
		return unit;
	}
	
	/**
	 * Adds a list of vectors to this vector, this version returns a vector.
	 * @param vs The list of Vectors;
	 */
	public Vector addim(Vector v) {
		Vector added = new Vector(this);
		added.add(v);
		return added;
	}
	
	/**
	 * Scales the coordinates of the Vector by a scalar, this version returns a new scaled vector. 
	 * @param c The scalar
	 */
	public Vector scaleim(Double c) {
		Vector scaled = new Vector(this);
		scaled.x *= c;
		scaled.y *= c;
		return scaled;
	}
	
	/**
	 * Scales the coordinates of the Vector by a scalar. 
	 * @param c The scalar
	 */
	public void scale(Double c) {
		this.x *= c;
		this.y *= c;
	}
	
	public Double roundDec(Double n, Integer precision) {
		return Math.rint(n*Math.pow(10,precision))/Math.pow(10,precision);
	}
	
	/**
	 * Rounds the vector according to the precision in constants, to avoid cumulative errors.
	 */
	public void round() {
		this.x = roundDec(x, Constants.precision);
		this.y = roundDec(y, Constants.precision);
	}
	
	/**
	 * Calculates the Vector which is the difference between this and the supplied Vector.
	 * 
	 * @param v
	 * @return the resulting Vector.
	 */
	public Vector vectorTo(Vector v) {
		return new Vector(v.x - this.x, v.y - this.y);
	}
	
	/**
	 * Calculates the length of this vector.
	 * 
	 * @return the length of the vector.
	 */
	public Double length() {
		return Math.sqrt(x*x+y*y);
	}
	
	public Double dot(Vector v) {
		return x*v.x + y*v.y;
	}
	
	/**
	 * Tests whether one vector is equal to another
	 *
	 * @param obj The vector to be tested
	 * @return True if the coordinate are equal, False otherwise.
	 */
	public boolean equals(Object obj) {
		Vector v = (Vector) obj;
		return (v.x.equals(this.x) && v.y.equals(this.y));
	}
	
	/**
	 * Converts a Vector to it's String representation.
	 * 
	 * @return The string representation
	 */
	public String toString() {
		return "("+x+", "+y+")";
	}
}
