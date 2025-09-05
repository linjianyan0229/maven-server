package com.blog.mavenserver.repository;

import com.blog.mavenserver.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    
    @Query("SELECT v FROM VerificationCode v WHERE v.email = :email AND v.code = :code AND v.expireTime > :now ORDER BY v.createdTime DESC")
    Optional<VerificationCode> findValidCode(@Param("email") String email, @Param("code") String code, @Param("now") LocalDateTime now);
    
    @Query("SELECT v FROM VerificationCode v WHERE v.email = :email ORDER BY v.createdTime DESC")
    Optional<VerificationCode> findLatestByEmail(@Param("email") String email);
    
    @Modifying
    @Query("DELETE FROM VerificationCode v WHERE v.expireTime < :now")
    void deleteExpiredCodes(@Param("now") LocalDateTime now);
    
    @Modifying
    @Query("DELETE FROM VerificationCode v WHERE v.email = :email")
    void deleteByEmail(@Param("email") String email);
    
    boolean existsByEmailAndExpireTimeAfter(String email, LocalDateTime now);
}