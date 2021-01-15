package com.techelevator.tenmo.models;

public class ToFromTransfer {

	private double amount;
	private int transfer_id;
	private String toFromUserName;

//	public ToFromTransfer(int transfer_id, String name, double amount) {
//		this.transfer_id = transfer_id;
//		this.toFromUserName = name;
//		this.amount = amount;
//	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getTransfer_id() {
		return transfer_id;
	}

	public void setTransfer_id(int transfer_id) {
		this.transfer_id = transfer_id;
	}

	public String getToFromUserName() {
		return toFromUserName;
	}

	public void setToFromUserName(String toFromUserName) {
		this.toFromUserName = toFromUserName;
	}

}
