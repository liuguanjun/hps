<%@page pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<head>
	<meta charset="UTF-8">
	<title>房屋综合查询</title>
	<meta name="menu" content="FangwuZongheChaxun"/>
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
		    			   <input id="danyuan_q" name="danyuan" style="width:155px;"></input>
		    			</td>
	    			</tr>
		    		<tr>
		    		    <td><span style="margin-left: 10px;">房号:</span></td>
		    			<td>
		    			   <input id="houseNo_q" name="houseNo" style="width:155px;"></input>
		    			</td>
		    			<td><span style="margin-left: 10px;">户主号:</span></td>
		    			<td>
		    			   <input id="ownerNo_q" name="ownerNo" style="width:155px;"></input>
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
		    			<td><span style="margin-left: 10px;">房主备注:</span></td>
		    			<td colspan="3">
		    			   <input id="remarks_q" name="remarks" style="width:155px;"></input>
		    			</td>
	    			</tr>
	    	   </table>
			</div>
	   </div>
	</div>
	<table id="data_grid" class="easyui-datagrid" title="查询结果" 
			data-options="url:'${ctx}/admin/basedata/houses',
			singleSelect:true,
			collapsible:true,
			method:'get',
			pageSize:10,
			onBeforeLoad:setQueryParams,
			toolbar: [
				    {
					   iconCls: 'icon-add',
					   text:'添加',
					   handler:function() { openNewDialog(); }
				    }, '-' ,
				    <security:authorize ifAnyGranted='ROLE_SUPERADMIN'>  
				    {
					   iconCls: 'icon-edit',
					   text:'修改房屋',
					   handler: function() { openUpdateDialog(); }
				   }, '-' ,
				   </security:authorize>
				    {
					   iconCls: 'icon-edit',
					   text:'房屋更名',
					   handler: function() { openChangeDialog(); }
				   }, '-' ,
				    {
					   iconCls: 'icon-cancel',
					   text:'删除',
					   handler: function() { deleteHouse(); }
				   }]"
			rownumbers="true" pagination="true">
		<thead>
			<tr>
				<th data-options="field:'ck',width:60,align:'center',checkbox:true">选择</th>
				<th data-options="field:'owner.name',width:100,align:'center',formatter:getNestedValue">房主名</th>
				<th data-options="field:'owner.no',width:80,align:'center',formatter:getNestedValue">房主号</th>
				<th data-options="field:'shenfenXingzhi.name',width:80,align:'center',formatter:getNestedValue">身份性质</th>
				<th data-options="field:'no',width:80,align:'center'">房号</th>
				<th data-options="field:'louzuo.area.baseName',width:100,align:'center',formatter:getNestedValue">基地</th>
				<th data-options="field:'louzuo.area.name',width:100,align:'center',formatter:getNestedValue">区域</th>
				<th data-options="field:'louzuo.name',width:60,align:'center',formatter:getNestedValue">楼座</th>
				<th data-options="field:'danyuan',width:60,align:'center'">单元</th>
				<th data-options="field:'ceng',width:60,align:'center'">层</th>
				<th data-options="field:'doorNo',width:80,align:'center'">门牌号</th>
				<th data-options="field:'warmArea',width:100,align:'center'">采暖面积（㎡）</th>
				<th data-options="field:'repairArea',width:100,align:'center'">房维面积（㎡）</th>
			</tr>
		</thead>
	</table>
	<div id="houseDialog" class="easyui-dialog" title="新建房屋" style="width:400px" data-options="closed:true,modal:true">
		<div style="padding:10px 0 10px 60px">
	    <form id="houseForm" method="post">
	        <input id="id" type="hidden" name="id"/>
	        <input id="method" type="hidden" name="_method" value="post"/>
	        <input id="ownerId" type="hidden" name="owner.id"/>
	    	<table>
	    		<tr>
	    			<td>户主姓名:</td>
	    			<td><input class="easyui-validatebox" data-options="required:true"
	    				 id="ownerName" name="owner.name" style="width:150px;"></td></td>
	    		</tr>
	    		<tr>
	    			<td>身份证号:</td>
	    			<td>
	    				<input class="easyui-validatebox" data-options="required:false"
	    					 id="ownerIdCardNo" name="owner.idCardNo" style="width:150px;"></input>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td>房主电话:</td>
	    			<td>
	    				<input class="easyui-validatebox" data-options="required:false"
	    					 id="ownerPhoneNo" name="owner.phoneNo" style="width:150px;"></input>
	    			</td>
	    		</tr>
	    		<tr id="ownerNoRow">
	    			<td>户主编号:</td>
	    			<td><input id="ownerNo" name="owner.no" style="width:150px;" readonly="readonly"></td></td>
	    		</tr>
	    		<tr>
	    			<td>身份性质:</td>
	    			<td>
	    				<input id="shenfenXingzhi" name="shenfenXingzhi.code"
	    				 	class="easyui-combobox" name="dept" style="width:155px;"
	    				 	type="dictitem" dictcode="SHENFEN_XINGZHI">   
						</input>
					</td>
	    		</tr>
	    		<tr>
	    			<td>工资号:</td>
	    			<td>
	    				<input id="wageNum" name="owner.wageNum" style="width:150px;"></input>
					</td>
	    		</tr>
	    		<tr>
	    			<td>户主其他信息:</td>
	    			<td>
	    				<input id="remarks" name="owner.remarks" style="width:150px;"></input>
					</td>
	    		</tr>
	    		<tr id="noRow">
	    			<td>房号:</td>
	    			<td><input id="no" name="no" style="width:150px;" readonly="readonly"></input>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td>基地:</td>
	    			<td>
	    				<input id="baseCode" name="louzuo.area.base.code" class="easyui-combobox" style="width:155px;"
	    					type="base" panelHeight="120px">   
						</input>
					</td>
	    		</tr>
	    		<tr>
	    			<td>区域:</td>
	    			<td>
	    				<input id="areaCode" name="louzuo.area.code" class="easyui-combobox" type="area"
	    					 baseComboboxId="baseCode" style="width:155px;">   
						</input>
					</td>
	    		</tr>
	    		<tr>
	    			<td>楼座:</td>
	    			<td>
	    				<input id="louzuo" type="louzuo" name="louzuo.code"
	    					areaComboboxId="areaCode" style="width:155px;" panelHeight="200px">   
						</input>
					</td>
	    		</tr>
	    		<tr>
	    			<td>单元:</td>
	    			<td><input class="easyui-numberbox" data-options="required:true,min:1,precision:0" id="danyuan" name="danyuan" style="width:150px;"/></td>
	    		</tr>
	    		<tr>
	    			<td>楼层:</td>
	    			<td><input class="easyui-numberbox" data-options="required:true,min:1,precision:0" id="ceng" name="ceng" style="width:150px;"/></td>
	    		</tr>
	    		<tr>
	    			<td>门牌号:</td>
	    			<td><input id="doorNo" class="easyui-validatebox" data-options="required:true"
	    				 name="doorNo" style="width:150px;"/></td>
	    		</tr>
	    		<tr>
	    			<td>工商注册号:</td>
	    			<td>
	    				<input id="gongshangNo" name="gongshangNo" style="width:150px;"></input>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td>采暖面积:</td>
	    			<td>
	    				<input id="warmArea" class="easyui-numberbox" data-options="required:true,min:0,precision:2" name="warmArea" style="width:150px;"></input>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td>房维面积:</td>
	    			<td>
	    				<input id="repairArea" class="easyui-numberbox" data-options="required:true,min:0,precision:2" name="repairArea" style="width:150px;"></input>
	    			</td>
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
	    	</table>
	    </form>
	    <div style="text-align:center;padding:5px">
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitHouseForm();">确定</a>
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#houseDialog').dialog('close');">取消</a>
	    </div>
	    </div>
	</div>
	<c:set var="scripts" scope="request">
	   <%@ include file="/scripts/admin/basedata/FangwuZongheChaxun.js"%>
	</c:set>
</body>
