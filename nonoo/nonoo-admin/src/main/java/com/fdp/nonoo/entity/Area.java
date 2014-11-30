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
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "tb_area")
public class Area extends OrderEntity {
	private static final long serialVersionUID = -2158109459123036967L;

	private String name;
	private String fullName;
	private String treePath;
	private Area parent;
	private List<Area> children = new ArrayList<Area>();
	
	@NotEmpty
	@Length(max = 100)
	@Column(nullable = false, length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(nullable = false, length = 500)
	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Column(nullable = false, updatable = false)
	public String getTreePath() {
		return this.treePath;
	}

	public void setTreePath(String treePath) {
		this.treePath = treePath;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Area getParent() {
		return this.parent;
	}

	public void setParent(Area parent) {
		this.parent = parent;
	}

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE })
	@OrderBy("order asc")
	public List<Area> getChildren() {
		return this.children;
	}

	public void setChildren(List<Area> children) {
		this.children = children;
	}

	

	@PrePersist
	public void prePersist() {
		Area parent = getParent();
		if (parent != null) {
			setFullName(parent.getFullName() + getName());
			setTreePath(parent.getTreePath() + parent.getId() + ",");
		} else {
			setFullName(getName());
			setTreePath(",");
		}
	}

	@PreUpdate
	public void preUpdate() {
		Area parent = getParent();
		if (parent != null)
			setFullName(parent.getFullName() + getName());
		else
			setFullName(getName());
	}

	@PreRemove
	public void preRemove() {

	}

	public String toString() {
		return getFullName();
	}
}