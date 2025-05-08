package com.example.demo.concurrency.lock.optimistic;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OptimisticCounterService {

    private final OptimisticCounterRepository optimisticCounterRepository;

    @Transactional
    public void save(OptimisticCounter optimisticCounter) {
        optimisticCounterRepository.save(optimisticCounter);
    }

    @Transactional
    public void reset() {
        OptimisticCounter optimisticCounter = optimisticCounterRepository.findById(1L).orElseThrow();
        optimisticCounter.initCount();
        optimisticCounterRepository.save(optimisticCounter);
    }

    @Transactional
    public void decreaseCountWithLock() {
        OptimisticCounter optimisticCounter = optimisticCounterRepository.findByIdWithOptimisticLock(1L).orElseThrow();
        optimisticCounter.decreaseCount();
        optimisticCounterRepository.save(optimisticCounter);
    }

    public void printCount() {
        OptimisticCounter optimisticCounter = optimisticCounterRepository.findById(1L).orElseThrow();
        System.out.println("count = " + optimisticCounter.getCount() + ", version = " + optimisticCounter.getVersion());
    }
}
