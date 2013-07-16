package dartie.xml;

import java.io.File;
import java.util.HashMap;
import java.io.FileFilter;

import dartie.ontology.rdf.impl.RdfsParser;

/**
 * @author eric
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class XmlGeneration {

	public static void main(String[] args){
		HashMap hash = new HashMap();
		File dir = new File("WEB-INF\\classes\\etc\\DartCore\\store\\ontology");
		FileFilter ff = new FileFilter(){
			public boolean accept(File pathname){
				return pathname.getAbsolutePath().endsWith(".rdfs");
			}
		};
		File[] files = dir.listFiles(ff);
		for(int i=0;i<files.length;i++){
			hash.put("rdf"+i,files[i].getAbsolutePath());
			System.out.println(files[i].getAbsolutePath()+" ...");
		}
		
		RdfsParser rdfsParser = new RdfsParser();
		rdfsParser.initXml(hash);
		rdfsParser.saveClassAsXMLFile("dartie\\Class.xml");
    }
}
