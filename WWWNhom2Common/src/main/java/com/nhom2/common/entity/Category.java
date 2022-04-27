package com.nhom2.common.entity;

import java.beans.Transient;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;


@Entity
@Table(name = "categories")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(length = 128, nullable = false, unique = true)
	private String name;

	@Column(length = 64, nullable = false, unique = true)
	private String alias;

	@Column(length = 128, nullable = false)
	private String image;

	private boolean enabled;

	@Column(name = "all_parent_ids", length = 256,nullable = true)
	private String allParentIDs;
	
	@OneToOne
	@JoinColumn(name = "parent_id")
	private Category parent;

	@OneToMany(mappedBy = "parent")
	@OrderBy("name asc")
	private Set<Category> children = new HashSet<>();

	public Category() {
	}

	public Category(Integer id) {
		super();
		this.id = id;
	}

	public static Category copyIdAndName(Category category) {
		Category copy = new Category();
		copy.setId(category.getId());
		copy.setName(category.getName());

		return copy;
	}

	public static Category copyIdAndName(Integer id, String name) {
		Category copy = new Category();
		copy.setId(id);
		copy.setName(name);

		return copy;
	}

	public static Category copyFull(Category category) {
		Category copy = new Category();
		copy.setId(category.getId());
		copy.setName(category.getName());
		copy.setImage(category.getImage());
		copy.setAlias(category.getAlias());
		copy.setEnabled(category.isEnabled());
		copy.setHasChildren(category.getChildren().size() > 0);
		return copy;
	}

	public static Category copyFull(Category category, String name) {
		Category copy = Category.copyFull(category);
		copy.setName(name);

		return copy;
	}

	public String getAllParentIDs() {
		return allParentIDs;
	}

	public void setAllParentIDs(String allParentIDs) {
		this.allParentIDs = allParentIDs;
	}

	public Category(String name) {
		this.name = name;
		this.alias = name;
		this.image = "default.png";
	}

	public Category(String name, Category parent) {
		super();
		this.name = name;
		this.parent = parent;
	}

	public Category(Integer id, String name, String alias) {
		super();
		this.id = id;
		this.name = name;
		this.alias = alias;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public Set<Category> getChildren() {
		return children;
	}

	public void setChildren(Set<Category> children) {
		this.children = children;
	}

	@Transient
	public String getPhotosImagesPath() {
		if (this.id == null)
			return "/images/068 image-thumbnail.png";

		return "/categories-images/" + this.id + "/" + this.image;
	}

	public boolean isHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

	@javax.persistence.Transient
	private boolean hasChildren;

	@Override
	public String toString() {
		return "Category [name=" + name + "]";
	}
	
}
