package com.blog.mavenserver.repository;

import com.blog.mavenserver.entity.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConfigRepository extends JpaRepository<Config, Long> {
    
    Optional<Config> findByConfigKey(String configKey);
    
    @Query("SELECT c.configValue FROM Config c WHERE c.configKey = :configKey")
    Optional<String> findValueByConfigKey(@Param("configKey") String configKey);
    
    List<Config> findByStatus(String status);
    
    @Query("SELECT c FROM Config c WHERE c.status = '公开'")
    List<Config> findPublicConfigs();
    
    @Query("SELECT c FROM Config c WHERE c.status = '私人'")
    List<Config> findPrivateConfigs();
    
    boolean existsByConfigKey(String configKey);
}