<%@ page pageEncoding="UTF-8" %>
<script type="text/javascript">
$(function(){
   $('#areaTree').tree({
	  data:[
			{"id":1, "text":"口前基地","dataSource":"系统内置", "lastModifyTime":"","isBase":true,"iconCls":"icon-ok"},
			{"id":2, "text":"红石基地","dataSource":"系统内置", "lastModifyTime":"","isBase":true,"iconCls":"icon-ok"},
			{"id":3, "text":"白山基地","dataSource":"系统内置", "lastModifyTime":"","isBase":true,"iconCls":"icon-ok"}
		],
	 onClick: function(node){
	   if(node.id ==11 || node.id ==12) {
		   loadLvShun2();
	   } else if (node.id == 4 || node.id==13) {
		   loadLvShun1();
	   } else {
		   loadNormal();
	   }
	 }
   });
   var node = $('#areaTree').tree('find', 1);
   $('#areaTree').tree('select', node.target);
   $('#data_grid').datagrid({
	    pageSize:10,
		data: {total:16, rows :[
			{"yongfangXingzhi":"2012-11-1", "unit":"3‰/日", "weisheng":"-", "paiwu":"-", "zhaoming":"-",isLevel:true}
		]},
		toolbar: [{
			iconCls: 'icon-add',
			text:"添加",
			handler:function(){$('#newUserDialog0').dialog('open');}
		}]
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
</script>


