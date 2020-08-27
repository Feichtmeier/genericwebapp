package org.feichtmeier.genericwebapp.controller;

import org.feichtmeier.genericwebapp.entity.View;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/views")
public class ViewRestController extends GenericRestController<View> {

    @Override
    protected View updateEntity(View e, View newE) {

        e.setName(newE.getName());

        return e;
    }
    
}