var SortingFailHandleSource = function () {

    this.labelPrinterStore = Ext.create("Ext.data.JsonStore", {
        fields: ['value', 'displayValue'],
        scope: this,
        autoLoad: true,
        proxy: {
            type: "ajax",
            url: "wms/sortingFailHandle/getLabelPrintIds",
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




    this.com_labelPrinter = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '标签打印机',
        name: 'labelPrinterId',
        store: this.labelPrinterStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });


    this.com_labelPrinter2 = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '标签打印机',
        name: 'labelPrinterId',
        store: this.labelPrinterStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

};
