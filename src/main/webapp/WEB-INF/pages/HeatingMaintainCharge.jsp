<%@page pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<jsp:useBean id="now" class="java.util.Date" /> 
<head>
	<meta charset="UTF-8">
	<title>收供暖改造费</title>
	<meta name="menu" content="HeatingMaintain"/>
</head>
<body>
    <div class="col-sm-10">
	<div class="easyui-panel" title="查询条件" collapsible="true" style="height:140px;">
	  <div class="easyui-layout" fit="true">
			<div region="east" border="false"  split="true"  style="width: 200px;">
				<div style="padding-left: 10px;padding-top: 20px;;">
					<a href="#" class="easyui-linkbutton"  iconCls="icon-search" style="margin-bottom: 5px;" onclick="$('#data_grid').datagrid('reload')">查询</a>
					<a href="#" class="easyui-linkbutton" iconCls="icon-reload" onclick="clearHeatingQuery()">重置</a>
				</div>
			</div>
			<div region="center" border="false" style="border:1px solid #ccc;">
				 <table>
		    		<tr>
			    		<td><span style="margin-left: 10px;">基地:</span></td>
		    			<td>
		    			   <input id="base_q" name="baseCode" class="easyui-combobox" type="base"
		    			   	  style="width:155px;" panelHeight="120px">
	                        </input>
		    			</td>
			    		<td><span style="margin-left: 10px;">区域:</span></td>
		    			<td>
		    			   <input id="area_q" name="areaCode" class="easyui-combobox" type="area" baseComboboxId="base_q" hasEmpty="true"
		    			   	  style="width:155px;" panelHeight="200px">
	                        </input>
		    			</td>
		    		    <td><span style="margin-left: 10px;">户主名称:</span></td>
		    			<td>
		    			   <input id="ownerName_q" name="ownerName" style="width:155px;"></input>
		    			</td>
	    			</tr>
		    		<tr>
		    			<td><span style="margin-left: 10px;">工资号:</span></td>
		    			<td>
		    			   <input id="wageNum_q" name="wageNum" style="width:155px;"></input>
		    			</td>
			    		<td><span style="margin-left: 10px;">房号:</span></td>
		    			<td>
		    			   <input id="house_q" name="houseNo" style="width:155px;"></input>
		    			</td>
		    			<td><span style="margin-left: 10px;">楼座:</span></td>
		    			<td>
		    			   <input id="louzuo_q" type="louzuo" baseComboboxId="base_q" 
		    			   		areaComboboxId="area_q" name="louzuoCode" class="easyui-combobox" 
		    			   		hasEmpty="true" style="width:155px;" panelHeight="200px">
	                        </input>
		    			</td>
	    			</tr>
	    			<tr>
		    			<td><span style="margin-left: 10px;">单元:</span></td>
		    			<td>
		    			   <input id="danyuan_q" name="danyuan" style="width:155px;"></input>
		    			</td>
		    			<td><span style="margin-left: 10px;">楼层:</span></td>
		    			<td>
		    			   <input id="louceng_q" name="ceng" style="width:155px;"></input>
		    			</td>

						<td><span style="margin-left: 10px;">收费员:</span></td>
		    			<td>
		    			   <input id="operName_q" name="operName" style="width:155px;"></input>
		    			</td>
	    			</tr>
	    			<tr>
		    			<td><span style="margin-left: 10px;">收费日期:</span></td>
		    			<td>
		    			   <input id="chargeDate_q" class="easyui-datebox" name="chargeDate" style="width:155px;"></input>
		    			</td>
		    			<td><span style="margin-left: 10px;">状态:</span></td>
		    			<td>
		    			   <input id="chargeState_q" type="electricChargeState" 
		    			   		hasEmpty="true" name="chargeState" style="width:155px;" panelHeight="100px"></input>
		    			</td>
		    			<td><span style="margin-left: 10px;">缴费备注:</span></td>
		    			<td>
		    			   <input id="recordRemarks_q" name="recordRemarks" style="width:155px;"></input>
		    			</td>
	    			</tr>
	    	   </table>
			</div>
	   </div>
	</div>
	<table id="data_grid" class="easyui-datagrid" title="查询结果" 
			data-options="url:'${ctx}/heatingmaintain2015/chargerecords',
			singleSelect:true,
			collapsible:true,
			method:'get',
			pageSize:10,
			onBeforeLoad:setQueryParams,
			onCheck : recordOnCheck,
			onUncheck : recordOnUncheck,
			onLoadSuccess: onLoadSuccess,
			toolbar: [
				    {
				       id : 'toolbarChargeBtn',
					   iconCls: 'icon-add',
					   text:'缴费',
					   handler:function() { openChargeDialog(); }
				    }, '-' ,
				    <security:authorize ifAnyGranted='ROLE_SUPERADMIN,ROLE_ADMIN'>  
				    {
				       id : 'toolbarCancelBtn',
					   iconCls: 'icon-cancel',
					   text:'取消结算',
					   handler: function() { openCancelDialog(); }
				   }, '-' ,
				   </security:authorize>
				    {
				       id : 'toolbarDetailBtn',
					   iconCls: 'icon-search',
					   text:'查看',
					   handler: function() { openDetailDialog(); }
				   },'-' ,
				   {
				       id : 'toolbarExport',
					   iconCls: 'icon-print',
					   text:'导出缴费记录',
					   handler:function() { exportHeatingReport(); }
				    }]"
			rownumbers="true" pagination="true">
		<thead>
			<tr>
				<th data-options="field:'ck',align:'center',checkbox:true"></th>
				<th data-options="field:'paymentDate.title',width:180,align:'center',formatter:getNestedValue">缴费项目名称</th>
				<th data-options="field:'house.no',width:80,align:'center',formatter:getNestedValue">房号</th>
				<th data-options="field:'houseOwner.name',width:70,align:'center',formatter:getNestedValue">户主姓名</th>
				<th data-options="field:'house.address',width:240,align:'center',formatter:getNestedValue">房屋地址</th>
				<th data-options="field:'chargeState',width:60,align:'center'">缴费状态</th>
				<th data-options="field:'house.repairArea',width:70,align:'center',formatter:getNestedValue">维修面积</th>
				<th data-options="field:'paymentDate.unit',width:70,align:'center',formatter:getNestedValue">单价</th>
				<th data-options="field:'chargeDate',width:130,align:'center'">缴费日期</th>
				<th data-options="field:'mustCharge',width:70,align:'center'">应收合计</th>
				<th data-options="field:'actualCharge',width:70,align:'center'">实收合计</th>
				<th data-options="field:'wageNum',width:100,align:'center'">工资号</th>
			</tr>
		</thead>
	</table>
	<div class="easyui-layout" fit="true" style="margin-top: 5px;">
	    <table id="tongjiResult">
    		<tr>
    		    <td><span style="margin-left: 10px;">应收改造费:</span></td>
    			<td>
    			   <span style="margin-left: 10px;" id="yingshouHeatingCharge">0</span>元</td>
    			</td>
    			<td><span style="margin-left: 10px;">已收改造费:</span></td>
    			<td>
    			   <span style="margin-left: 10px;" id="yishouHeatingCharge">0</span>元</td>
    			</td>
    		</tr>
		</table>
	</div>
	<div id="chargeDialog" class="easyui-dialog" title="缴费" style="width:760px;height: 320px;" data-options="closed:true,modal:true">
		<div class="easyui-layout" fit="true" >
		   <div region="east" border="false" split="true"  style="width:260px;">
				 <div class="easyui-panel"  title="结算">
				 <form id="chargeForm" method="post">
				  <input type="hidden" id="recordId" name="recordId" />
				  <input type="hidden" name="_method" value="put" />
	              <table>
				    <tr>
		    			<td><span style="margin-left: 5px;">收费日期:</span></td>
		    			<td><input id="chargeDate" readonly="readonly" style="width:100px;"
		    			 	    value='<fmt:formatDate value="${now}" type="both" dateStyle="long" pattern="yyyy-MM-dd" />'>  
		    			    </input>
		    			</td>
		    		</tr>
		    		<tr>
		    			<td><span style="margin-left: 5px;">收费员:</span></td>
		    			<td><input id="operUserName" readonly="readonly" style="width:100px;">  
		    			    </input>
		    			</td>
		    		</tr>
		    		<tr>
		    			<td><span style="margin-left: 5px;">应收金额 :</span></td>
		    			<td><input id="mustCharge" readonly="readonly" style="width:100px;text-align: right;"></input>元</td>
		    		</tr>
		    		<tr>
		    		   <td><span style="margin-left: 5px;">实收金额:</span></td>
		    			<td>
		    				<input id="actualCharge" class="easyui-numberbox" data-options="required:true,min:0,precision:2,max:1000000"
		    					 name="actualCharge" style="width:100px;text-align: right;" value="0"></input>元
						</td>
		    		</tr>
		    		<tr>
		    		   <td><span style="margin-left: 5px;">工资号:</span></td>
		    			<td>
		    				<input id="wageNum" name="wageNum" style="width:180px;text-align: left;"></input>
						</td>
		    		</tr>
		    		<tr>
		    		   <td><span style="margin-left: 5px;">备注:</span></td>
		    		   <td>
		    		       <textarea id="remarks" name="remarks" style="font-size: small;width: 180px;height:80px;"></textarea>
		    		   </td>
		    		</tr>
		    	  </table>
		    	  </form>
				</div>
				<div style="text-align:center;padding:5px;white-space: nowrap;">
