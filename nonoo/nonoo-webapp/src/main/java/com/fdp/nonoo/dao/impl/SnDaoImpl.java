package com.fdp.nonoo.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.fdp.nonoo.dao.SnDao;
import com.fdp.nonoo.entity.Sn;

@Repository("snDao")
public class SnDaoImpl implements SnDao, InitializingBean {
	private HiloOptimizer productOptimizer;
	private HiloOptimizer orderOptimizer;
	private HiloOptimizer paymentOptimizer;
	private HiloOptimizer refundsOptimizer;
	private HiloOptimizer shippingOptimizer;
	private HiloOptimizer returnsOptimizer;

	@PersistenceContext
	private EntityManager entityManager;

	@Value("${sn.product.prefix}")
	private String productPrefix;

	@Value("${sn.product.maxLo}")
	private int productMaxLo;

	@Value("${sn.order.prefix}")
	private String orderPrefix;

	@Value("${sn.order.maxLo}")
	private int orderMaxLo;

	@Value("${sn.payment.prefix}")
	private String paymentPrefix;

	@Value("${sn.payment.maxLo}")
	private int paymentMaxLo;

	@Value("${sn.refunds.prefix}")
	private String refundsPrefix;

	@Value("${sn.refunds.maxLo}")
	private int refundsMaxLo;

	@Value("${sn.shipping.prefix}")
	private String shippingPrefix;

	@Value("${sn.shipping.maxLo}")
	private int shippingMaxLo;

	@Value("${sn.returns.prefix}")
	private String returnsPrefix;

	@Value("${sn.returns.maxLo}")
	private int returnsMaxLo;

	public void afterPropertiesSet() {
		productOptimizer = new HiloOptimizer(this, Sn.Type.product,
				productPrefix, productMaxLo);
		orderOptimizer = new HiloOptimizer(this, Sn.Type.order, orderPrefix,
				orderMaxLo);
		paymentOptimizer = new HiloOptimizer(this, Sn.Type.payment,
				paymentPrefix, paymentMaxLo);
		refundsOptimizer = new HiloOptimizer(this, Sn.Type.refunds,
				refundsPrefix, refundsMaxLo);
		shippingOptimizer = new HiloOptimizer(this, Sn.Type.shipping,
				shippingPrefix, shippingMaxLo);
		returnsOptimizer = new HiloOptimizer(this, Sn.Type.returns,
				returnsPrefix, returnsMaxLo);
	}

	public String generate(Sn.Type type) {
		Assert.notNull(type);
		if (type == Sn.Type.product)
			return productOptimizer.generate();
		if (type == Sn.Type.order)
			return orderOptimizer.generate();
		if (type == Sn.Type.payment)
			return paymentOptimizer.generate();
		if (type == Sn.Type.refunds)
			return refundsOptimizer.generate();
		if (type == Sn.Type.shipping)
			return shippingOptimizer.generate();
		if (type == Sn.Type.returns)
			return returnsOptimizer.generate();
		return null;
	}

	private long getSn(Sn.Type paramType) {
		String sql = "select sn from Sn sn where sn.type = :type";
		Sn sn = (Sn) entityManager.createQuery(sql, Sn.class)
				.setFlushMode(FlushModeType.COMMIT)
				.setParameter("type", paramType)
				.setLockMode(LockModeType.PESSIMISTIC_WRITE).getSingleResult();
		long l = sn.getLastValue();
		sn.setLastValue(l + 1L);
		entityManager.merge(sn);
		return l;
	}

	class HiloOptimizer {
		private Sn.Type type;
		private String prefix;
		private int maxLo;
		private int nextNo;
		private long begin;
		private long sn;
		private SnDaoImpl snDao;

		public HiloOptimizer(SnDaoImpl snDao, Sn.Type type, String prefix,
				int maxLo) {
			this.type = type;
			this.prefix = ((prefix != null) ? prefix.replace("{", "${") : "");
			this.maxLo = maxLo;
			this.nextNo = (maxLo + 1);
			this.snDao = snDao;
		}

		public synchronized String generate() {
			if (nextNo > maxLo) {
				sn = snDao.getSn(type);
				nextNo = ((sn == 0L) ? 1 : 0);
				begin = (sn * (maxLo + 1));
			}
			return prefix + (begin + nextNo++);
		}
	}
}