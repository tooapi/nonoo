package com.fdp.nonoo.dao.impl;

import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.fdp.nonoo.dao.FriendLinkDao;
import com.fdp.nonoo.entity.FriendLink;

@Repository("friendLinkDao")
public class FriendLinkDaoImpl extends BaseDaoImpl<FriendLink, Long> implements
		FriendLinkDao {
	public List<FriendLink> findList(FriendLink.Type type) {
		CriteriaBuilder criteriaBuilder =entityManager.getCriteriaBuilder();
		CriteriaQuery<FriendLink> criteriaQuery = criteriaBuilder.createQuery(FriendLink.class);
		Root<FriendLink> root = criteriaQuery.from(FriendLink.class);
		criteriaQuery.select(root);
		if (type != null){
			criteriaQuery.where(criteriaBuilder.equal(root.get("type"), type));
		}
		criteriaQuery.orderBy(new Order[] { criteriaBuilder.asc(root.get("order")) });
		return this.entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT).getResultList();
	}
}