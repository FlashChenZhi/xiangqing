import React from 'react';
import {hashHistory} from 'react-router';
import {message} from 'antd';

import reqwest from 'reqwest';

export function getReqwestErrData(err) {
    const status = err.status;
    const statusText = err.statusText;
    const responseURL = err.responseURL;
    // console.error('getReqwestErrData', responseURL, status, statusText);
    let data = {};
    data.redirect = false;
    switch (status) {
        case 401:
            data.redirect = true;
            data.redirectHash = '/login';
            data.msg = "用户未登录";
            break;
        case 404:
            data.msg = "404, 请求URL不存在";
            break;
        case 500:
            data.msg = "500, 服务器发生了异常";
            break;
        case 502:
            data.msg = "502, 连接服务器失败";
            break;
        default:
            data.msg = status + ", " + statusText;
    }
    return data;
}

export function redirect(hash) {
    // console.info('-----------redirect-----------', hash);
    hashHistory.push(hash);
}

export function reqwestError(err) {
    const errData = getReqwestErrData(err);
    if (errData.redirect) {
        redirect(errData.redirectHash);
    } else {
        message.error(errData.msg);
    }
}

export function clearLocalStorage() {
    localStorage.clear();
}

export function getAreaData() {
    return localStorage.areaCascader;
}

export function getAuthData() {
    return localStorage.authData;
}

export function getExcelMaxRow() {
    return 50000;
}
function PrefixInteger(num, length) {
    return (Array(length).join('0') + num).slice(-length);
}

export function dateFormat(date) {
    return date.getFullYear() + PrefixInteger(date.getMonth() + 1, 2) + PrefixInteger(date.getDate(), 2);
}

