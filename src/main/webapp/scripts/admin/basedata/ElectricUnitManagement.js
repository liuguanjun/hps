<%@ page pageEncoding="UTF-8" %>
<script type="text/javascript">
$(function(){
	$('#unitForm').form({url : "${ctx}/admin/electricUnit/unit",
		success : function(data) {
			if (typeof data == "string") {
				var dataObj = $.parseJSON(data);  
				if (dataObj.error) {
					$.messager.alert('警告', dataObj.errorMsg);
					return;
				}
			}
			$('#unitDialog').dialog('close');
			$('#data_grid').datagrid('reload');
		}
	});
});

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
	params.doorNo = $('#doorNo_q').val();
	if ($('#yongfangXingzhi_q').combobox("getValue") != COMBOBOX_DEFAULT_SEL_VAL) {
		params.yongfangXingzhiCode = $('#yongfangXingzhi_q').combobox("getValue");
	}
	$("#toolbarEditBtn").linkbutton("disable");
	$("#toobarDelBtn").linkbutton("disable");
}

function unitRowOnCheck() {
	$("#toolbarEditBtn").linkbutton("enable");
	$("#toobarDelBtn").linkbutton("enable");
}

function openNewLevelDialog() {
	clearUnitForm();
	$("#method").val("post");
	$("#level").val("true");
	$('#unitForm').form('validate');
	$('#unitDialog').dialog('setTitle', "新建阶梯电价");
	$("#leveUnitRow1").css("display", "");
	$("#leveUnitRow2").css("display", "");
	$("#leveUnitRow3").css("display", "");
	$("#unit").numberbox("setValue", "0");
	$("#unitRow").css("display", "none");
	$('#unitDialog').dialog('open');
}

function clearUnitForm() {
	$("#id").val("");
	$('#areaCode').combobox("select", COMBOBOX_DEFAULT_SEL_VAL);
	$('#louzuoCode').combobox("select", COMBOBOX_DEFAULT_SEL_VAL);
	$("#danyuan").numberbox("setValue", "");
	$("#ceng").numberbox("setValue", "");
	$("#doorNo").val("");
	$("#start1").numberbox("setValue", "");
	$("#end1").numberbox("setValue", "");
	$("#unit1").numberbox("setValue", "");
	$("#start2").numberbox("setValue", "");
	$("#end2").numberbox("setValue", "");
	$("#unit2").numberbox("setValue", "");
	$("#start3").numberbox("setValue", "");
	$("#end3").numberbox("setValue", "");
	$("#unit3").numberbox("setValue", "");
	$("#unit").numberbox("setValue", "");
	$("#zhinaScale").numberbox("setValue", "");
	
	$("#weishengfei").numberbox("setValue", "");
	$("#paiwufei").numberbox("setValue", "");
	$("#zhaomingfei").numberbox("setValue", "");
}

function openNewNormalDialog() {
	clearUnitForm();
	$("#method").val("post");
	$("#level").val("false");
	$('#unitForm').form('validate');
	$('#unitDialog').dialog('setTitle', "新建单一电价");
	$("#start1").numberbox("setValue", "0");
	$("#unit1").numberbox("setValue", "0")
	$("#leveUnitRow1").css("display", "none");
	$("#leveUnitRow2").css("display", "none");
	$("#leveUnitRow3").css("display", "none");
	$("#unitRow").css("display", "");
	$('#unitDialog').dialog('open');
}

