var RetrievalBatchManageSource = function () {

    this.batchStatusMap = function () {
        var map = new Map();
        map.put('0', '待创建');
        map.put('1', '待开始');
        map.put('2', '作业中');
        map.put('3', '完成');
        map.put('8', '系统取消');
        map.put('9', '用户取消');

        return map;
    };

    this.batchTypeMap = function () {
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

    this.yesOrNoMap = function () {
        var map = new Map();
        map.put(true, '是');
        map.put(false, '否');

        return map;
    };


    var batchStatusStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '0', 'displayValue': '待创建'},
                {'value': '1', 'displayValue': '待开始'},
                {'value': '2', 'displayValue': '作业中'},
                {'value': '3', 'displayValue': '完成'},
                {'value': '8', 'displayValue': '系统取消'},
                {'value': '9', 'displayValue': '用户取消'}

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

    this.com_batchStatus = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '波次状态',
        name: 'batchStatus',
        emptyText: '请选择',
        store: batchStatusStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.txt_createUser = Ext.create("Ext.form.field.Text", {
        fieldLabel: '创建人',
        name: 'createUser'
    });


    this.txt_startUser = Ext.create("Ext.form.field.Text", {
        fieldLabel: '开始人',
        name: 'startUser'
    });

    this.txt_finishUser = Ext.create("Ext.form.field.Text", {
        fieldLabel: '结束人',
        name: 'finishUser'
    });
};
