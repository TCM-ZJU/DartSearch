/*
 * Created on 2005-12-29
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.splitter.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;

import edu.zju.tcmsearch.lucene.splitter.data.DictValue;

public class TcmDictDAO implements DictDAO{
    private DataSource dataSource;
    private String tableName;

    /**
     * @return Returns the dataSource.
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * @param dataSource The dataSource to set.
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @SuppressWarnings("unchecked")
    public List<DictValue> getDictValues(String strPrefix) {
        DictMappingQuery dictMappingQuery=new DictMappingQuery(getDataSource());
        Object[] parameters = new Object[1];
        parameters[0]=strPrefix+"%";
        return dictMappingQuery.execute(parameters);
    }
    
    private class DictMappingQuery extends MappingSqlQuery {
        public DictMappingQuery(DataSource ds) {
            super(ds, "select CONCEPT,USECOUNT from "+getTableName()+" where CONCEPT like ?");
            declareParameter(
                    new SqlParameter("CONCEPT", Types.VARCHAR));
            compile();
        }

        public Object mapRow(ResultSet rs, int rowNumber) throws SQLException {
            DictValue dictValue=new DictValue(rs.getString("CONCEPT"));
            dictValue.setUseCount(rs.getInt("USECOUNT")); 
            return dictValue;         
        }
    }

    public List<String> getAllPrefixes() {
        DictPrefixMappingQuery prefixQuery=new DictPrefixMappingQuery(getDataSource());
        return prefixQuery.execute();
    }    

    private class DictPrefixMappingQuery extends MappingSqlQuery {
        public DictPrefixMappingQuery(DataSource ds) {
            super(ds, "select substr(concept,0,1) conceptPrefix from "+getTableName()+" group by substr(concept,0,1)");
            compile();
        }

        public Object mapRow(ResultSet rs, int rowNumber) throws SQLException {
            return rs.getString("conceptPrefix");         
        }
    }

    /**
     * @return Returns the tableName.
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * @param tableName The tableName to set.
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }    
}
