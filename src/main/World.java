package main;


import java.lang.String;
import java.util.ArrayList;

import except.OutsideWorldException;
/**
 * 
 * @author Emmanuel Malikides (u4672869)
 * August 2009
 * 
 * The World class contains a list of type Objex, and a size. It implements methods
 * that initialize various shaped collections of Matter, and that update and maintain 
 * the present state of the world being simulated.
 */
public class World {

	//World has a size and a list of objects.
	public Integer xsize, ysize;
	public Objex objects;
	
	/**
	 * Builds a world of size (Xsize,Ysize), both measured in pixels.
	 * @param Xsize
	 * @param Ysize
	 */
	public World(Integer Xsize, Integer Ysize) 
	{
		xsize = Xsize;
        ysize = Ysize;
        objects = new Objex(Constants.nParticles); 
	}
    
    /**
     * Method that determines whether a point (x,y) is in this <i> World. </i>
     * @param x
     * @param y
     * @return Returns a boolean corresponding to whether the point is in the world.
     */
    public Boolean isInWorld(Double x, Double y)
    {
        return (x>=0)&&(x<=xsize)&&(y>=0)&&(y<=ysize);
    }
    
    /**
     * Method that draws the current <i> World </i> into a given Graphics object <i> canvas. </i>
     * @param canvas
     */
    public void draw(SimulationCanvas canvas) 
    {
        objects.draw(canvas);   	
    }
    
    /**
     * Finds a list of the possible atoms that that could collide with the atom at <i>index</i> in <i>objects</i>
     * @param Index
     * @return The list of atoms it could possibly collide with.
     */
    public Objex possibleCollisions(int index)
    {
        Objex others = new  Objex(Constants.nParticles);
        others.addAll(objects);

        others.remove(index);
    
        return others;
    }
    
    /**
     * Method that returns a string representing the current state of the world.
     */
    public String toString() {
    	StringBuffer buf = new StringBuffer();
    	for (Matter b: objects) {
    		buf.append(b.toString());
    		buf.append("\n");
    	}
    	return buf.toString();
    }
    
    /**
     * Method that updates the position and velocities of the <i> Matter </i> in <i> objects. </i>
     */
    public void step() 
    {
    	//update the acceleration of all the particles.
        for (Matter p: objects) {
        	if (p != null){
        		p.updateAcc();
        	}        	
        }
        
    	//update the velocity of all the particles.
    	for (Matter p: objects) 
        {
    		if (p != null){
        		p.updateV();
        	}
        }
    	
    	//Update the position of all the particles, after all the velocities have been updated, so as to make the movement symmetrical.
        for (Matter p: objects) {
        	if (p != null){
        		p.updateP();
        	}
        }
        
        //Check for collisions *After* the particles have been moved, this is so that if a particle is moved into 
        //another particle, this can be handled before the forces are calculated.
    	for (int i=0; i < objects.size(); i++){
    		Matter a = objects.get(i);
    		if (a != null){
    			a.boundary();
        	}     		  		
    		for (Matter b : possibleCollisions(i)){
    			if (b != null){
    				if (a != null){
                		a.collision(b);
                	} 
            	}	    			   			
    		}    		
        }       
        
    }
    
    /**
     * Method that constructs a single atom of <i> Matter </i> of type <i> bondedAtom </i>, at a specified position
     * With Specified velocity, and adds this to <i> objects </i>.
     * @param position
     * @param matterType
     * @param mass
     * @param xVelocity
     * @param yVelocity
     * @throws OutsideWorldException
     */
    public void makePoint(Vector position, String matterType, Double mass, Double xVelocity, Double yVelocity)
    throws OutsideWorldException
    {
        if (matterType.equals("bondedAtom")) 
        {
            BondedAtom newAtom = new BondedAtom(Constants.bondedRadius,
                                                   mass,
                                                   position.x,position.y,
                                                   xVelocity,
                                                   yVelocity
                                                   );
            BondedAtom newAtom2 = new BondedAtom(Constants.bondedRadius,
                                                   mass,
                                                   position.x,position.y-40,
                                                   xVelocity,
                                                   yVelocity
                                                   );
            newAtom.addBond(newAtom2,Constants.bondStrength, Constants.bondRestLength, Constants.bondMaxLength);
            objects.add(newAtom2);
            objects.add(newAtom);
        }
    }

