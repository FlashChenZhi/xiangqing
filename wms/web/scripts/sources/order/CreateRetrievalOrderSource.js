var CreateRetrievalOrderSource = function () {

    this.orderTypeMap = function () {
        map.put('91', '移库（311-WMS）');
        map.put('92', 'zm55');
        map.put('93', '老翻新');
        return map;
    };

    var orderTypeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '91', 'displayValue': '移库（311-WMS）'},
                {'value': '92', 'displayValue': 'zm55'},
                {'value': '93', 'displayValue': '老翻新'}
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
        name: 'com_orderType',
        emptyText: '请选择',
        store: orderTypeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });


    this.txt_planQty = Ext.create("Ext.form.field.Number", {
        fieldLabel: '数量',
        name: 'planQty',
        minValue: 0
    });


};
