var BatchMergingSource = function () {

    this.txt_batchNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '波次号',
        name: 'batchNo'
    });


    this.txt_barcode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '条码',
        name: 'barcode'
    });

    this.txt_qty = Ext.create("Ext.form.field.Number", {
        fieldLabel: '数量',
        name: 'qty',
        minValue: 1
    });
};