    /**
     * Returns a boolean determining whether a particular position is within the circle defined by the given
     * centre and radius.
     * @param position 
     * @param centre
     * @param radius
     * @return 
     */
    public Boolean inCircle(Vector position,Vector centre, Double radius) 
    {
    	//return (position.x>=centre.x-radius)&&(position.x<=centre.x+radius)&&(position.y>=centre.y-radius)&&(position.y<=centre.y+radius);
    	return (position.vectorTo(centre).length()) <= radius;
    }	

    /**
     * Method that makes a ball-like collection of atoms of <i> Matter </i>, and adds them to <i> objects </i>.
     * @param position
     * @param radius
     * @param matterType
     * @param mass
     * @param xVelocity
     * @param yVelocity
     * @throws OutsideWorldException
     */
    public void makeBall(Vector position, Double radius,
            			 String matterType, 
            			 Double mass, Double xVelocity,
            			 Double yVelocity)
            			 throws OutsideWorldException
    {
    	// i and j are incrementors, nUpMiss is the number of balls that couldn't bond upwards in this row from
    	// the left.
    	int i=0,j=0, nUpMiss=0, nDownMissLast=0, nDownMissTmp=0, nInRow=0, nInRowTmp=0;
    	Boolean hasBondedUp = false, hasBondDown = false;
    	
    	int side = (int)((2.0*radius)/Constants.bondRestLength);
    	Double x = position.x - radius;
    	Double y = position.y - radius;
        if (!isInWorld(x,y))
        	throw new OutsideWorldException("You tried to make a ball outside of the world.");
    	
        //ensure the Array is big enough for the particles to be added.
        objects.ensureCapacity(objects.size() + side*side);
        
    	if (matterType.equals("bondedAtom"))
        {
            {            	             	
                for (j=0; j<side; j++)
                {
                    if (y > ysize)
                        throw new OutsideWorldException("You tried to construct a rectangle that was too high check3.");
                    while (i<side)
                    {
                        if (x > xsize) 
                            throw new OutsideWorldException("You tried to construct a rectangle that was too wide check2.");
                        
                        if (inCircle(new Vector(x,y), position, radius))
		                    {
		                    BondedAtom newAtom = new BondedAtom(Constants.bondedRadius,
		                                                   mass,
		                                                   x,y,
		                                                   xVelocity,
		                                                   yVelocity
		                                    );
		                    //add relevant bonds to the newAtom. 
		                    if ((!inCircle(new Vector(x,y-Constants.bondRestLength),position,radius))&&(nInRowTmp==0))
		                    {
		                       //Don't add bonds

		                    	if (!hasBondedUp)  nUpMiss++;
		                    }
		                    else if (nInRowTmp == 0)
		                    {
		                    	hasBondedUp = false;
		                        newAtom.addBond((BondedAtom)objects.get(objects.size()-(nInRow+nUpMiss-nDownMissLast)),
		                                                 Constants.bondStrength,
		                                                 Constants.bondRestLength,
		                                                 Constants.bondMaxLength
		                                                 );
		                    }
		                    else if ((!inCircle(new Vector(x,y-Constants.bondRestLength),position,radius)))
		                    {
		                    	newAtom.addBond((BondedAtom)objects.get(objects.size()-1),
                                        Constants.bondStrength,
                                        Constants.bondRestLength,
                                        Constants.bondMaxLength
                                        );
		                    	if (!hasBondedUp)  nUpMiss++;
		                    }
		                    else 
		                    {
		                    	hasBondedUp = false;
		                        newAtom.addBond((BondedAtom)objects.get(objects.size()-(nInRow+nUpMiss-nDownMissLast)),
		                                                 Constants.bondStrength,
		                                                 Constants.bondRestLength,
		                                                 Constants.bondMaxLength
		                                                 );
		                        newAtom.addBond((BondedAtom)objects.get(objects.size()-1),
		                                                 Constants.bondStrength,
		                                                 Constants.bondRestLength,
		                                                 Constants.bondMaxLength
		                                                 );
		                    }
		                	nInRowTmp++;
		                	if (inCircle(new Vector(x,y+Constants.bondRestLength), position, radius)) hasBondDown = true;
		                	if ((!hasBondDown)&&(!inCircle(new Vector(x,y+Constants.bondRestLength), position, radius))) nDownMissTmp++;
		                    objects.add(newAtom);
		                }
		                x += Constants.bondRestLength;
                        i++;
                    }
                    nInRow = nInRowTmp;
                    nInRowTmp = 0;
                    nDownMissLast = nDownMissTmp;
                    nDownMissTmp = 0;
                    nUpMiss = 0;
                    hasBondDown = false;
                    hasBondedUp = false;
                    i = 0;
                    x = position.x-radius;
                    y += Constants.bondRestLength;
                   }
            }
        }
        else if (matterType.equals("Sand"))
        {
            //TODO
        }
    }
    
