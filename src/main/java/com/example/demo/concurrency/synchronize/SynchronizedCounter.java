package com.example.demo.concurrency.synchronize;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class SynchronizedCounter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int count;

    public SynchronizedCounter(int count) {
        this.count = count;
    }

    public void initCount() {
        this.count = 1000;
    }

    public void decreaseCount() {
        this.count--;
    }
}