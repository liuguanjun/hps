<%@ page pageEncoding="UTF-8" %>
<script type="text/javascript">
$(function(){
	$('#newWeixiufeiUnitForm').form({
		url : "${ctx}/weixiufeiUnit/addWeixiufeiUnit",
		method : "post",
		success : function(data) {
			if (typeof data == "string") {
				var dataObj = $.parseJSON(data);  
				if (dataObj.error) {
					$.messager.alert('警告', dataObj.errorMsg);
					return;
				}
			}
			$('#newWeixiufeiUnitDialog').dialog('close');
			$('#data_grid').datagrid('reload');
			$('#paymentDate_id').val($('#paymentDateId_q').combobox("getValue"));
		}
	});
});

function loadShouquYear(node) {
	var baseCode = node.code;
	var param = {baseCode:baseCode};
	// 基地没有选择时，不加载缴费年费下拉列表
	$.ajax({
		url: ctx + '/maintaincharge/' + baseCode + '/paymentdates',
		type : "get",
		data : param,
		success: function(data) {
			$("#paymentDateId_q").combobox({
				valueField:'id',    
			    textField:'title',
			    data: data,
			    onLoadSuccess : function(data) {
			    	// 没有查询到数据，选中默认选项
			    	if (!data || !data[0]) {
			    		data = [ { id : COMBOBOX_DEFAULT_SEL_VAL }];
			    	}
			    	$(this).combobox("setValue", data[0].id);
			    	$('#data_grid').datagrid('reload');
			    	$('#paymentDate_id').val($('#paymentDateId_q').combobox("getValue"));
			    },
			    onChange:function(data) {
			    	$('#data_grid').datagrid('reload');
			    	$('#paymentDate_id').val($('#paymentDateId_q').combobox("getValue"));
			    } 
			});
		}
	});
}

function formatTreeNode(node) {
	return node.name;
}

function selectFirstNode(node, data) {
	var firstRootNode = $('#baseTree').tree("getRoot");
	$('#baseTree').tree("select", firstRootNode.target);
	loadShouquYear(firstRootNode);
}

function setQueryParams(params) {
	// data_grid加载的时候，可能页面的combobox还没有加载完成，所以可能会出现异常
	// 此处忽略发生的异常
	try {
		if ($('#paymentDateId_q').combobox("getValue") != COMBOBOX_DEFAULT_SEL_VAL) {
			params.paymentDateId = $('#paymentDateId_q').combobox("getValue");
		}
		
	} catch (e) {
	}

}

function getOperHTML(value,row,index){
	return '<input type="button" value="编辑" style="margin-right: 3px;" onclick="openUpdateDialog(' + row.id + ');"></input>';
}

function openNewWeixiufeiUnitDialog() {
	$('#newWeixiufeiUnitDialog #id').val("");
	$('#newWeixiufeiUnitDialog').dialog('open');
}

function submitNewWeixiufeiUnitForm() {
	if ($('#newWeixiufeiUnitDialog').form('validate')) {
		$('#newWeixiufeiUnitForm').submit();
	}
}

function openUpdateDialog(UnitId) {
	if (!UnitId) {
		var selectedRow = $('#data_grid').datagrid('getSelected');
		if (selectedRow) {
			paymentDateId = selectedRow.id;
		} else {
			$.messager.alert('警告','请选择要编辑的记录');
			return;
		}
	}
	$.ajax({
		url: "${ctx}/weixiufeiUnit/getWeixiufeiUnitById/" + UnitId, 
		success: function(data) {
			$('#newWeixiufeiUnitDialog #id').val(data[0].id);// id
			$('#newWeixiufeiUnitDialog #unit').val(data[0].unit);// 单价
			$('#newWeixiufeiUnitDialog #yongfangXingzhi').val(data[0].yongfangXingzhi.name);// 用房性质
			$('#newWeixiufeiUnitDialog').dialog('open');
		}});
}

</script>

