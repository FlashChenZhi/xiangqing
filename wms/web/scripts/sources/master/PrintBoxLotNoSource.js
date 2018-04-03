/**
 * Created by root on 2015/10/14.
 */

var PrintBoxLotNoSource = function(){

    this.labelPrinterStore = Ext.create("Ext.data.JsonStore", {
        fields: ['value', 'displayValue'],
        scope: this,
        autoLoad: true,
        proxy: {
            type: "ajax",
            url: "wms/master/printBoxLotNo/getLabelPrintIds",
            actionMethods: {read: "POST"},
            reader: {
                type: "json",
                rootProperty: "res"
            },
            listeners: {
                exception: function (thiz, response) {
                    extStoreLoadFail(thiz, response);
                }
            }
        }
    });


    var printType = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data:{
            'res':[
                {'value':'1','displayValue':'箱码'},
                {'value':'2','displayValue':'批次码'}
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

    this.com_printType =Ext.create('Ext.form.ComboBox', {
        fieldLabel: '打印类型',
        name: 'printType',
        emptyText:'请选择',
        store: printType,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus : true,
        valueField: 'value'
    });


    this.txt_loteNo = Ext.create("Ext.form.field.Number", {
        fieldLabel: '批次码',
        name: 'loteNo'
    });

    this.txt_qty = Ext.create("Ext.form.field.Number", {
        fieldLabel: '数量',
        name: 'qty',
        minValue: 0
    });

    this.txt_barCode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '箱码',
        name: 'barCode'
    });

    this.com_labelPrinter = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '标签打印机',
        name: 'labelPrinterId',
        store: this.labelPrinterStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

};