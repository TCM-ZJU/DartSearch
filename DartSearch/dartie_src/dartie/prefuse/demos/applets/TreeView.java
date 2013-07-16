package dartie.prefuse.demos.applets;

import dartie.prefuse.util.ui.JPrefuseApplet;


public class TreeView extends JPrefuseApplet {

    public void init() {
    	this.setContentPane(
            dartie.prefuse.demos.TreeView.demo("name"));
    }
    
} // end of class TreeView
