package dartie.ontology.rdf.impl;

import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import dartie.ontology.model.IOntoClass;
import dartie.ontology.model.impl.OntoClass;
import dartie.ontology.rdf.IRdfsParser;
import dartie.ontology.util.Constants;
import dartie.ontology.util.ModelImporter;


public class RdfsParser implements IRdfsParser{

	private String inputFileName;
	private String uri;
	private Map rootClassesMap	= new HashMap();
	private Map allClassesMap 	= new HashMap();
	
	public void init() {
		// create an empty model
        Model model = ModelFactory.createDefaultModel();
        // read the RDF/XML file
        model = ModelImporter.getFromRdfFile(inputFileName);

        ResIterator resIter = model.listSubjectsWithProperty(RDF.type, RDFS.Class);
        while (resIter.hasNext()) {
            Resource r = resIter.nextResource();
            findSuperOnto(r);
        }
        resIter.close();
	}
	
	public void initXml(HashMap hash) {
		// create an empty model
        Model model = ModelFactory.createDefaultModel();
        Iterator iter = hash.values().iterator();
        int count = 1;
        while (iter.hasNext()) {
        	if(count == 1)
        		model = ModelImporter.getFromRdfFile(iter.next().toString());
        	else {
        		Model model1 = ModelFactory.createDefaultModel();
        		model1= ModelImporter.getFromRdfFile(iter.next().toString());
        		model.add(model1);
        	}
        	count++;
        }
        //Model model2 = ModelFactory.createDefaultModel();
        //// read the RDF/XML file
        //model1 = ModelImporter.getFromRdfFile("C:/Documents and Settings/eric/桌面/tcmsearch/WEB-INF/classes/etc/DartCore/store/ontology/dart.rdfs");
        //model2 = ModelImporter.getFromRdfFile("C:/Documents and Settings/eric/桌面/tcmsearch/WEB-INF/classes/etc/DartCore/store/ontology/dart_expriment.rdfs");
        //model1.add(model2);
        ResIterator resIter = model.listSubjectsWithProperty(RDF.type, RDFS.Class);
        while (resIter.hasNext()) {
            Resource r = resIter.nextResource();
            findSuperOnto(r);
        }
        resIter.close();
	}

	private IOntoClass findSuperOnto(Resource resource) {
		IOntoClass oc = new OntoClass();
		oc.setLabel(resource.getLocalName());
		oc.setUri(resource.getURI());
		
		if(!allClassesMap.containsKey(resource.getLocalName())) {
			allClassesMap.put(oc.getLabel(),oc);
		} else {
			return (OntoClass)allClassesMap.get(oc.getLabel());
		}
			
		if(resource.hasProperty(RDFS.subClassOf)) {
			if(!rootClassesMap.containsKey(oc.getLabel())) {
				StmtIterator stmtIter = resource.listProperties(RDFS.subClassOf);
				while(stmtIter.hasNext()){
					Resource r = stmtIter.nextStatement().getResource();
									
					IOntoClass ocParent = findSuperOnto(r);
					ocParent.addSubClass(oc.getLabel(), oc);
				}
				stmtIter.close();
			}
		} else {
			rootClassesMap.put(oc.getLabel(),oc);
		}
		return oc;
	}

	public void showHierarchy() {
		Iterator iter = rootClassesMap.values().iterator();
		while(iter.hasNext()) {
			IOntoClass oc = (OntoClass) iter.next();
        	PrintOntos(oc, 0);
        }
	}
	
	private void PrintOntos(IOntoClass oc, int level) {
		for(int i=0; i<level; i++) 
			System.out.print(" ");
			System.out.println(oc.getLabel());
		Iterator iter = oc.getSubClasses().values().iterator();
		while(iter.hasNext()) {
			IOntoClass subClass = (OntoClass) iter.next();
			PrintOntos(subClass, level+1);
		}
	}

