package dartie.prefuse.visual.expression;

import dartie.prefuse.Visualization;
import dartie.prefuse.data.Tuple;
import dartie.prefuse.data.expression.BooleanLiteral;
import dartie.prefuse.data.expression.Expression;
import dartie.prefuse.data.search.SearchTupleSet;
import dartie.prefuse.visual.VisualItem;

/**
 * Expression that indicates if an item is currently a member of a data group of the type
 * {@link dartie.prefuse.data.search.SearchTupleSet}, but including a possible special case in
 * which all items should be pass through the predicate if no search query is specified.
 * The data group name is provided by a String-valued sub-expression.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class SearchPredicate extends InGroupPredicate {

    private Expression m_incEmpty;
    private int paramCount = 0;
    
    /**
     * Create a new SearchPredicate. By default, looks into the
     * {@link dartie.prefuse.Visualization#ALL_ITEMS} data group and assumes all items
     * should pass the predicate if no search query has been specified.
     */
    public SearchPredicate() {
        this(Visualization.SEARCH_ITEMS, true);
        paramCount = 0;
    }
    
    /**
     * Create a new SearchPredicate. By default, looks into the
     * {@link dartie.prefuse.Visualization#ALL_ITEMS} data group.
     * @param includeAllByDefault indicates if all items
     * should pass the predicate if no search query has been specified.
     */
    public SearchPredicate(boolean includeAllByDefault) {
        this(Visualization.SEARCH_ITEMS, includeAllByDefault);
    }
    
    /**
     * Create a new SearchPredicate.
     * @param group the data group to look up, should resolve to a
     * {@link dartie.prefuse.data.search.SearchTupleSet} instance.
     * @param includeAllByDefault indicates if all items
     * should pass the predicate if no search query has been specified.
     */
    public SearchPredicate(String group, boolean includeAllByDefault) {
        super(group);
        m_incEmpty = new BooleanLiteral(includeAllByDefault);
        paramCount = 2;
    }
    
    /**
     * @see dartie.prefuse.data.expression.Expression#getBoolean(prefuse.data.Tuple)
     */
    public boolean getBoolean(Tuple t) {
        String group = getGroup(t);
        if ( group == null ) return false;
        boolean incEmpty = m_incEmpty.getBoolean(t);
        
        VisualItem item = (VisualItem)t;
        Visualization vis = item.getVisualization();
        SearchTupleSet search = (SearchTupleSet)vis.getGroup(group);
        if ( search == null && incEmpty )
            return true;
        
        String query = search.getQuery();
        return (incEmpty && (query==null || query.length()==0)) 
                || vis.isInGroup(item, group);
    }

    /**
     * @see dartie.prefuse.data.expression.Function#addParameter(prefuse.data.expression.Expression)
     */
    public void addParameter(Expression e) {
        if ( paramCount == 0 )
            super.addParameter(e);
        else if ( paramCount == 1 )
            m_incEmpty = e;
        else
            throw new IllegalStateException(
              "This function takes only 2 parameters.");
    }

    /**
     * @see dartie.prefuse.data.expression.Function#getName()
     */
    public String getName() {
        return "MATCH";
    }

    /**
     * @see dartie.prefuse.data.expression.Function#getParameterCount()
     */
    public int getParameterCount() {
        return 2;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return getName()+"("+m_group+", "+m_incEmpty+")";
    }
    
} // end of class SearchPredicate
