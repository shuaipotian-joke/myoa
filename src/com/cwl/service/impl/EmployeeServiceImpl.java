package com.cwl.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwl.mapper.EmployeeCustomMapper;
import com.cwl.mapper.EmployeeMapper;
import com.cwl.mapper.SysPermissionCustomMapper;
import com.cwl.mapper.SysRoleMapper;
import com.cwl.pojo.Employee;
import com.cwl.pojo.EmployeeExample;
import com.cwl.pojo.SysRole;
import com.cwl.pojo.SysRoleExample;
import com.cwl.pojo.TreeMenu;
import com.cwl.pojo.TreePermission;
import com.cwl.service.EmployeeService;
@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService{
	@Autowired
	EmployeeMapper employeeMapper;
	@Autowired
	SysPermissionCustomMapper sysPermissionCustomMapper;
	@Autowired
	EmployeeCustomMapper employeeCustomMapper;
	@Autowired
	SysRoleMapper sysRoleMapper;
	@Override
	public Employee findEmployeeByUsername(String username) {
		EmployeeExample example = new EmployeeExample();
		EmployeeExample.Criteria criteria = example.createCriteria();
		criteria.andNameEqualTo(username);
		List<Employee> list = employeeMapper.selectByExample(example);
		if(list!=null&&list.size()>0) {
			return list.get(0);
		}
		return null;
	}
	@Override
	public Employee findEmployeeByManagerId(Long manager_id) {
		return employeeMapper.selectByPrimaryKey(manager_id);
	}
	@Override
	public List<TreeMenu> findMenu() {
		return sysPermissionCustomMapper.findMenuList();
	}
	@Override
	public Employee findEmpByUserCode(String userCode) {
		EmployeeExample example = new EmployeeExample();
		EmployeeExample.Criteria criteria = example.createCriteria();
		criteria.andNameEqualTo(userCode);
		List<Employee> list = employeeMapper.selectByExample(example);
		if(list!=null&&list.size()>0) {
			return list.get(0);
		}
		return null;
	}
	@Override
	public List<SysRole> findRoleAndPermission(String name) {
		return employeeCustomMapper.findPermissionAndRoleListByUserId(name);
	}
	@Override
	public void updateRoleIdByUserName(String rolename, String roleId) {
		EmployeeExample example = new EmployeeExample();
		EmployeeExample.Criteria criteria = example.createCriteria();
		criteria.andNameEqualTo(rolename);	
		List<Employee> selectByExample = employeeMapper.selectByExample(example);
		Employee employee = selectByExample.get(0);
		employee.setRole(roleId);
		employeeMapper.updateByPrimaryKeySelective(employee);
		
	}
	@Override
	public List<TreePermission> findAllPermission() {
		// TODO Auto-generated method stub
		return employeeCustomMapper.findPermissionMenu();
	}
	@Override
	public List<SysRole> findAllRoles() {
		SysRoleExample example = new SysRoleExample();
		return sysRoleMapper.selectByExample(example);
	}
	@Override
	public void deleteRole(String id) {
		sysRoleMapper.deleteByPrimaryKey(id);
	}
	
}
