package com.mbrs.framework.service;

import java.util.List;

public interface BaseService<E, ID> {
    
    E findById(ID id);
    
    List<E> findAll();
    
    E save(E entity);
    
    E update(ID id, E entity);
    
    void delete(ID id);
}
