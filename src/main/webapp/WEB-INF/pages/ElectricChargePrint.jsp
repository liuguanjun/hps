<%@page pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<jsp:useBean id="now" class="java.util.Date" /> 
<head>
	<meta charset="UTF-8">
	<title>打印电费收据</title>
	<t:assets/>
</head>
<script type="text/javascript">

$(function(){
	var recordId = window.dialogArguments;
	$.ajax({
		url: "${ctx}/elecharge/" + recordId + "/chargerecord", 
		success: function(data) {
		    $(data.chaobiaoSet).each(function(index, element) {
		    	$("#chargeMonth" + index).text(element.paymentDate.monthFormatStr);
		    	$("#previousReadout" + index).text(element.provReadoutsElectric);
		    	$("#currentReadout" + index).text(element.readoutsElectric);
		    	$("#electricCount" + index).text(element.electricCount);
		    	$("#electricCharge" + index).text(element.electricCharge);
		    	if (element.zhinajinDayCount)
		    		$("#zhinaDayCount" + index).text(element.zhinajinDayCount);
		    	if (element.zhinajin)
		    		$("#zhinaCharge" + index).text(element.zhinajin + ((!data.zhinajinOn && element.zhinajin > 0) ? "(免)" : ""));
		    });
			var weishengChargeSum = 0;
			var paiwuChargeSum = 0;
			var zhaomingChargeSum = 0;
			var unitDesc = "";
			//$(data.chaobiaoSet).each(function(index, element) {
			for (var index = 0; index < data.chaobiaoSet.length; index++) {
				var element = data.chaobiaoSet[index];
				if (index == 0) {
					unitDesc = element.unit.desc;
				}
				if (element.weishengCharge)
					weishengChargeSum += element.weishengCharge;
				if (element.zhaomingCharge)
					paiwuChargeSum += element.paiwuCharge;
				if (element.paiwuCharge)
					zhaomingChargeSum += element.zhaomingCharge;
			}
			$("#weishengChargeSum").text(formatNummber(weishengChargeSum, 2));
			$("#paiwuChargeSum").text(formatNummber(paiwuChargeSum, 2));
			$("#zhaomingChargeSum").text(formatNummber(zhaomingChargeSum, 2));
			$("#unitDesc").text(unitDesc);
			$("#surplus").text(formatNummber(data.currentSurplus, 3) + "元");
			$("#previousSurplus").text(formatNummber(data.previousSurplus, 3) + "元");
			
			$("#houseNo").text(data.house.no);
			$('#houseOwnerName').text(data.houseOwner.name);
			$('#houseAddress').text(data.house.shortAddress);
			$('#mustSumCharge').text(data.mustCharge + "元");
			$('#actualSumCharge').text(data.actualCharge + "元");
			// 大写金额
			$("#mustSumChargeUpperCase").text(atoc(data.actualCharge));
			$("#operUserName").text(data.chargeUser.userName);
		}});
});


function printElectricCharge() {
	$("#printControls").hide();
	window.print();
	closeElectricCharge();
}

function closeElectricCharge() {
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
			水电一局基地管理处电费收据
		</div>
		<div
			style="width: 100%; font-size: 11.2pt; text-align: center; margin: 0.2cm 0px;white-space: nowrap;">
			房号：<div style="width: 2cm;display: inline;text-align: left;" id="houseNo"></div>
			户名：<div style="width: 2cm;display: inline;text-align: left;" id="houseOwnerName"></div>
			地址：<div style="width: 7.2cm;display: inline;text-align:left;" id="houseAddress"></div>
		</div>
		<div
			style="width: 14cm; font-size: 11.2pt; height: 5.6cm; border: solid 1px black; margin: 0px auto;">
			<table cellspacing="0" border="0"
				style="width: 100%; font-size: 11.2pt;font-weight: bold;color: black;">
				<thead>
				    <tr>
				      <td align="center">收取月份</td>
				      <td align="center">上期表值</td>
				      <td align="center">本期表值</td>
				      <td align="center">&nbsp;&nbsp;电量&nbsp;</td>
				      <td align="center">&nbsp;&nbsp;电费&nbsp;</td>
				      <td align="center">滞纳天数</td>
				      <td align="center">滞纳金</td>
				    </tr>
				</thead>
				<tbody>
				    <tr style="height: 25px;">
					    <td align="center" id="chargeMonth0"></td>
						<td align="center" id="previousReadout0"></td>
						<td align="center" id="currentReadout0"></td>
						<td align="center" id="electricCount0"></td>
						<td align="center" id="electricCharge0"></td>
						<td align="center" id="zhinaDayCount0"></td>
						<td align="center" id="zhinaCharge0"></td>
					</tr>
					<tr style="height: 25px;">
						<td align="center" id="chargeMonth1"></td>
						<td align="center" id="previousReadout1"></td>
						<td align="center" id="currentReadout1"></td>
						<td align="center" id="electricCount1"></td>
						<td align="center" id="electricCharge1"></td>
						<td align="center" id="zhinaDayCount1"></td>
						<td align="center" id="zhinaCharge1"></td>
					</tr>
					<tr style="height: 25px;">
					    <td align="center" id="chargeMonth2"></td>
						<td align="center" id="previousReadout2"></td>
						<td align="center" id="currentReadout2"></td>
						<td align="center" id="electricCount2"></td>
						<td align="center" id="electricCharge2"></td>
						<td align="center" id="zhinaDayCount2"></td>
						<td align="center" id="zhinaCharge2"></td>
					</tr>
				</tbody>
			</table>
			<table cellspacing="0" border="0"
				style="width: 100%; font-size: 11.2pt;font-weight: bold;color: black;">
				<tbody>
				    <tr style="height: 5px;">
						<td style="width: 2cm;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
						<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
						<td style="width: 2cm;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
						<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
						<td style="width: 2cm;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
						<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
					</tr>
					<tr style="height: 25px;">
						<td>卫生费:</td>
						<td id="weishengChargeSum"></td>
						<td>排污费:</td>
						<td id="paiwuChargeSum"></td>
						<td>照明费:</td>
						<td id="zhaomingChargeSum"></td>
					</tr>
					<tr style="height: 25px;">
						<td>电费单价:</td>
						<td id="unitDesc" colspan="5"></td>
						
					</tr>
					<tr style="height: 25px;">
						<td>应收金额:</td>
						<td id="mustSumCharge"></td>
						<td>实收金额:</td>
						<td id="actualSumCharge"></td>
						<td>上次余额:</td>
						<td id="previousSurplus"></td>
					</tr>
					<tr style="height: 25px;">
						<td>金额:</td>
						<td id="mustSumChargeUpperCase" colspan="3"></td>
						<td>本次余额:</td>
						<td id="surplus"></td>
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
		<div style="text-align: center; margin-top: 1cm;" id="printControls">
			<input type="button" onclick="printElectricCharge();" value="打印">
			<input type="button" onclick="closeElectricCharge();" value="取消">
		</div>
	</div>
</body>
</body>