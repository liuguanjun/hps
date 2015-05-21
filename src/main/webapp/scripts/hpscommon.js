
var COMBOBOX_DEFAULT_SEL_VAL = "";
var g_baseInitialized = false;
var g_areaInitialized = false;
var g_louzuoInitialized = false;
var g_dictInitialized = false;
var g_heatingInitialized = false;
var g_maitainInitialized = false;

var g_chargestate_cancelled = "已取消";
var g_chargestate_uncharged = "未缴费";
var g_chargestate_charged = "已缴费";
var baseComboboxOnCharge;

$(function(){
	loadBaseCombobox();
	initSexCombobox();
	initUserTypeCombobox();
	loadDictItemsCombobox();
	setReadonlyBackground();
	loadChargeStateCombobox();
});

function loadBaseCombobox() {
	if ($("input[type=base]").size() > 0 || $("input[type=area]").size() > 0) {
		$.ajax({
			url: ctx + '/admin/BaseManagement/baseTreeElements',
			type : "get",
			success: function(data) {
				initBaseCombobox(data);
			}});
	} else {
		// 页面没有基地与区域
		g_baseInitialized = true;
		g_areaInitialized = true;
		g_louzuoInitialized = true;
		g_heatingInitialized = true;
		g_maitainInitialized = true;
	}
}

function initAreaCombobox(baseData, baseCombobox, baseCode) {
	if ($("input[type=area]").size() > 0) {
		$("input[type=area]").each(function (index,element) {
			var baseComboboxId = $(element).attr("baseComboboxId");
			if (baseComboboxId != baseCombobox.id) {
				// 不是关联的基地列表发生了变化，不做任何处理
				return;
			}
			var areaData = [];
			if (baseCode != COMBOBOX_DEFAULT_SEL_VAL) {
				areaData = getAreaData(baseData, baseCode);
			}
			$(element).combobox({
				valueField:'code',    
			    textField:'name',
			    data: eval(JSON.stringify(areaData)),
			    loadFilter : function(data) {
			    	var hasEmpty = $(element).attr("hasEmpty");
			    	if (hasEmpty == "true" && data.length > 1) {
			    		data.unshift({
			    			code: COMBOBOX_DEFAULT_SEL_VAL,
			    			name: "不限"
			    		});
			    	}
			    	return data;
			    },
			    onLoadSuccess : function(data) {
			    	if (!data || !data[0]) {
			    		data = [ { code : COMBOBOX_DEFAULT_SEL_VAL }];
			    	}
			    	$(this).combobox("setValue", data[0].code);
		    		// 重新加载绑定到本区域的楼座
		    		loadLouzuoCombobox(element);
			    },
			    onChange : function (newValue, oldValue) {
		    		loadLouzuoCombobox(element);
			    }
			});
		});
		g_areaInitialized = true;
	} else {
		g_areaInitialized = true;
		g_louzuoInitialized = true;
	}
}

function loadLouzuoCombobox(areaCombobox) {
	g_louzuoInitialized = false;
	if ($("input[type=louzuo]").size() > 0) {
		$("input[type=louzuo]").each(function (index,element) {
			var areaComboboxId = $(element).attr("areaComboboxId");
			// 如果当前楼座并没有绑定到变化的区域下拉列表，直接返回
			if (areaComboboxId != areaCombobox.id) {
				return;
			}
			var areaCode = $(areaCombobox).combobox("getValue");
			var param = {areaCode:areaCode};
			if (areaCode && areaCode != COMBOBOX_DEFAULT_SEL_VAL) {
				// 区域没有选择是，不加载楼座
				$.ajax({
					url: ctx + '/admin/LouzuoManagement/arealouzuo',
					type : "get",
					data : param,
					success: function(data) {
						initLouzuoCombobox(element, data);
					}});
			} else {
				initLouzuoCombobox(element, []);
			}
		});
	} else {
		g_louzuoInitialized = true;
	}
}

function loadHeatingPaymentDatesCombobox(baseCombobox) {
	if ($("input[type=paymentDate]").size() > 0) {
		$("input[type=paymentDate]").each(function (index,element) {
			var baseComboboxId = $(element).attr("baseComboboxId");
			// 如果当前缴费日期并没有绑定到变化的基地下拉列表，直接返回
			if (baseComboboxId != baseCombobox.id) {
				return;
			}
			var baseCode = $(baseCombobox).combobox("getValue");
			var param = {baseCode:baseCode};
			if (baseCode && baseCode != COMBOBOX_DEFAULT_SEL_VAL) {
				// 基地没有选择时，不加载缴费年费下拉列表
				$.ajax({
					url: ctx + '/heatingcharge/' + baseCode + '/paymentdates',
					type : "get",
					data : param,
					
					success: function(data) {
						initHeatingPaymentDatesCombobox(element, data);
					}});
			} else {
				initHeatingPaymentDatesCombobox(element, []);
			}
		});
	} else {
		g_heatingInitialized = true;
	}
}

