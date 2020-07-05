package com.cwl.mapper;

import java.util.List;

import com.cwl.pojo.SysPermission;
import com.cwl.pojo.TreeMenu;

public interface SysPermissionCustomMapper {
	public List<TreeMenu> findMenuList();
	
	public List<SysPermission> getSubMenu();
	
	public List<SysPermission> findPermissionByUsername(String name);
}