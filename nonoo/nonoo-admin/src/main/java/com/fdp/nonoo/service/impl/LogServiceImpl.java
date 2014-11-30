package com.fdp.nonoo.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fdp.nonoo.dao.LogDao;
import com.fdp.nonoo.entity.Log;
import com.fdp.nonoo.service.LogService;

@Service("logService")
public class LogServiceImpl extends BaseServiceImpl<Log, Long> implements
		LogService {

	@Resource(name = "logDao")
	private LogDao logDao;

	@Resource(name = "logDao")
	public void setBaseDao(LogDao logDao) {
		super.setBaseDao(logDao);
	}

	public void clear() {
		this.logDao.removeAll();
	}
}