    /**
     *Makes a rectangle-shaped lattice of atoms of type <i> matterType </i> (either "bondedAtom" or "sand").
     *It then adds this lattice to <i> objects</i>. 
     *If bondedAtom is not specified as the matterType, it is assumed to be sand.
     * @param position: position of top left-hand corner.
     * @param width: width in numbers-of-atoms.
     * @param height
     * @param matterType
     * @param mass
     * @param xVelocity
     * @param yVelocity
     * @throws OutsideWorldException
     */
    public void makeRectangle(Vector position, Integer width,
                              Integer height, String matterType, 
                              Double mass, Double xVelocity,
                              Double yVelocity)
                              throws OutsideWorldException
        {
        
        //Distance is the distance between the centre of various BondedAtoms.
        Double distance = Constants.bondRestLength;
        int i=0, j=0;
        Double x = position.x, y = position.y;

        //check if the position is in the world.
        if (!isInWorld(x,y))
            throw new OutsideWorldException("The position of your rectangle was not in the world check1");

        //ensure the Array is big enough for the particles to be added.
        objects.ensureCapacity(objects.size() + height*width);
        
        //add the particles and bonds.
        if (matterType.equals("bondedAtom"))
        {
            //handle the initial corner atom.
            BondedAtom initAtom = new BondedAtom(Constants.bondedRadius,
                                                   mass,
                                                   x,y,
                                                   xVelocity,
                                                   yVelocity
                                                );
            objects.add(initAtom);
            x += distance;
            i++;
            for (j=0; j<height; j++)
            {
                if (y > ysize)
                    throw new OutsideWorldException("You tried to construct a rectangle that was too high check3.");
                while (i<width)
                {
                    if (x > xsize) 
                        throw new OutsideWorldException("You tried to construct a rectangle that was too wide check2.");
                    BondedAtom newAtom = new BondedAtom(Constants.bondedRadius,
                                                   mass,
                                                   x,y,
                                                   xVelocity,
                                                   yVelocity
                                    );
                    //add relevant bonds to the newAtom. 
                    if (j==0)
                    {
                        newAtom.addBond((BondedAtom)objects.get(objects.size()-1),
                                                     Constants.bondStrength,
                                                     Constants.bondRestLength,
                                                     Constants.bondMaxLength
                                                     );
                    }    
                    else if (i==0) 
                    {
                        //we don't need to subtract one, because we haven't
                        //added the newAtom yet.
                        newAtom.addBond((BondedAtom)objects.get(objects.size()-width),
                                                 Constants.bondStrength,
                                                 Constants.bondRestLength,
                                                 Constants.bondMaxLength
                                                 );
                    }
                    else 
                    {
                        newAtom.addBond((BondedAtom)objects.get(objects.size()-width),
                                                 Constants.bondStrength,
                                                 Constants.bondRestLength,
                                                 Constants.bondMaxLength
                                                 );
                        newAtom.addBond((BondedAtom)objects.get(objects.size()-1),
                                                 Constants.bondStrength,
                                                 Constants.bondRestLength,
                                                 Constants.bondMaxLength
                                                 );
                     }
                    objects.add(newAtom);
                    x += distance;
                    i++;
                }
                i = 0;
                x = position.x;
                y += distance;
               }
        }
        else 
        {
        //TODO
        }
        
        }
    
    
    /**
     *Defines the shape in which the initial lattic can be created. 
     *@param x,y: coordinates of atom
     *@param initX, initY: coordinates of the square's top left corner
     *@param width : square's side length or circle's diameter.
     */
    public boolean inShape(Double x, Double y, Double initX, Double initY, Double width){
    	Double r = width*Constants.bondRestLength/2;
    	// For a circle
    	return (Math.sqrt(Math.pow(initX+r-x,2) + Math.pow(initY+r-y, 2))<= r);
    	// For a rectangle
    	//return true;
    }
    
