/**
 * Created by root on 2015/11/17.
 */

var InventoryLogSource = function (data) {

    this.logType = function () {
        var map = new Map();
        map.put('1', '收货');
        map.put('2', '上架');
        map.put('3', '拣选');
        map.put('4', '打包');
        map.put('5', '分拣');
        map.put('6', '集货');
        map.put('7', '发货复核');
        map.put('8', '发货确认');
        map.put('9', '出库');
        map.put('10', '补货');
        map.put('11', '移库');
        map.put('12', '码垛');
        map.put('20', '盘亏');
        map.put('21', '盘盈');
        map.put('22', '例外入库');
        map.put('23', '质量变更');
        map.put('24', 'WMS内部维护减少');
        map.put('25', 'WMS内部维护增加');
        map.put('26', 'WMS空货位维护库存');
        map.put('27', '强制移库');
        map.put('28', '发货单位变更');
        map.put('40', '电商取消确认');
        map.put('41', '电商取消分配');
        map.put('29', '库存批次变更');

        return map;
    };

    this.qaStatusMap = function () {
        var map = new Map();
        map.put('1', '待质检');
        map.put('2', '合格');
        map.put('3', '不合格');
        return map;
    };


    var typeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '收货'},
                {'value': '2', 'displayValue': '上架'},
                {'value': '3', 'displayValue': '拣选'},
                {'value': '4', 'displayValue': '打包'},
                {'value': '5', 'displayValue': '分拣'},
                {'value': '6', 'displayValue': '集货'},
                {'value': '7', 'displayValue': '发货复核'},
                {'value': '8', 'displayValue': '发货确认'},
                {'value': '9', 'displayValue': '出库'},
                {'value': '10', 'displayValue': '补货'},
                {'value': '11', 'displayValue': '移库'},
                {'value': '12', 'displayValue': '码垛'},
                {'value': '20', 'displayValue': '盘亏'},
                {'value': '21', 'displayValue': '盘盈'},
                {'value': '22', 'displayValue': '例外入库'},
                {'value': '23', 'displayValue': '质量变更'},
                {'value': '24', 'displayValue': 'WMS内部维护减少'},
                {'value': '25', 'displayValue': 'WMS内部维护增加'},
                {'value': '26', 'displayValue': 'WMS空货位维护库存'},
                {'value': '27', 'displayValue': '强制移库'},
                {'value': '28', 'displayValue': '发货单位变更'},
                {'value': '40', 'displayValue': '电商取消确认'},
                {'value': '41', 'displayValue': '电商取消分配'},
                {'value': '29', 'displayValue': '库存批次变更'}
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

    var searchLocationStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '历史表'},
                {'value': '0', 'displayValue': '当前表'},
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

    this.com_logType = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '日志类型',
        name: 'type',
        emptyText: '请选择',
        store: typeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_containerType = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '容器类型',
        name: 'containerType',
        emptyText: '请选择',
        store: containerTypeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });


    this.txt_beginCreateDate = Ext.create("Ext.form.field.Date", {
        fieldLabel: '创建日期From',
        name: 'beginCreateDate',
        emptyText: "请选择日期",
        selectOnFocus: true,
        format: "Y-m-d",
        altFormats: "Y/m/d|Ymd"
    });

    this.txt_endCreateDate = Ext.create("Ext.form.field.Date", {
        fieldLabel: '创建日期To',
        name: 'endCreateDate',
        emptyText: "请选择日期",
        selectOnFocus: true,
        format: "Y-m-d",
        altFormats: "Y/m/d|Ymd"
    });

    this.com_fromLogicZoneCode = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '源作业区',
        name: 'fromLogicZoneCode',
        emptyText: '请选择',
        store: data.logicZoneCodesStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_toLogicZoneCode = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '目的作业区',
        name: 'toLogicZoneCode',
        emptyText: '请选择',
        store: data.logicZoneCodesStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_searchLocation = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '查询位置',
        name: 'searchLocation',
        emptyText: '请选择',
        store: searchLocationStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.txt_fromLocation = Ext.create("Ext.form.field.Text", {
        fieldLabel: '源货位',
        name: 'fromLocation'
    });

    this.txt_toLocation = Ext.create("Ext.form.field.Text", {
        fieldLabel: '目标货位',
        name: 'toLocation'
    });

    this.txt_fromContainer = Ext.create("Ext.form.field.Text", {
        fieldLabel: '源容器',
        name: 'fromContainer'
    });

    this.txt_toContainer = Ext.create("Ext.form.field.Text", {
        fieldLabel: '目标容器',
        name: 'toContainer'
    });

    this.txt_recvPlanNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '收货单号',
        name: 'recvPlanNo'
    });

    this.txt_batchNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '波次号',
        name: 'batchNo'
    });

    this.txt_sendCarOrderNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '派车单号',
        name: 'sendCarOrderNo'
    });

    this.txt_jobId = Ext.create("Ext.form.field.Text", {
        fieldLabel: '作业ID',
        name: 'jobId'
    });

    this.txt_inventoryId = Ext.create("Ext.form.field.Text", {
        fieldLabel: '库存ID',
        name: 'inventoryId'
    });

    this.txt_logictictsBarcode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '物流码',
        name: 'logictictsBarcode'
    });

    this.txt_retrievalCnorDocNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '出库单SAP单据号',
        name: 'retrievalCnorDocNo'
    });

    this.txt_receivingCnorDocNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '收货单SAP单据号',
        name: 'receivingCnorDocNo'
    });
};