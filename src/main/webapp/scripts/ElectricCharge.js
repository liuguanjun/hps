<%@ page pageEncoding="UTF-8" %>
<script type="text/javascript">

var DialogTyoe_Charge = "Charge";
var DialogTyoe_Detail = "Detail";

$(function(){
	
	loadChargeState();
	
});

function openPrintDialog(recordObj) {
	var chaobiaoSize = recordObj.chaobiaoSet.length;
	var page = "";
	var dialogHeight = "400px";
	if (recordObj.base.id == 4) { // 旅顺
		page = "ElectricChargePrintForLvshun";
		dialogHeight = "300px";
	} else if (chaobiaoSize <= 3) {
		page = "ElectricChargePrint";
	} else if (chaobiaoSize <= 11) {
		page = "ElectricChargePrint2Page";
	} else {
		page = "ElectricChargePrint3Page";
	}
	window.showModalDialog("${ctx}/" + page,
			recordObj.id, "dialogWidth=800px;dialogHeight=" + dialogHeight + ";status:no;center:yes");
}

function openChargeRecordsDialog() {
	var selectedRow = $('#data_grid').datagrid('getSelected');
	if (selectedRow) {
		houseId = selectedRow.house.id;
	} else {
		$.messager.alert('警告','请选择记录');
		return;
	}
	$('#data_grid_chargerecord').datagrid("options").url = ctx + "/elecharge/" + houseId + "/chargerecords";
	$('#chargeRecordDialog').dialog('open');
	$("#toolbarCancelBtn").linkbutton("disable");
	$("#toolbarDetailBtn").linkbutton("disable");
	$('#data_grid_chargerecord').datagrid("reload");
	
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
	if ($('#electricChargeState_q').combobox("getValue") != COMBOBOX_DEFAULT_SEL_VAL) {
		params.electricChargeState = $('#electricChargeState_q').combobox("getValue");
	}
	params.ownerIdCardNo = $('#ownerIdCardNo_q').val();
	params.houseNo = $('#houseNo_q').val();
	params.ownerNo = $('#ownerNo_q').val();
	$("#toolbarChargeBtn").linkbutton("disable");
	$("#toolbarSearchChargeRecord").linkbutton("disable");
	$("#toolbarInitSurplusBtn").linkbutton("disable");
	return true;
}

function recordOnCheck(rowIndex,rowData) {
	if (rowData.electricChargeStateName == g_chargestate_uncharged) { // 未缴费
		$("#toolbarChargeBtn").linkbutton("enable");
	} else { // 已缴费
		if (rowData.unchargedChaobiaoRecords.length > 0) {
			$("#toolbarChargeBtn").linkbutton("enable");
		} else {			
			$("#toolbarChargeBtn").linkbutton("disable");
		}
	}
	$("#toolbarSearchChargeRecord").linkbutton("enable");
	if (!rowData.lastChargeDate) {
		$.messager.alert('警告', rowData.house.address + " 在13年可能有欠费或者余额，需要去老系统确认后进行缴费操作。");
		$("#toolbarInitSurplusBtn").linkbutton("enable");
	} else {
		$("#toolbarInitSurplusBtn").linkbutton("disable");
	}
}

function setChaobiaoQueryParams(params) {

}


function clearHouseQuery() {
	//$('#base_q').combobox("select", COMBOBOX_DEFAULT_SEL_VAL);
	$('#area_q').combobox("select", COMBOBOX_DEFAULT_SEL_VAL);
	$('#louzuo_q').combobox("select", COMBOBOX_DEFAULT_SEL_VAL);
	$('#danyuan_q').val("");
	$('#louceng_q').val("");
	$('#ownerName_q').val("");
	$('#shenfenXingzhi_q').combobox("select", COMBOBOX_DEFAULT_SEL_VAL);
	$('#ownerIdCardNo_q').val("");
	$('#houseNo_q').val("");
	$('#ownerNo_q').val("");
	$('#yongfangXingzhi_q').combobox("select", COMBOBOX_DEFAULT_SEL_VAL);
	$('#electricChargeState_q').combobox("select", COMBOBOX_DEFAULT_SEL_VAL);
}

function openChargeDialog(houseId) {
	if (!houseId) {
		var selectedRow = $('#data_grid').datagrid('getSelected');
		if (selectedRow) {
			houseId = selectedRow.house.id;
		} else {
			$.messager.alert('警告','请选择记录');
			return;
		}
	}
	$('#dialogType').val(DialogTyoe_Charge);
	$('#houseId').val(houseId);
	$('#zhinajinOn').removeProp("checked");
	$('#data_grid_chaobiao').datagrid("options").url = ctx + "/elecharge/" + houseId + "/chargestate";
	$('#chargeDialog').dialog('open');
	$('#data_grid_chaobiao').datagrid("reload");
}

