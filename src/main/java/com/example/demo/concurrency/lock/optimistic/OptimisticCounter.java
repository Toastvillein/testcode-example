package com.example.demo.concurrency.lock.optimistic;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class OptimisticCounter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int count;

    @Version
    private int version;

    public OptimisticCounter(int count) {
        this.count = count;
    }

    public void initCount() {
        this.count = 1000;
    }

    public void decreaseCount() {
        this.count--;
    }
}