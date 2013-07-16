/*
 * Created on 2005-12-16
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.analysis;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import junit.framework.Assert;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

/**一个用于分词的类
 * 读入字符串，分成词符
 * @author zhm
 *
 */
public class AnalyzerUtils {
	
	
    /**利用Analyzer来实现分词，分词后的结果曾在token中，token是一个词符单元，不含标点符合和空格
     * Analyzer是一个抽象类，有很多实现，不同的实现，其分词效果不一样
     * @param analyzer
     * @param text
     * @return  an array of Token. Token is a an object that  use to show a single serial 
     * @throws IOException
     */
    public static Token[] tokensFromAnalysis(Analyzer analyzer, String text)
            throws IOException {    	
        TokenStream stream = analyzer.tokenStream("contents", new StringReader(
                text));
        ArrayList tokenList = new ArrayList();
        while (true) {
            Token token = stream.next();
            if (token == null)
                break;

            tokenList.add(token);
        }       
        return (Token[]) tokenList.toArray(new Token[0]);
    }

    /**分词，并显示分词结果。
     * @param analyzer
     * @param text
     * @throws IOException
     */
    public static void displayTokens(Analyzer analyzer, String text)
            throws IOException {
        Token[] tokens = tokensFromAnalysis(analyzer, text);

        for (int i = 0; i < tokens.length; i++) {
            Token token = tokens[i];

            System.out.print("[" + token.termText() + "] ");
        }
    }

    /**分词并显示分词后，每个词符的位置。
     * @param analyzer
     * @param text
     * @throws IOException
     */
    public static void displayTokensWithPositions(Analyzer analyzer, String text)
            throws IOException {
        Token[] tokens = tokensFromAnalysis(analyzer, text);

        int position = 0;

        for (int i = 0; i < tokens.length; i++) {
            Token token = tokens[i];

            int increment = token.getPositionIncrement();

            if (increment > 0) {
                position = position + increment;
                System.out.println();
                System.out.print(position + ": ");
            }

            System.out.print("[" + token.termText() + "] ");
        }
        System.out.println();
    }

    /**分词并显示分词后，每个分词的详细信息。比displayTokens（）更加详细。
     * @param analyzer
     * @param text
     * @throws IOException
     */
    public static void displayTokensWithFullDetails(Analyzer analyzer,
            String text) throws IOException {
        Token[] tokens = tokensFromAnalysis(analyzer, text);

        int position = 0;

        for (int i = 0; i < tokens.length; i++) {
            Token token = tokens[i];

            int increment = token.getPositionIncrement();

            if (increment > 0) {
                position = position + increment;
                System.out.println();
                System.out.print(position + ": ");
            }

            System.out.print("[" + token.termText() + ":" + token.startOffset()
                    + "->" + token.endOffset() + ":" + token.type() + "] ");
        }
        System.out.println();
    }

    public static void assertTokensEqual(Token[] tokens, String[] strings) {
        Assert.assertEquals(strings.length, tokens.length);

        for (int i = 0; i < tokens.length; i++) {
            Assert.assertEquals("index " + i, strings[i], tokens[i].termText());
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("SimpleAnalyzer");
//        用SimpleAnalyzer（）分词的结果：
//        1: [学习外语的乐园:0->7:word] 
//            2: [英语:8->10:word] 
//            3: [法语:11->13:word] 
//            4: [德语:14->16:word] 
//            5: [俄语:17->19:word] 
//            6: [专业外语等:20->25:word] 
        
        displayTokensWithFullDetails(new SimpleAnalyzer(),
                "学习外语的乐园，英语、法语、德语、俄语，专业外语等");
        displayTokensWithPositions(new SimpleAnalyzer(),
        "学习外语的乐园，英语、法语、德语、俄语，专业外语等");
        System.out.println("\n----");
        System.out.println("StandardAnalyzer");
        
        
//        用StandardAnalyzer分词的结果：
//        	  1: [学:0->1:<CJ>] 
//            2: [习:1->2:<CJ>] 
//            3: [外:2->3:<CJ>] 
//            4: [语:3->4:<CJ>] 
//            5: [的:4->5:<CJ>] 
//            6: [乐:5->6:<CJ>] 
//            7: [园:6->7:<CJ>] 
//            8: [英:8->9:<CJ>] 
//            9: [语:9->10:<CJ>] 
//            10: [法:11->12:<CJ>] 
//            11: [语:12->13:<CJ>] 
//            12: [德:14->15:<CJ>] 
//            13: [语:15->16:<CJ>] 
//            14: [俄:17->18:<CJ>] 
//            15: [语:18->19:<CJ>] 
//            16: [专:20->21:<CJ>] 
//            17: [业:21->22:<CJ>] 
//            18: [外:22->23:<CJ>] 
//            19: [语:23->24:<CJ>] 
//            20: [等:24->25:<CJ>] 
                 
        displayTokensWithFullDetails(new StandardAnalyzer(),
                "学习外语的乐园，英语、法语、德语、俄语，专业外语等");
        displayTokensWithPositions(new StandardAnalyzer(),
                "学习外语的乐园，英语、法语、德语、俄语，专业外语等");
    }
}
