package com.example.demo.concurrency.lock.pessimistic;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.Optional;

public interface PessimisticCounterRepository extends JpaRepository<PessimisticCounter, Long> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000")})  // timeout 설정 (3초)
    @Query("select p from PessimisticCounter p where p.id in :id")
    Optional<PessimisticCounter> findByIdWithPessimisticLock(Long id);
}
