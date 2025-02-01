package com.demo.cashcards;

import org.springframework.data.annotation.Id;

// entitiy class 
public record CashCard(@Id Long id, Double amount, String owner) {
    
	public CashCard(Double amount, String owner) {
		this(null, amount, owner);
	}
}

