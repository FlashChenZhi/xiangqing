/**
 * Created by Van on 2015/6/4.
 */

var MergeDifferencesCheckSource = function () {

    this.txt_deliveryOrderNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: 'SAP单号',
        name: 'deliveryOrderNo'
    });

    this.txt_waveNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '波次号',
        name: 'waveNo'
    });

};