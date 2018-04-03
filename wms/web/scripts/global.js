function HTMLEncode(input) {
    var div = document.createElement('div');
    div.innerText = input;
    return div.innerHTML;
}

function HTMLDecode(input) {
    var div = document.createElement('div');
    div.innerHTML = input;
    return div.innerText;
}

function curDateTime() {
    var d = new Date();
    var year = d.getFullYear();
    var month = d.getMonth() + 1;
    var date = d.getDate();
    var day = d.getDay();
    var hours = d.getHours();
    var minutes = d.getMinutes();
    var seconds = d.getSeconds();
    var ms = d.getMilliseconds();
    var curDateTime = year;
    if (month > 9)
        curDateTime = curDateTime + "-" + month;
    else
        curDateTime = curDateTime + "-0" + month;
    if (date > 9)
        curDateTime = curDateTime + "-" + date;
    else
        curDateTime = curDateTime + "-0" + date;
    if (hours > 9)
        curDateTime = curDateTime + " " + hours;
    else
        curDateTime = curDateTime + " 0" + hours;
    if (minutes > 9)
        curDateTime = curDateTime + ":" + minutes;
    else
        curDateTime = curDateTime + ":0" + minutes;
    if (seconds > 9)
        curDateTime = curDateTime + ":" + seconds;
    else
        curDateTime = curDateTime + ":0" + seconds;
    return curDateTime;
}

function objectParsetString(obj) {
    if (obj === undefined || obj === null || obj === "" || obj === "undefined")
        return "";
    return obj;
}

function HTMLReplace(str) {
    str = str.replace(/\&/, "&amp;");
    str = str.replace(/"/g, "&quot;");
    str = str.replace(/'/g, "&apos;");
    str = str.replace(/\</ig, "&lt;");
    str = str.replace(/\>/ig, "&gt;");
    str = str.replace(/ /ig, "&nbsp;");
    return str;
}

/**
 * 字符串转JSON
 *
 * @param strData
 * @returns
 */
function parseJSON(strData) {
    return (new Function("return " + strData))();
}

function goBack() {
    window.history.back();
}

function extStoreLoadFail(thiz, request) {
    if (request.timedout) {
        Ext.warn('连接服务器超时');
    } else if (request.responseText == null || request.responseText == '') {
        Ext.error('请求服务器失败,请联系管理员');
    } else {
        var result = Ext.util.JSON.decode(request.responseText);
        Ext.error(result.msg);
    }
}

function extAjaxFail(response, options) {
    if (response.timedout) {
        Ext.warn('连接服务器超时');
    } else if (response.responseText == null || response.responseText == '') {
        Ext.error('请求服务器失败,请联系管理员');
    } else {
        var result = Ext.util.JSON.decode(response.responseText);
        Ext.error(result.msg);
    }
}