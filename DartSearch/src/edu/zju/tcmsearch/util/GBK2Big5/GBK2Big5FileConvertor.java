package edu.zju.tcmsearch.util.GBK2Big5;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;

public class GBK2Big5FileConvertor {
	/**
	 * 
	 * @param src  File must be a file not a directory
	 * @param dest File must be a file not a directory
	 * @param encoding
	 */
	public static void convert(File src,File dest,String encoding){
		try {
			FileInputStream fis = new FileInputStream(src);
			InputStreamReader reader = new InputStreamReader(fis,encoding);
			FileOutputStream fos = new FileOutputStream(dest);
			OutputStreamWriter writer = new OutputStreamWriter(fos,encoding);
			char buf[]=new char[102400];
			while(reader.read(buf)>0){
				String s = new String(buf);
				String o = GB2Big5.getInstance().simple2traditional(s,encoding);
				writer.write(o);
			}
			reader.close();
			writer.close();
			fis.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public static void convertAll(File srcDir,File destDir,String encoding){
		if(srcDir.isDirectory()){
			for(File srcSub:srcDir.listFiles()){
				File destSub = new File(destDir,srcSub.getName());
				if(srcSub.isDirectory()){
					destSub.mkdir();
					convertAll(srcSub,destSub,encoding);
				}else{
					convert(srcSub,destSub,encoding);
				}
					
			}
		}else{
			convert(srcDir,destDir,encoding);
		}
	}

}
