var SingleOrderBatchStartSource = function () {

    this.orderTypeMap = function() {
        var map = new Map();
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
        map.put('90', '转仓发货(311)');
        map.put('91', '转仓发货(311-WMS)');
        map.put('92', 'ZM55');
        map.put('93', '老翻新');
        map.put('95', '例外出库');
        return map;
    };

    var orderTypeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
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

    this.com_orderType = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '订单类型',
        name: 'orderType',
        emptyText: '请选择',
        store: orderTypeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });




};
