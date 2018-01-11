package com.n26.coding.challenge.dto;

import org.immutables.value.Value.Immutable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Immutable
@JsonSerialize(as = ImmutableTransactionStatisticsDto.class)
@JsonDeserialize(as = ImmutableTransactionStatisticsDto.class)
public interface TransactionStatisticsDto {

    long getCount();

    double getSum();

    double getMax();

    double getMin();

    default double getAvg() {
        return getCount() > 0 ? getSum() / getCount() : 0.0;
    }

    TransactionStatisticsDto NO_TRANSACTION = ImmutableTransactionStatisticsDto.builder()
            .count(0)
            .sum(0.0)
            .max(0.0)
            .min(0.0)
            .build();


    default TransactionStatisticsDto save(double amount) {
        return this.equals(NO_TRANSACTION) ?
        		ImmutableTransactionStatisticsDto.builder()
                        .count(1)
                        .sum(amount)
                        .min(amount)
                        .max(amount)
                        .build() :
                ImmutableTransactionStatisticsDto.builder()
                        .count(getCount() + 1)
                        .sum(getSum() + amount)
                        .min(Math.min(getMin(), amount))
                        .max(Math.max(getMax(), amount))
                        .build();
    }

    default TransactionStatisticsDto merge(TransactionStatisticsDto that) {
        if (this.equals(NO_TRANSACTION)) {
            return that;
        }
        if (that.equals(NO_TRANSACTION)) {
            return this;
        }
        return ImmutableTransactionStatisticsDto.builder()
                .count(this.getCount() + that.getCount())
                .sum(this.getSum() + that.getSum())
                .min(Math.min(this.getMin(), that.getMin()))
                .max(Math.max(this.getMax(), that.getMax()))
                .build();
    }
}
