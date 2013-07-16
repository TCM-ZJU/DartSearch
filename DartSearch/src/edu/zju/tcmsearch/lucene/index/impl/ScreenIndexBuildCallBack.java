/*
 * Created on 2005-12-21
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.index.impl;

import org.apache.log4j.Logger;

import edu.zju.tcmsearch.lucene.index.IndexBuildCallBack;
import edu.zju.tcmsearch.lucene.index.data.DbRecData;

public class ScreenIndexBuildCallBack implements IndexBuildCallBack{
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger
            .getLogger(ScreenIndexBuildCallBack.class);

    private int infoRate=3000;
    
    /**
     * @return Returns the infoRate.
     */
    public int getInfoRate() {
        return infoRate;
    }

    /**
     * @param infoRate The infoRate to set.
     */
    public void setInfoRate(int infoRate) {
        this.infoRate = infoRate;
    }

    public void execute(int indexCount, DbRecData dbRecData) {
        if (indexCount % infoRate==0){
            System.out.println("now is indexing "+indexCount);
            System.out.println("indexing table is "+dbRecData.getTableInfo().getTableName());
            //System.out.println("indexing data is "+dbRecData.getShowContent());
        }
    }
}

