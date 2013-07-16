package dartie.ontology.rdf.impl;

import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
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
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import dartie.ontology.model.IOntoClass;
import dartie.ontology.model.impl.OntoClass;
import dartie.ontology.rdf.IRdfParser;
import dartie.ontology.util.Constants;

public class RdfParser implements IRdfParser{

	private String inputFileName;
	private String uri;
	private Model model = ModelFactory.createDefaultModel();
	private Map rootClassesMap	= new HashMap();
	private Map allClassesMap 	= new HashMap();
	
	public void init() {
		// use the FileManager to find the input file
        InputStream in = FileManager.get().open(inputFileName);
        if (in == null) {
            throw new IllegalArgumentException( "File: " + inputFileName + " not found");
        }        
        // read the RDF/XML file
        model.read( in, "" );
        
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

	private Document getInstanceXMLDocument() {
		Resource startResource = model.getResource(uri);
		Document document = DocumentHelper.createDocument();
        Element root = document.addElement(Constants.PREFUSE_GRAPHVIEW_GRAPHML);
        Element graph= root.addElement(Constants.PREFUSE_GRAPHVIEW_GRAPH).addAttribute(Constants.PREFUSE_GRAPHVIEW_EDGEDEFAULT, Constants.PREFUSE_GRAPHVIEW_UNDIRECTED);
        graph.addElement(Constants.PREFUSE_GRAPHVIEW_KEY)
        	.addAttribute(Constants.PREFUSE_GRAPHVIEW_ID, Constants.PREFUSE_GRAPHVIEW_NAME)
        	.addAttribute(Constants.PREFUSE_GRAPHVIEW_FOR, Constants.PREFUSE_GRAPHVIEW_NODE)
        	.addAttribute(Constants.PREFUSE_GRAPHVIEW_ATTRNAME, Constants.PREFUSE_GRAPHVIEW_NAME)
        	.addAttribute(Constants.PREFUSE_GRAPHVIEW_ATTRTYPE, Constants.PREFUSE_GRAPHVIEW_STRING);
        graph.addElement(Constants.PREFUSE_GRAPHVIEW_NODE)
        	.addAttribute(Constants.PREFUSE_GRAPHVIEW_ID, "n0")
        	.addElement(Constants.PREFUSE_GRAPHVIEW_DATA)
        	.addAttribute(Constants.PREFUSE_GRAPHVIEW_KEY, Constants.PREFUSE_GRAPHVIEW_NAME)
        	.addAttribute(Constants.PREFUSE_GRAPHVIEW_URI, startResource.getURI())
        	.addAttribute(Constants.PREFUSE_GRAPHVIEW_NODETYPE, Constants.PREFUSE_GRAPHVIEW_CLASS)
        	.addText(startResource.getLocalName());
        int i = 1;
		ResIterator resIter = model.listSubjectsWithProperty(RDF.type, startResource);
        while (resIter.hasNext()) {
            Resource r = resIter.nextResource();
            graph.addElement(Constants.PREFUSE_GRAPHVIEW_NODE)
        	.addAttribute(Constants.PREFUSE_GRAPHVIEW_ID,"n"+i)
        	.addElement(Constants.PREFUSE_GRAPHVIEW_DATA)
        	.addAttribute(Constants.PREFUSE_GRAPHVIEW_KEY, Constants.PREFUSE_GRAPHVIEW_NAME)
        	.addAttribute(Constants.PREFUSE_GRAPHVIEW_URI, r.getURI())
        	.addAttribute(Constants.PREFUSE_GRAPHVIEW_NODETYPE, Constants.PREFUSE_GRAPHVIEW_INSTANCE)
        	.addText(r.getLocalName());
            graph.addElement(Constants.PREFUSE_GRAPHVIEW_EDGE)
            	.addAttribute(Constants.PREFUSE_GRAPHVIEW_ID, "e"+i)
            	.addAttribute(Constants.PREFUSE_GRAPHVIEW_SOURCE, "n"+i)
            	.addAttribute(Constants.PREFUSE_GRAPHVIEW_TARGET, "n0");
            i++;
        }
        resIter.close();
        return document;
	}
	
	public Document getPropertyXMLDocument() {
		Resource startResource = model.getResource(uri);
		Document document = DocumentHelper.createDocument();
        Element root = document.addElement(Constants.PREFUSE_GRAPHVIEW_GRAPHML);
        Element graph= root.addElement(Constants.PREFUSE_GRAPHVIEW_GRAPH).addAttribute(Constants.PREFUSE_GRAPHVIEW_EDGEDEFAULT, Constants.PREFUSE_GRAPHVIEW_UNDIRECTED);
        graph.addElement(Constants.PREFUSE_GRAPHVIEW_KEY)
        	.addAttribute(Constants.PREFUSE_GRAPHVIEW_ID, Constants.PREFUSE_GRAPHVIEW_NAME)
        	.addAttribute(Constants.PREFUSE_GRAPHVIEW_FOR, Constants.PREFUSE_GRAPHVIEW_NODE)
        	.addAttribute(Constants.PREFUSE_GRAPHVIEW_ATTRNAME, Constants.PREFUSE_GRAPHVIEW_NAME)
        	.addAttribute(Constants.PREFUSE_GRAPHVIEW_ATTRTYPE, Constants.PREFUSE_GRAPHVIEW_STRING);
        graph.addElement(Constants.PREFUSE_GRAPHVIEW_NODE)
        	.addAttribute(Constants.PREFUSE_GRAPHVIEW_ID, "n0")
        	.addElement(Constants.PREFUSE_GRAPHVIEW_DATA)
        	.addAttribute(Constants.PREFUSE_GRAPHVIEW_KEY, Constants.PREFUSE_GRAPHVIEW_NAME)
        	.addAttribute(Constants.PREFUSE_GRAPHVIEW_URI, startResource.getURI())
        	.addAttribute(Constants.PREFUSE_GRAPHVIEW_NODETYPE, Constants.PREFUSE_GRAPHVIEW_INSTANCE)
        	.addText(startResource.getLocalName());
        StmtIterator iter = startResource.listProperties();
        int i = 1;
        while(iter.hasNext()) {
        	Statement stmt = (Statement)iter.next();
        	Property  predicate = stmt.getPredicate();
        	RDFNode   object    = stmt.getObject();
        	String strProperty = "";
        	String uriProperty = "";
        	String nodeType = "";
        	if (object instanceof Resource) {
        		strProperty = object.asNode().getLocalName();
        		uriProperty = ((Resource)object).getURI();
        		nodeType = Constants.PREFUSE_GRAPHVIEW_INSTANCE;
        		ResIterator resIter = model.listSubjectsWithProperty(RDF.type, model.getResource(uriProperty));
                if(resIter.hasNext()) 
                	nodeType = Constants.PREFUSE_GRAPHVIEW_CLASS;
            } else {
            	strProperty = object.toString();
            	nodeType = Constants.PREFUSE_GRAPHVIEW_PROPERTY;
            }
        	graph.addElement(Constants.PREFUSE_GRAPHVIEW_NODE)
        	.addAttribute(Constants.PREFUSE_GRAPHVIEW_ID,"n"+i)
        	.addElement(Constants.PREFUSE_GRAPHVIEW_DATA)
        	.addAttribute(Constants.PREFUSE_GRAPHVIEW_KEY, Constants.PREFUSE_GRAPHVIEW_NAME)
        	.addAttribute(Constants.PREFUSE_GRAPHVIEW_URI, uriProperty)
        	.addAttribute(Constants.PREFUSE_GRAPHVIEW_NODETYPE, nodeType)
        	.addText(predicate.getLocalName()+":"+strProperty);
            graph.addElement(Constants.PREFUSE_GRAPHVIEW_EDGE)
            	.addAttribute(Constants.PREFUSE_GRAPHVIEW_ID, "n"+i)
            	.addAttribute(Constants.PREFUSE_GRAPHVIEW_SOURCE, "n"+i)
            	.addAttribute(Constants.PREFUSE_GRAPHVIEW_TARGET, "n0");
            i++;
        }
        iter.close();
        return document;
	}

	public InputStream getInstanceStream() {
		Document document = getInstanceXMLDocument();
        InputStream in = null;
		try {
			in = new ByteArrayInputStream(document.asXML().getBytes(Constants.SYS_ENCODING_UTF8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        return in;
	}
	
	public InputStream getPropertyStream() {
		Document document = getPropertyXMLDocument();
        InputStream in = null;
		try {
			in = new ByteArrayInputStream(document.asXML().getBytes(Constants.SYS_ENCODING_UTF8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        return in;
	}

	public void saveInstanceAsXMLFile(String filePath) {
        // lets write to a file
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding(Constants.SYS_ENCODING);
        XMLWriter writer;
		try {
			writer = new XMLWriter(new FileWriter(filePath), format);
	        writer.write(getInstanceXMLDocument());
	        writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void savePropertyAsXMLFile(String filePath) {
        // lets write to a file
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding(Constants.SYS_ENCODING);
        XMLWriter writer;
		try {
			writer = new XMLWriter(new FileWriter(filePath), format);
	        writer.write(getPropertyXMLDocument());
	        writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	public InputStream getClassStream() {
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
}
