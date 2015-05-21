<%@page pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<head>
	<meta charset="UTF-8">
	<title>楼座管理</title>
    <meta name="menu" content="AdminMenu"/>
</head>
<body>
    <div class="col-sm-10">
    <div class="easyui-layout" style="height: 500px;">
    <div data-options="region:'west',split:true,title:'区域导航'" style="width:170px;">
       <ul id="areaTree" class="easyui-tree" 
           data-options="url:'${ctx}/admin/BaseManagement/baseTreeElements',
			method:'get',
			formatter:formatTreeNode,
			onLoadSuccess : selectFirstNode,
			onClick: function() { $('#data_grid').datagrid('reload'); }
			"></ul>
    </div>
    <div id="mainPanle" region="center">
	<div id="newLouzuoDialog" class="easyui-dialog" title="新建楼座" style="width:300px" data-options="closed:true">
		<div style="padding:10px 0 10px 20px">
	    <form:form id="newLouzuoDialogForm" method="post">
	    	<input id="base_id" type="hidden" name="base.id"/>
	    	<input id="area_id" type="hidden" name="area.id"/>
	    	<input id="area_name" type="hidden" name="area.name"/>
	    	<table>
	    		<tr>
	    			<td>楼座编号:</td>
	    			<td><input class="easyui-validatebox" type="text" name="code" data-options="required:true" style="width: 150px;"></input></td>
	    		</tr>
	    		<tr>
	    			<td>楼座名称:</td>
	    			<td><input class="easyui-validatebox" type="text" name="name" data-options="required:false" style="width: 150px;"></input></td>
	    		</tr>
	    	</table>
	    </form:form>
	    </div>
	    <div style="text-align:center;padding:5px">
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitnewLouzuoDialogForm()">确定</a>
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#newLouzuoDialog').dialog('close');">取消</a>
	    </div>
	</div>
	<table id="data_grid" class="easyui-datagrid"  title="查询结果" 
			data-options="url:'${ctx}/admin/LouzuoManagement/baselouzuo',
			singleSelect:false,
			collapsible:true,
			method:'get',
			onBeforeLoad:setQueryParams,
			pageSize:10,
				toolbar: [
				    {
					   iconCls: 'icon-add',
					   text:'添加',
					   handler:function() { openNewLouzuoDialog(); }
				    }]"
			rownumbers="true" pagination="true" >
		<thead>
			<tr>
				<th data-options="field:'code',width:200,align:'center'">楼座编码</th>
				<th data-options="field:'name',width:100,align:'center'">楼座名称</th>
				<th data-options="field:'area.name',width:100,align:'center',formatter:getNestedValue">所属区域</th>
				<th data-options="field:'id',width:120,align:'center',formatter:getOperHTML">操作</th>
			</tr>
		</thead>
	</table>
	<c:set var="scripts" scope="request">
	
	   <%@ include file="/scripts/admin/basedata/LouzuoManagement.js"%>
	</c:set>
	</div>
	</div>
	</div>
</body>
