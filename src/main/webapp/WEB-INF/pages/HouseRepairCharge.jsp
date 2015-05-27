<%@page pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<jsp:useBean id="now" class="java.util.Date" /> 
<head>
	<meta charset="UTF-8">
	<title>收房屋维修费</title>
	<meta name="menu" content="HouseRepairCharge"/>
</head>
<body>
	<div class="easyui-panel" title="查询条件" collapsible="true" style="height:130px;">
	  <div class="easyui-layout" fit="true">
			<div region="east" border="false"  split="true"  style="width: 200px;">
				<div style="padding-left: 10px;padding-top: 20px;;">
					<a href="#" class="easyui-linkbutton"  iconCls="icon-search" style="margin-bottom: 5px;" onclick="$('#data_grid').datagrid('reload')">查询</a>
					<a href="#" class="easyui-linkbutton" iconCls="icon-reload" onclick="clearHouseRepairQuery()">重置</a>
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
		    			<td><span style="margin-left: 10px;">收取年度:</span></td>
		    			<td>
		    			   <input id="paymentDateId_q" name="paymentDateId" baseComboboxId="base_q" class="easyui-combobox" 
		    			   		hasEmpty="true" type="maintainPaymentDate" style="width:155px;" panelHeight="150px">
	                        </input>
		    			</td>
			    		<td><span style="margin-left: 10px;">区域:</span></td>
		    			<td>
		    			   <input id="area_q" name="areaCode" class="easyui-combobox" type="area" baseComboboxId="base_q" hasEmpty="true"
		    			   	  style="width:155px;" panelHeight="200px">
	                        </input>
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
		    		    <td><span style="margin-left: 10px;">房号:</span></td>
		    			<td>
		    			   <input id="houseNo_q" name="houseNo" style="width:155px;"></input>
		    			</td>
		    			<td><span style="margin-left: 10px;">单元:</span></td>
		    			<td>
		    			   <input id="danyuan_q" name="danyuan" style="width:155px;"></input>
		    			</td>
		    			<td><span style="margin-left: 10px;">楼层:</span></td>
		    			<td>
		    			   <input id="louceng_q" name="ceng" style="width:155px;"></input>
		    			</td>
		    		    <td><span style="margin-left: 10px;">户主名称:</span></td>
		    			<td>
		    			   <input id="ownerName_q" name="ownerName" style="width:155px;"></input>
		    			</td>
	    			</tr>
	    			<tr>
			    		<td><span style="margin-left: 10px;">身份性质:</span></td>
		    			<td>
		    			   <input id="shenfenXingzhi_q" name="shenfenXingzhiCode" type="dictitem"
		    			   		 dictcode="SHENFEN_XINGZHI" style="width:155px;" panelHeight="200px"
		    			   		 hasEmpty="true"></input>
		    			</td>
			    		<td><span name="yongfangXingzhi" style="margin-left: 10px;">房屋性质:</span></td>
		    			<td>
		    			   <input id="yongfangXingzhi_q" name="yongfangXingzhiCode" style="width:155px;" type="dictitem" 
		    			   		dictcode="YONGFANG_XINGZHI" panelHeight="200px" hasEmpty="true"></input>
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
	    			<tr>
		    			<td><span style="margin-left: 10px;">收费员:</span></td>
		    			<td>
		    			   <input id="operName_q" name="operName" style="width:155px;"></input>
		    			</td>
		    			<td><span style="margin-left: 10px;">收费日期:</span></td>
		    			<td>
		    			   <input id="chargeDate_q" class="easyui-datebox" name="chargeDate" style="width:155px;"></input>
		    			</td>
		    			<td><span style="margin-left: 10px;">工资号:</span></td>
		    			<td>
		    			   <input id="wageNum_q" name="wageNum" style="width:155px;"></input>
		    			</td>
			    		<td><span style="margin-left: 10px;">其他条件:</span></td>
		    			<td style="font-size: 12px;">
		    			     <input style="vertical-align: middle;" id="diverted_q" name="diverted" type="checkbox"></input><span style="vertical-align: middle;">&nbsp;结转&nbsp;&nbsp;&nbsp;&nbsp;</span>
		    			     <input style="vertical-align: middle;" id="gratis_q" name="gratis" type="checkbox"></input><span style="vertical-align: middle;">&nbsp;免缴&nbsp;&nbsp;&nbsp;&nbsp;</span>
		    			</td>
	    			</tr>
	    	   </table>
			</div>
	   </div>
	</div>
	<table id="data_grid" class="easyui-datagrid" title="查询结果" 
			data-options="url:'${ctx}/maintaincharge/chargerecords',
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
				   }]"
			rownumbers="true" pagination="true">
		<thead>
			<tr>
				<th data-options="field:'ck',align:'center',checkbox:true"></th>
				<th data-options="field:'paymentDate.title',width:190,align:'center',formatter:getNestedValue">收取年度</th>
				<th data-options="field:'houseOwner.name',width:80,align:'center',formatter:getNestedValue">户主姓名</th>
				<th data-options="field:'house.no',width:80,align:'center',formatter:getNestedValue">房号</th>
				<th data-options="field:'house.address',width:250,align:'center',formatter:getNestedValue">房屋地址</th>
				<th data-options="field:'chargeState',width:60,align:'center'">缴费状态</th>
				<th data-options="field:'house.warmArea',width:80,align:'center',formatter:getNestedValue">供热面积</th>
				<th data-options="field:'unit.desc',width:170,align:'center',formatter:getNestedValue">单价</th>
				<th data-options="field:'chargeDate',width:150,align:'center'">缴费日期</th>
				<th data-options="field:'diverted',width:60,align:'center',formatter:getBooleanValue">结转</th>
				<th data-options="field:'gratis',width:60,align:'center',formatter:getBooleanValue">免缴</th>
				<th data-options="field:'divertedCharge',width:80,align:'center'">历年结转</th>
				<th data-options="field:'monthCount',width:80,align:'center'">付费月数</th>
				<th data-options="field:'mustCharge',width:80,align:'center'">应收金额</th>
				<th data-options="field:'actualCharge',width:80,align:'center'">实收金额</th>
				<th data-options="field:'operUser.userName',width:80,align:'center',formatter:getNestedValue">收费员</th>
				<th data-options="field:'wageNum',width:100,align:'center'">工资号</th>
			</tr>
		</thead>
	</table>
	<div class="easyui-layout" fit="true" style="margin-top: 5px;">
	    <table id="tongjiResult">
    		<tr>
    		    <td><span style="margin-left: 10px;">应收维修费:</span></td>
    			<td>
    			   <span style="margin-left: 10px;" id="yingshouMaintainCharge">0</span>元</td>
    			</td>
    			<td><span style="margin-left: 10px;">已收维修费:</span></td>
    			<td>
    			   <span style="margin-left: 10px;" id="yishouMaintainCharge">0</span>元</td>
    			</td>
    		</tr>
		</table>
	</div>
	<div id="chargeDialog" class="easyui-dialog" title="缴费" style="width:760px;height: 370px;" data-options="closed:true,modal:true">
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
		    			<td><span style="margin-left: 5px;">付费月数 :</span></td>
		    			<td>
		    			   <input id="monthCount" name="monthCount" style="width:100px;" class="easyui-numberbox" 
		    			   		data-options="required:true,min:1,precision:0,max:12" value="12">  
		    			    </input>
		    			</td>
		    		</tr>
		    		<tr>
		    			<td><span style="margin-left: 5px;">免缴 :</span></td>
		    			<td>
		    			   <input style="vertical-align: middle;" id="gratis" name="gratis" type="checkbox" onclick="gratisOnClick();"></input>
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
				    <td><span style="margin-left: 20px;">收取年度:</span></td>
					<td><input id="paymentDateTitle" readonly="readonly" style="width:135px;"></input></td>
					<td><span style="margin-left: 20px;">缴费状态:</span></td>
					<td><input id="chargeState" readonly="readonly" style="width:135px;"></input></td>
				   </tr>
				   <tr>
				    <td><span style="margin-left: 10px;">单价:</span></td>
					<td><input id="unit" readonly="readonly" style="width:135px;"></input></td>
					<td><span style="margin-left: 20px;">是否结转:</span></td>
					<td><input id="diverted" readonly="readonly" style="width:135px;"></input></td>
				   </tr>
				   <tr>
				    <td><span style="margin-left: 20px;">历年结转:</span></td>
					<td><input id="divertedCharge" readonly="readonly" style="width:135px;"></input></td>
				    <td><span style="margin-left: 10px;">是否免缴:</span></td>
					<td><input id="gratis" readonly="readonly" style="width:135px;"></input></td>
				   </tr>
				   <tr>
				    <td><span style="margin-left: 20px;">结转详细:</span></td>
					<td colspan="3"><input id="divertedMsg" readonly="readonly" style="width:350px;"></input></td>
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
				    <td><span style="margin-left: 20px;">房屋性质:</span></td>
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
	<c:set var="scripts" scope="request">
	   <%@ include file="/scripts/HouseRepairCharge.js"%>
	</c:set>
</body>
