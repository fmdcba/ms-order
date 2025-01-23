package com.mindhub.ms_order.services;

import java.util.List;

public interface GenericService<E> {

    E findById(Long id) throws Exception;
    List<E> findAll();
    void deleteById(Long id);
    E save(E entity);
    Boolean existsById(Long id);
}
