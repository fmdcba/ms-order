package com.mindhub.ms_order.services;

import com.mindhub.ms_order.exceptions.NotAuthorizedException;
import com.mindhub.ms_order.exceptions.NotFoundException;
import org.springframework.http.HttpEntity;

import java.util.List;

public interface GenericService<E> {

    E findById(Long id) throws Exception;
    List<E> findAll();
    void deleteById(Long id);
    E save(E entity);
    void existsById(Long id) throws NotFoundException;
    <T> HttpEntity<T> createAuthHttpEntity(T body);
    void validateIsAdmin() throws NotAuthorizedException;
}
