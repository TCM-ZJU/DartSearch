package edu.zju.tcmsearch.lucene.config.impl;

import edu.zju.tcmsearch.lucene.config.IIndexDirectory;

import org.apache.log4j.Logger;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
/**
 * 索引目录管理，在indexDirBase下建立两个子目录：一个作为当前索引目录，另一个作为新建索引的目录， statusFileName纪录含有最新索引的子目录
 */
public class IndexDirectory implements IIndexDirectory {

	private String indexDirBase;
	private String statusFileName;

    private static final Logger logger = Logger.getLogger(IndexDirectory.class);	
	
	public String getIndexDirectory() {
		// TODO Auto-generated method stub
		int id = latestIndexDir();
		return indexDirBase+"\\sub"+id;
	}

	public String getIndexBuildDirectory() {
		// TODO Auto-generated method stub
		int id = latestIndexDir();
		return id==1 ? indexDirBase+"\\sub"+2 : indexDirBase+"\\sub"+1;
	}

	public void setIndexDirBase(String dir) {
		indexDirBase = dir;
	}
	
	public void setStatusFile(String filename) {
		statusFileName = filename;
	}
	
    protected int latestIndexDir(){
        File statusFile = new File(indexDirBase+"\\"+statusFileName);
        String subdir1=indexDirBase+"\\sub"+1;
        String subdir2=indexDirBase+"\\sub"+2;
        File baseDir = new File(indexDirBase);
        File dir1 = new File(subdir1);
        File dir2 = new File(subdir2);
        
        if(!baseDir.exists()){
        	baseDir.mkdir();
        }
        if(!dir1.exists() && dir2.exists()){
        	dir1.mkdir();
        	return 2;
        }else if(dir1.exists() && !dir2.exists()){
        	dir2.mkdir();
        	return 1;
        }else if(!dir1.exists() && !dir2.exists()){
        	dir1.mkdir();
        	dir2.mkdir();
        	return 1;
        }
        
        try{
         FileReader reader = new FileReader(statusFile);
         char[] buf= new char["latest index dir = 1".length()];
         reader.read(buf,0,buf.length);
         String str = new String(buf);
         if(str.compareTo("latest index dir = 1")==0){
        	 return 1;
         }else if(str.compareTo("latest index dir = 2")==0){
        	 return 2;
         }else {
        	 return 1;
         }
        }catch (FileNotFoundException e){
        	logger.info("status file does not exist , use "+indexDirBase+"\\sub"+1 + " as latest index dir.");
        	try{
        	statusFile.createNewFile();
        	FileWriter writer = new FileWriter(statusFile);
        	writer.write("latest index dir = 1");
        	writer.close();
        	}catch(Exception exception){}
        	return 1;        	
        }catch (IOException e){
        	logger.info("status file read error , use "+indexDirBase+"\\sub"+1 + " as latest index dir.");
        	return 1;        	
        }
    }
    
    /**
     * 目录切换，新的索引建立以后，将新的索引目录设置为当前索引目录。
     */
    public void setIndexDirectory(String dir){
    	File statusFile = new File(indexDirBase+"\\"+statusFileName);
    	try{
        FileWriter writer = new FileWriter(statusFile);
        String subdir1=indexDirBase+"\\sub"+1;
        String subdir2=indexDirBase+"\\sub"+2;
        String latestDir ="latest index dir = X";
        if(dir.compareTo(subdir1)==0){
        	latestDir = "latest index dir = 1";
        }else if(dir.compareTo(subdir2)==0){
        	latestDir = "latest index dir = 2";
        }
        writer.write(latestDir);
        writer.close();
    	}catch (IOException e){
    		logger.info("status file can not be wirtten");
    	}
           
    }
}
