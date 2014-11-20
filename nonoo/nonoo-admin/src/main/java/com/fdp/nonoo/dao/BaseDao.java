package com.fdp.nonoo.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.LockModeType;

import com.fdp.nonoo.common.Filter;
import com.fdp.nonoo.common.Order;
import com.fdp.nonoo.common.Page;
import com.fdp.nonoo.common.Pageable;

public abstract interface BaseDao<T, ID extends Serializable> {
	public abstract T find(ID id);

	public abstract List<T> findList(Integer first, Integer count,List<Filter> filters, List<Order> orders);

	public abstract Page<T> findPage(Pageable pageable);

	public abstract long count(Filter[] filters);

	public abstract void persist(T entity);

	public abstract T merge(T entity);

	public abstract void remove(T entity);

	public abstract void refresh(T entity);

	public abstract ID getIdentifier(T entity);

	public abstract boolean isManaged(T entity);

	public abstract void detach(T entity);

	public abstract void lock(T entity, LockModeType type);

	public abstract void clear();

	public abstract void flush();
}