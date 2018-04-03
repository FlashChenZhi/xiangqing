/**
 * Created by root on 2016/2/24.
 */
var ChangeConfigSource = function () {


    this.valueStroeChange = function (key) {
        valueStore.removeAll();
        if (key == 'CHECK_LAST_RETRIEVAL_DATE') {
            valueStore.add({'value': '0', 'displayValue': '不检查'});
            valueStore.add({'value': '1', 'displayValue': '检查最晚出库日'});
            valueStore.add({'value': '2', 'displayValue': '检查最佳销售期'});
        }
        else if (key == 'PRODUCT_DATE_TOLERATE') {
            for (var i = 0; i <= 365; i++)
                valueStore.add({'value': i, 'displayValue': i})
        }
        else if (key == 'CHECK_PALLET_WEIGHT_FLAG') {
            valueStore.add({'value': '1', 'displayValue': '是'});
            valueStore.add({'value': '0', 'displayValue': '否'});
        } else if (key == 'CHECK_PALLET_WEIGHT_RATIO') {
            for (var i = 1; i <= 500; i++)
                valueStore.add({'value': i / 10, 'displayValue': i / 10})
        } else if (key == 'MANTY') {
            for (var i = 0; i <= 365; i++)
                valueStore.add({'value': i, 'displayValue': i})
        } else if (key == 'CHECK_TOTE_WEIGHT_RATIO') {
            for (var i = 0; i <= 20; i++)
                valueStore.add({'value': i, 'displayValue': i})
        } else if (key == 'TOTE_WEIGHT') {
            for (var i = 1; i <= 5; i++)
                valueStore.add({'value': i, 'displayValue': i})
        } else if (key == 'PALLET_WEIGHT') {
            for (var i = 5; i <= 30; i++)
                valueStore.add({'value': i, 'displayValue': i})
        } else if (key == 'PALLET_VOLUME') {
            for (var i = 1550000; i <= 2550000; i += 10000)
                valueStore.add({'value': i, 'displayValue': i})
        } else if (key == 'WORKBIN_VOLUME' || key == 'WORKBIN_TC9_VOLUME') {
            for (var i = 50000; i <= 80000; i += 500)
                valueStore.add({'value': i, 'displayValue': i})
        } else if (key == 'VOLUME_RATIO' || key == 'WORKBIN_VOLUME_RATIO') {
            for (var i = 0.1; i <= 1; i += 0.1)
                valueStore.add({'value': i, 'displayValue': i})
        } else if (key == 'INV_TYPE_A' || key == 'INV_TYPE_B') {
            for (var i = 5; i <= 100; i += 5)
                valueStore.add({'value': i, 'displayValue': i})
        } else if (key == 'DEFAULT_LOCATION_CHANGE_QTY') {
            for (var i = 100; i < 1000; i += 100)
                valueStore.add({'value': i, 'displayValue': i})
        } else if (key == 'TOTE_TC9_WEIGHT') {
            for (var i = 1; i <= 5; i++)
                valueStore.add({'value': i, 'displayValue': i})
        }
    };

    var configStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': 'CHECK_LAST_RETRIEVAL_DATE', 'displayValue': '出库批次检查'},
                {'value': 'PRODUCT_DATE_TOLERATE', 'displayValue': '整箱批次分配容忍值'},
                {'value': 'CHECK_PALLET_WEIGHT_FLAG', 'displayValue': '密集架是否称重检测'},
                {'value': 'CHECK_PALLET_WEIGHT_RATIO', 'displayValue': '密集架误差比率'},
                {'value': 'CHECK_TOTE_WEIGHT_RATIO', 'displayValue': '周转箱误差比率'},
                {'value': 'KEY_PALLET_CARTON_QTY', 'displayValue': '托盘平均装箱子数量'},
                {'value': 'KEY_AUTO_ZONE_STORE_RATIO', 'displayValue': '密集架空托盘利用率'},
                {'value': 'MANTY', 'displayValue': '电商大订单区分'},
                {'value': 'TOTE_WEIGHT', 'displayValue': '周转箱重量'},
                {'value': 'TOTE_TC9_WEIGHT', 'displayValue': 'TC9周转箱重量'},
                {'value': 'PALLET_WEIGHT', 'displayValue': '托盘重量'},
                {'value': 'PALLET_VOLUME', 'displayValue': '托盘装载体积'},
                {'value': 'WORKBIN_VOLUME', 'displayValue': '周转箱装载体积'},
                {'value': 'VOLUME_RATIO', 'displayValue': '托盘装载体积误差率'},
                {'value': 'WORKBIN_VOLUME_RATIO', 'displayValue': '周转箱装载体积误差率'},
                {'value': 'WORKBIN_TC9_VOLUME', 'displayValue': 'TC9周转箱装载体积'},
                {'value': 'INV_TYPE_A', 'displayValue': '库存A类占比'},
                {'value': 'INV_TYPE_B', 'displayValue': '库存B类占比'},
                {'value': 'DEFAULT_LOCATION_CHANGE_QTY', 'displayValue': '存储位转拣选位阈值'},
                {'value':'WMS_NOTICE','displayValue':'WMS系统通知'}
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

    var valueStore = Ext.create("Ext.data.JsonStore", {
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

    this.com_config = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '配置项',
        name: 'key',
        emptyText: '请选择',
        store: configStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_value = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '值',
        name: 'value',
        emptyText: '请选择',
        store: valueStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

};