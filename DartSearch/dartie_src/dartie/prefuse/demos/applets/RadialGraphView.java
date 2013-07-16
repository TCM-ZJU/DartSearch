package dartie.prefuse.demos.applets;

import dartie.prefuse.util.ui.JPrefuseApplet;


public class RadialGraphView extends JPrefuseApplet {

    public void init() {
        this.setContentPane(
            dartie.prefuse.demos.RadialGraphView.demo("", "name", ""));
    }
    
} // end of class RadialGraphView
