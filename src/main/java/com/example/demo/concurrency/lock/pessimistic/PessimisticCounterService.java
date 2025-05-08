package com.example.demo.concurrency.lock.pessimistic;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PessimisticCounterService {

    private final PessimisticCounterRepository pessimisticCounterRepository;

    public void save(PessimisticCounter pessimisticCounter) {
        pessimisticCounterRepository.save(pessimisticCounter);
    }

    public void reset() {
        PessimisticCounter pessimisticCounter = pessimisticCounterRepository.findById(1L).orElseThrow();
        pessimisticCounter.initCount();
        pessimisticCounterRepository.save(pessimisticCounter);
    }

    @Transactional
    public void decreaseCount() {
        PessimisticCounter pessimisticCounter = pessimisticCounterRepository.findById(1L).orElseThrow();
        pessimisticCounter.decreaseCount();
        pessimisticCounterRepository.save(pessimisticCounter);
    }

    @Transactional
    public void decreaseCountWithLock() {
        PessimisticCounter pessimisticCounter = pessimisticCounterRepository.findByIdWithPessimisticLock(1L).orElseThrow();
        pessimisticCounter.decreaseCount();
        pessimisticCounterRepository.save(pessimisticCounter);
    }

    public void printCount() {
        PessimisticCounter pessimisticCounter = pessimisticCounterRepository.findById(1L).orElseThrow();
        System.out.println("count = " + pessimisticCounter.getCount());
    }
}
