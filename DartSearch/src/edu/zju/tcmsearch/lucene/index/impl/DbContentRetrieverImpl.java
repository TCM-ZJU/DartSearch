/*
 * Created on 2005-12-16
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.index.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.apache.lucene.index.IndexWriter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.jdbc.support.lob.LobHandler;

import cn.edu.zju.dart.core.resregistry.model.IRelationSpace;
import edu.zju.tcmsearch.exception.lucene.TcmLuceneException;
import edu.zju.tcmsearch.lucene.index.DbContentRetriever;
import edu.zju.tcmsearch.lucene.index.DbInfoRetriever;
import edu.zju.tcmsearch.lucene.index.IndexBuildCallBack;
import edu.zju.tcmsearch.lucene.index.IndexBuildUtil;
import edu.zju.tcmsearch.lucene.index.data.ColumnInfo;
import edu.zju.tcmsearch.lucene.index.data.DataSourceMap;
import edu.zju.tcmsearch.lucene.index.data.DbRecData;
import edu.zju.tcmsearch.lucene.index.data.DbResInfo;
import edu.zju.tcmsearch.lucene.index.data.TableInfo;
import edu.zju.tcmsearch.lucene.index.data.TableInfoMap;

/**
 * 该类的主要方法为buildRecIndex(),
 * dbInfoRetriever:主要用在getDartSourceMap()方法中，构建dataSourceMap
 * dataSourceMap:保存dataSource的Map
 * lobHandler:对Lob类型的字段处理的辅助类
 * tableInfoMap:保存tableInfo的Map
 * @author ddream
 *
 */
public class DbContentRetrieverImpl implements DbContentRetriever {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger
            .getLogger(DbContentRetrieverImpl.class);

    private DbInfoRetriever dbInfoRetriever;

    private DataSourceMap dataSourceMap;

    private LobHandler lobHandler;
    
    private TableInfoMap tableInfoMap;   

    
    private IndexBuildUtil indexBuildUtil;
    


    /**
     * @return Returns the indexBuildUtil.
     */
    public IndexBuildUtil getIndexBuildUtil() {
        return indexBuildUtil;
    }

    /**
     * @param indexBuildUtil The indexBuildUtil to set.
     */
    public void setIndexBuildUtil(IndexBuildUtil indexBuildUtil) {
        this.indexBuildUtil = indexBuildUtil;
    }


    /**
     * @return Returns the tableInfoMap.
     */
    public TableInfoMap getTableInfoMap() {
        return tableInfoMap;
    }

    /**
     * @param tableInfoMap The tableInfoMap to set.
     */
    public void setTableInfoMap(TableInfoMap tableInfoMap) {
        this.tableInfoMap = tableInfoMap;
    }

    /**
     * @return Returns the dataSourceMap.
     */
    public DataSourceMap getDataSourceMap() {
        return dataSourceMap;
    }

    /**
     * @param dataSourceMap
     *            The dataSourceMap to set.
     */
    public void setDataSourceMap(DataSourceMap dataSourceMap) {
        this.dataSourceMap = dataSourceMap;
    }

    /**
     * @return Returns the dbInfoRetriever.
     */
    public DbInfoRetriever getDbInfoRetriever() {
        return dbInfoRetriever;
    }

    /**
     * @param dbInfoRetriever
     *            The dbInfoRetriever to set.
     */
    public void setDbInfoRetriever(DbInfoRetriever dbInfoRetriever) {
        this.dbInfoRetriever = dbInfoRetriever;
    }

    /**
     * @return Returns the lobHandler.
     */
    public LobHandler getLobHandler() {
        return lobHandler;
    }

    /**
     * @param lobHandler
     *            The lobHandler to set.
     */
    public void setLobHandler(LobHandler lobHandler) {
        this.lobHandler = lobHandler;
    }

