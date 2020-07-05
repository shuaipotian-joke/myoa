package com.cwl.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwl.mapper.BaoxiaobillMapper;
import com.cwl.mapper.EmployeeCustomMapper;
import com.cwl.mapper.SysPermissionCustomMapper;
import com.cwl.mapper.SysRoleMapper;
import com.cwl.pojo.ActiveUser;
import com.cwl.pojo.Baoxiaobill;
import com.cwl.pojo.Employee;
import com.cwl.pojo.EmployeeCustom;
import com.cwl.pojo.SysPermission;
import com.cwl.pojo.SysRole;
import com.cwl.pojo.SysRoleExample;
import com.cwl.service.BaoxiaoService;
import com.cwl.utils.Constants;
@Service
public class BaoxiaoServiceImpl implements BaoxiaoService{
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private BaoxiaobillMapper baoxiaobillMapper;
	@Autowired
	private TaskService taskService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private SysPermissionCustomMapper sysPermissionCustomMapper;
	@Autowired
	private EmployeeCustomMapper employeeCustomMapper;
	@Autowired
	private SysRoleMapper sysRoleMapper;
	@Override
	public List<Baoxiaobill> findAllBill() {
		return baoxiaobillMapper.selectByExample(null);
	}
	
	@Override
	public void saveBaoxiaoAndStartProcess(Baoxiaobill bill, ActiveUser activeUser) {
		bill.setUserId(activeUser.getId().intValue());
		bill.setCreatdate(new Date());
		bill.setState(1);
		this.baoxiaobillMapper.insert(bill);
		Map<String,Object> map = new HashMap<>();
		map.put("userId",activeUser.getName());
		String processKey = Constants.BAOXIAO_KEY;
		String bussiness_key = processKey+"."+bill.getId();
		this.runtimeService.startProcessInstanceByKey(processKey,bussiness_key,map);
	}

	@Override
	public void deleteBaoxiao(int id) {
		baoxiaobillMapper.deleteByPrimaryKey(id);
	}

	@Override
	public Task findTaskIdByBussinessKey(String key) {
		return this.taskService.createTaskQuery().processInstanceBusinessKey(key).singleResult();
	}

	@Override
	public List<Comment> findCommentByBillId(int id) {
		String bussinessKey = Constants.BAOXIAO_KEY+"."+id;
		HistoricProcessInstance pi = this.historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(bussinessKey).singleResult();
		List<Comment> list = this.taskService.getProcessInstanceComments(pi.getId());
		return list;
	}

	@Override
	public Baoxiaobill findBillById(int id) {
		return this.baoxiaobillMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<SysPermission> findPermissionListByUserId(String username) {
		return sysPermissionCustomMapper.findPermissionByUsername(username);
	}

	@Override
	public List<EmployeeCustom> findAllUserList() {
		
		return employeeCustomMapper.findUserAndRoleList();
	}

	@Override
	public List<SysRole> findAllRoles() {
		SysRoleExample example = new SysRoleExample();
		List<SysRole> list = sysRoleMapper.selectByExample(example);
		return list;
	}

	@Override
	public String findManagerByName(String name) {
		return employeeCustomMapper.findUserRoleByName(name);
	}

}
