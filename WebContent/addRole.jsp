<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>流程管理</title>
<%
	pageContext.setAttribute("ctp", request.getContextPath());
%>
<!-- Bootstrap -->
<link href="bootstrap/css/bootstrap.css" rel="stylesheet">
<link href="css/content.css" rel="stylesheet">
<script src="js/jquery.min.js"></script>
<script src="bootstrap/js/bootstrap.min.js"></script>
</head>
<body>

	<!--路径导航-->
	<ol class="breadcrumb breadcrumb_nav">
		<li>首页</li>
		<li>用户管理</li>
		<li class="active">角色添加</li>
	</ol>
	<!--路径导航-->

	<div class="page-content">
		<form class="form-inline" id="addRolmByForm" enctype="multipart/form-data">
			<div class="panel panel-default">
				<div class="panel-heading">添加角色&nbsp;&nbsp;&nbsp;</div>
				<div class="table-responsive">
					<div class="form-group">
						<label for="exampleInputEmail2">角色名称</label> <input type="text"
							class="form-control">
					</div>
					<input type="submit" class="btn btn-primary" value="保存角色和权限">
				</div>
			</div>
			<div class="panel panel-default">
				<div class="panel-heading">
					权限分配列表&nbsp;&nbsp;&nbsp;
					<button type="button" class="btn btn-primary" title="新建"
						data-toggle="modal" data-target="#createUserModal">新建权限</button>
				</div>

				<div class="table-responsive">
					<table class="table table-striped table-hover">
						<thead>
							<tr>
								<th width="35%">主菜单</th>
								<th width="65%">子菜单和权限</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="permission" items="${PermissionList}">
								<tr>
									<td><div class="checkbox">
											<label> <input name="checkName" type="checkbox">
												${permission.name}
											</label>
										</div></td>
									<td><c:forEach items="${permission.childrenName}"
											var="child">
											<div class="checkbox">
												<label> <input name="checkName" type="checkbox">
													${child.name}
												</label>
											</div>
											<br />
										</c:forEach></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</form>
	</div>
	<!--添加用户 编辑窗口 -->
	<div class="modal fade" id="createUserModal" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<form id="permissionForm" action="saveUser" method="post">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-hidden="true">×</button>
						<h3 id="myModalLabel">编辑用户</h3>
					</div>
					<div class="modal-body">
						<table class="table table-bordered table-striped" width="800px">
							<tr>
								<td>帐号</td>
								<td><input class="form-control" name="name"
									placeholder="名称"></td>
							</tr>
							<tr>
								<td>初始密码</td>
								<td><input class="form-control" type="password"
									name="password" placeholder="名称"></td>
							</tr>
							<tr>
								<td>电子邮箱</td>
								<td><input class="form-control" name="email"
									placeholder="链接"></td>
							</tr>
							<tr>
								<td>级别</td>
								<td><select class="form-control" name="role"
									onchange="getNextManager(this.value)">
										<option value="1">普通员工</option>
										<option value="2">一级主管</option>
										<option value="3">二级主管</option>
										<option value="4">总经理</option>
								</select></td>
							</tr>
							<tr>
								<td>上级主管</td>
								<td><select id="selManager" class="form-control"
									name="managerId"></select></td>
							</tr>

						</table>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-success" data-dismiss="modal"
							aria-hidden="true"
							onclick="javascript:document.getElementById('permissionForm').submit()">保存</button>
						<button class="btn btn-default" data-dismiss="modal"
							aria-hidden="true">关闭</button>
					</div>
				</div>
			</div>
		</form>
	</div>
	<!-- 查看用户角色权限窗口 -->
	<div class="modal fade" id="editModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h3 id="myModalLabel">权限列表</h3>
				</div>
				<div class="modal-body" id="roleList">
					<table class="table table-bordered" width="800px">
						<thead>
							<tr>
								<th>角色</th>
								<th>权限</th>
							</tr>
						</thead>
						<tbody id="roleListBody">
							<tr>
								<td id="td1"></td>
								<td id="td2"></td>
							</tr>
						</tbody>
					</table>
				</div>
				<div class="modal-footer">
					<button class="btn btn-default" data-dismiss="modal"
						aria-hidden="true">关闭</button>
				</div>
			</div>
		</div>

	</div>

	<script type="text/javascript">
	 var addRolmByForm = $('#addRolmByForm');
	var formData = new FormData($("#addRolmByForm")[0]);
	$.ajax({
	      dataType: "json",
	      type: "post", // 提交方式 get/post
	      url: '${ctp}/getForm', // 需要提交的 url
	      data: formData,
	      processData: false,
	      contentType: false,
	      success: function(data) {
	    	  alert("success:"+data);
	      },
		  error: function(error) {
		  	  alert("error:"+error);
		  } 
	}); 
	
	</script>
</body>
</html>