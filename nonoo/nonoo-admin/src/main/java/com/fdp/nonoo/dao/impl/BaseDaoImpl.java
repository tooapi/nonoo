package com.fdp.nonoo.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import com.fdp.nonoo.common.Filter;
import com.fdp.nonoo.common.Order;
import com.fdp.nonoo.common.Page;
import com.fdp.nonoo.common.Pageable;
import com.fdp.nonoo.dao.BaseDao;
import com.fdp.nonoo.entity.OrderEntity;

public abstract class BaseDaoImpl<T, ID extends Serializable> implements
		BaseDao<T, ID> {
	private Class<T> entityClass;
	private static volatile long IIIlllII = 0L;

	@PersistenceContext
	protected EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public BaseDaoImpl() {
		Type type = super.getClass().getGenericSuperclass();
		Type[] trueType = ((ParameterizedType) type).getActualTypeArguments();
		entityClass = (Class<T>) trueType[0];

	}

	public T find(ID id) {
		if (id != null)
			return entityManager.find(entityClass, id);
		return null;
	}

	public void persist(T entity) {
		Assert.notNull(entity);
		entityManager.persist(entity);
	}

	public T merge(T entity) {
		Assert.notNull(entity);
		return entityManager.merge(entity);
	}

	public void remove(T entity) {
		if (entity == null)
			return;
		entityManager.remove(entity);
	}

	public void refresh(T entity) {
		Assert.notNull(entity);
		entityManager.refresh(entity);
	}

	@SuppressWarnings("unchecked")
	public ID getIdentifier(T entity) {
		Assert.notNull(entity);
		ID id = (ID) entityManager.getEntityManagerFactory()
				.getPersistenceUnitUtil().getIdentifier(entity);
		return id;
	}

	public boolean isManaged(T entity) {
		return entityManager.contains(entity);
	}

	public void detach(T entity) {
		entityManager.detach(entity);
	}

	public void lock(T entity, LockModeType type) {
		if ((entity == null) || (type == null))
			return;
		entityManager.lock(entity, type);
	}

	public void clear() {
		entityManager.clear();
	}

	public void flush() {
		entityManager.flush();
	}

	public List<T> findList(Integer first, Integer count, List<Filter> filters,
			List<Order> orders) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = criteriaBuilder
				.createQuery(entityClass);
		criteriaQuery.select(criteriaQuery.from(entityClass));
		return listQuery(criteriaQuery, first, count, filters, orders);
	}

	public Page<T> findPage(Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = criteriaBuilder
				.createQuery(entityClass);
		criteriaQuery.select(criteriaQuery.from(entityClass));
		return pageQuery(criteriaQuery, pageable);
	}

	public long count(Filter[] filters) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = criteriaBuilder
				.createQuery(entityClass);
		criteriaQuery.select(criteriaQuery.from(entityClass));
		return queryCount(criteriaQuery,
				(filters != null) ? Arrays.asList(filters) : null);
	}

	@SuppressWarnings("rawtypes")
	protected List<T> listQuery(CriteriaQuery<T> query, Integer first,
			Integer count, List<Filter> filters, List<Order> orders) {
		Assert.notNull(query);
		Assert.notNull(query.getSelection());
		Assert.notEmpty(query.getRoots());
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		Root root = getRoot(query);
		queryFilter(query, filters);
		queryOrder(query, orders);
		if (query.getOrderList().isEmpty())
			if (OrderEntity.class.isAssignableFrom(entityClass))
				query.orderBy(new javax.persistence.criteria.Order[] { criteriaBuilder
						.asc(root.get("order")) });
			else
				query.orderBy(new javax.persistence.criteria.Order[] { criteriaBuilder
						.desc(root.get("createDate")) });
		TypedQuery<T> typedQuery = entityManager.createQuery(query)
				.setFlushMode(FlushModeType.COMMIT);
		if (first != null)
			typedQuery.setFirstResult(first);
		if (count != null)
			typedQuery.setMaxResults(count);
		return typedQuery.getResultList();
	}

	@SuppressWarnings("rawtypes")
	protected Page<T> pageQuery(CriteriaQuery<T> query, Pageable pageable) {
		Assert.notNull(query);
		Assert.notNull(query.getSelection());
		Assert.notEmpty(query.getRoots());
		if (pageable == null)
			pageable = new Pageable();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		Root root = getRoot(query);
		getPageFilter(query, pageable);
		getPageOrder(query, pageable);
		if (query.getOrderList().isEmpty()) {
			if (OrderEntity.class.isAssignableFrom(entityClass))
				query.orderBy(new javax.persistence.criteria.Order[] { criteriaBuilder
						.asc(root.get("order")) });
			else
				query.orderBy(new javax.persistence.criteria.Order[] { criteriaBuilder
						.desc(root.get("createDate")) });
		}
		long total = queryCount(query, null);
		int pageSize = (int) Math.ceil(total / pageable.getPageSize());
		if (pageSize < pageable.getPageNumber())
			pageable.setPageNumber(pageSize);
		TypedQuery<T> typedQuery = entityManager.createQuery(query)
				.setFlushMode(FlushModeType.COMMIT);
		typedQuery.setFirstResult((pageable.getPageNumber() - 1)
				* pageable.getPageSize());
		typedQuery.setMaxResults(pageable.getPageSize());
		return new Page<T>(typedQuery.getResultList(), total, pageable);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Long queryCount(CriteriaQuery<T> query, List<Filter> filterList) {
		Assert.notNull(query);
		Assert.notNull(query.getSelection());
		Assert.notEmpty(query.getRoots());
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		queryFilter(query, filterList);
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder
				.createQuery(Long.class);
		Iterator iterator = query.getRoots().iterator();
		Root root;
		while (iterator.hasNext()) {
			root = (Root) iterator.next();
			Root root2 = criteriaQuery.from(root.getJavaType());
			root2.alias(getAlias(root));
			join(root, root2);
		}
		Root root1 = getRoot(criteriaQuery, query.getResultType());
		criteriaQuery.select(criteriaBuilder.count(root1));
		if (query.getGroupList() != null)
			criteriaQuery.groupBy(query.getGroupList());
		if (query.getGroupRestriction() != null)
			criteriaQuery.having(query.getGroupRestriction());
		if (query.getRestriction() != null)
			criteriaQuery.where(query.getRestriction());
		return entityManager.createQuery(criteriaQuery)
				.setFlushMode(FlushModeType.COMMIT).getSingleResult();
	}

	private synchronized String getAlias(Selection<?> selection) {
		if (selection != null) {
			String alias = selection.getAlias();
			if (alias == null) {
				if (IIIlllII >= 1000L)
					IIIlllII = 0L;
				alias = "fdpGeneratedAlias" + (IIIlllII++);
				selection.alias(alias);
			}
			return alias;
		}
		return null;
	}

	private Root<T> getRoot(CriteriaQuery<T> query) {
		if (query != null)
			return getRoot(query, query.getResultType());
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Root<T> getRoot(CriteriaQuery<?> query, Class<T> paramClass) {
		if ((query != null) && (query.getRoots() != null)
				&& (paramClass != null)) {
			Iterator iterator = query.getRoots().iterator();
			while (iterator.hasNext()) {
				Root root = (Root) iterator.next();
				if (paramClass.equals(root.getJavaType()))
					return (Root<T>) root.as(paramClass);
			}
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	private void join(From<?, ?> from, From<?, ?> to) {
		Iterator<?> iterator = from.getJoins().iterator();

		while (iterator.hasNext()) {
			Join join1 = (Join) iterator.next();
			Join join2 = to.join(join1.getAttribute().getName(),
					join1.getJoinType());
			join2.alias(getAlias(join1));
			join((From) join1, (From) join2);
		}
		Iterator<?> iterator2 = from.getFetches().iterator();
		while (iterator2.hasNext()) {
			Fetch fetch1 = (Fetch) iterator2.next();
			Fetch fetch2 = to.fetch(fetch1.getAttribute().getName());
			fetch(fetch1, fetch2);
		}
	}

	@SuppressWarnings("rawtypes")
	private void fetch(Fetch<?, ?> prev, Fetch<?, ?> next) {
		Iterator iterator = prev.getFetches().iterator();
		while (iterator.hasNext()) {
			Fetch fetch1 = (Fetch) iterator.next();
			Fetch fetch2 = next.fetch(prev.getAttribute().getName());
			fetch(fetch1, fetch2);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void queryFilter(CriteriaQuery<T> query, List<Filter> filterList) {
		 if ((query == null) || (filterList == null) || (filterList.isEmpty()))
		      return;
		    Root root = getRoot(query);
		    if (root == null)
		      return;
		    CriteriaBuilder criteriaBuilder =entityManager.getCriteriaBuilder();
		    Predicate predicate = (query.getRestriction() != null) ? query.getRestriction() : criteriaBuilder.conjunction();
		    Iterator iterator = filterList.iterator();
		    while (iterator.hasNext())
		    {
		      Filter filter = (Filter)iterator.next();
		      if (filter == null)
		        continue;
		      if (StringUtils.isEmpty(filter.getProperty()))
		        continue;
		      if ((filter.getOperator() == Filter.Operator.eq) && (filter.getValue() != null))
		      {
		        if ((filter.getIgnoreCase() != null) && (filter.getIgnoreCase().booleanValue()) && (filter.getValue() instanceof String))
		          predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(criteriaBuilder.lower(root.get(filter.getProperty())), ((String)filter.getValue()).toLowerCase()));
		        else
		          predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(filter.getProperty()), filter.getValue()));
		      }
		      else if ((filter.getOperator() == Filter.Operator.ne) && (filter.getValue() != null))
		      {
		        if ((filter.getIgnoreCase() != null) && (filter.getIgnoreCase().booleanValue()) && (filter.getValue() instanceof String))
		          predicate = criteriaBuilder.and(predicate, criteriaBuilder.notEqual(criteriaBuilder.lower(root.get(filter.getProperty())), ((String)filter.getValue()).toLowerCase()));
		        else
		          predicate = criteriaBuilder.and(predicate, criteriaBuilder.notEqual(root.get(filter.getProperty()), filter.getValue()));
		      }
		      else if ((filter.getOperator() == Filter.Operator.gt) && (filter.getValue() != null))
		      {
		        predicate = criteriaBuilder.and(predicate, criteriaBuilder.gt(root.get(filter.getProperty()), (Number)filter.getValue()));
		      }
		      else if ((filter.getOperator() == Filter.Operator.lt) && (filter.getValue() != null))
		      {
		        predicate = criteriaBuilder.and(predicate, criteriaBuilder.lt(root.get(filter.getProperty()), (Number)filter.getValue()));
		      }
		      else if ((filter.getOperator() == Filter.Operator.ge) && (filter.getValue() != null))
		      {
		        predicate = criteriaBuilder.and(predicate, criteriaBuilder.ge(root.get(filter.getProperty()), (Number)filter.getValue()));
		      }
		      else if ((filter.getOperator() == Filter.Operator.le) && (filter.getValue() != null))
		      {
		        predicate = criteriaBuilder.and(predicate, criteriaBuilder.le(root.get(filter.getProperty()), (Number)filter.getValue()));
		      }
		      else if ((filter.getOperator() == Filter.Operator.like) && (filter.getValue() != null) && (filter.getValue() instanceof String))
		      {
		        predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get(filter.getProperty()), (String)filter.getValue()));
		      }
		      else if ((filter.getOperator() == Filter.Operator.in) && (filter.getValue() != null))
		      {
		        predicate = criteriaBuilder.and(predicate, root.get(filter.getProperty()).in(new Object[] { filter.getValue() }));
		      }
		      else if (filter.getOperator() == Filter.Operator.isNull)
		      {
		        predicate = criteriaBuilder.and(predicate, root.get(filter.getProperty()).isNull());
		      }
		      else
		      {
		        if (filter.getOperator() != Filter.Operator.isNotNull)
		        {
		          continue;
		        }
		        predicate = criteriaBuilder.and(predicate, root.get(filter.getProperty()).isNotNull());
		      }
		    }
		    query.where(predicate);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void getPageFilter(CriteriaQuery<T> query, Pageable pageable) {
		 if ((query == null) || (pageable == null))
		      return;
		    Root root = getRoot(query);
		    if (root == null)
		      return;
		    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		    Predicate predicate = (query.getRestriction() != null) ? query.getRestriction() : criteriaBuilder.conjunction();
		    if ((StringUtils.isNotEmpty(pageable.getSearchProperty())) && (StringUtils.isNotEmpty(pageable.getSearchValue())))
		      predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get(pageable.getSearchProperty()), "%" + pageable.getSearchValue() + "%"));
		    if (pageable.getFilters() != null)
		    {
		      Iterator iterator = pageable.getFilters().iterator();
		      while (iterator.hasNext())
		      {
		        Filter filter = (Filter)iterator.next();
		        if (filter == null)
		          continue;
		        if (StringUtils.isEmpty(filter.getProperty()))
		          continue;
		        if ((filter.getOperator() == Filter.Operator.eq) && (filter.getValue() != null))
		        {
		          if ((filter.getIgnoreCase() != null) && (filter.getIgnoreCase().booleanValue()) && (filter.getValue() instanceof String))
		            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(criteriaBuilder.lower(root.get(filter.getProperty())), ((String)filter.getValue()).toLowerCase()));
		          else
		            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(filter.getProperty()), filter.getValue()));
		        }
		        else if ((filter.getOperator() == Filter.Operator.ne) && (filter.getValue() != null))
		        {
		          if ((filter.getIgnoreCase() != null) && (filter.getIgnoreCase().booleanValue()) && (filter.getValue() instanceof String))
		            predicate = criteriaBuilder.and(predicate, criteriaBuilder.notEqual(criteriaBuilder.lower(root.get(filter.getProperty())), ((String)filter.getValue()).toLowerCase()));
		          else
		            predicate = criteriaBuilder.and(predicate, criteriaBuilder.notEqual(root.get(filter.getProperty()), filter.getValue()));
		        }
		        else if ((filter.getOperator() == Filter.Operator.gt) && (filter.getValue() != null))
		        {
		          predicate = criteriaBuilder.and(predicate, criteriaBuilder.gt(root.get(filter.getProperty()), (Number)filter.getValue()));
		        }
		        else if ((filter.getOperator() == Filter.Operator.lt) && (filter.getValue() != null))
		        {
		          predicate = criteriaBuilder.and(predicate, criteriaBuilder.lt(root.get(filter.getProperty()), (Number)filter.getValue()));
		        }
		        else if ((filter.getOperator() == Filter.Operator.ge) && (filter.getValue() != null))
		        {
		          predicate = criteriaBuilder.and(predicate, criteriaBuilder.ge(root.get(filter.getProperty()), (Number)filter.getValue()));
		        }
		        else if ((filter.getOperator() == Filter.Operator.le) && (filter.getValue() != null))
		        {
		          predicate = criteriaBuilder.and(predicate, criteriaBuilder.le(root.get(filter.getProperty()), (Number)filter.getValue()));
		        }
		        else if ((filter.getOperator() == Filter.Operator.like) && (filter.getValue() != null) && (filter.getValue() instanceof String))
		        {
		          predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get(filter.getProperty()), (String)filter.getValue()));
		        }
		        else if ((filter.getOperator() == Filter.Operator.in) && (filter.getValue() != null))
		        {
		          predicate = criteriaBuilder.and(predicate, root.get(filter.getProperty()).in(new Object[] { filter.getValue() }));
		        }
		        else if (filter.getOperator() == Filter.Operator.isNull)
		        {
		          predicate = criteriaBuilder.and(predicate, root.get(filter.getProperty()).isNull());
		        }
		        else
		        {
		          if (filter.getOperator() != Filter.Operator.isNotNull)
		            continue;
		          predicate = criteriaBuilder.and(predicate, root.get(filter.getProperty()).isNotNull());
		        }
		      }
		    }
		    query.where(predicate);
	}

	@SuppressWarnings("rawtypes")
	private void queryOrder(CriteriaQuery<T> query, List<Order> orderList) {
		if ((query == null) || (orderList == null) || (orderList.isEmpty()))
			return;
		Root root = getRoot(query);
		if (root == null)
			return;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		List<javax.persistence.criteria.Order> result = new ArrayList<javax.persistence.criteria.Order>();
		if (!(query.getOrderList().isEmpty()))
			result.addAll(query.getOrderList());
		Iterator<Order> iterator = orderList.iterator();
		while (iterator.hasNext()) {
			Order order = (Order) iterator.next();
			if (order.getDirection() == Order.Direction.asc) {
				result.add(criteriaBuilder.asc(root.get(order.getProperty())));
			} else {
				if (order.getDirection() != Order.Direction.desc)
					continue;
				result.add(criteriaBuilder.desc(root.get(order.getProperty())));
			}
		}
		query.orderBy(result);
	}

	@SuppressWarnings("rawtypes")
	private void getPageOrder(CriteriaQuery<T> query, Pageable pageable) {
		if ((query == null) || (pageable == null))
			return;
		Root root = getRoot(query);
		if (root == null)
			return;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		List<javax.persistence.criteria.Order> result = new ArrayList<javax.persistence.criteria.Order>();
		if (!(query.getOrderList().isEmpty()))
			result.addAll(query.getOrderList());
		if ((StringUtils.isNotEmpty(pageable.getOrderProperty()))
				&& (pageable.getOrderDirection() != null))
			if (pageable.getOrderDirection() == Order.Direction.asc)
				result.add(criteriaBuilder.asc(root.get(pageable
						.getOrderProperty())));
			else if (pageable.getOrderDirection() == Order.Direction.desc)
				result.add(criteriaBuilder.desc(root.get(pageable
						.getOrderProperty())));
		if (pageable.getOrders() != null) {
			Iterator<Order> iterator = pageable.getOrders().iterator();
			while (iterator.hasNext()) {
				Order order = (Order) iterator.next();
				if (order.getDirection() == Order.Direction.asc) {
					result.add(criteriaBuilder.asc(root.get(order.getProperty())));
				} else {
					if (order.getDirection() != Order.Direction.desc)
						continue;
					result.add(criteriaBuilder.desc(root.get(order
							.getProperty())));
				}
			}
		}
		query.orderBy(result);
	}

}