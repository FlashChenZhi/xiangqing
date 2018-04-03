/**
 * Created by Van on 2015/6/4.
 */

var ShippingCheckByWaveSource = function () {

    var containerType = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '纸箱'},
                {'value': '2', 'displayValue': '料箱'}
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

    this.com_containerType = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '容器类型',
        name: 'containerType',
        emptyText: '请选择',
        store: containerType,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.txt_Qty = Ext.create("Ext.form.field.Number", {
        fieldLabel: '数量',
        name: 'qty',
        minValue: 0
    });

};