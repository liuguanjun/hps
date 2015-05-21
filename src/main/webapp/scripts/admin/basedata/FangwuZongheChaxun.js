<%@ page pageEncoding="UTF-8" %>
<script type="text/javascript">
var formActionUrl = "";
$(function(){
//	$('#houseForm').form({
		//url : "${ctx}/admin/basedata/house",
//		success : function(data) {
//			if (typeof data == "string") {
//				var dataObj = $.parseJSON(data);  
//				if (dataObj.error) {
//					$.messager.alert('警告', dataObj.errorMsg);
//					return;
//				}
//			}
//			$('#houseDialog').dialog('close');
//			$('#data_grid').datagrid('reload');
//		}
//	});
//	$("#ownerName").blur(loadOwnerInfo);
//	$("#ownerIdCardNo").blur(loadOwnerInfo);
});

//function loadOwnerInfo() {
//	var idCardNo = $("#ownerIdCardNo").val();
//	var name = $("#ownerName").val();
//	if (!idCardNo && !name) {
//		// 如果名称和身份证号都没有输入
//		return;
//	}
//	$.ajax({
//		url: "${ctx}/admin/basedata/houseowners", 
//		data: { name : name,  idCardNo : idCardNo},
//		type : "get",
//		success: function(data) {
//			if (typeof data == "string") {
//				var dataObj = $.parseJSON(data);  
//				if (dataObj.error) {
//					$.messager.alert('警告', dataObj.errorMsg);
//					return;
//				}
//			}
//			if (data.length == 1) { // 如果只获取到一个房主的数据，则将其数据赋值到页面上
//				$("#ownerIdCardNo").val(data[0].idCardNo);
//				$("#ownerName").val(data[0].name);
//				$("#ownerNo").val(data[0].no);
////				$('#shenfenXingzhi').combobox("select", data[0].shenfenXingzhi.code);
//				$("#ownerNoRow").css("display", "");
//				$("#ownerId").val(data.id);
//			} else {
//				$("#ownerNo").val("");
//				// 如果查询不到户主信息，认为是新建
//				// 如果查询出多个户主信息，也认为是新建，在保存时，如果根据输入（户主名与身份证号）不能唯一确定一个房主
//				// 会提示错误
//				$("#ownerId").val("");
//			}
//		}});
//}

function setQueryParams(params) {
	// data_grid加载的时候，可能页面的combobox还没有加载完成，所以可能会出现异常
	// 此处忽略发生的异常
	try {
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
		params.houseNo = $('#houseNo_q').val();
		params.ownerNo = $('#ownerNo_q').val();
		params.ownerName = $('#ownerName_q').val();
		if ($('#shenfenXingzhi_q').combobox("getValue") != COMBOBOX_DEFAULT_SEL_VAL) {
			params.shenfenXingzhiCode = $('#shenfenXingzhi_q').combobox("getValue");
		}
		params.remarks = $('#remarks_q').val();
		if ($('#yongfangXingzhi_q').combobox("getValue") != COMBOBOX_DEFAULT_SEL_VAL) {
			params.yongfangXingzhiCode = $('#yongfangXingzhi_q').combobox("getValue");
		}
	} catch (e) {
	}

}

function openNewDialog() {
	$("#id").val("");
	$("#method").val("post");
	$("#ownerName").val("");
	$("#ownerIdCardNo").val("");
	$("#ownerPhoneNo").val("");
	$("#ownerId").val("");
	$("#ownerNo").val("");
	$("#ownerNoRow").css("display", "none");
	$("#no").val("");
	$("#noRow").css("display", "none");
	$("#danyuan").numberbox("setValue", "");
	$("#danyuan").removeProp("readonly");
	$("#danyuan").css("background-color", "#FFFFFF");
	$("#ceng").numberbox("setValue", "");
	$("#ceng").removeProp("readonly");
	$("#ceng").css("background-color", "#FFFFFF");
	$("#doorNo").val("");
	$("#doorNo").removeProp("readonly");
	$("#doorNo").css("background-color", "#FFFFFF");
	$("#gongshangNo").val("");
	$("#gongshangNo").removeProp("readonly");
	$("#gongshangNo").css("background-color", "#FFFFFF");
	$("#warmArea").numberbox("setValue", "");
	$("#warmArea").removeProp("readonly");
	$("#warmArea").css("background-color", "#FFFFFF");
	$("#repairArea").numberbox("setValue", "");
	$("#repairArea").removeProp("readonly");
	$("#repairArea").css("background-color", "#FFFFFF");
	$("#wageNum").val("");
	$("#remarks").val("");
	$('#houseForm').form('validate');
	$('#houseDialog').dialog('setTitle', "新建房屋");
	$('#shenfenXingzhi').combobox("enable");
	$('#baseCode').combobox("enable");
	$('#areaCode').combobox("enable");
	$('#yongfangXingzhi').combobox("enable");
	$('#louzuo').combobox("enable");
	formActionUrl = "${ctx}/admin/basedata/house";
	$('#houseDialog').dialog('open');
}

