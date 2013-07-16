package dartie.prefuse.demos.applets;

import dartie.prefuse.util.ui.JPrefuseApplet;

/**
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class GraphView extends JPrefuseApplet {

	 public void init() {
	    	this.setContentPane(
	            dartie.prefuse.demos.GraphView.demo("name"));
	 }
    
} // end of class GraphView
