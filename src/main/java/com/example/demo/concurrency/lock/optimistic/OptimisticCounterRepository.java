package com.example.demo.concurrency.lock.optimistic;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OptimisticCounterRepository extends JpaRepository<OptimisticCounter, Long> {


    @Lock(LockModeType.OPTIMISTIC)
    @Query("select p from OptimisticCounter p where p.id in :id")
    Optional<OptimisticCounter> findByIdWithOptimisticLock(Long id);
}
