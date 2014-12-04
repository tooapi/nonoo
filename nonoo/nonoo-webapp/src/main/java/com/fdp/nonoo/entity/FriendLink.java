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
@Table(name = "tb_friend_link")
public class FriendLink extends OrderEntity {
	private static final long serialVersionUID = 3019642557500517628L;
	private String name;
	private Type type;
	private String logo;
	private String url;

	private FriendLink parent;
	private List<FriendLink> children = new ArrayList<FriendLink>();

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
	public Type getType() {
		return this.type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Length(max = 200)
	public String getLogo() {
		return this.logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	public FriendLink getParent() {
		return this.parent;
	}

	public void setParent(FriendLink parent) {
		this.parent = parent;
	}

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE })
	@OrderBy("order asc")
	public List<FriendLink> getChildren() {
		return this.children;
	}

	public void setChildren(List<FriendLink> children) {
		this.children = children;
	}

	public static enum Type {
		text, image;
	}
}