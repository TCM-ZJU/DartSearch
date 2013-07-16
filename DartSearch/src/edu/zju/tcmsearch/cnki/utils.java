package edu.zju.tcmsearch.cnki;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class utils {
	public static String toJavaSourceString(String is){
		StringBuilder sb = new StringBuilder(is.length());
		Scanner scan = new Scanner(is);
		boolean first = true;
		while(scan.hasNextLine()){
			String line = scan.nextLine();
			line = line.replace("\\","\\\\")
			           .replace("\"","\\\"")
			           .replace("\'","\\\'");
			if(first){
				first = false;
                sb.append(" \"").append(line).append("\\n\"").append("\n");
			}else{
				sb.append("+\"").append(line).append("\\n\"").append("\n");
			}
		}
		sb.append(";");
		return sb.toString();
	}
	
	public static String toRegularExprString(String is){
		StringBuilder sb = new StringBuilder(is.length());
		for(char ch:is.toCharArray()){
			String s = null;
			switch(ch){
			case '(': s="\\(";break;
			case ')': s="\\)";break;
			}
			if(null==s){
				sb.append(ch);
			}else{
				sb.append(s);
			}
		}
		return sb.toString();
	}

	public static String unicodeEscape(String s,StringBuilder remain){
		StringBuilder sb = new StringBuilder();
		int iStart = 0;
		int iEnd = 0;
		while(true){
			iEnd=s.indexOf("%",iStart);
			if(iEnd==-1){
				remain.append(s.substring(iStart));
				sb.append(s.substring(iStart));
				break;
			}
			if(iStart!=iEnd){
				remain.append(s.subSequence(iStart,iEnd));
				sb.append(s.subSequence(iStart,iEnd));
			}
			
			if(s.charAt(iEnd+1)=='u'){
				iEnd +=2;
				char chr =(char) Integer.parseInt(s.substring(iEnd,iEnd+4),16);
				sb.append(chr);
				iStart = iEnd+4;
			}else{
				iEnd ++;
				char chr =(char) Integer.parseInt(s.substring(iEnd,iEnd+2),16);
				sb.append(chr);
				iStart = iEnd+2;
			}
		}
		return sb.toString();
	}
	
	public static String decodeBase64(String b,String encoding){
		try {
			BASE64Decoder b64=new BASE64Decoder();
			String s = new String(b64.decodeBuffer(b),encoding);
            return s;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String encodeBase64(String b,String encoding){
		try {
			BASE64Encoder b64=new BASE64Encoder();
			String s = b64.encode(b.getBytes(encoding));
            return s;
		} catch (Exception e) {
			return null;
		}		
	}
	
	public static String urlEncode(String b,String encoding){
		try {
			return URLEncoder.encode(b,encoding);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	public static String urlDecode(String b,String encoding){
		try {
			return URLDecoder.decode(b,encoding);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	public String toUnicode(String s){
		StringBuilder sb = new StringBuilder();
		for(char c:s.toCharArray()){
			Formatter fm = new Formatter();
			sb.append("\\u").append(fm.format("%1$04x",(int)c).toString());
		}
		return sb.toString();
	}	
	
	
	public static Map<String,String> parseNameValueString(String nv){
		Map<String,String> vnp = new HashMap<String,String>();
		for(String pair:nv.split("&")){
			String[] vn = pair.split("=");
			if(vn.length==2){
				vnp.put(vn[0],vn[1]);
			}else if(vn.length==1){
				vnp.put(vn[0],"");
			}
		} 
		return vnp;
	}
	
	public static String makeNameValueString(Map<String,String> vnp,boolean breakIntoLine){
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for(Map.Entry<String,String> p:vnp.entrySet()){
			if(first) first=false;
			else sb.append(breakIntoLine? "\n":"&");
			sb.append(p.getKey());
			sb.append("=");
			sb.append(p.getValue());
		}
		return sb.toString();
	}	
	
}

