package cn.edu.seu.check;



public class Check {

	private int checkId;
	private String payerName;
	private String payerCardnum;
	private String payerImei;
	private Double totalPrice;
	private String transferTime;
	private String isCashed;
	private String xml;
	
	public Check(){
		
	}
	public Check(int checkId , String payerName , String payerCardnum , String payerImei , Double totalPrice , String transferTime , String isCashed, String xml){
		setCheckId(checkId);
		setPayerName(payerName);
		setPayerCardnum(payerCardnum);
		setPayerImei(payerImei);
		setTotalPrice(totalPrice);
		setTransferTime(transferTime);
		setIsCashed(isCashed);
		setXml(xml);
	}
	public void setCheckId(int checkId){
		this.checkId = checkId;
	}
	public int getCheckId(){
		return checkId;
	}
	public void setPayerName(String payerName){
		this.payerName = payerName;
	}
	public String getPayerName(){
		return payerName;
	}
	public void setPayerCardnum(String payerCardnum){
		this.payerCardnum = payerCardnum;
	}
	public String getPayerCardnum(){
		return payerCardnum;
	}
	public void setPayerImei(String payerImei){
		this.payerImei = payerImei;
	}
	public String getPayerImei(){
		return payerImei;
	}
	public void setTotalPrice(Double totalPrice){
		this.totalPrice = totalPrice;
	}
	public Double getTotalPrice(){
		return totalPrice;
	}
	public void setTransferTime(String transferTime){
		this.transferTime = transferTime;
	}
	public String getTransferTime(){
		return transferTime;
	}
	public void setIsCashed(String isCashed){
		this.isCashed = isCashed;
	}
	public String getIsCashed(){
		return isCashed;
	}
	public void setXml(String xml){
		this.xml = xml;
	}
	public String getXml(){
		return xml;
	}
}
