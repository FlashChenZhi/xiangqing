var ReturnSkuReceivingSource = function () {

    this.returnSkuTypeMap = function () {
        var map = new Map();
        map.put('T1', '统退');
        map.put('T1', '质量问题');
        map.put('T3', '客退');
        map.put('T4', '临保');
        map.put('T5', '鼠咬');
        map.put('T6', '其他');
        return map;
    };

    var splitBoxTypeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '拆箱'},
                {'value': '2', 'displayValue': '不拆箱'}
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


    var typeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': 'T1', 'displayValue': '统退'},
                {'value': 'T1', 'displayValue': '质量问题'},
                {'value': 'T3', 'displayValue': '客退'},
                {'value': 'T4', 'displayValue': '临保'},
                {'value': 'T5', 'displayValue': '鼠咬'},
                {'value': 'T6', 'displayValue': '其他'}
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

    this.returnSkuTypeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        proxy: {
            type: 'memory',
            reader: {
                type: 'json',
                rootProperty: 'res'
            }
        }
    });


    this.com_splitBoxType = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '拆箱类型',
        name: 'splitBoxType',
        store: splitBoxTypeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        editable: false,
        triggerAction: "all",
        value: '',
        valueField: 'value'
    });

    this.com_returnSkuType = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '退货类型',
        name: 'returnSkuType',
        store: this.returnSkuTypeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        editable: false,
        triggerAction: "all",
        value: '',
        valueField: 'value'
    });

    this.com_returnType = Ext.create('Ext.form.ComboBox', {
        name: 'returnSkuType',
        store: typeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        editable: false,
        triggerAction: "all",
        value: '',
        valueField: 'value'
    });


    //this.birDate = Ext.create("Ext.form.field.Date", {
    //    fieldLabel: '生产日期',
    //    name: 'birDate',
    //    emptyText: "请选择日期",
    //    selectOnFocus:true,
    //    format: "Y-m-d",
    //    altFormats: "Y/m/d|Ymd"
    //});
    //
    //this.txt_tidyQty = Ext.create("Ext.form.field.Number", {
    //    fieldLabel: '整理数量',
    //    name: 'tidyQty',
    //    minValue: 0
    //});
    //
    //this.txt_tidyWeight = Ext.create("Ext.form.field.Number", {
    //    fieldLabel: '重量',
    //    name: 'tidyWeight',
    //    minValue: 0
    //});


};
