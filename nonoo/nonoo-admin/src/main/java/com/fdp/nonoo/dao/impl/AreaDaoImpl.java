package com.fdp.nonoo.dao.impl;

import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.fdp.nonoo.dao.AreaDao;
import com.fdp.nonoo.entity.Area;

@Repository("areaDaoImpl")
public class AreaDaoImpl extends BaseDaoImpl<Area, Long> implements AreaDao {
	public List<Area> findRoots(Integer count) {
		String sql = "select area from Area area where area.parent is null order by area.order asc";
		TypedQuery<Area> query = entityManager.createQuery(sql, Area.class).setFlushMode(FlushModeType.COMMIT);
		if (count != null)
			query.setMaxResults(count);
		return query.getResultList();
	}
}