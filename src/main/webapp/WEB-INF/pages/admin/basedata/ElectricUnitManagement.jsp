<%@page pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<head>
	<meta charset="UTF-8">
	<title>电费单价管理</title>
	<meta name="menu" content="AdminMenu"/>
</head>
<body>
    <div class="col-sm-10">
    <div class="easyui-panel" title="查询条件" collapsible="true" style="height:100px;">
	  <div class="easyui-layout" fit="true">
			<div region="east" border="false"  split="true"  style="width: 140px;">
				<div style="padding-left: 10px;padding-top: 10px;;">
					<a href="#" class="easyui-linkbutton"  iconCls="icon-search" onclick="$('#data_grid').datagrid('reload')"
						style="margin-bottom: 5px;">查询</a>
					<a href="#" class="easyui-linkbutton" iconCls="icon-reload"
						onclick="clearQuery()">重置</a>
				</div>
			</div>
			<div region="center" border="false" style="border:1px solid #ccc;padding-top: 5px;">
			   <table>
		    		<tr>
			    		<td><span style="margin-left: 10px;">基地:</span></td>
		    			<td>
		    			   <input id="base_q" name="baseCode" class="easyui-combobox" type="base" hasEmpty="true"
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
		    			   <input id="danyuan_q" name="danyuan" style="width:100px;"></input>
		    			</td>
	    			</tr>
		    		<tr>
		    		    <td><span style="margin-left: 10px;">楼层:</span></td>
		    			<td>
		    			   <input id="louceng_q" name="ceng" style="width:155px;"></input>
		    			</td>
		    		    <td><span style="margin-left: 10px;">门牌号:</span></td>
		    			<td>
		    			   <input id="doorNo_q" name="doorNo" style="width:155px;"></input>
		    			</td>
		    			<td><span name="yongfangXingzhi" style="margin-left: 10px;">房屋性质:</span></td>
		    			<td colspan="3">
		    			   <input id="yongfangXingzhi_q" name="yongfangXingzhiCode" style="width:155px;" type="dictitem" 
		    			   		dictcode="YONGFANG_XINGZHI" panelHeight="200px" hasEmpty="true"></input>
		    			</td>
	    			</tr>
	    	   </table>
			</div>
	   </div>
	</div>
	<table id="data_grid" class="easyui-datagrid" title="查询结果" 
			data-options="url:'${ctx}/admin/electricUnit/units',
			singleSelect:true,
			collapsible:true,
			method:'get',
			pageSize:10,
			onBeforeLoad:setQueryParams,
			onCheck : unitRowOnCheck,
			toolbar: [
				    {
					   iconCls: 'icon-add',
					   text:'添加单一电价',
					   handler:function() { openNewNormalDialog(); }
				    }, '-' ,
				    {
					   iconCls: 'icon-add',
					   text:'添加阶梯电价',
					   handler:function() { openNewLevelDialog(); }
				    }, '-' ,
				    {
				       id : 'toolbarEditBtn',
					   iconCls: 'icon-edit',
					   text:'修改',
					   handler: function() { openUpdateDialog(); }
				   }, '-' ,
				    {
				       id : 'toobarDelBtn',
					   iconCls: 'icon-cancel',
					   text:'删除',
					   handler: function() { deleteUnit(); }
				   }]"
			rownumbers="true" pagination="true">
		<thead>
			<tr>
			    <th data-options="field:'ck',width:60,align:'center',checkbox:true" rowspan="2">选择</th>
			    <th data-options="field:'base.name',width:100,align:'center',formatter:getNestedValue" rowspan="2">基地</th>
			    <th data-options="field:'area.name',width:100,align:'center',formatter:getNestedValue" rowspan="2">区域</th>
			    <th data-options="field:'louzuo.name',width:60,align:'center',formatter:getNestedValue" rowspan="2">楼座</th>
			    <th data-options="field:'danyuan',width:60,align:'center'" rowspan="2">单元</th>
			    <th data-options="field:'ceng',width:60,align:'center'" rowspan="2">层</th>
			    <th data-options="field:'doorNo',width:110,align:'center'" rowspan="2">门牌号</th>
				<th data-options="field:'yongfangXingzhi.name',width:90,align:'center',formatter:getNestedValue" rowspan="2">房屋性质</th>
				<th data-options="field:'level',width:60,align:'center',formatter:getBooleanValue" rowspan="2">阶梯电价</th>
				<th data-options="field:'unit',width:90,align:'center'" rowspan="2">单一电价</th>
				<th data-options="align:'center'" colspan="2">阶梯1电价</th>
				<th data-options="align:'center'" colspan="2">阶梯2电价</th>
				<th data-options="align:'center'" colspan="2">阶梯3电价</th>
				<th data-options="field:'zhinaScale',width:90,align:'center',formatter:getZhinaDispValue" rowspan="2">滞纳金比例</th>
				<th data-options="field:'weishengfei',width:90,align:'center'" rowspan="2">卫生费</th>
				<th data-options="field:'paiwufei',width:90,align:'center'" rowspan="2">排污费</th>
				<th data-options="field:'zhaomingfei',width:90,align:'center'" rowspan="2">照明费</th>
			</tr>
			<tr>
			     <th data-options="field:'start1',width:80,align:'center',formatter:getLevelRangeValue1" >开始 - 结束</th>
			     <th data-options="field:'unit1',width:80,align:'center'" >单价</th>
			     <th data-options="field:'start2',width:80,align:'center',formatter:getLevelRangeValue2" >开始 - 结束</th>
			     <th data-options="field:'unit2',width:80,align:'center'" >单价</th>
			     <th data-options="field:'start3',width:80,align:'center',formatter:getLevelRangeValue3" >开始 - 结束</th>
			     <th data-options="field:'unit3',width:80,align:'center'" >单价</th>
			</tr>
		</thead>
	</table>
	<div id="unitDialog" class="easyui-dialog" title="新建电费单价" style="width:400px" data-options="closed:true,modal:true">
		<div style="padding:10px 0 10px 60px">
	    <form id="unitForm" method="post">
	        <input id="id" type="hidden" name="id"/>
	        <input id="method" type="hidden" name="_method" value="post"/>
	        <input id="level" type="hidden" name="level"/>
	    	<table>
	    		<tr>
	    			<td>基地:</td>
	    			<td>
	    				<input id="baseCode" name="base.code" class="easyui-combobox" style="width:155px;"
	    					type="base" panelHeight="120px">   
						</input>
					</td>
	    		</tr>
	    		<tr>
	    			<td>区域:</td>
	    			<td>
	    				<input id="areaCode" name="area.code" class="easyui-combobox" type="area"
	    					 baseComboboxId="baseCode" hasEmpty="true" style="width:155px;">   
						</input>
					</td>
	    		</tr>
	    		<tr>
	    			<td>楼座:</td>
	    			<td>
	    				<input id="louzuoCode" type="louzuo" name="louzuo.code"
	    					areaComboboxId="areaCode" hasEmpty="true" style="width:155px;" panelHeight="200px">   
						</input>
					</td>
	    		</tr>
	    		<tr>
	    			<td>单元:</td>
	    			<td><input class="easyui-numberbox" data-options="required:false,min:1,precision:0" id="danyuan" name="danyuan" style="width:150px;"/></td>
	    		</tr>
	    		<tr>
	    			<td>楼层:</td>
	    			<td><input class="easyui-numberbox" data-options="required:false,min:1,precision:0" id="ceng" name="ceng" style="width:150px;"/></td>
	    		</tr>
	    		<tr>
	    			<td>门牌号:</td>
	    			<td><input id="doorNo" class="easyui-validatebox" data-options="required:false"
	    				 name="doorNo" style="width:150px;"/></td>
	    		</tr>
	    		<tr>
	    			<td>房屋性质:</td>
	    			<td>
	    				<input id="yongfangXingzhi" class="easyui-combobox" name="yongfangXingzhi.code" 
	    					style="width:155px;"type="dictitem" 
		    			   	dictcode="YONGFANG_XINGZHI" panelHeight="200px">      
						</input>
					</td>
	    		</tr>
	    		<tr id="leveUnitRow1">
	    			<td>阶梯1:</td>
	    			<td><input id="start1" name="start1" class="easyui-numberbox" data-options="required:true,min:0,precision:0" value="0" type="text" style="width: 35px;text-align: right;"></input>
	    			~
	    			<input id="end1" name="end1" class="easyui-numberbox" data-options="required:false,min:0,precision:0" type="text" style="width: 35px;text-align: right;"></input>
	    			<input id="unit1" name="unit1" class="easyui-numberbox" 
	    				data-options="required:true,min:0,precision:3" type="text" style="width: 45px;text-align: right;"></input>
	    			元/度
	    			</td>
	    		</tr>
	    		<tr id="leveUnitRow2">
	    			<td>阶梯2:</td>
	    			<td><input id="start2" name="start2" class="easyui-numberbox" data-options="required:false,min:0,precision:0" value="0" type="text" style="width: 35px;text-align: right;"></input>
	    			~
	    			<input id="end2" name="end2" class="easyui-numberbox" data-options="required:false,min:0,precision:0" type="text" style="width: 35px;text-align: right;"></input>
	    			<input id="unit2" name="unit2" class="easyui-numberbox" 
	    				data-options="required:false,min:0,precision:3" type="text" style="width: 45px;text-align: right;"></input>
	    			元/度
	    			</td>
	    		</tr>
	    		<tr id="leveUnitRow3">
	    			<td>阶梯3:</td>
	    			<td><input id="start3" name="start3" class="easyui-numberbox" data-options="required:false,min:0,precision:0" value="0" type="text" style="width: 35px;text-align: right;"></input>
	    			~
	    			<input id="end3" name="end3" data-options="required:false,min:0,precision:0" class="easyui-numberbox" type="text" style="width: 35px;text-align: right;"></input>
	    			<input id="unit3" name="unit3" class="easyui-numberbox" 
	    				data-options="required:false,min:0,precision:3" type="text" style="width: 45px;text-align: right;"></input>
	    			元/度
	    			</td>
	    		</tr>
	    		<tr id="unitRow">
	    			<td>单一电价:</td>
	    			<td><input id="unit" name="unit" class="easyui-numberbox" type="text" data-options="required:true,min:0,precision:3" style="width: 150px;text-align: right;"></input>
	    			元/度</td>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td>滞纳金:</td>
	    			<td><input id="zhinaScale" name="zhinaScale" class="easyui-numberbox" type="text" data-options="required:false,min:0,precision:0" style="width: 150px;text-align: right;"></input>
	    			‰/日</td>
	    			</td>
	    		</tr>
	    		<tr style="height: 15px;"><td colspan="2" valign="middle">
	    		   <div style="border:1px solid #ccc;width: 100%;vertical-align: middle;"></div>
	    		</td></tr>
	    		<tr>
	    			<td>卫生费:</td>
	    			<td>
	    			   <input id="weishengfei" class="easyui-numberbox" type="text" value="2.00" name="weishengfei" data-options="required:false,min:0,precision:2" style="width: 150px;text-align: right;">
	    			   元/月
	    			</td>
	    		</tr>
	    		<tr>
	    			<td>排污费:</td>
	    			<td>
	    			   <input id="paiwufei" class="easyui-numberbox" type="text" name="paiwufei" data-options="required:false,min:0,precision:2" style="width: 150px;text-align: right;">
	    			   元/月
	    			</td>
	    		</tr>
	    		<tr>
	    			<td>照明费:</td>
	    			<td>
	    			   <input id="zhaomingfei" class="easyui-numberbox"  type="text" name="zhaomingfei" data-options="required:false,min:0,precision:2" style="width: 150px;text-align: right;">
	    			   元/月
	    			</td>
	    		</tr>
	    	</table>
	    </form>
	    <div style="text-align:center;padding:5px">
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitUnitForm();">确定</a>
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#unitDialog').dialog('close');">取消</a>
	    </div>
	    </div>
	</div>
	<c:set var="scripts" scope="request">
	   <%@ include file="/scripts/admin/basedata/ElectricUnitManagement.js"%>
	</c:set>
	</div>
</body>
