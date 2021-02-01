package org.feichtmeier.genericwebapp.view;

import org.feichtmeier.genericwebapp.entity.AbstractEntity;

public interface EntityEditor<E extends AbstractEntity> {

    public void editEntity(E entity);

}
