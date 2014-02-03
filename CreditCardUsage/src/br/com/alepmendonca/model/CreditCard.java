package br.com.alepmendonca.model;

public class CreditCard {

	private long id;
	private int lastNumbers;
	private String holder;
	private String issuer;

	public CreditCard(long _id, int _lastNumbers, String _holder, String _issuer) {
		super();
		setId(_id);
		setLastNumbers(_lastNumbers);
		setHolder(_holder);
		setIssuer(_issuer);
	}

	public long getId() {
		return id;
	}
	private void setId(long id) {
		this.id = id;
	}
	public int getLastNumbers() {
		return lastNumbers;
	}
	private void setLastNumbers(int lastNumbers) {
		this.lastNumbers = lastNumbers;
	}
	public String getHolder() {
		return holder;
	}
	private void setHolder(String holder) {
		this.holder = holder;
	}
	public String getIssuer() {
		return issuer;
	}
	private void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	
	@Override
	public String toString() {
		return String.valueOf(getLastNumbers());
	}
}
