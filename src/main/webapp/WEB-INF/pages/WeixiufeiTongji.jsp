<%@page pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<head>
	<meta charset="UTF-8">
	<title>维修费统计报表</title>
	<meta name="menu" content="WeixiufeiTongji"/>
</head>
<body>
    <div class="easyui-layout" style="height: 500px;">
	<div data-options="region:'west',split:true,title:'区域导航'" style="width:170px;">
       <ul id="baseTree" class="easyui-tree" 
    	    data-options="url:'${ctx}/admin/BaseManagement/baseTree',
			method:'get',
			formatter:formatTreeNode,
			onLoadSuccess : selectFirstNode,
			onClick: loadCombobox">
    	</ul>
    </div>
    <div id="mainPanle" region="center">
    <div class="easyui-panel" title="查询条件" collapsible="true" style="height:100px;">
	  <div class="easyui-layout" fit="true">
  		<div region="east" border="false"  split="true"  style="width:210px;">
			<div style="padding-left: 10px;padding-top: 20px;;">
				<a href="#" class="easyui-linkbutton"  iconCls="icon-search" onclick="searchTongjiInfo()"
					style="margin-bottom: 5px;">查询</a>
				<a href="#" class="easyui-linkbutton" iconCls="icon-reload"
					onclick="clearHouseQuery()">重置</a>
			</div>
		</div>
		<div region="center" border="false" style="border:1px solid #ccc;padding-top: 20px;">
		   <table>
	    		<tr>
	    			<td><span style="margin-left: 10px;">收取年度:</span></td>
	    			<td>
	    			   <input id="paymentDateId_q" name="paymentDateId" baseComboboxId="base_q" class="easyui-combobox" 
	    			   		 style="width:155px;" panelHeight="100px">
                        </input>
	    			</td>
	    			<td><span style="margin-left: 10px;">操作员:</span></td>
	    			<td>
	    			   <input id="hpsUser_q" name="user.username" class="easyui-combobox" 
	    			   		type="caozuoyuan" hasEmpty="true" style="width:100px;" panelHeight="150px">
                       </input>
	    			</td>
	    			<td><div style="margin-left: 10px;text-align:right">起始日期:</div></td>
	    			<td><input id="startDate" style="width: 120px;" class="easyui-datebox" type="text" name="payStartDate" data-options="required:true"></input>
    				</td>
    				<td><div style="margin-left: 10px;text-align:right">截止日期:</div></td>
	    			<td><input id="endDate"  style="width: 120px;" class="easyui-datebox" type="text" name="payEndDate" data-options="required:true"></input>
    				</td>
    			</tr>
    	   </table>
		</div>
	   </div>
	</div>
	<div  class="easyui-layout" style="padding-top:5px">
	<table id="data_grid" class="easyui-datagrid"  title="维修费统计结果" 
			data-options="singleSelect:false,
			collapsible:true,
			method:'get',
			pageSize:10,
			onBeforeLoad:setQueryParams,
			onLoadSuccess:function() { $('#toolbarExport').linkbutton('enable'); },
			toolbar: [
				    {
				       id : 'toolbarExport',
					   iconCls: 'icon-print',
					   text:'导出缴费记录',
					   handler:function() { exportMaintainReport(); }
				    }]"
			rownumbers="true" pagination="true">
		<thead>
			<tr>
				<th data-options="field:'countId',width:110,align:'center',rowspan:2">收费户数</th>
				<th data-options="field:'mustChargeSum',align:'center',width:200,rowspan:3" >维修费计划总额</th>
				<th data-options="field:'actualChargeSum',width:200,align:'center',rowspan:2">维修费实缴总额</th>
			</tr>
		</thead>
	</table>
	</div>
	<c:set var="scripts" scope="request">
	   <%@ include file="/scripts/WeixiufeiTongji.js"%>
	</c:set>
	</div>
	</div>
</body>
