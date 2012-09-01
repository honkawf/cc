package cn.edu.seu.xml;

import android.util.Log;


public class Transfer {

	private String payerdevice="";
	private String receiverdevice="";
	private String payername="";
	private String payerimei="";
	private String receivername="";
	private String receiverimei="";
	private String tradetime="";
	private String totalprice="";
	private String cipher="";
	private String payercardnumber="";
	private String receivercardnumber="";
	public Transfer()
	{
		
	}
	public Transfer(String payerdevice,String payername,String payerimei, String payercardnumber,String receiverdevice,String receivername,
			String receiverimei,String receivercardnumber,String tradetime,String totalprice,String cipher)
	{
		this.setPayerDevice(payerdevice);
		this.setPayerName(payername);
		this.setPayerIMEI(payerimei);
		this.setPayerCardNumber(payercardnumber);
		this.setReceiverDevice(receiverdevice);
		this.setReceiverIMEI(receiverimei);
		this.setReceiverName(receivername);
		this.setReceiverCardNumber(receivercardnumber);
		this.setTradeTime(tradetime);
		this.setTotalPrice(totalprice);
		this.setCipher(cipher);
		
	}
	public String getPayerDevice()
	{
		return payerdevice;
	}
	public String getReceiverDevice()
	{
		return receiverdevice;
	}
	public String getPayerName()
	{
		return payername;
	}
	public String getReceiverName()
	{
		return receivername;
	}
	public String getTradeTime()
	{
		return tradetime;
	}
	public String getTotalPrice()
	{
		return totalprice;
	}
	public String getCipher()
	{
		return cipher;
	}
	public String getPayCardNumber()
	{
		return payercardnumber;
	}

	public String getReceiverCardNumber()
	{
		return receivercardnumber;
	}
	public String getPayerIMEI()
	{
		return payerimei;
	}
	public String getReceiverIMEI()
	{
		return receiverimei;
	}
	public void setPayerDevice(String payerdevice)
	{
		this.payerdevice=payerdevice;
	}
	public void setReceiverDevice(String receiverdevice)
	{
		this.receiverdevice=receiverdevice;
	}
	public void setPayerName(String payername)
	{
		this.payername=payername;
	}
	public void setReceiverName(String receivername)
	{
		this.receivername=receivername;
	}
	public void setTradeTime(String tradetime)
	{
		this.tradetime=tradetime;
	}
	public void setTotalPrice(String totalprice)
	{
		this.totalprice=totalprice;
	}
	public void setCipher(String cipher)
	{
		this.cipher=cipher;
	}
	public void setPayerCardNumber(String payercardnumber)
	{
		this.payercardnumber=payercardnumber;
	}

	public void setReceiverCardNumber(String receivercardnumber)
	{
		this.receivercardnumber=receivercardnumber;
	}
	public void setPayerIMEI(String payerimei)
	{
		this.payerimei=payerimei;
	}
	public void setReceiverIMEI(String receiverimei)
	{
		this.receiverimei=receiverimei;
	}
}
