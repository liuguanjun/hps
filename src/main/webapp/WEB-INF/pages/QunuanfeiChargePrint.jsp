<%@page pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<jsp:useBean id="now" class="java.util.Date" /> 
<head>
	<meta charset="UTF-8">
	<title>打印取暖费收据</title>
	<t:assets/>
</head>
<script type="text/javascript">

$(function(){
	var recordId = window.dialogArguments;
	$.ajax({
		url: "${ctx}/heatingcharge/chargerecord/" + recordId, 
		success: function(data) {
			$("#title").text(data.paymentDate.title);
			$("#houseNo").text(data.house.no);
			$('#houseOwnerName').text(data.houseOwner.name);
// 			$('#paymentDateTile').text(data.paymentDate.title);
			$('#houseAddress').text(data.house.shortAddress);
			$('#warmArea').text(data.house.warmArea + "平米");
			$('#unit').text(data.unit.unit + "元/平米");
			$('#normalHeatingCharge').text(data.normalHeatingCharge + "元");
			$('#preferential').text(data.preferential + "元");
			$('#preferentialDesc').text(data.preferentialDesc);
			$('#divertedCharge').text(data.divertedCharge + "元");
			$('#stopped').text(data.stopped ? "是" : "否");
// 			var stopHeatingRate = data.paymentDate.stopHeatingRate;
// 			if (stopHeatingRate) {
// 				$('#stopHeatingRate').text(stopHeatingRate * 100 + "%");
// 			}
			$('#expiredDays').text(data.expiredDays);
			$('#zhinajinRate').text(data.paymentDate.zhinajinRate * 100 + "‰/日");
			$('#zhinajin').text(data.mustZhinajin + "元" + ((!data.zhinajinOn && data.mustZhinajin > 0) ? "(免)" : ""));
			$('#mustSumCharge').text(data.mustSumCharge + "元");
			$('#actualSumCharge').text(data.actualSumCharge + "元");
			// 大写金额
			$("#mustSumChargeUpperCase").text(atoc(data.actualSumCharge.replace(",", "")));
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
				style="width: 100%; font-size: 11.2pt;font-weight: bold;color: black;">
				<tbody>
					<tr style="height: 25px;">
						<td>采暖面积:</td>
						<td id="warmArea"></td>
						<td>单价:</td>
						<td id="unit"></td>
						<td>标收热费:</td>
						<td id="normalHeatingCharge"></td>
					</tr>
					<tr style="height: 25px;">
						<td>优惠金额:</td>
						<td id="preferential"></td>
						<td>优惠项:</td>
						<td id="preferentialDesc" colspan="3"></td>
					</tr>
					<tr style="height: 25px;">
						<td>历年结转:</td>
						<td id="divertedCharge"></td>
						<td>停供:</td>
						<td colspan="3" id="stopped"></td>
					</tr>
					<tr style="height: 25px;">
						<td>滞纳天数:</td>
						<td id="expiredDays"></td>
						<td>滞纳率:</td>
						<td id="zhinajinRate"></td>
						<td>滞纳金:</td>
						<td id="zhinajin"></td>
					</tr>
					<tr style="height: 25px;">
						<td>应收金额:</td>
						<td id="mustSumCharge"></td>
						<td>实收金额:</td>
						<td id="actualSumCharge" colspan="3"></td>
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