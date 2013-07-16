/*
 * Created on 2005-12-19
 * author 谢骋超
 * 
 * 
 * Modified by zhm
 * 2008-04-30
 * 增加了相关图片的搜索 relatedPic
 */
package edu.zju.tcmsearch.lucene.search;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import edu.zju.tcmsearch.picGenerator.PictureInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.search.Hits;

import edu.zju.tcmsearch.common.domain.DartOntology;
import edu.zju.tcmsearch.exception.lucene.TcmLuceneException;

public class SearchResults implements PageList {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(SearchResults.class);


	private ArrayList<PictureInfo> pictureList=new ArrayList<PictureInfo>();
    private Hits hits;

    private int pageSize = 30;

    private List<Integer> pageIdList = new ArrayList<Integer>();

    private QueryStorer queryStorer;

    private int curPageId;

    /**
     * @return Returns the curPageId.
     */
    public int getCurPageId() {
        return curPageId;
    }

    /**
     * @param curPageId
     *            The curPageId to set.
     */
    public void setCurPageId(int curPageId) {
        this.curPageId = curPageId;
        pageIdList.clear();
        for (int i = 1; i <= getPageCount(); i++) {
            if (getPageCount()<=10){
                pageIdList.add(i);
                continue;
            }
            if (i<=5 || i%computeSpan()==0 || i==getPageCount()){//四种情况下要将pageId加进去:id<=5, id与当前pageId距离<=2, id是span的倍数, id 是最大的page
              pageIdList.add(i);
            }
        }
    }

    private int computeSpan() {
        int span=10;

        if (getPageCount()<=100){
            span=10;
        }
//        else if (getPageCount()<=500){
//            span=50;
//        }
//        else if (getPageCount()<=1000){
//            span=100;
//        }
        else {
            span=getPageCount()/10;
        }
        return span;
    }   
    

    public int getPrevPageId() {
        return curPageId - 1;
    }

    public int getNextPageId() {
        return curPageId + 1;
    }
    
    public boolean isCurPage(int pageId){
        return curPageId==pageId;
    }
    
    public boolean isFirstPage(){
        return curPageId==1;
    }
    
    public boolean isLastPage(){
        return curPageId==getPageCount();
    }

    /**
     * @param pageSize
     *            The pageSize to set.
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public SearchResults(Hits hits, QueryStorer queryStorer) {
        super();

        this.hits = hits;
        this.queryStorer = queryStorer;
    }

    /**
     * @return Returns the hits.
     */
    public Hits getHits() {
        return hits;
    }

    /**
     * @param hits
     *            The hits to set.
     */
    public void setHits(Hits hits) {
        this.hits = hits;
    }

    public int getResultCount() {
        return this.hits.length();
    }

    public int getPageCount() {
        int pageCount = getResultCount() / getPageSize();
        if (getResultCount() % getPageSize() != 0) {
            pageCount++;
        }
        return pageCount;
    }

    public List<Integer> getPageIdList() {
	    return pageIdList;
	}

	public int getPageSize() {
        return this.pageSize;
    }

    public List<SearchResult> getPage(int pageId) {
        List<SearchResult> resultList = new ArrayList<SearchResult>();

        for (int i = getFirstRecordPos(pageId); i <= getLastRecordPos(pageId); i++) {
            SearchResult searchResult = getPosSearchResult(i);
            resultList.add(searchResult);
        }

        return resultList;
    }

	private SearchResult getPosSearchResult(int i)  {
		try {
			SearchResult searchResult =null;
			//如果是专题搜索，则生成AdvancedSearchResult;否则生成普通的SearchResult
			if(queryStorer.getQueryInfo("belongToTheme")==null)
				searchResult= new SearchResult();
			else
				searchResult= new AdvancedSearchResult();
			searchResult.setDocId(hits.id(i));
			searchResult.setDocument(hits.doc(i));
			searchResult.setScore(hits.score(i));
			searchResult.setQueryStorer(queryStorer);
			return searchResult;
		} catch (IOException e) {
			throw new TcmLuceneException(
                    "error while retrieve documnet from hits!", e);
		}
	}

    public boolean isLastPage(int pageId) {
        return pageId <= getPageCount();
    }

    public boolean isFirstPage(int pageId) {
        return pageId == 1;// id从1开始分页
    }

    private int getFirstRecordPos(int pageId) {
        return (pageId - 1) * pageSize;
    }

    private int getLastRecordPos(int pageId) {
        if ((pageId * pageSize) > getResultCount()) {
            return getResultCount() - 1;
        } else {
            return pageId * pageSize - 1;
        }
    }

    /**
     * 从所有hits中抽样出部分的searchResult,作为统计用,由getHitOntoList()调用
     */
    private List<SearchResult> getSelectResultList(){
    	int hitCount=300;
    	int step=1;
    	if (getResultCount()>hitCount){
    		step=getResultCount()/hitCount;
    	}

    	List<SearchResult> resultList=new ArrayList<SearchResult>();
    	for (int i=0;i<getResultCount();i+=step){
    		SearchResult searchResult=getPosSearchResult(i);
    		resultList.add(searchResult);
    	}
    	return resultList;
    }
    /**
     * 取样搜索到的本体信息列表
     * @return
     */
    public List<HitOntology> getHitOntoList(){
    	Map<String,HitOntology> hitOntoMap=new HashMap<String,HitOntology>();
    	for (SearchResult searchResult:getSelectResultList()){
    		List<DartOntologyInfo> dartOntoList=searchResult.getOntologyInfos();
    		for (DartOntologyInfo dartOntologyInfo:dartOntoList){
    			HitOntology hitOntology=hitOntoMap.get(dartOntologyInfo.getOntoIdentity());
    			if (null==hitOntology){
    				DartOntology dartOnto = dartOntologyInfo.getDartOntology();
    				if(dartOnto == null){//bugfix -- by mike & tjm 2006-4-11 @Railway z21 from Beijing2Shanghai 
    					logger.error("Can't find ontology in search result: " + dartOntologyInfo.getOntoIdentity());
    				}else{
    					hitOntology=new HitOntology(dartOnto);  
    					hitOntoMap.put(hitOntology.getOntoIdentity(),hitOntology);
    					hitOntology.incCount();
    	    			hitOntology.incScore(searchResult.getScore());
    				}
    			}else{
	    			hitOntology.incCount();
	    			hitOntology.incScore(searchResult.getScore());
    			}
    		}
    	}
    	List hitOntoList=new ArrayList<HitOntology>(hitOntoMap.values());
    	Collections.sort(hitOntoList);  
    	Collections.reverse(hitOntoList);
    	return hitOntoList;
    }

	public ArrayList<PictureInfo> getPictureList() {
		return pictureList;
	}

	public void setPictureList(ArrayList<PictureInfo> pictureList) {
		this.pictureList = pictureList;
	}
    
}
