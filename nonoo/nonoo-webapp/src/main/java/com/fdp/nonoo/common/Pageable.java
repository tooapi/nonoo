package com.fdp.nonoo.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Pageable implements Serializable {
	private static final long serialVersionUID = -3930180379790344299L;
	private static final int DEFAULT_PAGE_NUMBER = 1;
	private static final int DEFAULT_PAGE_SIZE = 20;
	private static final int MAX_PAGE_SIZE = 1000;
	private int pageNumber = DEFAULT_PAGE_NUMBER;
	private int pageSize = DEFAULT_PAGE_SIZE;
	private String searchProperty;
	private String searchValue;
	private String orderProperty;
	private Order.Direction orderDirection;
	private List<Filter> filters = new ArrayList<Filter>();
	private List<Order> orders = new ArrayList<Order>();

	public Pageable() {
	}

	public Pageable(Integer pageNumber, Integer pageSize) {
		if ((pageNumber != null)
				&& (pageNumber>= DEFAULT_PAGE_NUMBER))
			this.pageNumber = pageNumber;
		if ((pageSize == null) || (pageSize< DEFAULT_PAGE_NUMBER)
				|| (pageSize> MAX_PAGE_SIZE))
			return;
		this.pageSize = pageSize;
	}

	public int getPageNumber() {
		return this.pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		if (pageNumber < DEFAULT_PAGE_NUMBER)
			pageNumber = DEFAULT_PAGE_NUMBER;
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return this.pageSize;
	}

	public void setPageSize(int pageSize) {
		if ((pageSize < DEFAULT_PAGE_NUMBER) || (pageSize > MAX_PAGE_SIZE))
			pageSize = DEFAULT_PAGE_SIZE;
		this.pageSize = pageSize;
	}

	public String getSearchProperty() {
		return this.searchProperty;
	}

	public void setSearchProperty(String searchProperty) {
		this.searchProperty = searchProperty;
	}

	public String getSearchValue() {
		return this.searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}

	public String getOrderProperty() {
		return this.orderProperty;
	}

	public void setOrderProperty(String orderProperty) {
		this.orderProperty = orderProperty;
	}

	public Order.Direction getOrderDirection() {
		return this.orderDirection;
	}

	public void setOrderDirection(Order.Direction orderDirection) {
		this.orderDirection = orderDirection;
	}

	public List<Filter> getFilters() {
		return this.filters;
	}

	public void setFilters(List<Filter> filters) {
		this.filters = filters;
	}

	public List<Order> getOrders() {
		return this.orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (super.getClass() != obj.getClass())
			return false;
		if (this == obj)
			return true;
		Pageable pageable = (Pageable) obj;
		return new EqualsBuilder()
				.append(getPageNumber(), pageable.getPageNumber())
				.append(getPageSize(), pageable.getPageSize())
				.append(getSearchProperty(), pageable.getSearchProperty())
				.append(getSearchValue(), pageable.getSearchValue())
				.append(getOrderProperty(), pageable.getOrderProperty())
				.append(getOrderDirection(), pageable.getOrderDirection())
				.append(getFilters(), pageable.getFilters())
				.append(getOrders(), pageable.getOrders()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getPageNumber())
				.append(getPageSize()).append(getSearchProperty())
				.append(getSearchValue()).append(getOrderProperty())
				.append(getOrderDirection()).append(getFilters())
				.append(getOrders()).toHashCode();
	}
}