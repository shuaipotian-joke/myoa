package com.cwl.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.cwl.pojo.ActiveUser;
import com.cwl.pojo.Employee;
import com.cwl.pojo.SysPermission;
import com.cwl.pojo.TreeMenu;
import com.cwl.service.BaoxiaoService;
import com.cwl.service.EmployeeService;


public class CustomRealm extends AuthorizingRealm {
	
	@Autowired
	EmployeeService employeeService;
	@Autowired
	BaoxiaoService baoxiaoService;
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String userCode = (String)token.getPrincipal();
		Employee emp = employeeService.findEmpByUserCode(userCode);
		
		if(emp==null){
			return null;
		}
		List<TreeMenu> menus = employeeService.findMenu();
		String password_db = emp.getPassword();
		String salt_db = emp.getSalt();
		ActiveUser activeUser = new ActiveUser();
		activeUser.setId(emp.getId());
		activeUser.setName(userCode);
		activeUser.setPassword(password_db);
		activeUser.setMenus(menus);
		activeUser.setManagerId(emp.getManagerId());
//		activeUser.setPermissions(permissions);
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(activeUser,password_db,ByteSource.Util.bytes(salt_db),"CustomRealm");
		return info;
		
	}
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
		ActiveUser activeUser = (ActiveUser)principal.getPrimaryPrincipal();
		List<SysPermission> permissions = baoxiaoService.findPermissionListByUserId(activeUser.getName());
		List<String> permissionlist = new ArrayList<>();
		for (SysPermission sysPermission : permissions) {
			permissionlist.add(sysPermission.getPercode());
		}
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.addStringPermissions(permissionlist);
		return info;
	}

	

}
