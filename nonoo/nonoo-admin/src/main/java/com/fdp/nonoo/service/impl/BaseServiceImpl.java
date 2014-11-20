package com.fdp.nonoo.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.fdp.nonoo.common.Filter;
import com.fdp.nonoo.common.Order;
import com.fdp.nonoo.common.Page;
import com.fdp.nonoo.common.Pageable;
import com.fdp.nonoo.dao.BaseDao;
import com.fdp.nonoo.service.BaseService;

@Transactional
public class BaseServiceImpl<T, ID extends Serializable> implements
		BaseService<T, ID> {

	private BaseDao<T, ID> baseDao;

	public void setBaseDao(BaseDao<T, ID> baseDao) {
		this.baseDao = baseDao;
	}

	@Transactional(readOnly = true)
	public T find(ID id) {
		return baseDao.find(id);
	}

	@Transactional(readOnly = true)
	public List<T> findAll() {
		return findList(null, null, null, null);
	}

	@Transactional(readOnly = true)
	public List<T> findList(ID[] ids) {
		List<T> results = new ArrayList<T>();
		if (ids != null)
			for (ID id : ids) {
				T t = find(id);
				if (t == null)
					continue;
				results.add(t);
			}
		return results;
	}

	@Transactional(readOnly = true)
	public List<T> findList(Integer count, List<Filter> filters,
			List<Order> orders) {
		return findList(null, count, filters, orders);
	}

	@Transactional(readOnly = true)
	public List<T> findList(Integer first, Integer count, List<Filter> filters,
			List<Order> orders) {
		return baseDao.findList(first, count, filters, orders);
	}

	@Transactional(readOnly = true)
	public Page<T> findPage(Pageable pageable) {
		return baseDao.findPage(pageable);
	}

	@Transactional(readOnly = true)
	public long count() {
		return count(new Filter[0]);
	}

	@Transactional(readOnly = true)
	public long count(Filter[] filters) {
		return baseDao.count(filters);
	}

	@Transactional(readOnly = true)
	public boolean exists(ID id) {
		return baseDao.find(id) != null;
	}

	@Transactional(readOnly = true)
	public boolean exists(Filter[] filters) {
		return baseDao.count(filters) > 0L;
	}

	@Transactional
	public void save(T entity) {
		baseDao.persist(entity);
	}

	@Transactional
	public T update(T entity) {
		return baseDao.merge(entity);
	}

	@Transactional
	public T update(T entity, String[] ignoreProperties) {
		Assert.notNull(entity);
		if (baseDao.isManaged(entity))
			throw new IllegalArgumentException("Entity must not be managed");
		Object localObject = baseDao.find(baseDao.getIdentifier(entity));
		if (localObject != null) {

		}
		return update(entity);
	}

	@Transactional
	public void delete(ID id) {
		delete(baseDao.find(id));
	}

	@Transactional
	public void delete(ID[] ids) {
		if (ids == null)
			return;
		for (ID localSerializable : ids)
			delete(baseDao.find(localSerializable));
	}

	@Transactional
	public void delete(T entity) {
		baseDao.remove(entity);
	}

}