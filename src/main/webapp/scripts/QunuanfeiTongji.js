<%@ page pageEncoding="UTF-8" %>
<script type="text/javascript">
$(function(){
	$('#toolbarExport').linkbutton('disable');
});
function loadHeatingPaymentDatesCombobox(node) {
	var baseCode = node.code;
	var param = {baseCode:baseCode};
	// 基地没有选择时，不加载缴费年费下拉列表
	$.ajax({
		url: ctx + '/heatingcharge/' + baseCode + '/paymentdates',
		type : "get",
		data : param,
		success: function(data) {
			$("#paymentDateId_q").combobox({
				valueField:'id',    
			    textField:'title',
			    data: eval(JSON.stringify(data)),
			    loadFilter : function(data) {
			    	var hasEmpty = $(this).attr("hasEmpty");
			    	if (hasEmpty == "true" && data.length > 1) {
			    		data.unshift({
			    			id: COMBOBOX_DEFAULT_SEL_VAL,
			    			title: "不限"
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
			    }
			});
		}
	});
}
function loadCaozuoyuan(node) {
	var baseCode = node.code;
	var param = {baseCode:baseCode};
	// 基地没有选择时，不加载缴费年费下拉列表
	$.ajax({
		url: ctx + '/getQunuanfeiTongji/caozuoyuan/' + baseCode,
		type : "get",
		data : param,
		success: function(data) {
			$("#hpsUser_q").combobox({
				valueField:'id',    
			    textField:'userName',
			    data: data,
			    loadFilter : function(data) {
			    	var hasEmpty = $(this).attr("hasEmpty");
			    	if (hasEmpty == "true" && data.length >= 1) {
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
			    }
			});
		}
	});
}

function exportHeatingReport() {
	var params = {};
	setQueryParams(params);
	if (!params.paymentDateId) {
		return;
	}
//	$.ajax({
//		url: ctx + '/report/heating',
//		type : "get",
//		data : params
//	});
	var form = $("<form>");   //定义一个form表单
    form.attr('style','display:none');   //在form表单中添加查询参数
    form.attr('target','');
    form.attr('method','get');
    form.attr('action', ctx + '/report/heating');
   
    var inputPaymentDate = $('<input>'); 
    inputPaymentDate.attr('type','hidden'); 
    inputPaymentDate.attr('name','paymentDateId'); 
    inputPaymentDate.attr('value',params.paymentDateId);
    
    var inputStartDate = $('<input>'); 
    inputStartDate.attr('type','hidden'); 
    inputStartDate.attr('name','starDate'); 
    inputStartDate.attr('value',params.starDate);
    
    var inputEndDate = $('<input>'); 
    inputEndDate.attr('type','hidden'); 
    inputEndDate.attr('name','endDate'); 
    inputEndDate.attr('value',params.endDate);
    
    var inputOperUserId = $('<input>'); 
    inputOperUserId.attr('type','hidden'); 
    inputOperUserId.attr('name','operUserId'); 
    inputOperUserId.attr('value',params.operUserId);
   
    $('body').append(form);  //将表单放置在web中
    form.append(inputPaymentDate);   //将查询参数控件提交到表单上
    form.append(inputStartDate);
    form.append(inputEndDate);
    form.append(inputOperUserId);
    form.submit();   //表单提交
}

function formatTreeNode(node) {
	return node.name;
}

function selectFirstNode(node, data) {
	var firstRootNode = $('#baseTree').tree("getRoot");
	$('#baseTree').tree("select", firstRootNode.target);
	loadCaozuoyuan(firstRootNode);
	loadHeatingPaymentDatesCombobox(firstRootNode);
}

function setQueryParams(params) {
	// data_grid加载的时候，可能页面的combobox还没有加载完成，所以可能会出现异常
	// 此处忽略发生的异常
	try {
		if ($('#paymentDateId_q').combobox("getValue") != COMBOBOX_DEFAULT_SEL_VAL) {
			params.paymentDateId = $('#paymentDateId_q').combobox("getValue");
		}
		if ($('#hpsUser_q').combobox("getValue") != COMBOBOX_DEFAULT_SEL_VAL) {
			params.operUserId = $('#hpsUser_q').combobox("getValue");
		}
		params.starDate = $('#startDate').datebox('getValue');
		params.endDate = $('#endDate').datebox('getValue');
		var selectedNode = $('#baseTree').tree("getSelected");
		params.baseId = selectedNode.id;
	} catch (e) {
	}
}

function searchTongjiInfo() {
	$('#data_grid').datagrid('options').url='${ctx}/getQunuanfeiTongji/qunuanfeiTongji/{baseCode}';
	$('#data_grid').datagrid('reload');
}
function loadCombobox(node) {
	loadCaozuoyuan(node);
	loadHeatingPaymentDatesCombobox(node);
}
</script>

