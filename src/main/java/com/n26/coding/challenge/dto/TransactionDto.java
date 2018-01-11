package com.n26.coding.challenge.dto;


import static java.time.temporal.ChronoUnit.SECONDS;

import java.io.Serializable;

import com.n26.coding.challenge.annotations.LastMinute;

public class TransactionDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private double amount;
    private long timestamp;
    
	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@LastMinute(duration = 60, unit = SECONDS)
	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}
