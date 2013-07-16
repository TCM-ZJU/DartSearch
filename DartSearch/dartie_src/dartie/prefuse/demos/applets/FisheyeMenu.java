package dartie.prefuse.demos.applets;

import dartie.prefuse.util.ui.JPrefuseApplet;


public class FisheyeMenu extends JPrefuseApplet {
    
    public void init() {
        dartie.prefuse.demos.FisheyeMenu fm = dartie.prefuse.demos.FisheyeMenu.demo();
        this.getContentPane().add(fm);
        fm.getVisualization().run("init");
    }
    
} // end of class FisheyeMenu
