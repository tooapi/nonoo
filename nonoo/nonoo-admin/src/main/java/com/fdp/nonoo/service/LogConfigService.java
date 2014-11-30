package com.fdp.nonoo.service;

import java.util.List;

import com.fdp.nonoo.common.LogConfig;

public abstract interface LogConfigService {
	public abstract List<LogConfig> getAll();
}