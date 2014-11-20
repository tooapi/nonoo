package com.fdp.nonoo.dao;

import com.fdp.nonoo.entity.Admin;

public abstract interface AdminDao extends BaseDao<Admin, Long> {
	public abstract boolean usernameExists(String username);

	public abstract Admin findByUsername(String username);
}