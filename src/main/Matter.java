/**
 * @author Michael Asher u4666424
 * August 2009
 * Comp1110 Assignment
 */

package main;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public abstract class Matter {
	static final ImageIcon matter = new ImageIcon("matter.png");
	public Vector position;
	public Vector velocity;
	public Vector acceleration;
	public Double radius;
	public Double mass;
	
	/**
	 * @param radius - the radius of the particle
	 * @param mass - the mass of the particle
	 * @param position - a 2D vector representing the position of the particle
	 * @param velocity- a 2D vector representing the velocity of the particle
	 */	
	
	public Matter(Double radius, Double mass, Vector pos, Vector vel) {
		this.radius =radius;
		this.mass = mass;
		this.position = pos;
		this.velocity = vel;
		this.acceleration = new Vector(0.0,0.0);
	}

    /**
	 * Checks if a particle is near enough to another to collide, 
	 * then updates the velocity after an elastic collision.
	 * @param p - the other particle 
	 * @return boolean - whether or not the particle p is close enough to collide
	 */
	
	public Boolean checkCollision(Matter p) {
		return (((position.vectorTo(p.position)).length() <= radius + p.radius)
				//extra check to ensure the balls are moving towards each other.
		  &&(position.vectorTo(p.position).dot(velocity) - position.vectorTo(p.position).dot(p.velocity) > 0));   
	 }
	/**
	 * Function that returns whether a particular atom occupies the same space as another.
	 * @param p
	 * @return
	 */
	public Boolean areCoincident(Matter p) {
		return 	((position.vectorTo(p.position)).length() == 0);
	}
	
// Collision method that is just wrong
/*	public void collision(Matter p){
		if (this.checkCollision(p)){
			this.velocity.x = (this.velocity.x*(this.mass-p.mass) + 2*p.mass*p.velocity.x)/(this.mass+p.mass);
			this.velocity.y = (this.velocity.y*(this.mass-p.mass) + 2*p.mass*p.velocity.y)/(this.mass+p.mass);
		}		
	}
	*/
//Other collision methods that are 'right' but wrong. 
	/*
	public void collision(Matter p){
		if (this.checkCollision(p)){
			Double g1 = Math.atan((velocity.y-p.velocity.y)/(velocity.x-p.velocity.x));
			Double g2 = Math.atan((p.position.y-position.y)/(p.position.x-position.x));
			Double d = Math.sqrt((position.x-p.position.x)*(position.x-p.position.x) + (position.y-p.position.y)*(position.y-p.position.y));
			Double g3 = g1-g2;
			if (g3 > 2*Math.PI) {
				g3 -= 2*Math.PI;
			}
			else if (g3 < 2*Math.PI) {
				g3 += 2*Math.PI;
			}
			Double alpha = Math.asin(d*(Math.sin(g3))/(2*radius));				
			Double a = Math.tan(g1+alpha);		
			Double deltaV = -2*(velocity.x-p.velocity.x+ a*(velocity.y-p.velocity.y))/((1+a*a)*(1+p.mass/mass));
			velocity.x -= (p.mass/mass)*deltaV;
			velocity.y -= a*(p.mass/mass)*deltaV;
			
			//Slightly edited, still doesn't work.
			Double g1 = Math.atan((velocity.y-p.velocity.y)/(velocity.x-p.velocity.x));
			Double g2 = Math.atan((p.position.y-position.y)/(p.position.x-position.x));
			Double d = position.vectorTo(p.position).length();
			Double g3 = g1-g2;
			if (g3 > 2*Math.PI) {
				g3 -= 2*Math.PI;
			}
			else if (g3 < -2*Math.PI) {
				g3 += 2*Math.PI;
			}
			Double alpha = Math.asin(d*(Math.sin(g3))/(radius+p.radius));				
			Double a = Math.tan(g1+alpha);
			Double deltaV = -2*(velocity.x-p.velocity.x+ a*(velocity.y-p.velocity.y))/((1+a*a)*(1+mass/p.mass));
			p.velocity.x -= (mass/p.mass)*deltaV*Constants.timeResolution;
			p.velocity.y -= a*(mass/p.mass)*deltaV*Constants.timeResolution;
			velocity.x += deltaV*Constants.timeResolution;
			velocity.y += a*deltaV*Constants.timeResolution;
		}
	}
*/	
	/**
	 * A method that alters the velocities of two particles of <i> Matter </i> according to
	 * a simple elastic collision, after first checking whether the particles collide.
	 * @param p The particle that could collide with this particle.
	 */
	public void collision(Matter p){
		if (this.checkCollision(p)){
			
			
			//Eric's Swapping the projection method, with ingenious modification that treats the projection
			//as a separate one-dee Newtonian collision and accounts for mass this way.
			//(May not be physically correct, but it sure looks it).
			Vector this2p = new Vector(p.position);
			this2p.subtract(position);
			
			//rely on slow enough speeds to ensure this2p is never zero. -possibly a bad decision:
			//This vector is the projection of this velocity onto the line between the atoms.
			Vector vProjectionthis = new Vector(this2p.scaleim(this2p.dot(velocity)/Math.pow(this2p.length(),2)));
			
			//This vector is the projection of the other atom's velocity onto the line between the atoms.
			Vector vProjectionp = new Vector(this2p.scaleim(this2p.dot(p.velocity)/Math.pow(this2p.length(),2)));
			
			//Store relevant temporary vectors.
			Vector vPTtmp = new Vector(vProjectionthis);
			Vector vpptmp = new Vector(vProjectionp);
			
			//scale the vectors depending on the masses.
			vProjectionthis = (vProjectionthis.scaleim(this.mass-p.mass).addim(vProjectionp.scaleim(2*p.mass))).scaleim(1/(this.mass+p.mass));
			vProjectionp = (vProjectionp.scaleim(p.mass-this.mass).addim(vPTtmp.scaleim(2*mass))).scaleim(1/(this.mass+p.mass));
			
			//Swap the relevant velocities, scaling by relevant damping.
			velocity.subtract(vPTtmp);
			velocity.add(vProjectionthis.scaleim(Constants.damping));
			p.velocity.subtract(vpptmp);
			p.velocity.add(vProjectionp.scaleim(Constants.damping));
		
			/*
			//Newtonian 1D in both dimensions, does not account for angle, but does account for masses !works!.
			Double vxtmp = velocity.x;
			Double vytmp = velocity.y;
			
			this.velocity.x = (this.velocity.x*(this.mass-p.mass) + 2*p.mass*p.velocity.x)/(this.mass+p.mass);
			this.velocity.y = (this.velocity.y*(this.mass-p.mass) + 2*p.mass*p.velocity.y)/(this.mass+p.mass);
			p.velocity.x = ((p.velocity.x*(p.mass-mass) + 2*mass*vxtmp)/(this.mass+p.mass))*Constants.damping;
			p.velocity.y = ((p.velocity.y*(p.mass-mass) + 2*mass*vytmp)/(this.mass+p.mass))*Constants.damping;
			*/
		} else if (areCoincident(p))
		{
			//Attempt to reset the position of this particle so it is outside the colliding particle,
			//by moving it back on its trajectory until it is exactly at the other atom.
			if (velocity.length() != 0) position.add(velocity.scaleim(-2*Constants.bondedRadius/velocity.length())); 
			//really this should never happen, unless as a result of poor initialization.
			//A possible alternative might be to throw an error.
			else {
				Vector v = new Vector (Math.random(),Math.random());
				position.add(v.scaleim(-2*Constants.bondedRadius/v.length()));
			}
			
			Vector this2p = new Vector(p.position);
			this2p.subtract(position);
			
			//This vector is the projection of this velocity onto the line between the atoms.
			Vector vProjectionthis = new Vector(this2p.scaleim(this2p.dot(velocity)/Math.pow(this2p.length(),2)));
			//This vector is the projection of the other atom's velocity onto the line between the atoms.
			Vector vProjectionp = new Vector(this2p.scaleim(this2p.dot(p.velocity)/Math.pow(this2p.length(),2)));
			
			//Swap the relevant velocities, scaling by relevant damping.
			velocity.subtract(vProjectionthis);
			velocity.add(vProjectionp.scaleim(Constants.damping));
			p.velocity.subtract(vProjectionp);
			p.velocity.add(vProjectionthis.scaleim(Constants.damping));
		}
	
	
	}
	
	/**
	 * Checks if a particle is near enough a boundary of the world 
	 * and moving towards that boundary, 
	 * then updates its velocity after an elastic collision with the boundary.
	 */
	
	public void boundary(){
		if ((Constants.xsize - position.x < radius)&&(velocity.x > 0)) {
			position.x = Constants.xsize - radius; 
			velocity.x = -Constants.damping*(velocity.x);	
		}
		else if ((position.x < radius)&&(velocity.x<0)){
			position.x = radius;
			velocity.x = -Constants.damping*(velocity.x);			
		}
		else if ((Constants.ysize - position.y < radius)&&(velocity.y > 0)){
			position.y = Constants.ysize - radius; 
			velocity.y = -Constants.damping*(velocity.y);	
		}
		else if ((position.y < radius)&&(velocity.y<0)){
			position.y = radius;
			velocity.y = -Constants.damping*(velocity.y);			
		}			
	}
	
/* Reduntant version 
 * public void boundary(){
		if((Constants.xsize - position.x < radius) && (velocity.x > 0)){			
			velocity.x = -Constants.damping*(velocity.x);			
		}
		else if((position.x < radius)&&(velocity.x<0)){
			velocity.x = -Constants.damping*(velocity.x);			
		}
		else if((Constants.ysize - position.y < radius) && (velocity.y > 0)){			
			velocity.y = -Constants.damping*(velocity.y);			
		}
		else if((position.y < radius)&&(velocity.y<0)){
			velocity.y = -Constants.damping*(velocity.y);			
		}		
	}
 */
	
	/**
	 * Updates the acceleration property of this particle of <i> Matter. </i>
	 */
	abstract public void updateAcc();
	
	/**
	 * Updates the velocity of this matter according to its acceleration.
	 */
	public void updateV(){
		velocity.add(acceleration.scaleim(Constants.timeResolution));
		//Round to reduce cumulative error 
		velocity.round();
	}
	
	/**
	 * Updates the position of this matter according to its velocity.
	 */
	abstract public void updateP();
	
	/**
	 * Step now in world, at this level of the hierarchy, we should not need to know about other 
	 * particles in the world, passing an ArrayList is unneeded, when it could all be done simply in world.
	 */

	

/*OLD UPDATE
 	public Vector updateAcc(ArrayList<Matter> particles){
		Vector boundaryAcc = new Vector(0.0,0.0);
		Vector b = boundary();
		b.scale(-1.0);
		boundaryAcc.add(b);
		
		Vector collisionAcc = new Vector(0.0,0.0);
		for(Matter p : particles){
			Vector c = collision(p);
			c.scale(-1.0);
			collisionAcc.add(c);						
		}
				Vector totalAcc = new Vector(0.0,0.0);
		totalAcc.add(collisionAcc);
		totalAcc.add(boundaryAcc);
		totalAcc.add(Constants.gravity);
		return totalAcc;
	}
*/
	/**
	 * Draws the image for matter at the matters location	 
	 * @param canvas - the SimulationCanvas 
	 *
	 */


	public abstract void draw(SimulationCanvas canvas) ;
    
	/**
	 * Returns a string containing the state of this matter.
	 */
	public String toString() {
		return ("Position: "+position.toString()+" velocity: "+velocity.toString()+"Acceleration: "+acceleration.toString()+" radius: "+radius+" mass: "+mass);
	}
}
