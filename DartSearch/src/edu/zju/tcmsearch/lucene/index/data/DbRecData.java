/*
 * Created on 2005-12-16
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.index.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.zju.tcmsearch.common.ApplicationContextHolder;
import edu.zju.tcmsearch.common.TcmConstants;
import edu.zju.tcmsearch.common.domain.OntologyTheme;
import edu.zju.tcmsearch.common.domain.OntologyThemesFromDB;
import edu.zju.tcmsearch.util.dartcore.OntoUriParseUtil;

public class DbRecData {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(DbRecData.class);

    private TableInfo tableInfo;

    private List<FieldValue> fieldValueList=new ArrayList<FieldValue>();
    
    
    /**
     * @return Returns the fieldValueList.
     */
    public List<FieldValue> getFieldValueList() {
        return fieldValueList;
    }
    /**
     * @param fieldValueList The fieldValueList to set.
     */
    public void setFieldValueList(List<FieldValue> fieldValueList) {
        this.fieldValueList = fieldValueList;
    }
    /**
     * @return Returns the clobContent.
     */
    public String getClobContent() {
        StringBuilder sb=new StringBuilder();
        int i=0;
        for (FieldValue fieldValue:fieldValueList){
            i++;
            if (fieldValue.getColumnInfo().isClob()){
                sb.append(fieldValue.getValue());
                if (i<fieldValueList.size() && !"".equals(fieldValue.getValue())){
                    sb.append("\\");//最后一个词或上一个词为空时无需分隔符
                }
            }
        }
        return sb.toString();
    }

    /**
     * @return Returns the fieldContent.
     */
    public String getFieldContent() {
        StringBuilder sb=new StringBuilder();
        int i=0;
        for (FieldValue fieldValue:fieldValueList){
            i++;
            if (!fieldValue.getColumnInfo().isClob()){
                sb.append(fieldValue.getValue());
                if (i<fieldValueList.size() && !"".equals(fieldValue.getValue())){
                  sb.append("\\");//最后一个词或上一个词为空时无需分隔符
                }
            }
        }
        return sb.toString();
    }
    
    public String getShowContent(){
        StringBuilder sb=new StringBuilder();
        for (FieldValue fieldValue:fieldValueList){
            //sb.append("<span>"+fieldValue.getColumnInfo().getOntologyName()+"</span>");
            sb.append(fieldValue.getColumnInfo().getOntologyName());
            sb.append(":");
            if (fieldValue.getColumnInfo().isClob()){
                sb.append("大字段");
            }
            else{
                sb.append(fieldValue.getValue());
            }
            if (sb.length()>TcmConstants.LUCENE_LIMITFIELDLENGTH){//limit the size of showContent
                break;
            }
            sb.append("; ");
        }
        return sb.toString();        
    }
    


    /**
     * @return Returns the tableInfo.
     */
    public TableInfo getTableInfo() {
        return tableInfo;
    }
    /**
     * @param tableInfo The tableInfo to set.
     */
    public void setTableInfo(TableInfo tableInfo) {
        this.tableInfo = tableInfo;
    }

    public void addFieldValue(ColumnInfo columnInfo,String value){
        this.fieldValueList.add(new FieldValue(columnInfo,value));
    }
    
    public ColumnInfo getPrimaryKeyColumn(){
    	for (FieldValue fieldValue:fieldValueList){
            if (fieldValue.getColumnInfo().equals(tableInfo.getPrimaryKeyColumn())){
                return fieldValue.getColumnInfo();
            }
        }   
    	return null;
    }
    
    /**
     * 0 代表没找到主键
     * @return
     */
    public String getPrimaryKey(){
        for (FieldValue fieldValue:fieldValueList){
            if (fieldValue.getColumnInfo().equals(tableInfo.getPrimaryKeyColumn())){
                return fieldValue.getValue();
            }
        }
        //logger.info ("没找到表"+tableInfo.getTableName()+ "的对应主键！");
        return "0";
    }
    
    Set<String> dataSpecifiedOnto = new HashSet<String>();
    public void addDataSpecifiedOnto(String onto){
    	dataSpecifiedOnto.add(onto);
    }
    public String getDataSpecifiedOntoIdentityStr(){
        StringBuilder sb=new StringBuilder();
        int i=0;
        for (String ontoUri:dataSpecifiedOnto){
            sb.append(OntoUriParseUtil.getOntoIdentity(ontoUri));
            i++;
            if (i<dataSpecifiedOnto.size()){
                sb.append("; ");
            }
        }
        return sb.toString(); 
    }
    public String getDataSpecifiedOntoStr(){
        StringBuilder sb=new StringBuilder();
        int i=0;
        for (String ontoUri:dataSpecifiedOnto){
            sb.append(OntoUriParseUtil.getOntoName(ontoUri));
            i++;
            if (i<dataSpecifiedOnto.size()){
                sb.append(";");
            }
        }
        return sb.toString();  
    }
    public String getOntoNameStr(){
    	String ontoName = tableInfo.getOntoNameStr().trim(); 
    	if(dataSpecifiedOnto.size() != 0 && !("".equals(ontoName)) ){
    		ontoName += ";";
    	}
    	return ontoName + getDataSpecifiedOntoStr();
    }
    public String getOntoIdentityStr(){
    	String ontoId = tableInfo.getOntoIdentityStr().trim(); 
    	if(dataSpecifiedOnto.size() != 0 && !("".equals(ontoId)) ){
    		ontoId += ";";
    	}
    	return  ontoId + getDataSpecifiedOntoIdentityStr();
    }
    public List<String> getOntoIdentities(){
    	List<String> ontoIDs = new ArrayList<String>();
    	ontoIDs.addAll(tableInfo.getOntoIdentities());
        for (String ontoUri:dataSpecifiedOnto){
        	ontoIDs.add(OntoUriParseUtil.getOntoIdentity(ontoUri));
        }
        return ontoIDs;       
    }  
    
    public OntologyThemesFromDB getOntologyThemes() {
		return (OntologyThemesFromDB) ApplicationContextHolder.getContext().getBean("ontologyThemes");
	}
    public Set<OntologyTheme> getBelongToThemes(){
    	Set<OntologyTheme> themeSet=new HashSet<OntologyTheme>();
    	for (String ontoIdentity:getOntoIdentities()){
    		themeSet.addAll(getOntologyThemes().getOntoBelongToThemes(ontoIdentity));
    	}
    	return themeSet;
    }
    public String getBelongToThemeStr(){
    	StringBuilder sb=new StringBuilder();
    	for (OntologyTheme ontologyTheme: getBelongToThemes()){
    		sb.append(ontologyTheme.getName()+" ");
    	}
    	return sb.toString().trim();
    }
}
