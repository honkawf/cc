package cn.edu.seu.xml;

/**
 * PersonInfo entity. @author MyEclipse Persistence Tools
 */
public class PersonInfo implements java.io.Serializable {

	// Fields

	private String username;
	private String customername;
	private String cardnum;
	private String bluetoothmac;
	private String privatekey;
	private String identificationcardnum;
	private String phonenum;
	private String balance;
	private String password;
	private String publickeyn;
	private String publickeyd;
	private String imei;

	// Constructors

	/** default constructor */
	public PersonInfo() {
	}

	/** minimal constructor */
	public PersonInfo(String username) {
		this.username = username;
	}

	/** full constructor */
	public PersonInfo(String username, String customername, String cardnum,
			String bluetoothmac, String privatekey,
			String identificationcardnum, String phonenum, String balance,
			String password, String publickeyn, String publickeyd, String imei) {
		this.username = username;
		this.customername = customername;
		this.cardnum = cardnum;
		this.bluetoothmac = bluetoothmac;
		this.privatekey = privatekey;
		this.identificationcardnum = identificationcardnum;
		this.phonenum = phonenum;
		this.balance = balance;
		this.password = password;
		this.publickeyn = publickeyn;
		this.publickeyd = publickeyd;
		this.imei = imei;
	}

	// Property accessors
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCustomername() {
		return this.customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public String getCardnum() {
		return this.cardnum;
	}

	public void setCardnum(String cardnum) {
		this.cardnum = cardnum;
	}

	public String getBluetoothmac() {
		return this.bluetoothmac;
	}

	public void setBluetoothmac(String bluetoothmac) {
		this.bluetoothmac = bluetoothmac;
	}

	public String getPrivatekey() {
		return this.privatekey;
	}

	public void setPrivatekey(String privatekey) {
		this.privatekey = privatekey;
	}

	public String getIdentificationcardnum() {
		return this.identificationcardnum;
	}

	public void setIdentificationcardnum(String identificationcardnum) {
		this.identificationcardnum = identificationcardnum;
	}

	public String getPhonenum() {
		return this.phonenum;
	}

	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}

	public String getBalance() {
		return this.balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPublickeyn() {
		return this.publickeyn;
	}

	public void setPublickeyn(String publickeyn) {
		this.publickeyn = publickeyn;
	}

	public String getPublickeyd() {
		return this.publickeyd;
	}

	public void setPublickeyd(String publickeyd) {
		this.publickeyd = publickeyd;
	}

	public String getImei() {
		return this.imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

}