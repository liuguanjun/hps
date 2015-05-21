<%@ page pageEncoding="UTF-8" %>
<script type="text/javascript">

var yearInitialized = false;
var previousSelectedNode;

$(function(){
	$('#paymentDateForm').form({url : "${ctx}/admin/electricPaymentDate/paymentdate",
		success : function(data) {
			if (typeof data == "string") {
				var dataObj = $.parseJSON(data);  
				if (dataObj.error) {
					$.messager.alert('警告', dataObj.errorMsg);
					return;
				}
			}
			$('#paymentDateDialog').dialog('close');
			$('#data_grid').datagrid('reload');
		}
	});
});

function selectFirstNode(node, data) {
	var firstRootNode = $('#baseTree').tree("getRoot");
	$('#baseTree').tree("select", firstRootNode.target);
	treeNodeOnClick(firstRootNode);
}

function treeNodeOnClick(node) {
	$('#data_grid').datagrid("options").url = ctx + "/admin/electricPaymentDate/" 
		+ node.code + "/paymentdates";
	if (previousSelectedNode && node.code == previousSelectedNode.code) {
		// 基地没有改变，不需要重新load页面combobox
	} else {
		loadYearCombobox(node);
		if (previousSelectedNode) {
			// 节点切换时
			$('#data_grid').datagrid('reload');
		}
	}
	previousSelectedNode = node;
}

function setQueryParams(params) {
	if (!yearInitialized) {
		// 如果页面没有加载完，延迟一会儿再加载datagrid
		setTimeout(function() {
			$('#data_grid').datagrid('reload');
		}, 500);
		return false;
	}
	var year = $('#year_q').combobox("getValue");
	if (year && year != COMBOBOX_DEFAULT_SEL_VAL) {
		params.year = year;
	}
	$("#toolbarEditBtn").linkbutton("disable");
	return true;
}

function loadYearCombobox(node) {
	var baseCode = node.code;
	var param = {baseCode:baseCode};
	$.ajax({
		url: ctx + '/admin/electricPaymentDate/' + baseCode + "/paymentyears",
		type : "get",
		data : param,
		success: function(data) {
			$("#year_q").combobox({
				valueField:'year',    
			    textField:'year',
			    data: data,
			    onLoadSuccess : function(data) {
			    	// 没有查询到数据，选中默认选项
			    	if (!data || !data[0]) {
			    		data = [ { id : COMBOBOX_DEFAULT_SEL_VAL }];
			    	}
			    	$(this).combobox("setValue", data[0].year);
			    	yearInitialized = true;
			    },
			    onChange : function (newValue, oldValue) {
			    	$('#data_grid').datagrid('reload');
			    }
			});
			//yearInitialized = true;
		}
	});
}

function openPaymentDateDialog() {
	var id;
	var selectedRow = $('#data_grid').datagrid('getSelected');
	if (selectedRow) {
		id = selectedRow.id;
	} else {
		$.messager.alert('警告','请选择要编辑的数据');
		return;
	}
	$.ajax({
		url: "${ctx}/admin/electricPaymentDate/paymentdate/" + id, 
		success: function(data) {
			$("#id").val(data.id);
			$("#month").val(data.monthFormatStr);
			$("#startDate").datebox('setValue', data.startDate);
			$("#endDate").datebox('setValue', data.endDate);
			$('#paymentDateForm').form('validate');
			$('#paymentDateDialog').dialog('open');
		}});
}

function submitPaymentDateForm() {
	if ($('#paymentDateDialog').form('validate')) {
		$('#paymentDateForm').submit();
	}
}

function formatTreeNode(node) {
	return node.name;
}

function paymentDateRowOnCheck() {
	$("#toolbarEditBtn").linkbutton("enable");
}


</script>

