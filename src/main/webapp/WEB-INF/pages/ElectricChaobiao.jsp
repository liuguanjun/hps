<%@page pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<head>
	<meta charset="UTF-8">
	<title>抄表</title>
	<meta name="menu" content="ElectricChaobiao"/>
</head>
<body>
	<div class="easyui-panel" title="查询条件" collapsible="true" style="height:110px;">
	  <div class="easyui-layout" fit="true">
			<div region="east" border="false"  split="true"  style="width: 150px;">
				<div style="padding-left: 10px;padding-top: 20px;;">
					<a href="#" class="easyui-linkbutton"  iconCls="icon-search"  onclick="$('#data_grid').datagrid('reload')"
					   style="margin-bottom: 5px;">查询</a>
					<a href="#" class="easyui-linkbutton" iconCls="icon-reload" onclick="clearQuery()">重置</a>
				</div>
			</div>
			<div region="center" border="false" style="border:1px solid #ccc;">
				 <table id="dianjiaTable">
		    		<tr>
		    		    <td><span style="margin-left: 10px;">基地:</span></td>
		    			<td>
		    			   <input id="base_q" name="baseCode" class="easyui-combobox" type="base" hasEmpty="false"
		    			   	  style="width:155px;" panelHeight="120px">
	                        </input>
		    			</td>
		    			<td><span style="margin-left: 10px;">收取月份：</span></td>
		    			<td>
		    			   <input id="electricmonth_q" class="easyui-combobox" type="chargemonth" hasEmpty="false"
		    			   	  style="width:155px;" panelHeight="200px">
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
		    			<td><span style="margin-left: 10px;">单元:</span></td>
		    			<td>
		    			   <input id="danyuan_q" name="danyuan" style="width:155px;"></input>
		    			</td>
			    		<td><span style="margin-left: 10px;">楼层:</span></td>
		    			<td>
		    			   <input id="louceng_q" name="ceng" style="width:155px;"></input>
		    			</td>
		    			<td><span style="margin-left: 10px;">房号：</td>
		    			<td>
		    			   <input id="houseno_q" name="houseno" style="width:155px;"></input>
		    			</td>
		    			<td><span style="margin-left: 10px;">房主名称：</span></td>
		    			<td>
		    			   <input id="houseowner_q" name="houseownername" style="width:155px;"></input>
		    			</td>
		    		</tr>
		    		<tr>
		    			<td><span style="margin-left: 10px;">抄表状态：</span></td>
		    			<td>
		    			   <table>
		    			      <tr>
		    			      	 <td>
		    			             <input type="checkbox" name="noRecord" id="noRecord_q"></input>
		    			         </td>
		    			         <td>未抄表</td>
		    			         <td>
		    			             <input type="checkbox" name="recorded" id="recorded_q"></input>
		    			         </td>
		    			         <td>已抄表</td>
                              </tr>		    			     
		    			   </table>
		    			</td>
		    			<td><span style="margin-left: 10px;">缴费状态：</span></td>
		    			<td>
		    			   <table>
		    			      <tr>
		    			      	 <td>
		    			             <input type="checkbox" name="charged" id="charged_q"></input>
		    			         </td>
		    			         <td>已缴费</td>
		    			         <td>
		    			             <input type="checkbox" name="unCharged" id="unCharged_q"></input>
		    			         </td>
		    			         <td>未缴费</td>
                              </tr>		    			     
		    			   </table>
		    			</td>
		    		    <td><span style="margin-left: 10px;">抄表状态：</span></td>
		    			<td colspan="3">
		    			   <table>
		    			      <tr>
		    			      	 <td>
		    			             <input type="checkbox" name="newElectricUser" id="newElectricUser_q"></input>
		    			         </td>
		    			         <td>上月无抄表</td>
		    			         <td>
		    			             <input type="checkbox" name="oldElectricUser" id="oldElectricUser_q"></input>
		    			         </td>
		    			         <td>上月有抄表</td>
                              </tr>		    			     
		    			   </table>
		    			</td>
		    		</tr>
		    	   </table>
			</div>
	   </div>
	</div>
	<table id="data_grid" class="easyui-datagrid" title="抄表记录" 
			data-options="url:'${ctx}/admin/ElectricManagement/houseOfElectric',
			singleSelect:true,
			collapsible:true,
			method:'get',
			pageSize: 8,
            pageList: [8, 10, 20, 30],
			onBeforeLoad:setQueryParams,
			onClickRow: onClickRow,
			onAfterEdit: onAfterEdit,
			onLoadSuccess: onLoadSuccess,
			toolbar: [
				    {
					   iconCls: 'icon-edit',
					   text:'保存',
					   handler: function() { saveData(); }
				   }]"
			rownumbers="true" pagination="true">
		<thead>
			<tr>
				<th data-options="field:'chargeMonth',width:90,align:'center'">收取月份</th>
				<th data-options="field:'hourseNo',width:50,align:'center'">户号</th>
				<th data-options="field:'houseOwnerName',width:70,align:'center'">户名</th>
				<th data-options="field:'address',width:200,align:'center'">地址</th>
				<th data-options="field:'provReadoutsElectric',width:80,align:'center'
				<security:authorize ifAnyGranted='ROLE_SUPERADMIN,ROLE_ADMIN'> 
				,editor:{type:'numberbox'}
				</security:authorize>
				">上期表值</th>
				<th data-options="field:'readoutsElectric',width:80,align:'center',editor:{type:'numberbox'}">本期表值</th>
				<th data-options="field:'newReadoutsElectric',width:80,align:'center',editor:{type:'numberbox'}">表变更抄值</th>
				<th data-options="field:'electricCount',width:80,align:'center',formatter:formatElectricCount">用电量</th>
				<th data-options="field:'readMeterDate',width:100,align:'center'">抄表日期</th>
				<th data-options="field:'chargeDate',width:130,align:'center'">缴费日期</th>
				<th data-options="field:'inputCheckResult',width:80,align:'center',formatter:formatInputStatus">输入校验</th>
				<th data-options="field:'nosave',width:40,align:'center'"></th>
			</tr>
		</thead>
	</table>
	<div class="easyui-layout" fit="true" style="margin-top: 5px;">
	    <table id="tongjiResult">
    		<tr>
    		    <td><span style="margin-left: 10px;">应收电量:</span></td>
    			<td>
    			   <span style="margin-left: 10px;" id="yingshouElectricCount">0</span>度</td>
    			</td>
    			<td><span style="margin-left: 10px;">应收电费:</span></td>
    			<td>
    			   <span style="margin-left: 10px;" id="yingshouElectricCharge">0</span>元</td>
    			</td>
    		</tr>
    		</tr>
	    		<td><span style="margin-left: 10px;">已收电量:</span></td>
    			<td>
    			   <span style="margin-left: 10px;" id="yishouElectricCount">0</span>度</td>
    			</td>
	    		<td><span style="margin-left: 10px;">已收电费:</span></td>
    			<td>
    			   <span style="margin-left: 10px;" id="yishouElectricCharge">0</span>元</td>
    			</td>
    		</tr>
		</table>
	</div>
	<c:set var="scripts" scope="request">
	   <%@ include file="/scripts/ElectricChaobiao.js"%>
	</c:set>
</body>
