package com.mbrs.framework.service;

import com.mbrs.framework.exception.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract class BaseServiceImpl<E, ID> implements BaseService<E, ID> {

    protected abstract JpaRepository<E, ID> getRepository();

    @Override
    public E findById(ID id) {
        return getRepository().findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity with id " + id + " not found"));
    }

    @Override
    public List<E> findAll() {
        return getRepository().findAll();
    }

    @Override
    public E save(E entity) {
        return getRepository().save(entity);
    }

    @Override
    public E update(ID id, E entity) {
        if (!getRepository().existsById(id)) {
            throw new EntityNotFoundException("Entity with id " + id + " not found");
        }
        // Specific field updates are usually handled at the DTO level or specialized service
        // For base implementation, we just save assuming the ID is set
        return getRepository().save(entity);
    }

    @Override
    public void delete(ID id) {
        if (!getRepository().existsById(id)) {
            throw new EntityNotFoundException("Entity with id " + id + " not found");
        }
        getRepository().deleteById(id);
    }
}
