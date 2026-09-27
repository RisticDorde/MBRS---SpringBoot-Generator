package com.mbrs.framework.mapper;

import org.mapstruct.TargetType;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
public class ReferenceMapper {

    @PersistenceContext
    private EntityManager entityManager;

    public <T extends com.mbrs.framework.entity.BaseEntity> T resolve(Long id, @TargetType Class<T> entityClass) {
        return id != null ? entityManager.getReference(entityClass, id) : null;
    }
}
