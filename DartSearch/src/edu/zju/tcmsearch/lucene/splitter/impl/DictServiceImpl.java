/*
 * Created on 2005-12-30
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.splitter.impl;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.zju.tcmsearch.exception.lucene.SerializeLuceneException;
import edu.zju.tcmsearch.lucene.splitter.DictService;
import edu.zju.tcmsearch.lucene.splitter.DictTree;
import edu.zju.tcmsearch.lucene.splitter.dao.DictDAO;
import edu.zju.tcmsearch.lucene.splitter.data.DictNode;
import edu.zju.tcmsearch.lucene.splitter.data.DictValue;

public class DictServiceImpl implements DictService {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger
            .getLogger(DictServiceImpl.class);


    private DictTree dictTree;
    
    private String dictDir;
    


    /**
     * @return Returns the dictDir.
     */
    public String getDictDir() {
        return dictDir;
    }

    /**
     * @param dictDir The dictDir to set.
     */
    public void setDictDir(String dictDir) {
        this.dictDir = dictDir;
    }

    /**
     * @return Returns the dictTree.
     */
    public DictTree getDictTree() {
        return dictTree;
    }

    /**
     * @param dictTree
     *            The dictTree to set.
     */
    public void setDictTree(DictTree dictTree) {
        this.dictTree = dictTree;
    }


    
    private String getSerFileName(String strPrefix){
        String dirName=getDictDir()+"/"+strPrefix.hashCode()/100;
        File dir=new File(dirName);
        if (!dir.exists()){
            dir.mkdir();
        }        
        String fileName=dirName+"/"+strPrefix+".ser";
        return fileName;
    }

    public DictNode saveNode(String strPrefix) {
        DictNode dictNode = getDictTree().buildSubNodes(strPrefix);
        String fileName = getSerFileName(strPrefix);
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(dictNode);
            fos.close();
        } catch (FileNotFoundException e) {
            throw new SerializeLuceneException("file not find: " + fileName, e);
        } catch (IOException e) {
            throw new SerializeLuceneException(
                    "IOException on write serialize file: " + fileName, e);
        }
        return dictNode;
    }

    public DictNode readNode(String strPrefix) {
        String fileName = getSerFileName(strPrefix);
        try {
            FileInputStream fis = new FileInputStream(fileName);

            ObjectInputStream ois = new ObjectInputStream(fis);

            DictNode dictNode = (DictNode) ois.readObject();
            return dictNode;
        } catch (FileNotFoundException e) {
           // logger.debug("file not find on deserliaze: " + fileName);
            return null;
        } catch (IOException e) {
            throw new SerializeLuceneException(
                    "IOException on write serialize file: " + fileName, e);
        } catch (ClassNotFoundException e) {
            throw new SerializeLuceneException(
                    "class not found on write serialize: " + DictNode.class, e);
        }
    }

    public void saveAllNodeToFile() {
        Set<String> prefixSet=dictTree.getAllPrefixes();
        for (String strPrefix:prefixSet){
            if (isValidPrefix(strPrefix)){
                saveNode(strPrefix); 
            }
        }        
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
        if (Character.isLetter(prefixChar[0]) || Character.isDigit(prefixChar[0])){
            return true;
        }
        return false;
    }

}
