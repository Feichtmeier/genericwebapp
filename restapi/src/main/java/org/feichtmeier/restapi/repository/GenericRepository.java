package org.feichtmeier.restapi.repository;

import org.feichtmeier.restapi.entity.AbstractEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract interface GenericRepository<T extends AbstractEntity> extends JpaRepository<T, Long> {

}