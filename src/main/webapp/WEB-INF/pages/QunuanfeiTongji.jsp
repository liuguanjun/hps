<%@page pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<head>
	<meta charset="UTF-8">
	<title>取暖费统计报表</title>
	<meta name="menu" content="QunuanfeiTongji"/>
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
  		<div region="east" border="false"  split="true"  style="width: 210px;">
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
	<table id="data_grid" class="easyui-datagrid"  title="取暖费统计结果" 
			data-options="singleSelect:false,
			collapsible:true,
			method:'get',
			pageSize:10,
			onBeforeLoad: setQueryParams,
			onLoadSuccess:function() { $('#toolbarExport').linkbutton('enable'); },
			toolbar: [
				    {
				       id : 'toolbarExport',
					   iconCls: 'icon-print',
					   text:'导出缴费记录',
					   handler:function() { exportHeatingReport(); }
				    }]"
			rownumbers="true" pagination="true">
		<thead>
			<tr>
				<th data-options="field:'countId',width:110,align:'center',rowspan:2">收费户数</th>
				<th data-options="field:'jihuaAll',align:'center',width:100,rowspan:3" >计划总额</th>
				<th data-options="align:'center',colspan:8" >实缴取暖费</th>
				<th data-options="field:'shijiaoAllCharge',width:100,align:'center',rowspan:2">实缴取暖费总额</th>
				<th data-options="align:'center',colspan:2" >实缴滞纳金</th>
				<th data-options="align:'center',colspan:3" >减免</th>
				<th data-options="field:'jianmianAllCharge',width:100,align:'center',rowspan:2">减免总额</th>
			</tr>
			<tr>
				<th data-options="field:'quanECount',width:100,align:'center'">全额收费(例)</th>
				<th data-options="field:'quanECharge',width:100,align:'center'">全额收费额</th>
				<th data-options="field:'huihouCount',width:100,align:'center'">惠后收费(例)</th>
				<th data-options="field:'huihouCharge',width:100,align:'center'">惠后收费额</th>
				<th data-options="field:'tinggongCount',width:100,align:'center'">停供收费(例)</th>
				<th data-options="field:'tinggongCharge',width:100,align:'center'">停供收费额</th>
				<th data-options="field:'kunnanCount',width:100,align:'center'">困难住户收费(例)</th>
				<th data-options="field:'kunnanCharge',width:100,align:'center'">困难住户收费额</th>
				
				<th data-options="field:'zhinajinCount',width:100,align:'center'">滞纳缴费(例)</th>
				<th data-options="field:'zhinajinCharge',width:100,align:'center'">滞纳缴费额</th>
				
				<th data-options="field:'youhuiJianmianCharge',width:100,align:'center'">优惠减免额</th>
				<th data-options="field:'tinggongJianmianCharge',width:100,align:'center'">停供减免额</th>
				<th data-options="field:'kunnanJianmianCharge',width:100,align:'center'">困难住户减免额</th>
				
			</tr>
		</thead>
	</table>
	</div>
	<c:set var="scripts" scope="request">
	   <%@ include file="/scripts/QunuanfeiTongji.js"%>
	</c:set>
	</div>
	</div>
</body>
