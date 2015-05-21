<%@page pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<head>
	<meta charset="UTF-8">
	<title>取暖费优惠</title>
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
			onClick: loadShouquYear">
    	</ul>
    </div>
    <div id="mainPanle" region="center">
    <div class="easyui-panel" title="查询条件" collapsible="true" style="height:80px;">
	  <div class="easyui-layout" fit="true">
			<div region="center" border="false" style="border:1px solid #ccc;padding-top: 10px;">
			   <table>
		    		<tr>
			    		<td><span style="margin-left: 10px;">取暖费收取年度:</span></td>
		    			<td>
		    			   <input id="paymentDateId_q" name="paymentDateId" baseComboboxId="base_q" class="easyui-combobox" 
		    			   		type="paymentDate" hasEmpty="true" style="width:155px;" panelHeight="80px">
	                        </input>
		    			</td>
	    			</tr>
	    	   </table>
			</div>
	   </div>
	</div>
	<div id="newShenfenxingzhiDialog" class="easyui-dialog" title="收费补贴比例" style="width:400px" data-options="closed:true">
		<div style="padding:10px 0 10px 55px">
	    <form id="newShenfenxingzhiForm" method="post">
	    	<input id="paymentDate_id" type="hidden" name="paymentDate.id"/>
	    	<input id="id" type="hidden" name="id"/>
	    	<table>
	    		<tr>
	    			<td>用户身份性质:</td>
	    			<td>
		    			<input id="ownerShenfenXingzhi" name="shenfengXingzhi.code"
	    				 	class="easyui-combobox" name="dept" style="width:155px;" panelHeight="200px"
	    				 	type="dictitem" dictcode="SHENFEN_XINGZHI">   
						</input>
		    		</td>
	    		</tr>
	    		<tr>
	    			<td>取暖费收费比例:</td>
	    			<td><input id="payRate" class="easyui-validatebox" type="text" name="payRate" data-options="required:true" style="width: 100px;"></input>%</td>
	    		</tr>
	    	</table>
	    </form>
	    </div>
	    <div style="text-align:center;padding:5px">
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitNewShenfenxingzhiForm();">确定</a>
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#newShenfenxingzhiDialog').dialog('close');">取消</a>
	    </div>
	</div>
	<div id="newDateYouhuiDialog" class="easyui-dialog" title="收费优惠期优惠比例" style="width:400px" data-options="closed:true">
		<div style="padding:10px 0 10px 55px">
	    <form id="newDateYouhuiForm" method="post">
	    	<input id="paymentDate_id_dateYouhui" type="hidden" name="paymentDate.id"/>
	    	<input id="id" type="hidden" name="id"/>
	    	<table>
	    		<tr>
	    			<td>开始日期:</td>
	    			<td>
	    			    <input id="startDate" levelInput=false class="easyui-datebox" type="text" name="startDate"></input>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td>结束日期:</td>
	    			<td>
	    			    <input id="endDate" levelInput=false class="easyui-datebox" type="text" name="endDate"></input>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td>优惠比例:</td>
	    			<td><input id="offRate" class="easyui-validatebox"  name="offRate" data-options="required:true" style="width: 70px;"></input>%</td>
	    		</tr>
	    	</table>
	    </form>
	    </div>
	    <div style="text-align:center;padding:5px">
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitNewDateYouhuiForm();">确定</a>
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#newDateYouhuiDialog').dialog('close');">取消</a>
	    </div>
	</div>
	<div class="easyui-layout" style="padding-bottom:5px;padding-top:5px">
			<table id="data_grid_shenfenxingzhi" class="easyui-datagrid" title="收费补贴" 
					data-options="url:'${ctx}/qunuanfeiYouhui/getShenfenxingzhiYouhui',
									singleSelect:false,
									collapsible:true,
									method:'get',
									onBeforeLoad:setQueryParams,
									pageSize:10,
									toolbar: [
								    {
									   iconCls: 'icon-add',
									   text:'添加',
									   handler:function() { openNewShenfenxingzhiDialog(); }
								    }]"
					rownumbers="true" pagination="true">
				<thead>
					<tr>
						<th data-options="field:'shenfengXingzhi.name',width:120,align:'center',formatter:getNestedValue">用户身份性质</th>
						<th data-options="field:'payRate',width:110,align:'center',formatter:getNestedRateValue">取暖费收费比例</th>
						<th data-options="field:'id',width:100,align:'center',formatter:getOperHTMLShenfen">操作</th>
					</tr>
				</thead>
			</table>
	</div>
	<div class="easyui-layout">
       <table id="data_grid_payment" class="easyui-datagrid"  title="收费优惠期设定" 
			data-options="url:'${ctx}/qunuanfeiYouhui/getPaymentYouhui',
									singleSelect:false,
									collapsible:true,
									method:'get',
									onBeforeLoad:setQueryParams,
									pageSize:10,
									toolbar: [
								    {
									   iconCls: 'icon-add',
									   text:'添加',
									   handler:function() { openNewDateYouhuiDialog(); }
								    }]"
					rownumbers="true" pagination="true">
		<thead>
			<tr>
				<th data-options="field:'startDate',width:110,align:'center'" >开始日期</th>
				<th data-options="field:'endDate',width:110,align:'center'" >截止日期</th>
				<th data-options="field:'offRate',width:110,align:'center',formatter:getNestedRateValue" >优惠幅度</th>
				<th data-options="field:'id',width:120,align:'center',formatter:getOperHTMLPayment">操作</th>
			</tr>
		</thead>
	   </table>
	</div>
	<c:set var="scripts" scope="request">
	   <%@ include file="/scripts/admin/basedata/QunuanfeiDiscount.js"%>
	</c:set>
	</div>
	</div>
	</div>
</body>
