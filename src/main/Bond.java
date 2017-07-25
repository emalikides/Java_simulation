package main;

import except.NotInBondException;
import except.NoForceDirectionException;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Used to model the bonds between atoms;
 * 
 * @author u4685222
 */
public class Bond {
	
	/**
	 * The atoms which are bonded together;
	 */
	public BondedAtom atom1, atom2;
	
	/**
	 * A scalar value representing how much force will be applied, relative to how much the bond is stretched.
	 */
	public Double strength;
	
	/**
	 * The maximum length a bond can be before it breaks.
	 */
	public Double maxLength;
	
	/**
	 * The length the bond where it is not considered to be either stretched of compressed.
	 */
	public Double restLength;
	
	/**
	 * 
	 * @param strength - Strength of the bond.
	 * @param restLength - Length of bond when no force is applied.
	 * @param maxLength - Maximum length before the bond breaks.
	 * @param a1 - Atom 1
	 * @param a2 - Atom 2
	 */
	public Bond(Double strength, Double restLength, Double maxLength, BondedAtom a1, BondedAtom a2) {
		this.strength = strength;
		this.restLength = restLength;
		this.maxLength = maxLength;
		atom1 = a1;
		atom2 = a2;
	}
	
	/**
	 * When passed an atom which is a part of the bond, will return the other atom in the bond.
	 * 
	 * @param atom
	 * @return
	 */
	public BondedAtom getOtherAtom(BondedAtom atom) throws NotInBondException {
		if(atom == atom1)
			return atom2;
		else if(atom == atom2)
			return atom1;
		else
			throw new NotInBondException("Error: Atom "+ atom +" is not in bond");
	}
	
	/**
	 * Tests whether the atom is a member of this bond.
	 * @param atom
	 * @return
	 */
	public Boolean inBond(BondedAtom atom) {
		return (atom == atom1 || atom == atom2);
	}
	
	/**
	 * Adds this bond to the list of bonds in atom;
	 * @param atom
\0	 */
	public void addTo(BondedAtom atom) throws NotInBondException {
		if(inBond(atom) && !atom.bonds.contains(this))
			atom.bonds.add(this);
		else
			throw new NotInBondException("Error: Atom "+ atom +" is not in bond");
	}
	
	/**
	 * Calculates the stretch or compression of the bond.
	 * @return the stretch.
	 */
	public Double stretch() {
		return (restLength - length());
	}
	
	/**
	 * Calculates the length of the bond. (Difference between positions of atoms).
	 * @return the length.
	 */
	public Double length() {
		return Math.abs(atom1.position.vectorTo(atom2.position).length());
	}
	
	/**
	 * Calculates the force the bond is applying to the supplied atom;
	 * @param atom
	 * @return the force vector.
	 */
	public Vector force(BondedAtom atom) throws NoForceDirectionException {

		try {
			Vector f = atom.position.vectorTo(this.getOtherAtom(atom).position);
			Vector damp = new Vector(atom.velocity);
			damp.subtract(this.getOtherAtom(atom).velocity);
			//Scale the damping vector by some percentage of the bond strength, so as not to subtract too large a value.
			damp.scale(-0.5*Constants.bondStrength*Constants.bondDampingFactor);
			if (f.length()!= 0) {
				//f must be a unit vector in the direction of the other atom.
				f.scale(1.0/f.length());
				f.scale(-this.strength * this.stretch());
				//Damp the force applied by the bond, handling the case when the strength is zero.
				f.add((this.strength>0)?damp:new Vector(0.0,0.0));
				return f;
			}
			else 
			{ 
				throw new NoForceDirectionException("A Bond didn't know which direction to push!");
			}

		} catch (NotInBondException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Draws the bond.
	 * (For debugging purposes.)
	 * @param canvas
	 */
	public void draw(SimulationCanvas canvas) {
		if(Constants.debug) {
			Graphics g = canvas.getOffscreenGraphics();
			g.setColor(new Color(0,0,0));
			g.drawLine(atom1.position.x.intValue(), (atom1.position.y.intValue()), (atom2.position.x.intValue()), (atom2.position.y.intValue()));
		}
	}
	
	/**
	 * Tests whether two bonds are equivalent.
	 */
	public boolean equals(Object bond) {
		Bond b = (Bond)bond;
		return (this.inBond(b.atom1) 
				&& this.inBond(b.atom2)
				&& this.maxLength.equals(b.maxLength)
				&& this.restLength.equals(b.restLength)
				&& this.strength.equals(b.strength));

	}
}
