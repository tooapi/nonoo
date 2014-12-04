package com.fdp.nonoo.service;

import java.io.Serializable;
import java.util.List;

import com.fdp.nonoo.common.Filter;
import com.fdp.nonoo.common.Order;
import com.fdp.nonoo.common.Page;
import com.fdp.nonoo.common.Pageable;

public abstract interface BaseService<T, ID extends Serializable> {
	public abstract T find(ID id);

	public abstract List<T> findAll();

	public abstract List<T> findList(ID[] id);

	public abstract long count();

	public abstract boolean exists(ID id);

	public abstract void save(T entity);

	public abstract T update(T entity);

	public abstract T update(T entity, String[] ignoreProperties);

	public abstract void delete(ID id);

	public abstract void delete(ID[] id);

	public abstract void delete(T entity);
	
	public abstract Page<T> findPage(Pageable pageable);
	
    public abstract List<T> findList(Integer count, List<Filter> filters, List<Order> orders);

	public abstract List<T> findList(Integer  first, Integer count, List<Filter> filters, List<Order> orders);
}