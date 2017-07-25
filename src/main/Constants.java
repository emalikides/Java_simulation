/**
 * 
 */
package main;

/**
 * Maintains a list of constants used in many places throughout the program,
 * so that these need not always be passed around.
 * 
 * @author u4685222
 *
 */
public class Constants {

	/**
	 * Acceleration due to gravity.
	 */
	public static final Vector gravity = new Vector(0.0, 5.0);
	
	/**
	 * Width and Height of the simulation space.
	 */
	public static final Integer xsize=600, ysize =600;
	
	/**
	 * Number of steps to perform before stopping.
	 */
    public static final Integer numSteps = 100000;
    
    /**
     * The amount of simulation time per iteration, relates to how far things move etc.
     */
    public static final Double timeResolution = 0.01;

    /**
     * The number of decimal places rounded to to avoid cumulative errors.
     */
    public static final Integer precision = 6;
    
    /**
     * Radius of a typical bonded atom.
     */
    public static final Double bondedRadius = 3.0;
    
    /**
     * Rest length of a typical bond.
     */
    public static final Double bondRestLength = 6.0;
    
    /**
     * Maximum length of a typical bond.
     */
    public static final Double bondMaxLength = 12.0;
    
    /**
     * Strength of a typical bond. (100 is LARGE, 10 is STRONG, the rest are WEAK)
     */
    public static final Double bondStrength = 5.0;
    
    /**
     * A number between 0 and 1 that represents the factor of the bond strength that is lost each iteration.
     * A lower number means more damping. 
     * 
     */
    public static final Double bondDampingFactor = 0.9;
    
    /**
     * Damping factor for Collisions.
     */
    public static final Double damping = 1.0;
    
    /**
     * The expected number of particles to be used 
     */
    public static final Integer nParticles = 900;
    /**
     * Mass of a BondedAtom
     */
    public static final Double bondedMass = 20.0;
    /**
     * Mass of a Sand particle 
     */
    public static final Double sandMass = 1.0;
    /**
     * Whether or not to run the simulation in Debug Mode.
     */
    public static final Boolean debug = true;
    
  
}

