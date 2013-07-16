package edu.zju.tcmsearch.lucene.search;

import java.text.NumberFormat;

import org.apache.commons.lang.builder.HashCodeBuilder;

import edu.zju.tcmsearch.common.domain.DartOntology;
import edu.zju.tcmsearch.exception.lucene.TcmLuceneException;

/*
 * 浙江大学网格实验室
 * @author 谢骋超 
 * 2005年
 */

/**
 * 统计查询结果中该本体出现的次数
 */
public class HitOntology implements Comparable{
	private int count=0;
    private double score=0;
	private DartOntology dartOntology;

	public HitOntology(DartOntology ontology) {
		super();
		// TODO Auto-generated constructor stub
		dartOntology = ontology;
	}

	public double getScore() {
		return score;
	}
	
	public String getScoreStr(){
		NumberFormat nf=NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
        return nf.format(getScore());
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public DartOntology getDartOntology() {
		return dartOntology;
	}

	public void setDartOntology(DartOntology dartOntology) {
		this.dartOntology = dartOntology;
	}

	public String getOntoIdentity() {
		return dartOntology.getIdentity();
	}
	
	public String getOntoName() {
		return dartOntology.getName();
	}	
	
	public void incCount(){
		count++;
	}
	
	public void incScore(double score){
		this.score+=score;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof HitOntology)) {
			throw new TcmLuceneException("要比较的对象不是HitOnotology类型!");
		}
		if (null == obj) {
			return false;
		}

		if (this == obj) {
			return true;
		}

		HitOntology hitOntology = (HitOntology) obj;

		if (this.getOntoIdentity() == hitOntology.getOntoIdentity()) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(15, 19).append(getOntoIdentity())
				.toHashCode();
	}
	
	

	@Override
	public String toString() {	
		StringBuilder sb=new StringBuilder();
		sb.append(this.getOntoIdentity()+" "+" "+this.getScore()+" "+this.getScoreStr()+" "+this.getCount());
		return sb.toString();
	}

	public int compareTo(Object obj) {
		if (!(obj instanceof HitOntology)) {
			throw new TcmLuceneException("要比较的对象不是HitOnotology类型!");
		}
		if (null == obj) {
			throw new TcmLuceneException("要比较的对象为空!");
		}
		if (this == obj) {
			return 0;
		}
		HitOntology hitOntology = (HitOntology) obj;
		if (this.getScore()>hitOntology.getScore()){
			return 1;
		}
		else if (this.getScore()==hitOntology.getScore()){
			return 0;
		}
		else{
		  return -1;
		}
	}
}
