<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>流程管理</title>

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

<!--路径导航-->
<ol class="breadcrumb breadcrumb_nav">
    <li>首页</li>
    <li>流程管理</li>
    <li class="active">查看我的报销单</li>
</ol>
<!--路径导航-->

<div class="page-content">
    <form class="form-inline">
        <div class="panel panel-default">
            <div class="panel-heading">报销单列表</div>
            
            <div class="table-responsive">
                <table class="table table-striped table-hover">
                    <thead>
                    <tr>
                        <th width="5%">ID</th>
                        <th width="10%">报销金额</th>
                        <th width="10%">标题</th>
                        <th width="10%">备注</th>
                        <th width="27%">时间</th>
                        <th width="10%">状态</th>
                        <th width="28%">操作</th>
                    </tr>
                    </thead>
                    <tbody>
					<c:forEach var="list" items="${billList}">
						<c:if test="${list.state==2}">
							<tr>
		                        <td>${list.id}</td>
		                        <td>${list.money}</td>
		                        <td>${list.title}</td>
		                        <td>${list.remark}</td>
		                        <td>
		                        	<fmt:formatDate value="${list.creatdate}" pattern="yyyy-MM-dd HH:mm:ss"/>
		                        </td>
		                        <td>审核结束</td>
		                        <td>
		                            <a href="delBaoxiao?id=${list.id}" class="btn btn-danger btn-xs"><span class="glyphicon glyphicon-remove"></span> 删除</a>
		                            <a href="viewHistoryRecord?id=${list.id}" class="btn btn-success btn-xs"><span class="glyphicon glyphicon-eye-open"></span> 查看审核记录</a>
		                        </td>
		                      </tr>
						</c:if>
	                    <c:if test="${list.state==1}">
							<tr>
		                        <td>${list.id}</td>
		                        <td>${list.money}</td>
		                        <td>${list.title}</td>
		                        <td>${list.remark}</td>
		                        <td>
		                        	<fmt:formatDate value="${list.creatdate}" pattern="yyyy-MM-dd HH:mm:ss"/>
		                        </td>
		                        <td>审核中</td>
		                        <td>
		                            <a href="viewTaskRecord?id=${list.id}" class="btn btn-success btn-xs"><span class="glyphicon glyphicon-eye-open"></span> 查看审核记录</a>
		                            <a href="viewCurrentImageByBillId?id=${list.id}" class="btn btn-success btn-xs" target="_blank"><span class="glyphicon glyphicon-eye-open"></span> 查看当前流程图</a>
		                        </td>
		                      </tr>
						</c:if>
	                    </tr>
					</c:forEach>
					
                    </tbody>
                </table>
                
                
            </div>
        </div>
    </form>
    
   <div style="align: center;"><a href="${pageContext.request.contextPath}/myBaoxiaoBill?pageNum=${page.prePage}">上一页</a>当前第${page.pageNum}页/共${page.pages}页<a href="${pageContext.request.contextPath}/myBaoxiaoBill?pageNum=${page.nextPage}">下一页</a>，一共${page.total}记录</div>
</div>
</body>
</html>