package org.feichtmeier.genericwebapp.view;

import java.util.ArrayList;
import java.util.List;

import org.feichtmeier.genericwebapp.entity.User;

public interface UserFilter {

    public default List<User> listUsersByFullName(String filterText, List<User> allowedUsers) {
        if (null == filterText) {
            return allowedUsers;
        } else {
            List<User> matchedUsers = new ArrayList<>();
            for (User user : allowedUsers) {
                if (null != user.getFullName()
                        && user.getFullName().toUpperCase().startsWith(filterText.toUpperCase())) {
                    matchedUsers.add(user);
                }
            }
            return matchedUsers;
        }

    }

}
