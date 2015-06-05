<%@ page pageEncoding="UTF-8" %>
<script type="text/javascript">

var operInitialized = false;
var previousSelectedNode;

$(function(){
	$('#startTime_q').datetimebox('setValue', currentYmd + " 00:00:00");
	$('#endTime_q').datetimebox('setValue', currentYmd + " 23:59:59"); 
	$('#toolbarExport').linkbutton('disable');
});

function loadCaozuoyuan(node) {
	var baseCode = node.baseCode;
	var param = {baseCode:baseCode};
	// 基地没有选择时，不加载缴费年费下拉列表
	$.ajax({
		url: ctx + '/admin/UserManagement/' + baseCode + "/operators",
		type : "get",
		data : param,
		success: function(data) {
			$("#hpsUser_q").combobox({
				valueField:'id',    
			    textField:'userName',
			    data: data,
			    loadFilter : function(data) {
			    	if (data.length > 1) {
			    		data.unshift({
			    			id: COMBOBOX_DEFAULT_SEL_VAL,
			    			userName: "不限"
			    		});
			    	}
			    	return data;
			    },
			    onLoadSuccess : function(data) {
			    	// 没有查询到数据，选中默认选项
			    	if (!data || !data[0]) {
			    		data = [ { id : COMBOBOX_DEFAULT_SEL_VAL }];
			    	}
			    	$(this).combobox("setValue", data[0].id);
			    	operInitialized = true;
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
	treeNodeOnClick(firstRootNode);
}

function setQueryParams(params) {
	if (!operInitialized) {
		// 如果页面没有加载完，延迟一会儿再加载datagrid
		setTimeout(function() {
			$('#data_grid').datagrid('reload');
		}, 500);
		return false;
	}
	var selectedNode = $('#baseTree').tree("getSelected");
	params.baseCode = selectedNode.baseCode;
	if (!selectedNode.base) {
		params.areaCode = selectedNode.code;
	}
	var startChargeTime = $('#startTime_q').datetimebox('getValue');
	if (startChargeTime) {
		params.startChargeTime = startChargeTime;
	}
	var endChargeTime = $('#endTime_q').datetimebox('getValue');
	if (endChargeTime) {
		params.endChargeTime = endChargeTime;
	}
	var operUserId = $('#hpsUser_q').combobox("getValue");
	if (operUserId && operUserId != COMBOBOX_DEFAULT_SEL_VAL) {
		params.operUserId = operUserId;
	}
	return true;
}

function exportElectricReport() {
	var params = {};
	setQueryParams(params);
	if (!params.baseCode) {
		return;
	}
	var form = $("<form>");   //定义一个form表单
    form.attr('style','display:none');   //在form表单中添加查询参数
    form.attr('target','');
    form.attr('method','get');
    form.attr('action', ctx + '/report/electric');
   
    var inputBaseCode = $('<input>'); 
    inputBaseCode.attr('type','hidden'); 
    inputBaseCode.attr('name','baseCode'); 
    inputBaseCode.attr('value',params.baseCode);
    
    var inputAreaCode = $('<input>'); 
    inputAreaCode.attr('type','hidden'); 
    inputAreaCode.attr('name','areaCode'); 
    inputAreaCode.attr('value',params.areaCode);
    
    var inputStartTime = $('<input>'); 
    inputStartTime.attr('type','hidden'); 
    inputStartTime.attr('name','startChargeTime'); 
    inputStartTime.attr('value',params.startChargeTime);
    
    var inputEndTime = $('<input>'); 
    inputEndTime.attr('type','hidden'); 
    inputEndTime.attr('name','endChargeTime'); 
    inputEndTime.attr('value',params.endChargeTime);
    
    var inputOperUserId = $('<input>'); 
    inputOperUserId.attr('type','hidden'); 
    inputOperUserId.attr('name','operUserId'); 
    inputOperUserId.attr('value',params.operUserId);
   
    $('body').append(form);  //将表单放置在web中
    form.append(inputBaseCode);
    form.append(inputAreaCode);
    form.append(inputStartTime);
    form.append(inputEndTime);
    form.append(inputOperUserId);
    form.submit();   //表单提交
}

function searchTongjiInfo() {
	$('#data_grid').datagrid('reload');
}

function userTongjiRowOnCheck() {
	//$("#toolbarSearchChargeRecord").linkbutton("enable");
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

function treeNodeOnClick(node) {
	if (previousSelectedNode && node.baseCode == previousSelectedNode.baseCode) {
		// 基地没有改变，不需要重新load页面combobox
		if (node.id == previousSelectedNode.id) {
			// 节点没有变化，不做处理
		} else {
			searchTongjiInfo();
		}
	} else {
		loadCaozuoyuan(node);
		//loadPaymentDateCombobox(node);
		if (previousSelectedNode) {
			// 节点切换时
			searchTongjiInfo();
		}
	}
	previousSelectedNode = node;
}

function openChargeRecordsDialog() {
	$('#data_grid_chargerecord').datagrid("options").url = ctx + "/elecharge/operchargerecords";
	$('#chargeRecordDialog').dialog('open');
	$('#data_grid_chargerecord').datagrid("reload");
	
}

function setChargeRecordsQueryParams(params) {
	var selectedNode = $('#baseTree').tree("getSelected");
	if (!selectedNode) {
		return false;
	}
	params.baseCode = selectedNode.baseCode;
	if (!selectedNode.base) {
		params.areaCode = selectedNode.code;
	}
	var startChargeTime = $('#startTime_q').datetimebox('getValue');
	if (startChargeTime) {
		params.startChargeTime = startChargeTime;
	}
	var endChargeTime = $('#endTime_q').datetimebox('getValue');
	if (endChargeTime) {
		params.endChargeTime = endChargeTime;
	}
	var selectedRow = $('#data_grid').datagrid('getSelected');
	params.operUserId = selectedRow.operUser.id;
	$("#toolbarDetailBtn").linkbutton("disable");
	return true;
}

function chargeRecordOnCheck(rowIndex,rowData) {
	// 设置详细按钮的状态
	// 已经取消的缴费记录不可以查看详细
	if (rowData.cancelled) {
		$("#toolbarDetailBtn").linkbutton("disable");
	} else {
		$("#toolbarDetailBtn").linkbutton("enable");
	}
	
}

function openDetailDialog() {
	
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
	$('#data_grid_chaobiao').datagrid("options").url = ctx + "/elecharge/" + chargeRecordId + "/chargerecordstate";
	$('#chargeDialog').dialog('open');
	$('#data_grid_chaobiao').datagrid("reload");
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
	$('#previousSurplus').val(data.previousSurplus);
	$('#zhinajinOn').prop("disabled","true");
	$("#chargeValue").numberbox("setValue", data.actualCharge);
	$("#chargeValue").css("background-color", "#F3F3FA");
	$("#chargeValue").prop("readonly", "readonly");
	var chaobiaoGridData = {
			total: data.unchargedChaobiaoRecords.length,
			rows : data.unchargedChaobiaoRecords
	};
	return chaobiaoGridData;
}

</script>

