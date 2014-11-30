package com.fdp.nonoo.dao.impl;

import javax.persistence.FlushModeType;

import org.springframework.stereotype.Repository;

import com.fdp.nonoo.dao.LogDao;
import com.fdp.nonoo.entity.Log;

@Repository("logDao")
public class LogDaoImpl extends BaseDaoImpl<Log, Long> implements LogDao {
	public void removeAll() {
		String sql = "delete from Log log";
		this.entityManager.createQuery(sql).setFlushMode(FlushModeType.COMMIT).executeUpdate();
	}
}