package com.fdp.nonoo.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "tb_navigation")
public class Navigation extends OrderEntity {
	private static final long serialVersionUID = -7635757647887646795L;
	private String name;
	private Position position;
	private String url;
	private Boolean isBlankTarget;
	
	private Navigation parent;
	private List<Navigation> children = new ArrayList<Navigation>();

	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotNull
	@Column(nullable = false)
	public Position getPosition() {
		return this.position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@NotNull
	@Column(nullable = false)
	public Boolean getIsBlankTarget() {
		return this.isBlankTarget;
	}

	public void setIsBlankTarget(Boolean isBlankTarget) {
		this.isBlankTarget = isBlankTarget;
	}
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	public Navigation getParent() {
		return this.parent;
	}

	public void setParent(Navigation parent) {
		this.parent = parent;
	}

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE })
	@OrderBy("order asc")
	public List<Navigation> getChildren() {
		return this.children;
	}

	public void setChildren(List<Navigation> children) {
		this.children = children;
	}
	

	public static enum Position {
		top, middle, bottom;
	}
}