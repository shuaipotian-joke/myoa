package com.cwl.mapper;

import java.util.List;

import com.cwl.pojo.EmployeeCustom;
import com.cwl.pojo.SysRole;
import com.cwl.pojo.TreePermission;

public interface EmployeeCustomMapper {
	public List<EmployeeCustom> findUserAndRoleList();
	
	public String findUserRoleByName(String name);
	
	public List<SysRole> findPermissionAndRoleListByUserId(String name);
	
	public List<TreePermission> findPermissionMenu();
}