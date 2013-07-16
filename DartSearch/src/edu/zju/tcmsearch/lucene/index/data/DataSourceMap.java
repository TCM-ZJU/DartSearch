/*
 * Created on 2005-12-16
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.index.data;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;

import edu.zju.tcmsearch.exception.query.TcmRuntimeException;

public class DataSourceMap {
  private  Map<String,BasicDataSource> dsMap=new HashMap<String,BasicDataSource>();
  
  public BasicDataSource getDataSource(String dsName){
      return dsMap.get(dsName);
  }
  
  public void putDataSource(String dsName,BasicDataSource ds){
      dsMap.put(dsName,ds);
  }
  
  public void destroy(){
      for (BasicDataSource dataSource:dsMap.values()){
          try {
            dataSource.close();
        } catch (SQLException e) {
            throw new TcmRuntimeException("关闭datasource失败!");
        }
      }
  }
}
