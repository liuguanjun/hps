<%@ page pageEncoding="UTF-8" %>
<script type="text/javascript">
$(function(){
	$('#newLouzuoDialogForm').form({
		url : "${ctx}/admin/LouzuoManagement/louzuo",
		success : function(data) {
			if (typeof data == "string") {
				var dataObj = $.parseJSON(data);  
				if (dataObj.error) {
					$.messager.alert('警告', dataObj.errorMsg);
					return;
				}
			}
			$('#newLouzuoDialog').dialog('close');
//			$('#areaTree').tree('reload');
			$('#data_grid').datagrid('reload');
		}
	});

});

function selectFirstNode(node, data) {
	var firstRootNode = $('#areaTree').tree("getRoot");
	$('#areaTree').tree("select", firstRootNode.target);
	$('#data_grid').datagrid('reload');
}

function setQueryParams(params) {
	var selectedNode = $('#areaTree').tree("getSelected");
	if (selectedNode) {
		if (selectedNode.base) { // 基地
			$('#data_grid').datagrid("options").url = ctx + "/admin/LouzuoManagement/baselouzuo/" + selectedNode.bizzDataId;
		} else { // 区域
			$('#data_grid').datagrid("options").url = ctx + "/admin/LouzuoManagement/arealouzuo/" + selectedNode.bizzDataId;
		}
		return true;
	} else {
		return false;
	}
}

function getOperHTML(value,row,index){
    return '<input type="button" value="删除" onclick="deletelouzuo(' + value + ');"></input>';
}


function deleteUser() {
	$.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
	    if (r){    
	    	//$.messager.alert('确认','删除成功！');    
	    } else {
	    	
	    }
	});  
}


function formatTreeNode(node) {
	return node.name;
}

function openNewLouzuoDialog() {
	$('#newLouzuoDialogForm #code').val("");
	$('#newLouzuoDialogForm #name').val("");
	$('#newLouzuoDialog').dialog('open');
}

function submitnewLouzuoDialogForm() {
	if ($('#newLouzuoDialogForm').form('validate')) {
		var selectedNode = $('#areaTree').tree('getSelected');
		$('#newLouzuoDialogForm #area_id').val(selectedNode.bizzDataId);
		$('#newLouzuoDialogForm #area_name').val(selectedNode.name);
		$('#newLouzuoDialogForm').submit();
	}
}

function deletelouzuo(louzuoId) {
	$.messager.confirm('确认','您确认想要删除记录吗？',function(r){
	    if (r){    
	    	$.ajax({
	    		url: "${ctx}/admin/LouzuoManagement/louzuo/" + louzuoId, 
	    		data: { _method : "delete" },
	    		type : "post",
	    		success: function(data) {
	    			$('#data_grid').datagrid('reload');
	    		}});
	    }
	});  
}
</script>

