package dartie.prefuse.visual.tuple;

import dartie.prefuse.data.Graph;
import dartie.prefuse.data.Node;
import dartie.prefuse.data.Table;
import dartie.prefuse.visual.EdgeItem;
import dartie.prefuse.visual.NodeItem;

/**
 * EdgeItem implementation that used data values from a backing
 * VisualTable of edges.
 *  
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class TableEdgeItem extends TableVisualItem implements EdgeItem {

    protected Graph m_graph;
    
    /**
     * Initialize a new TableEdgeItem for the given graph, table, and row.
     * This method is used by the appropriate TupleManager instance, and
     * should not be called directly by client code, unless by a
     * client-supplied custom TupleManager.
     * @param table the backing VisualTable
     * @param graph the backing VisualGraph
     * @param row the row in the node table to which this Edge instance
     *  corresponds.
     */
    protected void init(Table table, Graph graph, int row) {
        m_table = table;
        m_graph = graph;
        m_row = m_table.isValidRow(row) ? row : -1;
    }
    
    /**
     * @see dartie.prefuse.data.Edge#getGraph()
     */
    public Graph getGraph() {
        return m_graph;
    }
    
    /**
     * @see dartie.prefuse.data.Edge#isDirected()
     */
    public boolean isDirected() {
        return m_graph.isDirected();
    }

    /**
     * @see dartie.prefuse.data.Edge#getSourceNode()
     */
    public Node getSourceNode() {
        return m_graph.getSourceNode(this);
    }

    /**
     * @see dartie.prefuse.data.Edge#getTargetNode()
     */
    public Node getTargetNode() {
        return m_graph.getTargetNode(this);
    }

    /**
     * @see dartie.prefuse.data.Edge#getAdjacentNode(prefuse.data.Node)
     */
    public Node getAdjacentNode(Node n) {
        return m_graph.getAdjacentNode(this, n);
    }
    
    /**
     * @see dartie.prefuse.visual.EdgeItem#getSourceItem()
     */
    public NodeItem getSourceItem() {
        return (NodeItem)getSourceNode();
    }

    /**
     * @see dartie.prefuse.visual.EdgeItem#getTargetItem()
     */
    public NodeItem getTargetItem() {
        return (NodeItem)getTargetNode();
    }

    /**
     * @see dartie.prefuse.visual.EdgeItem#getAdjacentItem(prefuse.visual.NodeItem)
     */
    public NodeItem getAdjacentItem(NodeItem n) {
        return (NodeItem)getAdjacentNode(n);
    }

} // end of class TableEdgeItem
