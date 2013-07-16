package edu.zju.tcmsearch.common;
/*
 * 浙江大学网格实验室
 * @author 谢骋超 
 * 2005年
 */
public class TcmConstants {
  public static final String VAR_PREFIX="http://dart.zju.edu.cn#";
  
  public static final int LEAF_CategoryLevel=2;
  
  public static final int LUCENE_LIMITFIELDLENGTH=2000;
  
  public static boolean isLeafCategory(int level){
      return LEAF_CategoryLevel==level;
  }
}
