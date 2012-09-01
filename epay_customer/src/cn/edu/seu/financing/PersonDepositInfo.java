package cn.edu.seu.financing;



public class PersonDepositInfo {

	// Fields

	private String username;
	private String depositway;
	private String interestrateway;
	private String amount;

	// Constructors

	/** default constructor */
	public PersonDepositInfo() {
	}

	/** full constructor */
	public PersonDepositInfo(String username,String depositway, String interestrateway,String amount) {
		this.username = username;
		this.depositway = depositway;
		this.interestrateway = interestrateway;
		this.amount = amount;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


	public String getDepositway() {
		return this.depositway;
	}

	public void setDepositway(String depositway) {
		this.depositway = depositway;
	}

	public String getInterestrateway() {
		return this.interestrateway;
	}

	public void setInterestrateway(String interestrateway) {
		this.interestrateway = interestrateway;
	}

	public String getAmount() {
		return this.amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

}