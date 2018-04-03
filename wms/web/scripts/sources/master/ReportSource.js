/**
 * Created by Administrator on 2016/10/17.
 */
var ReportSource = function () {

    var orderTypeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '10', 'displayValue': '门店发货'},
                {'value': '20', 'displayValue': '委外发货'},
                {'value': '30', 'displayValue': '特渠发货'},
                {'value': '40', 'displayValue': 'RDC发货'},
                {'value': '41', 'displayValue': '电商调拨发货'},
                {'value': '42', 'displayValue': '电商发货'},
                {'value': '50', 'displayValue': '加盟商发货'},
                {'value': '60', 'displayValue': '转仓发货'},
                {'value': '61', 'displayValue': '电商转仓发货'},
                {'value': '70', 'displayValue': '退厂'},
                {'value': '71', 'displayValue': '电商退厂'},
                {'value': '80', 'displayValue': '报废出库'},
                {'value': '90', 'displayValue': '转仓发货(311)'},
                {'value': '91', 'displayValue': '转仓发货(311-WMS)'},
                {'value': '92', 'displayValue': 'ZM55'},
                {'value': '93', 'displayValue': '老翻新'},
                {'value': '95', 'displayValue': '例外出库'}
            ]
        },
        proxy: {
            type: 'memory',
            reader: {
                type: 'json',
                rootProperty: 'res'
            }
        }
    });


    var receivingTypeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data:{
            'res':[
                {'value':'01','displayValue':'供应商采购入库'},
                {'value':'02','displayValue':'委外采购入库'},
            ]
        },
        proxy:{
            type:'memory',
            reader:{
                type:'json',
                rootProperty:'res'
            }
        }
    });

    var transferTypeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '60', 'displayValue': '转仓发货'},
                {'value': '90', 'displayValue': '转仓发货(311)'},
                {'value': '91', 'displayValue': '转仓发货(311-WMS)'}
            ]
        },
        proxy: {
            type: 'memory',
            reader: {
                type: 'json',
                rootProperty: 'res'
            }
        }
    });

    var recvChatStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '数量'},
                {'value': '2', 'displayValue': '订单'},
                {'value': '3', 'displayValue': '行项目'},
            ]
        },
        proxy: {
            type: 'memory',
            reader: {
                type: 'json',
                rootProperty: 'res'
            }
        }
    });

    var orderChatStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '数量'},
                {'value': '2', 'displayValue': '订单'},
                {'value': '3', 'displayValue': '送达方'},
                {'value': '4', 'displayValue': '派车单'},
                {'value': '5', 'displayValue': '波次'},
            ]
        },
        proxy: {
            type: 'memory',
            reader: {
                type: 'json',
                rootProperty: 'res'
            }
        }
    });

    var transferChatStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '数量'},
                {'value': '2', 'displayValue': '订单'},
                {'value': '3', 'displayValue': '行项目'},
            ]
        },
        proxy: {
            type: 'memory',
            reader: {
                type: 'json',
                rootProperty: 'res'
            }
        }
    });

    var countChatStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '数量'},
                {'value': '2', 'displayValue': '订单'},
                {'value': '3', 'displayValue': '行项目'},
            ]
        },
        proxy: {
            type: 'memory',
            reader: {
                type: 'json',
                rootProperty: 'res'
            }
        }
    });

    this.com_orderType = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '类型',
        name: 'orderType',
        emptyText: '请选择',
        store: orderTypeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_transferType = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '类型',
        name: 'orderType',
        emptyText: '请选择',
        store: transferTypeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });


    this.com_receivingType =Ext.create('Ext.form.ComboBox', {
        fieldLabel: '入库类型',
        name: 'receivingType',
        emptyText:'请选择',
        store: receivingTypeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus : true,
        valueField: 'value'
    });

    this.com_recvChartType =Ext.create('Ext.form.ComboBox', {
        fieldLabel: '图表类型',
        name: 'chartType',
        emptyText:'请选择',
        store: recvChatStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus : true,
        valueField: 'value'
    });


    this.com_orderChartType =Ext.create('Ext.form.ComboBox', {
        fieldLabel: '图表类型',
        name: 'chartType',
        emptyText:'请选择',
        store: orderChatStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus : true,
        valueField: 'value'
    });

    this.com_transferChartType =Ext.create('Ext.form.ComboBox', {
        fieldLabel: '图表类型',
        name: 'chartType',
        emptyText:'请选择',
        store: transferChatStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus : true,
        valueField: 'value'
    });

    this.com_countChartType =Ext.create('Ext.form.ComboBox', {
        fieldLabel: '图表类型',
        name: 'chartType',
        emptyText:'请选择',
        store: countChatStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus : true,
        valueField: 'value'
    });
};