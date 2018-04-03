/**
 * Created by root on 2015/12/4.
 */
var SapSource = function () {

    this.txt_cnorDocNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '单据号',
        name: 'cnorDocNo'
    });

    this.typeMap = function () {
        var map = new Map();
        map.put('1', '订单');
        map.put('2', '收货');
        return map;
    };

    this.boolMap = function () {
        var map = new Map();
        map.put(true, '头表');
        map.put(false, '明细');
        return map;
    };

    this.splitMap = function () {
        var map = new Map();
        map.put('02', '是');
        map.put('01', '否');
        return map;
    };

    this.flagMap = function () {
        var map = new Map();
        map.put(true, '是');
        map.put(false, '否');
        return map;
    };

    this.syncMap = function () {
        var map = new Map();
        map.put('0', '未同步');
        map.put('1', '已同步');
        map.put('9', '拒绝');
        return map;
    };

    this.qualityMap = function () {
        var map = new Map();
        map.put('1', '合格');
        map.put('2', '不合格');
        return map;
    };

    this.itemTypeMap = function () {
        var map = new Map();
        map.put('00', '商品');
        map.put('01', '周转箱');
        return map;
    };

    this.recvTypeMap = function () {
        var map = new Map();
        map.put('01', '供应商采购入库');
        map.put('02', '委外采购入库');
        map.put('03', '常规门店退货入库');
        map.put('04', '加盟退货入库');
        map.put('05', '团购退货入库');
        map.put('06', 'RDC调拨入库');
        map.put('07', 'RDC退货入库');
        map.put('08', '转仓入库');
        map.put('09', '转仓入库（311）');
        map.put('10', 'DIY礼盒入库');
        map.put('11', '调拨入库');
        map.put('12', '电商转仓入库');
        map.put('13', '电商B类退货入库');
        map.put('14', '电商退货入库');

        return map;
    };

    this.orderTypeMap = function () {
        var map = new Map();
        map.put('10', '门店');
        map.put('20', '委外加工');
        map.put('30', '特渠');
        map.put('40', 'RDC发货');
        map.put('50', '加盟商');
        map.put('60', '转仓发货');
        map.put('61', '电商转仓发货');
        map.put('70', '退厂');
        map.put('80', '转仓发货(311)');
        map.put('90', 'DIY礼盒');
        map.put('91', '转仓发货(311-WMS)');
        map.put('41', '电商调拨发货');
        map.put('42', '电商发货');
        map.put('71', '电商退厂');

        return map;
    };

    var typeStaus = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '订单'},
                {'value': '2', 'displayValue': '收货单'}
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

    var noticeTypeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '复核实绩'},
                {'value': '2', 'displayValue': '发货实绩'}
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
        store: typeStaus,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });
    this.com_noticeType = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '类型',
        name: 'noticeType',
        emptyText: '请选择',
        store: noticeTypeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });
    var syncStatusStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '0', 'displayValue': 'WMS未同步'},
                {'value': '1', 'displayValue': 'WMS已同步'},
                {'value': '9', 'displayValue': 'WMS拒绝'}
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

    this.com_syncStatus = Ext.create('Ext.form.ComboBox', {
        fieldLabel: 'WMS同步状态',
        name: 'status',
        emptyText: '请选择',
        store: syncStatusStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });


    this.txt_sendCarOrderNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '派车单号',
        name: 'sendCarOrderNo'
    });

    this.txt_recvPlanNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: 'SAP单号',
        name: 'recvNo'
    });


    this.txt_skuCode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '商品代码',
        name: 'skuCode'
    });

    this.txt_productCode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '生产日期',
        name: 'productNo'
    });

    this.txt_inspectnum = Ext.create("Ext.form.field.Text", {
        fieldLabel: '质检批',
        name: 'inspectnum'
    });

};