package dartie.application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import dartie.ontology.IParser;
import dartie.ontology.ParserFactory;
import dartie.prefuse.Display;
import dartie.prefuse.Visualization;
import dartie.prefuse.action.ActionList;
import dartie.prefuse.action.RepaintAction;
import dartie.prefuse.action.assignment.ColorAction;
import dartie.prefuse.action.assignment.FontAction;
import dartie.prefuse.action.filter.GraphDistanceFilter;
import dartie.prefuse.action.layout.graph.ForceDirectedLayout;
import dartie.prefuse.activity.Activity;
import dartie.prefuse.controls.ControlAdapter;
import dartie.prefuse.controls.DragControl;
import dartie.prefuse.controls.FocusControl;
import dartie.prefuse.controls.NeighborHighlightControl;
import dartie.prefuse.controls.PanControl;
import dartie.prefuse.controls.WheelZoomControl;
import dartie.prefuse.controls.ZoomControl;
import dartie.prefuse.controls.ZoomToFitControl;
import dartie.prefuse.data.Graph;
import dartie.prefuse.data.Tuple;
import dartie.prefuse.data.event.TupleSetListener;
import dartie.prefuse.data.io.DataIOException;
import dartie.prefuse.data.io.GraphMLReader;
import dartie.prefuse.data.search.KeywordSearchTupleSet;
import dartie.prefuse.data.tuple.TupleSet;
import dartie.prefuse.render.DefaultRendererFactory;
import dartie.prefuse.render.LabelRenderer;
import dartie.prefuse.util.ColorLib;
import dartie.prefuse.util.FontLib;
import dartie.prefuse.util.GraphicsLib;
import dartie.prefuse.util.PrefuseLib;
import dartie.prefuse.util.display.DisplayLib;
import dartie.prefuse.util.display.ItemBoundsListener;
import dartie.prefuse.util.ui.JFastLabel;
import dartie.prefuse.util.ui.NewJSearchPanel;
import dartie.prefuse.util.ui.UILib;
import dartie.prefuse.visual.VisualGraph;
import dartie.prefuse.visual.VisualItem;



