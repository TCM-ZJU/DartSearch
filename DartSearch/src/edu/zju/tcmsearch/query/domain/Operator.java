package edu.zju.tcmsearch.query.domain;

import static cn.edu.zju.dart.core.DartCoreConstants.RDF_DART_VALUE_NODE_BIGGERTHAN;
import static cn.edu.zju.dart.core.DartCoreConstants.RDF_DART_VALUE_NODE_BIGGER_OR_EQUAL;
import static cn.edu.zju.dart.core.DartCoreConstants.RDF_DART_VALUE_NODE_EQUALTO;
import static cn.edu.zju.dart.core.DartCoreConstants.RDF_DART_VALUE_NODE_LESSTHAN;
import static cn.edu.zju.dart.core.DartCoreConstants.RDF_DART_VALUE_NODE_LESS_OR_EQUAL;
import static cn.edu.zju.dart.core.DartCoreConstants.RDF_DART_VALUE_NODE_LIKE;

import java.util.LinkedHashMap;
import java.util.Map;

import com.hp.hpl.jena.rdf.model.Property;

/*
 * 浙江大学网格实验室
 * @author 谢骋超 
 * 2005年
 */
public class Operator {
	private String name;// 显示在页面上给用户看的名字

	private Property prop;// 本体论查询真正要用的是这个property

	public Operator(String name, Property prop) {
		this.name = name;
		this.prop = prop;
	}

	private static Map<String, Operator> operatorMap = new LinkedHashMap<String, Operator>();

	static {
		operatorMap.put(RDF_DART_VALUE_NODE_LIKE.toString(), new Operator("包含",
				RDF_DART_VALUE_NODE_LIKE));		
		operatorMap.put(RDF_DART_VALUE_NODE_EQUALTO.toString(), new Operator(
				"=", RDF_DART_VALUE_NODE_EQUALTO));		
		operatorMap.put(RDF_DART_VALUE_NODE_BIGGERTHAN.toString(),
				new Operator(">", RDF_DART_VALUE_NODE_BIGGERTHAN));
		operatorMap.put(RDF_DART_VALUE_NODE_BIGGER_OR_EQUAL.toString(),
				new Operator(">=", RDF_DART_VALUE_NODE_BIGGER_OR_EQUAL));
		operatorMap.put(RDF_DART_VALUE_NODE_LESSTHAN.toString(), new Operator(
				"<", RDF_DART_VALUE_NODE_LESSTHAN));
		operatorMap.put(RDF_DART_VALUE_NODE_LESS_OR_EQUAL.toString(),
				new Operator("<=", RDF_DART_VALUE_NODE_LESS_OR_EQUAL));
	}
	
	public static Map<String, Operator> getMap(){
		return operatorMap;
	}

	public static Operator get(String str) {
		return operatorMap.get(str);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Property getProp() {
		return prop;
	}

	public void setProp(Property prop) {
		this.prop = prop;
	}

	@Override
	public String toString() {
		return getName();
	}
	
	public String getPropStr(){
		return this.prop.toString();
	}

}