    public boolean inShape2(ArrayList<Vector> vectors, Vector pos){
    	/*int crosses = 0;
    	int vertices = vectors.length;
    	
    	// TODO Tricky if there is a horizontal side. 
    	
    	for (int i=0; i< vertices-1; i++){
    		// True if side is intersected by positive, horizontal ray from point.
    		if((((vectors[i+1].y < pos.y)&&(pos.y < vectors[i].y ))
    			|| ((vectors[i+1].y > pos.y)&&(pos.y > vectors[i].y)))
    			&&(!((vectors[i+1].x<pos.x)&&(vectors[i].x<pos.x)))){
    			crosses++;
    		}
    		//True if ray passes through vertex.
    		if((vectors[i].y==pos.y)&&(vectors[i].x>pos.x)) crosses++;
    	}
    	//For side from last to first vertex in array.
    	if((((vectors[vertices].y < pos.y)&&(pos.y < vectors[0].y ))
    			|| ((vectors[vertices].y > pos.y)&&(pos.y > vectors[0].y)))
    			&&(!((vectors[vertices].x<pos.x)&&(vectors[0].x<pos.x)))){
    			crosses++;
    		}
    	if((vectors[vertices].y==pos.y)&&(vectors[vertices].x>pos.x)) crosses++;
    	return (crosses % 2 != 0);    
    	
    	
    	int crosses = 0;
    	int vertices = vectors.size();
    	
    	// TODO Tricky if there is a horizontal side. 
    	
    	for (int i=0; i< vertices-1; i++){
    		// True if side is intersected by positive, horizontal ray from point.
    		if((((vectors.get(i+1).y < pos.y)&&(pos.y < vectors.get(i).y ))
    			|| ((vectors.get(i+1).y > pos.y)&&(pos.y > vectors.get(i).y)))
    			&&(!((vectors.get(i+1).x<pos.x)&&(vectors.get(i).x<pos.x)))){
    			crosses++;
    		}
    		//True if ray passes through vertex.
    		if((vectors.get(i).y==pos.y)&&(vectors.get(i).x>pos.x)) crosses++;
    	}
    	//For side from last to first vertex in array.
    	if((((vectors.get(vertices-1).y < pos.y)&&(pos.y < vectors.get(0).y ))
    			|| ((vectors.get(vertices-1).y > pos.y)&&(pos.y > vectors.get(0).y)))
    			&&(!((vectors.get(vertices-1).x<pos.x)&&(vectors.get(0).x<pos.x)))){
    			crosses++;
    		}
    	if((vectors.get(vertices-1).y==pos.y)&&(vectors.get(vertices-1).x>pos.x)) crosses++;
    	return (crosses % 2 != 0); 
    	*/    	
    	return true;
    }
    
    
    
    
    public boolean inShape3(ArrayList<Vector> vectors, Vector pos){
    	 	  /*int j=vectors.size()-1 ;
    		  boolean  oddNodes= false;
    		  for (int i=0; i<vectors.size(); i++) {
    		    if ((vectors.get(i).y<pos.y && vectors.get(j).y>=pos.y)
    		    ||  (vectors.get(j).y<pos.y && vectors.get(i).y>=pos.y)) {
    		      if (vectors.get(i).x+(pos.y-vectors.get(i).y)/(vectors.get(j).y-vectors.get(i).y)*(vectors.get(j).x-vectors.get(i).x)< pos.x) {
    		        oddNodes=!oddNodes; 
    		        System.out.println(oddNodes);
    		        //(polyX[i]+(y-polyY[i])/(polyY[j]-polyY[i])*(polyX[j]-polyX[i])<x)    		        
    		      }
    		    }
    		    j=i;
    		  }
    		  return oddNodes;*/
    	
		  boolean oddNodes= false;
		  int last = vectors.size()-1;
		  if ((vectors.get(0).y<pos.y && vectors.get(last).y>=pos.y)
					||  (vectors.get(last).y<pos.y && vectors.get(0).y>=pos.y)) {
					  if (vectors.get(0).x+(pos.y-vectors.get(0).y)/(vectors.get(last).y-vectors.get(0).y)*(vectors.get(last).x-vectors.get(0).x)> pos.x) {
					    oddNodes=!oddNodes; 				    		           		        
					  }
					}
		  for (int i=0; i<last; i++) {				  
				if ((vectors.get(i).y<pos.y && vectors.get(i+1).y>=pos.y)
				||  (vectors.get(i+1).y<pos.y && vectors.get(i).y>=pos.y)) {
				  if (vectors.get(i).x+(pos.y-vectors.get(i).y)/(vectors.get(i+1).y-vectors.get(i).y)*(vectors.get(i+1).x-vectors.get(i).x)> pos.x) {
				    oddNodes=!oddNodes; 				    		           		        
				  }
				}		    
		  }
		  return oddNodes;
		  
    	
    		 
    }
         
    
    public void createShape(ArrayList<Vector> vectors, Double mass, Vector velocity) throws OutsideWorldException{   	
    	
    	
    	/* 	 
     	if(vectors.length!=0){
    		Double minX = vectors[0].x;
        	Double maxX = vectors[0].x;
        	Double minY = vectors[0].y;
        	Double maxY = vectors[0].y;  
        	for (int i=1; i<vectors.length; i++) {
        		  if (vectors[i].x<minX) minX = vectors[i].x;
        		  if (vectors[i].x>maxX) maxX = vectors[i].x;
        		  if (vectors[i].y<minY) minY = vectors[i].y;
        		  if (vectors[i].y>maxY) maxY = vectors[i].y;    		  
        	}
        	Vector latticePos= new Vector(minX,minY);
        	Integer latticeW = (int)((maxX-minX)/Constants.bondRestLength);
        	Integer latticeH = (int)((maxY-minY)/Constants.bondRestLength);
        	makeShape(vectors,latticePos, latticeW,latticeH, "bondedAtom",mass, velocity);
    	}*/
    	if(!vectors.isEmpty()){
			Double minX = vectors.get(0).x;
	    	Double maxX = vectors.get(0).x;
	    	Double minY = vectors.get(0).y;
	    	Double maxY = vectors.get(0).y;  
	    	for (int i=1; i<vectors.size(); i++) {
	    		  if (vectors.get(i).x<minX) minX = vectors.get(i).x;
	    		  if (vectors.get(i).x>maxX) maxX = vectors.get(i).x;
	    		  if (vectors.get(i).y<minY) minY = vectors.get(i).y;
	    		  if (vectors.get(i).y>maxY) maxY = vectors.get(i).y;    		  
	    	}
	    	Vector latticePos= new Vector(minX,minY);
	    	Integer latticeW = (int)((maxX-minX)/Constants.bondRestLength);
	    	Integer latticeH = (int)((maxY-minY)/Constants.bondRestLength);
	    	makeShape(vectors,latticePos, latticeW,latticeH, "bondedAtom",mass, velocity);
    	}
    	
    }
    
    
    /**
     *Makes a lattice of bonded atoms in the intersection of the given rectangle 
     *and the space defined by inShape.
     *
     * @param position: position of top left-hand corner.
     * @param width: width of limiting rectangle in numbers-of-atoms.
     * @param height
     * @param matterType
     * @param mass
     * @param velocity
     * @throws OutsideWorldException
     */
    public void makeShape(ArrayList<Vector> vectors, Vector position, Integer width,
                              Integer height, String matterType, 
                              Double mass, Vector velocity)
                              throws OutsideWorldException
    {
        
        //Distance is the distance between the centre of various BondedAtoms.
        Double distance = Constants.bondRestLength;
        Double x = position.x, y = position.y;
        Double vX = velocity.x, vY = velocity.y;
        Double initX =x;
        Double initY =y;

        //check if the position is in the world.
        if (!isInWorld(x,y))
            throw new OutsideWorldException("The position of your rectangle was not in the world check1");

        //ensure the Array is big enough for the particles to be added.
        objects.ensureCapacity(objects.size() + height*width);
        
        //add the particles and bonds.
        for(int j=0; j<height; j++){
        	if (y > ysize)
                throw new OutsideWorldException("You tried to construct a rectangle that was too high check3.");
        	for (int i=0; i<width; i++){
        		if (x > xsize) 
                    throw new OutsideWorldException("You tried to construct a rectangle that was too wide check2.");
        		
        		//if (inShape(x,y, initX, initY, (double) width)){
        		if (inShape3(vectors,new Vector(x,y))){	
        			BondedAtom newAtom = new BondedAtom(Constants.bondedRadius,
   	                     mass,
   	                     x,y,
   	                     vX,
   	                     vY
   	        	 		 );
        			if (j!=0){
        				if (objects.get(objects.size()-width) != null){
            				newAtom.addBond((BondedAtom)objects.get(objects.size()-width),
            						(0.5+Math.random())*Constants.bondStrength,
     	                           Constants.bondRestLength,
     	                           Constants.bondMaxLength
     	                           );
        				}
        			}
        			if (i!=0){
        				if (objects.get(objects.size()-1) != null){
    	           			newAtom.addBond((BondedAtom)objects.get(objects.size()-1),
    		                           (0.5+Math.random())*Constants.bondStrength,
    		                           Constants.bondRestLength,
    		                           Constants.bondMaxLength
    		                           );
        				} 		
	           			
	           		}	        			           		
	           		objects.add(newAtom);	
        		}
        		else {
        			objects.add(null);	
        		}         		
        		x += distance;
        	}
        	x = initX;
        	y += distance;
        }
           
    }
    /**
	 * Makes a chain.
	 */
    public void makeChain(Vector position, Integer length,            
            Double mass, Vector velocity)
            throws OutsideWorldException
    {
    	 Double radius = Constants.bondedRadius;
         Double x = position.x, y = position.y;         
         
         //check if the position is in the world.
         if (!isInWorld(x,y))
             throw new OutsideWorldException("The position of your chain was not in the world check1");

         //ensure the Array is big enough for the particles to be added.
         objects.ensureCapacity(objects.size() + length);
         
         Pivot pivot = new Pivot(radius, mass, position, velocity);
         objects.add(pivot);
         System.out.println(pivot.position);  
         y += radius*2;
                
         for (int i=1;i<length;i++){
     		if ((y>ysize)||(x>xsize))
                 throw new OutsideWorldException("You tried to construct a chain that was too long.");    		
     		Chain newChain = new Chain(radius, mass, new Vector(x,y),
     				velocity, objects.get(objects.size()-1),null);
     		objects.add(newChain);
     		if (i>1) ((Chain) objects.get(objects.size()-2)).next = newChain;
     		System.out.println(newChain.position);
     		
     		
     		
     		y += radius*2;
     		
         }
         objects.get(objects.size()-length);
         
         // For the position updated one, which causes some whacky results.           
         /*for (int i=1;i<length;i++){
    		if ((y>ysize)||(x>xsize))
                throw new OutsideWorldException("You tried to construct a chain that was too long.");    		
    		Link newLink = new Link(radius, mass, new Vector(x,y),
    				velocity, objects.get(objects.size()-1));
    		objects.add(newLink);
    		y += radius*2;
    	
    	 }*/
    }
    
    	/**
    	 * Makes a pyramid shaped collection of atoms, and adds to objects.
    	 * @param position
    	 * @param height
    	 * @param matterType
    	 * @param mass
    	 * @param xVelocity
    	 * @param yVelocity
    	 * @throws OutsideWorldException
    	 */
        public void makePyramid (Vector position, Integer height, 
        						 String matterType, 
        						 Double mass, Double xVelocity,
        						 Double yVelocity)
            					 throws OutsideWorldException
        {
        }
        

        
}

