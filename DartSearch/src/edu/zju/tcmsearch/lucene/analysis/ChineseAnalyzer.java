/*
 * Created on 2005-12-28
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.analysis;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LetterTokenizer;
import org.apache.lucene.analysis.TokenStream;

import edu.zju.tcmsearch.lucene.splitter.Splitter;

/**具体实现了抽象类Analyzer
 * 实现了中文词法分析器
 * Analyzer的作用可以在AnalyzerUtil方法中看到。
 * @author zhm
 *
 */
public class ChineseAnalyzer  extends Analyzer{
    private List<String> filterTagList = new ArrayList<String>();
    private Splitter splitter;
    
    /**
     * @return Returns the splitter.
     */
    public Splitter getSplitter() {
        return splitter;
    }

    /**
     * @param splitter The splitter to set.
     */
    public void setSplitter(Splitter splitter) {
        this.splitter = splitter;
    }

    /**
     * @return Returns the filterTagList.
     */
    public List<String> getFilterTagList() {
        return filterTagList;
    }

    /**
     * @param filterTagList The filterTagList to set.
     */
    public void setFilterTagList(List<String> filterTagList) {
        this.filterTagList = filterTagList;
    }


    public TokenStream tokenStream(String fieldName, Reader reader) {
        Assert.assertNotNull(splitter);
        return new ChineseTokenFilter(new LetterTokenizer(reader),splitter,filterTagList);
      }
}
