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
<title>角色列表</title>
<%
	pageContext.setAttribute("ctp", request.getContextPath());
%>
<!-- Bootstrap -->
<link href="bootstrap/css/bootstrap.css" rel="stylesheet">
<link href="css/content.css" rel="stylesheet">

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
    <script src="http://cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="http://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
<script src="js/jquery.min.js"></script>
<script src="bootstrap/js/bootstrap.min.js"></script>
</head>
<body>
	<ol class="breadcrumb breadcrumb_nav">
		<li>首页</li>
		<li>用户管理</li>
		<li class="active">角色添加</li>
	</ol>
	${roleList}
	
	<div class="page-content">
		<div class="panel-heading">
			角色列表&nbsp;&nbsp;&nbsp;
			<div class="panel panel-default">
				<div class="table-responsive">
					<table class="table table-striped table-hover">
						<thead>
							<tr>
								<th width="70%">角色名称</th>
								<th width="30%">操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="role" items="${roleList}">
								<tr>
									<td>${role.name}</td>
									<td>
										<a href="#"  class="btn btn-primary btn-xs" data-toggle="modal" data-target="#editModal">
											<span class="glyphicon glyphicon-edit" ></span>编辑
										</a>
										<a href="${ctp}/delRole?id=${role.id}" class="btn btn-danger btn-xs">
											<span class="glyphicon glyphicon-remove"></span>删除
										</a>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog" >
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
			<h3 id="myModalLabel">权限列表</h3>
		</div>
		<div class="modal-body" id="roleList">
			 <table class="table table-bordered"  width="800px">	
			    <thead>	
			      	<tr>         
						<th width="35%">主菜单</th>
						<th width="65%">权限</th>
	                </tr>
                </thead>
                <tbody id="roleListBody">
                	<c:forEach var="roles" items="${menu}">
	                	<tr>         
							<td><input type="checkbox"/>${roles.name}</td>
							<td>
								<c:forEach var="role" items="${roles.children}">
									<input type="checkbox"/> ${role.name}<br/>
								</c:forEach>
							</td>
		                </tr>
                	</c:forEach>
                </tbody>
			 </table>				
		</div>
		<div class="modal-footer">
			<button class="btn btn-default" data-dismiss="modal" aria-hidden="true">关闭</button>
		</div>
	  </div>
	</div>

</div>
<script type="text/javascript">
function viewPermission(id){
	$.ajax({
		url:"${ctp}/showRoles",
		success:function(data){
			$.each(data,function(name,val){
				alert(name);
				alert(val);
				$("#td1").append(name);
				
			})
			
		}
	});
}
</script>
</body>
</html>