function openChangeDialog(houseId) {
	if (!houseId) {
		var selectedRow = $('#data_grid').datagrid('getSelected');
		if (selectedRow) {
			houseId = selectedRow.id;
		} else {
			$.messager.alert('警告','请选择要编辑的房屋');
			return;
		}
	}
	$.ajax({
		url: "${ctx}/admin/basedata/house/" + houseId, 
		success: function(data) {
			$("#id").val(data.id);
			$("#ownerId").val(""); // 更名时，不设置ownerId，假定是新的房主
			$("#method").val("put");
			$("#ownerName").val(data.owner.name);
			$("#ownerIdCardNo").val(data.owner.idCardNo);
			$("#ownerPhoneNo").val(data.owner.phoneNo);
			// 户主编号
			$("#ownerNo").val(data.owner.no);
			$("#ownerNoRow").css("display", "");
			$("#wageNum").val(data.owner.wageNum);
			$("#remarks").val(data.owner.remarks);
			// 房号
			$("#no").val(data.no);
			$("#noRow").css("display", "");
			$("#danyuan").numberbox("setValue", data.danyuan);
			$("#danyuan").prop("readonly", "readonly");
			$("#danyuan").css("background-color", "#F3F3FA");
			$("#ceng").numberbox("setValue", data.ceng);
			$("#ceng").prop("readonly", "readonly");
			$("#ceng").css("background-color", "#F3F3FA");
			$("#doorNo").val(data.doorNo);
			$("#doorNo").prop("readonly");
			$("#doorNo").css("background-color", "#F3F3FA");
			$("#gongshangNo").val(data.gongshangNo);
			$("#gongshangNo").prop("readonly");
			$("#gongshangNo").css("background-color", "#F3F3FA");
			$("#warmArea").numberbox("setValue", data.warmArea);
			$("#warmArea").prop("readonly");
			$("#warmArea").css("background-color", "#F3F3FA");
			$("#repairArea").numberbox("setValue", data.repairArea);
			$("#repairArea").prop("readonly");
			$("#repairArea").css("background-color", "#F3F3FA");
			$('#shenfenXingzhi').combobox("select", data.shenfenXingzhi.code);
//			$('#shenfenXingzhi').combobox("disable");
			$('#baseCode').combobox("select", data.baseCode);
			$('#baseCode').combobox("disable");
			$('#areaCode').combobox("select", data.louzuo.area.code);
			$('#areaCode').combobox("disable");
			setLouzuoCode(data.louzuo.code, true);
			$('#yongfangXingzhi').combobox("select", data.yongfangXingzhi.code);
			$('#yongfangXingzhi').combobox("disable");
			$('#houseForm').form('validate');
			$('#houseDialog').dialog('setTitle', "房屋更名");
			formActionUrl = "${ctx}/admin/basedata/changeHouse";
			$('#houseDialog').dialog('open');
		}});
}

