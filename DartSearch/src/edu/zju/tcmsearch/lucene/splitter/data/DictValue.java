/*
 * Created on 2005-12-28
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.splitter.data;

import org.apache.commons.lang.builder.HashCodeBuilder;

import edu.zju.tcmsearch.common.domain.DartOntology;
import edu.zju.tcmsearch.exception.lucene.TcmLuceneException;


public class DictValue {
  private String value;
 // private Reader reader;
  private int curPos=0;
  private int useCount;
public DictValue(String value) {
    super();
    // TODO Auto-generated constructor stub
    this.value = value;
}
/**
 * @return Returns the curPos.
 */
public int getCurPos() {
    return curPos;
}
/**
 * @param curPos The curPos to set.
 */
public void setCurPos(int curPos) {
    this.curPos = curPos;
}
/**
 * @return Returns the useCount.
 */
public int getUseCount() {
    return useCount;
}
/**
 * @param useCount The useCount to set.
 */
public void setUseCount(int useCount) {
    this.useCount = useCount;
}
/**
 * @return Returns the value.
 */
public String getValue() {
    return value;
}
/**
 * @param value The value to set.
 */
public void setValue(String value) {
    this.value = value;    
}

//private Reader getReader(){
//    if (null!=reader){
//        return reader;
//    }
//    reader=new StringReader(getValue());
//    return this.reader;
//}

public String nextValue(){
    if (!isEnd()){
        curPos++;
        return this.value.substring(curPos-1,curPos);
        
    }
    else{
        return null;
    }
}



/* (non-Javadoc)
 * @see java.lang.String#length()
 */
public int length() {
    return value.length();
}
public boolean isEnd(){
    return this.curPos==this.value.length();
}
/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Override
public String toString() {
    StringBuilder sb=new StringBuilder();
    sb.append(" dict value is "+getValue());
    sb.append(" dict curpos is "+getCurPos());
    sb.append(" dict useCount is "+getUseCount());
    sb.append(" dict isEnd is "+isEnd());
    return sb.toString();
}
  
@Override
public boolean equals(Object obj) {
    if (!(obj instanceof DictValue)){
        throw new TcmLuceneException("要比较的对象不是DictValue类型!");
    }
    if (null==obj){
        return false;
    }
    
    if (this==obj){
        return true;
    }
    
    DictValue compareValue=(DictValue)obj;
    
    if (this.getValue().equals(compareValue.getValue())) {
        return true;
    }else{
        return false;
    }         

}
/* (non-Javadoc)
 * @see java.lang.Object#hashCode()
 */
@Override
public int hashCode() {
    return new HashCodeBuilder(15, 19).append(getValue()).toHashCode();
}
  
}
