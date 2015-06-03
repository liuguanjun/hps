<%@page pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<jsp:useBean id="now" class="java.util.Date" /> 
<head>
	<meta charset="UTF-8">
	<title>打印取暖改造费收据</title>
	<t:assets/>
</head>
<script type="text/javascript">

$(function(){
	var recordId = window.dialogArguments;
	$.ajax({
		url: "${ctx}/heatingmaintain2015/chargerecord/" + recordId, 
		success: function(data) {
			$("#title").text(data.paymentDate.title);
			$("#houseNo").text(data.house.no);
			$('#houseOwnerName').text(data.houseOwner.name);
			$('#houseAddress').text(data.house.shortAddress);
			$('#repairArea').text(data.house.repairArea + "平米");
			$('#unit').text(data.paymentDate.unit + "元/平米");
			$('#mustCharge').text(data.mustCharge + "元");
			$('#actualCharge').text(data.actualCharge + "元");
			// 大写金额
			$("#mustSumChargeUpperCase").text(atoc(data.actualCharge.replace(",", "")));
			$("#operUserName").text(data.operUser.userName);
		}});
});


function printHeatingCharge() {
	$("#printControls").hide();
	window.print();
	closeHeatingCharge();
}

function closeHeatingCharge() {
	window.close();
}

</script>
<body>
	<div
		style="z-index: 100; position: absolute; width: 100%; height: 100%; left: 0px; top: 0px; filter: alpha(Opacity = 90); -moz-opacity: 0.9; opacity: 0.9; background-color: #fff;"
		id="printMask"></div>
	<div
		style="z-index: 101; position: absolute; left: 0px; top: 0px; width: 100%; height: 100%; font-family: SimSun; font-weight: bold;text-align: center;"
		id="printContent">
		<div
			style="font-size: 5.5mm; color: black; text-align: center; margin-top: 0mm; padding-bottom: 5px;">
			水电一局基地管理处<span id="title"></span>收据
		</div>
		<div
			style="width: 100%; font-size: 11.2pt; text-align: center; margin: 0.2cm 0px;white-space: nowrap;">
			房号：<div style="width: 2cm;display: inline;text-align: left;" id="houseNo"></div>
			户名：<div style="width: 2cm;display: inline;text-align: left;" id="houseOwnerName"></div>
			地址：<div style="width: 7.2cm;display: inline;text-align:left;" id="houseAddress"></div>
		</div>
		<div
			style="width: 14cm; font-size: 11.2pt; height: 4.5cm; border: solid 1px black; margin: 0px auto;">
			<table cellspacing="0" border="0"
				style="width: 100%; font-size: 11.2pt;font-weight: bold;color: black;margin-top: 0.5cm;" cellspacing="0" border="0">
				<tbody>
					<tr style="height: 25px;">
						<td>维修面积:</td>
						<td id="repairArea"></td>
						<td>单价:</td>
						<td id="unit"></td>
					</tr>
					<tr style="height: 25px;">
						<td>应收金额:</td>
						<td id="mustCharge"></td>
						<td></td>
						<td></td>
					</tr>
					<tr style="height: 25px;">
						<td>实收金额:</td>
						<td id="actualCharge"></td>
						<td></td>
						<td></td>
					</tr>
					<tr style="height: 25px;">
						<td>金额:</td>
						<td id="mustSumChargeUpperCase" colspan="3"></td>
					</tr>
				</tbody>
			</table>
		</div>
		<div
			style="width: 100%; text-align: center; font-size: 11.2pt; margin-top: 5px;">
			开具时间：
			<fmt:formatDate value="${now}" type="both" dateStyle="long"
				pattern="yyyy-MM-dd HH:mm:ss" />
			&nbsp;&nbsp;&nbsp;收款单位:（未盖章无效）&nbsp;&nbsp;收款人：<div style="display: inline; width: 2cm;" id="operUserName"></div>
		</div>
		<div style="text-align: center; margin-top: 2cm;" id="printControls">
			<input type="button" onclick="printHeatingCharge();" value="打印">
			<input type="button" onclick="closeHeatingCharge();" value="取消">
		</div>
	</div>
</body>
</body>