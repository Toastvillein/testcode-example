package com.example.demo.concurrency.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
//@Order(0)
public class DistributedLockAspect {

    private final RedissonClient redissonClient;
    private final PlatformTransactionManager transactionManager;

//    @Around("@annotation(distributedLock)")
//    public Object lock(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
//        String key = distributedLock.key();
//        long waitTime = distributedLock.waitTime();
//        long leaseTime = distributedLock.leaseTime();
//
//        RLock lock = redissonClient.getLock(key);
//
//        boolean isLocked = false;
//        try {
//            isLocked = lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
//            if (!isLocked) {
//                throw new IllegalStateException("락 획득 실패: key = " + key);
//            }
//
//            log.info("락 획득 성공: key = {}", key);
//            return joinPoint.proceed(); // 비즈니스 로직 실행 (decreaseCount())
//        } finally {
//            if (isLocked && lock.isHeldByCurrentThread()) {
//                lock.unlock();
//                log.info("락 해제: key = {}", key);
//            }
//        }
//    }


    @Around("@annotation(distributedLock)")
    public Object lock(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        String key = distributedLock.key();
        long waitTime = distributedLock.waitTime();
        long leaseTime = distributedLock.leaseTime();

        RLock lock = redissonClient.getLock(key);

        boolean isLocked = false;
        try {
            isLocked = lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
            if (!isLocked) {
                throw new IllegalStateException("락 획득 실패: key = " + key);
            }

            log.info("락 획득 성공: key = {}", key);
            TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
            transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

            return transactionTemplate.execute(status -> {
                try {
                    return joinPoint.proceed(); // 비즈니스 로직 실행
                } catch (Throwable throwable) {
                    // 트랜잭션 롤백을 명시적으로 설정
                    status.setRollbackOnly();
                    throw new RuntimeException(throwable);
                }
            });
        } finally {
            if (isLocked && lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.info("락 해제: key = {}", key);
            }
        }
    }
}