<%@ page pageEncoding="UTF-8" %>
<script type="text/javascript">
$(function(){
	$('#newShenfenxingzhiForm').form({
		url : "${ctx}/qunuanfeiYouhui/addShenfenxingzhiYouhui",
		method : "post",
		success : function(data) {
			if (typeof data == "string") {
				var dataObj = $.parseJSON(data);  
				if (dataObj.error) {
					$.messager.alert('警告', dataObj.errorMsg);
					return;
				}
			}
			$('#newShenfenxingzhiDialog').dialog('close');
			$('#data_grid_shenfenxingzhi').datagrid('reload');
			$('#paymentDate_id').val($('#paymentDateId_q').combobox("getValue"));
			
		}
	});
	$('#newDateYouhuiForm').form({
		url : "${ctx}/qunuanfeiYouhui/addPaymentYouhui",
		method : "post",
		success : function(data) {
			if (typeof data == "string") {
				var dataObj = $.parseJSON(data);  
				if (dataObj.error) {
					$.messager.alert('警告', dataObj.errorMsg);
					return;
				}
			}
			$('#newDateYouhuiDialog').dialog('close');
			$('#data_grid_payment').datagrid('reload');
			$('#paymentDate_id_dateYouhui').val($('#paymentDateId_q').combobox("getValue"));
		}
	});
});

function deleteUser() {
	$.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
	    if (r){    
	    	//$.messager.alert('确认','删除成功！');    
	    } else {
	    	
	    }
	});  
}

function loadShouquYear(node) {
	var baseCode = node.code;
	var param = {baseCode:baseCode};
	// 基地没有选择时，不加载缴费年费下拉列表
	$.ajax({
		url: ctx + '/heatingcharge/' + baseCode + '/paymentdates',
		type : "get",
		data : param,
		success: function(data) {
			//initHeatingPaymentDatesCombobox(element, data);
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
			    	$('#data_grid_shenfenxingzhi').datagrid('reload');
			    	$('#data_grid_payment').datagrid('reload');
			    	$('#paymentDate_id').val($('#paymentDateId_q').combobox("getValue"));
			    	$('#paymentDate_id_dateYouhui').val($('#paymentDateId_q').combobox("getValue"));
			    },
			    onChange:function(data) {
			    	$('#data_grid_shenfenxingzhi').datagrid('reload');
			    	$('#data_grid_payment').datagrid('reload');
			    	$('#paymentDate_id').val($('#paymentDateId_q').combobox("getValue"));
			    	$('#paymentDate_id_dateYouhui').val($('#paymentDateId_q').combobox("getValue"));
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
		} else {
			return false;
		}
		
	} catch (e) {
	}

}

function getOperHTMLShenfen(value,row,index){
	return '<input type="button" value="编辑" style="margin-right: 3px;" onclick="openUpdateShenfenxingzhiDialog(' + row.id + ');"></input>';
}

function getOperHTMLPayment(value,row,index){
	return '<input type="button" value="编辑" style="margin-right: 3px;" onclick="openUpdatePaymentDateDialog(' + row.id + ');"></input>';
}


function openNewShenfenxingzhiDialog() {
	$('#newShenfenxingzhiDialog #id').val("");
	$('#newShenfenxingzhiDialog').dialog('open');
}

function openNewDateYouhuiDialog() {
	$('#newDateYouhuiDialog #id').val("");
	$('#newDateYouhuiDialog').dialog('open');
}

function submitNewShenfenxingzhiForm() {
	if ($('#newShenfenxingzhiDialog').form('validate')) {
		$('#newShenfenxingzhiForm').submit();
	}
}

function submitNewDateYouhuiForm() {
	if ($('#newDateYouhuiDialog').form('validate')) {
		$('#newDateYouhuiForm').submit();
	}
}

function openUpdateShenfenxingzhiDialog(shenfenxingzhiYouhuiId) {
	if (!shenfenxingzhiYouhuiId) {
		var selectedRow = $('#data_grid_shenfenxingzhi').datagrid('getSelected');
		if (selectedRow) {
			shenfenxingzhiYouhuiId = selectedRow.id;
		} else {
			$.messager.alert('警告','请选择要编辑的记录');
			return;
		}
	}
	$.ajax({
		url: "${ctx}/qunuanfeiYouhui/getShenfenxingzhiYouhuiById/" + shenfenxingzhiYouhuiId, 
		success: function(data) {
			$('#newShenfenxingzhiDialog #id').val(data[0].id);
			$('#newShenfenxingzhiDialog #ownerShenfenXingzhi').combobox("select", data[0].shenfengXingzhi.code);
			$('#newShenfenxingzhiDialog #payRate').val(data[0].payRate * 100);
			$('#newShenfenxingzhiDialog').dialog('open');
		}});
}

function openUpdatePaymentDateDialog(paymentYouhuiId) {
	if (!paymentYouhuiId) {
		var selectedRow = $('#data_grid_payment').datagrid('getSelected');
		if (selectedRow) {
			paymentYouhuiId = selectedRow.id;
		} else {
			$.messager.alert('警告','请选择要编辑的记录');
			return;
		}
	}
	$.ajax({
		url: "${ctx}/qunuanfeiYouhui/getPaymentYouhuiById/" + paymentYouhuiId, 
		success: function(data) {
			$('#newDateYouhuiDialog #id').val(data[0].id);
			$('#newDateYouhuiDialog #startDate').datebox('setValue', data[0].startDate);
			$('#newDateYouhuiDialog #endDate').datebox('setValue', data[0].endDate);
			$('#newDateYouhuiDialog #offRate').val(data[0].offRate * 100);
			$('#newDateYouhuiDialog').dialog('open');
		}});
}
</script>

