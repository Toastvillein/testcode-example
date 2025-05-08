package com.example.demo.concurrency.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SampleService {

    private final CounterRepository counterRepository;

    @DistributedLock(key = "'sample:' + #sampleId")
    public void decreaseCount() {
        Counter counter = counterRepository.findById(1L).orElseThrow();
        counter.decreaseCount();
        counterRepository.save(counter);
    }
}
