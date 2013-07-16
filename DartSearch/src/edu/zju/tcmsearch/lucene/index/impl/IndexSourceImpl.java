/*
 * Created on 2005-12-16
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.index.impl;

import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.List;

import edu.zju.tcmsearch.lucene.index.DbContentRetriever;
import edu.zju.tcmsearch.lucene.index.DbInfoRetriever;
import edu.zju.tcmsearch.lucene.index.IndexSource;
import edu.zju.tcmsearch.lucene.index.data.DbRecData;
import edu.zju.tcmsearch.lucene.index.data.TableInfo;

public class IndexSourceImpl implements IndexSource {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger
            .getLogger(IndexSourceImpl.class);

    private DbContentRetriever dbContentRetriever;

    private DbInfoRetriever dbInfoRetriever;

    private List<TableInfo> tableInfoList;

    /**
     * @return Returns the dbContentRetriever.
     */
    public DbContentRetriever getDbContentRetriever() {
        return dbContentRetriever;
    }

    /**
     * @param dbContentRetriever The dbContentRetriever to set.
     */
    public void setDbContentRetriever(DbContentRetriever dbContentRetriever) {
        this.dbContentRetriever = dbContentRetriever;
    }

    /**
     * @return Returns the dbInfoRetriever.
     */
    public DbInfoRetriever getDbInfoRetriever() {
        return dbInfoRetriever;
    }

    /**
     * @param dbInfoRetriever The dbInfoRetriever to set.
     */
    public void setDbInfoRetriever(DbInfoRetriever dbInfoRetriever) {
        this.dbInfoRetriever = dbInfoRetriever;
    }

    /**
     * @param tableInfoList The tableInfoList to set.
     */
    public void setTableInfoList(List<TableInfo> tableInfoList) {
        this.tableInfoList = tableInfoList;
    }

    public List<TableInfo> getTableInfoList() {
        if (null != tableInfoList) {
            return tableInfoList;
        }
        tableInfoList = dbInfoRetriever.getAllTableInfos();
        return tableInfoList;
    }

    public Iterator<DbRecData> createIterator() {
        return new DbContentIterator();
    }
    
    /**
     * 一个空的Iterator, 表示Iterate完毕
     * @author xcc
     *
     */
    private class EmptyIterator implements Iterator{
        /**
         * Logger for this class
         */
        private final Logger logger = Logger.getLogger(EmptyIterator.class);

        public boolean hasNext() {
            return false;
        }

        public Object next() {
            throw new UnsupportedOperationException(
            "The EmptyIterator should not be called by next method!");
        }

        public void remove() {
            throw new UnsupportedOperationException(
            "don't support remove in EmptyIterator!");
        }
        
    }

    private class DbContentIterator implements Iterator {
        /**
         * Logger for this class
         */
        private final Logger logger = Logger.getLogger(DbContentIterator.class);

        private Iterator<TableInfo> tableInfoIterator = getTableInfoList()
                .iterator();

        @SuppressWarnings("unchecked")
        private Iterator<DbRecData> tableContentIterator = getNextContentIterator();
        

        private Iterator getNextContentIterator() {
            if (!tableInfoIterator.hasNext()) {
                return new EmptyIterator();            
            }       
            //循环往下找每个有没有下一个iterator，如果找到并且这个iterator里有东西就返回
            while (tableInfoIterator.hasNext()) {
                TableInfo curTableInfo = tableInfoIterator.next();
                List<DbRecData> curConentList=dbContentRetriever.getTableContent(curTableInfo);
                logger.debug("cur ConentList size is "+curConentList.size());
                Iterator curContentIterator = curConentList.iterator();
                if (curContentIterator.hasNext()) {
                    logger.debug("found the next Content Iterator, return!");
                    return curContentIterator;
                } 
            }
            //啥都没找到，返回null
            return new EmptyIterator(); 
        }

        @SuppressWarnings("unchecked")
        public boolean hasNext() {
            if (tableContentIterator.hasNext()) {
                return true;
            }
            tableContentIterator=getNextContentIterator();
            return tableContentIterator.hasNext();
        }

        @SuppressWarnings("unchecked")
        public Object next() {    
            if (tableContentIterator.hasNext()) {
                return tableContentIterator.next();
            }
            tableContentIterator=getNextContentIterator();
            return tableContentIterator.next();
        }

        public void remove() {
            throw new UnsupportedOperationException(
                    "don't support remove in DbContentIterator!");
        }

    }

}
