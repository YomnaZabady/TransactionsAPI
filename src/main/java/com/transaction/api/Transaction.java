package com.transaction.api;

public class Transaction {
	public Double amount;
	public long time;
	
	public Transaction(Double amount, long time) {
		this.amount = amount;
		this.time = time;
	}
}
