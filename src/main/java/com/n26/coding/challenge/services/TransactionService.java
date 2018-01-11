package com.n26.coding.challenge.services;

import static com.n26.coding.challenge.dto.TransactionStatisticsDto.NO_TRANSACTION;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.n26.coding.challenge.dao.TransactionStatisticsStore;
import com.n26.coding.challenge.dto.TransactionDto;
import com.n26.coding.challenge.dto.TransactionStatisticsDto;

@Service
public class TransactionService {

    private final TransactionStatisticsStore<TransactionStatisticsDto> transactions;

    @Autowired
    public TransactionService() {
        this(TransactionStatisticsStore.lastMinute(() -> NO_TRANSACTION));
    }
    
    protected TransactionService(TransactionStatisticsStore<TransactionStatisticsDto> transactions) {
        this.transactions = transactions;
    }

    public void save(TransactionDto transaction) {
        transactions.update(transaction.getTimestamp(), statistic -> statistic.save(transaction.getAmount()));
    }

    public TransactionStatisticsDto getStatistics() {
        return transactions.reduce(TransactionStatisticsDto::merge);
    }
}
