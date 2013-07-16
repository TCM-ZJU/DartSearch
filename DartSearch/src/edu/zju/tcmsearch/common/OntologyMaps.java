package edu.zju.tcmsearch.common;

import java.util.HashMap;
import java.util.Map;

import edu.zju.tcmsearch.common.domain.DartOntology;

/*
 * 浙江大学网格实验室
 * @author 谢骋超 
 * 2005年
 */
public class OntologyMaps {

	private static final Map<String, DartOntology> instanceMap = new HashMap();

	private static final Map<String, DartOntology> valueMap = new HashMap();

	public static Map<String, DartOntology> getInstanceMap() {
		return instanceMap;
	}

	public static Map<String, DartOntology> getValueMap() {
		return valueMap;
	}

	public static void putInstance(DartOntology dartOntology) {
		instanceMap.put(dartOntology.getIdentity(), dartOntology);
        putValue(dartOntology);//同时要在valueMap里填东西
	}

	public static DartOntology getInstance(String identity) {
		return instanceMap.get(identity);
	}

	public static int getInstanceCount() {
		return instanceMap.size();
	}

	public static boolean isEmptyInstance() {
		return 0 == getInstanceCount();
	}

	public static void putValue(DartOntology dartOntology) {
		valueMap.put(dartOntology.getURI(), dartOntology);
	}

	public static DartOntology getValue(String uri) {
		return valueMap.get(uri);
	}

	public static int getValueCount() {
		return valueMap.size();
	}

	public static boolean isEmptyValue() {
		return 0 == getValueCount();
	}
}
