function validationForm(formObj, ajaxMethod) {
	$(formObj).validate({
		meta : "validate",
		errorClass : "errormessage",
		errorClass : 'error',
		validClass : 'valid',
		onkeyup : false,
		ignoreTitle : true,
		submitHandler : function(form) {
			if (ajaxMethod != null) {
				ajaxMethod();
			}else{
				form.submit();
			}
		},
		errorPlacement : function(error, element) {
			var elem = $(element), corners = [ 'right center', 'center left' ], flipIt = elem.parents('span.right').length > 0;
			if (!error.is(':empty')) {
				elem.filter(':not(.valid)').qtip({
					overwrite : true,
					content : error,
					position : {
						my : corners[flipIt ? 0 : 1],
						at : corners[flipIt ? 1 : 0],
						viewport : $(window)
					},
					show : {
						ready : true,
						effect: function(offset) {
							 $(this).slideDown(0);
						}
					},
					hide : {
						event:'click',
						inactive:3000
					},
					style : {
						classes : 'ui-tooltip-red'
					}
				}).qtip('option', 'content.text', error);
			} else {
				elem.qtip('destroy');
			}
		},
		success : $.noop
	});
}
function validationFormParam(formObj, ajaxMethod, rules, messages) {
	validationFormContent(formObj, ajaxMethod, 'right center', 'center left', rules, messages);
}
function validationFormByPositionRTBL(formObj, ajaxMethod) {
	validationFormContent(formObj, ajaxMethod, 'right top', 'left bottom', new Array(), new Array());
}
function vfByPositionRTBLAndRM(formObj, ajaxMethod, rules, messages) {
	validationFormContent(formObj, ajaxMethod, 'right top', 'left bottom', rules, messages);
}
function validationFormContent(formObj, ajaxMethod, contentP, instructionP, rules, messages) {
	$(formObj).validate({
		meta : "validate",
		messages : messages,
		rules : rules,
		errorClass : "errormessage",
		errorClass : 'error',
		validClass : 'valid',
		onkeyup : false,
		ignoreTitle : true,
		submitHandler : function(form) {
			if (ajaxMethod != null) {
				ajaxMethod();
			}else{
				form.submit();
			}
		},
		errorPlacement : function(error, element) {
			var elem = $(element), corners = [ contentP, instructionP ], flipIt = elem.parents('span.right').length > 0;
			if (!error.is(':empty')) {
				elem.filter(':not(.valid)').qtip({
					overwrite : true,
					content : error,
					position : {
						my : corners[flipIt ? 0 : 1],
						at : corners[flipIt ? 1 : 0],
						viewport : $(window)
					},
					show : {
						event : 'blur',
						ready : true,
						delay : 500
					},
					hide : {
						event:'click',
						inactive:3000
					},
					style : {
						classes : 'ui-tooltip-red'
					}
				}).qtip('option', 'content.text', error);
			} else {
				elem.qtip('destroy');
			}
		},
		success : $.noop
	});
}
jQuery.validator.addMethod("workphone",function(value, element){
	 var tel = /^(\+*\-*\(*\)*\（*\）*\#*\s*\**\d*)*$/i;
	return this.optional(element) || (tel.test(value));
	},"请填写正确的电话号码");
jQuery.validator.addMethod("mobilephone",function(value, element){
	 var tel = /^(\d*\s*)*$/;
	return this.optional(element) || (tel.test(value));
	},"请填写正确的手机号码");
jQuery.validator.addMethod("emailaddress",function(value, element){
	 var tel = /^(((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?;?)+$/i;
	return this.optional(element) || (tel.test(value));
	},"邮件地址无效");
jQuery.validator.addMethod("qq",function(value, element){
	 var tel = /^\d*$/i;
	return this.optional(element) || (tel.test(value));
	},"请填写正确的qq号码");
jQuery.validator.addMethod("filtername",function(value, element){
	var bool = true;
	if(value.indexOf("\\")!=-1||value.indexOf(">")!=-1||value.indexOf("<")!=-1)
		bool = false;
	 return bool;
	},"您的姓名存在非法字符");
jQuery.validator.addMethod("date",function(value, element){
	 var tel = /^[0-9]{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30)))$/;
	return this.optional(element) || (tel.test(value));
	},"请填写正确的日期");
jQuery.validator.addMethod("number",function(value, element){
	value = $.trim(value);
	var tel = /^\d*$/i;
	return this.optional(element) || (tel.test(value));
	},"请输入非负整数");
jQuery.validator.addMethod("minute",function(value, element){
	 var tel = /^\d*$/i;
	return this.optional(element) || (tel.test(value));
	},"请填写正确的分钟数");
jQuery.validator.addMethod("jumpNo",function(value, element){
	value = $.trim(value);
	var tel = /^-*[0-9]*$/;
	return this.optional(element) || (tel.test(value));
},"请填写内容为数字的题目数");
jQuery.validator.addMethod("category",function(value, element){
	return value!="" && value!=null;
	},"请选择");
jQuery.validator.addMethod("largemin",function(value, element){
	var min = $(element).parent().children("input[name='minOpt']").val();
	var tel = /^\d*$/i;
	if(!tel.test(min))
		return true;
	return min<=value;
	},"最多选项不能少于最少选项！");
jQuery.validator.addMethod("repeatopt",function(value, element){
	var ul = $(element).parent().parent();
	var list = $(ul).children("li");
	var index = 0;
	for(var i = 0 ;i<list.length;i++){
		var val = $(list[i]).children("input").eq(0).val();
		if($.trim(value)==$.trim(val))
			index++;
	}
	return index<=1;
	},"不能存在重复的选项！");
jQuery.validator.addMethod("maxopt",function(value, element){
	var id = $(element).attr("id");
	if(id.indexOf("_")==-1)
		return false;
	id = id.substring(0,id.indexOf("_"));
	var li = $("#"+id+"_ul").children("li");
	var total = li.length;
	return value<=total;
	},"请填写符合当前选项数的整数！");
jQuery.validator.addMethod("checkboxmin",function(value, element,param){
	var min = param;
	var parent = $(element).parent().parent();
	var checkboxList = $(parent).children("div");
	var total = 0;
	for(var i = 0 ;i<$(checkboxList).length;i++){
		var checkbox = $(checkboxList)[i];
   	var checked = $(checkbox).children("input").attr("checked");
   	if(checked=='checked')
   		total++;
   }
	if(total==0)
		return true;
	return total>=min;
	},"最少必须选择{0}个！");
jQuery.validator.addMethod("checkboxmax",function(value, element,param){
	var max = param;
	var parent = $(element).parent().parent();
	var checkboxList = $(parent).children("div");
	var total = 0;
	for(var i = 0 ;i<$(checkboxList).length;i++){
		var checkbox = $(checkboxList[i]);
   	var checked = $(checkbox).children("input");
   	if($(checked).is(":checked"))
   		total++;
   }
	if(total==0)
		return true;
	return total<=max;
	},"最多只能选择{0}个！");
jQuery.validator.addMethod("checkboxrequired",function(value, element){
	var parent = $(element).parent().parent();
	var checkboxList = $(parent).children("div");
	var total = 0;
	for(var i = 0 ;i<$(checkboxList).length;i++){
		var checkbox = $(checkboxList)[i];
   	var checked = $(checkbox).children("input").attr("checked");
   	if(checked=='checked')
   		total++;
   }
	return total>0;
	},"必须选择1个！");
jQuery.validator.addMethod("minjump",function(value, element,param){
	value = $.trim(value);
	value = Math.ceil(value);
	return value>param || value<=0;  
	},"请选择当前题号之后的问题！");
jQuery.validator.addMethod("textlength",function(value, element,param){
	var valLength = value.length;
	return valLength<=param;  
	},"请输入最大长度为{0}的字符！");
jQuery.validator.addMethod("checkStartTime",function(value, element){
   if(startTime==" 00:00:00" && endTime==" 00:00:00")
	   return true;
   if(startTime>=endTime)
	   return false;
   return true;
	},"开始时间必须早于结束时间");
jQuery.validator.addMethod("checkStartHour",function(value, element){
   var startTime = $('#startTime').val();
   var startDate = NewDate(startTime);
   var nowDate = new Date();
   if(startTime == " 00:00:00")
	   return true;
   if(startDate < nowDate)
	   return false;
   return true;
	},"开始时间不能小于当前时间");
jQuery.validator.addMethod("checkStatTime",function(value, element){
	   var startTime = $('#startDateInput').val();
	   var endTime = $('#endDateInput').val();
	   if(startTime!=null&&startTime!=""&&endTime!=null&&endTime!=""){
		   if(startTime>endTime)
			   return false;
	   }
	   return true;
		},"开始时间必须小于结束时间");
jQuery.validator.addMethod("checkWorkTime",function(value, element,param){
	   var startTime = $(element).val();
	   var endTime = $('#'+param).val();
	   if(startTime!=null&&startTime!=""&&endTime!=null&&endTime!=""){
		   if(startTime>=endTime)
			   return false;
	   }
	   return true;
		},"上班时间必须早于下班时间");
jQuery.validator.addMethod("checkSeaMon",function(value, element){
		var startTime = $('#startDateInput').val();
		var endTime = $('#endDateInput').val();
		var stime = startTime.split('-');
		var etime = endTime.split("-");
	    var i = etime[1]-stime[1]+(etime[0]-stime[0])*12+1;
	   if(i>12&&$('#timeStr').val()=="month")
		   return false;
	   return true;
		},"按月查询，最多12个月");
jQuery.validator.addMethod("checkSeaWeek",function(value, element){
		var startTime = $('#startDateInput').val();
		var endTime = $('#endDateInput').val();
		var i = checkWeek(startTime,endTime);
		if(i+1>12&&$('#timeStr').val()=="week"){
			return false;
		}
	   return true;
		},"按周查询，最多12个周");
jQuery.validator.addMethod("checkSeaDay",function(value, element){
		var startTime = $('#startDateInput').val();
		var endTime = $('#endDateInput').val();
		var i = parseInt(DateDiff(startTime,endTime));
		if($('#timeStr').val()=="day"&&i+1>7)
			return false;
	   return true;
		},"按天查询，最多7天");
jQuery.validator.addMethod("scoreLength",function(value, element){
	var valStr = value + "";
	if(valStr.indexOf(".")!=-1)
		valStr = valStr.substring(0,valStr.indexOf("."));
   return valStr.length<=9;
	},"整数位长度不能大于9！");
jQuery.validator.addMethod("zero_ten",function(value, element){
	var valueStr = value+"";
   return value >= 0&& value <= 10 && (valueStr.indexOf(".")==-1);
	},"请输入0到10的整数！");
jQuery.validator.addMethod("zero_hundred",function(value, element){
	var valueStr = value+"";
   return value >= 0&& value <= 100 && (valueStr.indexOf(".")==-1);
	},"请输入0到100的整数！");
jQuery.validator.addMethod("one_ten",function(value, element){
	var valueStr = value+"";
	return value > 0&& value <= 10 && (valueStr.indexOf(".")==-1);
	return false;
	},"请输入1到10的整数！");
jQuery.validator.addMethod("one_ten_hundred",function(value, element){
	var valueStr = value+"";
   return value >= 10000&& value <= 100000 && (valueStr.indexOf(".")==-1);
	},"请输入1万到10万的整数！"); 
	jQuery.validator.addMethod("red_zero",function(value, element){
		if(validateNumber(value)){
			return parseInt(value)>0;
		}
		return true;
		},"红色预警阈值必须大于绿色预警的阈值");
jQuery.validator.addMethod("three_val",function(value, element){
	var nextVal = $(element).parent().next().find('input').val();
	if(validateNumber(nextVal)&&validateNumber(value)){
		return parseInt(value)>parseInt(nextVal);
	}
	return true;
	},"三包要求阈值必须大于红色预警的阈值");
jQuery.validator.addMethod("red_val",function(value, element){
	var nextVal = $(element).parent().next().find('input').val();
	if(validateNumber(nextVal)&&validateNumber(value))
		return parseInt(value)>parseInt(nextVal);
	return true;
	},"红色预警阈值必须大于黄色预警的阈值");
jQuery.validator.addMethod("yellow_val",function(value, element){
	var nextVal = $(element).parent().next().find('input').val();
	if(validateNumber(nextVal)&&validateNumber(value))
		return parseInt(value)>parseInt(nextVal);
	return true;
	},"黄色预警阈值必须大于绿色预警的阈值");
function validateNumber(value){
	var tel = /^-*[0-9]*$/;
	if(tel.test(value)) 
		return true;
	return false; 
}
/**
 * 验证只能是数字和字母
 */
jQuery.validator.addMethod("digitalOrLetter",function(value,element){
	return this.optional(element) || /^[A-Za-z0-9]+$/.test(value); 
},"只能是半角数字和字母");

/**
 * 验证只能是数字、字母和下划线
 */
jQuery.validator.addMethod("digitalLetterOrUnderscore",function(value,element){
	return this.optional(element) || /^[\w]+$/.test(value); 
},"只能是半角数字、字母和下划线");
function  DateDiff(sDate1,  sDate2){ 
    var  aDate,  oDate1,  oDate2,  iDays;
    aDate  =  sDate1.split("-");
    oDate1  =  new  Date(aDate[1]  +  '-'  +  aDate[2]  +  '-'  +  aDate[0]); // 12-18-2002
    aDate  =  sDate2.split("-");
    oDate2  =  new  Date(aDate[1]  +  '-'  +  aDate[2]  +  '-'  +  aDate[0]); 
    iDays  =  parseInt(Math.abs(oDate1  -  oDate2)  /  1000  /  60  /  60  /24);
    return  iDays;
}  
function checkWeek(startDateStr, endDateStr){
	//下面几句是把字符串转换成日期
	var temp1 = startDateStr.split("-");
	var temp2 = endDateStr.split("-");
	var startDate = new Date( parseInt(temp1[0]), parseInt(temp1[1]-1), parseInt(temp1[2]));
	var endDate = new Date( parseInt(temp2[0]), parseInt(temp2[1]-1), parseInt(temp2[2]));
	var week = 0;
	
	//拿到当前是星期几，是否够一个星期..
	var startDay = startDate.getDay();							//开始是星期几
	var endDay = endDate.getDay();								//结束是星期几
	var startDayCn = 0;											//换成中国的星期
	var subtractionDay = (endDate.getTime() - startDate.getTime())/1000/60/60/24;		//算出差值，看是必天
	
	if(startDay == 0){
		startDayCn = 7;
	} else {
		startDayCn = startDay;
	}
	if((startDayCn + subtractionDay)/7 < 1){
		week = 0;
		return week;
	}
	//取得星期数
	var a = 7 - startDayCn;										//拿到当前天数
	var b = (subtractionDay - a)/7;									//减去第一个星期的零散天数,算出一共有几周。
	var c = Math.ceil(b);											//进行上舍入。
	return c;
}  

jQuery.validator.addMethod("validate_email",function(value, element){
	 var tel =  /\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/;
	return this.optional(element) || (tel.test(value));
	},"邮件地址无效");
jQuery.validator.addMethod("validate_mobile",function(value, element){
	 var tel =  /^((13[0-9]{1})|(14[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8}$/;
	return this.optional(element) || (tel.test(value));
	},"手机号码无效");
jQuery.validator.addMethod("validate_areacode",function(value, element){
	 var tel =   /^[0]\d{2,3}$/;
	return this.optional(element) || (tel.test(value));
	},"区号无效");
jQuery.validator.addMethod("validate_tel",function(value, element){
	 var tel =   /^[1-9]\d{6,7}$/;
	return this.optional(element) || (tel.test(value));
	},"电话号码无效");
jQuery.validator.addMethod("validate_zipcode",function(value, element){
	 var tel =    /^[1-9]\d{5}$/;
	return this.optional(element) || (tel.test(value));
	},"邮编无效");
jQuery.validator.addMethod("validate_mileAge",function(value, element){
	 var tel =    /^[0-9]*$/;
	return this.optional(element) || (tel.test(value));
	},"里程数无效");
jQuery.validator.addMethod("halfwidthnum",function(value, element){
	return this.optional(element) || /^[0-9]+$/.test(value); 
	},"请输入半角数字！");
jQuery.validator.addMethod("maxLength100",function(value, element){
	var length = value.length;
	return length <= 100;
	},"可输入最大长度为100");
jQuery.validator.addMethod("timeFormat",function(value, element){
	if(/^\d{4}-{1}\d{2}-{1}\d{2} \d{2}:{1}\d{2}$/.test(value)){
		var date = Date.parse(value);
		if(date==null)
			return false;
		return value==date.toString('yyyy-MM-dd HH:mm');
	}
	return false;
	},"请输入符合要求的时间信息！");
jQuery.validator.addMethod("afterNow",function(value, element){
	var now = new Date();
	var plan =Date.parse(value.replace(/-/g,"/"));
	return plan > now;
	},"预约时间必须晚于当前时间！");
jQuery.validator.addMethod("timePickVa",function(value, element){
	return value!=""&&value!=" "&&value!=null;
	},"请输入预约时间!");
jQuery.validator.addMethod("menu_not_empty",function(value, element){
	var size = $(element).find("input:checked").size();
	if(size < 1)
		return false;
	return true;
	},"请选择授权菜单!");
jQuery.validator.addMethod("afterTime",function(value, element){
	var tr = $(element).parent("td").parent("tr");
	var input1 = $(tr).children("td").eq(1).children("input").val();
	var input2 = $(tr).children("td").eq(3).children("input").val();
	if(input1 == null || input1 == "" || input2 == null || input2 == "")
		return true;
	var sd = input1.split("-");
	var date1 = new Date(sd[0],sd[1],sd[2]);
	sd = input2.split("-");
	var date2 = new Date(sd[0],sd[1],sd[2]);
	return date2 >= date1;
	},"截至时间必须大于起始时间！");
jQuery.validator.addMethod("afterReplyTime",function(value, element){
	var resultTime = $("#resultBeginDate").val();
	var replyTime = $("#replyBeginDate").val();
	if(resultTime == null || resultTime == "" || replyTime == null || replyTime == "")
		return true;
	var st = resultTime.split("-");
	var date1 = new Date(st[0],st[1],st[2]);
	var pt = replyTime.split("-");
	var date2 = new Date(pt[0],pt[1],pt[2]);
	return date2 <= date1;
	},"批复时间必须晚于申请时间！");
jQuery.validator.addMethod("menu_check",function(value, element){
	var size = $("#menu_dl").find("input:checked").size();
	return size > 0;
	},"请至少选择1个菜单！");
jQuery.validator.addMethod("menu_total",function(value, element){
	var total = $(element).val();
	return total != "0";
	},"请至少选择1个菜单！");
jQuery.validator.addMethod("hourFormat",function(value, element){
	var hm = value.split(":");
	if(hm[0]=="__"&&hm[1]=="__")
		return true;
	if(hm[0]!="__"&&hm[1]!="__"){
		if(parseInt(hm[0])>23||hm[0]=="__")
			return false;
		if(parseInt(hm[1])>59||hm[1]=="__")
			return false;
	}
	return true;
	},"时间格式有误，请更正！");

jQuery.validator.addMethod("ceCharacter",function(value, element){
	var regx=/^[^<>]{1,32}$/;
	if(regx.exec(value)){
		return true;
	}
	return false;
},"最大长度为32位，且不能包含 < 或 >");

jQuery.validator.addMethod("comment",function(value, element){
	var regx=/^[^<>]{0,64}$/;
	if(regx.exec(value)){
		return true;
	}
	return false;
},"最大长度为64位，且不能包含 < 或 > ");

jQuery.validator.addMethod("numChar",function(value, element){
	var regx =/^[a-zA-Z0-9]{1,16}$/;
	if(regx.exec(value)){
		return true;
	}
	return false;
},"输入16位以内的字母和数字的组合");

jQuery.validator.addMethod("vehType",function(value, element){
	var regx =/^[^<>]{0,16}$/;
	if(regx.exec(value)){
		return true;
	}
	return false;
},"最大长度为16位，且不能包含 < 或 >");

jQuery.validator.addMethod("decimal",function(value, element,param){
	value=parseFloat(value);
	var regx =new RegExp("^[0-9]{"+param[0]+","+param[1]+"}([.][0-9]{1,2})?$");
	if(regx.exec(value)){
		return true;
	}
	return false;
});

jQuery.validator.addMethod("vsnNum",function(value, element,param){
	value=parseInt(value);
	var regx=new RegExp("^[0-9]{"+param[0]+","+param[1]+"}$");
	if(regx.exec(value)){
		return true;
	}
	return false;
});

function parseDate(datastr) {
	  var date = new Date();
	  datastr = datastr+"";
	  var perfex = datastr.split(" ")[0].split("-");
	  var surfex = datastr.split(" ")[1].split(":");
	   var y = perfex[0] / 1;
	   var m = perfex[1] / 1-1;
	   var d = perfex[2] / 1;
	   date.setFullYear(y, m, d);
	// 时分秒
		var h = surfex[0] / 1;
		 var m = surfex[1] / 1;
	  date.setHours(h, m);
	  return date.getTime();
} 
function NewDate(str) { 
	str = str.split('-'); 
	var date = new Date(); 
	date.setUTCFullYear(str[0], str[1] - 1, str[2]); 
	date.setUTCHours(0, 0, 0, 0);
	return date; 
} 
function NewDateParm(yy,mm,dd) { 
	var date = new Date(); 
	date.setUTCFullYear(yy, mm, dd); 
	date.setUTCHours(0, 0, 0, 0);
	return date; 
} 
jQuery.validator.addMethod("compareMile",function(value, element,param){
	var targetValue=$("#"+param).val();
	if((targetValue!=null&&targetValue!="")&&(value!=null&&value!="")&&targetValue-value>0)
		return false;
	return true;
	},"截止里程数必须大于开始里程数!");

//0到xx位字母数字下划线
jQuery.validator.addMethod("lnu10",function(value, element){
	var regx=/^[a-zA-Z0-9_]{0,10}$/;
	if(regx.exec(value)){
		return true;
	}
	return false;
},"0到10位字母数字下划线");
jQuery.validator.addMethod("lnu20",function(value, element){
	var regx=/^[a-zA-Z0-9_]{0,20}$/;
	if(regx.exec(value)){
		return true;
	}
	return false;
},"0到20位字母数字下划线");
jQuery.validator.addMethod("lnu30",function(value, element){
	var regx=/^[a-zA-Z0-9_]{0,30}$/;
	if(regx.exec(value)){
		return true;
	}
	return false;
},"0到30位字母数字下划线");

//1到xx位字母数字下划线
jQuery.validator.addMethod("lnu_1_10",function(value, element){
	var regx=/^[a-zA-Z0-9_]{1,10}$/;
	if(regx.exec(value)){
		return true;
	}
	return false;
},"1到10位字母数字下划线");
jQuery.validator.addMethod("lnu_1_20",function(value, element){
	var regx=/^[a-zA-Z0-9_]{1,20}$/;
	if(regx.exec(value)){
		return true;
	}
	return false;
},"1到20位字母数字下划线");
jQuery.validator.addMethod("lnu_1_30",function(value, element){
	var regx=/^[a-zA-Z0-9_]{1,30}$/;
	if(regx.exec(value)){
		return true;
	}
	return false;
},"1到30位字母数字下划线");

//0到xx位任意字符,且不能包含 < 或  >
jQuery.validator.addMethod("char10",function(value, element){
	var regx=/^[^<>]{0,10}$/;
	if(regx.exec(value)){
		return true;
	}
	return false;
},"0到10位任意字符，且不能包含 < 或  >");
jQuery.validator.addMethod("char20",function(value, element){
	var regx=/^[^<>]{0,20}$/;
	if(regx.exec(value)){
		return true;
	}
	return false;
},"0到20位任意字符，且不能包含 < 或  >");
jQuery.validator.addMethod("char30",function(value, element){
	var regx=/^[^<>]{0,30}$/;
	if(regx.exec(value)){
		return true;
	}
	return false;
},"0到30位任意字符，且不能包含 < 或  >");
jQuery.validator.addMethod("char50",function(value, element){
	var regx=/^[^<>]{0,50}$/;
	if(regx.exec(value)){
		return true;
	}
	return false;
},"0到50位任意字符，且不能包含 < 或  >");
jQuery.validator.addMethod("char100",function(value, element){
	var regx=/^[^<>]{0,100}$/;
	if(regx.exec(value)){
		return true;
	}
	return false;
},"0到100位任意字符，且不能包含 < 或  >");
jQuery.validator.addMethod("char200",function(value, element){
	var regx=/^[^<>]{0,200}$/;
	if(regx.exec(value)){
		return true;
	}
	return false;
},"0到200位任意字符，且不能包含 < 或  >");
//
//1到xx位任意字符,且不能包含 < 或  >
jQuery.validator.addMethod("char_1_10",function(value, element){
	var regx=/^[^<>]{1,10}$/;
	if(regx.exec(value)){
		return true;
	}
	return false;
},"1到10位任意字符，且不能包含 < 或  >");
jQuery.validator.addMethod("char_1_20",function(value, element){
	var regx=/^[^<>]{1,20}$/;
	if(regx.exec(value)){
		return true;
	}
	return false;
},"1到20位任意字符，且不能包含 < 或  >");
jQuery.validator.addMethod("char_1_30",function(value, element){
	var regx=/^[^<>]{1,30}$/;
	if(regx.exec(value)){
		return true;
	}
	return false;
},"1到30位任意字符，且不能包含 < 或  >");
jQuery.validator.addMethod("char_1_50",function(value, element){
	var regx=/^[^<>]{1,50}$/;
	if(regx.exec(value)){
		return true;
	}
	return false;
},"1到50位任意字符，且不能包含 < 或  >");
jQuery.validator.addMethod("char_1_100",function(value, element){
	var regx=/^[^<>]{1,100}$/;
	if(regx.exec(value)){
		return true;
	}
	return false;
},"1到100位任意字符，且不能包含 < 或  >");
jQuery.validator.addMethod("char_1_200",function(value, element){
	var regx=/^[^<>]{0,200}$/;
	if(regx.exec(value)){
		return true;
	}
	return false;
},"1到200位任意字符，且不能包含 < 或  >");
