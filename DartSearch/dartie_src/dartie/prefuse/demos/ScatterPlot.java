package dartie.prefuse.demos;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import dartie.prefuse.Constants;
import dartie.prefuse.Display;
import dartie.prefuse.Visualization;
import dartie.prefuse.action.ActionList;
import dartie.prefuse.action.RepaintAction;
import dartie.prefuse.action.assignment.ColorAction;
import dartie.prefuse.action.assignment.DataShapeAction;
import dartie.prefuse.action.filter.VisibilityFilter;
import dartie.prefuse.action.layout.AxisLayout;
import dartie.prefuse.controls.ToolTipControl;
import dartie.prefuse.data.Table;
import dartie.prefuse.data.expression.AndPredicate;
import dartie.prefuse.data.io.DelimitedTextTableReader;
import dartie.prefuse.data.query.RangeQueryBinding;
import dartie.prefuse.render.DefaultRendererFactory;
import dartie.prefuse.render.ShapeRenderer;
import dartie.prefuse.util.ColorLib;
import dartie.prefuse.util.UpdateListener;
import dartie.prefuse.util.ui.JRangeSlider;
import dartie.prefuse.visual.VisualItem;
import dartie.prefuse.visual.VisualTable;
import dartie.prefuse.visual.expression.VisiblePredicate;


/**
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class ScatterPlot extends JPanel {

    private static final String group = "data";
    
    private Visualization m_vis;
    private Display m_display;
    private ShapeRenderer m_shapeR = new ShapeRenderer(2);
    
    public ScatterPlot(Table t, String xfield, String yfield) {
        this(t, xfield, yfield, null);
    }
    
    public ScatterPlot(Table t, String xfield, String yfield, String sfield) {
        super(new BorderLayout());
        
        // --------------------------------------------------------------------
        // STEP 1: setup the visualized data
        
        m_vis = new Visualization();
        VisualTable vt = m_vis.addTable(group, t);
        
        DefaultRendererFactory rf = new DefaultRendererFactory(m_shapeR);
        m_vis.setRendererFactory(rf);
        
        // --------------------------------------------------------------------
        // STEP 2: create actions to process the visual data

        // set up dynamic queries, search set
        RangeQueryBinding  xaxisQ = new RangeQueryBinding(vt, xfield);
        RangeQueryBinding  yaxisQ = new RangeQueryBinding(vt, yfield);
        
        // construct the filtering predicate
        AndPredicate filter = new AndPredicate(xaxisQ.getPredicate(),
                                               yaxisQ.getPredicate());
        
        // set up the actions
        AxisLayout x_axis = new AxisLayout(group, xfield, 
                Constants.X_AXIS, VisiblePredicate.TRUE);
        x_axis.setRangeModel(xaxisQ.getModel());
        
        AxisLayout y_axis = new AxisLayout(group, yfield, 
                Constants.Y_AXIS, VisiblePredicate.TRUE);
        y_axis.setRangeModel(yaxisQ.getModel());

        ColorAction color = new ColorAction(group, 
                VisualItem.STROKECOLOR, ColorLib.rgb(100,100,255));

        DataShapeAction shape = new DataShapeAction(group, sfield);
        
        ActionList draw = new ActionList();
        draw.add(x_axis);
        draw.add(y_axis);
        if ( sfield != null )
            draw.add(shape);
        draw.add(color);
        draw.add(new RepaintAction());
        m_vis.putAction("draw", draw);

        ActionList update = new ActionList();
        update.add(new VisibilityFilter(group, filter));
        update.add(x_axis);
        update.add(y_axis);
        update.add(new RepaintAction());
        m_vis.putAction("update", update);
        
        UpdateListener lstnr = new UpdateListener() {
            public void update(Object src) {
                m_vis.run("update");
            }
        };
        filter.addExpressionListener(lstnr);
        
        // --------------------------------------------------------------------
        // STEP 3: set up a display and ui components to show the visualization

        m_display = new Display(m_vis);
        m_display.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        m_display.setSize(700,450);
        m_display.setHighQuality(true);
        
        ToolTipControl ttc = new ToolTipControl(new String[] {xfield,yfield});
        m_display.addControlListener(ttc);
        
        
        // --------------------------------------------------------------------        
        // STEP 4: launching the visualization
        
        this.addComponentListener(lstnr);
        
        JRangeSlider xslider = xaxisQ.createHorizontalRangeSlider();
        JRangeSlider yslider = yaxisQ.createVerticalRangeSlider();
        
        xslider.setThumbColor(null);
        yslider.setThumbColor(null);
        
        MouseAdapter qualityControl = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                m_display.setHighQuality(false);
            }
            public void mouseReleased(MouseEvent e) {
                m_display.setHighQuality(true);
                m_display.repaint();
            }
        };
        xslider.addMouseListener(qualityControl);
        yslider.addMouseListener(qualityControl);
        
        m_vis.run("draw");
        
        add(m_display, BorderLayout.CENTER);
        add(yslider, BorderLayout.EAST);
        
        Box xbox = new Box(BoxLayout.X_AXIS);
        xbox.add(xslider);
        int corner = yslider.getPreferredSize().width;
        xbox.add(Box.createHorizontalStrut(corner));
        add(xbox, BorderLayout.SOUTH);
    }
    
    public int getPointSize() {
        return m_shapeR.getBaseSize();
    }
    
    public void setPointSize(int size) {
        m_shapeR.setBaseSize(size);
        repaint();
    }
    
    public Display getDisplay() {
        return m_display;
    }
    
    // ------------------------------------------------------------------------
    
    public static void main(String[] argv) {
        String data = "/fisher.iris.txt";
        String xfield = "SepalLength";
        String yfield = "PetalLength";
        String sfield = "Species";
        if ( argv.length >= 3 ) {
            data = argv[0];
            xfield = argv[1];
            yfield = argv[2];
            sfield = ( argv.length > 3 ? argv[3] : null );
        }
        JFrame frame = new JFrame("p r e f u s e  |  s c a t t e r");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(demo(data, xfield, yfield, sfield));
        frame.pack();
        frame.setVisible(true);
    }
    
    public static ScatterPlot demo(String data, String xfield, String yfield) {
        return demo(data, xfield, yfield, null);
    }
    
    public static ScatterPlot demo(String data, String xfield,
                                   String yfield, String sfield)
    {
        Table table = null;
        try {
            table = new DelimitedTextTableReader().readTable(data);
        } catch ( Exception e ) {
            e.printStackTrace();
            return null;
        }
        ScatterPlot scatter = new ScatterPlot(table, xfield, yfield, sfield);
        scatter.setPointSize(10);
        return scatter;
    }
    
} // end of class ScatterPlot
