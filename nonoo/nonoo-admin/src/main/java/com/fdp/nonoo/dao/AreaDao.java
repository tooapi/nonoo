package com.fdp.nonoo.dao;

import java.util.List;

import com.fdp.nonoo.entity.Area;

public abstract interface AreaDao extends BaseDao<Area, Long> {
	public abstract List<Area> findRoots(Integer count);
}