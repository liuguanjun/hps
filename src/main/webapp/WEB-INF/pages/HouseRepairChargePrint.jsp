<%@page pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<jsp:useBean id="now" class="java.util.Date" /> 
<head>
	<meta charset="UTF-8">
	<title>打印房屋维修费收据</title>
	<t:assets/>
</head>
<script type="text/javascript">

$(function(){
	var recordId = window.dialogArguments;
	$.ajax({
		url: "${ctx}/maintaincharge/chargerecord/" + recordId, 
		success: function(data) {
			$("#title").text(data.paymentDate.title);
			$("#houseNo").text(data.house.no);
			$('#houseOwnerName').text(data.houseOwner.name);
			$('#houseAddress').text(data.house.shortAddress);
			$('#repairArea').text(data.house.repairArea + "平米");
			$('#unit').text(data.unit.unit + "元/每月每平米");
			$('#divertedCharge').text(data.divertedCharge + "元");
			$('#mustCharge').text(data.mustCharge + "元");
			$('#actualCharge').text(data.actualCharge + "元");
			// 大写金额
			$("#mustChargeUpperCase").text(atoc(data.actualCharge.replace(",", "")));
			$("#operUserName").text(data.operUser.userName);
		}});
});


function printMaintainCharge() {
	$("#printControls").hide();
	window.print();
// 	$("#printControls").show();
	closeMaintainCharge();
}

function closeMaintainCharge() {
	window.close();
}

</script>
<body>
	<div id="printMask"
		style="z-index: 100; position: absolute; width: 100%; height: 100%; left: 0px; top: 0px; filter: alpha(Opacity = 90); -moz-opacity: 0.9; opacity: 0.9; background-color: #fff;"></div>
	<div id="printContent"
		style="z-index: 101; position: absolute; left: 0px; top: 0px; width: 100%; height: 100%; font-family: SimSun; font-weight: bold;text-align: center;">
		<div
			style="font-size: 5.5mm; color: black; text-align: center; margin-top: 0.0cm;">
			水电一局基地管理处<span id="title"></span>收据
		</div>
		<div
			style="width: 100%; font-size: 11.2pt; text-align: center; margin: 0.2cm 0px;white-space: nowrap;">
			房号：<div style="width: 2cm;display: inline;text-align: left;" id="houseNo"></div>
			户名：<div style="width: 2cm;display: inline;text-align: left;" id="houseOwnerName"></div>
			地址：<div style="width: 7.2cm;display: inline;text-align: left;" id="houseAddress"></div>
		</div>
		<div
			style="width: 14cm; font-size: 11.2pt; height: 4.5cm; border: solid 1px black; margin: 0px auto;">
			<table style="width: 100%;font-weight: bold;color: black;margin-top: 0.5cm;" cellspacing="0" border="0">
				<tbody>
					<tr style="height: 25px;">
						<td>维修面积:</td>
						<td id="repairArea"></td>
						<td>单价:</td>
						<td id="unit"></td>
					</tr>
					<tr style="height: 25px;">
						<td>历年结转:</td>
						<td id="divertedCharge"></td>
						<td>应收维修费:</td>
						<td id="mustCharge"></td>
					</tr>
					<tr style="height: 25px;">
						<td>实收维修费:</td>
						<td id="actualCharge"></td>
						<td></td>
						<td></td>
					</tr>
					<tr style="height: 25px;">
						<td>金额:</td>
						<td id="mustChargeUpperCase" colspan="3"></td>
					</tr>
				</tbody>
			</table>
		</div>
		<div
			style="width: 100%; text-align: center; font-size: 11.2pt; margin-top: 5px;">
			开具时间：
			<fmt:formatDate value="${now}" type="both" dateStyle="long"
				pattern="yyyy-MM-dd HH:mm:ss" />
			&nbsp;&nbsp;&nbsp;收款单位:（未盖章无效）&nbsp;&nbsp;收款人：
			<div style="display: inline; width: 2cm;" id="operUserName"></div>
		</div>
		<div id="printControls" style="text-align: center; margin-top: 2cm;">
			<input value="打印" onclick="printMaintainCharge();" type="button">
			<input value="返回" onclick="closeMaintainCharge();" type="button">
		</div>
	</div>
</body>
</body>