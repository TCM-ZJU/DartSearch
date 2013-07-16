/*
 * Created on 2005-12-20
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.util.dartcore;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

public class OntoUriParseUtil {
    public static String getPrefix(String ontoUri){
        int pos=ontoUri.indexOf('#');
       // System.out.println(pos);
        return ontoUri.substring(0,pos);
    }
    
    public static String getOntoName(String ontoUri){
        int pos=ontoUri.indexOf('#');
       // System.out.println(pos);
        return ontoUri.substring(pos+1,ontoUri.length());
    }    
    
       
    
    public static String getOntoIdentity(String ontoUri){
        return getPrefix(ontoUri)+"__"+getOntoName(ontoUri);
    }
    
    public static String getOntoNameById(String ontoIdentity){
        int pos=ontoIdentity.indexOf("__");
        return ontoIdentity.substring(pos+2,ontoIdentity.length());
    } 
    
    public static String getPrefixById(String ontoIdentity){
        int pos=ontoIdentity.indexOf("__");
        return ontoIdentity.substring(0,pos);
    }    
    
    public static String getOntoUri(String ontoIdentity){        
        return getPrefixById(ontoIdentity)+"#"+getOntoNameById(ontoIdentity);
    }     
    
    public static List<String> parseOntoInfos(String ontoIdenity){
        String[] ontoInfos=StringUtils.tokenizeToStringArray(ontoIdenity,";");
        List<String> ontoInfoList=new ArrayList<String>();
        if (null==ontoInfos){
            ontoInfoList.add(ontoIdenity.trim());
            return ontoInfoList;
        }
        
        for (String ontoInfo:ontoInfos){
            ontoInfoList.add(ontoInfo.trim());
        }
        return ontoInfoList;
    }
    
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        String ontoUri="http://dart.zju.edu.cn/dartcore/dart#疾病信息";
        System.out.println(getPrefix(ontoUri));
        System.out.println(getOntoName(ontoUri));
        System.out.println(getOntoIdentity(ontoUri));
        
        String ontoIdentity=getOntoIdentity(ontoUri);
        System.out.println(ontoIdentity);
        
        System.out.println(getOntoNameById(ontoIdentity));
        System.out.println(getPrefixById(ontoIdentity));
        System.out.println(getOntoUri(ontoIdentity));       
        
    }

}
