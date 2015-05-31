<%@page pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<head>
	<meta charset="UTF-8">
	<title>供暖改造缴纳日期</title>
	<meta name="menu" content="AdminMenu"/>
</head>
<body>
    <div class="col-sm-10">
    <div class="easyui-layout" style="height: 500px;">
    <div id="mainPanle" region="center">
	<div id="newPaymentDateDialog" class="easyui-dialog" title="缴纳日期" style="width:700px" data-options="closed:true">
		<div style="padding:10px 0 10px 20px">
	    <form:form id="newPaymentDateDialogForm" method="post">
	        <input id="base_id" type="hidden" name="base.id"/>
	        <input id="id" type="hidden" name="id"/>
	    	<table>
	    		<tr>
	    			<td><div style="margin-left: 10px;text-align:right">供暖改造费缴纳标题:</div></td>
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
	    			<td>单价:</td>
	    			<td><input id="unit" class="easyui-validatebox" type="text" name="unit" data-options="required:true" style="width: 100px;"></input>元/平方米
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
			data-options="url:'${ctx}/weixiufeiPaymentDate/getWeixiufeiPayment',
			singleSelect:false,
			collapsible:true,
			method:'get',
			pageSize:10"
			rownumbers="true" pagination="true" >
		<thead>
			<tr>
				<th data-options="field:'title',width:200,align:'center'">供暖改造费缴纳标题</th>
				<th data-options="field:'payStartDate',width:100,align:'center'">缴纳日期开始</th>
				<th data-options="field:'payEndDate',width:100,align:'center',formatter:getNestedValue">缴纳日期结束</th>
				<th data-options="field:'unit',align:'center',width:100" >单价</th>
				<th data-options="field:'id',width:120,align:'center',formatter:getOperHTML">操作</th>
			</tr>
		</thead>
	</table>
	<c:set var="scripts" scope="request">
	   <%@ include file="/scripts/admin/basedata/HeatingMaintainPaymentDate.js"%>
	</c:set>
	</div>
	</div>
	</div>
</body>
