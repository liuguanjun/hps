<%@page pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<head>
	<meta charset="UTF-8">
	<title>用户管理</title>
	<meta name="menu" content="AdminMenu"/>
</head>
<body>
    <div class="col-sm-10">
	<div class="easyui-panel" title="查询条件" collapsible="true" style="height:95px;">
	  <div class="easyui-layout" fit="true">
			<div region="east" border="false"  split="true"  style="width: 150px;">
				<div style="padding-left: 10px;padding-top: 2px;;">
					<a href="#" class="easyui-linkbutton"  iconCls="icon-search" 
						style="margin-bottom: 2px;" onclick="$('#data_grid').datagrid('reload')">查询</a>
					<a href="#" class="easyui-linkbutton" iconCls="icon-reload" 
						onclick="clearUserQuery()">重置</a>
				</div>
			</div>
			<div region="center" border="false">
				<table>
		    		<tr>
			    		<td><span style="margin-left: 10px;">账户名:</span></td>
		    			<td>
		    			   <input id="accountName_q" style="width: 150px;" />
		    			</td>
			    		<td><span style="margin-left: 10px;">所属基地:</span></td>
		    			<td>
		    			   <input id="base_q" class="easyui-combobox" type="base" hasEmpty="true"
		    			   	  style="width:155px;" panelHeight="120px">
	                        </input>
		    			</td>
			    		<td><span style="margin-left: 10px;">用户类型:</span></td>
		    			<td>
		    			   <input id="type_q" class="easyui-combobox" type="userType" 
		    			   		hasEmpty="true" style="width:155px;" panelHeight="100px">
	                        </input>
		    			</td>
	    			</tr>
		    		<tr>
			    		<td><span style="margin-left: 10px;">性别:</span></td>
		    			<td>
		    			   <input id="sex_q" class="easyui-combobox" type="sex" 
		    			   		hasEmpty="true" style="width:155px;" panelHeight="80px">
                           </input>
		    			</td>
			    		<td><span style="margin-left: 10px;">姓名:</span></td>
		    			<td>
		    			   <input id="userName_q" style="width: 150px;">
		    			</td>
			    		<td><span style="margin-left: 10px;">手机号:</span></td>
		    			<td>
		    			  <input class="easyui-numberbox" id="mobilePhoneNo_q" data-options="min:0,precision:0" style="width: 150px;">
		    			</td>
	    			</tr>
	    		</table>
			</div>
	   </div>
	</div>
	<div id="newUserDialog" class="easyui-dialog" title="新建用户" style="width:400px" data-options="closed:true,modal:true">
		<div style="padding:10px 0 10px 60px">
	    <form id="newUserDialogForm" method="post">
	    	<table>
	    		<tr>
	    			<td>账户名:</td>
	    			<td>
	    			   <input id="accountName" value="dummy" class="easyui-validatebox" type="text" 
	    			       name="accountName" data-options="required:true" style="width: 150px;"></input></td>
	    		</tr>
	    		<tr>
	    			<td>密码:</td>
	    			<td><input id="password" value="dummy" class="easyui-validatebox" type="password" name="password" data-options="required:true" style="width: 150px;"></input></td>
	    		</tr>
	    		<tr>
	    			<td>确认密码:</td>
	    			<td><input id="passwordConfirm" value="dummy" class="easyui-validatebox" type="password" data-options="required:true" style="width: 150px;"></input></td>
	    		</tr>
	    		<tr>
	    			<td>所属基地:</td>
	    			<td>
	    			    <input id="base" class="easyui-combobox" name="base.code" style="width:155px;" type="base" hasEmpty="false"
	    			    	panelHeight="120px">
                        </input>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td>用户类型:</td>
	    			<td>
	    			    <input id="type" class="easyui-combobox" name="type" type="userType" style="width:155px;" panelHeight="100px">
                        </input>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td>性别:</td>
	    			<td>
	    			    <input id="sex" class="easyui-combobox" name="sex" type="sex" style="width:155px;" panelHeight="80px">
                        </input>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td>姓名:</td>
	    			<td>
	    				<input id="userName" class="easyui-validatebox" name="userName" data-options="required:false" style="width: 150px;" value="ttt"></input>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td>手机号:</td>
	    			<td>
	    				<input id="mobilePhoneNo" class="easyui-numberbox" name="mobilePhoneNo" data-options="required:false" style="width: 150px;" value="ttt"></input>
	    			</td>
	    		</tr>
	    	</table>
	    </form>
	    </div>
	    <div style="text-align:center;padding:5px">
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitNewUserDialogForm()">确定</a>
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#newUserDialog').dialog('close');">取消</a>
	    </div>
	</div>
	<div id="updateUserDialog" class="easyui-dialog" title="修改用户" style="width:400px" data-options="closed:true,modal:true">
		<div style="padding:10px 0 10px 60px">
		<form:form id="updateUserDialogForm" method="put">
	        <input id="id" type="hidden" name="id">
	    	<table>
	    		<tr>
	    			<td>账户名:</td>
	    			<td><input id="accountName" value="dummy" class="easyui-validatebox" type="text" 
	    			      name="accountName" data-options="required:true" style="width: 150px;" disabled="disabled"></input></td>
	    		</tr>
	    		<tr>
	    			<td>密码:</td>
	    			<td><input id="password" value="dummy" class="easyui-validatebox" type="password" name="password" data-options="required:true" style="width: 150px;"></input></td>
	    		</tr>
	    		<tr>
	    			<td>确认密码:</td>
	    			<td><input id="passwordConfirm" value="dummy" class="easyui-validatebox" type="password" data-options="required:true" style="width: 150px;"></input></td>
	    		</tr>
	    		<tr>
	    			<td>所属基地:</td>
	    			<td>
	    			    <input id="base" class="easyui-combobox" name="base.code" style="width:155px;" type="base" hasEmpty="false"
	    			    	panelHeight="120px">
                        </input>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td>用户类型:</td>
	    			<td>
	    			    <input id="type" class="easyui-combobox" type="userType" name="type" style="width:155px;" panelHeight="100px">
                        </input>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td>性别:</td>
	    			<td>
	    			    <input id="sex" class="easyui-combobox" type="sex" name="sex" style="width:155px;" panelHeight="80px">
                        </input>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td>姓名:</td>
	    			<td>
	    				<input id="userName" class="easyui-validatebox" name="userName" data-options="required:false"  style="width: 150px;"></input>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td>手机号:</td>
	    			<td>
	    				<input id="mobilePhoneNo" class="easyui-numberbox" name="mobilePhoneNo" data-options="required:false" style="width: 150px;"></input>
	    			</td>
	    		</tr>
	    	</table>
	    </form:form>
	    </div>
	    <div style="text-align:center;padding:5px">
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitUpdateUserDialogForm()">确定</a>
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#updateUserDialog').dialog('close');">取消</a>
	    </div>
	</div>
	<table id="data_grid" class="easyui-datagrid" title="查询结果" 
			data-options="url:'${ctx}/admin/UserManagement/users',
				singleSelect:true,
				collapsible:true,
				method:'get',
				pageSize:10,
				onBeforeLoad:setQueryParams,
				toolbar: [
				    {
					   iconCls: 'icon-add',
					   text:'添加',
					   handler:function() { openNewUserDialog(); }
				    }, '-' ,
				    {
					   iconCls: 'icon-edit',
					   text:'编辑',
					   handler: function() { openUpdateUserDialog(); }
				   }, '-' ,
				    {
					   iconCls: 'icon-cancel',
					   text:'删除',
					   handler: function() { deleteUser(); }
				   }]"
			rownumbers="true" pagination="true" >
		<thead>
			<tr>
			    <th data-options="field:'ck',checkbox:true"></th>
				<th data-options="field:'accountName',width:120,align:'center'">账户名</th>
				<th data-options="field:'base.name',width:90,align:'center',formatter:getNestedValue">所属基地</th>
				<th data-options="field:'typeStr',width:90,align:'center'">用户类型</th>
				<th data-options="field:'userName',width:90,align:'center'">姓名</th>
				<th data-options="field:'sexStr',width:60,align:'center'">性别</th>
				<th data-options="field:'mobilePhoneNo',width:120,align:'center'">手机号</th>
				<th data-options="field:'createTime',width:140,align:'center'">创建时间</th>
				<th data-options="field:'id',width:100,align:'center',formatter:getOperHTML">操作</th>
			</tr>
		</thead>
	</table>
	<c:set var="scripts" scope="request">
	   <%@ include file="/scripts/admin/UserManagement.js"%>
	</c:set>
	</div>
</body>
