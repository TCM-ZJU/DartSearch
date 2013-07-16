package edu.zju.tcmsearch.dao;


public class Expression {
	boolean isLeftExpr=false;
	boolean isRightExpr=false;
	Expression left=null;
	Expression right=null;
	String leftValue =null;
	String rightValue=null;
	String operator;
	
	public Expression(){}
	
	public Expression(Expression l,String op, Expression r){
		this.setLeft(l);
		this.setOperator(op);
		this.setRight(r);
	}
	
	public Expression(String l,String op, String r){
		this.setLeft(l);
		this.setOperator(op);
		this.setRight(r);
	}
		
	
	public String toString(){
		int d = Operator.NumParameters.get(operator);
		if(d==1){
			  return " "+operator+" "+ 
			         (isLeftExpr ?  Operator.LBrace+left.toString() +Operator.RBrace : (leftValue));
		}else if(d==-1){
			  return (isLeftExpr ?  Operator.LBrace+left.toString() +Operator.RBrace : (leftValue))+ " "+operator+" ";
	    }else if(d==2){
	    	  return (isLeftExpr ?  Operator.LBrace+left.toString() +Operator.RBrace  : (leftValue))+
	    	         " "+operator+" '"+ 
	    	         (isRightExpr ? Operator.LBrace+right.toString()+Operator.RBrace : (rightValue))+"'";
	    }else if(d==-2){
	    	  return (isLeftExpr ?  Operator.LBrace+left.toString() +Operator.RBrace  : (leftValue))+
 	                 " "+operator+" "+"'%"+
 	                 (isRightExpr ? Operator.LBrace+right.toString()+Operator.RBrace : (rightValue))+"%'";
	    }
		return "[Err]";
	}
	
	public void setLeft(String value){
		this.isLeftExpr = false;
		this.leftValue  = value;		
	}
	
	public void setLeft(Expression expr){
		this.isLeftExpr = true;
		this.left  = expr;
	}	
	
	public void setRight(String value){
		this.isRightExpr = false;
		this.rightValue  = value;		
	}
	
	public void setRight(Expression expr){
		this.isRightExpr = true;
		this.right  = expr;
	}	
		
	public void setOperator(String op){
		this.operator = op;
	}
	
	public boolean isVar(String var){
		return null==var ? false : var.startsWith("?") && var.length()>1;
	}

	protected String resolveMapping(String var){
		return var;
	}
	
}
