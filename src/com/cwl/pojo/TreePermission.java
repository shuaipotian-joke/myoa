package com.cwl.pojo;

import java.util.List;

public class TreePermission {
	private int id;
	private String name;
	private List<SysPermission> childrenName;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<SysPermission> getChildrenName() {
		return childrenName;
	}
	public void setChildrenName(List<SysPermission> childrenName) {
		this.childrenName = childrenName;
	}
	
}
