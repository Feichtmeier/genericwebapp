package org.feichtmeier.genericwebapp.controller;

import org.feichtmeier.genericwebapp.entity.View;

public class ViewRestController extends GenericRestController<View> {

    @Override
    protected View updateEntity(View e, View newE) {

        e.setName(newE.getName());
        e.setPermission(e.getPermission());

        return e;
    }
    
}