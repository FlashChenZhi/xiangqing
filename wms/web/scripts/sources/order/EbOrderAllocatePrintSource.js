var EbOrderAllocatePrintSource = function (){

    this.carrierCode = "";

    this.init = function(carrierCode) {
        this.carrierCode = carrierCode;
        this.labelPrinterStore.load();
    };

    this.logicZoneStore = Ext.create("Ext.data.JsonStore", {
        fields: ['value','displayValue'],
        scope: this,
        autoLoad: true,
        proxy: {
            type: "ajax",
            url: "wms/ebOrderAllocatePrint/getLogicZones",
            actionMethods: {read: "POST"},
            reader: {
                type: "json",
                rootProperty: "res"
            },
            listeners: {
                exception: function (thiz, response) {
                    var result = Ext.util.JSON.decode(response.responseText);
                    Ext.error(result.msg);
                }
            }
        }
    });

    this.labelPrinterStore = Ext.create("Ext.data.JsonStore", {
        fields: ['value','displayValue'],
        scope: this,
        autoLoad: false,
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = {'carrierCode': this.carrierCode};
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/ebOrderAllocatePrint/getlabelPrinters",
            actionMethods: {read: "POST"},
            reader: {
                type: "json",
                rootProperty: "res"
            },
            listeners: {
                exception: function (thiz, response) {
                    var result = Ext.util.JSON.decode(response.responseText);
                    Ext.error(result.msg);
                }
            }
        }
    });

    this.lazePrinterStore = Ext.create("Ext.data.JsonStore", {
        fields: ['value','displayValue'],
        scope: this,
        autoLoad: false,
        proxy: {
            type: "ajax",
            url: "wms/ebOrderAllocatePrint/getLazePrinters",
            actionMethods: {read: "POST"},
            reader: {
                type: "json",
                rootProperty: "res"
            },
            listeners: {
                exception: function (thiz, response) {
                    var result = Ext.util.JSON.decode(response.responseText);
                    Ext.error(result.msg);
                }
            }
        }
    });


    this.com_logicZone =Ext.create('Ext.form.ComboBox', {
        fieldLabel: '作业区',
        name: 'logicZoneCode',
        emptyText:'请选择',
        store: this.logicZoneStore,
        queryMode: 'local',
        displayField: 'displayValue',
        valueField: 'value',
        editable: false,
        triggerAction: "all",
        value: ''
    });


    this.com_labelPrinter =Ext.create('Ext.form.ComboBox', {
        fieldLabel: '标签打印机',
        name: 'labelPrinterId',
        emptyText:'请选择',
        store: this.labelPrinterStore,
        queryMode: 'local',
        displayField: 'displayValue',
        valueField: 'value',
        editable: false,
        triggerAction: "all",
        value: ''
    });

    this.com_lazePrinter =Ext.create('Ext.form.ComboBox', {
        fieldLabel: '激光打印机',
        name: 'lazePrinterId',
        emptyText:'请选择',
        store: this.lazePrinterStore,
        queryMode: 'local',
        displayField: 'displayValue',
        valueField: 'value',
        editable: false,
        triggerAction: "all",
        value: ''
    });

    this.txt_qty = Ext.create("Ext.form.field.Number", {
        fieldLabel: '数量',
        name: 'qty',
        minValue: 1
    });


    this.txt_batchCount = Ext.create("Ext.form.field.Number", {
        fieldLabel: '批次数',
        name: 'batchCount',
        minValue: 1
    });


};