function initMaintainPaymentDatesCombobox(element, paymentDatesData) {
	$(element).combobox({
		valueField:'id',    
	    textField:'title',
	    data: eval(JSON.stringify(paymentDatesData)),
	    loadFilter : function(data) {
	    	var hasEmpty = $(this).attr("hasEmpty");
	    	if (hasEmpty == "true" && data.length > 1) {
	    		data.push({
	    			id: COMBOBOX_DEFAULT_SEL_VAL,
	    			title: "不限"
	    		});
	    	}
	    	return data;
	    },
	    onLoadSuccess : function(data) {
	    	// 没有查询到数据，选中默认选项
	    	if (!data || !data[0]) {
	    		data = [ { id : COMBOBOX_DEFAULT_SEL_VAL }];
	    	}
	    	$(this).combobox("setValue", data[0].id);
	    }
	});
	g_maitainInitialized = true;
}

function initHeatingPaymentDatesCombobox(element, paymentDatesData) {
	$(element).combobox({
		valueField:'id',    
	    textField:'title',
	    data: eval(JSON.stringify(paymentDatesData)),
	    loadFilter : function(data) {
	    	var hasEmpty = $(this).attr("hasEmpty");
	    	if (hasEmpty == "true" && data.length > 1) {
	    		data.push({
	    			id: COMBOBOX_DEFAULT_SEL_VAL,
	    			title: "不限"
	    		});
	    	}
	    	return data;
	    },
	    onLoadSuccess : function(data) {
	    	// 没有查询到数据，选中默认选项
	    	if (!data || !data[0]) {
	    		data = [ { id : COMBOBOX_DEFAULT_SEL_VAL }];
	    	}
	    	$(this).combobox("setValue", data[0].id);
	    }
	});
	g_heatingInitialized = true;
}

function initLouzuoCombobox(element, louzuoData) {
	$(element).combobox({
		valueField:'code',    
	    textField:'name',
	    data: eval(JSON.stringify(louzuoData)),
	    loadFilter : function(data) {
	    	var hasEmpty = $(this).attr("hasEmpty");
	    	if (hasEmpty == "true" && data.length > 1) {
	    		data.unshift({
	    			code: COMBOBOX_DEFAULT_SEL_VAL,
	    			name: "不限"
	    		});
	    	}
	    	return data;
	    },
	    onLoadSuccess : function(data) {
	    	if (!data || !data[0]) {
	    		data = [ { code : COMBOBOX_DEFAULT_SEL_VAL }];
	    	}
	    	$(this).combobox("setValue", data[0].code);
	    }
	});
	g_louzuoInitialized = true;
}

function getAreaData(baseData, baseCode) {
	var areaData = null;
	$(baseData).each(function(index, element) {
		if (baseCode == element.code) {
			areaData = element.children;
		}
	});
	return areaData;
}

function initBaseCombobox(baseData) {
	$("input[type=base]").each(function (index,element) {
		$(element).combobox({
			valueField:'code',    
		    textField:'name',
		    data: eval(JSON.stringify(baseData)),
		    loadFilter : function(data) {
		    	var hasEmpty = $(this).attr("hasEmpty");
		    	if (hasEmpty == "true" && data.length > 1) {
		    		data.unshift({
		    			code: COMBOBOX_DEFAULT_SEL_VAL,
		    			name: "不限"
		    		});
		    	}
		    	return data;
		    },
		    onLoadSuccess : function(data) {
		    	$(this).combobox("setValue", data[0].code);
	    		// 重新加载绑定到本基地的区域
	    		initAreaCombobox(baseData, element, data[0].code);
	    		loadHeatingPaymentDatesCombobox(element);
	    		loadMaitainPaymentDatesCombobox(element);
		    },
		    onChange : function (newValue, oldValue) {
	    		initAreaCombobox(baseData, element, newValue);
	    		loadHeatingPaymentDatesCombobox(element);
	    		loadMaitainPaymentDatesCombobox(element);
	    		if (baseComboboxOnCharge) {
	    			baseComboboxOnCharge(newValue);
	    		}
		    }
		});
	});
	g_baseInitialized = true;
}

