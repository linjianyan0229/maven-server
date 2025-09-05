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
    
    @Query("SELECT v FROM VerificationCode v WHERE v.email = :email AND v.code = :code AND v.type = :type AND v.expireTime > :now ORDER BY v.createdTime DESC")
    Optional<VerificationCode> findValidCode(@Param("email") String email, @Param("code") String code, @Param("type") String type, @Param("now") LocalDateTime now);
    
    // 兼容旧版本的方法（默认查询注册类型）
    @Query("SELECT v FROM VerificationCode v WHERE v.email = :email AND v.code = :code AND v.type = '注册' AND v.expireTime > :now ORDER BY v.createdTime DESC")
    Optional<VerificationCode> findValidCode(@Param("email") String email, @Param("code") String code, @Param("now") LocalDateTime now);
    
    @Query("SELECT v FROM VerificationCode v WHERE v.email = :email AND v.type = :type ORDER BY v.createdTime DESC")
    Optional<VerificationCode> findLatestByEmailAndType(@Param("email") String email, @Param("type") String type);
    
    @Query("SELECT v FROM VerificationCode v WHERE v.email = :email ORDER BY v.createdTime DESC")
    Optional<VerificationCode> findLatestByEmail(@Param("email") String email);
    
    @Modifying
    @Query("DELETE FROM VerificationCode v WHERE v.expireTime < :now")
    void deleteExpiredCodes(@Param("now") LocalDateTime now);
    
    @Modifying
    @Query("DELETE FROM VerificationCode v WHERE v.email = :email AND v.type = :type")
    void deleteByEmailAndType(@Param("email") String email, @Param("type") String type);
    
    @Modifying
    @Query("DELETE FROM VerificationCode v WHERE v.email = :email")
    void deleteByEmail(@Param("email") String email);
    
    boolean existsByEmailAndTypeAndExpireTimeAfter(String email, String type, LocalDateTime now);
    
    boolean existsByEmailAndExpireTimeAfter(String email, LocalDateTime now);
}