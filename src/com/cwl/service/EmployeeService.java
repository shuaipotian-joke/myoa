package com.cwl.service;

import java.util.List;

import com.cwl.pojo.Employee;
import com.cwl.pojo.SysRole;
import com.cwl.pojo.TreeMenu;
import com.cwl.pojo.TreePermission;

public interface EmployeeService {
	//根据用户名查找用户
	public Employee findEmployeeByUsername(String username);

	//查询当前登录人的上级主管
	public Employee findEmployeeByManagerId(Long manager_id);
	
	//查询菜单
	public List<TreeMenu> findMenu();
	
	public Employee findEmpByUserCode(String userCode);
	
	public List<SysRole> findRoleAndPermission(String name);

	public void updateRoleIdByUserName(String rolename, String roleId);
	
	public List<TreePermission> findAllPermission();

	public List<SysRole> findAllRoles();

	public void deleteRole(String id);
}
