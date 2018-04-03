var CommonSource = function (data) {

    var skuCode = "";

    this.init = function (code) {
        skuCode = code;
        this.skuEomStore.load();
    };

    this.skuEomStore = Ext.create("Ext.data.JsonStore", {
        fields: ['value', 'displayValue'],
        scope: this,
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = {'skuCode': skuCode};
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/common/getSkuEom",
            actionMethods: {read: "POST"},
            reader: {
                type: "json",
                rootProperty: "res"
            },
            listeners: {
                exception: function (thiz, response) {
                    var result = Ext.util.JSON.decode(response.responseText);
                    Ext.Msg.alert('失败', result.msg);
                    console.log(result);
                }
            }
        }
    });


    this.com_skuEom = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '单位',
        name: 'eom',
        emptyText: '请选择',
        store: this.skuEomStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    var laserPrinterStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '打印机1'},
                {'value': '3', 'displayValue': '打印机2'}
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

    var carrierStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '顺丰'},
                {'value': '3', 'displayValue': '申通'}
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

    var jobTypeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '01', 'displayValue': '入库'},
                {'value': '03', 'displayValue': '出库'},
                {'value': '05', 'displayValue': '冻结'},
                {'value': '06', 'displayValue': '解除冻结'}
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


    var warningStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '预警'},
                {'value': '2', 'displayValue': '过期'}
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

    var zoneStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '密集货架区'},
                {'value': '2', 'displayValue': '平库区'}
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

    var pickingZoneStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '密集货架区'},
                {'value': '2', 'displayValue': '1楼平库'},
                {'value': '3', 'displayValue': '大促区'}
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

    var storageZoneStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '密集货架区'},
                {'value': '2', 'displayValue': '1楼平库'}
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

    var storeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '金沙江路'},
                {'value': '2', 'displayValue': '盛夏路'}
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

    var blockFlagStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': true, 'displayValue': '冻结'},
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

    var statusStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '0', 'displayValue': '正常'},
                {'value': '1', 'displayValue': '冻结'}
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


    var orderStatus = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '0', 'displayValue': '未开始'},
                {'value': '1', 'displayValue': '已开始'},
                {'value': '9', 'displayValue': '已取消'}

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


    var skuTypeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': 'A', 'displayValue': 'A'},
                {'value': 'B', 'displayValue': 'B'},
                {'value': 'C', 'displayValue': 'C'},
                {'value': 'D', 'displayValue': 'D'},
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

    var helpStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': 1, 'displayValue': '无法收货'},
                {'value': 2, 'displayValue': '无法上架'},
                {'value': 3, 'displayValue': '波次不分配库存'},
                {'value': 4, 'displayValue': '电商面单无法绑定周转箱'},
                {'value': 5, 'displayValue': '电商订单无法分配'},
                {'value': 6, 'displayValue': '电商订单无法打印面单'},
                {'value': 7, 'displayValue': '电商订单无法预分配'},
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

    var slideStatusStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '0', 'displayValue': '正常'},
                {'value': '1', 'displayValue': '冻结'},
                {'value': '2', 'displayValue': '被占用'}
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

    var abnormalFlagStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': true, 'displayValue': '故障'},
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

    var flatOnly = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': true, 'displayValue': '横梁架有库存'},
                {'value': false, 'displayValue': '横梁架无库存'}
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

    var restrictedFlagStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': true, 'displayValue': '限制使用'},
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

    var systemFlagStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': true, 'displayValue': '系统'},
                {'value': false, 'displayValue': '非系统'}
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

    var countingFlagStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': true, 'displayValue': '盘库中'},
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

    var bindSkuFlagStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': true, 'displayValue': '绑定'},
                {'value': false, 'displayValue': '未绑定'}
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

    var yesOrNoStore = Ext.create("Ext.data.JsonStore", {
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

    var fullFlagStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': true, 'displayValue': '实货位'},
                {'value': false, 'displayValue': '空货位'}
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


    var platFormTypeStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        data: {
            'res': [
                {'value': '1', 'displayValue': '密集货架区域月台'},
                {'value': '2', 'displayValue': '平库月台'},
                {'value': '3', 'displayValue': '退货月台'}
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

    this.com_orderstatus = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '状态',
        name: 'status',
        emptyText: '请选择',
        store: orderStatus,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });


    this.com_flatOnly = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '横梁架库存',
        name: 'allFlag',
        emptyText: '请选择',
        store: flatOnly,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_promotionMatchType = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否匹配数量',
        name: 'matchType',
        emptyText: '请选择',
        store: yesOrNoStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });


    this.com_locationFull = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否实货位',
        name: 'fullFlag',
        emptyText: '请选择',
        store: fullFlagStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });


    this.com_slideStatus = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '滑道状态',
        name: 'status',
        emptyText: '请选择',
        store: slideStatusStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_skuCode = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '商品代码',
        name: 'skuCode',
        emptyText: '请选择',
        store: data.skuCodeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });


    this.com_skuAbcType = Ext.create('Ext.form.ComboBox', {
        fieldLabel: 'ABC类型',
        name: 'skuType',
        emptyText: '请选择',
        store: skuTypeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });


    this.com_errorCode = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '错误信息',
        name: 'errorCode',
        emptyText: '请选择',
        store: helpStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });


    this.com_users = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '用户',
        name: 'userCode',
        emptyText: '请选择',
        store: data.usersStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });


    this.com_roleId = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '角色',
        name: 'roleId',
        emptyText: '请选择',
        store: data.roleIdsStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_firstMenuId = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '一级菜单',
        name: 'firstMenuId',
        emptyText: '请选择',
        store: data.firstMenuIdsStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });


    this.com_pickingZone = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '拣选区',
        name: 'pickingZoneId',
        emptyText: '请选择',
        store: pickingZoneStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });

    this.com_storageZone = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '存储区',
        name: 'storageZoneId',
        emptyText: '请选择',
        store: storageZoneStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });
    this.com_platFormType = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '存储区',
        name: 'platFormType',
        emptyText: '请选择',
        store: platFormTypeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });


    this.com_blockFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否冻结',
        name: 'blockFlag',
        emptyText: '请选择',
        store: blockFlagStore,
        queryMode: 'local',
        displayField: 'displayValue',
        editable: false,
        triggerAction: "all",
        valueField: 'value'
    });



    this.com_jobType = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '作业类型',
        name: 'jobType',
        emptyText: '请选择',
        store: jobTypeStore,
        queryMode: 'local',
        displayField: 'displayValue',
        editable: false,
        triggerAction: "all",
        valueField: 'value'
    });



    this.com_abnormalFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否故障',
        name: 'abnormalFlag',
        emptyText: '请选择',
        store: abnormalFlagStore,
        queryMode: 'local',
        displayField: 'displayValue',
        editable: false,
        triggerAction: "all",
        valueField: 'value'
    });

    this.com_restrictedFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否限制使用',
        name: 'restrictedFlag',
        emptyText: '请选择',
        store: restrictedFlagStore,
        queryMode: 'local',
        displayField: 'displayValue',
        editable: false,
        triggerAction: "all",
        valueField: 'value'
    });

    this.com_systemFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否系统',
        name: 'systemFlag',
        emptyText: '请选择',
        store: systemFlagStore,
        queryMode: 'local',
        displayField: 'displayValue',
        editable: false,
        triggerAction: "all",
        valueField: 'value'
    });

    this.com_bindSkuFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否绑定SKU',
        name: 'bindSkuFlag',
        emptyText: '请选择',
        store: bindSkuFlagStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });


    this.com_countingFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '是否盘库中',
        name: 'countingFlag',
        emptyText: '请选择',
        store: countingFlagStore,
        queryMode: 'local',
        displayField: 'displayValue',
        editable: false,
        triggerAction: "all",
        valueField: 'value'
    });

    this.com_warningTypeFlag = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '类型',
        name: 'type',
        emptyText: '请选择',
        store: warningStore,
        queryMode: 'local',
        displayField: 'displayValue',
        editable: false,
        triggerAction: "all",
        valueField: 'value'
    });


    this.txt_ownerCode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '货主代码',
        name: 'ownerCode'
    });

    this.txt_providerCode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '供应商代码',
        name: 'providerCode'
    });

    this.txt_carrierCode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '承运商代码',
        name: 'carrierCode'
    });

    this.txt_zoneCode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '区域代码',
        name: 'zoneCode'
    });

    this.txt_platFormCode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '月台代码',
        name: 'platFormCode'
    });

    this.txt_pickingBatchNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '波次号',
        name: 'lotNo'
    });

    this.txt_truckingOrderNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '派车单号',
        name: 'truckingOrderNo'
    });

    this.txt_LocationNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '货位号',
        name: 'locationNo'
    });

    this.txt_bankNo = Ext.create("Ext.form.field.Number", {
        fieldLabel: '排(bank)',
        name: 'bankNo'
    });

    this.txt_position = Ext.create("Ext.form.field.Number", {
        fieldLabel: '区域',
        name: 'position'
    });


    this.txt_bayNo = Ext.create("Ext.form.field.Number", {
        fieldLabel: '列(bay)',
        name: 'bayNo'
    });

    this.txt_levelNo = Ext.create("Ext.form.field.Number", {
        fieldLabel: '层(level)',
        name: 'levelNo'
    });

    this.txt_skuBarCode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '商品条码',
        name: 'basicBarcode'
    });

    this.txt_skuBoxBarCode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '商品箱码',
        name: 'skuBoxBarCode'
    });

    this.txt_basicBarcode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '基本条码',
        name: 'basicBarcode'
    });

    this.txt_purcharseBarcode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '采购条码',
        name: 'purcharseBarcode'
    });


    this.txt_returnSkuCnorDocNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '退货单据号',
        name: 'returnSkuCnorDocNo'
    });


    this.txt_skuCode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '商品代码',
        name: 'skuCode'
    });

    this.txt_skuName = Ext.create("Ext.form.field.Text", {
        fieldLabel: '商品名称',
        name: 'skuName'
    });

    this.txt_eom = Ext.create("Ext.form.field.Text", {
        fieldLabel: '单位',
        name: 'eom'
    });

    this.txt_receivingQty = Ext.create("Ext.form.field.Text", {
        fieldLabel: '收货数量',
        name: 'receivingQty'
    });

    this.date_lot = Ext.create("Ext.form.field.Date", {
        fieldLabel: '批次',
        name: 'lot',
        emptyText: "请选择日期",
        selectOnFocus: true,
        format: "Y-m-d",
        altFormats: "Y/m/d|Ymd"
    });

    this.txt_bindingContainerBarCode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '绑定容器',
        name: 'bindingContainerBarCode'
    });

    this.txt_bindingQty = Ext.create("Ext.form.field.Number", {
        fieldLabel: '绑定数量',
        decimalPrecision: 3,
        name: 'bindingQty'
    });

    this.txt_allocateQty = Ext.create("Ext.form.field.Number", {
        fieldLabel: '分配数',
        name: 'allocateQty',
        minValue: 0
    });

    this.txt_waveNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '波次号',
        name: 'skuName'
    });

    this.txt_receivingPlanOrderNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '收货单号',
        name: 'orderNo'
    });

    this.date_productDate = Ext.create("Ext.form.field.Date", {
        fieldLabel: '生产日期',
        name: 'productDate',
        emptyText: "生产日期",
        selectOnFocus: true,
        format: "Y-m-d",
        altFormats: "Y/m/d|Ymd"
    });

    this.txt_memo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '备注',
        name: 'memo'
    });

    this.txt_weight = Ext.create("Ext.form.field.Number", {
        fieldLabel: '重量(kg)',
        decimalPrecision: 3,
        name: 'weight'
    });

    this.txt_qty = Ext.create("Ext.form.field.Number", {
        fieldLabel: '数量',
        name: 'qty',
        minValue: 0
    });

    this.txt_eom = Ext.create("Ext.form.field.Text", {
        fieldLabel: '单位',
        name: 'eom'
    });

    this.txt_batchCount = Ext.create("Ext.form.field.Number", {
        fieldLabel: '批次数',
        name: 'batchCount',
        minValue: 0
    });

    this.txt_receivingOrderNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '收货单号',
        name: 'receivingOrderNo'
    });

    this.txt_batchNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '批次',
        name: 'batchNo'
    });

    this.txt_sendCarOrderNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '派车单号',
        name: 'sendCarOrderNo'
    });

    this.txt_cnorDocNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: 'SAP单据号',
        name: 'cnorDocNo'
    });

    this.txt_retrievalOrderNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '出库单号',
        name: 'orderNo'
    });

    this.txt_address = Ext.create("Ext.form.field.Text", {
        fieldLabel: '地址',
        name: 'address'
    });

    this.txt_welcome = Ext.create("Ext.form.field.Text", {
        fieldLabel: '欢迎语',
        name: 'welcome'
    });


    this.txt_locationNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '货位号',
        name: 'locationNo'
    });

    this.txt_containerBarcode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '容器号',
        name: 'containerBarcode'
    });

    this.txt_whCode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '仓库',
        name: 'whCode'
    });

    this.txt_dpsAlias = Ext.create("Ext.form.field.Text", {
        fieldLabel: 'DPS地址',
        name: 'dpsAlias'
    });

    this.txt_toContainerBarcode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '目标容器',
        name: 'toContainerBarcode'
    });

    this.txt_purcharseBarcodeOrSkuCode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '箱码/代码',
        name: 'purcharseBarcodeOrSkuCode'
    });

    this.txt_barcodes = Ext.create("Ext.form.field.Text", {
        fieldLabel: '条码',
        name: 'barcodes'
    });

    this.date_beginDate = Ext.create("Ext.form.field.Date", {
        fieldLabel: '开始时间',
        name: 'beginDate',
        emptyText: "请选择日期",
        selectOnFocus: true,
        format: "Y-m-d",
        altFormats: "Y/m/d|Ymd"
    });

    this.date_endDate = Ext.create("Ext.form.field.Date", {
        fieldLabel: '结束时间',
        name: 'endDate',
        emptyText: "请选择日期",
        selectOnFocus: true,
        format: "Y-m-d",
        altFormats: "Y/m/d|Ymd"
    });


    this.txt_fromContainerBarcode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '源容器号',
        name: 'fromContainerBarcode'
    });


    this.txt_recoveryQty = Ext.create("Ext.form.field.Number", {
        fieldLabel: '回收数量',
        name: 'recoveryQty',
        minValue: 1
    });

    this.txt_joinQty = Ext.create("Ext.form.field.Number", {
        fieldLabel: '门店交接数量',
        name: 'joinQty',
        minValue: 1
    });

    this.txt_userCode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '工号',
        name: 'userCode'
    });

    this.txt_logisticsBarcode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '物流码',
        name: 'logisticsBarcode'
    });

    this.txt_slideNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '滑道',
        name: 'slideNo'
    });

    this.txt_checkUseId = Ext.create("Ext.form.field.Text", {
        fieldLabel: '复核人',
        name: 'checkUseId'
    });

    this.txt_expressNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '运单号',
        name: 'expressNo'
    });

    this.txt_shippingBarcode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '发货条码',
        name: 'shippingBarcode'
    });

    this.txt_startBatchNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '开始波次号',
        name: 'startBatchNo'
    });

    this.txt_endBatchNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '结束波次号',
        name: 'endBatchNo'
    });

    this.txt_carNo = Ext.create("Ext.form.field.Text", {
        fieldLabel: '车牌号',
        name: 'carNo'
    });

    this.txt_provice = Ext.create("Ext.form.field.Text", {
        fieldLabel: '省份',
        name: 'provice'
    });

    this.txt_skuCode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '商品代码',
        name: 'skuCode'
    });


    this.txt_promotionName = Ext.create("Ext.form.field.Text", {
        fieldLabel: '活动名称',
        name: 'name'
    });

    this.txt_helpCode = Ext.create("Ext.form.field.Text", {
        fieldLabel: '数据',
        name: 'helpCode'
    });

    this.txt_daily = Ext.create("Ext.form.field.Text", {
        fieldLabel: '日期',
        name: 'daily'
    });

    this.date_allocateStopDate = Ext.create("Ext.form.field.Date", {
        fieldLabel: '截止时间',
        name: 'allocateStopDate',
        emptyText: "截止时间",
        selectOnFocus: true,
        format: "Y-m-d H:i:s",
    });

    this.ftpServer = "ftp://172.24.1.2/";

};