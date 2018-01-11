package com.n26.coding.challenge.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.n26.coding.challenge.dto.TransactionDto;
import com.n26.coding.challenge.dto.TransactionStatisticsDto;
import com.n26.coding.challenge.services.TransactionService;

@RestController
public class TransactionsController {

	@Autowired
    private TransactionService transactionService;

    @PostMapping(value = "/api/transactions", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void registerTransaction(@RequestBody TransactionDto transaction) {
    	transactionService.save(transaction);
    }

    @GetMapping(value = "/api/statistics", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TransactionStatisticsDto getTransactionStatistics() {
        return transactionService.getStatistics();
    }    
}
