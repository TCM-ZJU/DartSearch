/*
 * Created on 2005-12-26
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.index;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.springframework.jdbc.support.lob.LobHandler;

import edu.zju.tcmsearch.exception.lucene.TcmLuceneException;
import edu.zju.tcmsearch.lucene.index.data.ColumnInfo;
import edu.zju.tcmsearch.lucene.index.data.DbRecData;
import edu.zju.tcmsearch.lucene.index.data.TableInfo;

public class IndexBuildUtil {
	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory.getLog(IndexBuildUtil.class);

    LobHandler lobHandler;
    
    /**
     * @return Returns the lobHandler.
     */
    public LobHandler getLobHandler() {
        return lobHandler;
    }

    /**
     * @param lobHandler The lobHandler to set.
     */
    public void setLobHandler(LobHandler lobHandler) {
        this.lobHandler = lobHandler;
    }

    /**
     * 得到tableInfo中的一行数据信息DbRecData
     * @param rs
     * @param tableInfo
     * @return
     * @throws SQLException
     */
    public DbRecData retrieveRecData(ResultSet rs,TableInfo tableInfo) throws SQLException {
        DbRecData dbRecData = new DbRecData();
        dbRecData.setTableInfo(tableInfo);
        for (ColumnInfo columnInfo : tableInfo.getColumnList()) {
            String columnValue = "";
            if (columnInfo.isClob()) {
                columnValue = getLobHandler().getClobAsString(rs,
                        columnInfo.getTableColumn());
            } else {
                columnValue = rs.getString(columnInfo.getTableColumn());
            }
            dbRecData.addFieldValue(columnInfo,columnValue);
        }
        return dbRecData;
    }  
    /**
     * 得到tableInfo中的一行数据的主键PrimaryKey信息
     * @param rs
     * @param tableInfo
     * @return
     * @throws SQLException
     */
    public DbRecData retrieveRecPrimaryKey(ResultSet rs,TableInfo tableInfo) throws SQLException {
        DbRecData dbRecData = new DbRecData();
        dbRecData.setTableInfo(tableInfo);
        for (ColumnInfo columnInfo : tableInfo.getColumnList()) {
            String columnValue = "";
            if(columnInfo.isPrimaryKey()){
	            if (columnInfo.isClob()) {
	                columnValue = getLobHandler().getClobAsString(rs,
	                        columnInfo.getTableColumn());
	            } else {
	                columnValue = rs.getString(columnInfo.getTableColumn());
	            }
	            dbRecData.addFieldValue(columnInfo,columnValue);
            }
        }
        return dbRecData;
    }  
    /**
     * 
     * 对数据表中的每行数据添加field，实际建立索引.
     * 其中的每行数据recData为一个Document
     * field包括：showContent,clobContent,ontoIdentity,ontologyName,
     * primaryKey,tableName,tableSchema,belongToTheme
     * @param writer 
     * @param curRecData
     */
    public void writeRecIndex(IndexWriter writer, DbRecData curRecData) {
        Document doc = new Document();
//        Field filedContent=Field.UnStored("fieldContent", curRecData.getFieldContent());
//        filedContent.setBoost(1.5f);
//        doc.add(filedContent);
        //curRecData.getShowContent()把该表的所有字段内容通过StringBuilder组合在一起
        Field showContent=new Field("showContent", curRecData.getShowContent(),Field.Store.YES, Field.Index.TOKENIZED);
        showContent.setBoost(1.5f);
        doc.add(showContent);
        
        //doc.add(Field.UnIndexed("showContent", curRecData.getShowContent()));
        //添加大文本clob field
        Field filedClob=new Field("clobContent", curRecData.getClobContent(),Field.Store.NO, Field.Index.TOKENIZED);
        filedClob.setBoost(0.5f);        
        doc.add(filedClob);
        
        doc.add(new Field("ontoIdentity",curRecData.getOntoIdentityStr(),Field.Store.YES, Field.Index.NO));
        doc.add(new Field("ontologyName",curRecData.getOntoNameStr(),Field.Store.YES, Field.Index.TOKENIZED));
       // doc.add(Field.UnIndexed("tableOntology",curRecData.getTableInfo().getOntologyURI()));
        doc.add(new Field("primaryKey",curRecData.getPrimaryKey(),Field.Store.YES, Field.Index.NO));
        doc.add(new Field("tableName",curRecData.getTableInfo().getTableName(),Field.Store.YES, Field.Index.NO));      
        doc.add(new Field("tableSchema",curRecData.getTableInfo().getTableSchema(),Field.Store.YES, Field.Index.NO));   
        doc.add(new Field("belongToTheme",curRecData.getBelongToThemeStr(),Field.Store.NO, Field.Index.TOKENIZED));
        
        
        if(log.isDebugEnabled())
        	//log.debug("insert index rec:\n" + doc.toString());
        	if(!curRecData.getBelongToThemes().isEmpty()){
        		log.debug("insert index rec:\n" + doc.toString());
        	}
        try {
            writer.addDocument(doc);
        } catch (IOException e) {
            throw new TcmLuceneException("write document exception in writeRecIndex!",e);
        }
    }
}
