package com.example.demo.concurrency.redis;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
public class CounterServiceTest {

    @Autowired
    private CounterService counterService;

    @BeforeEach
    void setUp() {
        counterService.save(new Counter(1000));
    }

    @AfterEach
    void tearDown() {
        counterService.reset();
    }


    @Test
    void concurrencyTest() {
        System.out.println("\n\n\n\n[concurrencyTest]");
        IntStream.range(0, 1000).parallel().forEach(i -> counterService.decreaseCount());
        counterService.printCount();
    }

    @Test
    void concurrencyTestUsingLock() {
        System.out.println("\n\n\n\n[concurrencyTestUsingLock]");
        IntStream.range(0, 1000).parallel().forEach(i -> counterService.decreaseCountUsingLock());
        counterService.printCount();
    }

    @Test
    @DisplayName("AOP 를 활용한 분산락")
    void concurrencyTestWithAop() {
        System.out.println("\n\n\n\n[concurrencyTestWithAOP]");
        IntStream.range(0, 1000).parallel().forEach(i -> counterService.decreaseCountWithAop(1L));
        counterService.printCount();
    }

    @Test
    @DisplayName("Redis의 싱글 스레드 특성을 이용한 동시성 제어")
    void concurrencyTestWithRedis() {
        counterService.initializeCounter();
        counterService.getCounter();
        System.out.println("\n\n\n\n[concurrencyTestNoLockWithRedis]");
        IntStream.range(0, 1000).parallel().forEach(i -> counterService.decrementCounter());
        counterService.getCounter();
    }
}
