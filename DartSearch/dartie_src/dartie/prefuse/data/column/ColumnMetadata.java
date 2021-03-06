package dartie.prefuse.data.column;

import java.util.Comparator;
import java.util.Map;

import dartie.prefuse.data.Table;
import dartie.prefuse.data.event.ColumnListener;
import dartie.prefuse.data.util.Index;
import dartie.prefuse.util.DataLib;
import dartie.prefuse.util.TypeLib;
import dartie.prefuse.util.collections.DefaultLiteralComparator;


/**
 * TODO consider refactor. is non-dynamic mode needed? pass Column reference in?
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class ColumnMetadata implements ColumnListener {

    private Table   m_table;
    private String  m_field;
    private boolean m_dynamic;
    private boolean m_init;

    private Comparator m_cmp;
    
    private Object m_default;
    private int m_min;
    private int m_max;
    private int m_median;
    private int m_unique;
    private Double m_mean;
    private Double m_stdev;
    private Double m_sum;
    private Object[] m_ordinalA;
    private Map m_ordinalM;
    
    // ------------------------------------------------------------------------
    
    public ColumnMetadata(Table table, String column) {
        this(table, column, DefaultLiteralComparator.getInstance(), true);
    }
    
    public ColumnMetadata(Table table, String column, 
            Comparator cmp, boolean dynamic)
    {
        m_table = table;
        m_field = column;
        m_cmp = cmp;
        m_dynamic = dynamic;
    }
    
    public void dispose() {
        m_table.getColumn(m_field).removeColumnListener(this);
    }

    // ------------------------------------------------------------------------
    
    private void clearCachedValues() {
        m_min    = -1;
        m_max    = -1;
        m_median = -1;
        m_unique = -1;
        m_mean   = null;
        m_stdev  = null;
        m_sum    = null;
        m_ordinalA = null;
        m_ordinalM = null;
    }
    
    public void calculateValues() {
        clearCachedValues();
        boolean dyn = m_dynamic;
        m_dynamic = true;
        getMinimumRow();
        getMaximumRow();
        getMedianRow();
        getUniqueCount();
        if ( TypeLib.isNumericType(m_table.getColumnType(m_field)) ) {
            getMean();
            getDeviation();
            getSum();
        }
        getOrdinalArray();
        getOrdinalMap();
        m_init = true;
        m_dynamic = dyn;
    }
    
    private void accessCheck() {
        if ( m_init ) return;
        
        if ( m_dynamic ) {
          clearCachedValues();
          m_table.getColumn(m_field).addColumnListener(this);
        } else {
          calculateValues();
        }
        m_init = true;
    }
    
    // ------------------------------------------------------------------------
    
    public Comparator getComparator() {
        return m_cmp;
    }
    
    public void setComparator(Comparator c) {
        m_cmp = c;
        clearCachedValues();
    }
    
    public Object getDefaultValue() {
        return m_default;
    }
    
    public int getMinimumRow() {
        accessCheck();
        if ( m_min == -1 && m_dynamic ) {
            Index idx = m_table.getIndex(m_field);
            if ( idx != null ) {
                m_min = idx.minimum();
            } else {
                m_min = DataLib.min(m_table.tuples(), m_field, m_cmp).getRow();
            }
        }
        return m_min;
    }
    
    public int getMaximumRow() {
        accessCheck();
        if ( m_max == -1 && m_dynamic ) {
            Index idx = m_table.getIndex(m_field);
            if ( idx != null ) {
                m_max = idx.maximum();
            } else {
                m_max = DataLib.max(m_table.tuples(), m_field, m_cmp).getRow();
            }
        }
        return m_max;
    }
    
    public int getMedianRow() {
        accessCheck();
        if ( m_median == -1 && m_dynamic ) {
            Index idx = m_table.getIndex(m_field);
            if ( idx != null ) {
                m_max = idx.median();
            } else {
                m_median = DataLib.median(
                                m_table.tuples(), m_field, m_cmp).getRow();
            }
        }
        return m_median;
    }
    
    public int getUniqueCount() {
        accessCheck();
        if ( m_unique == -1 && m_dynamic ) {
            Index idx = m_table.getIndex(m_field);
            if ( idx != null ) {
                m_unique = idx.uniqueCount();
            } else {
                m_unique = DataLib.uniqueCount(m_table.tuples(), m_field);
            }
        }
        return m_unique;
    }
    
    public double getMean() {
        accessCheck();
        if ( m_mean == null && m_dynamic ) {
            m_mean = new Double(DataLib.mean(m_table.tuples(), m_field));
        }
        return m_mean.doubleValue();
    }
    
    public double getDeviation() {
        accessCheck();
        if ( m_stdev == null && m_dynamic ) {
            m_stdev = new Double(
                    DataLib.deviation(m_table.tuples(), m_field, getMean()));
        }
        return m_stdev.doubleValue();
    }
    
    public double getSum() {
        accessCheck();
        if ( m_sum == null && m_dynamic ) {
            m_sum = new Double(DataLib.sum(m_table.tuples(), m_field));
        }
        return m_sum.doubleValue();
    }
    
    public Object[] getOrdinalArray() {
        accessCheck();
        if ( m_ordinalA == null && m_dynamic ) {
            m_ordinalA = DataLib.ordinalArray(m_table.tuples(),m_field,m_cmp);
        }
        return m_ordinalA;
    }
    
    public Map getOrdinalMap() {
        accessCheck();
        if ( m_ordinalM == null && m_dynamic ) {
            m_ordinalM = DataLib.ordinalMap(m_table.tuples(), m_field, m_cmp);
        }
        return m_ordinalM;
    }
    
    // ------------------------------------------------------------------------
    
    public void columnChanged(Column src, int type, int start, int end) {
        clearCachedValues();
    }
    
    public void columnChanged(Column src, int idx, boolean prev) {
        columnChanged(src, 0, idx, idx);
    }

    public void columnChanged(Column src, int idx, double prev) {
        columnChanged(src, 0, idx, idx);
    }

    public void columnChanged(Column src, int idx, float prev) {
        columnChanged(src, 0, idx, idx);
    }
    
    public void columnChanged(Column src, int idx, int prev) {
        columnChanged(src, 0, idx, idx);
    }

    public void columnChanged(Column src, int idx, long prev) {
        columnChanged(src, 0, idx, idx);
    }

    public void columnChanged(Column src, int idx, Object prev) {
        columnChanged(src, 0, idx, idx);
    }
    
} // end of class ColumnMetadata
