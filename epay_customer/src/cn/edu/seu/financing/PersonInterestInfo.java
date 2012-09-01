package cn.edu.seu.financing;


public class PersonInterestInfo implements java.io.Serializable {

	private String username;
	private String financingway;

	// Constructors

	public PersonInterestInfo() {
	}
	
	public PersonInterestInfo(String username, String financingway) {
		this.username = username;
		this.financingway = financingway;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	
	public String getFinancingway() {
		return this.financingway;
	}

	public void setFinancingway(String financingway) {
		this.financingway = financingway;
	}

}