/**
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class GraphView extends Display {

    private static final String graph = "graph";
    private static final String nodes = "graph.nodes";
    private static final String edges = "graph.edges";

    private FocusControl focusControl;
    
    public GraphView(Graph g, String label, String uri) {
        
    	super(new Visualization());
        // create a new, empty visualization for our data
        // -- set up visualization --
        m_vis.add(graph, g); 
        // --------------------------------------------------------------------
        // set up the renderers
        
        LabelRenderer tr = new LabelRenderer();
        tr.setRoundedCorner(8, 8);
        m_vis.setRendererFactory(new DefaultRendererFactory(tr));

        // --------------------------------------------------------------------
        // register the data with a visualization
        
        // adds graph to visualization and sets renderer label field
        setGraph(g, label);
        
        // fix selected focus nodes
        TupleSet focusGroup = m_vis.getGroup(Visualization.FOCUS_ITEMS); 
        focusGroup.addTupleSetListener(new TupleSetListener() {
            public void tupleSetChanged(TupleSet ts, Tuple[] add, Tuple[] rem)
            {
                for ( int i=0; i<rem.length; ++i )
                    ((VisualItem)rem[i]).setFixed(false);
                for ( int i=0; i<add.length; ++i ) {
                    ((VisualItem)add[i]).setFixed(false);
                    ((VisualItem)add[i]).setFixed(true);
                }
                m_vis.run("draw");
            }
        });
        
        TupleSet search = new KeywordSearchTupleSet(); 
        m_vis.addFocusGroup(Visualization.SEARCH_ITEMS, search);
        search.addTupleSetListener(new TupleSetListener() {
            public void tupleSetChanged(TupleSet t, Tuple[] add, Tuple[] rem) {
                m_vis.cancel("animatePaint");
                m_vis.run("fullPaint");
                m_vis.run("animatePaint");
            }
        });
        
        
        
        // --------------------------------------------------------------------
        // create actions to process the visual data

        int hops = 30;
        final GraphDistanceFilter filter = new GraphDistanceFilter(graph, hops);
        filter.setDistance(400);
        
        ActionList draw = new ActionList();
        draw.add(filter);
        draw.add(new ColorAction(nodes, VisualItem.FILLCOLOR, ColorLib.rgb(200,200,255)));
        draw.add(new ColorAction(nodes, VisualItem.STROKECOLOR, 0));
        draw.add(new ColorAction(nodes, VisualItem.TEXTCOLOR, ColorLib.rgb(0,0,0)));
        draw.add(new ColorAction(edges, VisualItem.FILLCOLOR, ColorLib.gray(200)));
        draw.add(new ColorAction(edges, VisualItem.STROKECOLOR, ColorLib.gray(200)));
        draw.add(new FontAction(nodes, FontLib.getFont("simsun", 12)));
        
        ColorAction fill = new ColorAction(nodes, 
                VisualItem.FILLCOLOR, ColorLib.rgb(200,200,255));
        fill.add(VisualItem.FIXED, ColorLib.rgb(255,100,100));
        fill.add(VisualItem.HIGHLIGHT, ColorLib.rgb(255,200,125));
        
        ActionList animate = new ActionList(Activity.INFINITY);
        animate.add(new ForceDirectedLayout(graph));
        animate.add(fill);
        animate.add(new RepaintAction());
        
        // finally, we register our ActionList with the Visualization.
        // we can later execute our Actions by invoking a method on our
        // Visualization, using the name we've chosen below.
        m_vis.putAction("draw", draw);
        m_vis.putAction("layout", animate);

        m_vis.runAfter("draw", "layout");
        
        
        // --------------------------------------------------------------------
        // set up a display to show the visualization

        setSize(500,500);
        setForeground(Color.GRAY);
        setBackground(Color.WHITE);
        
        // main display controls
        focusControl = new FocusControl(1);
        addControlListener(focusControl);
        addControlListener(new DragControl());
        addControlListener(new PanControl());
        addControlListener(new ZoomControl());
        addControlListener(new WheelZoomControl());
        addControlListener(new ZoomToFitControl());
        addControlListener(new NeighborHighlightControl());

        // overview display
//        Display overview = new Display(vis);
//        overview.setSize(290,290);
//        overview.addItemBoundsListener(new FitOverviewListener());
        
        setForeground(Color.GRAY);
        setBackground(Color.WHITE);
                
        // now we run our action list
        m_vis.run("draw");
    }
    
    public void setGraph(Graph g, String label) {
        // update labeling
        DefaultRendererFactory drf = (DefaultRendererFactory)
                                                m_vis.getRendererFactory();
        ((LabelRenderer)drf.getDefaultRenderer()).setTextField(label);
        
        // update graph
        m_vis.removeGroup(graph);
        VisualGraph vg = m_vis.addGraph(graph, g);
        m_vis.setValue(edges, null, VisualItem.INTERACTIVE, Boolean.FALSE);
        VisualItem f = (VisualItem)vg.getNode(0);
        m_vis.getGroup(Visualization.FOCUS_ITEMS).setTuple(f);
        f.setFixed(true);        
        PrefuseLib.setX(f, null, 400);
        PrefuseLib.setY(f, null, 250);

    }
    
    // ------------------------------------------------------------------------
    // Main and demo methods
    
    public static void main(String[] args) {
        //UILib.setPlatformLookAndFeel();
        
        //JFrame frame = GetGraph();
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public static JFrame GetGraph(String uri) {
    	UILib.setPlatformLookAndFeel();
    	final String label = "name";
    	Graph g = null;
		try {
			String inputFile = ParserFactory.getInputRdfFile();
			IParser parser = ParserFactory.createParser(inputFile, uri);
			g = new GraphMLReader().readGraph(parser.getClassStream());
			
		} catch (DataIOException e1) {
			e1.printStackTrace();
		}
        GraphView view = new GraphView(g, label, uri);
        Visualization vis = view.getVisualization();
        // --------------------------------------------------------------------        

        final JFastLabel title = new JFastLabel("                 ");
        title.setPreferredSize(new Dimension(350, 20));
        title.setVerticalAlignment(SwingConstants.BOTTOM);
        title.setBorder(BorderFactory.createEmptyBorder(3,0,0,0));
        title.setFont(FontLib.getFont("simsum", Font.PLAIN, 12));
        
        view.addControlListener(new ControlAdapter() {
            public void itemEntered(VisualItem item, MouseEvent e) {
                if ( item.canGetString(label) )
                    title.setText(item.getString(label));
            }
            public void itemExited(VisualItem item, MouseEvent e) {
                title.setText(null);
            }
        });
        
        Box box = new Box(BoxLayout.X_AXIS);
        box.add(Box.createHorizontalStrut(10));
        box.add(title);
        box.add(Box.createHorizontalGlue());
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(view, BorderLayout.CENTER);
        panel.add(box, BorderLayout.SOUTH);
        
        Color BACKGROUND = Color.WHITE;
        Color FOREGROUND = Color.DARK_GRAY;
        UILib.setColor(panel, BACKGROUND, FOREGROUND);
        
        Panel fpanel = new Panel();
        // create a search panel for the tree map
        NewJSearchPanel searchPanel = new NewJSearchPanel(vis,
        		nodes, Visualization.SEARCH_ITEMS, label, true, true);
        searchPanel.setShowResultCount(true);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(3,0,0,0));
        searchPanel.setFont(FontLib.getFont("simsun", Font.PLAIN, 12));
        searchPanel.setFocusControl(view.focusControl);
        Box cf1 = new Box(BoxLayout.Y_AXIS);
        cf1.add(searchPanel);
        cf1.setBorder(BorderFactory.createTitledBorder("Search"));
        fpanel.add(cf1);        
        fpanel.add(Box.createVerticalGlue());
        
        // create a new JSplitPane to present the interface
        JSplitPane split = new JSplitPane();
        split.setLeftComponent(panel);
        split.setRightComponent(fpanel);
        split.setOneTouchExpandable(true);
        split.setContinuousLayout(false);
        split.setDividerLocation(500);
        split.setDividerLocation(800);
        // launch window
        JFrame frame = new JFrame("Ontology Explorer");
        frame.setResizable(false);
        frame.setContentPane(split);
        frame.pack();
        frame.setVisible(true);
        
        return frame;
    }
    
    
    // ------------------------------------------------------------------------
    
    /**
     * Swing menu action that loads a graph into the graph viewer.
     */
    public abstract static class GraphMenuAction extends AbstractAction {
        private GraphView m_view;
        public GraphMenuAction(String name, String accel, GraphView view) {
            m_view = view;
            this.putValue(AbstractAction.NAME, name);
            this.putValue(AbstractAction.ACCELERATOR_KEY,
                          KeyStroke.getKeyStroke(accel));
        }
        public void actionPerformed(ActionEvent e) {
            m_view.setGraph(getGraph(), "label");
        }
        protected abstract Graph getGraph();
    }
    
    public static class FitOverviewListener implements ItemBoundsListener {
        private Rectangle2D m_bounds = new Rectangle2D.Double();
        private Rectangle2D m_temp = new Rectangle2D.Double();
        private double m_d = 15;
        public void itemBoundsChanged(Display d) {
            d.getItemBounds(m_temp);
            GraphicsLib.expand(m_temp, 25/d.getScale());
            
            double dd = m_d/d.getScale();
            double xd = Math.abs(m_temp.getMinX()-m_bounds.getMinX());
            double yd = Math.abs(m_temp.getMinY()-m_bounds.getMinY());
            double wd = Math.abs(m_temp.getWidth()-m_bounds.getWidth());
            double hd = Math.abs(m_temp.getHeight()-m_bounds.getHeight());
            if ( xd>dd || yd>dd || wd>dd || hd>dd ) {
                m_bounds.setFrame(m_temp);
                DisplayLib.fitViewToBounds(d, m_bounds, 0);
            }
        }
    }
    
    
    public static JFrame GetGraphXML(String file,String uri) {
    	UILib.setPlatformLookAndFeel();
    	final String label = "name";
    	Graph g = null;
		try {
			
			g = new GraphMLReader().readGraph(file);
			
		} catch (DataIOException e1) {
			e1.printStackTrace();
		}
        GraphView view = new GraphView(g, label, uri);
        Visualization vis = view.getVisualization();
        // --------------------------------------------------------------------        

        final JFastLabel title = new JFastLabel("                 ");
        title.setPreferredSize(new Dimension(350, 20));
        title.setVerticalAlignment(SwingConstants.BOTTOM);
        title.setBorder(BorderFactory.createEmptyBorder(3,0,0,0));
        title.setFont(FontLib.getFont("simsum", Font.PLAIN, 12));
        
        view.addControlListener(new ControlAdapter() {
            public void itemEntered(VisualItem item, MouseEvent e) {
                if ( item.canGetString(label) )
                    title.setText(item.getString(label));
            }
            public void itemExited(VisualItem item, MouseEvent e) {
                title.setText(null);
            }
        });
        
        Box box = new Box(BoxLayout.X_AXIS);
        box.add(Box.createHorizontalStrut(10));
        box.add(title);
        box.add(Box.createHorizontalGlue());
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(view, BorderLayout.CENTER);
        panel.add(box, BorderLayout.SOUTH);
        
        Color BACKGROUND = Color.WHITE;
        Color FOREGROUND = Color.DARK_GRAY;
        UILib.setColor(panel, BACKGROUND, FOREGROUND);
        
        Panel fpanel = new Panel();
        // create a search panel for the tree map
        NewJSearchPanel searchPanel = new NewJSearchPanel(vis,
        		nodes, Visualization.SEARCH_ITEMS, label, true, true);
        searchPanel.setShowResultCount(true);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(3,0,0,0));
        searchPanel.setFont(FontLib.getFont("simsun", Font.PLAIN, 12));
        searchPanel.setFocusControl(view.focusControl);
        Box cf1 = new Box(BoxLayout.Y_AXIS);
        cf1.add(searchPanel);
        cf1.setBorder(BorderFactory.createTitledBorder("Search"));
        fpanel.add(cf1);        
        fpanel.add(Box.createVerticalGlue());
        
        // create a new JSplitPane to present the interface
        JSplitPane split = new JSplitPane();
        split.setLeftComponent(panel);
        split.setRightComponent(fpanel);
        split.setOneTouchExpandable(true);
        split.setContinuousLayout(false);
        split.setDividerLocation(500);
        split.setDividerLocation(800);
        // launch window
        JFrame frame = new JFrame("Ontology Explorer");
        frame.setResizable(false);
        frame.setContentPane(split);
        frame.pack();
        frame.setVisible(true);
        
        return frame;
    }    
    
} // end of class GraphView
