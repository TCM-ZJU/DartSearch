package edu.zju.tcmsearch.util.web;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class ParameterValueDecoder {

	public static String decode(String value){
		try{
			return new String(value.getBytes("ISO-8859-1"),"UTF-8");
		}catch(UnsupportedEncodingException e){
			return "Unsupported Encoding";
		}
	}
	
	public static String decode(String value,String wrap,String org){
		try{
			return new String(value.getBytes(wrap),org);
		}catch(UnsupportedEncodingException e){
			return "Unsupported Encoding";
		}		
	}
	
	public static String guessEncoding(String value){
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<encoding.length;i++){
			for(int j=0;j<encoding.length;j++){
				if(i==j)
					continue;
				String v = decode(value,encoding[i],encoding[j]);
				sb.append(encoding[i]+" --> "+encoding[j]+":&nbsp;&nbsp;");
				sb.append(v).append("&nbsp;&nbsp;&nbsp;").append(URLDecoder.decode(v)).append("   </br>\n");
				
			}
		}
		return sb.toString();
	}
	
	public static String encoding[] = new String[]{"utf-8","gbk","gb2312","ISO-8859-1"};
}
