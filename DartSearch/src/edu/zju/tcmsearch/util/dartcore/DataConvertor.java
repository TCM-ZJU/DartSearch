package edu.zju.tcmsearch.util.dartcore;
/*
 * 浙江大学网格实验室
 * @author 谢骋超 
 * 2005年
 */
public class DataConvertor {
  public static boolean toBoolean(String value){
	  if ("true".equals(value)){
		  return true;
	  }
	  else if ("false".equals(value)){
		  return false;
	  }
	  return false;
	  //throw new TcmRuntimeException("convert to boolean error: the input vlaue is not 'true' or 'false'!");
  }
}
