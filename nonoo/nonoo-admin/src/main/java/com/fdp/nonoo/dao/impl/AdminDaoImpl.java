package com.fdp.nonoo.dao.impl;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import com.fdp.nonoo.dao.AdminDao;
import com.fdp.nonoo.entity.Admin;

@Repository("adminDaoImpl")
public class AdminDaoImpl extends BaseDaoImpl<Admin, Long> implements AdminDao {
	public boolean usernameExists(String username) {
		if (username == null)
			return false;
		String sql = "select count(*) from Admin admin where lower(admin.username) = lower(:username)";
		Long count = (Long) entityManager.createQuery(sql, Long.class)
				.setFlushMode(FlushModeType.COMMIT)
				.setParameter("username", username).getSingleResult();
		return count > 0L;
	}

	public Admin findByUsername(String username) {
		if (username == null)
			return null;
		try {
			String sql = "select admin from Admin admin where lower(admin.username) = lower(:username)";
			return (Admin) entityManager.createQuery(sql, Admin.class)
					.setFlushMode(FlushModeType.COMMIT)
					.setParameter("username", username).getSingleResult();
		} catch (NoResultException e) {
			e.printStackTrace();
		}
		return null;
	}
}