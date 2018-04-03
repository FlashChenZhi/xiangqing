var InventorySrouce = function () {

    this.containerTypeMap = function () {
        var map = new Map();
        map.put('0', 'MOCK');
        map.put('1', '托盘');
        map.put('2', '周转箱');
        map.put('3', '纸箱');
        return map;
    };

    this.returnSkuTypeMap = function () {
        var map = new Map();
        map.put('T1', '统退');
        map.put('T2', '质量问题');
        map.put('T3', '客退');
        map.put('T4', '临保');
        map.put('T5', '鼠咬');
        map.put('T6', '其他');
        map.put('5', '再销售');
        return map;
    };

    this.scrapTypeMap = function () {
        var map = new Map();
        map.put('1', '鼠咬');
        map.put('2', '客退');
        map.put('3', '过保');
        return map;
    };

    this.qaStatusMap = function () {
        var map = new Map();
        map.put('1', '待质检');
        map.put('2', '合格');
        map.put('3', '不合格');
        return map;
    };

    this.reservedTypeMap = function () {
        var map = new Map();
        map.put('1', '订单');
        map.put('2', '补货');
        map.put('3', '拣选缺货');
        map.put('4', '补货缺货');
        return map;
    };

    this.containerReservedTypeMap = function () {
        var map = new Map();
        map.put('1', '上架');
        map.put('2', '密集出库');
        return map;
    };

    this.frozenTypeMap = function () {
        var map = new Map();
        map.put('1', '报废冻结');
        map.put('2', '盘库冻结');
        map.put('3', '质检冻结')
        return map;
    };

    this.yesOrNoMap = function () {
        var map = new Map();
        map.put(true, '是');
        map.put(false, '否');
        return map;
    };

    this.transferTypeMap = function () {
        var map = new Map();
        map.put('1', 'A001->A004');
        map.put('2', 'A011->A009');
        map.put('3', 'A011->A007');
        map.put('4', 'A008->A002');

        return map;
    };

    var transferTypeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': 'C001->C004'},
                {'value': '2', 'displayValue': 'C011->C009'},
                {'value': '3', 'displayValue': 'C011->C007'},
                {'value': '4', 'displayValue': 'C008->C002'}
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

    var qaStatusStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '待质检'},
                {'value': '2', 'displayValue': '合格'},
                {'value': '3', 'displayValue': '不合格'}

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

    var containerTypeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '托盘'},
                {'value': '2', 'displayValue': '周转箱'}

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

    var adjustmentTypeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '0', 'displayValue': '盘亏'},
                {'value': '1', 'displayValue': '盘盈'}
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


    var scrapTypeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '鼠咬'},
                {'value': '2', 'displayValue': '客退'},
                {'value': '3', 'displayValue': '过保'}

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


    var changeQaStatus = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', displayValue: '合格'},
                {'value': '2', 'displayValue': '不合格'},
                {'value': '3', 'displayValue': '带质检'}
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

    var overLotNoStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': true, 'displayValue': '过保'},
                {'value': false, 'displayValue': '未过保'}
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

    var exceedLastRetrievalDateStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': true, 'displayValue': '已超过'},
                {'value': false, 'displayValue': '未超过'}
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

    var exceedBestSellDateStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': true, 'displayValue': '已超过'},
                {'value': false, 'displayValue': '未超过'}
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

    var onTransferLineFlagStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': true, 'displayValue': '上线'},
                {'value': false, 'displayValue': '不上线'}
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

    var containerTypeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '0', 'displayValue': 'MOCK'},
                {'value': '1', 'displayValue': '托盘'},
                {'value': '2', 'displayValue': '周转箱'},
                {'value': '3', 'displayValue': '纸箱'}
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


    var printLogisticsBarcodeFlagStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': true, 'displayValue': '已打印'},
                {'value': false, 'displayValue': '未打印'}
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

    var sortingFlagStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': true, 'displayValue': '已分拣'},
                {'value': false, 'displayValue': '未分拣'}
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


    var boolStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': true, 'displayValue': '是'},
                {'value': false, 'displayValue': '否'}
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


    var reservedTypeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '订单预约'},
                {'value': '2', 'displayValue': '补货预约'},
                {'value': '3', 'displayValue': '拣选缺货'},
                {'value': '4', 'displayValue': '补货缺货'}
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

    var frozenTypeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '报废冻结'},
                {'value': '2', 'displayValue': '盘库冻结'}
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


    this.com_reservedType = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '预约类型',
        name: 'reservedType',
        emptyText: '请选择',
        store: reservedTypeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_overLotNoType = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否过保',
        name: 'overLotNo',
        emptyText: '请选择',
        store: overLotNoStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_frozenType = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '冻结类型',
        name: 'frozenType',
        emptyText: '请选择',
        store: frozenTypeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_changeQaStatus = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '质检状态',
        name: 'qaStatus',
        emptyText: '请选择',
        store: changeQaStatus,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_qaStatus = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '质检状态',
        name: 'qaStatus',
        emptyText: '请选择',
        store: qaStatusStore,
        queryMode: 'local',
        displayField: 'displayValue',
        editable: false,
        triggerAction: "all",
        valueField: 'value'
    });

    this.com_adjustmentType = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '调整类型',
        name: 'adjustmentType',
        emptyText: '请选择',
        store: adjustmentTypeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_transferType = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '移库类型',
        name: 'transferType',
        emptyText: '请选择',
        store: transferTypeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_containerType = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '托盘类型',
        name: 'containerType',
        emptyText: '请选择',
        store: containerTypeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_scrapType = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '报废类型',
        name: 'scrapType',
        emptyText: '请选择',
        store: scrapTypeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });


    this.txt_Qty = Ext.create("Ext.form.field.Number", {
        fieldLabel: '数量',
        name: 'qty',
        minValue: 0
    });

    this.txt_transferQty = Ext.create("Ext.form.field.Number", {
        fieldLabel: '移库数量',
        name: 'transferQty',
        decimalPrecision: 3,
        minValue: 0
    });

    this.txt_toLocation = Ext.create("Ext.form.field.Text", {
        fieldLabel: '目标货位',
        name: 'toLocation'
    });

    this.txt_Location = Ext.create("Ext.form.field.Text", {
        fieldLabel: '货位',
        name: 'toLocation'
    });

    this.date_lot = Ext.create("Ext.form.field.Date", {

        fieldLabel: '批次',
        name: 'lot',
        emptyText: "请选择日期",
        selectOnFocus: true,
        format: "Y-m-d",
        altFormats: "Y/m/d|Ymd"
    });


    this.txt_inventoryId = Ext.create("Ext.form.field.Text", {
        fieldLabel: '库存ID',
        name: 'inventoryId',
        minValue: 0
    });

    this.txt_qty = Ext.create("Ext.form.field.Number", {
        fieldLabel: '调整数量',
        name: 'qty',
        decimalPrecision: 3,
        minValue: 0
    });

    this.txt_fromLocationNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '源货位',
        name: 'fromLocationNo'
    });

    this.txt_toLocationNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '目标货位',
        name: 'toLocationNo'
    });

    this.txt_toContainerBarcode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '目标容器号',
        name: 'toContainerBarcode'
    });


    this.com_exceedLastRetrievalDate = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '最晚出库日',
        name: 'exceedLastRetrievalDate',
        emptyText: '请选择',
        store: exceedLastRetrievalDateStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_exceedBestSellDate = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '最佳销售期',
        name: 'exceedBestSellDate',
        emptyText: '请选择',
        store: exceedBestSellDateStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_onTransferLineFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否投线',
        name: 'onTransferLineFlag',
        emptyText: '请选择',
        store: onTransferLineFlagStore,
        queryMode: 'local',
        displayField: 'displayValue',
        editable: false,
        triggerAction: "all",
        valueField: 'value'
    });

    this.com_printLogisticsBarcodeFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '打印物流码',
        name: 'printLogisticsBarcodeFlag',
        emptyText: '请选择',
        store: printLogisticsBarcodeFlagStore,
        queryMode: 'local',
        displayField: 'displayValue',
        editable: false,
        triggerAction: "all",
        valueField: 'value'
    });

    this.com_sortingFlagFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否已分拣',
        name: 'sortingFlag',
        emptyText: '请选择',
        store: sortingFlagStore,
        queryMode: 'local',
        displayField: 'displayValue',
        editable: false,
        triggerAction: "all",
        valueField: 'value'
    });


    this.com_frozenFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否冻结',
        name: 'frozenFlag',
        emptyText: '请选择',
        store: boolStore,
        queryMode: 'local',
        displayField: 'displayValue',
        editable: false,
        triggerAction: "all",
        valueField: 'value'
    });

    this.com_reservedFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否预约',
        name: 'reservedFlag',
        emptyText: '请选择',
        store: boolStore,
        queryMode: 'local',
        displayField: 'displayValue',
        editable: false,
        triggerAction: "all",
        valueField: 'value'
    });


    this.com_returnSkuFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否退品',
        name: 'returnSkuFlag',
        emptyText: '请选择',
        store: boolStore,
        queryMode: 'local',
        displayField: 'displayValue',
        editable: false,
        triggerAction: "all",
        valueField: 'value'
    });

    this.com_containerType = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '容器类型',
        name: 'containerType',
        emptyText: '请选择',
        store: containerTypeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        editable: false,
        triggerAction: "all",
        valueField: 'value'
    });

    this.txt_logisticsBarcode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '物流码',
        name: 'logisticsBarcode'
    });

    this.num_overShipDate = Ext.create("Ext.form.field.Number", {
        fieldLabel: '超最晚出库天数',
        name: 'overShipDate',
        minValue: 0
    });

    this.num_overBestSellDate = Ext.create("Ext.form.field.Number", {
        fieldLabel: '超最最佳销售日天数',
        name: 'overBestSellDate',
        minValue: 0
    });

};