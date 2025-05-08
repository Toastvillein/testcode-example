package com.example.demo.concurrency.synchronize;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SynchronizedCounterRepository extends JpaRepository<SynchronizedCounter, Long> {
}
