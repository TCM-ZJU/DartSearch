package dartie.application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Panel;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;

import dartie.ontology.IParser;
import dartie.ontology.ParserFactory;
import dartie.prefuse.Constants;
import dartie.prefuse.Display;
import dartie.prefuse.Visualization;
import dartie.prefuse.action.ActionList;
import dartie.prefuse.action.GroupAction;
import dartie.prefuse.action.ItemAction;
import dartie.prefuse.action.RepaintAction;
import dartie.prefuse.action.animate.ColorAnimator;
import dartie.prefuse.action.animate.PolarLocationAnimator;
import dartie.prefuse.action.animate.VisibilityAnimator;
import dartie.prefuse.action.assignment.ColorAction;
import dartie.prefuse.action.assignment.FontAction;
import dartie.prefuse.action.layout.CollapsedSubtreeLayout;
import dartie.prefuse.action.layout.graph.RadialTreeLayout;
import dartie.prefuse.activity.SlowInSlowOutPacer;
import dartie.prefuse.controls.ControlAdapter;
import dartie.prefuse.controls.DragControl;
import dartie.prefuse.controls.FocusControl;
import dartie.prefuse.controls.HoverActionControl;
import dartie.prefuse.controls.PanControl;
import dartie.prefuse.controls.WheelZoomControl;
import dartie.prefuse.controls.ZoomControl;
import dartie.prefuse.controls.ZoomToFitControl;
import dartie.prefuse.data.Graph;
import dartie.prefuse.data.Node;
import dartie.prefuse.data.Tuple;
import dartie.prefuse.data.event.TupleSetListener;
import dartie.prefuse.data.io.GraphMLReader;
import dartie.prefuse.data.search.KeywordSearchTupleSet;
import dartie.prefuse.data.search.SearchTupleSet;
import dartie.prefuse.data.tuple.DefaultTupleSet;
import dartie.prefuse.data.tuple.TupleSet;
import dartie.prefuse.render.AbstractShapeRenderer;
import dartie.prefuse.render.DefaultRendererFactory;
import dartie.prefuse.render.EdgeRenderer;
import dartie.prefuse.render.LabelRenderer;
import dartie.prefuse.util.ColorLib;
import dartie.prefuse.util.FontLib;
import dartie.prefuse.util.ui.JFastLabel;
import dartie.prefuse.util.ui.NewJSearchPanel;
import dartie.prefuse.util.ui.UILib;
import dartie.prefuse.visual.VisualItem;
import dartie.prefuse.visual.expression.InGroupPredicate;
import dartie.prefuse.visual.sort.TreeDepthItemSorter;




