<%@page pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<head>
	<meta charset="UTF-8">
	<title>电费报表</title>
	<meta name="menu" content="ElectricTongji"/>
</head>
<body>
    <div class="easyui-layout" style="height: 500px;">
    <div data-options="region:'west',split:true,title:'区域导航'" style="width:170px;">
       <ul id="baseTree" class="easyui-tree" 
           data-options="url:'${ctx}/admin/BaseManagement/baseTreeElements',
			method:'get',
			formatter:formatTreeNode,
			onLoadSuccess : selectFirstNode,
			onClick: treeNodeOnClick
			"></ul>
    </div>
    <div id="mainPanle" region="center">
    <div class="easyui-panel" title="查询条件" collapsible="true" style="height:70px;">
	  <div class="easyui-layout" fit="true">
  		<div region="east" border="false"  split="true"  style="width:200px;">
			<div style="padding-left: 10px;padding-top: 10px;">
				<a href="#" class="easyui-linkbutton"  iconCls="icon-search" onclick="searchTongjiInfo()"
					style="margin-bottom: 5px;">查询</a>
				<a href="#" class="easyui-linkbutton" iconCls="icon-reload"
					onclick="clearHouseQuery()">重置</a>
			</div>
		</div>
		<div region="center" border="false" style="border:1px solid #ccc;padding-top: 5px;">
		   <table>
	    		<tr>
	    		    <td><span style="margin-left: 10px;">操作员:</span></td>
	    			<td>
	    			   <input id="hpsUser_q" name="user.username" class="easyui-combobox" 
	    			   		style="width:100px;" panelHeight="200px">
                       </input>
	    			</td>
	    			<td><div style="margin-left: 10px;text-align:right">缴费时间:</div></td>
	    			<td>
	    			   <input id="startTime_q" style="width: 155px;" class="easyui-datetimebox" type="text" name="payStartDate"></input>
	    			   ~
	    			   <input id="endTime_q"  style="width: 155px;" class="easyui-datetimebox" type="text" name="payEndDate"></input>
    				</td>
    			</tr>
    	   </table>
		</div>
	   </div>
	</div>
	<div  class="easyui-layout" style="padding-top:5px">
	<table id="data_grid" class="easyui-datagrid"  title="电费统计结果" 
			data-options="url:'${ctx}/elecharge/usertongjiresult',
			singleSelect:true,
			collapsible:true,
			method:'get',
			showFooter: true,
			onCheck : userTongjiRowOnCheck,
			onBeforeLoad:setQueryParams,
			onLoadSuccess:function() { if($('#data_grid').datagrid('getData').rows.length > 0) {
			                             $('#toolbarExport').linkbutton('enable');
			                           } 
			                         },
			toolbar: [
				    {
				       id : 'toolbarSearchChargeRecord',
					   iconCls: 'icon-search',
					   text:'查看缴费记录',
					   handler:function() { openChargeRecordsDialog(); },
					   
				    }, '-' ,
				    {
				       id : 'toolbarExport',
					   iconCls: 'icon-print',
					   text:'导出缴费记录',
					   handler:function() { exportElectricReport(); }
			       }]"
			rownumbers="true">
		<thead>
			<tr>
				<th data-options="field:'operName',width:110,align:'center'">操作员</th>
				<th data-options="field:'electricCharge',width:100,align:'center'">电费</th>
				<th data-options="field:'electricCount',width:100,align:'center'">电量</th>
				<th data-options="field:'zhinajin',width:100,align:'center'">滞纳金</th>
				<th data-options="field:'weishengfei',width:100,align:'center'">卫生费</th>
				<th data-options="field:'paiwufei',width:100,align:'center'">排污费</th>
				<th data-options="field:'zhaomingfei',width:100,align:'center'">照明费</th>
				<th data-options="field:'actualCharge',width:100,align:'center'">实际收取合计</th>
				<th data-options="field:'juminMonthCount',width:100,align:'center'">居民累计月数</th>
				<th data-options="field:'receiptCnt',width:100,align:'center'">理论收据张数</th>
			</tr>
		</thead>
	</table>
	</div>
	<div id="comment"  class="easyui-layout"  style="margin-top: 10px;margin-left: 10px;">
	   <span style="color: red;font-weight: bold;">
	      注：由于某些情况需要手开收据，所以理论收据张数可能与实际张数存在出入。
	   </span>
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
				onBeforeLoad:setChargeRecordsQueryParams,
				onCheck : chargeRecordOnCheck,
				toolbar: [
					    {
					       id : 'toolbarDetailBtn',
						   iconCls: 'icon-edit',
						   text:'详细',
						   handler:function() { openDetailDialog(); }
					    }
					    ]"
				rownumbers="true"
				pagination="true">
			<thead>
				<tr>
				    <th data-options="field:'ck',width:60,align:'center',checkbox:true">选择</th>
					<th data-options="field:'chargeDate',width:150,align:'center'">缴费时间</th>
					<th data-options="field:'provReadoutsElectric',width:80,align:'center',formatter:getProvReadoutsElectricValue"">上期表值</th>
					<th data-options="field:'readoutsElectric',width:80,align:'center',formatter:getReadoutsElectricValue"">本期表值</th>
					<th data-options="field:'electricCount',width:60,align:'center',formatter:getElectricCountValue"">用电量</th>
				    <th data-options="field:'mustCharge',width:80,align:'center'">应缴金额</th>
					<th data-options="field:'actualCharge',width:80,align:'center'">实缴金额</th>
					<th data-options="field:'currentSurplus',width:80,align:'center'">余额</th>
				    <th data-options="field:'houseOwner.name',width:100,align:'center',formatter:getNestedValue">房主</th>
				    <th data-options="field:'house.shortAddress',width:250,align:'center',formatter:getNestedValue">地址</th>
				    <th data-options="field:'chargeUser.userName',width:90,align:'center',formatter:getNestedValue">收费员</th>
				</tr>
			 </thead>
		  </table>
	  </div>
	</div>
	</div>
	<div id="chargeDialog" class="easyui-dialog" title="缴费详细" style="width:870px;height: 380px;" data-options="closed:true,modal:true">
	<div class="easyui-layout" fit="true" >
	   <div region="east" border="false" split="true"  style="width:220px;">
			 <div class="easyui-panel"  title="结算详细">
              <table>
			    <tr>
	    			<td><span style="margin-left: 5px;">收费日期:</span></td>
	    			<td><input id="chargeDate" readonly="readonly" style="width:115px;">  
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
			</div>
			<div style="text-align:center;padding:5px;white-space: nowrap;white-space: nowrap;">
			   <a id="closeBtn" href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#chargeDialog').dialog('close')">关闭</a>
		  </div>
	  </div>
	  <div region="center" border="false" style="border:1px solid #ccc;">
		   <table id="data_grid_chaobiao" class="easyui-datagrid" title="电费记录" 
				data-options="
				singleSelect:true,
				loadFilter: filterChaobiao,
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
				    <th data-options="field:'weishengCharge',width:60,align:'center'">卫生费</th>
				    <th data-options="field:'paiwuCharge',width:60,align:'center'">排污费</th>
				    <th data-options="field:'zhaomingCharge',width:60,align:'center'">照明费</th>
				    <th data-options="field:'chargeState',width:60,align:'center'">状态</th>
				    <th data-options="field:'readMeterDate',width:120,align:'center'">抄表日期</th>
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
	<c:set var="scripts" scope="request">
	   <%@ include file="/scripts/ElectricTongji.js"%>
	</c:set>
	</div>
	</div>
</body>
