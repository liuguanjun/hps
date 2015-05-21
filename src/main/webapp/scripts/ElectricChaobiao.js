<%@ page pageEncoding="UTF-8" %>
<script type="text/javascript">

var paymentDateComboboxInitialized = false;

$(function(){
	loadChargeMonthCombobox();
	baseComboboxOnCharge = loadChargeMonthCombobox;
});

function loadChargeMonthCombobox() {
	if (!commonItemsInitialized()) {
		// 如果页面没有加载完，延迟一会儿再加载
		setTimeout(function() {
			loadChargeMonthCombobox();
		}, 500);
		return;
	}
	paymentDateComboboxInitialized = false;
	var baseCode = $("#base_q").combobox("getValue");
	$.ajax({
		url: ctx + '/admin/electricPaymentDate/' + baseCode + "/paymentdatesbynow",
		type : "get",
		success: function(data) {// 
			$("#electricmonth_q").combobox({
				valueField:'id',    
			    textField:'monthFormatStr',
			    data: data,
			    onLoadSuccess : function(data) {
			    	// 没有查询到数据，选中默认选项
			    	if (!data || !data[0]) {
			    		data = [ { id : COMBOBOX_DEFAULT_SEL_VAL }];
			    	}
			    	$(this).combobox("setValue", data[0].id);
			    	paymentDateComboboxInitialized = true;
			    },
			});
		}});
}

function clearQuery() {
	$('#area_q').combobox("select", COMBOBOX_DEFAULT_SEL_VAL);
	$('#louzuo_q').combobox("select", COMBOBOX_DEFAULT_SEL_VAL);
	$('#danyuan_q').val("");
	$('#louceng_q').val("");
	$('#houseno_q').val("");
	$('#houseowner_q').val("");
	$('#recorded_q').removeProp("checked");
	$('#noRecord_q').removeProp("checked");
	$('#charged_q').removeProp("checked");
	$('#unCharged_q').removeProp("checked");
	$('#newElectricUser_q').removeProp("checked");
	$('#oldElectricUser_q').removeProp("checked");
}

function setQueryParams(params) {
	if (!commonItemsInitialized() || !paymentDateComboboxInitialized) {
		// 如果页面没有加载完，延迟一会儿再加载datagrid
		setTimeout(function() {
			$('#data_grid').datagrid('reload');
		}, 500);
		return false;
	}
	if ($('#electricmonth_q').combobox("getValue") != COMBOBOX_DEFAULT_SEL_VAL) {
		params.paymentDateId = $('#electricmonth_q').combobox("getValue");
	}
	if ($('#base_q').combobox("getValue") != COMBOBOX_DEFAULT_SEL_VAL) {
		params.base_code = $('#base_q').combobox("getValue");
	}
	if ($('#area_q').combobox("getValue") != COMBOBOX_DEFAULT_SEL_VAL) {
		params.area_code = $('#area_q').combobox("getValue");
	}
	if ($('#louzuo_q').combobox("getValue") != COMBOBOX_DEFAULT_SEL_VAL) {
		params.louzuo_code = $('#louzuo_q').combobox("getValue");
	}
	params.danyuan = $('#danyuan_q').val();
	params.ceng = $('#louceng_q').val();
	params.houseOwnerName = $('#houseowner_q').val();
	params.house_no = $('#houseno_q').val();
	params.recorded = $('#recorded_q').prop("checked");
	params.noRecord = $('#noRecord_q').prop("checked");
	
	params.charged = $('#charged_q').prop("checked");
	params.unCharged = $('#unCharged_q').prop("checked");
	params.newElectricUser = $('#newElectricUser_q').prop("checked");
	params.oldElectricUser = $('#oldElectricUser_q').prop("checked");
	
	var dataRows = $("#data_grid").datagrid('getChanges','updated');
	if (dataRows.length > 0) {
		for (var i = 0; i < dataRows.length; i++) {
			var element = dataRows[i];
			if (element.inputCheckResult == "输入有误") {
				return true;
			}
		}		
		return confirm('有尚未保存的抄表数据，刷新之后会丢失未保存的数据，确认要刷新吗？');
	} else {
		return true;
	}
}

function saveData() {
	var dataRows = $("#data_grid").datagrid('getChanges','updated');
	for (var i = 0; i < dataRows.length; i++) {
		var element = dataRows[i];
		if (element.inputCheckResult == "输入有误") {
			$.messager.alert('消息','您输入的表值有错误，请修改后保存'); 
			return;
		}
	}
	endEditing();
	accept();
}

function accept() {
	var updateRows = $('#data_grid').datagrid('getChanges','updated');
	$(updateRows).each(function(rowIndex, element) {
		$('#data_grid').datagrid('endEdit', rowIndex);
	});
	var effectRow = new Object();
    if (updateRows.length) {
            effectRow["updated"] = JSON.stringify(updateRows);
    }
    $.post(ctx + "/admin/ElectricManagement/saveElectricChaobiao", effectRow, function(dataObj) {
    	if (dataObj.error) {
			$.messager.alert('警告', dataObj.errorMsg);
			return;
		}
    	$.messager.alert('消息','保存成功'); 
    	// alert('保存成功'); 
        $("#data_grid").datagrid('acceptChanges');
        $('#data_grid').datagrid('reload');
    }, "JSON");

}

function formatElectricCount(value, rowData, rowIndex) {
	if (rowData.provReadoutsElectric && rowData.readoutsElectric) {
		return rowData.readoutsElectric - rowData.provReadoutsElectric;
	} else {
		return "";
	}
}

function formatInputStatus(value, rowData, rowIndex) {
	if (value == "输入正常") {
		return "<span style='color: green;font-weight: bold;vertical-align: middle;'>输入正常</span>";
	} else if (value == "输入有误") {
		return "<span style='color: red;font-weight: bold;vertical-align: middle;'>输入错误</span>";
	} else {
		return "";
	}
}

var editIndex = undefined;
function endEditing(){
	if (editIndex == undefined) { return; }
	if ($('#data_grid').datagrid('validateRow', editIndex)) {
		$('#data_grid').datagrid('endEdit', editIndex);
		editIndex = undefined;
		return;
	} else {
		return;
	}
}

function onAfterEdit(rowIndex, rowData, changes) {
	if (changes.hasOwnProperty("readoutsElectric") ||
			changes.hasOwnProperty("provReadoutsElectric") || changes.hasOwnProperty("newReadoutsElectric")) {
		var checkResult = "输入正常";
		var electricCount = rowData.readoutsElectric - rowData.provReadoutsElectric;
		if (isNaN(electricCount) || electricCount < 0) {
			checkResult = "输入有误";
		}
		$('#data_grid').datagrid('getRows')[rowIndex]['inputCheckResult'] = checkResult;
		$('#data_grid').datagrid('getRows')[rowIndex]['nosave'] = "*";
		$('#data_grid').datagrid('refreshRow', rowIndex);
	}
}

function onClickRow(index, rowData) {
	endEditing();
	if (!rowData.chargeDate) {
		$('#data_grid').datagrid('beginEdit', index);
		editIndex = index;
	}
}

function onLoadSuccess(data) {
	$('#yingshouElectricCount').text(data.yingshouElectricCount);
	$('#yingshouElectricCharge').text(data.yingshouElectricCharge);
	$('#yishouElectricCount').text(data.yishouElectricCount);
	$('#yishouElectricCharge').text(data.yishouElectricCharge);
}
</script>