/**
 * Demonstration of a node-link tree viewer
 *
 * @version 1.0
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class RadialGraphView extends Display {

    private static final String tree = "tree";
    private static final String treeNodes = "tree.nodes";
    private static final String treeEdges = "tree.edges";
    private static final String linear = "linear";
    
    private LabelRenderer m_nodeRenderer;
    private EdgeRenderer m_edgeRenderer;
    
    private String m_label = "label";
    private FocusControl focusControl;
    
    public RadialGraphView(Graph g, String label) {
        super(new Visualization());
        m_label = label;

        // -- set up visualization --
        m_vis.add(tree, g);
        m_vis.setInteractive(treeEdges, null, false);
        
        // -- set up renderers --
        m_nodeRenderer = new LabelRenderer(m_label);
        m_nodeRenderer.setRenderType(AbstractShapeRenderer.RENDER_TYPE_FILL);
        m_nodeRenderer.setHorizontalAlignment(Constants.CENTER);
        m_nodeRenderer.setRoundedCorner(8,8);
        m_edgeRenderer = new EdgeRenderer();
        
        DefaultRendererFactory rf = new DefaultRendererFactory(m_nodeRenderer);
        rf.add(new InGroupPredicate(treeEdges), m_edgeRenderer);
        m_vis.setRendererFactory(rf);
               
        // -- set up processing actions --
        
        // colors
        ItemAction nodeColor = new NodeColorAction(treeNodes);
        ItemAction textColor = new TextColorAction(treeNodes);
        m_vis.putAction("textColor", textColor);
        
        ItemAction edgeColor = new ColorAction(treeEdges, VisualItem.STROKECOLOR, ColorLib.rgb(200,200,200));
        
        FontAction fonts = new FontAction(treeNodes, FontLib.getFont("simsum", 12));
        fonts.add("ingroup('_focus_')", FontLib.getFont("simsum", 12));
        
        // recolor
        ActionList recolor = new ActionList();
        recolor.add(nodeColor);
        recolor.add(textColor);
        m_vis.putAction("recolor", recolor);
        
        // repaint
        ActionList repaint = new ActionList();
        repaint.add(recolor);
        repaint.add(new RepaintAction());
        m_vis.putAction("repaint", repaint);
        
        // animate paint change
        ActionList animatePaint = new ActionList(400);
        animatePaint.add(new ColorAnimator(treeNodes));
        animatePaint.add(new RepaintAction());
        m_vis.putAction("animatePaint", animatePaint);
        
        // create the tree layout action
        RadialTreeLayout treeLayout = new RadialTreeLayout(tree);
        //treeLayout.setAngularBounds(-Math.PI/2, Math.PI);
        m_vis.putAction("treeLayout", treeLayout);
        
        CollapsedSubtreeLayout subLayout = new CollapsedSubtreeLayout(tree);
        m_vis.putAction("subLayout", subLayout);
        
        // create the filtering and layout
        ActionList filter = new ActionList();
        filter.add(new TreeRootAction(tree));
        filter.add(fonts);
        filter.add(treeLayout);
        filter.add(subLayout);
        filter.add(textColor);
        filter.add(nodeColor);
        filter.add(edgeColor);
        m_vis.putAction("filter", filter);
        
        // animated transition
        ActionList animate = new ActionList(1250);
        animate.setPacingFunction(new SlowInSlowOutPacer());
        // animate.add(new QualityControlAnimator());
        animate.add(new VisibilityAnimator(tree));
        animate.add(new PolarLocationAnimator(treeNodes, linear));
        animate.add(new ColorAnimator(treeNodes));
        animate.add(new RepaintAction());
        m_vis.putAction("animate", animate);
        m_vis.alwaysRunAfter("filter", "animate");
        
        // ------------------------------------------------
        
        // initialize the display
        setSize(500,500);
        setItemSorter(new TreeDepthItemSorter());
        addControlListener(new DragControl());
        addControlListener(new ZoomToFitControl());
        addControlListener(new ZoomControl());
        addControlListener(new PanControl());
        focusControl = new FocusControl(1, "filter");
        addControlListener(focusControl);
        addControlListener(new HoverActionControl("repaint"));
        addControlListener(new WheelZoomControl());
        
        // ------------------------------------------------
        
        // filter graph and perform layout
        m_vis.run("filter");
        
        // maintain a set of items that should be interpolated linearly
        // this isn't absolutely necessary, but makes the animations nicer
        // the PolarLocationAnimator should read this set and act accordingly
        m_vis.addFocusGroup(linear, new DefaultTupleSet());
        m_vis.getGroup(Visualization.FOCUS_ITEMS).addTupleSetListener(
            new TupleSetListener() {
                public void tupleSetChanged(TupleSet t, Tuple[] add, Tuple[] rem) {
                    TupleSet linearInterp = m_vis.getGroup(linear);
                    if ( add.length < 1 ) return; linearInterp.clear();
                    for ( Node n = (Node)add[0]; n!=null; n=n.getParent() )
                        linearInterp.addTuple(n);
                }
            }
        );
        
        SearchTupleSet search = new KeywordSearchTupleSet();
        m_vis.addFocusGroup(Visualization.SEARCH_ITEMS, search);
        search.addTupleSetListener(new TupleSetListener() {
            public void tupleSetChanged(TupleSet t, Tuple[] add, Tuple[] rem) {
                m_vis.cancel("animatePaint");
                m_vis.run("recolor");
                m_vis.run("animatePaint");
            }
        });
    }
    
    // ------------------------------------------------------------------------
    
    public static void main(String argv[]) {
              
    }
    
    public static JFrame GetGraph(String uri) {        
    	UILib.setPlatformLookAndFeel();
        Graph g = null;
        try {
        	IParser parser = ParserFactory.createParser(ParserFactory.getInputRdfFile(), uri);
			g = new GraphMLReader().readGraph(parser.getPropertyStream());
        } catch ( Exception e ) {
            e.printStackTrace();
            System.exit(1);
        }
    	final String label = "name";
    	// create a new radial tree view
        final RadialGraphView gview = new RadialGraphView(g, label);
        Visualization vis = gview.getVisualization();
        
        Panel fpanel = new Panel();
        // create a search panel for the tree map
        NewJSearchPanel search = new NewJSearchPanel(vis,
                treeNodes, Visualization.SEARCH_ITEMS, label, true, true);
        search.setFocusControl(gview.focusControl);
        search.setShowResultCount(true);
        search.setBorder(BorderFactory.createEmptyBorder(5,5,4,0));
        search.setFont(FontLib.getFont("simsum", Font.PLAIN, 12));
        Box cf1 = new Box(BoxLayout.Y_AXIS);
        cf1.add(search);
        cf1.setBorder(BorderFactory.createTitledBorder("Search"));
        fpanel.add(cf1);
        
        final JFastLabel title = new JFastLabel("                 ");
        title.setPreferredSize(new Dimension(350, 20));
        title.setVerticalAlignment(SwingConstants.BOTTOM);
        title.setBorder(BorderFactory.createEmptyBorder(3,0,0,0));
        title.setFont(FontLib.getFont("simsum", Font.PLAIN, 12));
        
        gview.addControlListener(new ControlAdapter() {
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
        panel.add(gview, BorderLayout.CENTER);
        panel.add(box, BorderLayout.SOUTH);
        
        
        Color BACKGROUND = Color.WHITE;
        Color FOREGROUND = Color.DARK_GRAY;
        UILib.setColor(panel, BACKGROUND, FOREGROUND);
        
//      create a new JSplitPane to present the interface
        JSplitPane split = new JSplitPane();
        split.setLeftComponent(panel);
        split.setRightComponent(fpanel);
        split.setOneTouchExpandable(true);
        split.setContinuousLayout(false);
        split.setDividerLocation(500);
        split.setDividerLocation(800);
        
        //      launch window
        JFrame frame = new JFrame("Ontology Explorer");
        frame.setResizable(false);
        frame.setContentPane(split);
        frame.pack();
        frame.setVisible(true);
        return frame;
    }
    
    // ------------------------------------------------------------------------
    
    /**
     * Switch the root of the tree by requesting a new spanning tree
     * at the desired root
     */
    public static class TreeRootAction extends GroupAction {
        public TreeRootAction(String graphGroup) {
            super(graphGroup);
        }
        public void run(double frac) {
            TupleSet focus = m_vis.getGroup(Visualization.FOCUS_ITEMS);
            if ( focus==null || focus.getTupleCount() == 0 ) return;
            
            Graph g = (Graph)m_vis.getGroup(m_group);
            Node f = null;
            while ( !g.containsTuple(f=(Node)focus.tuples().next()) ) {
                f = null;
            }
            if ( f == null ) return;
            g.getSpanningTree(f);
        }
    }
    
    /**
     * Set node fill colors
     */
    public static class NodeColorAction extends ColorAction {
        public NodeColorAction(String group) {
            super(group, VisualItem.FILLCOLOR, ColorLib.rgba(255,255,255,0));
            add("_hover", ColorLib.gray(220,230));
            add("ingroup('_search_')", ColorLib.rgb(255,190,190));
            add("ingroup('_focus_')", ColorLib.rgb(198,229,229));
        }
                
    } // end of inner class NodeColorAction
    
    /**
     * Set node text colors
     */
    public static class TextColorAction extends ColorAction {
        public TextColorAction(String group) {
            super(group, VisualItem.TEXTCOLOR, ColorLib.gray(0));
            add("_hover", ColorLib.rgb(255,0,0));
        }
    } // end of inner class TextColorAction
    
    
} // end of class RadialGraphView
