package edu.zju.tcmsearch.lucene.search;

import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.HitCollector;
import java.util.List;
import java.util.ArrayList;

public class PagedTopFieldDocCollector extends HitCollector {
	  private int startIndex;
	  private int count=0;
	  private int numHits;
	  private float minScore = 0.0f;

	  int totalHits;
	  List<ScoreDoc> sDocs;
	    

	  public PagedTopFieldDocCollector(int numHits,int startIndex) {
	    this(numHits, startIndex,new ArrayList<ScoreDoc>());
	  }

	  public PagedTopFieldDocCollector(int numHits,int startIndex,List<ScoreDoc> sDocs){
	    this.numHits = numHits;
	    this.sDocs = sDocs;
	  }


	  public void collect(int doc, float score) {
	    if (score > 0.0f) {
	    	count++;
	    	if(count>=startIndex && sDocs.size()<=numHits)
	        {
	    		sDocs.add(new ScoreDoc(doc, score));
	        }
	    }
	    this.totalHits = sDocs.size();
	  }

	  /** The total number of documents that matched this query. */
	  public int getTotalHits() { return totalHits; }

	  /** The top-scoring hits. */
	  public List<ScoreDoc> getDocs() {
	    return sDocs;
	  }
	
}
