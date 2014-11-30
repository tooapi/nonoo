package com.fdp.nonoo.common;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Order implements Serializable {
	private static final long serialVersionUID = -3078342809727773232L;
	private static final Direction defaultDirection = Direction.desc;
	private String property;
	private Direction direction = defaultDirection;

	public Order() {
	}

	public Order(String property, Direction direction) {
		this.property = property;
		this.direction = direction;
	}

	public static Order asc(String property) {
		return new Order(property, Direction.asc);
	}

	public static Order desc(String property) {
		return new Order(property, Direction.desc);
	}

	public String getProperty() {
		return this.property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public Direction getDirection() {
		return this.direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (super.getClass() != obj.getClass())
			return false;
		if (this == obj)
			return true;
		Order order = (Order) obj;
		return new EqualsBuilder().append(getProperty(), order.getProperty())
				.append(getDirection(), order.getDirection()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getProperty())
				.append(getDirection()).toHashCode();
	}

	public enum Direction {
		asc, desc;

		public static Direction fromString(String value) {
			return valueOf(value.toLowerCase());
		}
	}
}