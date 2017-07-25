/**
 * @author Michael Asher u4666424
 * August 2009
 * Comp1110 Assignment
 */

package main;

import java.awt.Color;
import java.awt.Graphics;


public class Link extends Matter {
	/**
	 * The previous link in the chain.
	 */
	Matter link;
	
	public Link(Double radius, Double mass, Double x, Double y, Double vx, Double vy, Matter l)  {
		super(radius, mass, new Vector(x, y), new Vector (vx, vy));
		link =l;
	}
	
	public Link(Double radius, Double mass, Vector pos, Vector vel, Matter l) {
		super(radius, mass, pos, vel);
		link =l;
	}
	
	/**
	 * Updates the acceleration of the link.
	 */
	public void updateAcc() {		
		acceleration.set(0.0,0.0);				
		acceleration.add(Constants.gravity);
		//acceleration.round();		
	}
	
	/**
	 * Updates the position of this matter according to its velocity.
	 */
	public void updateP(){
		this.position.add(velocity.scaleim(Constants.timeResolution));
		Vector toL = this.position.vectorTo(link.position);
		Vector toLDirection = toL.unit();
		Double toLDistance = toL.length()-2*this.radius;		
		this.position.add(toLDirection.scaleim(toLDistance));
	}
	
	/**
	 * {@inheritDoc Matter#draw(SimulationCanvas)}
	 */
	public void draw(SimulationCanvas canvas) 
    {
		Graphics g = canvas.getOffscreenGraphics();
		Color c = new Color(67,147,194);
		g.setColor(c);
		g.fillOval((int)(position.x - radius), (int)(position.y - radius), (int)(2*radius), (int)(2*radius));
		//Attempt at drawing the links between links.
		g.drawLine(this.position.x.intValue(), this.position.y.intValue(), 
				this.link.position.x.intValue() , this.link.position.y.intValue());
				
	}
	
}
