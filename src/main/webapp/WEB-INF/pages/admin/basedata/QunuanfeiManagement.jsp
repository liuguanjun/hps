<%@page pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<head>
	<meta charset="UTF-8">
	<title>取暖费单价</title>
	<meta name="menu" content="AdminMenu"/>
</head>
<body>
    <div class="col-sm-10">
    <div class="easyui-layout" style="height: 500px;">
	<div data-options="region:'west',split:true,title:'区域导航'" style="width:170px;">
       <ul id="areaTree" class="easyui-tree"></ul>
    </div>
    <div id="mainPanle" region="center">
	<div id="newUserDialog0" class="easyui-dialog" title="修改取暖费" style="width:400px" data-options="closed:true">
		<div style="padding:10px 0 10px 55px">
	    <form id="ff" method="post">
	    	<table id="dianjiaTable">
	    	    <tr>
	    			<td>房屋性质:</td>
	    			<td>
	    			    <select class="easyui-combobox" name="state" style="width:155px;" panelHeight="90px">
							<option value="AL">居民</option>
							<option value="AK">商业</option>
                        </select>
	    			</td>
	    		</tr>
	    	    <tr>
	    			<td>取暖费单价:</td>
	    			<td><input value="38" type="text" style="width: 35px;"></input>
	    			元/平方米
	    			</td>
	    		</tr>
	    	</table>
	    </form>
	    </div>
	    <div style="text-align:center;padding:5px">
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#newUserDialog').dialog('close');">确定</a>
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#newUserDialog').dialog('close');">取消</a>
	    </div>
	</div>
	<div  class="easyui-layout" >
	<table id="data_grid" class="easyui-datagrid"  title="取暖费" 
			data-options="singleSelect:true,collapsible:true,method:'get'"
			rownumbers="true">
		<thead>
			<tr>
				<th data-options="field:'yongfangXingzhi',width:110,align:'center'" >房屋性质</th>
				<th data-options="field:'unit',align:'center',width:100" >取暖费单价</th>
				<th data-options="field:'isLevel',width:120,align:'center',formatter:getOperHTML0">操作</th>
			</tr>
		</thead>
	</table>
	</div>
	<div id="comment"  class="easyui-layout"  style="margin-top: 10px;margin-left: 10px;">
	   <span style="color: red;font-weight: bold;">
	      注：经济性房屋层高基数为3米，每超高0.3米，加收热费5%，最高加收不超过50%。
	   </span>
	</div>
	<c:set var="scripts" scope="request">
	   <%@ include file="/scripts/admin/basedata/QunuanfeiManagement.js"%>
	</c:set>
	</div>
	</div>
	</div>
</body>
