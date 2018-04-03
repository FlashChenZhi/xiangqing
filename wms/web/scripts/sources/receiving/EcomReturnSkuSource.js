/**
 * Created by xingf on 2015/5/5.
 */

var EcomReturnSkuSource = function(){

    this.txt_expressNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '快递单号',
        name: 'expressNo'
    });

    this.txt_orderNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '原始订单号',
        name: 'orderNo'
    });

    this.birDate = Ext.create("Ext.form.field.Date", {
        fieldLabel: '生产日期',
        name: 'birDate',
        emptyText: "请选择日期",
        selectOnFocus:true,
        format: "Y-m-d",
        altFormats: "Y/m/d|Ymd"
    });

    var typeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data:{
            'res':[
                {'value':'1','displayValue':'正常'},
                {'value':'2','displayValue':'报废'}
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
    this.com_type =Ext.create('Ext.form.ComboBox', {
        fieldLabel: '入库类型',
        name: 'type',
        emptyText:'请选择',
        store: typeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus : true,
        valueField: 'value'
    });
}