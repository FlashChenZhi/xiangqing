var SkuChangeLogSource = function () {

    var typeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '箱单位变更'},
                {'value': '2', 'displayValue': '发货单位变更'},
                {'value': '3', 'displayValue': '基本单位变更'},
                {'value': '4', 'displayValue': '箱条码'},
                {'value': '5', 'displayValue': '基本条码'},
                {'value': '6', 'displayValue': '发货条码'},
                {'value': '7', 'displayValue': '箱单位系数'},
                {'value': '8', 'displayValue': '发货单位系数'},
                {'value': '9', 'displayValue': '箱单位重量'},
                {'value': '10', 'displayValue': '发货单位重量'},
                {'value': '11', 'displayValue': '过期日'},
                {'value': '12', 'displayValue': '允收期'},
                {'value': '13', 'displayValue': '最晚出库日'},
                {'value': '14', 'displayValue': '最佳销售日'},
                {'value': '15', 'displayValue': '是否短保'},
                {'value': '16', 'displayValue': '检验设置'},
                {'value': '17', 'displayValue': '商品名称'},
                {'value': '18', 'displayValue': '是否投线'},
                {'value': '19', 'displayValue': '发货体积'},
                {'value': '20', 'displayValue': '箱体积'},
                {'value': '21', 'displayValue': '码垛规则层'},
                {'value': '21', 'displayValue': '码垛规则面'}

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

    var changeTypeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '新增'},
                {'value': '2', 'displayValue': '变更'}
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

    var recvStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': true, 'displayValue': '接受'},
                {'value': false, 'displayValue': '拒绝'}
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


    this.changeTypeMap = function () {
        var map = new Map();
        map.put('1', '新增');
        map.put('2', '变更');
        return map;
    };

    this.recvStatusMap = function () {
        var map = new Map();
        map.put(true, '接受');
        map.put(false, '拒绝');
        return map;
    };

    this.typeMap = function () {
        var map = new Map();
        map.put('1', '箱单位变更');
        map.put('2', '发货单位变更');
        map.put('3', '基本单位变更');
        map.put('4', '箱条码');
        map.put('5', '基本条码');
        map.put('6', '发货条码');
        map.put('7', '箱单位系数');
        map.put('8', '发货单位系数');
        map.put('9', '箱单位重量');
        map.put('10', '发货单位重量');
        map.put('11', '过期日');
        map.put('12', '允收期');
        map.put('13', '最晚出库日');
        map.put('14', '最佳销售日');
        map.put('15', '是否短保');
        map.put('16', '检验设置');
        map.put('17', '商品名称');
        map.put('18', '是否投线');
        map.put('19', '发货体积');
        map.put('20', '箱体积');
        map.put('21', '码垛规则层');
        map.put('22', '码垛规则面');

        return map;
    };

    this.com_type = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '变更类型',
        name: 'type',
        emptyText: '请选择',
        store: typeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_changeType = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '记录类型',
        name: 'logType',
        emptyText: '请选择',
        store: changeTypeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_recvStatus = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '下发状态',
        name: 'recvStatus',
        emptyText: '请选择',
        store: recvStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

};
