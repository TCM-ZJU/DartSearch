package dartie.prefuse.demos.applets;

import dartie.prefuse.data.Table;
import dartie.prefuse.data.io.DataIOException;
import dartie.prefuse.data.io.DelimitedTextTableReader;
import dartie.prefuse.util.ui.JPrefuseApplet;


public class ZipDecode extends JPrefuseApplet {

    public void init() {
        DelimitedTextTableReader tr = new DelimitedTextTableReader();
        Table t = null;
        try {
            t = tr.readTable("/zipcode.txt");        
        } catch ( DataIOException e ) {
            e.printStackTrace();
            System.exit(1);
        }
        this.setContentPane(new dartie.prefuse.demos.ZipDecode(t));
    }
    
} // end of class ZipDecode
