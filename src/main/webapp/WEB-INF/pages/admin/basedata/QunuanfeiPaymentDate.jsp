<%@page pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<head>
	<meta charset="UTF-8">
	<title>取暖费缴纳日期</title>
	<meta name="menu" content="AdminMenu"/>
</head>
<body>
    <div class="col-sm-10">
    <div class="easyui-layout" style="height: 500px;">
	<div data-options="region:'west',split:true,title:'区域导航'" style="width:170px;">
    	<ul id="baseTree" class="easyui-tree" 
    	    data-options="url:'${ctx}/admin/BaseManagement/baseTree',
			method:'get',
			formatter:formatTreeNode,
			onLoadSuccess : selectFirstNode,
			onClick: function() { $('#data_grid').datagrid('reload'); }">
    	</ul>
    </div>
    <div id="mainPanle" region="center">
	<div id="newPaymentDateDialog" class="easyui-dialog" title="缴纳日期和滞纳金收取比例" style="width:700px" data-options="closed:true">
		<div style="padding:10px 0 10px 20px">
	    <form:form id="newPaymentDateDialogForm" method="post">
	        <input id="base_id" type="hidden" name="base.id"/>
	        <input id="id" type="hidden" name="id"/>
	    	<table>
	    		<tr>
	    			<td><div style="margin-left: 10px;text-align:right">取暖费缴纳标题:</div></td>
	    			<td><input id="title" class="easyui-validatebox" type="text" name="title" data-options="required:true" style="width: 313px;"></input></td>
	    		</tr>
	    		<tr>
	    			<td><div style="margin-left: 10px;text-align:right">缴纳日期:</div></td>
	    			<td><input id="payStartDate" class="easyui-datebox" type="text" name="payStartDate" data-options="required:true"></input>
    			   		~
    			   		<input id="payEndDate" class="easyui-datebox" type="text" name="payEndDate" data-options="required:true"></input>
    				</td>
	    		</tr>
	    		<tr>
    			<td><div style="margin-left: 10px;text-align:right">滞纳金收取比例:</div></td>
    			<td><input id="zhinajinRate" class="easyui-validatebox"  type="text" name="zhinajinRate" style="width: 70px;" data-options="required:true"></input>‰/日
    			</td>
    			</tr>
    			<tr>
    			<td><div style="margin-left: 10px;text-align:right">申请停供收取比例:</div></td>
    			<td><input id="stopHeatingRate" class="easyui-validatebox"  type="text" name="stopHeatingRate" style="width: 70px;" data-options="required:true"></input>%
    			</td>
    			</tr>
    			<tr>
    			<td><div style="margin-left: 10px;text-align:right">困难住户收取比例:</div></td>
    			<td><input id="livingSoHardRate" class="easyui-validatebox"  type="text" name="livingSoHardRate" style="width: 70px;" data-options="required:true"></input>%
    			</td>
    		</tr>
	    	</table>
	    </form:form>
	    </div>
	    <div style="text-align:center;padding:5px">
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitNewPaymentDateForm()">确定</a>
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#newPaymentDateDialog').dialog('close');">取消</a>
	    </div>
	</div>
	<table id="data_grid" class="easyui-datagrid"  title="查询结果" 
			data-options="url:'${ctx}/heatingPaymentDate/getHeatPaymentAndZhinajin',
			singleSelect:false,
			collapsible:true,
			method:'get',
			onBeforeLoad:setQueryParams,
			pageSize:10,
				toolbar: [
				    {
					   iconCls: 'icon-add',
					   text:'添加',
					   handler:function() { openNewPaymentDateDialog(); }
				    }]"
			rownumbers="true" pagination="true" >
		<thead>
			<tr>
				<th data-options="field:'title',width:200,align:'center'">取暖费缴纳标题</th>
				<th data-options="field:'payStartDate',width:100,align:'center'">缴纳日期开始</th>
				<th data-options="field:'payEndDate',width:100,align:'center',formatter:getNestedValue">缴纳日期结束</th>
				<th data-options="field:'zhinajinRate',width:100,align:'center',formatter:getNestedZhinaValue">滞纳金收取比例</th>
				<th data-options="field:'stopHeatingRate',width:120,align:'center',formatter:getNestedRateValue">申请停供收取比例</th>
				<th data-options="field:'livingSoHardRate',width:120,align:'center',formatter:getNestedRateValue">困难住户收取比例</th>
				<th data-options="field:'id',width:120,align:'center',formatter:getOperHTML">操作</th>
			</tr>
		</thead>
	</table>
	<c:set var="scripts" scope="request">
	   <%@ include file="/scripts/admin/basedata/QunuanfeiPaymentDate.js"%>
	</c:set>
	</div>
	</div>
	</div>
</body>
