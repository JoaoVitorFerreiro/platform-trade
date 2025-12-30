package com.plataformtrade.infra.persistence.repositories;

import com.plataformtrade.infra.persistence.entities.OutboxEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxEventJpaRepository extends JpaRepository<OutboxEventEntity, String> {
    List<OutboxEventEntity> findTop100ByStatusOrderByOccurredOnAsc(String status);
}
