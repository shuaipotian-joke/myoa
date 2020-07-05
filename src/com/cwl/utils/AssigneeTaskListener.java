package com.cwl.utils;

import javax.servlet.http.HttpSession;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.cwl.pojo.ActiveUser;
import com.cwl.pojo.Employee;
import com.cwl.service.EmployeeService;

public class AssigneeTaskListener implements TaskListener{

	@Override
	public void notify(DelegateTask deligateTask) {
		//调用业务层查询当前登陆人的上级主管
		//1.硬编码获取spring容器
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		EmployeeService employeeService = (EmployeeService)context.getBean("employeeService");
		//2.获取session
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpSession session = requestAttributes.getRequest().getSession();
//		Employee employee = (Employee) session.getAttribute(Constants.GLOBAL_SESSION_ID);
		ActiveUser activeUser = (ActiveUser)SecurityUtils.getSubject().getPrincipal();
		Employee manager = employeeService.findEmployeeByManagerId(activeUser.getManagerId());
		deligateTask.setAssignee(manager.getName());
	}

}
