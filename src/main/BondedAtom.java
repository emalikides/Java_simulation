package main;

import java.awt.*;
import except.NotInBondException;
import except.ZeroMassException;

/**
 * Represents an Atom (a single element of matter) in the simulation
 * which may or may not be bonded to other atoms.
 * 
 * @author u4685222
 *
 */
public class BondedAtom extends Matter {
	
	/**
	 * The list of bonds that this atom is a member of.
	 */
	public Bonds bonds = new Bonds();
	
	public BondedAtom(Double radius, Double mass, Double x, Double y, Double vx, Double vy)  {
		super(radius, mass, new Vector(x, y), new Vector (vx, vy));
	}
	
	public BondedAtom(Double radius, Double mass, Vector pos, Vector vel) {
		super(radius, mass, pos, vel);
	}
	
	/**
	 * Adds a bond to this atom's list of bonds, and does the same for the other atom in the bond.
	 * @param b the bond to be added.
	 */
	public void addBond(Bond b) {
		try {
			b.addTo(this);
			b.addTo(b.getOtherAtom(this));
		} catch (NotInBondException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Constructs a new bond, and the adds it to the list of bonds for this atom,
	 * and also to the list of bonds for the supplied atom.
	 * 
	 * @param atom the other atom in the bond.
	 * @param strength {@link Bond#strength}
	 * @param restlength {@link Bond#restLength}
	 * @param maxLength {@link Bond#maxLength}
	 */
	public void addBond(BondedAtom atom, Double strength, Double restlength, Double maxLength) {
		Bond b = new Bond(strength, restlength, maxLength, this, atom);
		this.addBond(b);
	}

	/**
	 * {@inheritDoc Matter#step(ArrayList)}
	 */
	//Step is now entirely in world, at this level of the heirarchy, we should not need to know about other 
	//particles in the world, passing an arraylist is unneeded, when it could all be done simply in world.
	
	/**
	 * Updates the acceleration of the <i> BondedAtom </i> according to the bonds this atom has, and checks
	 * If these bonds should still exist, if not, it breaks them.
	 */
	public void updateAcc() {
		acceleration.set(0.0,0.0);
		bonds.breakBond();
		try {
		acceleration.add(bonds.acceleration(this));
		acceleration.add(Constants.gravity);
		//acceleration.round();
		} catch (ZeroMassException e) {
			System.out.println(e.getMessage());
		}
	}
		
	/**
	 * Updates the position of this matter according to its velocity.
	 */
	public void updateP(){
		this.position.add(velocity.scaleim(Constants.timeResolution));
	}
	/**
	 * {@inheritDoc Matter#draw(SimulationCanvas)}
	 */
	public void draw(SimulationCanvas canvas) 
    {
		Graphics g = canvas.getOffscreenGraphics();
		Color c = new Color(24,116,205);
		g.setColor(c);
		g.fillOval((int)(position.x - radius), (int)(position.y - radius), (int)(2*radius), (int)(2*radius));
		bonds.draw(canvas);
	}
}
