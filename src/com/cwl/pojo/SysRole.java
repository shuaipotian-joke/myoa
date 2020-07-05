package com.cwl.pojo;

import java.util.List;

public class SysRole {
    private String id;

    private String name;

    private String available;

    private List<SysPermission> permission;
    
    
    public List<SysPermission> getPermission() {
		return permission;
	}

	public void setPermission(List<SysPermission> permission) {
		this.permission = permission;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available == null ? null : available.trim();
    }
}