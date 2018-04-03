/**
 * Created by Van on 2015/6/4.
 */

var OldToNewRecevingSource = function () {

    this.txt_palletNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '托盘号',
        name: 'palletNo'
    });

    this.txt_Qty = Ext.create("Ext.form.field.Number", {
        fieldLabel: '数量',
        name: 'qty',
        minValue: 0
    });

    this.date_receivingDate = Ext.create("Ext.form.field.Date", {
        fieldLabel: '日期',
        name: 'lot',
        emptyText: "请选择日期",
        selectOnFocus:true,
        format: "Y-m-d",
        altFormats: "Y/m/d|Ymd"
    });

};