var ZeroPickingReCheckSource = function () {

    var printerStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data:{
            'res':[
                {'value':'1','displayValue':'打印机1'}
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

    this.txt_containerBarcode =  Ext.create("Ext.form.field.Text", {
        fieldLabel: '容器条码',
        name: 'barcode'
    });

    this.txt_productCode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '商品条码',
        name: 'productCode'
    });

    this.txt_metriNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '包材条码',
        name: 'metriNo'
    });

    this.txt_lockNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '锁扣条码',
        name: 'lockNo'
    });

    this.txt_weight = Ext.create("Ext.form.field.Text", {
        fieldLabel: '重量',
        name: 'productCode'
    });

    this.com_printer =Ext.create('Ext.form.ComboBox', {
        fieldLabel: '打印机',
        name: 'printerId',
        emptyText:'请选择',
        store: printerStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus : true,
        valueField: 'value'
    });
};