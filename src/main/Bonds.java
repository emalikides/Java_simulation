package main;
/**
 * 
 */

import java.util.ArrayList;
import java.util.Iterator;

import except.NoForceDirectionException;
import except.ZeroMassException;

/**
 * Represents a list of Bonds.
 * Essentially just a wrapper class, since we generally want to do something for every bond a
 * BondedAtom has.
 * @author u4685222
 *
 */
public class Bonds extends ArrayList<Bond> {

	private static final long serialVersionUID = 1022957989675094436L;

	/**
	 * Calculates the acceleration due to bonds on the supplied atom.
	 * @param a the atom to calculate the acceleration upon.
	 * @return the net acceleration
	 */
	public Vector acceleration(BondedAtom a) throws ZeroMassException {
		Vector force = new Vector(0.0, 0.0);
		for (Bond b : this){
			try {
				force.add(b.force(a));
			} catch (NoForceDirectionException e)
			{
				System.out.println(e.getMessage());
				e.printStackTrace();
				return null;
			}
			
		}
		
		if (a.mass != 0) {
			force.scale(1.0/a.mass);
			return force;
		} else {
			throw new ZeroMassException("You Tried to calculate the acceleration of a bonded atom with zero mass!");
		}

	}
	
	/**
	 * Calculates which bonds are beyond their maximum length, and breaks (removes) them accordingly.
	 */
	public void breakBond() {
		Iterator<Bond> i = this.iterator();
		while (i.hasNext()) {
			Bond bond = (Bond) i.next();
			if(bond.length() > bond.maxLength) {
				i.remove();
			}
		}
	}
	
	/**
	 * Draws the bonds.
	 * (For debugging purposes.)
	 * @param canvas
	 */
	public void draw(SimulationCanvas canvas) {
		for (Bond b : this) {
			b.draw(canvas);
		}
	}
	
}
