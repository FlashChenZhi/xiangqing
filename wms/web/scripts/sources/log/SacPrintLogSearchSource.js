var SacPrintLogSearchSource = function () {

    this.statusMap = function () {
        var map = new Map();
        map.put('0', '正常');
        map.put('1', '载荷错误');
        map.put('5', '无数据');
        map.put('6', '其他异常');

        return map;
    };

    this.dummpMap = function () {
        var map = new Map();
        map.put(true, '是');
        map.put(false, '否');

        return map;
    };

    this.updateProductDateFlagMap = function () {
        var map = new Map();
        map.put(true, '已更新');
        map.put(false, '正常');

        return map;
    };

    this.pushRedisSuccessMap = function () {
        var map = new Map();
        map.put(true, '成功');
        map.put(false, '失败');

        return map;
    };

    this.printSuccessMap = function () {
        var map = new Map();
        map.put(true, '成功');
        map.put(false, '失败');

        return map;
    };

    var sacNameStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': 'SAC01', 'displayValue': 'SAC01'},
                {'value': 'SAC03', 'displayValue': 'SAC03'}
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

    var targetQueueNameStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': 'QUEUE_SAC01LB01_SAC01LB02', 'displayValue': 'QUEUE_SAC01LB01_SAC01LB02'},
                {'value': 'QUEUE_SAC01LB03_SAC01LB04', 'displayValue': 'QUEUE_SAC01LB03_SAC01LB04'},
                {'value': 'QUEUE_SAC03LB05', 'displayValue': 'QUEUE_SAC03LB05'},
                {'value': 'QUEUE_SAC03LB06', 'displayValue': 'QUEUE_SAC03LB06'},
                {'value': 'QUEUE_SAC03LB07', 'displayValue': 'QUEUE_SAC03LB07'},
                {'value': 'QUEUE_SAC03LB08', 'displayValue': 'QUEUE_SAC03LB08'}
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
                {'value': '0', 'displayValue': '正常'},
                {'value': '1', 'displayValue': '载荷错误'},
                {'value': '5', 'displayValue': '无数据'},
                {'value': '9', 'displayValue': '其他异常'}
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

    var pushRedisSuccessStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': true, 'displayValue': '成功'},
                {'value': false, 'displayValue': '失败'}

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

    var printSuccessStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': true, 'displayValue': '成功'},
                {'value': false, 'displayValue': '失败'}

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

    var updateProductDateFlagStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': true, 'displayValue': '已更新'},
                {'value': false, 'displayValue': '正常'}

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

    this.txt_groupNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '打印组号',
        name: 'groupNo'
    });


    this.txt_barcode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '条码',
        name: 'barcode'
    });

    this.txt_logisticsBarcode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '物流码',
        name: 'logisticsBarcode'
    });

    this.txt_allocateCount = Ext.create("Ext.form.field.Number", {
        fieldLabel: '分配次数',
        name: 'allocateCount'
    });


    this.txt_shortErrorMsg = Ext.create("Ext.form.field.Text", {
        fieldLabel: '异常简称',
        name: 'shortErrorMsg'
    });


    this.com_sacName = Ext.create('Ext.form.ComboBox', {
        fieldLabel: 'SAC名称',
        name: 'sacName',
        emptyText: '请选择',
        store: sacNameStore,
        queryMode: 'local',
        displayField: 'displayValue',
        editable: false,
        triggerAction: "all",
        valueField: 'value'
    });

    this.com_targetQueueName = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '目标队列',
        name: 'targetQueueName',
        emptyText: '请选择',
        store: targetQueueNameStore,
        queryMode: 'local',
        displayField: 'displayValue',
        editable: false,
        triggerAction: "all",
        valueField: 'value'
    });


    this.com_status = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '打印状态',
        name: 'status',
        emptyText: '请选择',
        store: statusStore,
        queryMode: 'local',
        displayField: 'displayValue',
        editable: false,
        triggerAction: "all",
        valueField: 'value'
    });


    this.date_beginDate = Ext.create("Ext.form.field.Date", {
        fieldLabel: '开始时间',
        name: 'beginDate',
        emptyText: "请选择日期",
        selectOnFocus: true,
        format: "Y-m-d H:i:s",
        altFormats: "Y/m/d|Ymd"
    });

    this.date_endDate = Ext.create("Ext.form.field.Date", {
        fieldLabel: '结束时间',
        name: 'endDate',
        emptyText: "请选择日期",
        selectOnFocus: true,
        format: "Y-m-d H:i:s",
        altFormats: "Y/m/d|Ymd"
    });

    this.com_pushRedisSuccess = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '推送REDIS',
        name: 'pushRedisSuccess',
        emptyText: '请选择',
        store: pushRedisSuccessStore,
        queryMode: 'local',
        displayField: 'displayValue',
        editable: false,
        triggerAction: "all",
        valueField: 'value'
    });

    this.com_printSuccess = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '打印结果',
        name: 'printSuccess',
        emptyText: '请选择',
        store: printSuccessStore,
        queryMode: 'local',
        displayField: 'displayValue',
        editable: false,
        triggerAction: "all",
        valueField: 'value'
    });


    this.com_updateProductDateFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '更新批次',
        name: 'updateProductDateFlag',
        emptyText: '请选择',
        store: updateProductDateFlagStore,
        queryMode: 'local',
        displayField: 'displayValue',
        editable: false,
        triggerAction: "all",
        valueField: 'value'
    });

};