function initSexCombobox() {
	$("input[type=sex]").each(function (index,element) {
		$(element).combobox({
			valueField:'code',    
		    textField:'name',
		    data : [ { code: "MALE",
		    		   name: "男"
		    	},
		    	{ code: "FEMALE",
		    		   name: "女"
		    	}
		    ],
		    loadFilter : function(data) {
		    	var hasEmpty = $(this).attr("hasEmpty");
		    	if (hasEmpty == "true") {
		    		data.unshift({
		    			code: COMBOBOX_DEFAULT_SEL_VAL,
		    			name: "不限"
		    		});
		    	}
		    	return data;
		    },
		    onLoadSuccess : function(data) {
		    	$(this).combobox("setValue", data[0].code);
		    }
		});
	});
}

function initUserTypeCombobox() {
	$("input[type=userType]").each(function (index,element) {
		$(element).combobox({
			valueField:'code',    
		    textField:'name',
		    data : [ { code: "ROLE_ADMIN",
		    		   name: "管理员"
		    	},
		    	{ code: "ROLE_USER",
		    		   name: "操作员"
		    	},
		    	{ code: "ROLE_SUPERADMIN",
		    		   name: "超级管理员"
		    	}
		    	,
		    	{ code: "ROLE_HOUSEADMIN",
		    		   name: "房管员"
		    	}
		    ],
		    loadFilter : function(data) {
		    	var hasEmpty = $(this).attr("hasEmpty");
		    	if (hasEmpty == "true") {
		    		data.unshift({
		    			code: COMBOBOX_DEFAULT_SEL_VAL,
		    			name: "不限"
		    		});
		    	}
		    	return data;
		    },
		    onLoadSuccess : function(data) {
		    	$(this).combobox("setValue", data[0].code);
		    }
		});
	});
}

function commonItemsInitialized() {
	return g_baseInitialized && g_areaInitialized && g_louzuoInitialized && g_dictInitialized
		&& g_heatingInitialized && g_maitainInitialized;
}

function loadChargeStateCombobox() {
	$("input[type=electricChargeState]").each(function (index,element) {
		$(element).combobox({
			valueField:'code',    
		    textField:'name',
		    data : [ { code: "CHARGED",
		    		   name: g_chargestate_charged
		    	},
		    	{ code: "UNCHARGED",
		    		   name: g_chargestate_uncharged
		    	},
		    	{ code: "CANCELLED",
		    		   name: g_chargestate_cancelled
		    	}
		    ],
		    loadFilter : function(data) {
		    	var hasEmpty = $(this).attr("hasEmpty");
		    	if (hasEmpty == "true") {
		    		data.unshift({
		    			code: COMBOBOX_DEFAULT_SEL_VAL,
		    			name: "不限"
		    		});
		    	}
		    	return data;
		    },
		    onLoadSuccess : function(data) {
		    	$(this).combobox("setValue", data[0].code);
		    }
		});
	});
}

function loadDictItemsCombobox() {
	if ($("input[type=dictitem]").size() > 0) {
		$.ajax({
			url: ctx + '/admin/dicts',
			type : "get",
			success: function(data) {
				initDictItemsCombobox(data);
			}});
	} else {
		g_dictInitialized = true;
	}
}

function initDictItemsCombobox(dictData) {
	$("input[type=dictitem]").each(function (index, element) {
		for (var i = 0; i < dictData.length; i++) {
			var jqueryItemsObj = $(element);
			if (dictData[i].code == jqueryItemsObj.attr("dictcode")) {
				jqueryItemsObj.combobox({
					valueField:'code',    
				    textField:'name',
				    data : eval(JSON.stringify(dictData[i].itemSet)),
				    loadFilter : function(data) {
				    	var hasEmpty = $(element).attr("hasEmpty");
				    	if (hasEmpty == "true") {
				    		data.unshift({
				    			code: COMBOBOX_DEFAULT_SEL_VAL,
				    			name: "不限"
				    		});
				    	}
				    	return data;
				    },
				    onLoadSuccess : function(data) {
				    	$(this).combobox("setValue", data[0].code);
				    }
				});
			}
		}
	});
	g_dictInitialized = true;
}

function setReadonlyBackground() {
	$("input[readonly=readonly]").each(function(index, element) {
		$(element).css("background-color", "#F3F3FA");
	});
}

//function setTDRequired() {
//	$("td[hpsrequired=true]").append('<span style="color: red;font-weight: bold;vertical-align: middle;">*</span>');
//}

function getNestedValue(value, rowData, rowIndex) {
	try {
		return eval("rowData." + this.field);
	} catch(e) {
	   return "";
	}
}

function getBooleanValue(value, rowData, rowIndex) {
	if (value) {
		return "是";
	} else {
		return "否";
	}
}

