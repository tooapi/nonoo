package com.fdp.nonoo.service;

import java.util.List;

import com.fdp.nonoo.common.Filter;
import com.fdp.nonoo.common.Order;
import com.fdp.nonoo.entity.FriendLink;

public abstract interface FriendLinkService extends
		BaseService<FriendLink, Long> {
	public abstract List<FriendLink> findList(FriendLink.Type type);

	public abstract List<FriendLink> findList(Integer count,List<Filter> filters, List<Order> orders, String cacheRegion);
}