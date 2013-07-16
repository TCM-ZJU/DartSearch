/*
 * Created on 2005-12-16
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.index.data;

import java.sql.Types;
import java.util.Arrays;

import edu.zju.tcmsearch.util.dartcore.OntoUriParseUtil;

public class ColumnInfo {
    private String ontologyURI;
    private String tableColumn;
    private int columnType=Types.VARCHAR;
    private boolean primaryKey=false;
    private int[] numberArray={Types.BIT,Types.TINYINT,Types.SMALLINT,Types.INTEGER,Types.BIGINT,
            Types.FLOAT,Types.REAL,Types.DOUBLE,Types.NUMERIC,Types.DECIMAL,Types.BOOLEAN};
    private int[] stringArray={Types.CHAR,Types.VARCHAR,Types.LONGVARCHAR};
    private int[] timeArray={Types.DATE,Types.TIME,Types.TIMESTAMP};
    

  /**
   * @return Returns the primaryKey.
   */
  public boolean isPrimaryKey() {
      return primaryKey;
  }
  /**
   * @param primaryKey The primaryKey to set.
   */
  public void setPrimaryKey(boolean primaryKey) {
      this.primaryKey = primaryKey;
  }
  /**
   * @return Returns the ontologyURI.
   */
  public String getOntologyURI() {
      return ontologyURI;
  }
  /**
   * @param ontologyURI The ontologyURI to set.
   */
  public void setOntologyURI(String ontologyURI) {
      this.ontologyURI = ontologyURI;
  }
  /**
   * @return Returns the tableColumn.
   */
  public String getTableColumn() {
      return tableColumn;
  }
  /**
   * @param tableColumn The tableColumn to set.
   */
  public void setTableColumn(String tableColumn) {
      this.tableColumn = tableColumn;
  }
  /**
   * @return Returns the columnType.
   */
  public int getColumnType() {
      return columnType;
  }
  /**
   * @param columnType The columnType to set.
   */
  public void setColumnType(int columnType) {
      this.columnType = columnType;
  }


  /**
   * @return Returns the ontologyName.
   */
  public String getOntologyName() {
      return OntoUriParseUtil.getOntoName(getOntologyURI());
  }


  public boolean isNumber(){
      for (int i=0;i<numberArray.length;i++){
          if (columnType==numberArray[i]){
              return true;
          }
      }
      return false;
  }

  public boolean isString(){
      for (int i=0;i<stringArray.length;i++){
          if (columnType==stringArray[i]){
              return true;
          }
      }
      return false;
  }

  public boolean isClob(){
      return columnType==Types.CLOB;
  }
/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Override
public String toString() {
    StringBuilder sb=new StringBuilder();
    
    sb.append(" column ontoUri is "+getOntologyURI()+"\n");
    sb.append(" column name is "+getTableColumn()+"\n");
    sb.append(" column type is "+getColumnType()+"\n");
    
    return sb.toString();
}
}