	private Element getChildrenElement(Map map, IOntoClass oc) {
		if(map.size() == 0) {
			// 如果找到叶子节点
			Element ele = DocumentHelper.createElement(Constants.PREFUSE_TREEVIEW_LEAF);
			ele.addElement(Constants.PREFUSE_TREEVIEW_ATTRIBUTE)
				.addAttribute(Constants.PREFUSE_TREEVIEW_NAME, Constants.PREFUSE_TREEVIEW_NAME)
				.addAttribute(Constants.PREFUSE_TREEVIEW_VALUE, oc.getLabel())
				.addAttribute(Constants.PREFUSE_TREEVIEW_URI, oc.getUri())
				.addAttribute(Constants.PREFUSE_TREEVIEW_NODETYPE, Constants.PREFUSE_TREEVIEW_CLASS);
			return ele;
		}
		
		Element nele = DocumentHelper.createElement(Constants.PREFUSE_TREEVIEW_BRANCH);
		nele.addElement(Constants.PREFUSE_TREEVIEW_ATTRIBUTE)
			.addAttribute(Constants.PREFUSE_TREEVIEW_NAME, Constants.PREFUSE_TREEVIEW_NAME)
			.addAttribute(Constants.PREFUSE_TREEVIEW_VALUE, oc.getLabel()+Constants.PREFUSE_SQUARE_BRACKET_LEFT
					+oc.getSubClasses().size()+Constants.PREFUSE_SQUARE_BRACKET_RIGHT)
			.addAttribute(Constants.PREFUSE_TREEVIEW_URI, oc.getUri())
			.addAttribute(Constants.PREFUSE_TREEVIEW_NODETYPE, Constants.PREFUSE_TREEVIEW_CLASS);
		
		Iterator iter = map.values().iterator();
		while(iter.hasNext()) {
			IOntoClass ioc = (OntoClass) iter.next();
			nele.add(getChildrenElement(ioc.getSubClasses(), ioc));
        }
		return nele;
	}

	public void saveClassAsXMLFile(String filePath) {
		Document document = DocumentHelper.createDocument();
        Element root = document.addElement( Constants.PREFUSE_TREEVIEW_TREE );
        
        root.addElement( Constants.PREFUSE_TREEVIEW_DECLARATIONS )
        	.addElement(Constants.PREFUSE_TREEVIEW_ATTRIBUTEDECL)
            .addAttribute( Constants.PREFUSE_TREEVIEW_NAME, Constants.PREFUSE_TREEVIEW_NAME )
            .addAttribute( Constants.PREFUSE_TREEVIEW_TYPE, Constants.PREFUSE_TREEVIEW_STRING );
        
		Iterator iter = rootClassesMap.values().iterator();
		if(iter.hasNext()) {
			IOntoClass oc = (OntoClass) iter.next();
			root.add(getChildrenElement(oc.getSubClasses(), oc));
        }
        
        // write to a file
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding(Constants.SYS_ENCODING);
        try {
	        XMLWriter writer = new XMLWriter(new FileWriter(filePath), format);
	        writer.write( document);
	        writer.close();
        } catch (Exception e) {
        	
        }
	}
	
	public InputStream getClassStream()
	{
		Document document = getClassXMLFile();
        InputStream in = null;
		try {
			in = new ByteArrayInputStream(document.asXML().getBytes(Constants.SYS_ENCODING_UTF8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        return in;
	}
	
	private Document getClassXMLFile() {
		Document document = DocumentHelper.createDocument();
        Element root = document.addElement( Constants.PREFUSE_TREEVIEW_TREE );
        
        root.addElement( Constants.PREFUSE_TREEVIEW_DECLARATIONS )
        	.addElement(Constants.PREFUSE_TREEVIEW_ATTRIBUTEDECL)
            .addAttribute( Constants.PREFUSE_TREEVIEW_NAME, Constants.PREFUSE_TREEVIEW_NAME )
            .addAttribute( Constants.PREFUSE_TREEVIEW_TYPE, Constants.PREFUSE_TREEVIEW_STRING );
        
		Iterator iter = rootClassesMap.values().iterator();
		if(iter.hasNext()) {
			IOntoClass oc = (OntoClass) iter.next();
			root.add(getChildrenElement(oc.getSubClasses(), oc));
        }

		return document;
	}

	/**
	 * @return Returns the inputFileName.
	 */
	public String getInputFileName() {
		return inputFileName;
	}

	/**
	 * @param inputFileName The inputFileName to set.
	 */
	public void setInputFileName(String inputFileName) {
		this.inputFileName = inputFileName;
	}

	public InputStream getInstanceStream() {
		return null;
	}

	public InputStream getPropertyStream() {
		return null;
	}

	public void saveInstanceAsXMLFile(String filePath) {
		
	}

	public void savePropertyAsXMLFile(String filePath) {
		
	}

	/**
	 * @return Returns the uri.
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * @param uri The uri to set.
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

}