<!-- 				   <span style="vertical-align: middle;">打印</span> -->
<!-- 				   <input style="vertical-align: middle;" type="checkbox" id="print" checked="checked"></input> -->
				   <a id="chargeBtn" href="javascript:void(0)" class="easyui-linkbutton" onclick="charge()">缴费</a>
				   <a id="closeBtn" href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#chargeDialog').dialog('close')">取消</a>
			    </div>
		  </div>
		  <div region="center" border="false" style="border:1px solid #ccc;">
		      <div class="easyui-panel"  title="缴费信息">
			  	<table>
				   <tr>
				    <td><span style="margin-left: 20px;">收费项目:</span></td>
					<td><input id="paymentDateTitle" readonly="readonly" style="width:135px;"></input></td>
					<td><span style="margin-left: 20px;">缴费状态:</span></td>
					<td><input id="chargeState" readonly="readonly" style="width:135px;"></input></td>
				   </tr>
				   <tr>
				    <td><span style="margin-left: 10px;">单价:</span></td>
					<td><input id="unit" readonly="readonly" style="width:135px;"></input></td>
					<td><span style="margin-left: 10px;">是否被取消:</span></td>
					<td><input id="cancelled" readonly="readonly" style="width:135px;"></input></td>
				   </tr>
				   <tr>
				    <td><span style="margin-left: 20px;">取消时间:</span></td>
					<td><input id="cancelledTime" readonly="readonly" style="width:135px;"></input></td>
					<td><span style="margin-left: 20px;">取消人:</span></td>
					<td><input id="cancelledUserName" readonly="readonly" style="width:135px;"></input></td>
				   </tr>
				   <tr>
				    <td><span style="margin-left: 20px;">取消原因:</span></td>
					<td colspan="3"><input id="cancelledCause" readonly="readonly" style="width:350px;"></input></td>
				   </tr>
				</table>
			  </div>
			  <div class="easyui-panel"  title="房屋基本信息">
			  	<table>
				   <tr>
				    <td><span style="margin-left: 20px;">房屋地址:</span></td>
					<td colspan="3"><input id="houseAddress" readonly="readonly" style="width:350px;"></input></td>
				   </tr>
				   <tr>
				    <td><span style="margin-left: 20px;">用房性质:</span></td>
					<td><input id="yongfangXingzhi" readonly="readonly" style="width:135px;"></input></td>
				    <td><span style="margin-left: 20px;">维修面积:</span></td>
					<td><input id="repairArea" readonly="readonly" style="width:135px;"></input></td>
				   </tr>
				   <tr>
					<td><span style="margin-left: 20px;">户主姓名:</span></td>
					<td><input id="ownerName" readonly="readonly" style="width:135px;"></input></td>
					<td><span>户主身份性质:</span></td>
					<td><input id="shenfenXingzhi" readonly="readonly" style="width:135px;"></input></td>
				   </tr>
				   <tr>
					<td><span>户主身份证号:</span></td>
					<td><input id="owerIdCardNo" readonly="readonly" style="width:135px;"></input></td>
					<td><span style="margin-left: 20px;">户主电话:</span></td>
					<td><input id="ownerPhone" readonly="readonly" style="width:135px;"></input></td>
				   </tr>
				</table>
			  </div>
		  </div>
		</div>
	</div>
	<div id="cancelDialog" class="easyui-dialog" title="取消结算" style="width:350px;height: 150px;" data-options="closed:true,modal:true">
	<div class="easyui-layout" fit="true" >
	    <div style="margin-top: 10px;">
	    <form id="cancelChargeForm" method="post">
  	      <input type="hidden" id="cancelRecordId" name="recordId" />
  	      <input type="hidden" name="_method" value="put" />
		  <table>
		   <tr>
		    <td class="jiaofeiLabel" style="width: 60px;"><span style="text-align: center;vertical-align: middle;margin-left: 10px;white-space: nowrap;">取消原因:</span></td>
			<td class="jiaofeiInput"><textarea id="cancelledRemarks" name="remarks" 
				style="font-size: small;width: 250px;"></textarea></td>
		   </tr>
		  </table>
		</form>
		</div>
	    <div style="text-align:center;white-space: nowrap;margin-top: 5px;">
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="cancel();">确定</a>
	    </div>
	</div>
	</div>
	</div>
	<c:set var="scripts" scope="request">
	   <%@ include file="/scripts/HeatingMaintainCharge.js"%>
	</c:set>
</body>
