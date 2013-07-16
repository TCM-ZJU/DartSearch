package dartie.prefuse.demos.applets;

import dartie.prefuse.util.ui.JPrefuseApplet;


public class TreeMap extends JPrefuseApplet {

    public void init() {
        this.setContentPane(
            dartie.prefuse.demos.TreeMap.demo("/chi-ontology.xml.gz", "name"));
    }
    
} // end of class TreeMap
