package com.fdp.nonoo.dao;

import com.fdp.nonoo.entity.Log;

public abstract interface LogDao extends BaseDao<Log, Long> {
	public abstract void removeAll();
}