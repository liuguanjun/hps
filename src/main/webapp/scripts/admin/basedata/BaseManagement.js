<%@ page pageEncoding="UTF-8" %>
<script type="text/javascript">
$(function(){
	$('#newAreaDialogForm').form({
		url : "${ctx}/admin/BaseManagement/area",
		success : function(data) {
			if (typeof data == "string") {
				var dataObj = $.parseJSON(data);  
				if (dataObj.error) {
					$.messager.alert('警告', dataObj.errorMsg);
					return;
				}
			}
			$('#newAreaDialog').dialog('close');
			$('#tree_grid').treegrid('reload');
		}
	});
});

function deleteBaseTreeElement(areaId) {
	$.messager.confirm('确认','您确认想要删除记录吗？',function(r){
	    if (r){    
	    	$.ajax({
	    		url: "${ctx}/admin/BaseManagement/baseTreeElements/" + areaId, 
	    		data: { _method : "delete" },
	    		type : "post",
	    		success: function(data) {
	    			$('#tree_grid').treegrid('reload');
	    		}});
	    }
	});  
}

function getOperHTML(value,row,index){
    if (!row.base) {
    	return '<input type="button" value="删除" onclick="deleteBaseTreeElement(' + value + ');"></input>';
    } else {
    	return '<input type="button" value="添加区域" style="margin-right: 3px;" onclick="openNewAreaDialog(' + value + ')"></input>';
    }
}

function openNewAreaDialog(value) {
	$('#newAreaDialogForm #code').val("");
	$('#newAreaDialogForm #name').val("");
	$("#base_id").val(value);
	$('#newAreaDialog').dialog('open');
}

function submitNewAreaDialogForm() {
	if ($('#newAreaDialogForm').form('validate')) {
		$('#newAreaDialogForm').submit();
	}
}
</script>
