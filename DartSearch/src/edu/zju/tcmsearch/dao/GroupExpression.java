package edu.zju.tcmsearch.dao;

import java.util.ArrayList;
import java.util.List;

public class GroupExpression extends Expression {
	private List<Expression> set=new ArrayList<Expression>();
	
	public GroupExpression(){}
	
	public void setOperator(String op){
		this.operator = op;
	}
	
	public void addExpression(Expression expr){
		this.set.add(expr);
	}
	

	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(Expression expr:set){
			sb.append(" (");
			sb.append(expr.toString());
			sb.append(") ").append(super.operator);
		}
		
		sb.append("( 1 = 1)");
		return sb.toString();
	}

}
