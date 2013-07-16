/*
 * Created on 2005-12-30
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.splitter.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.zju.tcmsearch.lucene.index.DbInfoRetriever;
import edu.zju.tcmsearch.lucene.splitter.data.DictValue;
import edu.zju.tcmsearch.util.dartcore.OntoUriParseUtil;

public class OntoDictDAO implements DictDAO{
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(OntoDictDAO.class);

    private DbInfoRetriever dbInfoRetriever;
    private List<String> ontoUriNames;
    private List<String> prefixes;
    
    private List<String> getOntoUriNames(){
        if (null!=ontoUriNames){
            return ontoUriNames;
        }
        ontoUriNames=new ArrayList<String>();
        for (String ontoUri:getDbInfoRetriever().getAllOntoUris()){
            ontoUriNames.add(OntoUriParseUtil.getOntoName(ontoUri));
        }
        
        return ontoUriNames;
    }
    
    

    public List<DictValue> getDictValues(String strPrefix) {
        List<DictValue> dictValues=new ArrayList<DictValue>();
        for (String ontoUriName:getOntoUriNames()){
            if (ontoUriName.startsWith(strPrefix)){
                DictValue dictValue=new DictValue(ontoUriName);
                dictValue.setCurPos(0);
                dictValue.setUseCount(0);
                dictValues.add(dictValue);
            }
        }
        return dictValues;
    }

    /**
     * @return Returns the dbInfoRetriever.
     */
    public DbInfoRetriever getDbInfoRetriever() {
        return dbInfoRetriever;
    }

    /**
     * @param dbInfoRetriever The dbInfoRetriever to set.
     */
    public void setDbInfoRetriever(DbInfoRetriever dbInfoRetriever) {
        this.dbInfoRetriever = dbInfoRetriever;
    }



    public List<String> getAllPrefixes() {
        if (null!=prefixes){
            return prefixes;
        }
        Set prefixesSet=new HashSet<String>();
        for (String ontoUriName:getOntoUriNames()){
            logger.debug("ontoUriName: "+ontoUriName);
            prefixesSet.add(ontoUriName.substring(0,1));
        }
        prefixes=new ArrayList(prefixesSet);
        return prefixes;
    }

}
