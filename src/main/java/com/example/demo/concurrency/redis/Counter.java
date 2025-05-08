package com.example.demo.concurrency.redis;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Counter {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int count;

    public Counter(int count) {
        this.count = count;
    }

    public void initCount() {
        this.count = 1000;
    }
    public void decreaseCount() {
        this.count--;
    }
}
