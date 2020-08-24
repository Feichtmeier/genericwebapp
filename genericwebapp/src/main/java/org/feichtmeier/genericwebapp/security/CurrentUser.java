package org.feichtmeier.genericwebapp.security;

import org.feichtmeier.genericwebapp.entity.User;

@FunctionalInterface
public interface CurrentUser {

	User getUser();
}