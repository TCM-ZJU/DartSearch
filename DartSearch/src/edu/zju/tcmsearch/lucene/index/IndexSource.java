/*
 * Created on 2005-12-16
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.index;

import java.util.Iterator;

import edu.zju.tcmsearch.lucene.index.data.DbRecData;

public interface IndexSource {
    public Iterator<DbRecData> createIterator();
}
