package cn.edu.seu.datatransportation;

import java.io.Serializable;

public class LocalInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param args
	 */
	private String userName;
	private String password;
	private String gesturePwd;
	private String cardnumber;
	private String balance;
	private String availablebalance;
	private String privateKey;
	private String publicKeyn;
	private String customerName;
	
	public LocalInfo(){
		setUserName("0");
		setPassword("0");
		setGesturePwd("0");
		setCardnum("0");
		setBalance("0");
		setAvailableBalance("0");
		setPrivateKey("0");
		setPublicKeyn("0");
		setCustomerName("0");
	}
	public LocalInfo(String userName , String password , String gesturePwd , String cardnum , String balance , String availableBalance , String privateKey , String publicKeyn , String customerName){
		setUserName(userName);
		setPassword(password);
		setGesturePwd(gesturePwd);
		setCardnum(cardnum);
		setBalance(balance);
		setAvailableBalance(availableBalance);
		setPrivateKey(privateKey);
		setPublicKeyn(publicKeyn);
		setCustomerName(customerName);
	}
	public void setUserName(String userName){
		this.userName = userName;
	}
	public String getUserName(){
		return userName;
	}
	public void setPassword(String password){
		this.password = password;
	}
	public String getPassword(){
		return password;
	}
	public void setGesturePwd(String gesturePwd){
		this.gesturePwd = gesturePwd;
	}
	public String getGesturePwd(){
		return gesturePwd;
	}
	public void setCardnum(String cardnumber){
		this.cardnumber = cardnumber;
	}
	public String getCardnum(){
		return cardnumber;
	}
	public void setBalance(String balance){
		this.balance = balance;
	}
	public String getBalance(){
		return balance;
	}
	public void setAvailableBalance(String availablebalance){
		this.availablebalance = availablebalance;
	}
	public String getAvailableBalance(){
		return availablebalance;
	}
	public void setPrivateKey(String privateKey){
		this.privateKey = privateKey;
	}
	public String getPrivateKey(){
		return privateKey;
	}
	public void setPublicKeyn(String publicKeyn){
		this.publicKeyn = publicKeyn;
	}
	public String getPublicKeyn(){
		return publicKeyn;
	}
	public void setCustomerName(String customerName)
	{
		this.customerName=customerName;
	}
	public String getCustomerName()
	{
		return customerName;
	}
}
