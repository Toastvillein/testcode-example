package com.example.demo.concurrency.redis;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CounterService {

    private final CounterRepository counterRepository;
    private final RedissonClient redissonClient;
    private final RedisTemplate<String, Long> redisTemplate;
    private final SampleService sampleService;

    private static final String LOCK_KEY = "counterLock";

    @Transactional
    public void save(Counter counter) {
        counterRepository.save(counter);
    }

    @Transactional
    public void reset() {
        Counter counter = counterRepository.findById(1L).orElseThrow();
        counter.initCount();
        counterRepository.save(counter);
    }

    public void decreaseCount() {
        Counter counter = counterRepository.findById(1L).orElseThrow(); // 1000
        counter.decreaseCount();
        counterRepository.save(counter);
    }

//    @Transactional
//    public void decreaseCountUsingLock() {
//        RLock lock = redissonClient.getFairLock(LOCK_KEY);
//        try {
//            boolean isLocked = lock.tryLock(10, 60, TimeUnit.SECONDS);
//            if (isLocked) {
//                Counter counter = counterRepository.findById(1L).orElseThrow();
//                counter.decreaseCount();
//                counterRepository.save(counter);
//            }
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        } finally {
//            lock.unlock();
//        }
//    }

    // 락 부터 해제하고, 트랜잭션 커밋하기 때문에 그 짧은 찰나에 다른 스레드가 또 다시 락을 점유해서 이전 데이터를 그대로 본다.
    @Transactional
    public void decreaseCountUsingLock() {
        RLock lock = redissonClient.getFairLock(LOCK_KEY);
        try {
            boolean isLocked = lock.tryLock(10, 60, TimeUnit.SECONDS);
            if (isLocked) {
                decreaseCount();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    // 1. 트랜잭션 시작
    // 2. 락 점유
    // 3. 락 점유 해제
    // 4. 트랜잭션 커밋


//    @Transactional
//    protected void decreaseCount() {
//        Counter counter = counterRepository.findById(1L).orElseThrow();
//        counter.decreaseCount();
//        counterRepository.save(counter);
//    }

    public void initializeCounter() {
        ValueOperations<String, Long> ops = redisTemplate.opsForValue();
        ops.set("counter", 1000L);
    }

    public void decrementCounter() {
        ValueOperations<String, Long> ops = redisTemplate.opsForValue();
        ops.decrement("counter"); // value 1 감소
    }

    public void getCounter() {
        ValueOperations<String, Long> ops = redisTemplate.opsForValue();
        Long value = ops.get("counter");
        System.out.println(value);
    }

    @DistributedLock(key = "'counter:' + #counterId")
    public void decreaseCountWithAop(Long counterId) {
        // 락이 적용된 로직
        Counter counter = counterRepository.findById(counterId).orElseThrow();
        counter.decreaseCount();
        counterRepository.save(counter);
    }

    // 1. 트랜잭션 커밋
    // 2. 락 점유 해제


    public void printCount() {
        System.out.println("\n\n\n====================\n");
        System.out.println(counterRepository.findById(1L).orElseThrow().getCount());
        System.out.println("\n====================\n\n\n");
    }
}
