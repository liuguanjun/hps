<%@ page pageEncoding="UTF-8" %>
<script type="text/javascript">
$(function(){
	$('#newQunuanfeiUnitForm').form({
		url : "${ctx}/qunuanfeiUnit/addQunuanfeiUnit",
		method : "post",
		success : function(data) {
			if (typeof data == "string") {
				var dataObj = $.parseJSON(data);  
				if (dataObj.error) {
					$.messager.alert('警告', dataObj.errorMsg);
					return;
				}
			}
			$('#newQunuanfeiUnitDialog').dialog('close');
			$('#data_grid').datagrid('reload');
			$('#paymentDate_id').val($('#paymentDateId_q').combobox("getValue"));
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
			    	$('#data_grid').datagrid('reload');
			    	$('#paymentDate_id').val($('#paymentDateId_q').combobox("getValue"));
			    },
			    onChange:function(data) {
			    	$('#data_grid').datagrid('reload');
			    	$('#paymentDate_id').val($('#paymentDateId_q').combobox("getValue"));
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
		}
		
	} catch (e) {
	}

}

function getOperHTML(value,row,index){
	return '<input type="button" value="编辑" style="margin-right: 3px;" onclick="openUpdateDialog(' + row.id + ');"></input>';
}

function openNewQunuanfeiUnitDialog() {
	$('#newQunuanfeiUnitDialog #id').val("");
	$('#newQunuanfeiUnitDialog').dialog('open');
}

function submitNewQunuanfeiUnitForm() {
	if ($('#newQunuanfeiUnitDialog').form('validate')) {
		$('#newQunuanfeiUnitForm').submit();
	}
}

function openUpdateDialog(UnitId) {
	if (!UnitId) {
		var selectedRow = $('#data_grid').datagrid('getSelected');
		if (selectedRow) {
			paymentDateId = selectedRow.id;
		} else {
			$.messager.alert('警告','请选择要编辑的记录');
			return;
		}
	}
	$.ajax({
		url: "${ctx}/qunuanfeiUnit/getQunuanfeiUnitById/" + UnitId, 
		success: function(data) {
			$('#newQunuanfeiUnitDialog #id').val(data[0].id);// id
			$('#newQunuanfeiUnitDialog #unit').val(data[0].unit);// 单价
			$('#newQunuanfeiUnitDialog #yongfangXingzhi').val(data[0].yongfangXingzhi.name);// 用房性质
			$('#newQunuanfeiUnitDialog').dialog('open');
		}});
}

</script>

