package dartie.applets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.InputStream;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import dartie.prefuse.Display;
import dartie.prefuse.Visualization;
import dartie.prefuse.action.ActionList;
import dartie.prefuse.action.RepaintAction;
import dartie.prefuse.action.assignment.ColorAction;
import dartie.prefuse.action.filter.GraphDistanceFilter;
import dartie.prefuse.action.layout.graph.ForceDirectedLayout;
import dartie.prefuse.activity.Activity;
import dartie.prefuse.controls.DragControl;
import dartie.prefuse.controls.FocusControl;
import dartie.prefuse.controls.NeighborHighlightControl;
import dartie.prefuse.controls.PanControl;
import dartie.prefuse.controls.WheelZoomControl;
import dartie.prefuse.controls.ZoomControl;
import dartie.prefuse.controls.ZoomToFitControl;
import dartie.prefuse.data.Graph;
import dartie.prefuse.data.Tuple;
import dartie.prefuse.data.tuple.TupleSet; 

import dartie.prefuse.data.event.TupleSetListener;
import dartie.prefuse.data.io.GraphMLReader;
import dartie.prefuse.render.DefaultRendererFactory;
import dartie.prefuse.render.LabelRenderer;
import dartie.prefuse.util.ColorLib;
import dartie.prefuse.util.GraphLib;
import dartie.prefuse.util.PrefuseLib;
import dartie.prefuse.util.force.ForceSimulator;
import dartie.prefuse.util.ui.JForcePanel;
import dartie.prefuse.util.ui.JPrefuseApplet;
import dartie.prefuse.util.ui.JValueSlider;
import dartie.prefuse.util.ui.UILib;
import dartie.prefuse.visual.NodeItem;
import dartie.prefuse.visual.VisualGraph;
import dartie.prefuse.visual.VisualItem;

import dartie.application.Context;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class GraphView extends JPrefuseApplet {

    private static final String graph = "graph";
    private static final String nodes = "graph.nodes";
    private static final String edges = "graph.edges";
    
    public void init() {
        UILib.setPlatformLookAndFeel();
        String xml=getXML();
        System.out.println(xml);
        removeXML();
        JComponent graphview = demos("name",xml);
        this.getContentPane().add(graphview);
    }

    public String getXML(){
    	try{
        	URL url = new URL(getParameter("url")+"&cmd=GETXML");
        	System.out.println(url.toString());
        	URLConnection con = url.openConnection();
        	InputStream in = con.getInputStream();
        	InputStreamReader reader = new InputStreamReader(in,"UTF-8");
        	BufferedReader breader = new BufferedReader(reader);
        	String xml = breader.readLine();
        	breader.close();
        	reader.close();
        	in.close();
            return xml;
            }catch(Exception e){
            	e.printStackTrace();
            }
        return Context.getDefaultXML();
    }
    public void removeXML(){
    	try{
        	URL url = new URL(getParameter("url")+"&cmd=REMOVE");
        	System.out.println(url.toString());
        	URLConnection con = url.openConnection();
        	con.getInputStream().read();
        	con.getInputStream().close();
            }catch(Exception e){
            	e.printStackTrace();
            }
    }
    public JComponent demos(String label,String xml){
    	
    	Graph g=null;
    	try{
    		InputStream is  = new java.io.ByteArrayInputStream(xml.getBytes("utf-8"));
    	    g = new GraphMLReader().readGraph(is);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return demo(g,label);
    }    
    
    public JComponent demo(Graph g, String label) {

        // create a new, empty visualization for our data
        final Visualization vis = new Visualization();
        VisualGraph vg = vis.addGraph(graph, g);
        vis.setValue(edges, null, VisualItem.INTERACTIVE, Boolean.FALSE);
        
        TupleSet focusGroup = vis.getGroup(Visualization.FOCUS_ITEMS); 
        focusGroup.addTupleSetListener(new TupleSetListener() {
            public void tupleSetChanged(TupleSet ts, Tuple[] add, Tuple[] rem)
            {
                for ( int i=0; i<rem.length; ++i )
                    ((VisualItem)rem[i]).setFixed(false);
                for ( int i=0; i<add.length; ++i ) {
                    ((VisualItem)add[i]).setFixed(false);
                    ((VisualItem)add[i]).setFixed(true);
                }
                vis.run("draw");
            }
        });
        
        LabelRenderer tr = new LabelRenderer(label);
        tr.setRoundedCorner(8, 8);
        vis.setRendererFactory(new DefaultRendererFactory(tr));
        
        
        int maxhops = 4, hops = 4;
        final GraphDistanceFilter filter = new GraphDistanceFilter(graph, hops);

        ActionList draw = new ActionList();
        draw.add(filter);
        draw.add(new ColorAction(nodes, VisualItem.FILLCOLOR, ColorLib.rgb(200,200,255)));
        draw.add(new ColorAction(nodes, VisualItem.STROKECOLOR, 0));
        draw.add(new ColorAction(nodes, VisualItem.TEXTCOLOR, ColorLib.rgb(0,0,0)));
        draw.add(new ColorAction(edges, VisualItem.FILLCOLOR, ColorLib.gray(200)));
        draw.add(new ColorAction(edges, VisualItem.STROKECOLOR, ColorLib.gray(200)));
        
        ColorAction fill = new ColorAction(nodes, 
                VisualItem.FILLCOLOR, ColorLib.rgb(200,200,255));
        fill.add("_fixed", ColorLib.rgb(255,100,100));
        fill.add("_highlight", ColorLib.rgb(255,200,125));
        
        ForceDirectedLayout fdl = new ForceDirectedLayout(graph);
        ForceSimulator fsim = fdl.getForceSimulator();
        fsim.getForces()[0].setParameter(0, -1.2f);
        
        ActionList animate = new ActionList(Activity.INFINITY);
        animate.add(fdl);
        animate.add(fill);
        animate.add(new RepaintAction());
        
        vis.putAction("draw", draw);
        vis.putAction("layout", animate);
        vis.runAfter("draw", "layout");
        
        Display display = new Display(vis);
        display.setSize(500,500);
        display.setForeground(Color.GRAY);
        display.setBackground(Color.WHITE);
        
        // main display controls
        //display.addControlListener(new FocusControl(1));
        display.addControlListener(new DragControl());
        display.addControlListener(new PanControl());
        display.addControlListener(new ZoomControl());
        display.addControlListener(new WheelZoomControl());
        display.addControlListener(new ZoomToFitControl());
        display.addControlListener(new NeighborHighlightControl());
        
        display.setForeground(Color.BLUE);
        display.setBackground(Color.WHITE);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(display, BorderLayout.CENTER);
        
        // position and fix the default focus node
        NodeItem focus = (NodeItem)vg.getNode(0);
        PrefuseLib.setX(focus, null, 400);
        PrefuseLib.setY(focus, null, 250);
        focusGroup.setTuple(focus);

        return panel;
    }
    
} 