function openUpdateDialog() {
	var unitId;
	var selectedRow = $('#data_grid').datagrid('getSelected');
	if (selectedRow) {
		unitId = selectedRow.id;
	} else {
		$.messager.alert('警告','请选择要编辑的数据');
		return;
	}
	$.ajax({
		url: "${ctx}/admin/electricUnit/unit/" + unitId, 
		success: function(data) {
			clearUnitForm();
			$("#id").val(data.id);
			$("#method").val("put");
			$("#leveUnitRow1").css("display", data.level ? "" : "none");
			$("#leveUnitRow2").css("display", data.level ? "" : "none");
			$("#leveUnitRow3").css("display", data.level ? "" : "none");
			$("#unitRow").css("display", data.level ? "none" : "");
			if (data.level) {
				$("#start1").numberbox("setValue", data.start1);
				$("#end1").numberbox("setValue", data.end1);
				$("#unit1").numberbox("setValue", data.unit1);
				$("#start2").numberbox("setValue", data.start2);
				$("#end2").numberbox("setValue", data.end2);
				$("#unit2").numberbox("setValue", data.unit2);
				$("#start3").numberbox("setValue", data.start3);
				$("#end3").numberbox("setValue", data.end3);
				$("#unit3").numberbox("setValue", data.unit3);
				$("#unit").numberbox("setValue", "0");
				$("#level").val("true");
			} else {
				$("#unit").numberbox("setValue", data.unit);
				$("#start1").numberbox("setValue", "0");
				$("#unit1").numberbox("setValue", "0");
				$("#level").val("false");
			}
			$('#baseCode').combobox("select", data.base.code);
			if (data.area && data.area.code) {
				$('#areaCode').combobox("select", data.area.code);
			}
			if (data.louzuo && data.louzuo.code) {
				setLouzuoCode(data.louzuo.code);
			}
			$('#yongfangXingzhi').combobox("select", data.yongfangXingzhi.code);
			$("#danyuan").numberbox("setValue", data.danyuan);
			$("#ceng").numberbox("setValue", data.ceng);
			$("#doorNo").val(data.doorNo);
			$("#weishengfei").numberbox("setValue", data.weishengfei);
			$("#paiwufei").numberbox("setValue", data.paiwufei);
			$("#zhaomingfei").numberbox("setValue", data.zhaomingfei);
			$('#unitForm').form('validate');
			$('#unitDialog').dialog('setTitle', "编辑电费单价");
			$('#unitDialog').dialog('open');
		}});
}

function setLouzuoCode(code) {
	if (!g_louzuoInitialized) {
		setTimeout(function() { setLouzuoCode(code); }, 500);
		return;
	}
	$('#louzuoCode').combobox("select", code);
}

function deleteUnit(unitId) {
	var unitId;
	var selectedRow = $('#data_grid').datagrid('getSelected');
	if (selectedRow) {
		unitId = selectedRow.id;
	} else {
		$.messager.alert('警告','请选择要编辑的数据');
		return;
	}
	$.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
	    if (r){    
	    	$.ajax({
	    		url: "${ctx}/admin/electricUnit/unit/" + unitId, 
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

function getZhinaDispValue(value, rowData, rowIndex) {
	if (value) {
		return value * 1000 + "‰/日";
	} else {
		return "";
	}
}

function getLevelRangeValue1(value, rowData, rowIndex) {
	if (rowData.level) {
		var start = rowData.start1;
		var end = rowData.end1;
		var result = "";
		if (start) {
			result = start + "~";
		} else {
			return result;
		}
		if (end) {
			result += end;
		} else {
			return result;
		}
		return result;
	}
	return "";
}

function getLevelRangeValue2(value, rowData, rowIndex) {
	if (rowData.level) {
		var start = rowData.start2;
		var end = rowData.end2;
		var result = "";
		if (start) {
			result = start + "~";
		} else {
			return result;
		}
		if (end) {
			result += end;
		} else {
			return result;
		}
		return result;
	}
	return "";
}

function getLevelRangeValue3(value, rowData, rowIndex) {
	if (rowData.level) {
		var start = rowData.start3;
		var end = rowData.end3;
		var result = "";
		if (start) {
			result = start + "~";
		} else {
			return result;
		}
		if (end) {
			result += end;
		} else {
			return result;
		}
		return result;
	}
	return "";
}

function submitUnitForm() {
	if ($('#unitForm').form('validate')) {
		var yongfangXingzhi = $('#yongfangXingzhi').combobox("getValue");
		if (!yongfangXingzhi) {
			$.messager.alert('警告','请选择房屋性质');
			return;
		}
		$('#unitForm').submit();
	}
}

function clearQuery() {
	$('#base_q').combobox("select", COMBOBOX_DEFAULT_SEL_VAL);
	$('#area_q').combobox("select", COMBOBOX_DEFAULT_SEL_VAL);
	$('#louzuo_q').combobox("select", COMBOBOX_DEFAULT_SEL_VAL);
	$('#danyuan_q').val("");
	$('#louceng_q').val("");
	$("#doorNo_q").val("");
	$('#yongfangXingzhi_q').combobox("select", COMBOBOX_DEFAULT_SEL_VAL);
}

</script>

