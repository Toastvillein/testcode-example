package com.example.demo.concurrency.lock.pessimistic;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
class PessimisticCounterServiceTest {

    @Autowired
    private PessimisticCounterService pessimisticCounterService;

    @BeforeEach
    void setUp() {
        pessimisticCounterService.save(new PessimisticCounter(1000));
    }

    @AfterEach
    void tearDown() {
        pessimisticCounterService.reset();
    }


    @Test
    @DisplayName("동시성 이슈 발생")
    void concurrencyTest() {
        System.out.println("\n\n\n\n[concurrencyTestWithPessimisticLock]");

        pessimisticCounterService.printCount();

        IntStream.range(0, 1000).parallel().forEach(i -> {
            pessimisticCounterService.decreaseCount();
        });

        pessimisticCounterService.printCount();
    }

    @Test
    @DisplayName("비관적 락을 이용한 동시성 제어")
    void concurrencyTestWithPessimisticLock() {
        System.out.println("\n\n\n\n[concurrencyTestWithPessimisticLock]");

        pessimisticCounterService.printCount();

        IntStream.range(0, 1000).parallel().forEach(i -> {
            pessimisticCounterService.decreaseCountWithLock();
        });

        pessimisticCounterService.printCount();
    }


}