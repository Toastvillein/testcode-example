package com.example.demo.concurrency.synchronize;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SynchronizedCounterService {

    private final SynchronizedCounterRepository synchronizedCounterRepository;


    public void save(SynchronizedCounter synchronizedCounter) {
        synchronizedCounterRepository.save(synchronizedCounter);
    }

    public void reset() {
        SynchronizedCounter synchronizedCounter = synchronizedCounterRepository.findById(1L).orElseThrow();
        synchronizedCounter.initCount();
        synchronizedCounterRepository.save(synchronizedCounter);
    }

    // count를 1 감소시키는 메서드
    public void decreaseCount() {
        SynchronizedCounter synchronizedCounter = synchronizedCounterRepository.findById(1L).orElseThrow();
        synchronizedCounter.decreaseCount();
        synchronizedCounterRepository.save(synchronizedCounter);
    }

    public synchronized void decreaseCountWithSynchronized() {
        SynchronizedCounter synchronizedCounter = synchronizedCounterRepository.findById(1L).orElseThrow();
        synchronizedCounter.decreaseCount();
        synchronizedCounterRepository.save(synchronizedCounter);
    }

    // count == 1000
    // count == 999
    // 메서드가 종료되면
    // 1. 트랜잭션 커밋 (DB 에 변경사항 반영)
    // 2. 대기하던 다음 쓰레드가 들어와서 로직 수행

    @Transactional
    public synchronized void decreaseCountWithSynchronizedAndTransactional() {
        SynchronizedCounter synchronizedCounter = synchronizedCounterRepository.findById(1L).orElseThrow();
        synchronizedCounter.decreaseCount();
        synchronizedCounterRepository.save(synchronizedCounter);    // 메서드 종료 이후 Transaction commit -> DB 반영
    }

    public void printCount() {
        SynchronizedCounter synchronizedCounter = synchronizedCounterRepository.findById(1L).orElseThrow();
        System.out.println("count = " + synchronizedCounter.getCount());
    }
}
