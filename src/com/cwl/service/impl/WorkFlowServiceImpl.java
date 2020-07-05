package com.cwl.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwl.mapper.BaoxiaobillMapper;
import com.cwl.mapper.LeavebillMapper;
import com.cwl.pojo.Baoxiaobill;
import com.cwl.pojo.Employee;
import com.cwl.pojo.Leavebill;
import com.cwl.service.WorkFlowService;
import com.cwl.utils.Constants;
@Service
public class WorkFlowServiceImpl implements WorkFlowService{
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private LeavebillMapper leavebillMapper;
	@Autowired
	private BaoxiaobillMapper baoxiaobillMapper;
	@Override
	public void deployProcess(String processName, InputStream input) {
		ZipInputStream inputStream = new ZipInputStream(input);
		Deployment deployment = this.repositoryService
									.createDeployment()
									.name(processName)
									.addZipInputStream(inputStream)
									.deploy();
	}
	@Override
	public List<Deployment> findAllDeployeements() {
		return this.repositoryService.createDeploymentQuery().list();
	}
	@Override
	public List<ProcessDefinition> findAllProcessDefinitions() {
		return this.repositoryService.createProcessDefinitionQuery().list();
	}
	@Override
	public void saveLeaveAndStartProcess(Leavebill bill, Employee employee) {
		//1.保存申请单
		bill.setLeavedate(new Date());
		bill.setState(1);
		bill.setUserId(employee.getId());
		leavebillMapper.insert(bill);//新添加的数据Id值要回填到唯一标识当中
		//2.启动流程实例
		String key = Constants.LEAVEBILL_KEY;
		Map<String,Object> map = new HashMap<>();
		//3.设置代办人表达式
		map.put("userId",employee.getName());
		//怎样设置流程数据和业务数据的关联性？设置bussiness_key
		String bussiness_key = Constants.LEAVEBILL_KEY+"."+bill.getId();
		this.runtimeService.startProcessInstanceByKey(key,bussiness_key,map);
	}
	@Override
	public List<Task> findMyTaskListByUserId(String name) {
		return this.taskService.createTaskQuery().taskAssignee(name).list();
	}
	@Override
	public Map<String,Object> findBillByTaskId(String taskId) {
		Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();
		ProcessInstance processInstance = this.runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
		String business_key = processInstance.getBusinessKey();
		System.out.println(business_key);
		String billId = business_key.split("\\.")[1];
		String billName = business_key.split("\\.")[0];
		Map<String,Object> map = new HashMap<>();
		if(billName.equals("leaveProcess")) {
			Leavebill leavebill = leavebillMapper.selectByPrimaryKey(Long.parseLong(billId));
			map.put("leaveProcess", leavebill);
			return map;
		}else {
			Baoxiaobill baoxiaobill = baoxiaobillMapper.selectByPrimaryKey(Integer.parseInt(billId));
			map.put("baoxiaoProcess", baoxiaobill);
			return map;
		}
		
	}
	@Override
	public List<Comment> findCommentListByTaskId(String taskId) {
		Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();
		
		List<Comment> commentList = this.taskService.getProcessInstanceComments(task.getProcessInstanceId());
		return commentList;
	}
	@Override
	public void submitTask(String taskId, String comment, long id, String name,String message) {
		Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		//加批注前必须指定待办人
		Authentication.setAuthenticatedUserId(name);
		//1.加批注
		this.taskService.addComment(taskId, processInstanceId, comment);
		//2.任务完成
		Map<String,Object> map = new HashMap<>();
		map.put("message",message);
		this.taskService.complete(taskId, map);
		//3.判断流程实例是否结束，如果结束，请假单的状态改为2
		ProcessInstance pi = this.runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		if(pi == null) {//流程结束，只要有end_time就意味着结束 
//			Leavebill bill = this.leavebillMapper.selectByPrimaryKey(id);
			Baoxiaobill bill = this.baoxiaobillMapper.selectByPrimaryKey((int)id);
			System.out.println("没问题");
			bill.setState(2);
			this.baoxiaobillMapper.updateByPrimaryKey(bill);
		}
	}
	@Override
	public InputStream findImageInputStream(String deploymentId, String imageName) {
		
		return this.repositoryService.getResourceAsStream(deploymentId, imageName);
	}
	@Override
	public ProcessDefinition findProcessDefinitionByTaskId(String taskId) {
		Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();
		String processDefinitionId = task.getProcessDefinitionId();
		ProcessDefinition pd = this.repositoryService.createProcessDefinitionQuery()
							  .processDefinitionId(processDefinitionId)
							  .singleResult();
		return pd;
	}
	@Override
	public Map<String, Object> findCoordingByTask(String taskId) {
		//存放坐标
		Map<String, Object> map = new HashMap<String,Object>();
		//使用任务ID，查询任务对象
		Task task = taskService.createTaskQuery()//
					.taskId(taskId)//使用任务ID查询
					.singleResult();
		//获取流程定义的ID
		String processDefinitionId = task.getProcessDefinitionId();
		//获取流程定义的实体对象（对应.bpmn文件中的数据）
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity)repositoryService.getProcessDefinition(processDefinitionId);
		//流程实例ID
		String processInstanceId = task.getProcessInstanceId();
		//使用流程实例ID，查询正在执行的执行对象表，获取当前活动对应的流程实例对象
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()//创建流程实例查询
											.processInstanceId(processInstanceId)//使用流程实例ID查询
											.singleResult();
		//获取当前活动的ID
		String activityId = pi.getActivityId();
		//获取当前活动对象
		ActivityImpl activityImpl = processDefinitionEntity.findActivity(activityId);//活动ID
		//获取坐标
		map.put("x", activityImpl.getX());
		map.put("y", activityImpl.getY());
		map.put("width", activityImpl.getWidth());
		map.put("height", activityImpl.getHeight());
		return map;
	}
	@Override
	public void deleteDeploymentByDeployeementId(String deployeementId) {
		this.repositoryService.deleteDeployment(deployeementId,true);
		System.out.println("删除成功");
	}
	
	@Override
	public List<String> findOutComeListByTaskId(String taskId) {
		//返回存放连线的名称集合
		List<String> list = new ArrayList<String>();
		//1:使用任务ID，查询任务对象
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		//2：获取流程定义ID
		String processDefinitionId = task.getProcessDefinitionId();
		//3：查询ProcessDefinitionEntiy对象
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);
		//使用任务对象Task获取流程实例ID
		String processInstanceId = task.getProcessInstanceId();
		//使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()//
					.processInstanceId(processInstanceId)//使用流程实例ID查询
					.singleResult();
		//获取当前活动的id
		String activityId = pi.getActivityId();
		//4：获取当前的活动
		ActivityImpl activityImpl = processDefinitionEntity.findActivity(activityId);
		//5：获取当前活动完成之后连线的名称
		List<PvmTransition> pvmList = activityImpl.getOutgoingTransitions();
		if(pvmList!=null && pvmList.size()>0){
			for(PvmTransition pvm:pvmList){
				String name = (String) pvm.getProperty("name");
				if(StringUtils.isNotBlank(name)){
					list.add(name);
				} else{
					list.add("默认提交");
				}
			}
		}
		return list;
	}
	
}