function loadMaitainPaymentDatesCombobox(baseCombobox) {
	if ($("input[type=maintainPaymentDate]").size() > 0) {
		$("input[type=maintainPaymentDate]").each(function (index,element) {
			var baseComboboxId = $(element).attr("baseComboboxId");
			// 如果当前缴费日期并没有绑定到变化的基地下拉列表，直接返回
			if (baseComboboxId != baseCombobox.id) {
				return;
			}
			var baseCode = $(baseCombobox).combobox("getValue");
			var param = {baseCode:baseCode};
			if (baseCode && baseCode != COMBOBOX_DEFAULT_SEL_VAL) {
				// 基地没有选择时，不加载缴费年费下拉列表
				$.ajax({
					url: ctx + '/maintaincharge/' + baseCode + '/paymentdates',
					type : "get",
					data : param,
					
					success: function(data) {
						initMaintainPaymentDatesCombobox(element, data);
					}});
			} else {
				initMaintainPaymentDatesCombobox(element, []);
			}
		});
	} else {
		g_maitainInitialized = true;
	}
}

// 转换大写金额
function atoc(numberValue) {
	var numberValue = new String(Math.round(numberValue * 100)); // 数字金额  
	var chineseValue = ""; // 转换后的汉字金额  
	var String1 = "零壹贰叁肆伍陆柒捌玖"; // 汉字数字  
	var String2 = "万仟佰拾亿仟佰拾万仟佰拾元角分"; // 对应单位  
	var len = numberValue.length; // numberValue 的字符串长度  
	var Ch1; // 数字的汉语读法  
	var Ch2; // 数字位的汉字读法  
	var nZero = 0; // 用来计算连续的零值的个数  
	var String3; // 指定位置的数值  
	if (len > 15) {
		alert("超出计算范围");
		return "";
	}
	if (numberValue == 0) {
		chineseValue = "零元整";
		return chineseValue;
	}
	String2 = String2.substr(String2.length - len, len); // 取出对应位数的STRING2的值  
	for (var i = 0; i < len; i++) {
		String3 = parseInt(numberValue.substr(i, 1), 10); // 取出需转换的某一位的值  
		if (i != (len - 3) && i != (len - 7) && i != (len - 11)
				&& i != (len - 15)) {
			if (String3 == 0) {
				Ch1 = "";
				Ch2 = "";
				nZero = nZero + 1;
			} else if (String3 != 0 && nZero != 0) {
				Ch1 = "零" + String1.substr(String3, 1);
				Ch2 = String2.substr(i, 1);
				nZero = 0;
			} else {
				Ch1 = String1.substr(String3, 1);
				Ch2 = String2.substr(i, 1);
				nZero = 0;
			}
		} else { // 该位是万亿，亿，万，元位等关键位  
			if (String3 != 0 && nZero != 0) {
				Ch1 = "零" + String1.substr(String3, 1);
				Ch2 = String2.substr(i, 1);
				nZero = 0;
			} else if (String3 != 0 && nZero == 0) {
				Ch1 = String1.substr(String3, 1);
				Ch2 = String2.substr(i, 1);
				nZero = 0;
			} else if (String3 == 0 && nZero >= 3) {
				Ch1 = "";
				Ch2 = "";
				nZero = nZero + 1;
			} else {
				Ch1 = "";
				Ch2 = String2.substr(i, 1);
				nZero = nZero + 1;
			}
			if (i == (len - 11) || i == (len - 3)) { // 如果该位是亿位或元位，则必须写上  
				Ch2 = String2.substr(i, 1);
			}
		}
		chineseValue = chineseValue + Ch1 + Ch2;
	}
	if (String3 == 0) { // 最后一位（分）为0时，加上“整”  
		chineseValue = chineseValue + "整";
	}
	return chineseValue;
}

function formatNummber(src, pos, ceil)  
{  
	src = src + "";
	src = src.replace(",", "");
	var result = 0;
	if (ceil) {
		result = Math.ceil(src*Math.pow(10, pos))/Math.pow(10, pos); 
	} else {
		result = Math.round(src*Math.pow(10, pos))/Math.pow(10, pos); 
	}
    result = addCommas(result);
    var x = result.split('.');
    var suffix = "";
    if (x.length == 1) {
    	suffix = ".00";
    } else {
    	suffix = "." + x[1];
    	while (suffix.length < pos + 1) {
    		suffix += "0";
    	}
    }
    return x[0] + suffix;
}  

function addCommas(nStr)
{
	nStr += '';
	var x = nStr.split('.');
	var x1 = x[0];
	var x2 = x.length > 1 ? '.' + x[1] : '';
	var rgx = /(\d+)(\d{3})/;
	while (rgx.test(x1)) {
		x1 = x1.replace(rgx, '$1' + ',' + '$2');
	}
	return x1 + x2;
}
function getNestedZhinaValue (value, rowData, rowIndex) {
	return value * 1000 + "‰/日 ";
}

function getNestedRateValue (value, rowData, rowIndex) {
	return value * 100 + "%";
}