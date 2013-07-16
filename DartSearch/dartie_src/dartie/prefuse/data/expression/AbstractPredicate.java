package dartie.prefuse.data.expression;

import dartie.prefuse.data.Schema;
import dartie.prefuse.data.Tuple;

/**
 * Abstract base class for dedicated Predicate instances.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public abstract class AbstractPredicate extends AbstractExpression 
    implements Predicate
{

    /**
     * Returns boolean.class.
     * @see dartie.prefuse.data.expression.Expression#getType(prefuse.data.Schema)
     */
    public Class getType(Schema s) {
        return boolean.class;
    }

    /**
     * Returns the wrapper Object type for the result of
     * {@link Expression#getBoolean(Tuple)}.
     * @see dartie.prefuse.data.expression.Expression#get(prefuse.data.Tuple)
     */
    public Object get(Tuple t) {
        return ( getBoolean(t) ? Boolean.TRUE : Boolean.FALSE );
    }
    
} // end of class AbstractPredicate
