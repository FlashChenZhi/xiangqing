var LogSearchSource = function (){

    this.systemCodeMap= function() {
        var map = new Map();
        map.put('WMS','WMS');
        map.put('AGC','AGC');
        map.put('SAC','SAC');
        map.put('SAC01','SAC01');
        map.put('SAC02','SAC02');
        map.put('SAC03','SAC03');
        map.put('SAC04','SAC04');
        map.put('INTERFACE','SAP接口');
        map.put('LIFTER','升降机');

        return map;
    };

    this.levelMap= function() {
        var map = new Map();
        map.put('1','DEBUG');
        map.put('2','INFO');
        map.put('3','WARN');
        map.put('4','ERROR');

        return map;
    };

    var codeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '401001', 'displayValue': '401001:与Oracle数据库断开连接'},
                {'value': '401002', 'displayValue': '401002:系统错误'},
                {'value': '401003', 'displayValue': '401003:业务异常'},
                {'value': '401004', 'displayValue': '401004:乐观锁异常'},
                {'value': '204001', 'displayValue': '204001:波次分配引擎'},
                {'value': '204002', 'displayValue': '204002:波次管理'},
                {'value': '404001', 'displayValue': '404001:存在多条状态为作业中的波次'},
                {'value': '206001', 'displayValue': '206001:订单分配完成'},
                {'value': '206002', 'displayValue': '206002:分配信息'},
                {'value': '206003', 'displayValue': '206003:分配失败'},
                {'value': '206004', 'displayValue': '206004:调拨单分配信息'},
                {'value': '406001', 'displayValue': '406001:分配引擎异常'},
                {'value': '406002', 'displayValue': '406002:预分配失败'},
                {'value': '207001', 'displayValue': '207001:订单拣选完成'},
                {'value': '207002', 'displayValue': '207002:订单拣选出库'},
                {'value': '207003', 'displayValue': '207003:拣选纸箱生成'},
                {'value': '207004', 'displayValue': '207004:纸箱打印透支'},
                {'value': '207005', 'displayValue': '207005:拣选作业体积超出'},
                {'value': '207006', 'displayValue': '207006:订单拣选取消'},
                {'value': '207007', 'displayValue': '207007:拣选作业创建'},
                {'value': '207008', 'displayValue': '207008:拣选作业创建失败'},
                {'value': '207009', 'displayValue': '207009:拣选作业完成'},
                {'value': '207010', 'displayValue': '207010:纸箱生成明细'},
                {'value': '207011', 'displayValue': '207011:拣选作业创建耗时'},
                {'value': '207012', 'displayValue': '207012:订单缺货拣选'},
                {'value': '207013', 'displayValue': '207013:DPS拣选设定等待补货'},
                {'value': '407001', 'displayValue': '407001:纸箱创建异常'},
                {'value': '407002', 'displayValue': '407002:自动生成拣选作业失败,货位存在其他波次'},
                {'value': '407003', 'displayValue': '407003:订单拣选出库异常'},
                {'value': '407004', 'displayValue': '407004:拣选完成异常'},
                {'value': '407005', 'displayValue': '407005:拣选作业创建时异常'},
                {'value': '209001', 'displayValue': '209001:预分拣'},
                {'value': '409001', 'displayValue': '409001:预分拣异常'},
                {'value': '212001', 'displayValue': '212001:发货确认'},
                {'value': '212002', 'displayValue': '212002:发货确认汇总'},
                {'value': '413001', 'displayValue': '413001:移库单异常'},
                {'value': '415001', 'displayValue': '415001:不良品出库异常'},
                {'value': '415002', 'displayValue': '415002:没有可用的站台'},
                {'value': '415003', 'displayValue': '415003:站台的代理货位为空'},
                {'value': '415004', 'displayValue': '415004:没有足够的密集型DPS拣选货位'},
                {'value': '415005', 'displayValue': '415005:没有可用的迂回货位'},
                {'value': '415006', 'displayValue': '415006:自动拣选出库引擎异常'},
                {'value': '416001', 'displayValue': '416001:DPS异常'},
                {'value': '216001', 'displayValue': '216001:DPS分组'},
                {'value': '216002', 'displayValue': '216002:DPS强制再点灯'},
                {'value': '217001', 'displayValue': '217001:补货'},
                {'value': '217002', 'displayValue': '217002:密集架补货'},
                {'value': '417001', 'displayValue': '417001:补货异常'},
                {'value': '417002', 'displayValue': '417002:密集架补货异常'},
                {'value': '217003', 'displayValue': '217003:缺货补货拣选'},
                {'value': '220001', 'displayValue': '220001:SKU导入失败'},
                {'value': '220002', 'displayValue': '220002:商品信息修改'},
                {'value': '221001', 'displayValue': '221001:货位信息修改'},
                {'value': '440001', 'displayValue': '440001:LED异常'},
                {'value': '242001', 'displayValue': '242001:数据清理'},
                {'value': '242002', 'displayValue': '242002:序列重建'},
                {'value': '442001', 'displayValue': '442001:数据清理异常'},
                {'value': '442002', 'displayValue': '442002:序列重建异常'},
                {'value': '243001', 'displayValue': '243001:任务灯'},
                {'value': '243002', 'displayValue': '243002:任务灯重连'},
                {'value': '443001', 'displayValue': '443001:任务灯异常'},
                {'value': '244001', 'displayValue': '244001:数据纠正'},
                {'value': '444001', 'displayValue': '444001:数据纠正异常'},
                {'value': '250001', 'displayValue': '250001:AGC消息中心'},
                {'value': '250002', 'displayValue': '250002:AGC消息'},
                {'value': '250003', 'displayValue': '250003:SAC消息'},
                {'value': '251001', 'displayValue': '251001:AGC日志信息'},
                {'value': '251003', 'displayValue': '251003:ASRS日志信息'},
                {'value': '251004', 'displayValue': '251004:整库'},
                {'value': '451002', 'displayValue': '451002:AGC异常日志'},
                {'value': '451003', 'displayValue': '451003:ASRS异常日志'},
                {'value': '451004', 'displayValue': '451004:整库异常'},
                {'value': '252001', 'displayValue': '252001:SAC日志信息'},
                {'value': '452001', 'displayValue': '452001:SAC异常日志'},
                {'value': '453001', 'displayValue': '453001:SAP采购单错误信息'},
                {'value': '453002', 'displayValue': '453002:SAP订单错误信息'},
                {'value': '453003', 'displayValue': '453003:SAP商品错误信息'},
                {'value': '453004', 'displayValue': '453004:SAP供应商错误信息'},
                {'value': '253001', 'displayValue': '253001:商品主数据修改'},
                {'value': '254001', 'displayValue': '254001:升降机BCR信息'},
                {'value': '454001', 'displayValue': '454001:升降机BCR异常'},
                {'value': '255001', 'displayValue': '255001:Hibris新建订单推送信息'},
                {'value': '255002', 'displayValue': '255002:Hibris新建退货单推送信息'},
                {'value': '255003', 'displayValue': '255003:Hibris取消订单订单推送信息'},
                {'value': '255012', 'displayValue': '255012:Hibris取消退货单推送信息'},
                {'value': '255004', 'displayValue': '255004:wms新建订单返回信息'},
                {'value': '255005', 'displayValue': '255005:wms新建退货单返回信息'},
                {'value': '255006', 'displayValue': '255006:wms取消订单返回信息'},
                {'value': '255011', 'displayValue': '255011:wms取消退货单返回信息'},
                {'value': '255007', 'displayValue': '255007:wms推送订单状态'},
                {'value': '255008', 'displayValue': '255008:wms推送退货单状态'},
                {'value': '255009', 'displayValue': '255009:Hibris返回订单推送信息'},
                {'value': '255010', 'displayValue': '255010:Hibris返回退货单推送信息'}
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



    this.com_code = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '日志代码',
        name: 'code',
        emptyText: '请选择',
        store: codeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });


    this.txt_name = Ext.create("Ext.form.field.Text", {
        fieldLabel: '日志名称',
        name: 'name'
    });

    var systemCodeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': 'WMS', 'displayValue': 'WMS'},
                {'value': 'AGC', 'displayValue': 'AGC'},
                {'value': 'SAC', 'displayValue': 'SAC'},
                {'value': 'SAC01', 'displayValue': 'SAC01'},
                {'value': 'SAC02', 'displayValue': 'SAC02'},
                {'value': 'SAC03', 'displayValue': 'SAC03'},
                {'value': 'SAC04', 'displayValue': 'SAC04'},
                {'value': 'INTERFACE', 'displayValue': 'SAP接口'},
                {'value': 'LIFTER', 'displayValue': '升降机'}
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

    var levelStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': 'DEBUG'},
                {'value': '2', 'displayValue': 'INFO'},
                {'value': '3', 'displayValue': 'WARN'},
                {'value': '4', 'displayValue': 'ERROR'}
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

    this.txt_simpleSummary = Ext.create("Ext.form.field.Text", {
        fieldLabel: '备注',
        name: 'summary'
    });

    this.com_systemCode = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '系统代码',
        name: 'systemCode',
        emptyText: '请选择',
        store: systemCodeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_level = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '日志级别',
        name: 'level',
        emptyText: '请选择',
        store: levelStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
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

    this.txt_summary = Ext.create("Ext.form.field.TextArea", {
        anchor: "100%",
        height: 400
    });

};
