package edu.zju.tcmsearch.dao;

import java.util.HashMap;
import java.util.Map;

public class Operator {
	public static final String Like = "like";
	
	public static final String Equal = "=";
	
	public static final String NotEqual = "<>";
	
	public static final String MoreThan = ">";
	
	public static final String LessThan ="<";
	
	public static final String MoreOrEqualThan =">=";
	
	public static final String LessOrEqualThan ="<=";
	
	public static final String IsNull ="is null";
	
	public static final String And = "and";
	
	public static final String Or ="or";
	
	public static final String Not ="not";
	
	public static final String LBrace ="(";
	
	public static final String RBrace =")";
	
	public static final Map<String,Integer> NumParameters = new HashMap<String,Integer>();
	
	static{
		NumParameters.put(Equal,2);
		NumParameters.put(NotEqual,2);
		NumParameters.put(MoreThan,2);
		NumParameters.put(LessThan,2);
		NumParameters.put(MoreOrEqualThan,2);
		NumParameters.put(LessOrEqualThan,2);
		NumParameters.put(And,2);
		NumParameters.put(Or,2);
		NumParameters.put(Not,1);
		NumParameters.put(IsNull,-1);		
		NumParameters.put(Like,-2);
		
	}

}
