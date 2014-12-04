package com.fdp.nonoo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tb_sn")
public class Sn extends BaseEntity {
	private static final long serialVersionUID = -2330598144835706164L;
	private Type type;
	private Long lastValue;

	@Column(nullable = false, updatable = false, unique = true)
	public Type getType() {
		return this.type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Column(nullable = false)
	public Long getLastValue() {
		return this.lastValue;
	}

	public void setLastValue(Long lastValue) {
		this.lastValue = lastValue;
	}

	public enum Type {
		product, order, payment, refunds, shipping, returns;
	}
}