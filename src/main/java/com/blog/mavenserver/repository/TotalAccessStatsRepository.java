package com.blog.mavenserver.repository;

import com.blog.mavenserver.entity.TotalAccessStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TotalAccessStatsRepository extends JpaRepository<TotalAccessStats, Long> {
    
    @Query("SELECT t FROM TotalAccessStats t ORDER BY t.id LIMIT 1")
    Optional<TotalAccessStats> findFirst();
}