package com.n26.coding.challenge.dao;

import static java.time.temporal.ChronoUnit.MILLIS;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.n26.coding.challenge.exceptions.ExpiredTimestampException;

public class TransactionStatisticsStore<T> {

    private final Supplier<Long> now;
    private final Supplier<T> factory;

    private final TemporalUnit targetUnit;
    private final TemporalUnit groupUnit;

    private final AtomicReferenceArray<Reference<T>> store;

    public static <T> TransactionStatisticsStore<T> lastMinute(Supplier<T> factory) {
        return new TransactionStatisticsStore<>(ChronoUnit.MINUTES, ChronoUnit.SECONDS, 64, factory);
    }

    public TransactionStatisticsStore(TemporalUnit targetUnit, TemporalUnit groupUnit, int bufferSize, Supplier<T> factory) {
        this(targetUnit, groupUnit, bufferSize, factory, System::currentTimeMillis);
    }

    protected TransactionStatisticsStore(TemporalUnit targetUnit, TemporalUnit groupUnit, int bufferSize, Supplier<T> factory, Supplier<Long> now) {
        this.now = now;
        this.factory = factory;
        this.targetUnit = targetUnit;
        this.groupUnit = groupUnit;
        this.store = new AtomicReferenceArray<>(bufferSize);
    }

    public void update(long timestamp, UnaryOperator<T> updater) {
        getReference(timestamp).update(updater);
    }

    public T reduce(BinaryOperator<T> reducer) {
        return getReferenceStream().reduce(factory.get(), reducer);
    }

    protected Reference<T> getReference(long timestamp) {
        int index = checkIndex(timestamp);
        int offset = offset(index);
        return store.updateAndGet(offset, value -> actual(index, value));
    }

    private Stream<T> getReferenceStream() {
        long now = this.now.get();

        int firstIndex = minIndex(now);
        int lastIndex = currentIndex(now);

        return IntStream.rangeClosed(firstIndex, lastIndex)
                .mapToObj(index -> historical(index, store.get(offset(index))))
                .filter(Objects::nonNull)
                .map(Reference::getValue);
    }

    private Reference<T> historical(int index, Reference<T> reference) {
        return reference != null && reference.getIndex() == index ? reference : null;
    }

    private Reference<T> actual(int index, Reference<T> value) {
        return value == null || value.getIndex() < index ? new Reference<>(index, factory.get()) : value;
    }

    private int offset(int index) {
        return index % store.length();
    }

    private int currentIndex(long timestamp) {
        return (int) Duration.of(timestamp, MILLIS).get(groupUnit);
    }

    private int minIndex(long timestamp) {
        return (int) Duration.of(timestamp, MILLIS).minus(1, targetUnit).get(groupUnit);
    }

    private int checkIndex(long timestamp) {
        long now = this.now.get();

        int minimalIndex = minIndex(now);
        int maximalIndex = currentIndex(now);

        int index = currentIndex(timestamp);

        ExpiredTimestampException.check(index >= minimalIndex, "Timestamp is older than 1 min");
        ExpiredTimestampException.check(index <= maximalIndex, "Timestamp is too young");

        return index;
    }

    protected static class Reference<E> {
        private final long index;
        private final AtomicReference<E> value;

        public Reference(long index, E value) {
            this.index = index;
            this.value = new AtomicReference<>(value);
        }

        public void update(UnaryOperator<E> updater) {
            value.updateAndGet(updater);
        }

        public E getValue() {
            return value.get();
        }

        public long getIndex() {
            return index;
        }
    }
}
