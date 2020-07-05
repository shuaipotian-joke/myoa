package com.cwl.pojo;

import java.util.List;


public class ActiveUser {
	private Long id;

    private String name;

    private String password;
    
    private Long ManagerId;
    private List<TreeMenu> menus;// 菜单
	private List<SysPermission> permissions;// 权限
	
	public Long getManagerId() {
		return ManagerId;
	}
	public void setManagerId(Long managerId) {
		ManagerId = managerId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<TreeMenu> getMenus() {
		return menus;
	}
	public void setMenus(List<TreeMenu> menus) {
		this.menus = menus;
	}
	public List<SysPermission> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<SysPermission> permissions) {
		this.permissions = permissions;
	}
	
}
