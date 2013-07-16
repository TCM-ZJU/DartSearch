/*
 * Created on 2005-12-16
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.index.data;

public class FieldValue {
  private String value;
  private ColumnInfo columnInfo;
public FieldValue(ColumnInfo info, String value) {
    columnInfo = info;
    this.value = value;
}
/**
 * @return Returns the columnInfo.
 */
public ColumnInfo getColumnInfo() {
    return columnInfo;
}
/**
 * @param columnInfo The columnInfo to set.
 */
public void setColumnInfo(ColumnInfo columnInfo) {
    this.columnInfo = columnInfo;
}
/**
 * @return Returns the value.
 */
public String getValue() {
    if (null==value){
        return ""; //把null滤掉,减少噪声
    }
    return value;
}
/**
 * @param value The value to set.
 */
public void setValue(String value) {
    this.value = value;
}
  
  
}
