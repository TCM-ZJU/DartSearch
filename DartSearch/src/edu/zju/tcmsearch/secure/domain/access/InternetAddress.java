package edu.zju.tcmsearch.secure.domain.access;

import java.lang.NumberFormatException;



public class InternetAddress {

	private byte[] iAddress;
	
	public InternetAddress(){
		iAddress = new byte[4];
		iAddress[0] = iAddress[1] = iAddress[2] = iAddress[3] = 0;
	}
	
	public static InternetAddress getInternetAddress(String ipStr){
		if(null!=ipStr && ipStr.matches("[01\\.]*")&&ipStr.length()==35){
			return getInternetAddressBin(ipStr);
		}else{
			return getInternetAddressDec(ipStr);
		}
	}
	
	public static InternetAddress getInternetAddressDec(String ipStr){
		try{
		if(null!=ipStr){
		   String[] ips = ipStr.split("\\.");
		   if(ips.length>0 && ips.length<5){
			   InternetAddress iAddress = new InternetAddress();
			   for(int i=0;i<ips.length;i++){
					iAddress.iAddress[i] =(byte) Integer.parseInt(ips[i]);
			   }
			   return iAddress;
		   }
		}
		}catch(NumberFormatException e){
			return null;
		}
		return null;
	}
	
	public static InternetAddress getInternetAddressBin(String ipStr){
		try{
			if(null!=ipStr){
			   String[] ips = ipStr.split("\\.");
			   if(ips.length>0 && ips.length<5){
				   InternetAddress iAddress = new InternetAddress();
				   for(int i=0;i<ips.length;i++){
						iAddress.iAddress[i] =(byte) Integer.parseInt(ips[i],2);
				   }
				   return iAddress;
			   }
			}
			}catch(NumberFormatException e){
				return null;
			}
			return null;		
	}
	public static InternetAddress getInternetAddress(byte[] ip){
		if(ip.length<1 || ip.length>4)
			return null;
		InternetAddress iAddress = new InternetAddress();
		for(int i=0;i<ip.length;i++){
			iAddress.iAddress[i]=ip[i];
		}
		return iAddress;
	}
	
	public InternetAddress getNetAddress(InternetAddress mask){
		InternetAddress netAddress = new InternetAddress();
		for(int i=0;i<4;i++){
			netAddress.iAddress[i] =(byte) (this.iAddress[i] & mask.iAddress[i]);
		}
		return netAddress;
	}
	
	public boolean equals(Object obj){
		if(obj instanceof InternetAddress){
			InternetAddress iAddress = (InternetAddress)obj;
			return this.iAddress[0]==iAddress.iAddress[0] && this.iAddress[1]==iAddress.iAddress[1] && 
			       this.iAddress[2]==iAddress.iAddress[2] && this.iAddress[3]==iAddress.iAddress[3];
		}else{
			return false;
		}
	}
	
	public String toString(){
		return getUnsignedInt(iAddress[0])+"."+ getUnsignedInt(iAddress[1])+"."+getUnsignedInt(iAddress[2])+"."+getUnsignedInt(iAddress[3]);
	}
	
	protected int getUnsignedInt(byte b){
		int i = b;
		return i<0 ? 255-(~i):i;
	}
	
	public static String getNetAddress(String ip,String mask){
		InternetAddress ipA = getInternetAddress(ip);
		InternetAddress maskA = getInternetAddress(mask);
		if(null!=ipA && null!=maskA){
		    return ipA.getNetAddress(maskA).toString();
		}else{
			return null;
		}
	}
	
	public int getMaxHostsInSubnet(){
		int ones = 0;
		for(int i=0;i<=3;i++){
			int tmp = getUnsignedInt(this.iAddress[i]);
            while(tmp>0){
            	ones= (tmp&1)==1 ? (1+ones):ones;
            	tmp>>=1;
            }
		}
		return 1<<(32-ones);
	}
}
