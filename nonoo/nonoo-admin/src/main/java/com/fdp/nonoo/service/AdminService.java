package com.fdp.nonoo.service;

import java.util.List;

import com.fdp.nonoo.entity.Admin;

public abstract interface AdminService extends BaseService<Admin, Long> {
	public abstract boolean usernameExists(String username);

	public abstract Admin findByUsername(String username);

	public abstract List<String> findAuthorities(Long username);

	public abstract boolean isAuthenticated();

	public abstract Admin getCurrent();

	public abstract String getCurrentUsername();
}