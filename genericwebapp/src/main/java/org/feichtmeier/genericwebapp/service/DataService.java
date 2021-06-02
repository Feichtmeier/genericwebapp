package org.feichtmeier.genericwebapp.service;

import java.util.List;

import org.feichtmeier.genericwebapp.entity.AbstractEntity;

public interface DataService<E extends AbstractEntity> {

    public E getOne(E entity);

    public List<E> findAll();

    public void save(E entity);

    public void delete(E entity);

}
