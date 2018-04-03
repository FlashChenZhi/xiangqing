/**
 * Created by root on 2015/11/9.
 */

var ReplenishConfigSource = function (data) {

    this.com_fromLogicZone = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '补货作业区',
        name: 'fromLogicZone',
        emptyText: '请选择',
        store: data.logicZoneCodesStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_toLogicZone = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '目的作业区',
        name: 'toLogicZone',
        emptyText: '请选择',
        store: data.logicZoneCodesStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.txt_sec_qty = Ext.create("Ext.form.field.Number", {
        fieldLabel: '安全库存',
        name: 'secQty',
        minValue: 0
    });

    this.txt_max_qty = Ext.create("Ext.form.field.Number", {
        fieldLabel: '最大补货数量',
        name: 'maxQty',
        minValue: 0
    });



};