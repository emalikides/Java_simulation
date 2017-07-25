package unit;

import java.util.ArrayList;
import java.util.LinkedList;

import main.Vector;

import org.junit.Before;
import org.junit.Test;
import junit.framework.TestCase;

/**
 * VectorTest tests methods in the Vector class.
 * 
 * @author u4685222 - Joshua Korner-Godsiff
 */
public class VectorTest extends TestCase {

	private Vector test;
	
	/**
	 * Sets up a new Vector with coords (1.0, 1.0) for each test.
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		test = new Vector(1.0, 1.0);
	}
	
	/**
	 * Tests setting up a new Vector
	 * Test method for {@link Vector#Vector(Double, Double)}
	 */
	@Test
	public void testVector() {
		Vector v1;
		v1 = new Vector(-0.5, 3.0);
		assertEquals(v1.x, -0.5, 0.0);
		assertEquals(v1.y, 3.0, 0.0);
	}
	
	/**
	 * Tests setting the values of an already set Vector to something else.
	 * Test method for {@link Vector#set(Double, Double)}
	 */
	public void testSet() {
		Vector v1 = new Vector(1.0, 1.0);
		assertTrue(test.equals(v1));
		test.set(-3.2, 3.14159);
		assertEquals(test.x, -3.2, 0.0);
		assertEquals(test.y, 3.14159, 0.0);
		assertFalse(test.equals(v1));
	}

	/**
	 * Tests adding an x & y coordinate to an existing vector.
	 * This forms the basis for all other 'add' methods.
	 * Test method for {@link Vector#add(java.lang.Double, java.lang.Double)}.
	 */
	@Test
	public void testAddDoubleDouble() {
		Double x = 0.5;
		Double y = 1.5;
		Vector v = new Vector(1.0, 1.0);
		Vector u = new Vector(1.0, 1.0);
		test.add(x, y);
		v.add(x, y);
		assertTrue(test.equals(v));
		assertNotSame(test, v);
		assertFalse(test.equals(u));
		assertEquals(1.5, test.x, 0.0);
		assertEquals(2.5, test.y, 0.0);
	}

	/**
	 * Tests adding a two vectors together.
	 * Test method for {@link Vector#add(Vector)}.
	 */
	@Test
	public void testAddVector() {
		Vector u, v;
		u = new Vector(1.5, -3.5);
		v = new Vector(0.5, -4.5);
		test.add(v);
		assertEquals(1.5, test.x, 0.0);
		assertEquals(-3.5, test.y, 0.0);
		assertTrue(test.equals(u));
		assertNotSame(test, u);
	}
	
	/**
	 * Tests adding a List of vectors to an existing vector.
	 * @see main.Vector#add(java.util.List)
	 */
	@Test
	public void testAddArrayListVector() {
		//Tests adding an ArrayList of Vectors to a Vector
		ArrayList<Vector> vs = new ArrayList<Vector>();
		vs.add(new Vector(1.0, 1.0));
		vs.add(new Vector(-2.0, 3.0));
		test.add(vs);
		assertEquals(test.x, 0.0, 0.0);
		assertEquals(test.y, 5.0, 0.0);
		assertEquals(test, new Vector(0.0, 5.0));
		
		//Tests adding some non-ArrayList type of List to a Vector
		LinkedList<Vector> v2s = new LinkedList<Vector>();
		v2s.add(new Vector(-1.0, -1.0));
		v2s.add(new Vector(2.0, -6.0));
		test.add(v2s);
		assertEquals(test.x, 1.0, 0.0);
		assertEquals(test.y, -2.0, 0.0);
		assertEquals(test, new Vector(1.0, -2.0));
	}

	/**
	 * Tests creating a new vector, which is equal to the difference between two other vectors.
	 * Test method for {@link Vector#vectorTo(Vector)}.
	 */
	@Test
	public void testVectorTo() {
		Vector u, v, w;
		u = new Vector(-3.0, 4.5);
		v = new Vector(-4.0, 3.5);
		w = test.vectorTo(u);
		assertEquals(v, w);
	}

	/**
	 * Tests getting the length of a vector;
	 * Test method for {@link Vector#length()}.
	 */
	@Test
	public void testLength() {
		//Tests whether a Vector of known length returns the correct value.
		Vector v1 = new Vector(3.0, 4.0);
		assertEquals(5.0, v1.length(), 0.0);
		
		//Tests whether two Vectors of known length but different coordinates return the same lenght.
		Vector v2, v3;
		v2 = new Vector(-1.0, 0.0);
		v3 = new Vector(Math.cos(Math.PI/2.0), Math.sin(Math.PI/2.0));
		assertEquals((double) v2.length(), (double) v3.length(), 0.0);
	}
	
	/**
	 * Tests scaling of a vector by some scalar real number
	 * Test method for {@link Vector#scale(Double)}
	 */
	@Test
	public void testScale() {
		Double c = 5.0;
		Vector v = new Vector(0.4, 1.0);
		v.scale(c);
		assertTrue(v.equals(new Vector(2.0, 5.0)));
	}
	
	/**
	 * Tests as to whether two different Vector objects containing the same data are equal.
	 * Test method for {@link Vector#equals(Object)}
	 */
	@Test
	public void testEquals() {
		Vector v1 = new Vector(1.0, 1.0);
		assertTrue(test.equals(v1));
		assertNotSame(test, v1);
		assertFalse(test.equals(new Vector(-1.0, -1.0)));
	}
	
	/**
	 * Tests creating a String representation of the Vector;
	 * Test method for {@link Vector#toString()}
	 */
	@Test
	public void testToString() {
		String s = test.toString();
		assertEquals(s, "(1.0,1.0)");
	}

}