function openDetailDialog() {
	var selectedRow = $('#data_grid_chargerecord').datagrid('getSelected');
	var chargeRecordId;
	if (selectedRow) {
		chargeRecordId = selectedRow.id;
	} else {
		$.messager.alert('警告','请选择记录');
		return;
	}
	$('#dialogType').val(DialogTyoe_Detail);
	$('#data_grid_chaobiao').datagrid("options").url = ctx + "/elecharge/" + chargeRecordId + "/chargerecordstate";
	$('#chargeDialog').dialog('open');
	$('#data_grid_chaobiao').datagrid("reload");
}

function openInitSurplusDialog() {
	var selectedRow = $('#data_grid').datagrid('getSelected');
	var houseId;
	if (selectedRow) {
		houseId = selectedRow.house.id;
	} else {
		$.messager.alert('警告','请选择记录');
		return;
	}
	$('#initHouseId').val(houseId);
	$("#initSurplus").numberbox("setValue", 0);
	$("#initElectricReadout").numberbox("setValue", 0);
	$("#setInitSurplusDialog").dialog('open');
	
}

function initHouseSurplus() {
	var houseId = $('#initHouseId').val();
	$('#initSurplusForm').form("submit", {
		url : "${ctx}/elecharge/" + houseId + "/initsurplus",
		success : function(data) {
			if (typeof data == "string") {
				var dataObj = $.parseJSON(data);  
				if (dataObj.error) {
					$.messager.alert('警告', dataObj.errorMsg);
					return;
				}
			}
			$('#setInitSurplusDialog').dialog('close');
			$('#data_grid').datagrid('reload');
		}
	});
}

function cancelRecordCharge() {
	var recordId = $('#recordId').val();
	$('#cancelChargeForm').form("submit", {
		url : "${ctx}/elecharge/" + recordId + "/cancelledcause",
		success : function(data) {
			if (typeof data == "string") {
				var dataObj = $.parseJSON(data);  
				if (dataObj.error) {
					$.messager.alert('警告', dataObj.errorMsg);
					return;
				}
			}
			$('#cancelDialog').dialog('close');
			$("#toolbarCancelBtn").linkbutton("disable");
			$("#toolbarDetailBtn").linkbutton("disable");
			$('#data_grid_chargerecord').datagrid('reload');
		}
	});
}

function openCancelDialog() {
	var selectedRow = $('#data_grid_chargerecord').datagrid('getSelected');
	var recordId;
	if (selectedRow) {
		recordId = selectedRow.id;
		$('#recordId').val(recordId);
		$("#cause").val("");
		$('#cancelDialog').dialog('open');
	} else {
		$.messager.alert('警告','请选择记录');
		return;
	}
}

function charge() {
	if ($('#chargeForm').form('validate')) {
		$('#chargeForm').form("submit", {
			url : "${ctx}/elecharge/chargerecord",
			success : function(data) {
				if (typeof data == "string") {
					var dataObj = $.parseJSON(data);  
					if (dataObj.error) {
						$.messager.alert('警告', dataObj.errorMsg);
						return;
					}
					$('#chargeDialog').dialog('close');
					$('#data_grid').datagrid('reload');
					openPrintDialog(dataObj);
				}
			}
		});
	}
}

function chaobiaoLoadSuccess(data) {
	
}

function chargeRecordOnCheck(rowIndex,rowData) {
	var rows = $('#data_grid_chargerecord').datagrid('getRows');
	var firstChargedRowIndex = -1;
	$(rows).each(function(index,row) {
		if (!row.cancelled) {
			firstChargedRowIndex = index;
			return false;
		}
	});
	// 设置取消结算按钮的状态
	// 只有最近的缴费记录才可以取消
	if (rowIndex == firstChargedRowIndex && !rowData.cancelled) {
		$("#toolbarCancelBtn").linkbutton("enable");
	} else {
		$("#toolbarCancelBtn").linkbutton("disable");
	}
	// 设置详细按钮的状态
	// 已经取消的缴费记录不可以查看详细
	if (rowData.cancelled) {
		$("#toolbarDetailBtn").linkbutton("disable");
	} else {
		$("#toolbarDetailBtn").linkbutton("enable");
	}
	
}

