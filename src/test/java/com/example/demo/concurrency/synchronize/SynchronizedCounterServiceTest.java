package com.example.demo.concurrency.synchronize;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
class SynchronizedCounterServiceTest {

    @Autowired
    private SynchronizedCounterService synchronizedCounterService;

    @BeforeEach
    void setUp() {
        synchronizedCounterService.save(new SynchronizedCounter(1000));
    }

    @AfterEach
    void tearDown() {
        synchronizedCounterService.reset();
    }

    @Test
    @DisplayName("동시성 이슈 발생")
    void concurrencyTest() {
        System.out.println("\n\n\n\n[concurrencyTest]\n");

        synchronizedCounterService.printCount();    // 초기 값 출력

        IntStream.range(0, 1000).parallel().forEach(i -> {
            synchronizedCounterService.decreaseCount();
        });

        synchronizedCounterService.printCount();
    }

    @Test
    @DisplayName("Synchronized를 이용한 동시성 제어")
    void concurrencyTestWithSynchronized() {
        System.out.println("\n\n\n\n[concurrencyTestWithSynchronized]\n");

        synchronizedCounterService.printCount();    // 초기 값 출력

        IntStream.range(0, 1000).parallel().forEach(i -> {
            synchronizedCounterService.decreaseCountWithSynchronized();
        });

        synchronizedCounterService.printCount();
    }

    @Test
    @DisplayName("Synchronized & Transactional 를 이용한 동시성 제어")
    void concurrencyTestWithSynchronizedAndTransactional() {
        System.out.println("\n\n\n\n[concurrencyTestWithSynchronizedAndTransactional]\n");

        synchronizedCounterService.printCount();    // 초기 값 출력

        IntStream.range(0, 1000).parallel().forEach(i -> {
            synchronizedCounterService.decreaseCountWithSynchronizedAndTransactional();
        });

        synchronizedCounterService.printCount();
    }
}