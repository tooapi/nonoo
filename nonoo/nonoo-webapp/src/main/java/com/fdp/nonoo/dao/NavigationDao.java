package com.fdp.nonoo.dao;

import java.util.List;

import com.fdp.nonoo.entity.Navigation;

public abstract interface NavigationDao extends BaseDao<Navigation, Long> {
	public abstract List<Navigation> findList(Navigation.Position position);
}