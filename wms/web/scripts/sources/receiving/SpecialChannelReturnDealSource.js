var SpecialChannelReturnDealSource = function () {
    var DealWayStore = Ext.create("Ext.data.JsonStore", {
        fields: ["name", "value"],
        scope: this,
        data: {
            'res': [
                {'name': '出库A007反拆', 'value': '出库A007反拆'},
                {'name': '出库A001', 'value': '出库A001'},
                {'name': '出库A011', 'value': '出库A011'},
                {'name':'A002退品整理','value':'A002退品整理'}
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

    this.labelPrinterStore = Ext.create("Ext.data.JsonStore", {
        fields: ['value','displayValue'],
        scope: this,
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = {printerType:'1',carrierCode : carrierCode};
            }
        },
        proxy: {
            type: "ajax",
            url: "rubicware/common/getAvailablePrinter",
            actionMethods: {read: "POST"},
            reader: {
                type: "json",
                rootProperty: "res"
            },
            listeners: {
                exception: function (thiz, response) {
                    var result = Ext.util.JSON.decode(response.responseText);
                    Ext.Msg.alert('失败', result.msg);
                    console.log(result);
                }
            }
        }
    });

    this.txt_orderNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '收货单号',
        name: 'orderNo'
    });


    this.combo_DealWay = Ext.create('Ext.form.ComboBox', {
        name: 'dealWay',
        value: '',
        store: DealWayStore,
        queryMode: 'local',
        editable: false,
        displayField: 'name',
        valueField: 'value',
        allowBlank: false,
        blankText: "参数不允许为空"
    });

    this.com_labelPrinter =Ext.create('Ext.form.ComboBox', {
        fieldLabel: '票据打印机',
        name: 'labelPrinterId',
        emptyText:'请选择',
        store: this.labelPrinterStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus : true,
        valueField: 'value'
    });

};
