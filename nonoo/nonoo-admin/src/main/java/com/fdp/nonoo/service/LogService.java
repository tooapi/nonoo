package com.fdp.nonoo.service;

import com.fdp.nonoo.entity.Log;

public abstract interface LogService extends BaseService<Log, Long> {
	public abstract void clear();
}