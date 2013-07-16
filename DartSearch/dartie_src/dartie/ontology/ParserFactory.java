package dartie.ontology;

import dartie.ontology.owl.impl.OwlParser;
import dartie.ontology.rdf.impl.RdfParser;
import dartie.ontology.rdf.impl.RdfsParser;

public class ParserFactory {
	
	private static String inputClassFile;
	private static String inputRdfFile; 

	public static IParser createParser(String inputFileName, String uri) {
		int i = inputFileName.lastIndexOf('.');
		if(i > -1){
			String ext = inputFileName.substring(i, inputFileName.length());
			IParser parser;
			if (ext.equals(".owl")) {
				parser = new OwlParser();
			}
			else if (ext.equals(".rdfs")) {
				parser = new RdfsParser();
			}
			else if (ext.equals(".rdf")) {
				parser = new RdfParser();
			}
			else {
				return null;
			}
			parser.setInputFileName(inputFileName);
			parser.setUri(uri);
			parser.init();
			return parser;
		}
		return null;
	}
	
	public static IParser createRdfsParser() {
		IParser parser = new RdfsParser();
		return parser;
	}

	public static IParser createRdfParser() {
		IParser parser = new RdfParser();
		return parser;
	}
	
	public static IParser createRdfParser(String inputFileName) {
		IParser parser = new RdfParser();
		parser.setInputFileName(inputFileName);
		parser.init();
		return parser;
	}
	
	public static IParser createRdfParser(String inputFileName, String uri) {
		IParser parser = new RdfParser();
		parser.setInputFileName(inputFileName);
		parser.setUri(uri);
		parser.init();
		return parser;
	}
	
	public static IParser createOwlParser() {
		IParser parser = new OwlParser();
		return parser;
	}
	
	public static IParser createOwlParser(String inputFileName) {
		IParser parser = new OwlParser();
		parser.setInputFileName(inputFileName);
		parser.init();
		return parser;
	}
	
	public static IParser createOwlParser(String inputFileName, String uri) {
		IParser parser = new OwlParser();
		parser.setInputFileName(inputFileName);
		parser.setUri(uri);
		parser.init();
		return parser;
	}
	
	public static void SetInputFile(String fileName)
	{
		int i = fileName.lastIndexOf('.');
		if(i > -1){
			String ext = fileName.substring(i, fileName.length());
			if (ext.equals(".owl")) {
				inputClassFile = fileName;
				inputRdfFile = fileName;
			}
			else if (ext.equals(".rdfs")) {
				inputClassFile = fileName;
				inputRdfFile =fileName.substring(0,i)+".rdf";
			}
			else if (ext.equals(".rdf")) {
				inputClassFile = fileName;
				inputRdfFile = fileName;
			}
		}
	}

	/**
	 * @return Returns the inputClassFile.
	 */
	public static String getInputClassFile() {
		return inputClassFile;
	}

	/**
	 * @return Returns the inputRdfFile.
	 */
	public static String getInputRdfFile() {
		return inputRdfFile;
	}
}
