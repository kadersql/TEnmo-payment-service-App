package com.techelevator.tenmo.model;

public class TransferDetails {
	
	private int id;
	private String from;
	private String to;
	private String type;
	private double amount;
	private String status;
	
	
	
	public TransferDetails(int id, String from, String to, String type, String status, double amount) {
		this.id = id;
		this.from = from;
		this.to = to;
		this.type = type;
		this.status = status;
		this.amount = amount;
		
	}
	
	
	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}

}
