package com.cwl.service;

import java.util.List;

import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;

import com.cwl.pojo.ActiveUser;
import com.cwl.pojo.Baoxiaobill;
import com.cwl.pojo.Employee;
import com.cwl.pojo.EmployeeCustom;
import com.cwl.pojo.SysPermission;
import com.cwl.pojo.SysRole;

public interface BaoxiaoService {
	
	public void saveBaoxiaoAndStartProcess(Baoxiaobill bill,  ActiveUser activeUser);
	
	public List<Baoxiaobill> findAllBill();
	
	public void deleteBaoxiao(int id);

	public Task findTaskIdByBussinessKey(String key);

	public List<Comment> findCommentByBillId(int id);

	public Baoxiaobill findBillById(int id);
	
	public List<SysPermission> findPermissionListByUserId(String username);

	public List<EmployeeCustom> findAllUserList();

	public List<SysRole> findAllRoles();

	public String findManagerByName(String name);
}
