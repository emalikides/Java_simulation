package main;
import java.util.ArrayList;
/**
 * Objex creates a list of Matter, for storing all the atoms in a World.
 * @author Emmanuel Malikides
 * August 2009
 * 
 */
public class Objex extends ArrayList<Matter> {

    /**
     * Constructs a list with a initial capacity of sizeMe.
     * @param sizeMe
     */
	public Objex(int sizeMe) {
        super(sizeMe);
        }
    
	/**
	 * Draws the atoms of Matter contained in this class to a given Graphics object canvas.
	 * @param canvas
	 */
    public void draw(SimulationCanvas canvas)
	{
		for (Matter m: this)
		{
			if (m != null){
				m.draw(canvas);
			}		
		}
	}
}
