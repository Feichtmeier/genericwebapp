package org.feichtmeier.genericwebapp.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.feichtmeier.genericwebapp.entity.Project;
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

    public default List<User> listUserByProjectName(String projectName, List<User> allowedUsers) {
        if (null == projectName) {
            return allowedUsers;
        } else {
            List<User> matchedUsers = new ArrayList<>();
            for (User user : allowedUsers) {
                if (null != user.getProjects() && projectSetContainsProject(projectName, user.getProjects())) {
                    matchedUsers.add(user);
                }
            }
            return matchedUsers;
        }

    }

    public default boolean projectSetContainsProject(String project, Set<Project> projects) {
        boolean projectNameIsInProjectSet = false;
        for (Project p : projects) {
            if (project.equals(p.getName())) {
                projectNameIsInProjectSet = true;
                break;
            }
        }
        return projectNameIsInProjectSet;
    }

}
