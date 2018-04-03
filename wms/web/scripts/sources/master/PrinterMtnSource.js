/**
 * Created by Van on 2015/8/11.
 */
this.PrinterMtnSource = function () {

    var printerTypeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '激光'},
                {'value': '2', 'displayValue': '标签'},
                {'value': '3', 'displayValue': '票据'}
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
    var printerStatusStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '0', 'displayValue': '正常'},
                {'value': '1', 'displayValue': '禁用'}
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

    this.com_printerType = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '打印机类型',
        name: 'printerType',
        emptyText: '请选择',
        store: printerTypeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_printerStatus = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '打印机状态',
        name: 'printerStatus',
        emptyText: '请选择',
        store: printerStatusStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });


    this.txt_printName = Ext.create("Ext.form.field.Text", {
        fieldLabel: '打印机名称',
        name: 'printName'
    });

    this.txt_id = Ext.create("Ext.form.field.Text",{
        name:'id',
        hidden:true
    });

    this.txt_IP = Ext.create("Ext.form.field.Text",{
        fieldLabel: 'IP',
        name:'ip',
    });
};