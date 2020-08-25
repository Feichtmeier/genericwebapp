package org.feichtmeier.genericwebapp.repository;

import org.feichtmeier.genericwebapp.entity.AbstractEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract interface GenericRepository<T extends AbstractEntity> extends JpaRepository<T, Long> {

}