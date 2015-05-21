<%@page pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<head>
	<meta charset="UTF-8">
	<title>维修费单价</title>
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
			    		<td><span style="margin-left: 10px;">维修费收取年度:</span></td>
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
	<div id="newWeixiufeiUnitDialog" class="easyui-dialog" title="维修费单价" style="width:400px" data-options="closed:true">
		<div style="padding:10px 0 10px 55px">
	    <form id="newWeixiufeiUnitForm" method="post">
	    	<input id="paymentDate_id" type="hidden" name="paymentDate.id"/>
	    	<input id="id" type="hidden" name="id"/>
	    	<table id="dianjiaTable">
	    	    <tr>
	    			<td>房屋性质:</td>
	    			<td>
	    				<input id="yongfangXingzhi" class="easyui-combobox" name="yongfangXingzhi.code" 
	    					style="width:155px;"type="dictitem" 
		    			   	dictcode="YONGFANG_XINGZHI" panelHeight="200px">      
						</input>
					</td>
	    		</tr>
	    	    <tr>
	    			<td>维修费单价:</td>
	    			<td><input id="unit" class="easyui-validatebox" type="text" name="unit" data-options="required:true" style="width: 100px;"></input>元/平方米
	    			</td>
	    		</tr>
	    	</table>
	    </form>
	    </div>
	    <div style="text-align:center;padding:5px">
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitNewWeixiufeiUnitForm();">确定</a>
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#newWeixiufeiUnitDialog').dialog('close');">取消</a>
	    </div>
	</div>
	<div  class="easyui-layout" style="padding-top:5px">
	<table id="data_grid" class="easyui-datagrid"  title="维修费单价" 
			data-options="url:'${ctx}/weixiufeiUnit/getWeixiufeiUnit',
									singleSelect:false,
									collapsible:true,
									method:'get',
									onBeforeLoad:setQueryParams,
									pageSize:10,
									toolbar: [
								    {
									   iconCls: 'icon-add',
									   text:'添加',
									   handler:function() { openNewWeixiufeiUnitDialog(); }
								    }]"
			rownumbers="true" pagination="true">
		<thead>
			<tr>
				<th data-options="field:'yongfangXingzhi.name',width:110,align:'center',formatter:getNestedValue">房屋性质</th>
				<th data-options="field:'unit',align:'center',width:100" >维修费单价</th>
				<th data-options="field:'id',width:120,align:'center',formatter:getOperHTML">操作</th>
			</tr>
		</thead>
	</table>
	</div>
	<c:set var="scripts" scope="request">
	   <%@ include file="/scripts/admin/basedata/WeixiufeiUnitManagement.js"%>
	</c:set>
	</div>
	</div>
	</div>
</body>
