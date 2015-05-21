<%@ page pageEncoding="UTF-8" %>
<script type="text/javascript">
var nowDateStr = '<fmt:formatDate value="${now}" type="both" dateStyle="long" pattern="yyyy-MM-dd" />';

$(function(){
	$('#monthCount').blur(function () {
		var monthCount = $("#monthCount").numberbox("getValue");
		var gratis = $('#gratis').prop("checked");
		var chargeState = $("#chargeState").val();
		if (chargeState != "未缴费") {
			return;
		}
		if (!gratis) {// 收费的情况
			var selectedRow = $('#data_grid').datagrid('getSelected');
			var unit = selectedRow.unit.unit;
			var area = selectedRow.house.repairArea;
			var mustCharge = unit * area * monthCount;
			$("#mustCharge").val(formatNummber(mustCharge, 2));
			var actualCharge = formatNummber(mustCharge, 1) + "0"; // 不收分钱
			$("#actualCharge").numberbox("setValue", formatNummber(actualCharge, 2).replace(",", ""));
		}
	});
});


function charge() {
	if ($('#chargeForm').form('validate')) {
		var recordId = $('#recordId').val();
		$('#chargeForm').form("submit", {
			url : "${ctx}/maintaincharge/" + recordId + "/actualCharge",
			success : function(data) {
				if (typeof data == "string") {
					var dataObj = $.parseJSON(data);  
					if (dataObj.error) {
						$.messager.alert('警告', dataObj.errorMsg);
						return;
					}
				}
				$('#chargeDialog').dialog('close');
				$('#data_grid').datagrid('reload');
				openPrintDialog(recordId);
			}
		});
	}
}

function openPrintDialog(recordId) {
	if (!recordId) {
		var selectedRow = $('#data_grid').datagrid('getSelected');
		if (selectedRow) {
			recordId = selectedRow.id;
		} else {
			$.messager.alert('警告','请选择记录');
			return;
		}
	}
	window.showModalDialog("${ctx}/HouseRepairChargePrint",
			recordId, "dialogWidth=800px;dialogHeight=400px;status:no;center:yes");
}

function cancel() {
	if ($('#cancelChargeForm').form('validate')) {
		var recordId = $('#cancelRecordId').val();
		$('#cancelChargeForm').form("submit", {
			url : "${ctx}/maintaincharge/" + recordId + "/cancel",
			success : function(data) {
				if (typeof data == "string") {
					var dataObj = $.parseJSON(data);  
					if (dataObj.error) {
						$.messager.alert('警告', dataObj.errorMsg);
						return;
					}
				}
				$('#cancelDialog').dialog('close');
				$('#data_grid').datagrid('reload');
			}
		});
	}
}

function openChargeDialog(recordId) {
	if (!recordId) {
		var selectedRow = $('#data_grid').datagrid('getSelected');
		if (selectedRow) {
			recordId = selectedRow.id;
		} else {
			$.messager.alert('警告','请选择记录');
			return;
		}
	}
	setDialogData(recordId);
	$("#chargeBtn").show();
	$('#chargeDialog').dialog('open');
}

function openDetailDialog(recordId) {
	if (!recordId) {
		var selectedRow = $('#data_grid').datagrid('getSelected');
		if (selectedRow) {
			recordId = selectedRow.id;
		} else {
			$.messager.alert('警告','请选择记录');
			return;
		}
	}
	setDialogData(recordId, true);
	$("#chargeBtn").hide();
	$('#chargeDialog').dialog('open');
}

function openCancelDialog(recordId) {
	if (!recordId) {
		var selectedRow = $('#data_grid').datagrid('getSelected');
		if (selectedRow) {
			recordId = selectedRow.id;
		} else {
			$.messager.alert('警告','请选择记录');
			return;
		}
	}
	$('#cancelRecordId').val(recordId);
	$('#cancelledRemarks').val("");
	$('#cancelDialog').dialog('open');
}

