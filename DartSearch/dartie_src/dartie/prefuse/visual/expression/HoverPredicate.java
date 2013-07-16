package dartie.prefuse.visual.expression;

import dartie.prefuse.data.expression.ColumnExpression;
import dartie.prefuse.data.expression.Expression;
import dartie.prefuse.data.expression.Function;
import dartie.prefuse.data.expression.NotPredicate;
import dartie.prefuse.data.expression.Predicate;
import dartie.prefuse.visual.VisualItem;

/**
 * Expression that indicates if an item is currently under the mouse
 * pointer.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class HoverPredicate extends ColumnExpression
    implements Predicate, Function
{
    /** Convenience instance for the hover == true case. */
    public static final Predicate TRUE = new HoverPredicate();
    /** Convenience instance for the hover == false case. */
    public static final Predicate FALSE = new NotPredicate(TRUE);
    
    /**
     * Create a new HoverPredicate.
     */
    public HoverPredicate() {
        super(VisualItem.HOVER);
    }

    /**
     * @see dartie.prefuse.data.expression.Function#getName()
     */
    public String getName() {
        return "HOVER";
    }

    /**
     * @see dartie.prefuse.data.expression.Function#addParameter(prefuse.data.expression.Expression)
     */
    public void addParameter(Expression e) {
        throw new IllegalStateException("This function takes 0 parameters");
    }

    /**
     * @see dartie.prefuse.data.expression.Function#getParameterCount()
     */
    public int getParameterCount() {
        return 0;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return getName()+"()";
    }

} // end of class HoverPredicate
