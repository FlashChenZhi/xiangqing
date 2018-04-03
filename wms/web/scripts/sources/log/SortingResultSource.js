/**
 * Created by root on 2015/11/19.
 */
var SortingResultSource = function () {
    
    this.statusMap = function () {
        var map = new Map();
        map.put('0','正常');
        map.put('1','目的地使用不可');
        map.put('2','分歧错误');
        map.put('3','目的地未定义');
        map.put('4','目的地指示为收信');
        map.put('5','目的地请求为送信');
        map.put('6','目的地指示接受慢');

        return map;
    };

    var statusStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '0', 'displayValue': '正常'},
                {'value': '1', 'displayValue': '目的地使用不可'},
                {'value': '2', 'displayValue': '分歧错误'},
                {'value': '3', 'displayValue': '目的地未定义'},
                {'value': '4', 'displayValue': '目的地指示未收信'},
                {'value': '5', 'displayValue': '目的地请求未送信'},
                {'value': '6', 'displayValue': '目的地指示接收慢'}
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

    this.errMap = function () {
        var map = new Map();
        map.put('0','正常');
        map.put('1','货长异常（MAX_OVER）');
        map.put('2','货长异（MIN_UNDER）');
        map.put('3','货长变化异常');
        map.put('4','间隔异常（前）');
        map.put('5','间隔异常（后）');
        map.put('6','异常滑下（后）');
        map.put('7','异常滑下（前）');
        map.put('8','有数据无载荷');
        map.put('9','IJP错误');
        return map;
    };
    
    this.errorCodeMap = function () {
        var map = new Map();
        map.put('0001','获取不到库存订单信息');
        map.put('0002','根据物流码获取不到容器数据');
        map.put('0003','物流码为空');
        map.put('0004','物流码NO READ');
        map.put('0005','透支打印等待生成纸箱');
        return map;
    };

    var errStatusStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '0', 'displayValue': '正常'},
                {'value': '1', 'displayValue': '货长异常（MAX_OVER）'},
                {'value': '2', 'displayValue': '货长异（MIN_UNDER）'},
                {'value': '3', 'displayValue': '货长变化异常'},
                {'value': '4', 'displayValue': '间隔异常（前）'},
                {'value': '5', 'displayValue': '间隔异常（后）'},
                {'value': '6', 'displayValue': '异常滑下（后）'},
                {'value': '7', 'displayValue': '异常滑下（前）'},
                {'value': '8', 'displayValue': '有数据无载荷'},
                {'value': '9', 'displayValue': 'IJP错误'}
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

    var errCodeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '0001', 'displayValue': '获取不到库存订单信息'},
                {'value': '0002', 'displayValue': '根据物流码获取不到容器数据'},
                {'value': '0003', 'displayValue': '物流码为空'},
                {'value': '0004', 'displayValue': '物流码NO READ'},
                {'value': '0005', 'displayValue': '透支打印等待生成纸箱'}
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


    this.com_status = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '状态',
        name: 'status',
        emptyText: '请选择',
        store: statusStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_errStatus = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '异常状态',
        name: 'errStatus',
        emptyText: '请选择',
        store: errStatusStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });


    this.com_errCode = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '异常简称',
        name: 'errCode',
        emptyText: '请选择',
        store: errCodeStore,
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

    this.txt_logictictsBarcode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '物流码',
        name: 'containerBarcode'
    });

};
