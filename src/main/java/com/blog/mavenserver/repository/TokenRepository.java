package com.blog.mavenserver.repository;

import com.blog.mavenserver.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    
    Optional<Token> findByToken(String token);
    
    List<Token> findByUserId(Long userId);
    
    @Query("SELECT t FROM Token t WHERE t.token = :token AND t.expireTime > :now")
    Optional<Token> findValidToken(@Param("token") String token, @Param("now") LocalDateTime now);
    
    @Modifying
    @Query("DELETE FROM Token t WHERE t.expireTime < :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);
    
    @Modifying
    @Query("DELETE FROM Token t WHERE t.userId = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}