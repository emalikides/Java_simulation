/**
 * @author Michael Asher u4666424
 * August 2009
 * Comp1110 Assignment
 */

package main;

import java.awt.Color;
import java.awt.Graphics;

public class Pivot extends Matter{

	
	public Pivot(Double radius, Double mass, Double x, Double y, Double vx, Double vy)  {
		super(radius, mass, new Vector(x, y), new Vector (vx, vy));
	}
	
	public Pivot(Double radius, Double mass, Vector pos, Vector vel) {
		super(radius, mass, pos, vel);
	}
	/**
	 * Chain is the pivot for the chain, so acceleration is always zero. 
	 */
	public void updateAcc() {
		acceleration.set(0.0,0.0);
	}
	
		
	/**
	 * Updates the position of this matter according to its velocity. 
	 * It should be stationary.
	 */
	public void updateP(){
	}
	/**
	 * {@inheritDoc Matter#draw(SimulationCanvas)}
	 */
	public void draw(SimulationCanvas canvas) 
    {
		Graphics g = canvas.getOffscreenGraphics();
		Color c = new Color(245,186,65);
		g.setColor(c);
		g.fillOval((int)(position.x - radius), (int)(position.y - radius), (int)(2*radius), (int)(2*radius));
		
	}
	
}
