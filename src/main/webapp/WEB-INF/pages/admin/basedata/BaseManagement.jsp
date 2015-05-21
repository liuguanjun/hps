<%@page pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<head>
	<meta charset="UTF-8">
	<title>区域管理</title>
	<meta name="menu" content="AdminMenu"/>
</head>
<body>
	<div id="newAreaDialog" class="easyui-dialog" title="修改区域" style="width:300px" data-options="closed:true">
		<div style="padding:10px 0 10px 20px">
	    <form id="newAreaDialogForm" method="post">
	        <input id="base_id" type="hidden" name="base.id"/>
	    	<table>
	    		<tr>
	    			<td>区域Code:</td>
	    			<td><input id="code" class="easyui-validatebox" value="dummy" type="text" name="code" data-options="required:true" style="width: 150px;"></input></td>
	    		</tr>
	    		<tr>
	    			<td>区域名称:</td>
	    			<td><input id="name" class="easyui-validatebox" value="dummy" type="text" name="name" data-options="required:true" style="width: 150px;"></input></td>
	    		</tr>
	    	</table>
	    </form>
	    </div>
	    <div style="text-align:center;padding:5px">
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitNewAreaDialogForm()">确定</a>
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#newAreaDialog').dialog('close');">取消</a>
	    </div>
	</div>
    <div class="col-sm-10">
	<table id="tree_grid" class="easyui-treegrid"  title="区域列表" 
			data-options="url:'${ctx}/admin/BaseManagement/baseTreeElements',
			collapsible:true,
			method:'get',
			rownumbers: true,
			idField: 'id',
			treeField: 'name'
			" pagination="true" >
		<thead>
			<tr>
				<th data-options="field:'name',width:200,align:'left'">基地/区域名称</th>
			    <th data-options="field:'code',width:200,align:'center'">编码</th>
				<th data-options="field:'systemInner',width:110,align:'center'">数据来源</th>
				<th data-options="field:'id',width:150,align:'center',formatter:getOperHTML">操作</th>
			</tr>
		</thead>
	</table>
	<c:set var="scripts" scope="request">
	   <%@ include file="/scripts/admin/basedata/BaseManagement.js"%>
	</c:set>
	</div>
</body>
