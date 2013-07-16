package edu.zju.tcmsearch.secure.domain.fee;

public class TablePrice {
	private String tableId="";
	private String freeCharge = "";
	private int price=0;

	public void setTableId(String id){
		this.tableId =id;
	}
	

	/* WHEN this method exists the SMART sping binding will assure freeCharge is a Boolean
	 * 
	 *public boolean isFreeCharge(){
	 *	return this.freeCharge.equals(",true");
	 *}
	 */
	
	public void setFreeCharge(String free){
		this.freeCharge =free;
	}	
	
	public void setPrice(int price){
		this.price =price;
	}
	
	
	public String getTableId(){
		return this.tableId;
	}
	
	public String getFreeCharge(){
		return this.freeCharge;
	}

	public boolean isFree(){
		return this.freeCharge.equals(",true");	
	}
	
	public int getPrice(){
		return this.price;
	}
	
	public TablePrice(String id,String free,int price){
		this.tableId =id;
		this.freeCharge =free;
		this.price =price;
	}
	
	public TablePrice(){
		
	}
	public boolean equals(Object obj){
		if(obj instanceof TablePrice){
			TablePrice price = (TablePrice)obj;
			return price.getTableId().equals(this.tableId);
		}else{
			return false;
		}
	}
	
}
