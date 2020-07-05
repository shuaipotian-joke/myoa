package com.cwl.junit;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cwl.mapper.BaoxiaobillMapper;
import com.cwl.mapper.EmployeeCustomMapper;
import com.cwl.mapper.EmployeeMapper;
import com.cwl.mapper.SysPermissionCustomMapper;
import com.cwl.pojo.Baoxiaobill;
import com.cwl.pojo.Employee;
import com.cwl.pojo.EmployeeCustom;
import com.cwl.pojo.EmployeeExample;
import com.cwl.pojo.SysPermission;
import com.cwl.pojo.SysRole;
import com.cwl.pojo.TreeMenu;
import com.cwl.pojo.TreePermission;
import com.cwl.service.BaoxiaoService;
import com.cwl.service.EmployeeService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml","classpath:spring/springmvc.xml"})
public class TestTreeMenu {
	@Autowired
	private SysPermissionCustomMapper sysPermissionCustomMapper;
	@Autowired
	private BaoxiaobillMapper baoxiaobillMapper;
	@Autowired
	EmployeeMapper employeeMapper;
	@Autowired
	BaoxiaoService baoxiaoService;
	@Autowired
	EmployeeService employeeService;
	@Autowired 
	EmployeeCustomMapper employeeCustomMapper;
	@Test
	public void testMenu() {
		List<TreeMenu> findMenuList = sysPermissionCustomMapper.findMenuList();
		for (TreeMenu treeMenu : findMenuList) {
			System.out.println(treeMenu.getId()+"."+treeMenu.getName());
			
			List<SysPermission> children = treeMenu.getChildren();
			for (SysPermission sysPermission : children) {
				System.out.println("\t"+sysPermission.getName()+","+sysPermission.getUrl()+","+sysPermission.getPercode());
			}
		}
	}
	@Test
	public void testListbill() {
		List<Baoxiaobill> list = baoxiaobillMapper.selectByExample(null);
		for (Baoxiaobill baoxiaobill : list) {
			System.out.println(baoxiaobill.getId());
		}
	}
	
	@Test
	public void testUserCode() {
		EmployeeExample example = new EmployeeExample();
		EmployeeExample.Criteria criteria = example.createCriteria();
		criteria.andNameEqualTo("danny");
		List<Employee> list = employeeMapper.selectByExample(example);
		System.out.println(list.get(0).getPassword());
	}
	
	@Test
	public void testPermission() {
		List<SysPermission> list = sysPermissionCustomMapper.findPermissionByUsername("danny");
		for (SysPermission sysPermission : list) {
			System.out.println(sysPermission);
		}
	}
	
	@Test
	public void testListRole() {
		List<EmployeeCustom> findUserAndRoleList = employeeCustomMapper.findUserAndRoleList(); 
		for (EmployeeCustom employeeCustom : findUserAndRoleList) {
			System.out.println(employeeCustom.getRolename());
		}
	}
	
	@Test
	public void testRole() {
		List<SysRole> findPermissionAndRoleListByUserId = employeeCustomMapper.findPermissionAndRoleListByUserId("danny");
		for (SysRole sysRole : findPermissionAndRoleListByUserId) {
			List<SysPermission> permission = sysRole.getPermission();
			for (SysPermission permissions : permission) {
				System.out.println(permissions.getName());
				System.out.println(permissions.getType());
			}
		}
	}
	@Test
	public void testUpdateRole() {
		employeeService.updateRoleIdByUserName("danny","3");
	}
	@Test
	public void testfindPermissionMenu() {
		List<TreePermission> list = employeeCustomMapper.findPermissionMenu();
		for (TreePermission treePermission : list) {
			System.out.println(treePermission.getName());
			List<SysPermission> childrenName = treePermission.getChildrenName();
			for (SysPermission  listChild: childrenName) {
				System.out.println("\t"+listChild.getName());
			}
		}
	}
	@Test
	public void testfindAllRoles() {
		List<SysRole> roleList = employeeService.findAllRoles();
		for (SysRole sysRole : roleList) {
			System.out.println(sysRole.getName());
		}
	}
}
