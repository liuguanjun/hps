<%@page pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<jsp:useBean id="now" class="java.util.Date" /> 
<head>
	<meta charset="UTF-8">
	<title>收电费</title>
	<meta name="menu" content="ElectricCharge"/>
</head>
<body>
	<div class="easyui-panel" title="查询条件" collapsible="true" style="height:110px;">
	  <div class="easyui-layout" fit="true">
			<div region="east" border="false"  split="true"  style="width: 150px;">
				<div style="padding-left: 10px;padding-top: 20px;;">
					<a href="#" class="easyui-linkbutton"  iconCls="icon-search" onclick="$('#data_grid').datagrid('reload')"
						style="margin-bottom: 5px;">查询</a>
					<a href="#" class="easyui-linkbutton" iconCls="icon-reload"
						onclick="clearHouseQuery()">重置</a>
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
			    		<td><span style="margin-left: 10px;">楼座:</span></td>
		    			<td>
		    			   <input id="louzuo_q" type="louzuo" baseComboboxId="base_q" 
		    			   		areaComboboxId="area_q" name="louzuoCode" class="easyui-combobox" 
		    			   		hasEmpty="true" style="width:155px;" panelHeight="200px">
	                        </input>
		    			</td>
		    			<td><span style="margin-left: 10px;">单元:</span></td>
		    			<td>
		    			   <input id="danyuan_q" name="danyuan" style="width:155px;"></input>
		    			</td>
	    			</tr>
		    		<tr>
		    			<td><span style="margin-left: 10px;">房号:</span></td>
		    			<td>
		    			   <input id="houseNo_q" name="houseNo" style="width:155px;">
	                        </input>
		    			</td>
		    		    <td><span style="margin-left: 10px;">户主号:</span></td>
		    			<td>
		    			   <input id="ownerNo_q" name="ownerNo" style="width:155px;"></input>
		    			</td>
		    		    <td><span style="margin-left: 10px;">户主名称:</span></td>
		    			<td>
		    			   <input id="ownerName_q" name="ownerName" style="width:155px;"></input>
		    			</td>
			    		<td><span style="margin-left: 10px;">楼层:</span></td>
		    			<td>
		    			   <input id="louceng_q" name="ceng" style="width:155px;"></input>
		    			</td>
	    			</tr>
	    			<tr>
			    		<td><span style="margin-left: 10px;">身份性质:</span></td>
		    			<td>
		    			   <input id="shenfenXingzhi_q" name="shenfenXingzhiCode" type="dictitem"
		    			   		 dictcode="SHENFEN_XINGZHI" style="width:155px;" panelHeight="200px"
		    			   		 hasEmpty="true"></input>
		    			</td>
			    		<td><span style="margin-left: 10px;">身份证号:</span></td>
		    			<td>
		    			   <input id="ownerIdCardNo_q" name="ownerIdCardNo" style="width:155px;"></input>
		    			</td>
			    		<td><span name="yongfangXingzhi" style="margin-left: 10px;">房屋性质:</span></td>
		    			<td>
		    			   <input id="yongfangXingzhi_q" name="yongfangXingzhiCode" style="width:155px;" type="dictitem" 
		    			   		dictcode="YONGFANG_XINGZHI" panelHeight="200px" hasEmpty="true"></input>
		    			</td>
		    			<td><span style="margin-left: 10px;">状态:</span></td>
		    			<td>
		    			   <input id="electricChargeState_q" name="electricChargeState" style="width:155px;" panelHeight="100px"></input>
		    			</td>
	    			</tr>
	    	   </table>
			</div>
	   </div>
	</div>
	<table id="data_grid" class="easyui-datagrid" title="查询结果" 
			data-options="url:'${ctx}/elecharge/chargestates',
			singleSelect:true,
			collapsible:true,
			method:'get',
			pageSize:10,
			onCheck : recordOnCheck,
			onBeforeLoad:setQueryParams,
			toolbar: [
			       <security:authorize ifAnyGranted='ROLE_SUPERADMIN,ROLE_ADMIN'> 
					    {
					       id : 'toolbarInitSurplusBtn',
						   iconCls: 'icon-edit',
						   text:'设置初始余额',
						   handler:function() { openInitSurplusDialog(); }
					    }, '-' ,
				       </security:authorize>
				    {
				       id : 'toolbarChargeBtn',
					   iconCls: 'icon-add',
					   text:'缴费',
					   handler:function() { openChargeDialog(); }
				    }, '-' ,
				    {
				       id : 'toolbarSearchChargeRecord',
					   iconCls: 'icon-search',
					   text:'查看缴费记录',
					   handler:function() { openChargeRecordsDialog(); }
				    }]"
			rownumbers="true" pagination="true">
		<thead>
			<tr>
				<th data-options="field:'ck',width:60,align:'center',checkbox:true">选择</th>
				<th data-options="field:'houseOwner.name',width:80,align:'center',formatter:getNestedValue">房主名</th>
				<th data-options="field:'house.shenfenXingzhi.name',width:80,align:'center',formatter:getNestedValue">身份性质</th>
				<th data-options="field:'house.no',width:70,align:'center',formatter:getNestedValue">房号</th>
				<th data-options="field:'house.yongfangXingzhi.name',width:80,align:'center',formatter:getNestedValue">用房性质</th>
				<th data-options="field:'house.louzuo.area.baseName',width:100,align:'center',formatter:getNestedValue">基地</th>
				<th data-options="field:'house.louzuo.area.name',width:70,align:'center',formatter:getNestedValue">区域</th>
				<th data-options="field:'house.louzuo.name',width:110,align:'center',formatter:getNestedValue">楼座</th>
				<th data-options="field:'house.danyuan',width:50,align:'center',formatter:getNestedValue">单元</th>
				<th data-options="field:'house.ceng',width:50,align:'center',formatter:getNestedValue">楼层</th>
				<th data-options="field:'house.doorNo',width:80,align:'center',formatter:getNestedValue">门牌号</th>
				<th data-options="field:'surplus',width:80,align:'center',formatter:formatSurplus">余额（元）</th>
				<th data-options="field:'electricChargeStateName',width:60,align:'center'">状态</th>
				<th data-options="field:'lastChargeDate',width:150,align:'center'">上次缴费时间</th>
			</tr>
		</thead>
	</table>
	<div id="chargeDialog" class="easyui-dialog" title="缴费" style="width:870px;height: 400px;" data-options="closed:true,modal:true">
	<div class="easyui-layout" fit="true" >
	   <div region="east" border="false" split="true"  style="width:220px;">
			 <div class="easyui-panel"  title="结算详细">
			 <form id="chargeForm" method="post">
			  <input id="houseId" type="hidden" name="houseId"/>
			  <input id="dialogType" type="hidden" />
              <table>
			    <tr>
	    			<td><span style="margin-left: 5px;">收费日期:</span></td>
	    			<td><input id="chargeDate" readonly="readonly" style="width:115px;"
	    			 	    value='<fmt:formatDate value="${now}" type="both" dateStyle="long" pattern="yyyy-MM-dd" />'>  
	    			    </input>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td><span style="margin-left: 5px;">电量合计 :</span></td>
	    			<td><input id="electricAmountSum" readonly="readonly" style="width:100px;text-align: right;"></input>度</td>
	    		</tr>
	    		<tr>
	    			<td><span style="margin-left: 5px;">电费合计:</span></td>
	    			<td><input id="electricCostSum" readonly="readonly" style="width:100px;text-align: right;"></input>元</td>
				</tr>
	    		<tr>
	    			<td><span style="margin-left: 5px;">滞纳金合计 :</span></td>
	    			<td><input id="zhinaSum" readonly="readonly" style="width:100px;text-align: right;"></input>元</td>
	    		</tr>
	    		<tr>
		    	<td><span style="margin-left: 5px;">收取滞纳 :</span></td>
	    			<td><input id="zhinajinOn" name="zhinajinOn" type="checkbox" onclick="zhinajinOnClick();"></input></td>
	    		</tr>
	    		<tr>
	    		    <td><span style="margin-left: 5px;">卫生费合计:</span></td>
	    			<td><input id="weishengSum" readonly="readonly" style="width:100px;text-align: right;"></input>元</td>
	    		</tr>
	    			<td><span style="margin-left: 5px;">排污费合计 :</span></td>
	    			<td><input id="paiwuSum" readonly="readonly" style="width:100px;text-align: right;"></input>元</td>
				</tr>
	    		<tr>
	    			<td><span style="margin-left: 5px;">照明费合计 :</span></td>
	    			<td><input id="zhaomingSum" readonly="readonly" style="width:100px;text-align: right;"></input>元</td>
				</tr>
	    		<tr>
	    			<td><span style="margin-left: 5px;">上次结余 :</span></td>
	    			<td><input id="previousSurplus" readonly="readonly"  style="width:100px;text-align: right;"></input>元</td>
	    		</tr>
	    		<tr>
	    			<td><span style="margin-left: 5px;">欠费 :</span></td>
	    			<td><input id="qianfei" readonly="readonly" style="width:100px;text-align: right;"></input>元</td>
	    		</tr>
	    		<tr>
	    			<td><span style="margin-left: 5px;">应收 :</span></td>
	    			<td><input id="mustCharge" readonly="readonly" style="width:100px;text-align: right;"></input>元</td>
	    		</tr>
	    		<tr>
	    		   <td><span style="margin-left: 5px;">实收:</span></td>
	    			<td colspan="7">
	    				<input id="chargeValue" class="easyui-numberbox" data-options="required:true,min:0,precision:2,max:1000000"
	    					 name="chargeValue" style="width:100px;text-align: right;" value="0"></input>元
					</td>
	    		</tr>
	    	  </table>
	    	  </form>
			</div>
			<div style="text-align:center;padding:5px;white-space: nowrap;white-space: nowrap;">
			   <a id="chargeBtn" href="javascript:void(0)" class="easyui-linkbutton" onclick="charge()">缴费</a>
			   <a id="closeBtn" href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#chargeDialog').dialog('close')">关闭</a>
		  </div>
	  </div>
	  <div region="center" border="false" style="border:1px solid #ccc;">
		   <table id="data_grid_chaobiao" class="easyui-datagrid" title="电费记录" 
				data-options="
				singleSelect:true,
				loadFilter: filterChaobiao,
				onLoadSuccess : chaobiaoLoadSuccess,
				onBeforeLoad:setChaobiaoQueryParams,
				collapsible:true,method:'get'"
				rownumbers="true" pagination="false" style="height: 180px;">
			<thead>
				<tr>
					<th data-options="field:'paymentDate.monthFormatStr',width:80,align:'center',formatter:getNestedValue">收取月份</th>
					<th data-options="field:'provReadoutsElectric',width:100,align:'center'">上期表值</th>
				    <th data-options="field:'readoutsElectric',width:100,align:'center'">本期表值</th>
				    <th data-options="field:'electricCount',width:60,align:'center'">用电量</th>
				    <th data-options="field:'electricCharge',width:60,align:'center'">电费</th>
				    <th data-options="field:'zhinajinDayCount',width:60,align:'center'">逾期天数</th>
				    <th data-options="field:'zhinajin',width:60,align:'center'">滞纳金</th>
				    <th data-options="field:'readMeterDate',width:120,align:'center'">抄表日期</th>
				    <th data-options="field:'weishengCharge',width:60,align:'center'">卫生费</th>
				    <th data-options="field:'paiwuCharge',width:60,align:'center'">排污费</th>
				    <th data-options="field:'zhaomingCharge',width:60,align:'center'">照明费</th>
				    <th data-options="field:'chargeState',width:60,align:'center'">状态</th>
				</tr>
			 </thead>
		  </table>
		  <div class="easyui-panel"  title="房屋基本信息">
		  	<table>
			   <tr>
			    <td><span style="margin-left: 20px;">房屋地址:</span></td>
				<td colspan="5"><input id="houseAddress" readonly="readonly" style="width:450px;"></input></td>
			   </tr>
			   <tr>
			    <td><span style="margin-left: 20px;">电费单价:</span></td>
				<td colspan="5"><input id="electricUnitDesc" readonly="readonly" style="width:450px;"></input></td>
			   </tr>
			   <tr>
			    <td><span style="margin-left: 20px;">房屋编号:</span></td>
				<td><input id="houseNo" readonly="readonly" style="width:135px;"></input></td>
				<td><span style="margin-left: 20px;">户主姓名:</span></td>
				<td><input id="ownerName" readonly="readonly" style="width:135px;"></input></td>
				<td><span style="margin-left: 20px;">身份性质:</span></td>
				<td><input id="shenfenXingzhi" readonly="readonly" style="width:135px;"></input></td>
			   </tr>
			   <tr>
			    <td><span style="margin-left: 20px;">房屋性质:</span></td>
				<td><input id="yongfangXingzhi" readonly="readonly" style="width:135px;"></input></td>
			    <td><span style="margin-left: 20px;">身份证号:</span></td>
				<td><input id="ownerIdCardNo" readonly="readonly" style="width:135px;"></input></td>
				<td><span style="margin-left: 20px;">电话:</span></td>
				<td><input id="ownerPhone" readonly="readonly" style="width:135px;"></input></td>
			   </tr>
			</table>
		  </div>
	  </div>
	</div>
	</div>
	<div id="chargeRecordDialog" class="easyui-dialog" title="缴费记录" style="width:870px;height: 400px;" data-options="closed:true,modal:true">
	<div class="easyui-layout" fit="true" >
	  <div region="center" border="false" style="border:1px solid #ccc;">
		   <table id="data_grid_chargerecord" class="easyui-datagrid" title="缴费记录" 
				data-options="
				singleSelect:true,
				collapsible:true,
				method:'get',
				pageSize:10,
				onCheck : chargeRecordOnCheck,
				toolbar: [
				        <security:authorize ifAnyGranted='ROLE_SUPERADMIN,ROLE_ADMIN'> 
					    {
					       id : 'toolbarCancelBtn',
						   iconCls: 'icon-cancel',
						   text:'取消结算',
						   handler:function() { openCancelDialog(); }
					    }, '-' ,
				       </security:authorize>
					    {
					       id : 'toolbarDetailBtn',
						   iconCls: 'icon-edit',
						   text:'详细',
						   handler:function() { openDetailDialog(); }
					    }
					    ]""
				rownumbers="true"
				pagination="true">
			<thead>
				<tr>
				    <th data-options="field:'ck',width:60,align:'center',checkbox:true">选择</th>
					<th data-options="field:'chargeDate',width:150,align:'center'">缴费时间</th>
					<th data-options="field:'provReadoutsElectric',width:80,align:'center',formatter:getProvReadoutsElectricValue">上期表值</th>
					<th data-options="field:'readoutsElectric',width:80,align:'center',formatter:getReadoutsElectricValue">本期表值</th>
					<th data-options="field:'electricCount',width:60,align:'center',formatter:getElectricCountValue">用电量</th>
				    <th data-options="field:'mustCharge',width:80,align:'center'">应缴金额</th>
					<th data-options="field:'actualCharge',width:80,align:'center'">实缴金额</th>
					<th data-options="field:'currentSurplus',width:80,align:'center'">余额</th>
				    <th data-options="field:'houseOwner.name',width:100,align:'center',formatter:getNestedValue">房主</th>
				    <th data-options="field:'cancelled',width:60,align:'center',formatter:getBooleanValue">已取消</th>
				    <th data-options="field:'cancelledCause',width:200,align:'center'">取消原因</th>
				    <th data-options="field:'chargeUser.userName',width:60,align:'center',formatter:getNestedValue">收费员</th>
				</tr>
			 </thead>
		  </table>
	  </div>
	</div>
	</div>
	<div id="cancelDialog" class="easyui-dialog" title="取消结算" style="width:350px;height: 130px;" data-options="closed:true,modal:true">
	<div class="easyui-layout" fit="true" >
	    <div style="margin-top: 10px;">
	    <form id="cancelChargeForm" method="post">
        <input type="hidden" id="recordId" name="recordId" />
        <input type="hidden" name="_method" value="put" />
		<table>
		   <tr>
		    <td style="width: 50px;"><span style="text-align: center;vertical-align: middle;">原因:</span></td>
			<td><textarea id="cause" name="cause" style="font-size: small;width: 250px;"></textarea></td>
		   </tr>
		</table>
		</form>
		</div>
	    <div style="text-align:center;white-space: nowrap;white-space: nowrap;">
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="cancelRecordCharge();">取消结算</a>
	    </div>
	</div>
	</div>
	<div id="setInitSurplusDialog" class="easyui-dialog" title="设置初始余额" style="width:300px;height: 130px;" data-options="closed:true,modal:true">
	<div class="easyui-layout" fit="true" >
	    <div style="margin-top: 10px;">
	    <form id="initSurplusForm" method="post">
			<input type="hidden" id="initHouseId" name="initHouseId" /> 
			<input type="hidden" name="_method" value="put" />
		<table>
		   <tr>
		    <td style="width: 110px;" align="right"><span style="text-align: right;vertical-align: middle;">初始余额:</span></td>
			<td>
			  <input id="initSurplus" class="easyui-numberbox" data-options="required:true,min:-1000000,precision:3,max:1000000"
	    					name="initSurplus" style="width:100px;text-align: right;" value="0"></input>元
			</td>
		   </tr>
		   <tr>
		    <td style="width: 110px;" align="right"><span style="text-align: right;vertical-align: middle;">初始表值:</span></td>
			<td>
			  <input id="initElectricReadout" class="easyui-numberbox" data-options="required:true,min:0,precision:0,max:1000000"
	    					name="initElectricReadout" style="width:100px;text-align: right;" value="0"></input>度
			</td>
		   </tr>
		</table>
		</form>
		</div>
	    <div style="text-align:center;white-space: nowrap;white-space: nowrap;margin-top: 5px;">
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="initHouseSurplus();">确定</a>
	    </div>
	</div>
	</div>
	<c:set var="scripts" scope="request">
	   <%@ include file="/scripts/ElectricCharge.js"%>
	</c:set>
</body>