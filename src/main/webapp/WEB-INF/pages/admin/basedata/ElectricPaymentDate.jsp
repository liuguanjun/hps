<%@page pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<head>
	<meta charset="UTF-8">
	<title>电费缴纳日期</title>
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
			onClick: treeNodeOnClick">
    	</ul>
    </div>
    <div id="mainPanle" region="center">
    <div class="easyui-panel" title="查询条件" collapsible="true" style="height:80px;">
	  <div class="easyui-layout" fit="true">
			<div region="center" border="false" style="border:1px solid #ccc;padding-top: 10px;">
			   <table>
		    		<tr>
			    		<td><span style="margin-left: 10px;">收取年度:</span></td>
		    			<td>
		    			   <input id="year_q" class="easyui-combobox" 
		    			   		 style="width:155px;" panelHeight="200px">
	                        </input>
		    			</td>
	    			</tr>
	    	   </table>
			</div>
	   </div>
	</div>
	<div id="paymentDateDialog" class="easyui-dialog" title="电费缴费日期" style="width:400px" data-options="closed:true,modal:true">
		<div style="padding:10px 0 10px 55px">
	    <form id="paymentDateForm" method="post">
	    	<input id="id" type="hidden" name="id"/>
	    	<input id="method" type="hidden" name="_method" value="put"/>
	    	<table id="dianjiaTable">
	    	    <tr>
	    			<td>收取月份:</td>
	    			<td>
	    				<input id="month" type="text" style="width: 150px;background-color: #F3F3FA;" readonly="readonly" >      
						</input>
					</td>
	    		</tr>
	    	    <tr>
	    			<td>收费开始日:</td>
	    			<td><input id="startDate" name="startDate" class="easyui-datebox" type="text"
	    				 data-options="required:true" style="width: 150px;"></input>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td>收费结束日:</td>
	    			<td><input id="endDate" name="endDate" class="easyui-datebox" type="text"
	    				 data-options="required:true" style="width: 150px;"></input>
	    			</td>
	    		</tr>
	    	</table>
	    </form>
	    </div>
	    <div style="text-align:center;padding:5px">
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitPaymentDateForm();">确定</a>
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#paymentDateDialog').dialog('close');">取消</a>
	    </div>
	</div>
	<div  class="easyui-layout" style="padding-top:5px">
	<table id="data_grid" class="easyui-datagrid"  title="电费缴费日期" 
			data-options="singleSelect:true,
						collapsible:true,
						method:'get',
						onCheck : paymentDateRowOnCheck,
						onBeforeLoad:setQueryParams,
						toolbar: [
					    {
					       id : 'toolbarEditBtn',
						   iconCls: 'icon-edit',
						   text:'修改',
						   handler:function() { openPaymentDateDialog(); }
					    }]">
		<thead>
			<tr>
			    <th data-options="field:'ck',width:60,align:'center',checkbox:true">选择</th>
				<th data-options="field:'monthFormatStr',width:110,align:'center'">收取月份</th>
				<th data-options="field:'startDate',align:'center',width:150" >收取开始日</th>
				<th data-options="field:'endDate',width:150,align:'center'">收取结束日</th>
			</tr>
		</thead>
	</table>
	</div>
	<c:set var="scripts" scope="request">
	   <%@ include file="/scripts/admin/basedata/ElectricPaymentDate.js"%>
	</c:set>
	</div>
	</div>
	</div>
</body>
