package edu.zju.tcmsearch.util.web;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

public class XmlParse {

	/**
	  * 将传入的一个XML String转换成一个org.w3c.dom.Document对象返回。
	  * @param xmlString 一个符合XML规范的字符串表达。
	  * @return a Document
	  */
	 public static Document parseXMLDocument(String xmlString) {
	  if (xmlString == null) {
	   throw new IllegalArgumentException();
	  }
	  try {
	   return newDocumentBuilder().parse(
	    new InputSource(new StringReader(xmlString)));
	  } catch (Exception e) {
	   throw new RuntimeException(e.getMessage());
	  }
	 }
	 
	 
	 /**
	  * 初始化一个DocumentBuilder
	  * @return a DocumentBuilder
	  * @throws ParserConfigurationException
	  */
	 public static DocumentBuilder newDocumentBuilder()
	  throws ParserConfigurationException {
	  return newDocumentBuilderFactory().newDocumentBuilder();
	 }
	 
	 
	 /**
	  * 初始化一个DocumentBuilderFactory
	  * @return a DocumentBuilderFactory
	  */
	 public static DocumentBuilderFactory newDocumentBuilderFactory() {
	  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	  dbf.setNamespaceAware(true);
	  return dbf;
	 }
	 
	 
	 
	 public static Element getFirstElementByName(Element element,String tagName) {
			  return (Element) element.getElementsByTagName(tagName);
			 }
}
