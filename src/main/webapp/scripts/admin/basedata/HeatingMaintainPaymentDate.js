<%@ page pageEncoding="UTF-8" %>
<script type="text/javascript">
$(function(){
	$('#newPaymentDateDialogForm').form({
		url : "${ctx}/heatingmaintain2015/paymentdate",
		method : "put",
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
		url: "${ctx}/heatingmaintain2015/paymentdate/" + paymentDateId, 
		success: function(data) {
			$('#newPaymentDateDialog #id').val(data.id);
			$('#newPaymentDateDialog #title').val(data.title);
			$('#newPaymentDateDialog #payStartDate').datebox('setValue', data.payStartDate);
			$('#newPaymentDateDialog #payEndDate').datebox('setValue', data.payEndDate);
			$('#newPaymentDateDialog #unit').val(data.unit);
			$('#newPaymentDateDialog').form('validate')
			$('#newPaymentDateDialog').dialog('open');
		}});
}

function submitNewPaymentDateForm() {
	if ($('#newPaymentDateDialog').form('validate')) {
		$('#newPaymentDateDialogForm').submit();
	}
}


</script>

