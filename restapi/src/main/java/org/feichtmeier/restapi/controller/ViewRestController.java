package org.feichtmeier.restapi.controller;

import org.feichtmeier.restapi.entity.View;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/views")
public class ViewRestController extends GenericRestController<View> {

    @Override
    protected View updateEntity(View e, View newE) {

        e.setName(null != newE.getName() ? newE.getName() : e.getName());

        return e;
    }

}