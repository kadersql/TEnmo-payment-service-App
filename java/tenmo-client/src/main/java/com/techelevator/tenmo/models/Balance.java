package com.techelevator.tenmo.models;

public class Balance {
	
	private double balance;
	private double sendTEBuck;

	

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	
	public double getSendTEBuck() {
		return sendTEBuck;
	}

	public void setSendTEBuck(double sendTEBuck) {
		this.sendTEBuck = sendTEBuck;
	}

	public double sendTEBuck(double amountTEBuck) {
		try {
		if (balance >=amountTEBuck) {
		balance = balance - amountTEBuck;
		
		}
		}catch (Exception e) {
			System.out.println("Balance is low");
		}
		return balance;
	}

    public double receieveTEBuck(double amountTEBuck) {
		
		balance = balance + amountTEBuck;	
		return balance;
	}
}

