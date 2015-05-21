<%@ page pageEncoding="UTF-8" %>
<script type="text/javascript">
$(function(){
	$('#newPaymentDateDialogForm').form({
		url : "${ctx}/weixiufeiPaymentDate/addWeixiufeiPayment",
		method : "post",
		success : function(data) {
			if (typeof data == "string") {
				var dataObj = $.parseJSON(data);  
				if (dataObj.error) {
					$.messager.alert('警告', dataObj.errorMsg);
					return;
				}
			}
			$('#newPaymentDateDialog').dialog('close');
			$('#data_grid').datagrid('reload');
		}
	});

});

function formatTreeNode(node) {
	return node.name;
}

function selectFirstNode(node, data) {
	var firstRootNode = $('#baseTree').tree("getRoot");
	$('#baseTree').tree("select", firstRootNode.target);
	$('#data_grid').datagrid('reload');
}

function setQueryParams(params) {
	var selectedNode = $('#baseTree').tree("getSelected");
	if (selectedNode) {
		$('#data_grid').datagrid("options").url = ctx + "/weixiufeiPaymentDate/getWeixiufeiPayment/" + selectedNode.id;
		return true;
	} else {
		return false;
	}
}

function openNewPaymentDateDialog() {
	$('#newPaymentDateDialog #base_id').val("");
	$('#newPaymentDateDialog #id').val("");
	$('#newPaymentDateDialog').dialog('open');
}

function getOperHTML(value,row,index){
    return '<input type="button" value="编辑" onclick="openUpdateDialog(' + row.id + ');"></input>';
}

function openUpdateDialog(paymentDateId) {
	if (!paymentDateId) {
		var selectedRow = $('#data_grid').datagrid('getSelected');
		if (selectedRow) {
			paymentDateId = selectedRow.id;
		} else {
			$.messager.alert('警告','请选择要编辑的记录');
			return;
		}
	}
	$.ajax({
		url: "${ctx}/weixiufeiPaymentDate/getPaymentDateById/" + paymentDateId, 
		success: function(data) {
			$('#newPaymentDateDialog #id').val(data[0].id);
			$('#newPaymentDateDialog #title').val(data[0].title);
			$('#newPaymentDateDialog').dialog('open');
		}});
}

function submitNewPaymentDateForm() {
	if ($('#newPaymentDateDialog').form('validate')) {
		var selectedNode = $('#baseTree').tree('getSelected');
		$('#newPaymentDateDialogForm #base_id').val(selectedNode.id);
		$('#newPaymentDateDialogForm').submit();
	}
}


</script>

