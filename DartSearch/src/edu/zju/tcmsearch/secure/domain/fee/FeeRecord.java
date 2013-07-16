package edu.zju.tcmsearch.secure.domain.fee;

import java.util.HashMap;
import java.util.Map;

public class FeeRecord {
	
	private int intTime;
	private int accountId;
	private int type;
	private int fee;
	private String goodsIdentity;
	private String time;
	private String content;
	
	public String getContent(){
		return this.content;
	}
	
	public void setContent(String content){
		this.content = content;
	}
	
	public long getIntTime(){
		return this.intTime;
	}
	
	public void setIntTime(long time){
		this.intTime = (int)(time/1000);
	}
	
	public int getAccoutId(){
		return this.accountId;
	}
	
	public void setAccountId(int id){
		this.accountId = id;
	}
	
	public int getType(){
		return this.type;
	}
	
	public void setType(int type){
		this.type = type;
	}
	
	public void setType(String type){
		if(typeMap.containsKey(type)){
			this.type = typeMap.get(type).intValue();
		}else{
			this.type = typeMap.get(INVALID_FEE_TYPE).intValue();
		}
	}
	
	public int getFee(){
		return this.fee;
	}
	
	public void setFee(int fee){
		this.fee = fee;
	}
	
	public String getGoodsIdentity(){
		return this.goodsIdentity;
	}
	
	public void setGoodsIdentity(String id){
		this.goodsIdentity = id;
	}
	
	public String getTime(){
		return this.time;
	}
	
	public void setTime(String time){
		this.time = time;
	}
	

	public static final  String TABLE_RECORD_FEE = "TABLE_RECORD_FEE";
	public static final  String ONTOLOGY_FEE = "ONTOLOGY_FEE";
	public static final  String INVALID_FEE_TYPE = "INVALID_FEE_TYPE";
	protected static final Map<String,Integer> typeMap = new HashMap<String,Integer>();
	static {
		typeMap.put(INVALID_FEE_TYPE,0);
		typeMap.put(TABLE_RECORD_FEE,1);
		typeMap.put(ONTOLOGY_FEE,2);
	}
	
}
