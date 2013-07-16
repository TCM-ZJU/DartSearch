/*
 * Created on 2005-12-16
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.config;

public class LuceneConfig {
    private IIndexDirectory indexDir;
    private int minMergeDocs=50;
    private int maxMergeDocs=Integer.MAX_VALUE;
    private int mergeFactor=50;
    private int maxFieldLength=Integer.MAX_VALUE;
    private int limitShowFieldLength=1000;
    private String hightLightCss;



    /**
     * @return Returns the hightLightCss.
     */
    public String getHightLightCss() {
        return hightLightCss;
    }

    /**
     * @param hightLightCss The hightLightCss to set.
     */
    public void setHightLightCss(String hightLightCss) {
        this.hightLightCss = hightLightCss;
    }

    /**
     * @return Returns the limitShowFieldLength.
     */
    public int getLimitShowFieldLength() {
        return limitShowFieldLength;
    }

    /**
     * @param limitShowFieldLength The limitShowFieldLength to set.
     */
    public void setLimitShowFieldLength(int limitShowFieldLength) {
        this.limitShowFieldLength = limitShowFieldLength;
    }

    /**
     * @return Returns the indexDir.
     */
    public IIndexDirectory getIndexDir() {
        return indexDir;
    }

    /**
     * @param indexDir The indexDir to set.
     */
    public void setIndexDir(IIndexDirectory indexDir) {
        this.indexDir = indexDir;
    }

    /**
     * @return Returns the maxFieldLength.
     */
    public int getMaxFieldLength() {
        return maxFieldLength;
    }

    /**
     * @param maxFieldLength The maxFieldLength to set.
     */
    public void setMaxFieldLength(int maxFieldLength) {
        this.maxFieldLength = maxFieldLength;
    }

    /**
     * @return Returns the maxMergeDocs.
     */
    public int getMaxMergeDocs() {
        return maxMergeDocs;
    }

    /**
     * @param maxMergeDocs The maxMergeDocs to set.
     */
    public void setMaxMergeDocs(int maxMergeDocs) {
        this.maxMergeDocs = maxMergeDocs;
    }

    /**
     * @return Returns the mergeFactor.
     */
    public int getMergeFactor() {
        return mergeFactor;
    }

    /**
     * @param mergeFactor The mergeFactor to set.
     */
    public void setMergeFactor(int mergeFactor) {
        this.mergeFactor = mergeFactor;
    }

    /**
     * @return Returns the minMergeDocs.
     */
    public int getMinMergeDocs() {
        return minMergeDocs;
    }

    /**
     * @param minMergeDocs The minMergeDocs to set.
     */
    public void setMinMergeDocs(int minMergeDocs) {
        this.minMergeDocs = minMergeDocs;
    }
    
}
