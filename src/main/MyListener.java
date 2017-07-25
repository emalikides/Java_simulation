/**
 * @author Michael Asher u4666424
 * August 2009
 * Comp1110 Assignment
 */
package main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Component;
import java.awt.Point;

public class MyListener implements MouseListener, MouseMotionListener {
	
	private Simulation simulation;
	public static boolean simStarted;
	
	private int maxVertices = 5;
	private Double range = 10.0;//radius of circle around first vertex, clicking in which will terminate the polygon
	Vector first;
	
	public MyListener(Component comp,Simulation sim){
		simStarted = false;
		simulation = sim;
		comp.addMouseListener(this);
		comp.addMouseMotionListener(this);
	}
	
	public void mouseReleased(MouseEvent e) {
		
	}

	public void mouseMoved(MouseEvent e) {
		
	}
//TODO ensure at least 3 vertices are drawn
	public void mouseClicked(MouseEvent e) {
		if(!simStarted){
			Double x = (double) e.getX();
			Double y = (double) e.getY();
			
			System.out.println(x+","+y);
			Vector point = new Vector(x,y);
			System.out.println(Simulation.vertices.size());
			if(Simulation.vertices.isEmpty()){
				first = new Vector(x,y);
				Simulation.vertices.add(first);
			}	
			
			else if (Simulation.vertices.size() < maxVertices){				
				if (Math.abs(point.vectorTo(first).length())>range){
					Simulation.vertices.add(point);
				}
				else simStarted=true;
			}
			else simStarted=true;
		}		
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {
		
	}
	
	public void mouseDragged(MouseEvent e) {
		
	}
}
