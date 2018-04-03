var OrderSearchSource = function () {

    this.statusMap = function () {
        var map = new Map();
        map.put('1', '待分拣');
        map.put('2', '分拣中');
        map.put('3', '复核中');
        map.put('4', '待发货');
        map.put('5', '发货交接中');
        map.put('6', '在途');
        map.put('7', '司机到店');
        map.put('8', '完成');
        return map;
    };

    this.orderTypeMap = function () {
        var map = new Map();
        map.put('10', '门店发货');
        map.put('20', '委外发货');
        map.put('30', '特渠发货');
        map.put('40', 'RDC发货');
        map.put('41', '电商调拨发货');
        map.put('42', '电商发货');
        map.put('50', '加盟商发货');
        map.put('60', '转仓发货');
        map.put('61', '电商转仓发货');
        map.put('70', '退厂');
        map.put('71', '电商退厂');
        map.put('80', '报废出库');
        map.put('90', '转仓发货(311)');
        map.put('91', '转仓发货(311-WMS)');
        map.put('92', 'ZM55');
        map.put('93', '老翻新');
        map.put('95', '例外出库');
        return map;
    };

    this.finishFlagMap = function () {
        var map = new Map();
        map.put(true, '完成');
        map.put(false, '未完成');
        return map;
    };

    this.cancelFLagMap = function () {
        var map = new Map();
        map.put('0', '正常');
        map.put('1', '取消中');
        map.put('2', '取消完成');
        return map;
    };

    this.retrievalSeqMap = function () {
        var map = new Map();
        map.put('1', '先进先出');
        map.put('2', '后进先出');
        return map;
    };

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

    var statusStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '待分拣'},
                {'value': '2', 'displayValue': '分拣中'},
                {'value': '3', 'displayValue': '复核中'},
                {'value': '4', 'displayValue': '待发货'},
                {'value': '5', 'displayValue': '发货交接中'},
                {'value': '6', 'displayValue': '在途'},
                {'value': '7', 'displayValue': '司机到店'},
                {'value': '8', 'displayValue': '完成'}
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


    var finishStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': true, 'displayValue': '已完成'},
                {'value': false, 'displayValue': '未完成'}
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

    var oosStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': true, 'displayValue': '缺货完成'},
                {'value': false, 'displayValue': '正常完成'}
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

    var cancelFlagStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '0', 'displayValue': '正常'},
                {'value': '1', 'displayValue': '取消中'},
                {'value': '2', 'displayValue': '取消完成'}
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

    this.com_status = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '状态',
        name: 'status',
        emptyText: '请选择',
        store: statusStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
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


    this.txt_keyword = Ext.create("Ext.form.field.Text", {
        fieldLabel: '关键字',
        name: 'keyword'
    });


    this.com_allocatedFinishFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否分配完成',
        name: 'allocatedFinishFlag',
        emptyText: '请选择',
        store: finishStore,
        queryMode: 'local',
        displayField: 'displayValue',
        editable: false,
        triggerAction: "all",
        valueField: 'value'
    });


    this.com_pickedFinishFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否拣选完成',
        name: 'pickedFinishFlag',
        emptyText: '请选择',
        store: finishStore,
        queryMode: 'local',
        displayField: 'displayValue',
        editable: false,
        triggerAction: "all",
        valueField: 'value'
    });

    this.com_mergingFinishFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否集货完成',
        name: 'mergingFinishFlag',
        emptyText: '请选择',
        store: finishStore,
        queryMode: 'local',
        displayField: 'displayValue',
        editable: false,
        triggerAction: "all",
        valueField: 'value'
    });

    this.com_oosFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否缺货完成',
        name: 'oosFlag',
        emptyText: '请选择',
        store: oosStore,
        queryMode: 'local',
        displayField: 'displayValue',
        editable: false,
        triggerAction: "all",
        valueField: 'value'
    });

    this.com_cancelFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '取消标志',
        name: 'cancelFlag',
        emptyText: '请选择',
        store: cancelFlagStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });


};
