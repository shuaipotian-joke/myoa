package com.cwl.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;

import com.cwl.pojo.Baoxiaobill;
import com.cwl.pojo.Employee;
import com.cwl.pojo.Leavebill;

public interface WorkFlowService {
	public void deployProcess(String processName,InputStream input);
	
	public List<Deployment> findAllDeployeements();
	//返回流程定义信息
	public List<ProcessDefinition> findAllProcessDefinitions();

	public void saveLeaveAndStartProcess(Leavebill bill,Employee emp);

	public List<Task> findMyTaskListByUserId(String name);

	public Map<String, Object> findBillByTaskId(String taskId);

	public List<Comment> findCommentListByTaskId(String taskId);

	public void submitTask(String taskId, String comment, long id, String name,String message);

	public InputStream findImageInputStream(String deploymentId, String imageName);

	public ProcessDefinition findProcessDefinitionByTaskId(String taskId);

	public Map<String, Object> findCoordingByTask(String taskId);

	public void deleteDeploymentByDeployeementId(String deployeementId);


	public List<String> findOutComeListByTaskId(String taskId);
}
