package com.fdp.nonoo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fdp.nonoo.common.Filter;
import com.fdp.nonoo.common.Order;
import com.fdp.nonoo.dao.FriendLinkDao;
import com.fdp.nonoo.entity.FriendLink;
import com.fdp.nonoo.service.FriendLinkService;

@Service("friendLinkService")
public class FriendLinkServiceImpl extends BaseServiceImpl<FriendLink, Long>
		implements FriendLinkService {

	@Resource(name = "friendLinkDao")
	public FriendLinkDao friendLinkDao;

	@Resource(name = "friendLinkDao")
	public void setBaseDao(FriendLinkDao friendLinkDao) {
		super.setBaseDao(friendLinkDao);
	}

	@Transactional(readOnly = true)
	public List<FriendLink> findList(FriendLink.Type type) {
		return this.friendLinkDao.findList(type);
	}

	@Transactional(readOnly = true)
	@Cacheable({ "friendLink" })
	public List<FriendLink> findList(Integer count, List<Filter> filters,
			List<Order> orders, String cacheRegion) {
		return this.friendLinkDao.findList(null, count, filters, orders);
	}

	@Transactional
	@CacheEvict(value = { "friendLink" }, allEntries = true)
	public void save(FriendLink friendLink) {
		super.save(friendLink);
	}

	@Transactional
	@CacheEvict(value = { "friendLink" }, allEntries = true)
	public FriendLink update(FriendLink friendLink) {
		return ((FriendLink) super.update(friendLink));
	}

	@Transactional
	@CacheEvict(value = { "friendLink" }, allEntries = true)
	public FriendLink update(FriendLink friendLink, String[] ignoreProperties) {
		return ((FriendLink) super.update(friendLink, ignoreProperties));
	}

	@Transactional
	@CacheEvict(value = { "friendLink" }, allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Transactional
	@CacheEvict(value = { "friendLink" }, allEntries = true)
	public void delete(Long[] ids) {
		super.delete(ids);
	}

	@Transactional
	@CacheEvict(value = { "friendLink" }, allEntries = true)
	public void delete(FriendLink friendLink) {
		super.delete(friendLink);
	}
}