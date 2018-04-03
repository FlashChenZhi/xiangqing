var EbOrderSearchSource = function () {
    this.statusMap = function () {
        var map = new Map();
        map.put('1', '待分拣');
        map.put('2', '分拣中');
        map.put('3', '复核中');
        map.put('4', '待发货');
        map.put('8', '完成');
        return map;
    };

    this.typeMap = function () {
        var map = new Map();
        map.put('0', '销售订单');
        map.put('0', '退货订单');
        map.put('0', '调拨单');
        return map;
    };

    this.isMantyMap = function () {
        var map = new Map();
        map.put(true, '是');
        map.put(false, '否');
        return map;
    };

    this.cancelFlagMap = function () {
        var map = new Map();
        map.put('0', '未取消');
        map.put('1', '取消中');
        map.put('2', '取消完成');
        return map;
    };

    this.preAllocateFlagMap = function () {
        var map = new Map();
        map.put(false, '未分配');
        map.put(true, '已分配');
        return map;
    };

    this.finishFlagMap = function () {
        var map = new Map();
        map.put(true, '完成');
        map.put(false, '未完成');
        return map;
    };

    this.printedFlagMap = function () {
        var map = new Map();
        map.put(true, '已打印');
        map.put(false, '未打印');
        return map;
    };

    var orderTypeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["type", "typeValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'type': '销售订单', 'typeValue': '0'},
                {'type': '退货订单', 'typeValue': '0'},
                {'type': '调拨单', 'typeValue': '0'}
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

    var mantyFlagStore = Ext.create("Ext.data.JsonStore", {
        fields: ["isManty", "isMantyValue"],
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

    var printFlagStore = Ext.create("Ext.data.JsonStore", {
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

    var finishStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': true, 'displayValue': '已完成'},
                {'value': false, 'displayValue': '未完成'}
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

    var printTypeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '面单'},
                {'value': '2', 'displayValue': '装箱单'},
                {'value': '3', 'displayValue': '面单和装箱单'}
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

    var preAllocateStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': false, 'displayValue': '未分配'},
                {'value': true, 'displayValue': '已分配'}
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

    var cancelFlagStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '0', 'displayValue': '未取消'},
                {'value': '1', 'displayValue': '取消中'},
                {'value': '2', 'displayValue': '取消完成'}
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

    var statusStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '待分拣'},
                {'value': '2', 'displayValue': '分拣中'},
                {'value': '3', 'displayValue': '复核中'},
                {'value': '4', 'displayValue': '待发货'},
                {'value': '8', 'displayValue': '完成'}
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

    this.text_retrievalOrderBatchCode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '批次号',
        name: 'retrievalOrderBatchCode'
    });

    this.text_orderNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '订单号',
        name: 'orderNo'
    });

    this.text_shipperDocNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '发货方单据号',
        name: 'shipperDocNo'
    });

    this.text_limitDate = Ext.create("Ext.form.field.Text", {
        fieldLabel: '发货时限',
        name: 'limitFinishDate'
    });

    this.text_cnorShopNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '货主店铺',
        name: 'cnorShopNo'
    });

    this.text_customerOrderNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '客户主订单号',
        name: 'customerOrderNo'
    });

    this.com_status = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '订单状态',
        name: 'status',
        emptyText: '请选择',
        store: statusStore,
        queryMode: 'local',
        displayField: 'status',
        selectOnFocus: true,
        valueField: 'statusValue'
    });


    this.com_orderType = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '订单类型',
        name: 'type',
        emptyText: '请选择',
        store: orderTypeStore,
        queryMode: 'local',
        displayField: 'type',
        selectOnFocus: true,
        valueField: 'typeValue'
    });


    this.com_mantyFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否大订单',
        name: 'mantyFlag',
        emptyText: '请选择',
        store: mantyFlagStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_printFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否已打印',
        name: 'printFlag',
        emptyText: '请选择',
        store: printFlagStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_preAllocatedFinishFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否已预分配',
        name: 'preAllocatedFinishFlag',
        emptyText: '请选择',
        store: finishStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_allocatedFinishFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否已分配',
        name: 'allocatedFinishFlag',
        emptyText: '请选择',
        store: finishStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_pickedFinishFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否已拣选',
        name: 'pickedFinishFlag',
        emptyText: '请选择',
        store: finishStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_packingFinishFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否已打包',
        name: 'packingFinishFlag',
        emptyText: '请选择',
        store: finishStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_mergingFinishFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否已集货',
        name: 'mergingFinishFlag',
        emptyText: '请选择',
        store: finishStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_shippingFinishFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否已发货',
        name: 'shippingFinishFlag',
        emptyText: '请选择',
        store: finishStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.text_expressNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '运单号',
        name: 'expressNo'
    });

    this.text_cnorDocNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: 'SAP单据号',
        name: 'cnorDocNo'
    });

    this.text_platformOrderNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '外部订单号',
        name: 'platformOrderNo'
    });


    this.com_printType = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '打印类型',
        name: 'printType',
        emptyText: '请选择',
        store: printTypeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_preAllocateFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '预分配标志',
        name: 'preAllocateFlag',
        emptyText: '请选择',
        store: preAllocateStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_cancelFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '取消标志',
        name: 'cancelFlag',
        emptyText: '请选择',
        store: cancelFlagStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });


    this.txt_orderDate = Ext.create("Ext.form.field.Date", {
        fieldLabel: '订单日',
        name: 'orderDate',
        emptyText: "请选择日期",
        selectOnFocus: true,
        format: "Y-m-d",
        altFormats: "Y/m/d|Ymd"
    });

    this.date_orderDateFrom = Ext.create("Ext.form.field.Date", {
        fieldLabel: '订单时间开始',
        name: 'orderDateFrom',
        emptyText: "订单时间开始",
        selectOnFocus: true,
        format: "Y-m-d H:i:s",
    });
    this.date_orderDateTo = Ext.create("Ext.form.field.Date", {
        name: 'orderDateTo',
        emptyText: "订单时间截止",
        selectOnFocus: true,
        format: "Y-m-d H:i:s",
    });

    this.date_orderFinishDateFrom = Ext.create("Ext.form.field.Date", {
        fieldLabel: '订单完成时间开始',
        name: 'orderFinishDateFrom',
        emptyText: "订单完成时间开始",
        selectOnFocus: true,
        format: "Y-m-d H:i:s",
    });

    this.date_orderFinishDateTo = Ext.create("Ext.form.field.Date", {
        name: 'orderFinishDateTo',
        emptyText: "订单完成时间截止",
        selectOnFocus: true,
        format: "Y-m-d H:i:s",
    });


    this.date_pickingFinishDateFrom = Ext.create("Ext.form.field.Date", {
        fieldLabel: '订单拣选时间开始',
        name: 'pickingFinishDateFrom',
        selectOnFocus: true,
        format: "Y-m-d H:i:s",
    });

    this.date_pickingFinishDateTo = Ext.create("Ext.form.field.Date", {
        name: 'pickingFinishDateTo',
        emptyText: "订单拣选时间截止",
        selectOnFocus: true,
        format: "Y-m-d H:i:s",
    });

    this.date_packingFinishDateFrom = Ext.create("Ext.form.field.Date", {
        fieldLabel: '订单打包时间开始',
        name: 'packingFinishDateFrom',
        selectOnFocus: true,
        format: "Y-m-d H:i:s",
    });

    this.date_packingFinishDateTo = Ext.create("Ext.form.field.Date", {
        name: 'packingFinishDateTo',
        emptyText: "订单打包时间截止",
        selectOnFocus: true,
        format: "Y-m-d H:i:s",
    });

    this.date_mergingFinishDateFrom = Ext.create("Ext.form.field.Date", {
        fieldLabel: '订单集货时间开始',
        name: 'mergingFinishDateFrom',
        selectOnFocus: true,
        format: "Y-m-d H:i:s",
    });

    this.date_mergingFinishDateTo = Ext.create("Ext.form.field.Date", {
        name: 'mergingFinishDateTo',
        emptyText: "订单集货时间截止",
        selectOnFocus: true,
        format: "Y-m-d H:i:s",
    });

    this.txt_batchNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '批次号',
        name: 'batchNo'
    });

    this.txt_palletNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '集货托盘号',
        name: 'palletNo'
    });

    this.txt_phone = Ext.create("Ext.form.field.Text", {
        fieldLabel: '收货人电话',
        name: 'phone'
    });

    this.txt_name = Ext.create("Ext.form.field.Text", {
        fieldLabel: '收货人姓名',
        name: 'name'
    });

    this.com_weightFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否已称重',
        name: 'weightFlag',
        emptyText: '请选择',
        store: finishStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });


};