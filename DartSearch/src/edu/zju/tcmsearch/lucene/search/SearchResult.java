/*
 * Created on 2005-12-19
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.search;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.StringReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;

import edu.zju.tcmsearch.common.ApplicationContextHolder;
import edu.zju.tcmsearch.common.domain.DartOntology;
import edu.zju.tcmsearch.common.service.OntoService;
import edu.zju.tcmsearch.exception.lucene.TcmLuceneException;
import edu.zju.tcmsearch.lucene.config.LuceneConfig;
import edu.zju.tcmsearch.util.dartcore.OntoUriParseUtil;

/**
 * SearchResult的主要属性如下：
 * docId:  
 * document:
 * queryStorer:
 * score:
 * @author ddream
 *
 */
public class SearchResult {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(SearchResult.class);

    private Document document;

    private double score;

    int docId;

    private QueryStorer queryStorer;

    /**
     * @return Returns the docId.
     */
    public int getDocId() {
        return docId;
    }

    /**
     * @param docId
     *            The docId to set.
     */
    public void setDocId(int docId) {
        this.docId = docId;
    }

    /**
     * @return Returns the document.
     */
    public Document getDocument() {
        return document;
    }

    /**
     * @param document
     *            The document to set.
     */
    public void setDocument(Document document) {
        this.document = document;
    }

    /**
     * @return Returns the score.
     */
    public double getScore() {
        return score;
    }

    public String getScoreStr() {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(3);
        return nf.format(getScore());
    }

    /**
     * @param score
     *            The score to set.
     */
    public void setScore(double score) {
        this.score = score;
    }

    public String getShowContent() {
        return getDocument().getField("showContent").stringValue();
    }

    public String getFieldContent() {
        return getDocument().getField("fieldContent").stringValue();
    }

    public String getClobContent() {
        return getDocument().getField("clobContent").stringValue();
    }

    public String getPrimaryKey() {
        return getDocument().getField("primaryKey").stringValue();
    }  


    public String getTableName() {
        return getDocument().getField("tableName").stringValue();
    }

    public String getTableSchema() {
        return getDocument().getField("tableSchema").stringValue();
    }

    public String getTableIdentity() {
        return getTableSchema() + "." + getTableName();
    }

    public String getOntoIdentityStr() {
        return getDocument().getField("ontoIdentity").stringValue();
    }

    public List<String> getOntoIdenties() {
        return OntoUriParseUtil.parseOntoInfos(getOntoIdentityStr());
    }

    public List<DartOntologyInfo> getOntologyInfos() {
        List<DartOntologyInfo> dartOntoList = new ArrayList<DartOntologyInfo>();
        for (String ontoIdentity : getOntoIdenties()) {
            DartOntologyInfo dartOntoInfo = new DartOntologyInfo();
            dartOntoInfo.setOntoIdentity(ontoIdentity);
            dartOntoInfo.setOntoName(OntoUriParseUtil
                    .getOntoNameById(ontoIdentity));
            dartOntoInfo.setOntoUri(OntoUriParseUtil.getOntoUri(ontoIdentity));
            // dartOntoInfo.setDartOntology(getOntoService().getInstanceOntology(ontoIdentity));
            dartOntoList.add(dartOntoInfo);
        }
        return dartOntoList;
    }

    public OntoService getOntoService() {
        return (OntoService) ApplicationContextHolder.getContext().getBean(
                "ontoService");
    }
    
    public LuceneConfig getLuceneConfig(){
        return (LuceneConfig) ApplicationContextHolder.getContext().getBean(
        "luceneConfig");
    }

    public Analyzer getAnalyzer() {
        return (Analyzer) ApplicationContextHolder.getContext().getBean(
                "highlightAnalyzer");
    }

    public List<DartOntology> getInstanceOntologies() {
        List<DartOntology> dartOntoList = new ArrayList<DartOntology>();
        for (String dartOntoIdenty : getOntoIdenties()) {
            dartOntoList.add(getOntoService().getInstanceOntology(
                    dartOntoIdenty));
        }
        return dartOntoList;
    }

    /**
     * @return Returns the queryStorer.
     */
    public QueryStorer getQueryStorer() {
        return queryStorer;
    }

    /**
     * @param queryStorer
     *            The queryStorer to set.
     */
    public void setQueryStorer(QueryStorer queryStorer) {
        this.queryStorer = queryStorer;
    }
    
    private String getHighlightShowContent(int fragmentSize,int bestFragCount){
        String text=getShowContent();
        if (null == this.queryStorer) {
            logger.info("Can not retrieve query since QueryStorer is null, thus can not highlight!");
            return text;
        }
        Query query = this.queryStorer.getHighlightQuery();//显示highlight时本体部分是不需要highlight的,因此专门定义了HighlighQuery将本体那部分的highlight滤掉
        QueryScorer scorer = new QueryScorer(query);
        SimpleHTMLFormatter formatter = new SimpleHTMLFormatter(
                "<span class=\""+getLuceneConfig().getHightLightCss()+"\">", "</span>");
        Highlighter highlighter = new Highlighter(formatter, scorer);
        Fragmenter fragmenter = new SimpleFragmenter(fragmentSize);
        highlighter.setTextFragmenter(fragmenter);
        
        TokenStream tokenStream = getAnalyzer().tokenStream("showContent",
                new StringReader(text));
        String result;

        try {
            result = highlighter.getBestFragments(tokenStream, text, bestFragCount,
                    "...");
        } catch (IOException e) {
            throw new TcmLuceneException("IO error while get highlighter!");
        }
        return result;        
    }

    public String getHighlightShowContent() {
        return getHighlightShowContent(100,5);
    }
    
    public String getTitle() {
        return getHighlightShowContent(50,1);
    }    
}
