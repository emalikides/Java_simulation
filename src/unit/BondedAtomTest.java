package unit;

import main.BondedAtom;
import main.Vector;
import main.Bond;

import org.junit.Before;
import org.junit.Test;
import junit.framework.TestCase;

public class BondedAtomTest extends TestCase {

	private BondedAtom atom;
	@Before
	public void setUp() throws Exception {
		atom = new BondedAtom(1.0, 1.0, new Vector(0.0, 0.0), new Vector(0.0, 0.0));
	}

	@Test
	public void testAddBondBond() {
		BondedAtom atom2 = new BondedAtom(1.0, 1.0, new Vector(1.0, 1.0), new Vector(0.0, 0.0));
		Bond b = new Bond(1.0, 1.0, 2.0, atom, atom2);
		atom.addBond(b);
		assertTrue(atom.bonds.contains(b));
		assertTrue(atom2.bonds.contains(b));
		int ind1, ind2;
		ind1 = atom.bonds.indexOf(b);
		ind2 = atom2.bonds.indexOf(b);
		assertEquals(atom.bonds.get(ind1), atom2.bonds.get(ind2));
	}

	@Test
	public void testAddBondBondedAtomDoubleDoubleDouble() {
		BondedAtom atom2 = new BondedAtom(1.0, 1.0, new Vector(1.0, 1.0), new Vector(0.0, 0.0));
		atom.addBond(atom2, 1.0, 1.0, 2.0);
		Bond b = atom.bonds.get(atom.bonds.size()-1);
		Bond c = new Bond(1.0, 1.0, 2.0, atom, atom2);
		Bond d = atom2.bonds.get(atom.bonds.size()-1);
		assertEquals(b.maxLength, c.maxLength, 0.0);
		assertEquals(b.restLength, c.restLength, 0.0);
		assertEquals(b.maxLength, c.maxLength, 0.0);
		assertEquals(b.strength, c.strength, 0.0);
		assertTrue(b.inBond(c.atom1));
		assertTrue(b.inBond(c.atom2));
		assertEquals(c, b);
		assertEquals(c, d);
		assertTrue(b == d);
	}
}