    private BasicDataSource getDataSource(TableInfo tableInfo) {
        DbResInfo dbResInfo = dbInfoRetriever.getDbRes(tableInfo.getQName());
        logger.debug("ontoURI is "+dbResInfo.getOntologyURI());
        logger.debug("jdbUrl is "+dbResInfo.getJdbcUrl());
        logger.debug("JdbcUser is "+dbResInfo.getJdbcUser());
        logger.debug("jdbcPwd is "+dbResInfo.getJdbcPwd());

        if (null != dataSourceMap.getDataSource(dbResInfo.getOntologyURI())) {
            return dataSourceMap.getDataSource(dbResInfo.getOntologyURI());
        }
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(dbResInfo.getJdbcDriver());
        ds.setUrl(dbResInfo.getJdbcUrl());
        ds.setUsername(dbResInfo.getJdbcUser());
        ds.setPassword(dbResInfo.getJdbcPwd());
        dataSourceMap.putDataSource(dbResInfo.getOntologyURI(), ds);
               
        return ds;
    }

    public List<DbRecData> getTableContent(TableInfo tableInfo) {
        if (null==tableInfo || null==tableInfo.getColumnList() || 0==tableInfo.getColumnList().size()){
            return Collections.EMPTY_LIST;
        }
        DbTableMappingQuery dbTableMappingQuery=new DbTableMappingQuery(tableInfo);
        return dbTableMappingQuery.execute();
    }

    /**
     * 构造表全查 selet col1 , ... , coln from table , 后面没有where部分
     * @param tableInfo
     * @return
     */
    private String constructSQL(TableInfo tableInfo) {
        if (null==tableInfo || null==tableInfo.getColumnList() || 0==tableInfo.getColumnList().size()){
            return null;
        }
        StringBuilder querySb = new StringBuilder();
        querySb.append("select ");
        int i = 0;
        for (ColumnInfo columnInfo : tableInfo.getColumnList()) {
            i++;
            querySb.append(tableInfo.getTableName()+"."+columnInfo.getTableColumn());
            if (i < tableInfo.getColumnList().size()) {
                querySb.append(",");
            }
        }
        String tableSchema = tableInfo.getTableSchema();
        tableSchema = (tableSchema.equals(IRelationSpace.NULL_RELATION_SPACE_NAME))?"":tableSchema;
        querySb.append(" from " + tableSchema + "." + tableInfo.getTableName());
        return querySb.toString();
    }
    
 



    public DbRecData getTableContentByKey(String tableIdentity, String primaryKey) {
        TableInfo tableInfo=getTableInfoMap().getTableInfo(tableIdentity);        
        if (null==tableInfo || null==tableInfo.getColumnList() || 0==tableInfo.getColumnList().size()){
            throw new TcmLuceneException("table info is not correct!");
        }        
        BasicDataSource ds=getDataSource(tableInfo);
        DbIdTableMappingQuery dbIdTableMappingQuery=new DbIdTableMappingQuery(tableInfo,primaryKey);
        List<DbRecData> recList=dbIdTableMappingQuery.execute();
        if (recList.size()>0){
            return recList.get(0);
        }
        throw new TcmLuceneException("Can not find data by primary key "+primaryKey+" !");
    }
    
    /**
     * 暂时按主键为
     * @param tableInfo
     * @param primaryKey
     * @return
     */
    private String constructIdSQL(TableInfo tableInfo,String primaryKey){
        if (null==tableInfo.getPrimaryKeyColumn()){
            throw new TcmLuceneException("Can not get primary key from table "+tableInfo.getTableIdentity()); 
        }
        String whereStr=tableInfo.getPrimaryKeyColumn().getTableColumn()+"=";
        if (tableInfo.getPrimaryKeyColumn().isNumber()){
            whereStr+= primaryKey;
        }
        else if (tableInfo.getPrimaryKeyColumn().isString()){
            whereStr+= "'"+primaryKey+"'";
        }
        else{
            throw new TcmLuceneException("the primary key type is not number or string : "+tableInfo.getPrimaryKeyColumn().getColumnType());
        }
        String queryStr=constructSQL(tableInfo)+" where "+whereStr;
        return queryStr;
    }
    
