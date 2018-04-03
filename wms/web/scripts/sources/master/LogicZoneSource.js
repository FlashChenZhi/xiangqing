/**
 * Created by Van on 2015/8/24.
 */
var LogicZoneSource = function () {

    this.txt_logicZoneCode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '逻辑区代码',
        name: 'logicZoneCode'
    });

    this.txt_logicZoneName = Ext.create("Ext.form.field.Text", {
        fieldLabel: '逻辑区名称',
        name: 'logicZoneName'
    });

    var eomTypeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '采购单位'},
                {'value': '3', 'displayValue': '发货单位'},
                {'value': '4', 'displayValue': '基本单位'}
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

    var returnSkuTypeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '鼠咬'},
                {'value': '2', 'displayValue': '客退'},
                {'value': '3', 'displayValue': '过保'},
                {'value': '4', 'displayValue': '退厂'}
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

    var zoneTypeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '拣选区'},
                {'value': '2', 'displayValue': '存储区'},
                {'value': '3', 'displayValue': '存拣区'}
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
                {'value': '1', 'displayValue': '密集货架区'},
                {'value': '2', 'displayValue': '平库横梁式'},
                {'value': '3', 'displayValue': '平库隔板式'},
                {'value': '4', 'displayValue': '平库流利式dps'},
                {'value': '5', 'displayValue': '平库流利式'},
                {'value': '9', 'displayValue': '退品整理格斗货架区'}
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

    var allocateTypeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '作业区有限'},
                {'value': '2', 'displayValue': '作业区平均'},
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

    var pickingTypeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': 'DPS拣选'},
                {'value': '2', 'displayValue': '纸质单拣选'},
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

    var forcePickingStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': true, 'displayValue': '是'},
                {'value': false, 'displayValue': '否'},
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

    this.com_autoCreatePickingJob = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否自动创建作业',
        name: 'autoCreatePickingJob',
        emptyText: '请选择',
        store: forcePickingStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_allocateType = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '分配方式',
        name: 'allocateType',
        emptyText: '请选择',
        store: allocateTypeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_pickingType = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '拣选方式',
        name: 'pickingType',
        emptyText: '请选择',
        store: pickingTypeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_forcePicking = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否强制拣选',
        name: 'forcePicking',
        emptyText: '请选择',
        store: forcePickingStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_forcePacking = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否强制打包',
        name: 'forcePacking',
        emptyText: '请选择',
        store: forcePickingStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });


    this.com_mixStorage = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否混储',
        name: 'mixFlag',
        emptyText: '请选择',
        store: forcePickingStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_eomType = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '单位类型',
        name: 'eomType',
        emptyText: '请选择',
        store: eomTypeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_zoneType = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '区域类型',
        name: 'zoneType',
        emptyText: '请选择',
        store: zoneTypeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_type = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '类型',
        name: 'type',
        emptyText: '请选择',
        store: typeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });


    this.com_return_sku_type = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '退货类型',
        name: 'returnSkuType',
        emptyText: '',
        store: returnSkuTypeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.txt_seq = Ext.create("Ext.form.field.Text", {
        fieldLabel: '序列',
        name: 'seq'
    });

    this.txt_maxAllocateQty = Ext.create("Ext.form.field.Number", {
        fieldLabel: '最大分配数',
        name: 'maxAllocateQty'
    });


};