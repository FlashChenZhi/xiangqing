var AsrsSource = function () {


    var typeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': 'PUTAWAY', 'displayValue': '入库'},
                {'value': 'RETRIEVAL', 'displayValue': '出库'},
                {'value': 'ST2ST', 'displayValue': '站对站'},
                {'value': 'SHELF2SHELF', 'displayValue': '库对库'},
                {'value': 'EMPTY', 'displayValue': '空托盘'},
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

    this.com_type = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '类型',
        name: 'type',
        emptyText: '请选择',
        store: typeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.txt_mckey = Ext.create("Ext.form.field.Text", {
        fieldLabel: 'mckey',
        name: 'mckey'
    });

    this.txt_fromLocation = Ext.create("Ext.form.field.Text", {
        fieldLabel: '源货位',
        name: 'fromLocation'
    });

    this.txt_toLocation = Ext.create("Ext.form.field.Text", {
        fieldLabel: '目标货位',
        name: 'toLocation'
    });

    this.txt_fromStation = Ext.create("Ext.form.field.Text", {
        fieldLabel: '源站台',
        name: 'fromStation'
    });

    this.txt_toStation = Ext.create("Ext.form.field.Text", {
        fieldLabel: '目标站台',
        name: 'toStation'
    });


    this.txt_barcode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '托盘条码',
        name: 'barcode'
    });


    this.txt_summary = Ext.create("Ext.form.field.TextArea", {
        anchor: "100%",
        height: 400
    });

};