function recordOnCheck(rowIndex,rowData) {
	$("#toolbarDetailBtn").linkbutton("enable");
	if (rowData.chargeState == g_chargestate_uncharged) { // 未缴费
		$("#toolbarChargeBtn").linkbutton("enable");
		$("#toolbarCancelBtn").linkbutton("disable");
		$("#toolbarPrintBtn").linkbutton("disable");
	} else if (rowData.chargeState == g_chargestate_charged) { // 已缴费
		$("#toolbarChargeBtn").linkbutton("disable");
		$("#toolbarCancelBtn").linkbutton("enable");
		$("#toolbarPrintBtn").linkbutton("enable");
	} else { // 已取消
		$("#toolbarChargeBtn").linkbutton("disable");
		$("#toolbarCancelBtn").linkbutton("disable");
		$("#toolbarPrintBtn").linkbutton("disable");
	}
}

function recordOnUncheck() {
//	$("#toolbarChargeBtn").linkbutton("disable");
//	$("#toolbarDetailBtn").linkbutton("disable");
//	$("#toolbarCancelBtn").linkbutton("disable");
}

function setDialogData(recordId, readOnly) {
	$.ajax({
		url: "${ctx}/maintaincharge/chargerecord/" + recordId, 
		success: function(data) {
			$('#recordId').val(data.id);
			if (data.chargeState == g_chargestate_charged) { // 已缴费
				$('#chargeDate').val(data.chargeDate);
			} else {
				$('#chargeDate').val(nowDateStr);
			}
			$('#divertedCharge').val(data.divertedCharge);
			if (data.gratis) {
				$('#gratis').prop("checked", "checked");
			} else {
				$('#gratis').removeProp("checked");
			}
			$('#mustCharge').val(data.mustCharge);
			
			$('#paymentDateTitle').val(data.paymentDate.title);
			$('#chargeState').val(data.chargeState);
//			$('#payStartDate').val(data.paymentDate.payStartDate);
//			$('#payEndDate').val(data.paymentDate.payEndDate);
			$('#unit').val(data.unit.unit + "元/每平米每月");
			$('#diverted').val(data.diverted ? "是" : "否");
			$('#divertedMsg').val(data.divertedMsg);
			$('#cancelled').val(data.cancelled ? "是" : "否");
			
			$('#houseAddress').val(data.house.address);
			$('#yongfangXingzhi').val(data.house.yongfangXingzhi.name);
			$('#repairArea').val(data.house.repairArea + "平米");
			$('#ownerName').val(data.houseOwner.name);
			$('#shenfenXingzhi').val(data.house.shenfenXingzhi.name);
			$('#owerIdCardNo').val(data.houseOwner.idCardNo);
			$('#ownerPhone').val(data.houseOwner.phoneNo);
			$('#monthCount').numberbox("setValue", data.monthCount);
			$('#wageNum').val(data.wageNum)
			$('#operUserName').val(data.operUser.userName);
			$("#remarks").val(data.remarks);
			if (data.chargeState == g_chargestate_charged) { // 已缴费
				$("#actualCharge").numberbox("setValue", data.actualCharge.replace(",", ""));
			} else {
				var mustCharge = data.mustCharge.replace(",", ""); // 去除千位分隔符
				mustCharge = formatNummber(mustCharge, 1) + "0"; // 不收分钱
				$("#actualCharge").numberbox("setValue", readOnly ? "0" : mustCharge);
			}
			if (data.chargeState == g_chargestate_cancelled) { // 已取消
				// 此处其实应该有单独的字段作为取消时间以及取消人，为了简单，暂且使用记录的最后更改时间以及更改人
				$('#cancelledTime').val(data.lastUpdateTime);
				$('#cancelledUserName').val(data.lastUpdateUserName);
				$('#cancelledCause').val(data.cancelledCause);
			} else {
				$('#cancelledTime').val("");
				$('#cancelledUserName').val("");
				$('#cancelledCause').val("");
			}
		}});
}

function gratisOnClick() {
	var chargeState = $("#chargeState").val();
	var gratis = $('#gratis').prop("checked");
	if (chargeState != "未缴费") {
		return;
	}
	if (gratis) {
		$("#actualCharge").numberbox("setValue", 0);
	} else {
		var mustCharge = $("#mustCharge").val().replace(",", "");
		var actualSum = (Math.round(mustCharge * 10) / 10); // 不收分钱
		$("#actualCharge").numberbox("setValue", formatNummber(actualSum, 2).replace(",", ""));
	}
}

