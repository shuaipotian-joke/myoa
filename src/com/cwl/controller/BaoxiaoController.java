package com.cwl.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.http.HttpRequest;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cwl.pojo.ActiveUser;
import com.cwl.pojo.Baoxiaobill;
import com.cwl.pojo.Employee;
import com.cwl.pojo.EmployeeCustom;
import com.cwl.pojo.Leavebill;
import com.cwl.pojo.SysPermission;
import com.cwl.pojo.SysRole;
import com.cwl.service.BaoxiaoService;
import com.cwl.service.EmployeeService;
import com.cwl.service.WorkFlowService;
import com.cwl.utils.Constants;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
public class BaoxiaoController {
	@Autowired
	BaoxiaoService baoxiaoService;
	@Autowired
	WorkFlowService workFlowService;
	@Autowired
	EmployeeService employeeService;
	
	@RequestMapping("/main")
	public String main(Model model) {
		ActiveUser activeUser  = (ActiveUser)SecurityUtils.getSubject().getPrincipal();
		model.addAttribute("activeUser", activeUser);
		return "index";
	}
	
	@RequestMapping("/saveStartBaoxiao")
	public String saveStartBaoxiao(Baoxiaobill bill,HttpSession session) {
		ActiveUser activeUser  = (ActiveUser)SecurityUtils.getSubject().getPrincipal();
		this.baoxiaoService.saveBaoxiaoAndStartProcess(bill,activeUser);
		return "redirect:/myTaskList";
	}
	
	@RequestMapping("/myBaoxiaoBill")
	public String myBaoxiaoBill(Model model,@RequestParam(defaultValue = "1",required = false,value = "pageNum")int pageNum) {
		PageHelper.startPage(pageNum, 10);
		List<Baoxiaobill> billList = this.baoxiaoService.findAllBill();
		PageInfo<Baoxiaobill> page = new PageInfo<>(billList);
		model.addAttribute("billList", billList);
		model.addAttribute("page", page);
		return "showBaoxiao";
	}
	
	@RequestMapping("/delBaoxiao")
	public String delBaoxiao(int id) {
		this.baoxiaoService.deleteBaoxiao(id);
		return "redirect:/myBaoxiaoBill";
	}
	
	@RequestMapping("viewCurrentImageByBillId")
	public ModelAndView viewCurrentImageByBillId(int id) {
		//1.获取任务Id，获取任务对象，使用任务对象获取流程定义Id，查询流程定义对象
		String bussinesskey = Constants.BAOXIAO_KEY+"."+id;
		Task task = this.baoxiaoService.findTaskIdByBussinessKey(bussinesskey);
		ProcessDefinition pd = workFlowService.findProcessDefinitionByTaskId(task.getId());
		ModelAndView mv = new ModelAndView();
		mv.addObject("deploymentId", pd.getDeploymentId());
		mv.addObject("imageName", pd.getDiagramResourceName());
		Map<String,Object> map = workFlowService.findCoordingByTask(task.getId());
		mv.addObject("acs", map);
		mv.setViewName("viewimage");
		return mv;
	}
	
	@RequestMapping("/viewTaskRecord")
	public ModelAndView viewTaskRecord(int id) {
		String bussinessKey = Constants.BAOXIAO_KEY+"."+id;
		Task task = this.baoxiaoService.findTaskIdByBussinessKey(bussinessKey);
		String taskId = task.getId();
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
		mv.setViewName("approve_baoxiao");
		return mv;
	}
	
	@RequestMapping("viewHistoryRecord")
	public String viewHistoryRecord(int id,Model model) {
		Baoxiaobill bill = this.baoxiaoService.findBillById(id);
		List<Comment> commentList = baoxiaoService.findCommentByBillId(id);
		model.addAttribute("commentList", commentList);
		model.addAttribute("bill", bill);
		return "approve_baoxiao";
	}
	
	@RequestMapping("/findUserList")
	public String findUserList(Model model) {
		List<EmployeeCustom> userList = baoxiaoService.findAllUserList();
		List<SysRole> allRoles = baoxiaoService.findAllRoles();
		model.addAttribute("userList", userList);
		model.addAttribute("allRoles", allRoles);
		return "userlist";
	}
	@ResponseBody
	@RequestMapping("/lookRole")
	public Map<String,Object> lookRole(Model model,String name) {
		Map<String,Object> map = new HashMap<>();
		String rolename = baoxiaoService.findManagerByName(name);
		map.put("rolename", rolename);
		Map<String,Object> newmap = new HashMap<>();
		List<SysRole> roleAndPermission = employeeService.findRoleAndPermission(name);
		for (SysRole sysRole : roleAndPermission) {
			List<SysPermission> permissions = sysRole.getPermission();
			for (SysPermission sysPermission : permissions) {
				newmap.put(sysPermission.getName(), sysPermission.getName()+"【"+sysPermission.getType()+"】");
			}
		}
		map.put("newmap", newmap);
		return map;
	}
	
	
}