function filterChaobiao(data) {
	// 查看最近缴费记录，取消结算
	$('#houseAddress').val(data.house.address);
	$('#chargeDate').val(data.chargeDate);
	$('#electricUnitDesc').val(data.electricUnit.desc);
	$('#houseNo').val(data.house.no);
	$('#ownerName').val(data.houseOwner.name);
	$('#shenfenXingzhi').val(data.house.shenfenXingzhi.name);
	$('#yongfangXingzhi').val(data.house.yongfangXingzhi.name);
	$('#ownerIdCardNo').val(data.houseOwner.idCardNo);
	$('#ownerPhone').val(data.houseOwner.phoneNo);
	if (data.zhinajinOn) {
		$('#zhinajinOn').prop("checked","checked");
	} else {
		$('#zhinajinOn').removeProp("checked");
	}
	$('#electricAmountSum').val(data.electricAmountSum);
	$('#electricCostSum').val(data.electricCostSum);
	$('#zhinaSum').val(data.zhinaSum);
	$('#weishengSum').val(data.weishengSum);
	$('#paiwuSum').val(data.paiwuSum);
	$('#zhaomingSum').val(data.zhaomingSum);
	$('#mustCharge').val(data.mustCharge);
	$('#qianfei').val(data.qianfei);
	$('#previousSurplus').val(data.previousSurplus);
	
	
	var dialogType = $('#dialogType').val();
	if (dialogType == DialogTyoe_Charge) { // 缴费
		$('#zhinajinOn').removeProp("disabled");
		$("#chargeValue").numberbox("setValue", data.mustChargeToActual.replace(",", ""));
		$("#chargeValue").removeProp("readonly");
		$("#chargeValue").css("background-color", "#FFFFFF");
		$("#chargeBtn").show();
		$("#closeBtn").hide();
	} else {
		$('#zhinajinOn').prop("disabled","true");
		$("#chargeValue").numberbox("setValue", data.actualCharge);
		$("#chargeValue").css("background-color", "#F3F3FA");
		$("#chargeValue").prop("readonly", "readonly");
		$("#chargeBtn").hide();
		$("#closeBtn").show();
	}
	var chaobiaoGridData = {
			total: data.unchargedChaobiaoRecords.length,
			rows : data.unchargedChaobiaoRecords
	};
	return chaobiaoGridData;
}

function formatSurplus(value,row,index) {
	value = value.replace(",", "") * 1;
	if (value < 0) {
		return "<span style='color: red;font-weight: bold;vertical-align: middle;'>" + 
		    value +"</span>";
	}
	return value;
}

function zhinajinOnClick() {
	var zhinajinOn = $('#zhinajinOn').prop("checked");
	var dialogType = $("#dialogType").val();
	if (dialogType != DialogTyoe_Charge) {
		// 只在缴费页面点击收取滞纳金时，才有动作
		return;
	}
	var zhinajin = $("#zhinaSum").val().replace(",", "") * 1;
	if (zhinajin <= 0) {
		return;
	}
	if (zhinajinOn) { // 收取滞纳金
		var qianfei = $("#qianfei").val().replace(",", "");
		qianfei = qianfei * 1 + zhinajin * 1;
		var actualSum = formatNummber(qianfei, 0, true) + "00"; // 不收毛钱
		$("#chargeValue").numberbox("setValue", formatNummber(actualSum, 2).replace(",", ""));
	} else { // 不收滞纳金
		var qianfei = $("#qianfei").val().replace(",", "");
		var actualSum = formatNummber(qianfei, 0, true) + "00"; // 不收毛钱
		$("#chargeValue").numberbox("setValue", formatNummber(actualSum, 2).replace(",", ""));
	}
}

function loadChargeState() {
	$("#electricChargeState_q").combobox({
		valueField:'code',    
	    textField:'name',
	    data : [ 
            {
            	code: COMBOBOX_DEFAULT_SEL_VAL,
            	name: "不限"
            },
	        { code: "CHARGED",
	    		   name: g_chargestate_charged
	    	},
	    	{ code: "UNCHARGED",
	    		   name: g_chargestate_uncharged
	    	}
	    ],
	    onLoadSuccess : function(data) {
	    	$(this).combobox("setValue", data[0].code);
	    }
	});
}

function getProvReadoutsElectricValue(value, rowData, rowIndex) {
	if (rowData.chaobiaoSet.length > 0) {
		return rowData.chaobiaoSet[0].provReadoutsElectric;
	} else {
		return "";
	}
}

function getReadoutsElectricValue(value, rowData, rowIndex) {
	if (rowData.chaobiaoSet.length > 0) {
		return rowData.chaobiaoSet[rowData.chaobiaoSet.length - 1].readoutsElectric;
	} else {
		return "";
	}
}

function getElectricCountValue(value, rowData, rowIndex) {
	var electricCount = 0;
	for (var i = 0; i < rowData.chaobiaoSet.length; i++) {
		electricCount += rowData.chaobiaoSet[i].electricCount;
	}
	return electricCount;
}

</script>