function openUpdateDialog(houseId) {
	if (!houseId) {
		var selectedRow = $('#data_grid').datagrid('getSelected');
		if (selectedRow) {
			houseId = selectedRow.id;
		} else {
			$.messager.alert('警告','请选择要编辑的房屋');
			return;
		}
	}
	$.ajax({
		url: "${ctx}/admin/basedata/house/" + houseId, 
		success: function(data) {
			$("#id").val(data.id);
			$("#ownerId").val(data.owner.id);
			$("#method").val("put");
			$("#ownerName").val(data.owner.name);
			$("#ownerIdCardNo").val(data.owner.idCardNo);
			$("#ownerPhoneNo").val(data.owner.phoneNo);
			// 户主编号
			$("#ownerNo").val(data.owner.no);
			$("#ownerNoRow").css("display", "");
			$("#wageNum").val(data.owner.wageNum);
			$("#remarks").val(data.owner.remarks);
			// 房号
			$("#no").val(data.no);
			$("#noRow").css("display", "");
			$("#danyuan").numberbox("setValue", data.danyuan);
			$("#danyuan").removeProp("readonly");
			$("#danyuan").css("background-color", "#FFFFFF");
			$("#ceng").numberbox("setValue", data.ceng);
			$("#ceng").removeProp("readonly");
			$("#ceng").css("background-color", "#FFFFFF");
			$("#doorNo").val(data.doorNo);
			$("#doorNo").removeProp("readonly");
			$("#doorNo").css("background-color", "#FFFFFF");
			$("#gongshangNo").val(data.gongshangNo);
			$("#gongshangNo").removeProp("readonly");
			$("#gongshangNo").css("background-color", "#FFFFFF");
			$("#warmArea").numberbox("setValue", data.warmArea);
			$("#warmArea").removeProp("readonly");
			$("#warmArea").css("background-color", "#FFFFFF");
			$("#repairArea").numberbox("setValue", data.repairArea);
			$("#repairArea").removeProp("readonly");
			$("#repairArea").css("background-color", "#FFFFFF");
			$('#shenfenXingzhi').combobox("select", data.shenfenXingzhi.code);
			$('#shenfenXingzhi').combobox("enable");
			$('#baseCode').combobox("select", data.baseCode);
			$('#baseCode').combobox("enable");
			$('#areaCode').combobox("select", data.louzuo.area.code);
			$('#areaCode').combobox("enable");
			setLouzuoCode(data.louzuo.code, false);
			$('#yongfangXingzhi').combobox("select", data.yongfangXingzhi.code);
			$('#yongfangXingzhi').combobox("enable");
			$('#houseForm').form('validate');
			$('#houseDialog').dialog('setTitle', "编辑房屋");
			formActionUrl = "${ctx}/admin/basedata/house";
			$('#houseDialog').dialog('open');
		}});
}

function setLouzuoCode(code, disabled) {
	if (!g_louzuoInitialized) {
		setTimeout(function() { setLouzuoCode(code, disabled); }, 500);
		return;
	}
	$('#louzuo').combobox("select", code);
	if (disabled) {
		$('#louzuo').combobox("disable");
	} else {
		$('#louzuo').combobox("enable");
	}
}

function submitHouseForm() {
	if ($('#houseForm').form('validate')) {
		if ($('#louzuo').combobox("getValue")) {
			$('#houseForm').form("submit", {url : formActionUrl,
				success : function(data) {
					if (typeof data == "string") {
						var dataObj = $.parseJSON(data);  
						if (dataObj.error) {
							$.messager.alert('警告', dataObj.errorMsg);
							return;
						}
					}
					$('#houseDialog').dialog('close');
					$('#data_grid').datagrid('reload');
				}
			});
		} else {
			$.messager.alert('警告', "请选择楼座");
		}
	}
}

function clearHouseQuery() {
	$('#base_q').combobox("select", COMBOBOX_DEFAULT_SEL_VAL);
	$('#area_q').combobox("select", COMBOBOX_DEFAULT_SEL_VAL);
	$('#louzuo_q').combobox("select", COMBOBOX_DEFAULT_SEL_VAL);
	$('#danyuan_q').val("");
	$('#louceng_q').val("");
	$('#ownerName_q').val("");
	$('#ownerNo_q').val("");
	$('#houseNo_q').val("");
	$('#shenfenXingzhi_q').combobox("select", COMBOBOX_DEFAULT_SEL_VAL);
	$('#remarks_q').val("");
	$('#yongfangXingzhi_q').combobox("select", COMBOBOX_DEFAULT_SEL_VAL);
}

function deleteHouse(houseId) {
	if (!houseId) {
		var selectedRows = $('#data_grid').datagrid('getSelections');
		if (selectedRows.length > 0) {
			houseId = "";
			$(selectedRows).each(function(index, element) {
				if (index == 0) {
					houseId = element.id;
				} else {
					houseId = houseId + "," +  element.id;
				}
			});
		} else {
			$.messager.alert('警告','请选择要删除的房屋');
			return;
		}
	}
	$.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
	    if (r){    
	    	$.ajax({
	    		url: "${ctx}/admin/basedata/house/" + houseId, 
	    		data: { _method : "delete" },
	    		type : "post",
	    		success: function(data) {
	    			if (typeof data == "string") {
	    				var dataObj = $.parseJSON(data);  
	    				if (dataObj.error) {
	    					$.messager.alert('警告', dataObj.errorMsg);
	    					return;
	    				}
	    			}
	    			$('#data_grid').datagrid('reload');
	    		}});
	    }
	}); 
}

//function getOperHTML(value,row,index){
//	return '<input type="button" value="编辑" style="margin-right: 3px;"' +
//		'onclick="openUpdateDialog(' + value + ');"></input>' +
//	   '<input type="button" value="删除" onclick="deleteHouse(' + value + ');"></input>';
//}

</script>