function clearHouseRepairQuery() {
	$('#base_q').combobox("select", COMBOBOX_DEFAULT_SEL_VAL);
	$('#area_q').combobox("select", COMBOBOX_DEFAULT_SEL_VAL);
	$('#louzuo_q').combobox("select", COMBOBOX_DEFAULT_SEL_VAL);
	$('#danyuan_q').val("");
	$('#louceng_q').val("");
	$('#ownerName_q').val("");
	$('#shenfenXingzhi_q').combobox("select", COMBOBOX_DEFAULT_SEL_VAL);
	$('#recordRemarks_q').val("");
	$('#paymentDateId_q').combobox("select", COMBOBOX_DEFAULT_SEL_VAL);
	$('#houseNo_q').val("");
	$('#yongfangXingzhi_q').combobox("select", COMBOBOX_DEFAULT_SEL_VAL);
	$('#chargeState_q').combobox("select", COMBOBOX_DEFAULT_SEL_VAL);
	$('#diverted_q').removeProp("checked");
	$('#wageNum_q').val("")
	$('#gratis_q').removeProp("checked");
}

function setQueryParams(params) {
	if (!commonItemsInitialized()) {
		// 如果页面没有加载完，延迟一会儿再加载datagrid
		setTimeout(function() {
			$('#data_grid').datagrid('reload');
		}, 500);
		return false;
	}
	if ($('#base_q').combobox("getValue") != COMBOBOX_DEFAULT_SEL_VAL) {
		params.baseCode = $('#base_q').combobox("getValue");
	}
	if ($('#area_q').combobox("getValue") != COMBOBOX_DEFAULT_SEL_VAL) {
		params.areaCode = $('#area_q').combobox("getValue");
	}
	if ($('#louzuo_q').combobox("getValue") != COMBOBOX_DEFAULT_SEL_VAL) {
		params.louzuoCode = $('#louzuo_q').combobox("getValue");
	}
	params.danyuan = $('#danyuan_q').val();
	params.ceng = $('#louceng_q').val();
	params.ownerName = $('#ownerName_q').val();
	if ($('#shenfenXingzhi_q').combobox("getValue") != COMBOBOX_DEFAULT_SEL_VAL) {
		params.shenfenXingzhiCode = $('#shenfenXingzhi_q').combobox("getValue");
	}
	params.gongshangNo = $('#gongshangNo_q').val();
	if ($('#yongfangXingzhi_q').combobox("getValue") != COMBOBOX_DEFAULT_SEL_VAL) {
		params.yongfangXingzhiCode = $('#yongfangXingzhi_q').combobox("getValue");
	}
	if ($('#chargeState_q').combobox("getValue") != COMBOBOX_DEFAULT_SEL_VAL) {
		params.chargeState = $('#chargeState_q').combobox("getValue");
	}
	params.recordRemarks = $('#recordRemarks_q').val();
	if ($('#paymentDateId_q').combobox("getValue") != COMBOBOX_DEFAULT_SEL_VAL) {
		params.paymentDateId = $('#paymentDateId_q').combobox("getValue");
	}
	params.chargeDate = $('#chargeDate_q').datebox('getValue');
	params.operName = $('#operName_q').val();
	
	params.houseNo = $('#houseNo_q').val();
	params.diverted = $('#diverted_q').prop("checked");
	params.gratis = $('#gratis_q').prop("checked");
	params.wageNum = $('#wageNum_q').val();
	$("#toolbarChargeBtn").linkbutton("disable");
	$("#toolbarDetailBtn").linkbutton("disable");
	$("#toolbarCancelBtn").linkbutton("disable");
	$("#toolbarPrintBtn").linkbutton("disable");
	return true;
}

function onLoadSuccess(data) {
	$('#yingshouMaintainCharge').text(data.yingshouMaintainCharge);
	$('#yishouMaintainCharge').text(data.yishouMaintainCharge);
}

</script>