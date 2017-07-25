package main;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.MemoryImageSource;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import except.*;


/**
 * 
 * Simulation - This class is the main simulation class.
 * 
 * @author Eric McCreath
 * 
 *         2005, 2009
 * 
 * 
 * To turn this into a jar put all the files in the one directory and from the 
 * command line type:
 *   jar cmf MANIFEST.MF Simulation.jar *.java *.class *.png
 * Then to run it type:
 *   java -jar Simulation.jar
 *   
 * The MANIFEST.MF file should contain:
 *   Manifest-Version: 1.0
 *   Main-Class: Simulation
 * 
 */

public class Simulation implements ActionListener {

	final static Integer stepsTotal = 20;
	final static Integer delay = 90; // milliseconds

	private SimulationCanvas canvas;
	private World world;
	private JFrame jframe; 
	private MyListener listener;
	private Timer timer;
	private Integer steps;
	public static ArrayList<Vector> vertices = new ArrayList<Vector>();
	
	
	
			
	public Simulation() {
		jframe = new JFrame("Real Collsiions");
		canvas = new SimulationCanvas(Constants.xsize, Constants.ysize);
		(jframe.getContentPane()).add(canvas);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		//SandA
		/*try {
			menubar = new MenuBar(jframe, this);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}*/		
		jframe.setResizable(false); // fixed window size
		jframe.pack();		
		listener = new MyListener(jframe, this); // track mouse events
		
		//end, except jframe.pack();
		
		jframe.setVisible(true);
		timer = new Timer(delay, this);
						
		//Attempt at start button, works if put in main, but then the close button breaks.
		/*JButton start = new JButton("Start");
        JPanel panel = new JPanel();
        class StartListener implements ActionListener
        {
           public void actionPerformed(ActionEvent event)
           {
              starter = true;
           }
        }
        ActionListener listener = new StartListener();
        start.addActionListener(listener);       
        panel.add(start);
        jframe.add(panel);*/
				       				
		/* start = new JButton("Start");
				//start.setMnemonic(KeyEvent.VK_D);
		//start.addActionListener(this);
		start.setBounds(new Rectangle(150,60,80,30));
		start.setVisible(true);
		start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	 starter =true;
            	 start.setVisible(false);
            }
        });
		jframe.add(start);
		*/
    }

	public void runSimulation() {
		System.out.println("Execution mode enabled.");
		//setbackground();
        //Vector position3 = new Vector(300.0,300.0);
        world = new World(Constants.xsize, Constants.ysize);
      
	  try {
      	//world.makePoint(position1,"bondedAtom",Constants.bondedMass,0.0,0.0);
      	//world.makePoint(new Vector(300.0,300.0),"bondedAtom",1.0,0.0,0.0);           
          //world.makeBall(new Vector(300.0,300.0), 100.0, "bondedAtom", 1.0, 0.0, 0.0);
      	//world.makeShape(new Vector(300.0,240.0), 20, 20, "bondedAtom", 1.0, new Vector(0.0,0.0));
      	/*
      	        	
      	vertices.add(new Vector(100.0,100.0));
      	vertices.add( new Vector(120.0,105.0));
      	vertices.add( new Vector(125.0,125.0));
      	vertices.add( new Vector(95.0,110.0));
      	 */
      	vertices.add(new Vector(100.0,100.0));
      	vertices.add( new Vector(200.0,200.0));
      	vertices.add( new Vector(300.0,100.0));
      	vertices.add( new Vector(400.0,300.0));
      	vertices.add( new Vector(200.0,400.0));
      	          	
      	        	
      	world.createShape(vertices, 1.0, new Vector(0.0,0.0));
      	          	
      	//Both chains are fairly dodgy.
      	//world.makeChain(new Vector(200.0,280.0), 10, 20.0, new Vector(0.0,0.0));
      	//world.makeRectangle(new Vector(150.0,300.0),1,10,"bondedAtom", 20.0, 20.0, 0.0);
      	        	
          //world.makeRectangle(new Vector(200.0,150.0),3,2,"bondedAtom",1.0,-800.0,0.0);
          //world.makeRectangle(new Vector(100.0,20.0),10,10,"bondedAtom",1.0,0.0,0.0);
         world.makeRectangle(new Vector(120.0,300.0),2,2,"bondedAtom",100.0,50.0,0.0);
                     
          }
	
      catch (OutsideWorldException e) 
          {
              System.out.println(e.getMessage());
          }        
      for (Integer i = 0; i < Constants.numSteps; i++)  {
              
      		//System.out.println(world.objects.get(0).velocity.y+(world.objects.get(1).velocity.y));
      		//System.out.println(i+": \n" + world.toString());
              canvas.clearOffscreen();
              world.draw(canvas);
              canvas.drawOffscreen();
              world.step();	            
              try {
                  Thread.sleep(000L);
                  }
              catch (InterruptedException e) {
              System.out.println("error" +e);
              }
              //-This is wasteful, if it's running too fast, just decrease the timeResolution.
                
        }
      
        
        //steps =stepsTotal;
        timer.start();        
        System.out.println("sim. complete");
    }	

		
	private void setbackground() {
		Graphics g = canvas.getBackgroundGraphics();
		g.setColor(Color.PINK);
		g.fillRect(0, 0, Constants.xsize, Constants.ysize);
	}

	public void actionPerformed(ActionEvent event) {
		/*if (event.getSource() == timer) {
			canvas.clearOffscreen();
			world.draw(canvas);
			canvas.draw();
			world.step();
			
//TODO get rid of Erik's steps, i also don't think the above actionPerformed method does anything.
			//steps--;
			//if (steps <= 0)
			//	timer.stop();			
		}*/
	}
	
	public static void main(String[] args) {						
		Simulation sim;
		System.out.println("Let the fun begin!");
		sim = new Simulation();		
		sim.runSimulation(); 		    
			
	}
}
