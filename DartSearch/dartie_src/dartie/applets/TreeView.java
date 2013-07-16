package dartie.applets;

import dartie.prefuse.util.ui.JPrefuseApplet;
import netscape.javascript.JSObject;

/**
 * @author eric
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TreeView extends JPrefuseApplet{

    public void init() {
    	String treeFile = getParameter("treeFile");
    	JSObject jsObject = JSObject.getWindow(this);
    	String s = "as";
    	this.setContentPane(
            dartie.application.TreeView.GetTree(treeFile,jsObject));
    }
}
