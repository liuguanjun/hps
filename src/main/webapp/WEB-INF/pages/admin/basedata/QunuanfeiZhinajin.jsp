<%@page pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<head>
	<meta charset="UTF-8">
	<title>取暖费滞纳金</title>
	<meta name="menu" content="AdminMenu"/>
</head>
<body>
    <div class="col-sm-10">
    <div class="easyui-layout" style="height: 500px;">
	<div data-options="region:'west',split:true,title:'区域导航'" style="width:170px;">
       <ul id="areaTree" class="easyui-tree"></ul>
    </div>
    <div id="mainPanle" region="center">
	<div  class="easyui-layout"  style="margin-top: 10px; margin-left: 10px;">
	    <form id="ff" method="post">
		    	<table id="dianjiaTable">
		    	     <tr>
		    			<td>截止日期:</td>
		    			<td><input levelInput=false class="easyui-datebox" type="text" name="name" 
		    			   value="2013-11-01"></input>
		    			</td>
		    			<td><div style="margin-left: 10px;">滞纳金收取比例:</div></td>
		    			<td><input levelInput=false  type="text" name="name"  style="width: 70px;"
		    			   value="3"></input>‰/日
		    			</td>
		    			<td>
		    			  <div style="margin-left: 10px;">
					    	  <a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#newUserDialog').dialog('close');">确定</a>
					    	  <a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#newUserDialog').dialog('close');">取消</a>
					      </div>
		    			</td>
		    		</tr>
		    	</table>
		    </form>
	</div>
	</div>
	</div>
	<c:set var="scripts" scope="request">
	   <%@ include file="/scripts/admin/basedata/QunuanfeiZhinajin.js"%>
	</c:set>
	</div>
</body>
