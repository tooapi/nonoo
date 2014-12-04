package com.fdp.nonoo.service;

import java.util.List;

import com.fdp.nonoo.common.Filter;
import com.fdp.nonoo.common.Order;
import com.fdp.nonoo.entity.Navigation;

public abstract interface NavigationService extends
		BaseService<Navigation, Long> {
	public abstract List<Navigation> findList(Navigation.Position position);
   
	public abstract List<Navigation> findList(Integer count,List<Filter> filters, List<Order> orders, String cacheRegion);
}