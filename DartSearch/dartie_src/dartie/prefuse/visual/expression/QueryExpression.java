package dartie.prefuse.visual.expression;

import dartie.prefuse.Visualization;
import dartie.prefuse.data.Schema;
import dartie.prefuse.data.Tuple;
import dartie.prefuse.data.search.SearchTupleSet;
import dartie.prefuse.visual.VisualItem;

/**
 * Expression that returns the current query string of a data group of the type
 * {@link dartie.prefuse.data.search.SearchTupleSet}. The data group name is provided
 * by a String-valued sub-expression.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class QueryExpression extends GroupExpression {

    /**
     * Create a new QueryExpression.
     */
    public QueryExpression() {
        super();
    }
    
    /**
     * Create a new QueryExpression.
     * @param group @param group the data group name to use as a parameter
     */
    public QueryExpression(String group) {
        super(group);
    }
    
    /**
     * @see dartie.prefuse.data.expression.Function#getName()
     */
    public String getName() {
        return "QUERY";
    }

    /**
     * @see dartie.prefuse.data.expression.Expression#getType(prefuse.data.Schema)
     */
    public Class getType(Schema s) {
        return String.class;
    }
    
    /**
     * @see dartie.prefuse.data.expression.Expression#get(prefuse.data.Tuple)
     */
    public Object get(Tuple t) {
        VisualItem item = (VisualItem)t;
        Visualization vis = item.getVisualization();
        String group = getGroup(t);
        SearchTupleSet sts = (SearchTupleSet)vis.getGroup(group);
        return sts.getQuery();
    }

} // end of class QueryExpression
