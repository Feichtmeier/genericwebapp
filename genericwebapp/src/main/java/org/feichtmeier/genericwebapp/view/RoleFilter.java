package org.feichtmeier.genericwebapp.view;

import java.util.ArrayList;
import java.util.List;

import org.feichtmeier.genericwebapp.entity.Role;

public interface RoleFilter {

    public default List<Role> listRolesByName(String filterText, List<Role> allowedRoles) {
        if (null == filterText) {
            return allowedRoles;
        } else {
            List<Role> matchedRoles = new ArrayList<>();
            for (Role role : allowedRoles) {
                if (null != role.getName() && role.getName().toUpperCase().startsWith(filterText.toUpperCase())) {
                    matchedRoles.add(role);
                }
            }
            return matchedRoles;
        }
    }
}
