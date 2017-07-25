/**
 * 
 */
package unit;

import main.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import except.OutsideWorldException;

/**
 * Test class for World.
 * @author u4672869
 *
 */
@SuppressWarnings("unused")
public class WorldTest {

	private World newWorld;
	private Objex objects; 
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		newWorld = new World(Constants.xsize,Constants.ysize);
		objects = new Objex(17);
	
	}
	

	/**
	 * Test method for {@link main.World#isInWorld(java.lang.Double, java.lang.Double)}.
	 */
	@Test
	public void testIsInWorld() {
		for (int i=0; i<10; i++)
		{
			for (int j=0; j<10; j++)
			{
				assertTrue(newWorld.isInWorld(i*60.0,j*60.0));
			}
		}	
		assertFalse(newWorld.isInWorld(-12.0,1.0));
		assertFalse(newWorld.isInWorld(-100.0,-1.0));
		assertFalse(newWorld.isInWorld(-12.0,601.0));
		assertFalse(newWorld.isInWorld(3002.0,3000000.0));
		assertFalse(newWorld.isInWorld(-12.0,-6.0));
	}


	/**
	 * Test method for {@link main.World#possibleCollisions(java.lang.Integer)}.
	 */
	@Test
	public void testPossibleCollisions() {
		Objex objectsclone = new Objex(Constants.nParticles);
		Vector position1 = new Vector(200.0,200.0);
		try {
		newWorld.makePoint(position1, "bondedAtom", Constants.bondedMass, -2.0, 0.0);
		newWorld.makePoint(position1, "bondedAtom", Constants.bondedMass, -2.0, 0.0);
		}
		catch (OutsideWorldException e) {}
		objectsclone.addAll(newWorld.objects);
		objectsclone.remove(0);
		assertTrue((newWorld.possibleCollisions(0).size())== 1);
		assertTrue((newWorld.possibleCollisions(0)).equals(objectsclone));
	}

	/**
	 * Test method for {@link main.World#makePoint(main.Vector, java.lang.String, java.lang.Double, java.lang.Double, java.lang.Double)}.
	 */
	@Test
	public void testMakePoint() {
		Vector position = new Vector(20.0,400.0);
		Vector position1 = new Vector(30.0,400.0);
		Vector position2 = new Vector(40.0,400.0);
		try {
		newWorld.makePoint(position,  "bondedAtom", Constants.bondedMass, 0.0, 0.0);
		newWorld.makePoint(position1, "bondedAtom", Constants.bondedMass, 0.0, 0.0);
		newWorld.makePoint(position2, "bondedAtom", Constants.bondedMass, 0.0, 0.0);
		}
		catch (OutsideWorldException e)
		{
			System.out.println("Some error occured" + e.getMessage());
		}
		assertTrue((newWorld.objects.size())==3);
		assertTrue((newWorld.objects.get(2).position).equals(position2));
		assertTrue((newWorld.objects.get(2).mass).equals(Constants.bondedMass));
		assertTrue((newWorld.objects.get(2).velocity).equals(new Vector(0.0,0.0)));
	}

	/**
	 * Test method for {@link main.World#makeRectangle(main.Vector, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.Double, java.lang.Double, java.lang.Double)}.
	 */
	@Test
	public void testMakeRectangle() {
		Vector position = new Vector(20.0,400.0);
		try {
		newWorld.makeRectangle(position, 3, 3, "bondedAtom", Constants.bondedMass, 0.0, 0.0);
		}
		catch (OutsideWorldException e)
		{
			System.out.println("Some error occured" + e.getMessage());
		}
		assertTrue((newWorld.objects.size())==9);
		assertTrue((newWorld.objects.get(8).position.equals(new Vector(20.0 + Constants.bondRestLength*2, 400.0 + Constants.bondRestLength*2))));
		assertTrue((newWorld.objects.get(8).mass).equals(Constants.bondedMass));
		assertTrue((newWorld.objects.get(8).velocity).equals(new Vector(0.0,0.0)));
	}
	
	/**
	 * Test method for {@link main.World#draw(SimulationCanvas canvas)}.
	 
	@Test
	public void testdraw()
	{
	
		SimulationCanvas c1 = new SimulationCanvas(Constants.xsize, Constants.ysize);
		SimulationCanvas c2 = new SimulationCanvas(Constants.xsize, Constants.ysize);
		//assertTrue(c1.equals(c2));
		//newWorld.draw(c2);
		//assertFalse(c1.equals(c2));
	} **/
	
}
