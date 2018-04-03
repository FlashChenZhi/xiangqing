const reqPrefix = "/wms/";

function getReqUrl(url) {
    const res = reqPrefix + url;
    return res;
}

function writeLog(level, msg) {
    if (level == null || level == undefined) {
        console.error("writeLog level is null or undefined");
    } else if (level === 'debug') {
        console.debug(msg);
    } else if (level === 'info') {
        console.info(msg);
    } else if (level === 'warn') {
        console.warn(msg);
    } else if (level === 'error') {
        console.info(msg);
    } else {
        console.error("unknow log level[" + level + "]");
    }
}

function jsonSort(filed, rev, primer) {
    rev = (rev) ? -1 : 1;
    return function (a, b) {
        a = a[filed];
        b = b[filed];
        if (typeof (primer) != 'undefined') {
            a = primer(a);
            b = primer(b);
        }
        if (a < b) {
            return rev * -1;
        }
        if (a > b) {
            return rev * 1;
        }
        return 1;
    }
}

function replaceNull(value, defaultValue) {
    if (value === null || value === undefined)
        return defaultValue;
    return value;
}

function replaceJsonField(json, field, defaultValue) {
    if (json === null || json === undefined)
        return defaultValue;

    const returnValue = json[field];
    if (returnValue === null || returnValue === undefined)
        return defaultValue;
    return returnValue;
}

function HTMLEncode(input) {
    var converter = $(document.createElement("DIV"));
    converter.text(input);
    return converter.html();
}

function HTMLDecode(input) {
    var converter = $(document.createElement("DIV"));
    converter.html(input);
    return converter.text();
}

function escapeHtml(string) {
    var entityMap = {
        "&": "&amp;",
        "<": "&lt;",
        ">": "&gt;",
        '"': '&quot;',
        "'": '&#39;',
        "/": '&#x2F;'
    };
    return String(string).replace(/[&<>"'\/]/g, function (s) {
        return entityMap[s];
    });
}


function getHashValue(index) {
    var value = window.location.hash;
    value = value.substr(1);
    var splitValues = value.split("/");
    var values = new Array();
    var count = 0;
    for (var i = 0; i < splitValues.length; i++) {
        var d = splitValues[i];
        if (d == null || d == '')
            continue;

        values[count] = d;
        count++;
    }

    if (index > values.length)
        return "";

    var result = values[index];
    var questionMarkIndex = d.indexOf('?');
    if (questionMarkIndex !== -1) {
        result = result.substring(0, questionMarkIndex);
    }
    return result;
}


Date.prototype.format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1,                 //月份
        "d+": this.getDate(),                    //日
        "H+": this.getHours(),                   //小时
        "m+": this.getMinutes(),                 //分
        "s+": this.getSeconds(),                 //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds()             //毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};