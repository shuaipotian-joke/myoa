package com.cwl.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.cwl.pojo.ActiveUser;
import com.cwl.pojo.Baoxiaobill;
import com.cwl.pojo.Employee;
import com.cwl.pojo.Leavebill;
import com.cwl.service.WorkFlowService;
import com.cwl.utils.Constants;
@Controller
public class WorkFlowController {
	@Autowired
	WorkFlowService workFlowService;
	
	@RequestMapping("/deployProcess")
	public String deployProcess(String processName,MultipartFile fileName) {
		try {
			this.workFlowService.deployProcess(processName, fileName.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "redirect:/processDefinitionList";
	}
	
	@RequestMapping("/processDefinitionList")
	public ModelAndView processDefinitionList() {
		List<Deployment> deploymentList = this.workFlowService.findAllDeployeements();
		List<ProcessDefinition> Processlist = this.workFlowService.findAllProcessDefinitions();
		ModelAndView mv = new ModelAndView();
		mv.addObject("depList", deploymentList);
		mv.addObject("pdList", Processlist);
		
		mv.setViewName("workflow_list");
		return mv;
	}
	
	@RequestMapping("/saveStartLeave")
	public String saveStartLeave(Leavebill bill,HttpSession session) {
		//保存请假的业务数据和启动流程实例,启动实例时需要指定代办人是谁
		Employee employee = (Employee)session.getAttribute(Constants.GLOBAL_SESSION_ID);
		
		this.workFlowService.saveLeaveAndStartProcess(bill, employee);
		return "redirect:/myTaskList";
	}
	
	@RequestMapping("/myTaskList")
	public ModelAndView myTaskList() {
		ActiveUser activeUser  = (ActiveUser)SecurityUtils.getSubject().getPrincipal();
		List<Task> taskList = this.workFlowService.findMyTaskListByUserId(activeUser.getName());
		ModelAndView mv = new ModelAndView();
		mv.addObject("taskList", taskList);
		mv.setViewName("workflow_task");
		return mv;
	}
	
	@RequestMapping("/viewTaskForm")
	public ModelAndView viewTaskForm(String taskId) {
		//根据任务id查询当前对应的请假单信息
		Map<String,Object> map = this.workFlowService.findBillByTaskId(taskId);
		//查询当前任务的批注记录列表
		List<Comment> commentList = this.workFlowService.findCommentListByTaskId(taskId);
		List<String> buttonName = this.workFlowService.findOutComeListByTaskId(taskId);
		Object bill;
		ModelAndView mv = new ModelAndView();
		if(map.containsKey("baoxiaoProcess")) {
			bill = map.get("baoxiaoProcess");
			mv.addObject("bill", (Baoxiaobill)bill);
			mv.addObject("Object","baoxiaoProcess");
		}else if(map.containsKey("leaveProcess")) {
			bill = map.get("leaveProcess");
			mv.addObject("Object","leaveProcess");
			mv.addObject("bill", (Leavebill)bill);
		}
		mv.addObject("buttonName", buttonName);
		mv.addObject("commentList", commentList);
		mv.addObject("taskId", taskId);
		mv.setViewName("approve_leave");
		return mv;
	}
	
	@RequestMapping("/submitTask")
	public String submitTask(long id,String taskId,String comment,HttpSession session,String message) {
		System.out.println(message);
		
		ActiveUser activeUser  = (ActiveUser)SecurityUtils.getSubject().getPrincipal();
		this.workFlowService.submitTask(taskId,comment,id,activeUser.getName(),message);
		return "redirect:/myTaskList";
	}
	
	@RequestMapping("/viewImage")
	public String viewImage(String deploymentId,String imageName,HttpServletResponse response) throws IOException {
		InputStream in = workFlowService.findImageInputStream(deploymentId,imageName);
		OutputStream out = response.getOutputStream();
		for(int i;(i=in.read())!=-1;) {
			out.write(i);
		}
		out.close();
		in.close();
		return "redirect:/myTaskList";
	}
	
	@RequestMapping("/viewCurrentImage")
	public ModelAndView viewCurrentImage(String taskId) throws IOException {
		//1.获取任务Id，获取任务对象，使用任务对象获取流程定义Id，查询流程定义对象
		ProcessDefinition pd = workFlowService.findProcessDefinitionByTaskId(taskId);
		ModelAndView mv = new ModelAndView();
		mv.addObject("deploymentId", pd.getDeploymentId());
		mv.addObject("imageName", pd.getDiagramResourceName());
		Map<String,Object> map = workFlowService.findCoordingByTask(taskId);
		mv.addObject("acs", map);
		mv.setViewName("viewimage");
		return mv;
	}
	
	@RequestMapping("delDeployment")
	public String delDeployment(String deploymentId) {
		this.workFlowService.deleteDeploymentByDeployeementId(deploymentId);
		return "redirect:/processDefinitionList";
	}
	
	
}
