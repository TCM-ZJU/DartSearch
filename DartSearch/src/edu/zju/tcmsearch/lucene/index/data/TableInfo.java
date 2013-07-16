/*
 * Created on 2005-12-16
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.index.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;

import edu.zju.tcmsearch.util.dartcore.OntoUriParseUtil;

public class TableInfo {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(TableInfo.class);

    private List<String> mainOntoWithoutCons;
    private Map<String,String> mainOntoWithCons;
    private List<String> secondaryOntoWithoutCons;
    private Map<String,String> secondaryOntoWithCons;
    
    private List<String> ontoIdentities;

    private String tableSchema;

    private String tableName;

    private String dbNamespace;

    private String dbLocalpart;

    private List<ColumnInfo> columnList;
    
    private boolean inFullIndex=true;

    /**
     * @return Returns the inFullIndex.
     */
    public boolean isInFullIndex() {
        return inFullIndex;
    }

    /**
     * @param inFullIndex The inFullIndex to set.
     */
    public void setInFullIndex(boolean inFullIndex) {
        this.inFullIndex = inFullIndex;
    }

    /**
     * @return Returns the dbNamespace.
     */
    public String getDbNamespace() {
        return dbNamespace;
    }

    /**
     * @param dbNamespace
     *            The dbNamespace to set.
     */
    public void setDbNamespace(String dbNamespace) {
        this.dbNamespace = dbNamespace;
    }

    /**
     * @return Returns the tableName.
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * @param tableName
     *            The tableName to set.
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * @return Returns the tableSchema.
     */
    public String getTableSchema() {
        return tableSchema;
    }

    /**
     * @return Returns the dbLocalpart.
     */
    public String getDbLocalpart() {
        return dbLocalpart;
    }

    /**
     * @param dbLocalpart
     *            The dbLocalpart to set.
     */
    public void setDbLocalpart(String dbLocalpart) {
        this.dbLocalpart = dbLocalpart;
    }

    /**
     * @param tableSchema
     *            The tableSchema to set.
     */
    public void setTableSchema(String tableSchema) {
        this.tableSchema = tableSchema;
    }

    /**
     * @return Returns the columnList.
     */
    public List<ColumnInfo> getColumnList() {
        return columnList;
    }

    /**
     * @param columnList
     *            The columnList to set.
     */
    public void setColumnList(List<ColumnInfo> columnList) {
        this.columnList = columnList;
    }
    
    public String getTableURI(){
        return getDbNamespace()+"/"+getDbLocalpart();
    }
    
    public QName getQName(){
        return new QName(getDbNamespace(),getDbLocalpart());
    }
    
    public String getTableIdentity(){
        return getTableSchema()+"."+getTableName();
    }

    public ColumnInfo getPrimaryKeyColumn() {
        for (ColumnInfo columnInfo : getColumnList()) {
            if (columnInfo.isPrimaryKey()) {
                return columnInfo;
            }
        }
        //logger.info("Did not get primary from all column in table "+getTableIdentity());
        return null;
        //throw new TcmLuceneException("Did not get primary from all column!");
    }

    public List<String> getMainOntoWithoutCons() {
        return mainOntoWithoutCons;
    }

    public void setMainOntoWithoutCons(List<String> mainOntoURI) {
        this.mainOntoWithoutCons = mainOntoURI;
    }

    public List<String> getSecondaryOntoWithoutCons() {
        return secondaryOntoWithoutCons;
    }

    public void setSecondaryOntoWithoutCons(List<String> secondaryOntoURI) {
        this.secondaryOntoWithoutCons = secondaryOntoURI;
    }
    
    public String getOntoUriStr(){
        StringBuilder sb=new StringBuilder();
        int i=0;
        List<String> ontos = new ArrayList<String>();
        if(mainOntoWithoutCons != null)
        	ontos.addAll(mainOntoWithoutCons);
        if(mainOntoWithCons != null)
        	ontos.addAll(mainOntoWithCons.keySet());
        for (String ontoUri:ontos){
            sb.append(ontoUri);
            i++;
            if (i<ontos.size()){
                sb.append(";");
            }
        }
        return sb.toString();
    }
    
    public List<String> getOntoIdentities(){
    	if (null!=ontoIdentities){
    		return ontoIdentities;
    	}
    	ontoIdentities=new ArrayList<String>();
    	if(mainOntoWithoutCons != null){
	        for (String ontoUri:mainOntoWithoutCons){
	        	ontoIdentities.add(OntoUriParseUtil.getOntoIdentity(ontoUri));
	        }
    	}
        return ontoIdentities;       
    }    
    
    public String getOntoIdentityStr(){
        StringBuilder sb=new StringBuilder();
        int i=0;
        if(mainOntoWithoutCons != null){
	        for (String ontoUri:mainOntoWithoutCons){
	            sb.append(OntoUriParseUtil.getOntoIdentity(ontoUri));
	            i++;
	            if (i<mainOntoWithoutCons.size()){
	                sb.append("; ");
	            }
	        }
        }
        return sb.toString();       
    }
    
    public String getOntoNameStr(){
        StringBuilder sb=new StringBuilder();
        int i=0;
        if(mainOntoWithoutCons != null){
		    for (String ontoUri:mainOntoWithoutCons){
		        sb.append(OntoUriParseUtil.getOntoName(ontoUri));
		        i++;
		        if (i<mainOntoWithoutCons.size()){
		            sb.append(";");
		        }
		    }
        }
        return sb.toString();         
    }
    
    public boolean isRelationTable(){
        if ((null==getMainOntoWithoutCons() && null==getMainOntoWithCons())
        		|| 0==(getMainOntoWithoutCons().size() + getMainOntoWithCons().size())){
            return true;
        }
        return false;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append(" tableInf values is as follows ");
        sb.append("ontoUri : "+getOntoUriStr());
        sb.append("; tableUri : "+getTableURI());
        sb.append("; tableIdentity : "+getTableIdentity());
        sb.append("; column count : "+getColumnList().size());
        sb.append("; isInFullIndex : "+isInFullIndex());
        sb.append("; isRelationTable : "+isRelationTable());
        for (ColumnInfo columnInfo:getColumnList()){
            sb.append(" column info is "+columnInfo);
        }
        return sb.toString();
    }

    
   


	public Map<String, String> getMainOntoWithCons() {
		return mainOntoWithCons;
	}


	public void setMainOntoWithCons(Map<String, String> mainOntoWithCons) {
		this.mainOntoWithCons = mainOntoWithCons;
	}


	public Map<String, String> getSecondaryOntoWithCons() {
		return secondaryOntoWithCons;
	}


	public void setSecondaryOntoWithCons(Map<String, String> secondaryOntoWithCons) {
		this.secondaryOntoWithCons = secondaryOntoWithCons;
	}



}
