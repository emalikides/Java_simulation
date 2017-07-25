/**
 * @author Michael Asher u4666424
 * August 2009
 * Comp1110 Assignment
 */

package main;

import java.awt.Color;
import java.awt.Graphics;

import except.NotInBondException;
import except.ZeroMassException;

public class Chain extends Matter{

	/**
	 * The previous and next link in the chain.
	 */
	Matter previous, next;
	
	public Chain(Double radius, Double mass, Double x, Double y, Double vx, Double vy, Matter p, Matter n)  {
		super(radius, mass, new Vector(x, y), new Vector (vx, vy));
		previous = p;
		next= n;
	}
	
	public Chain(Double radius, Double mass, Vector pos, Vector vel, Matter p, Matter n) {
		super(radius, mass, pos, vel);
		previous = p;
		next= n;
	}

	/**
	 * Updates the acceleration of the link due to the previous and next link, and gravity.
	 */
	public void updateAcc() {		
		acceleration.set(0.0,0.0);	
		Vector chainAcc;
		Vector toP = position.vectorTo(previous.position);
		Vector toPDirection = toP.unit();
		Double toPDistance = toP.length()-2*this.radius;
		if(next != null){
			Vector toN = position.vectorTo(next.position);
			Vector toNDirection = toN.unit();
			Double toNDistance = toN.length()-2*radius;
			chainAcc = toNDirection.scaleim(toNDistance*Math.pow(1,Math.abs(toNDistance)));
			System.out.println(toN.toString());
			System.out.println(toNDirection.toString());
			System.out.println(toNDistance.toString());
			
		}
		else chainAcc = new Vector(0.0,0.0); 
		chainAcc.add(toPDirection.scaleim(toPDistance*Math.pow(1, Math.abs(toPDistance))));
			
		acceleration.add(chainAcc);
		acceleration.add(Constants.gravity);
		//acceleration.round();		
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
		Color c = new Color(67,147,194);
		g.setColor(c);
		g.fillOval((int)(position.x - radius), (int)(position.y - radius), (int)(2*radius), (int)(2*radius));
		//Attempt at drawing the links between links.
		g.drawLine(this.position.x.intValue(), this.position.y.intValue(), 
				this.previous.position.x.intValue() , this.previous.position.y.intValue());
				
	}
	
}