    private class DbTableMappingQuery extends MappingSqlQuery {
        TableInfo tableInfo;
        
        public DbTableMappingQuery(TableInfo tableInfo) {
            super(getDataSource(tableInfo), constructSQL(tableInfo));
            this.tableInfo=tableInfo;
            compile();
        }

        public Object mapRow(ResultSet rs, int rowNumber) throws SQLException {
            return indexBuildUtil.retrieveRecData(rs,tableInfo);
        }
    }
 
    private class DbIdTableMappingQuery extends MappingSqlQuery {
        TableInfo tableInfo;
        
        public DbIdTableMappingQuery(TableInfo tableInfo,String primaryKey) {
            super(getDataSource(tableInfo), constructIdSQL(tableInfo,primaryKey)); 
            this.tableInfo=tableInfo;
            compile();
        }

        public Object mapRow(ResultSet rs, int rowNumber) throws SQLException {
            return indexBuildUtil.retrieveRecData(rs,tableInfo);
        } 
    }
    /**
     * 根据tableInfo的信息得到其dataSource，利用Spring的jdbcTemplate执行查询，选择表的每行数据
     * 对每行数据的内容建立索引
     */
    public Integer buildRecIndex(IndexWriter writer,TableInfo tableInfo,IndexBuildCallBack indexBuildCallBack) {
        //下面一行代码得到选择表的每行数据的Sql语句，如对于表TCM.JMX_DZZ,其结果为
        //select JMZ_DZZ.JC,JMZ_DZZ.DZZMC,JMZ_DZZ.LS,JMZ_DZZ.NL,JMZ_DZZ.DZZ_ID,JMZ_DZZ.ZLYW,
    	//JMZ_DZZ.LCYJ_ID from TCM.JMZ_DZZ
    	String queryStr=constructSQL(tableInfo);
    	//getDataSource(tableInfo)得到该tableInfo的DataSource，构建JdbcTemplate类的查询
        JdbcTemplate jdbcTemplate=new JdbcTemplate(getDataSource(tableInfo));
        
        //下面一行代码与内核的本体有关，主要是解决本体的join问题，可以不看
        Map<String,String> mainOntoWithCons = tableInfo.getMainOntoWithCons();
        Map<String,Set<Object>> constraintOnto2Data = null;
        if(mainOntoWithCons != null){
	        constraintOnto2Data = new LinkedHashMap<String,Set<Object>>();
	        for(String onto : mainOntoWithCons.keySet()){
		        Set<Object> dataID = new LinkedHashSet<Object>();
		        constraintOnto2Data.put(onto,dataID);
		        ResultSetExtractor primaryKeyExtractor=new PrimaryKeyExtractor(dataID,tableInfo,indexBuildCallBack,this.indexBuildUtil);
		        //tricky method for temporal use
		        String qStr = queryStr + " where ("+ mainOntoWithCons.get(onto)+")";
		        logger.debug("sql with constraint : "+qStr);
		        jdbcTemplate.query(qStr,primaryKeyExtractor);
	        }
        }
        
        if (null==queryStr){
            return 0;//no record need to be index, return 0
        }
        logger.info("=========start building index of table "+tableInfo.getTableIdentity()+" ============");
        
        IndexBuildRsExtractor resultSetExtractor=new IndexBuildRsExtractor(writer,tableInfo,indexBuildCallBack,this.indexBuildUtil);
        resultSetExtractor.setConstraintOnto2Data(constraintOnto2Data);
        //下面代码执行select查询，转到resultSetExtractor的extractData()方法中执行
        Integer counter=(Integer) jdbcTemplate.query(queryStr,resultSetExtractor);    
        resultSetExtractor=null;//赶快释放内存
        jdbcTemplate=null;
        logger.info("=========end building index of table "+tableInfo.getTableIdentity()+" . total record is "+counter);
        return counter;
    }
}
