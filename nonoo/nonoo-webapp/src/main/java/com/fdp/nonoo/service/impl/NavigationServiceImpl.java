package com.fdp.nonoo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fdp.nonoo.common.Filter;
import com.fdp.nonoo.common.Order;
import com.fdp.nonoo.dao.NavigationDao;
import com.fdp.nonoo.entity.Navigation;
import com.fdp.nonoo.service.NavigationService;

@Service("navigationService")
public class NavigationServiceImpl extends BaseServiceImpl<Navigation, Long>
		implements NavigationService {

	@Resource(name = "navigationDao")
	private NavigationDao navigationDao;

	@Resource(name = "navigationDao")
	public void setBaseDao(NavigationDao navigationDao) {
		super.setBaseDao(navigationDao);
	}

	@Transactional(readOnly = true)
	public List<Navigation> findList(Navigation.Position position) {
		return navigationDao.findList(position);
	}

	
	@Transactional(readOnly = true)
	@Cacheable({ "navigation" })
	public List<Navigation> findList(Integer count, List<Filter> filters,
			List<Order> orders, String cacheRegion) {
		return navigationDao.findList(null, count, filters, orders);
	}

	@Transactional
	@CacheEvict(value = { "navigation" }, allEntries = true)
	public void save(Navigation navigation) {
		super.save(navigation);
	}

	@Transactional
	@CacheEvict(value = { "navigation" }, allEntries = true)
	public Navigation update(Navigation navigation) {
		return ((Navigation) super.update(navigation));
	}

	@Transactional
	@CacheEvict(value = { "navigation" }, allEntries = true)
	public Navigation update(Navigation navigation, String[] ignoreProperties) {
		return ((Navigation) super.update(navigation, ignoreProperties));
	}

	@Transactional
	@CacheEvict(value = { "navigation" }, allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Transactional
	@CacheEvict(value = { "navigation" }, allEntries = true)
	public void delete(Long[] ids) {
		super.delete(ids);
	}

	@Transactional
	@CacheEvict(value = { "navigation" }, allEntries = true)
	public void delete(Navigation navigation) {
		super.delete(navigation);
	}

	
}