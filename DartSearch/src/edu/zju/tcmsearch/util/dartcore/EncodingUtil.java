package edu.zju.tcmsearch.util.dartcore;

import java.io.UnsupportedEncodingException;

import edu.zju.tcmsearch.exception.query.TcmRuntimeException;

/*
 * 浙江大学网格实验室
 * @author 谢骋超 
 * 2005年
 */
public class EncodingUtil {
  public static String getChinaStr(String str){
	  try {
		return new String(str.getBytes("8859_1"), "GB2312");
	} catch (UnsupportedEncodingException e) {
		throw new TcmRuntimeException("字符转成中文出错!",e);
	}
  }
  
  public static String getEngStr(String str){
	  try {
		return new String(str.getBytes("GB2312"), "8859_1");
	} catch (UnsupportedEncodingException e) {
		throw new TcmRuntimeException("字符转成中文出错!",e);
	}
  }
}
