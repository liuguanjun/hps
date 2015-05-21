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
	   if(node.id ==1) {
		   loadKouqian();
	   } else if (node.id == 2) {
		   loadHongshi();
	   } else {
		   loadBaishan();
	   }
	 }
   });
   var node = $('#areaTree').tree('find', 1);
   $('#areaTree').tree('select', node.target);
   $('#data_grid').datagrid({
	    pageSize:10,
		data: {total:16, rows :[
			{"yongfangXingzhi":"居民", "unit":"28元/平方米", isLevel:true},
			{"yongfangXingzhi":"商业", "unit":"34元/平方米", isLevel:true},
			{"yongfangXingzhi":"车库", "unit":"31元/平方米", isLevel:true},
			{"yongfangXingzhi":"经济性用房", "unit":"37元/平方米", isLevel:true}
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

function loadKouqian() {
	   $('#data_grid').datagrid({
		    pageSize:10,
			data: {total:16, rows :[
				{"yongfangXingzhi":"居民", "unit":"28元/平方米", isLevel:true},
				{"yongfangXingzhi":"商业", "unit":"34元/平方米", isLevel:true},
				{"yongfangXingzhi":"车库", "unit":"31元/平方米", isLevel:true},
				{"yongfangXingzhi":"经济性用房", "unit":"37元/平方米", isLevel:true}
			]},
			toolbar: [{
				iconCls: 'icon-add',
				text:"添加",
				handler:function(){$('#newUserDialog0').dialog('open');}
			}]
		});
	   $("#comment").css("display", "block");
}

function loadHongshi() {
	   $('#data_grid').datagrid({
		    pageSize:10,
			data: {total:16, rows :[
				{"yongfangXingzhi":"居民", "unit":"27元/平方米", isLevel:true},
				{"yongfangXingzhi":"商业", "unit":"32元/平方米", isLevel:true}
			]},
			toolbar: [{
				iconCls: 'icon-add',
				text:"添加",
				handler:function(){$('#newUserDialog0').dialog('open');}
			}]
		});
	   $("#comment").css("display", "none");
}

function loadBaishan() {
	   $('#data_grid').datagrid({
		    pageSize:10,
			data: {total:16, rows :[
				{"yongfangXingzhi":"居民", "unit":"27元/平方米", isLevel:true},
				{"yongfangXingzhi":"商业", "unit":"32元/平方米", isLevel:true}
			]},
			toolbar: [{
				iconCls: 'icon-add',
				text:"添加",
				handler:function(){$('#newUserDialog0').dialog('open');}
			}]
		});
	   $("#comment").css("display", "none");
}

function getOperHTML0(value,row,index){
	//			return '<a href="#" class="easyui-linkbutton" style="margin-right: 3px;">编辑</a>' + 
	//			   '<a href="#" class="easyui-linkbutton">删除</a>';
			return '<input type="button" value="编辑" style="margin-right: 3px;" onclick="$(\'#newUserDialog0\').dialog(\'open\');"></input>' +
			    '<input type="button" value="删除" onclick="deleteUser();"></input>';
}
</script>

