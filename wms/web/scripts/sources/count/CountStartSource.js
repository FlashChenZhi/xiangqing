var CountStartSource = function () {

    var wareHouseId = null;

    this.setttingWareHouseId = function (selectValue) {
        wareHouseId = selectValue;
    };

    this.returnSkuTypeMap = function () {
        var map = new Map();
        map.put('1', '鼠咬');
        map.put('2', '客退');
        map.put('3', '过保');
        map.put('4', '退厂');
        map.put('5', '再销售');
        return map;
    };

    this.countingTypeMap = function () {
        var map = new Map();
        map.put('1', '自动库存储区');
        map.put('2', '横梁架');
        map.put('3', '密集DPS');
        map.put('4', '流利DPS');
        map.put('5', '流利架');
        map.put('6', '暂存区');
        return map;
    };

    this.countingDistMap = function () {
        var map = new Map();
        map.put('1', '明盘');
        map.put('2', '盲盘');
        return map;
    };

    this.statusMap = function () {
        var map = new Map();
        map.put('0', '正在初始化');
        map.put('1', '可作业');
        map.put('2', '作业中');
        map.put('3', '等待差异确认');
        map.put('4', '已结束');
        map.put('9', '已取消');
        return map;
    };


    this.qaStatusMap = function () {
        var map = new Map();
        map.put('1', '待质检');
        map.put('2', '合格');
        map.put('3', '不合格');
        return map;
    };

    this.yesOrNoMap = function () {
        var map = new Map();
        map.put(true, '是');
        map.put(false, '否');
        return map;
    };

    this.logicZoneStore = Ext.create("Ext.data.JsonStore", {
        fields: ['value', 'displayValue'],
        scope: this,
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = {'wareHouseId': wareHouseId};
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/countingStart/getLogizZones",
            actionMethods: {read: "POST"},
            reader: {
                type: "json",
                rootProperty: "res"
            },
            listeners: {
                exception: function (thiz, response) {
                    extStoreLoadFail(thiz, response);
                }
            }
        }
    });

    this.countingDistStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '明盘'},
                {'value': '2', 'displayValue': '盲盘'}
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

    this.countEmptyLocFlagStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': true, 'displayValue': '盘空货位'},
                {'value': false, 'displayValue': '不盘空货位'}
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

    this.countTempAreaFlagStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': true, 'displayValue': '盘暂存区'},
                {'value': false, 'displayValue': '不盘暂存区'}
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

    this.countTypeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '明盘'},
                {'value': '2', 'displayValue': '暗盘'}
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

    this.receiveFlagStore = Ext.create("Ext.data.JsonStore", {
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

    this.finishFlagStore = Ext.create("Ext.data.JsonStore", {
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

    this.existExceptionFlagStore = Ext.create("Ext.data.JsonStore", {
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

    this.emptyLocationFlagStore = Ext.create("Ext.data.JsonStore", {
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

    this.exceptionRecvingFlagStore = Ext.create("Ext.data.JsonStore", {
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

    this.chageSkuFlagStore = Ext.create("Ext.data.JsonStore", {
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


    this.com_logicZoneCode = Ext.create("Ext.form.ComboBox", {
        fieldLabel: '作业区',
        name: 'logicZoneCode',
        emptyText: '请选择',
        store: this.logicZoneStore,
        queryMode: 'local',
        editable: false,
        triggerAction: "all",
        displayField: 'displayValue',
        valueField: 'value',
        multiSelect: true,
        listConfig: {
            itemTpl: '<tpl for="."><div class="x-combo-list-item"><span><input type="checkbox"  value="{[values.id]}" />{displayValue}</span></div></tpl>',
            onItemSelect: function (record) {
                var node = this.getNode(record);
                if (node) {
                    Ext.fly(node).addCls(this.selectedItemCls);
                    var checkboxs = node.getElementsByTagName("input");
                    if (checkboxs != null) {
                        var checkbox = checkboxs[0];
                        checkbox.checked = true;
                    }
                }

            },
            listeners: {
                itemclick: function (view, record, item, index, e, eOpts) {
                    var isSelected = view.isSelected(item);
                    var checkboxs = item.getElementsByTagName("input");
                    if (checkboxs != null) {
                        var checkbox = checkboxs[0];
                        if (!isSelected) {
                            checkbox.checked = true;
                        } else {
                            checkbox.checked = false;
                        }
                    }
                }
            }
        }
    });

    this.com_countingDist = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '盘库区分',
        name: 'countingDist',
        store: this.countingDistStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_countType = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '盘库类型',
        name: 'countType',
        emptyText: '请选择',
        store: this.countTypeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_countEmptyLocFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否盘空货位',
        name: 'countEmptyLocFlag',
        emptyText: '请选择',
        store: this.countEmptyLocFlagStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });


    this.com_countTempAreaFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否盘暂存区',
        name: 'countTempAreaFlag',
        emptyText: '请选择',
        store: this.countTempAreaFlagStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });


    this.com_receiveFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否已领取',
        name: 'receiveFlag',
        emptyText: '请选择',
        store: this.receiveFlagStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_finishFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否已完成',
        name: 'finishFlag',
        emptyText: '请选择',
        store: this.finishFlagStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_existExceptionFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否盘库差异',
        name: 'existExceptionFlag',
        emptyText: '请选择',
        store: this.existExceptionFlagStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });


    this.com_exceptionRecvingFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否零库存盘盈',
        name: 'exceptionRecvingFlag',
        emptyText: '请选择',
        store: this.exceptionRecvingFlagStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_emptyLocationFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否空货位',
        name: 'emptyLocationFlag',
        emptyText: '请选择',
        store: this.emptyLocationFlagStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_chageSkuFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否SKU变更',
        name: 'chageSkuFlag',
        emptyText: '请选择',
        store: this.chageSkuFlagStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.txt_jobNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '盘库单号',
        name: 'jobNo'
    });

    this.txt_jobName = Ext.create("Ext.form.field.Text", {
        fieldLabel: '盘库名称',
        name: 'jobName'
    });

    this.txt_reJobName = Ext.create("Ext.form.field.Text", {
        fieldLabel: '复盘名称',
        name: 'reJobName'
    });

};
