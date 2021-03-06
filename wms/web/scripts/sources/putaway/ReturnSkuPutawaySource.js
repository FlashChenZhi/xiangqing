var ReturnSkuPutawaySource = function () {

    var typeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': 'T1', 'displayValue': '统退'},
                {'value': 'T2', 'displayValue': '质量问题'},
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

    var skuStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': 'test1'},
                {'value': '2', 'displayValue': 'test2'},
                {'value': '3', 'displayValue': 'test3'}
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

    var outletStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '门店1'},
                {'value': '2', 'displayValue': '门店2'}
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

    var providerStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '供应商1'},
                {'value': '2', 'displayValue': '供应商2'}
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

    var printerStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '打印机1'},
                {'value': '3', 'displayValue': '打印机2'}
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


    this.backcode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '退货单号',
        name: 'backcode'
    });


    this.number = Ext.create("Ext.form.field.Text", {
        fieldLabel: '基本数量',
        name: 'number'
    });

    this.provider = Ext.create("Ext.form.field.Text", {
        fieldLabel: '供应商',
        name: 'from'
    });

    this.txt_skuName = Ext.create("Ext.form.field.Text", {
        fieldLabel: '商品名称',
        name: 'skuName'
    });

    this.mark = Ext.create("Ext.form.field.Text", {
        fieldLabel: '备注',
        name: 'mark'
    });

    this.weight = Ext.create("Ext.form.field.Text", {
        fieldLabel: '重量',
        name: 'weight'
    });

    this.com_type = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '操作类型',
        name: 'type',
        emptyText: '请选择',
        store: typeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_sku = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '商品代码',
        name: 'skuId',
        emptyText: '请选择',
        store: skuStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_lutlet = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '门店',
        name: 'lutletId',
        emptyText: '请选择',
        store: outletStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_provider = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '供应商',
        name: 'providerId',
        emptyText: '请选择',
        store: providerStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_printer = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '打印机',
        name: 'printerId',
        emptyText: '请选择',
        store: printerStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });


    this.birDate = Ext.create("Ext.form.field.Date", {
        fieldLabel: '生产日期',
        name: 'birDate',
        emptyText: "请选择日期",
        selectOnFocus: true,
        format: "Y-m-d",
        altFormats: "Y/m/d|Ymd"
    });

    this.txt_tidyQty = Ext.create("Ext.form.field.Number", {
        fieldLabel: '整理数量',
        name: 'tidyQty',
        minValue: 0
    });

    this.txt_tidyWeight = Ext.create("Ext.form.field.Number", {
        fieldLabel: '重量',
        name: 'tidyWeight',
        minValue: 0
    });

    this.txt_packingQty = Ext.create("Ext.form.field.Number", {
        fieldLabel: '打包数量',
        name: 'packingQty',
        decimalPrecision: 3,
        minValue: 0
    });

    this.txt_packingEom = Ext.create("Ext.form.field.Text", {
        fieldLabel: '打包单位',
        name: 'packingEom',
    });

    this.txt_packingWeight = Ext.create("Ext.form.field.Number", {
        fieldLabel: '打包重量',
        name: 'packingWeight',
        minValue: 0
    });

    this.txt_type = Ext.create("Ext.form.field.Text", {
        fieldLabel: '退品类型',
        name: 'type'
    });

    this.txt_putawayLocationNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '上架货位',
        name: 'putawayLocationNo'
    });

    this.txt_putawayQty = Ext.create("Ext.form.field.Number", {
        fieldLabel: '上架数量',
        name: 'putawayQty',
        minValue: 0
    });

    this.txt_skuName = Ext.create("Ext.form.field.Text", {
        fieldLabel: '商品名称',
        name: 'skuName'
    });

    this.returnTypeMap = function () {
        var map = new Map();
        map.put('T1', '统退');
        map.put('T1', '质量问题');
        map.put('T3', '客退');
        map.put('T4', '临保');
        map.put('T5', '鼠咬');
        map.put('T6', '其他');
        return map;
    };

};
