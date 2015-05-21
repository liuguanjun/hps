<%@ page pageEncoding="UTF-8" %>
<script type="text/javascript">
$(function(){
	$('#newUserDialogForm').form({
		url : "${ctx}/admin/UserManagement/user",
		success : function(data) {
			if (typeof data == "string") {
				var dataObj = $.parseJSON(data);  
				if (dataObj.error) {
					$.messager.alert('警告', dataObj.errorMsg);
					return;
				}
			}
			$('#newUserDialog').dialog('close');
			$('#data_grid').datagrid('reload');
		}
	});
});

function deleteUser(userId) {
	if (!userId) {
		var selectedRow = $('#data_grid').datagrid('getSelected');
		if (selectedRow) {
			userId = selectedRow.id;
		} else {
			$.messager.alert('警告','请选择要删除的用户');
			return;
		}
	}
	$.messager.confirm('确认','您确认想要删除记录吗？',function(r){
	    if (r){    
	    	$.ajax({
	    		url: "${ctx}/admin/UserManagement/user/" + userId, 
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

function submitNewUserDialogForm() {
	if ($('#newUserDialogForm').form('validate')) {
		$('#newUserDialogForm').submit();
	}
}

function submitUpdateUserDialogForm() {
	if ($('#updateUserDialogForm').form('validate')) {
		var userId = $('#updateUserDialogForm #id').val();
		$('#updateUserDialogForm').form("submit", {
			url : "${ctx}/admin/UserManagement/user/" + userId,
			success : function(data) {
				if (typeof data == "string") {
					var dataObj = $.parseJSON(data);  
					if (dataObj.error) {
						$.messager.alert('警告', dataObj.errorMsg);
						return;
					}
				}
				$('#updateUserDialog').dialog('close');
				$('#data_grid').datagrid('reload');
			}
		});
	}
}

function getOperHTML(value,row,index){
	return '<input type="button" value="编辑" style="margin-right: 3px;"' +
		'onclick="openUpdateUserDialog(' + value + ');"></input>' +
	   '<input type="button" value="删除" onclick="deleteUser(' + value + ');"></input>';
}

function openNewUserDialog() {
	$('#newUserDialogForm #accountName').val("");
	$('#newUserDialogForm #password').val("");
	$('#newUserDialogForm #passwordConfirm').val("");
	$('#newUserDialogForm #userName').val("");
	$('#newUserDialogForm #mobilePhoneNo').val("");
	$('#newUserDialog').dialog('open');
}

function openUpdateUserDialog(userId) {
	if (!userId) {
		var selectedRow = $('#data_grid').datagrid('getSelected');
		if (selectedRow) {
			userId = selectedRow.id;
		} else {
			$.messager.alert('警告','请选择要编辑的用户');
			return;
		}
	}
	$.ajax({
		url: "${ctx}/admin/UserManagement/user/" + userId, 
		success: function(data) {
			$('#updateUserDialogForm #id').val(data.id);
			$('#updateUserDialogForm #accountName').val(data.accountName);
			$('#updateUserDialogForm #password').val(data.password);
			$('#updateUserDialogForm #passwordConfirm').val(data.password);
			$('#updateUserDialogForm #base').combobox("select", data.base.code);
			$('#updateUserDialogForm #type').combobox("select", data.type);
			$('#updateUserDialogForm #sex').combobox("select", data.sex);
			$('#updateUserDialogForm #userName').val(data.userName);
			$('#updateUserDialogForm #mobilePhoneNo').val(data.mobilePhoneNo);
			$('#updateUserDialog').dialog('open');
		}});
}

function setQueryParams(params) {
	if (!commonItemsInitialized()) {
		// 如果页面没有加载完，延迟一会儿再加载datagrid
		setTimeout(function() {
			$('#data_grid').datagrid('reload');
		}, 500);
		return false;
	}
	params.accountName = $('#accountName_q').val();
	if ($('#base_q').combobox("getValue") != COMBOBOX_DEFAULT_SEL_VAL) {
		params.baseCode = $('#base_q').combobox("getValue");
	}
	if ($('#type_q').combobox("getValue") != COMBOBOX_DEFAULT_SEL_VAL) {
		params.typeCode = $('#type_q').combobox("getValue");
	}
	if ($('#sex_q').combobox("getValue") != COMBOBOX_DEFAULT_SEL_VAL) {
		params.sexCode = $('#sex_q').combobox("getValue");
	}
	params.userName = $('#userName_q').val();
	params.mobilePhoneNo = $('#mobilePhoneNo_q').val();
}

function clearUserQuery() {
	$('#accountName_q').val("");
	$('#base_q').combobox("select", COMBOBOX_DEFAULT_SEL_VAL);
	$('#type_q').combobox("select", COMBOBOX_DEFAULT_SEL_VAL);
	$('#sex_q').combobox("select", COMBOBOX_DEFAULT_SEL_VAL);
	$('#userName_q').val("");
	$('#mobilePhoneNo_q').val("");
}


</script>

