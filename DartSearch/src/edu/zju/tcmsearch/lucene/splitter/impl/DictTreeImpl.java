/*
 * Created on 2005-12-28
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.splitter.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;
import org.testng.annotations.Test;

import edu.zju.tcmsearch.exception.lucene.SerializeLuceneException;
import edu.zju.tcmsearch.lucene.splitter.DictService;
import edu.zju.tcmsearch.lucene.splitter.DictTree;
import edu.zju.tcmsearch.lucene.splitter.dao.DictDAO;
import edu.zju.tcmsearch.lucene.splitter.data.DictNode;
import edu.zju.tcmsearch.lucene.splitter.data.DictValue;

public class DictTreeImpl implements DictTree {

    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(DictTreeImpl.class);

    private Map<String, DictNode> dictMap = new HashMap<String, DictNode>();

    private boolean rebuild;

    private String fileName;

    private List<DictDAO> dictDAOList;

    private boolean preload = false;

    private Set<String> prefixSet;

    private int maxNodeId = 1;

    private int maxWordLength =10;// 词典里最大词的默认长度,如果超过这个长度就略掉了.因为不会有人查20个以上的词,还会导致树的高度太高

    /**
     * @return Returns the maxWordLength.
     */
    public int getMaxWordLength() {
        return maxWordLength;
    }

    /**
     * @param maxWordLength
     *            The maxWordLength to set.
     */
    public void setMaxWordLength(int maxWordLength) {
        this.maxWordLength = maxWordLength;
    }

    /**
     * @return Returns the dictDAOList.
     */
    public List<DictDAO> getDictDAOList() {
        return dictDAOList;
    }

    /**
     * @param dictDAOList
     *            The dictDAOList to set.
     */
    public void setDictDAOList(List<DictDAO> dictDAOList) {
        this.dictDAOList = dictDAOList;
    }

    /**
     * @return Returns the rebuild.
     */
    public boolean isRebuild() {
        return rebuild;
    }

    /**
     * @param rebuild
     *            The rebuild to set.
     */
    public void setRebuild(boolean rebuild) {
        this.rebuild = rebuild;
    }

    /**
     * @return Returns the dictMap.
     */
    public Map<String, DictNode> getDictMap() {
        return dictMap;
    }

    /**
     * @param dictMap
     *            The dictMap to set.
     */
    public void setDictMap(Map<String, DictNode> dictMap) {
        this.dictMap = dictMap;
    }

    public DictNode getDictNode(String strPrefix) {
        return dictMap.get(strPrefix);
    }

    public void putNode(DictNode dictNode) {
        dictMap.put(dictNode.getValue(), dictNode);
    }

    public List<DictValue> getDictValues(String strPrefix) {
       // Set<DictValue> dictValues = new HashSet<DictValue>();
        List<DictValue> dictValues=null;
        for (DictDAO dictDAO : dictDAOList) {
            if (null==dictValues){
                dictValues=dictDAO.getDictValues(strPrefix);                
            }
            else{
              dictValues.addAll(dictDAO.getDictValues(strPrefix));
            }
        }
        return dictValues;
    }

    public DictNode loadDictNodes(String strPrefix) {
        loadDictMap();
        DictNode rootNode = getDictNode(strPrefix);
        if (null != rootNode) {
            return rootNode;
        }
        return rootNode;
    }

    private boolean isValidPrefix(String strPrefix) {
        if (null==strPrefix){
            return false;
        }
        char[] prefixChar=strPrefix.toCharArray();
        if (prefixChar.length!=1){
            logger.debug("wrong prefix, lengh!=1: "+strPrefix);
            return false;
        }
        if (Character.isLetter(prefixChar[0])){ //|| Character.isDigit(prefixChar[0])){
            return true;
        }
        return false;
    }
    /*
     * (non-Javadoc)
     * 
     * @see edu.zju.tcmgrid2.lucene.splitter.impl.DictTree#buildSubNodes(java.lang.String)
     */
    public DictNode buildSubNodes(String strPrefix) {
        if (null == strPrefix || !isValidPrefix(strPrefix)) {
            return null;
        }
        DictNode rootNode = getDictNode(strPrefix);
        if (null != rootNode) {
            return rootNode;
        }
        List<DictValue> dictValueList = getDictValues(strPrefix);
        if (0 == dictValueList.size()) {
            return null;
        }
        // logger.debug("prefix: "+strPrefix);
        rootNode = new DictNode(1, strPrefix, DictNode.EMPTY_NODE, maxNodeId++);
        putNode(rootNode);

        DictNode dictNode = rootNode;

        for (DictValue dictValue : dictValueList) {
            if (dictValue.length() > getMaxWordLength()) {
                continue;
            }
            if (dictValue.getValue().equals(strPrefix)) {// 首字作为一词的情况
                rootNode.setEnd(true);
                rootNode.setUseCount(dictValue.getUseCount());
            }
            DictNode parentNode = dictNode;
            dictValue.setCurPos(strPrefix.length());// 前面值是已经build过的,所以先跳过
            String curStr = "";
            int level = 1;
            while (true) {
                level++;
                curStr = dictValue.nextValue();
                if (null == curStr) {
                    break;
                }
                DictNode curDictNode = parentNode.getChildNode(curStr);
                if (curDictNode == null) {
                    curDictNode = new DictNode(level, curStr, parentNode,
                            maxNodeId++);
                    parentNode.addChildNode(curDictNode);
                }

                if (dictValue.isEnd()) {
                    // logger.debug("dict value is "+dictValue);
                    curDictNode.setEnd(true);
                    curDictNode.setUseCount(dictValue.getUseCount());
                    // logger.debug("dict node is "+curDictNode);
                }
                parentNode = curDictNode;
            }
        }
        dictValueList=null;
        return dictNode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.zju.tcmgrid2.lucene.splitter.impl.DictTree#buildOrGetSubNodes(java.lang.String,
     *      edu.zju.tcmgrid2.lucene.splitter.data.DictNode)
     */
    public DictNode buildOrGetSubNodes(String strPrefix, DictNode parentNode) {
        if (parentNode.getLevel() == 0) {
            if (isRebuild()) {
                return buildSubNodes(strPrefix);
            } else {
                return loadDictNodes(strPrefix);
            }
        }
        return parentNode.getChildNode(strPrefix);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.dictMap.toString();
    }

    public DictNode getRootNode(String strPrefix) {
        return getDictNode(strPrefix);
    }

    /**
     * @return Returns the fileName.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName
     *            The fileName to set.
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void saveDictMap() {
        int i = 0;
        maxNodeId = 1;
        logger.debug("start build Dict");
        for (String strPrefix : getAllPrefixes()) {
            // logger.debug("prefix: "+strPrefix);
            i++;
            //logger.debug("start put node : "+i+" prefix: "+strPrefix);            
            DictNode dictNode = buildSubNodes(strPrefix);
            if (null != dictNode) {
                putNode(dictNode);
                // logger.debug("put dictNode: "+dictNode);
            }
        }
        logger.debug("end build Dict!");
        // testBuildAndGetSubList();

        this.prefixSet=null;//用完后赶快释放
        
        // logger.debug("dict node size: "+i);
        logger.debug("start save Dict!");
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.dictMap);
            fos.close();
        } catch (FileNotFoundException e) {
            throw new SerializeLuceneException("file not find: " + fileName, e);
        } catch (IOException e) {
            throw new SerializeLuceneException(
                    "IOException on write serialize file: " + fileName, e);
        }
        logger.debug("end save Dict!");
    }


    public Set<String> getAllPrefixes() {
        if (null != prefixSet) {
            return prefixSet;
        }
        prefixSet = new HashSet<String>();
        for (DictDAO dictDAO : dictDAOList) {
            List<String> prefixList = dictDAO.getAllPrefixes();
            // logger.debug("prefixList size: "+prefixList.size());
            prefixSet.addAll(prefixList);
            prefixList=null;
        }
        // logger.debug("prefixSet size: "+prefixSet.size());
        return prefixSet;
    }

    public Map<String, DictNode> loadDictMap() {
        if (preload) {
            if (this.dictMap.size() == 0) {
                logger.warn("dict Map size is 0! something is wrong!");
            }
            return this.dictMap;
        }
        try {
            FileInputStream fis = new FileInputStream(fileName);

            ObjectInputStream ois = new ObjectInputStream(fis);

            this.dictMap = (Map<String, DictNode>) ois.readObject();
            preload = true;
            if (this.dictMap.size() == 0) {
                logger.warn("dict Map size is 0! something is wrong!");
            }
            return this.dictMap;
        } catch (FileNotFoundException e) {
            logger.debug("file not find on deserliaze: " + fileName);
            return null;
        } catch (IOException e) {
            throw new SerializeLuceneException(
                    "IOException on write serialize file: " + fileName, e);
        } catch (ClassNotFoundException e) {
            throw new SerializeLuceneException(
                    "class not found on write serialize: " + DictNode.class, e);
        } // TODO Auto-generated method stub
    }

}
