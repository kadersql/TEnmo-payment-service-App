package com.techelevator.tenmo.model;

public class Transfer {

	private double amount;
	private int fromId;
	private int toId;
	private int transferId;

	
	
	
	
	public int getTransferId() {
		return transferId;
	}

	public void setTransferId(int transferId) {
		this.transferId = transferId;
	}

	public Transfer() {

	}

	public Transfer(int transId, String name, double amount2) {

	}

	public int getFromId() {
		return fromId;
	}

	public void setFromId(int fromId) {
		this.fromId = fromId;
	}

	public int getToId() {
		return toId;
	}

	public void setToId(int toId) {
		this.toId = toId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

}
