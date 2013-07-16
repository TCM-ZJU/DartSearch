package dartie.prefuse.visual.expression;

import dartie.prefuse.data.expression.ColumnExpression;
import dartie.prefuse.data.expression.Expression;
import dartie.prefuse.data.expression.Function;
import dartie.prefuse.data.expression.NotPredicate;
import dartie.prefuse.data.expression.Predicate;
import dartie.prefuse.visual.VisualItem;

/**
 * Expression that indicates if an item's validated flag is set.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class ValidatedPredicate extends ColumnExpression
    implements Predicate, Function
{
    /** Convenience instance for the validated == true case. */
    public static final Predicate TRUE = new ValidatedPredicate();
    /** Convenience instance for the validated == false case. */
    public static final Predicate FALSE = new NotPredicate(TRUE);
    
    /**
     * Create a new ValidatedPredicate.
     */
    public ValidatedPredicate() {
        super(VisualItem.VALIDATED);
    }
    
    /**
     * @see dartie.prefuse.data.expression.Function#getName()
     */
    public String getName() {
        return "VALIDATED";
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

} // end of class ValidatedPredicate
