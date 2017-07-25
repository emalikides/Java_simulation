/**
 * 
 */
package main;

import java.util.ArrayList;
import java.util.List;

/**
 * @author u4685222
 *
 */
public class QuadTree {
	
	Integer xStart, xFin, yStart, yFin, depth;
	ArrayList<QuadTree> subtrees = new ArrayList<QuadTree>();
	ArrayList<Matter> elements = new ArrayList<Matter>();
	QuadTree parent = null;

	public QuadTree(Integer xStart, Integer xFin, Integer yStart, Integer yFin, Integer depth) {
		this.xStart = xStart;
		this.xFin = xFin;
		this.yStart = yStart;
		this.yFin = yFin;
		this.depth = depth;
	}
	
	public QuadTree(Integer xStart, Integer xFin, Integer yStart, Integer yFin, Integer depth, QuadTree parent) {
		this.xStart = xStart;
		this.xFin = xFin;
		this.yStart = yStart;
		this.yFin = yFin;
		this.depth = depth;
		this.parent = parent;
	}
	
	public ArrayList<Matter> possibleOverlaps(Matter m) {
		ArrayList<Matter> possible = new ArrayList<Matter>();
		if(depth > 0) {
			if(this.withinBounds(m)) {
				for(QuadTree sub : subtrees) {
					possible.addAll(sub.possibleOverlaps(m));
				}
				if (possible.isEmpty()) {
					possible.addAll(elements);
				}
			}
		} else {
			possible.addAll(elements);
		}
		
		return possible;
	}
	
	private QuadTree makeSubTree(Double x, Double y) {
		Integer newXFin, newYFin, newXStart, newYStart; 
		if (x < xFin/2) { 
			newXStart = xStart;
			newXFin = xFin/2;
		} else {
			newXStart = xStart + xFin/2;
			newXFin = xFin;
		}
		
		if (y < yFin/2) { 
			newYStart = yStart;
			newYFin = yFin/2;
		} else {
			newYStart = yStart + yFin/2;
			newYFin = yFin;
		}
		return new QuadTree(newXStart,newXFin,newYStart,newYFin,depth-1,this);
	}
	
	public void add(Matter m) {
		if(depth > 0) {
			QuadTree subtree = makeSubTree(m.position.x, m.position.y);
			if(subtree.withinBounds(m)) {
				subtrees.add(subtree);
				subtree.add(m);
			}
			elements.add(m);
		}
	}
	
	private Boolean withinBounds(Matter m) {
		return (m.position.x + m.radius < xFin
				&& m.position.x - m.radius >= xStart
				&& m.position.y + m.radius < yFin
				&& m.position.y - m.radius >= yStart);
	}
	
	public void add(List<Matter> l) {
		for (Matter matter : l) {
			add(matter);
		}
	}
	
}
