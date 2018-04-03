var LocationSource = function () {


    var alsleStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '巷道一'},
                {'value': '0', 'displayValue': '巷道二'}
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

    var pickerZoneStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '拣选区一'},
                {'value': '0', 'displayValue': '拣选区二'}
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

    var storeZoneStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '存储区一'},
                {'value': '0', 'displayValue': '存储区二'}
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

    var ZoneStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '区域一'},
                {'value': '0', 'displayValue': '区域二'}
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

    var isSystemStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '是'},
                {'value': '0', 'displayValue': '否'}
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
                {'value': '1', 'displayValue': '正常'},
                {'value': '0', 'displayValue': '冻结'}
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

    var zoneTypeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': "1", 'displayValue': '密集架'},
                {'value': "2", 'displayValue': '平库横梁架'}
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

    this.com_fullFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否实货位',
        name: 'fullFlag',
        emptyText: '请选择',
        store: boolStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_frozenFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否冻结',
        name: 'frozenFlag',
        emptyText: '请选择',
        store: boolStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_abnormalFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否故障',
        name: 'abnormalFlag',
        emptyText: '请选择',
        store: boolStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_restrictedFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否限制使用',
        name: 'restrictedFlag',
        emptyText: '请选择',
        store: boolStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_allowPutaway = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否允许上架',
        name: 'allowPutaway',
        emptyText: '请选择',
        store: boolStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_allowPicking = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否允许拣选',
        name: 'allowPicking',
        emptyText: '请选择',
        store: boolStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
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

    this.com_isSystemZone = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否系统货位',
        name: 'isSystemStore',
        emptyText: '请选择',
        store: isSystemStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });


    this.com_Zone = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '区域',
        name: 'ZoneStore',
        emptyText: '请选择',
        store: ZoneStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });


    this.com_pickingZone = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '拣选区',
        name: 'pickerZone',
        emptyText: '请选择',
        store: pickerZoneStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });


    this.com_StoreZone = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '存储区',
        name: 'storeZone',
        emptyText: '请选择',
        store: storeZoneStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_alsle = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '巷道',
        name: 'alsle',
        emptyText: '请选择',
        store: alsleStore,
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

    this.txt_LocationNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '货位号',
        name: 'locationNo'
    });

    this.txt_bankNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '排',
        name: 'bankNo'
    });

    this.txt_bayNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '列',
        name: 'bayNo'
    });

    this.txt_levelNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '层',
        name: 'levelNo'
    });

}