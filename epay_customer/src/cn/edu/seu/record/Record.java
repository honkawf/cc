package cn.edu.seu.record;



public class Record {

	private int tradenum;
	private String payerName;
	private String payerMac;
	private String payerImei;
	private String receiverName;
	private String receiverMac;
	private String receiverImei;
	private Double price;
	private String tradeType;
	private String tradeTime; 

	public Record(int tradenum,String payerName,String payerMac,String payerImei,String receiverName,String receiverMac,String receiverImei,Double price,String tradeType,String tradeTime){
		setTradenum(tradenum);
		setPayerName(payerName);
		setPayerMac(payerMac);
		setPayerImei(payerImei);
		setReceiverName(receiverName);
		setReceiverMac(receiverMac);
		setReceiverImei(receiverImei);
		setPrice(price);
		setTradeType(tradeType);
		setTradeTime(tradeTime);
	}
	public void setTradenum(int tradenum){
		this.tradenum = tradenum;
	}
	public int getTradenum(){
		return tradenum;
	}
	public void setPayerName(String payerName){
		this.payerName = payerName;
	}	
	public String getPayerName(){
		return payerName;
	}
	public void setPayerMac(String payerMac){
		this.payerMac = payerMac;
	}
	public String getPayerMac(){
		return payerMac;
	}
	public void setPayerImei(String payerImei){
		this.payerImei = payerImei;
	}
	public String getPayerImei(){
		return payerImei;
	}
	public void setReceiverName(String receiverName){
		this.receiverName = receiverName;
	}
	public String getReceiverName(){
		return receiverName;
	}
	public void setReceiverMac(String receiverMac){
		this.receiverMac = receiverMac;
	}
	public String getReceiverMac(){
		return receiverMac;
	}
	public void setReceiverImei(String receiveImei){
		this.receiverImei = receiveImei;
	}
	public String getReceiverImei(){
		return receiverImei;
	}
	public void setPrice(Double price){
		this.price = price;
	}	
	public Double getPrice(){
		return price;
	}
	public void setTradeType(String tradeType){
		this.tradeType = tradeType;
	}	
	public String getTradeType(){
		return tradeType;
	}	
	public void setTradeTime(String tradeTime){
		this.tradeTime = tradeTime;
	}	
	public String getTradeTime(){
		return tradeTime;
	}
}
