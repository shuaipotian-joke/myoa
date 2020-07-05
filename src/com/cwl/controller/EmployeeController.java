package com.cwl.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cwl.exception.CustomException;
import com.cwl.pojo.Employee;
import com.cwl.pojo.SysRole;
import com.cwl.pojo.TreeMenu;
import com.cwl.pojo.TreePermission;
import com.cwl.service.EmployeeService;
import com.cwl.utils.Constants;

@Controller
public class EmployeeController {
	@Autowired
	EmployeeService employeeService;
	@RequestMapping("/login")
	public String login(HttpServletRequest request) throws Exception {
		String exceptionName = (String)request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
		System.out.println("exceptionName-----------------------------------"+exceptionName);
		System.out.println("request.getAttribute(\"errMsg\")-----------------------------------"+request.getAttribute("errMsg"));
		if(exceptionName!=null) {
			if(UnknownAccountException.class.getName().equals(exceptionName)) {
				throw new CustomException("用户账号不存在");
			}else if(IncorrectCredentialsException.class.getName().equals(exceptionName)){
				throw new CustomException("用户密码错误");
			}else {
				throw new CustomException("未知错误");
			}
		}
		return "login";
	}
	@ResponseBody
	@RequestMapping("/updateRole")
	public Map<String,Object> updateRole(String rolename,String roleId) {
		employeeService.updateRoleIdByUserName(rolename,roleId);
		Map<String,Object> map = new HashMap<>();
		map.put("msg", "修改成功");
		return map;
	}
	
	@RequestMapping("/toAddRole")
	public String toAddRole(Model model) {
		List<TreePermission> PermissionList = employeeService.findAllPermission();
		model.addAttribute("PermissionList", PermissionList);
		return "addRole";
	}
	
	@ResponseBody
	@RequestMapping("/getForm")
	public String getForm(@RequestParam ("checkName") String checkName) {
		return checkName;
	}
	
	@RequestMapping("/findRoles")
	public String findRoles(Model model) {
		List<SysRole> roleList = employeeService.findAllRoles();
		List<TreeMenu> menu = employeeService.findMenu();
		model.addAttribute("menu", menu);
		model.addAttribute("roleList", roleList) ;
		return "seeRoles";
	}
	
	@RequestMapping("/delRole")
	public String delRole(String id) {
		employeeService.deleteRole(id);
		return "redirect:/findRoles";
	}
	
	
//	@RequestMapping("/logout")
//	public String logout(HttpSession session) {
//		session.invalidate();
//		return "login";
//	}
}	
